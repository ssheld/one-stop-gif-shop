package com.ssheld.onestopgifshop.gifrekognition;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.AmazonRekognitionException;
import com.amazonaws.services.rekognition.model.DetectLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectLabelsResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.Label;
import com.ssheld.onestopgifshop.model.GifMetadata;
import com.ssheld.onestopgifshop.model.Keyword;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Stephen Sheldon
 **/
public class GifMetadataExtractor {

    private ImageFrame[] imageFrames;

    private byte[] imageBytes;

    private List<String> imageLabels;

    private List<Keyword> keywordList;

    MultipartFile file;

    private static Logger logger = LoggerFactory.getLogger(GifMetadataExtractor.class);

    AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.defaultClient();

    public GifMetadataExtractor(MultipartFile file) {
        this.file = file;
        this.imageLabels = new ArrayList<>();
        this.keywordList = new ArrayList<>();
    }

    public GifMetadata generateGifMetadata() throws IOException {


        GifMetadata gifMetaData = new GifMetadata();

        imageBytes = file.getBytes();

        // Generate image frames
        generateImageFrames();

        List<Label> labelList = null;

        // Sample the first 10 frames
        for (int i = 0; i < imageFrames.length; i=i+8) {
            ImageFrame imageFrame = imageFrames[i];

            com.amazonaws.services.rekognition.model.Image image = new Image();

            // Get the buffered image from the frame
            BufferedImage bufferedImage = imageFrame.getImage();

            // Get byte array in PNG format
            byte[] imageBytes = toByteArray(bufferedImage, "png");

            // Convert byte array to ByteBuffer
            ByteBuffer byteBuffer = ByteBuffer.wrap(imageBytes);

            // Set the image bytes
            image.setBytes(byteBuffer);

            // Detect labels
            DetectLabelsRequest labelsRequest = new DetectLabelsRequest()
                    .withImage(image)
                    .withMaxLabels(10).withMinConfidence(75F);

            try {
                DetectLabelsResult labelResult = rekognitionClient.detectLabels(labelsRequest);
                labelList = labelResult.getLabels();

            } catch (AmazonRekognitionException amazonRekognitionException) {
                logger.error("AMAZONREKOGNITIONEXCEPTION in GifMetadata.java");
            }

            if (labelList != null) {
                for (Label label : labelList) {
                    if (!imageLabels.contains(label.getName())) {
                        imageLabels.add(label.getName());
                        Keyword keyword = new Keyword();
                        keyword.setKeyword(label.getName());
                        keywordList.add(keyword);
                    }
                }
            }
        }
        gifMetaData.setKeywordList(keywordList);

        return gifMetaData;
    }

    private void generateImageFrames() {

        ByteArrayInputStream stream = new ByteArrayInputStream(imageBytes);

        ArrayList<ImageFrame> frames = new ArrayList<>(2);

        ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
        try {
            reader.setInput(ImageIO.createImageInputStream(stream));
        } catch (IOException e) {
            logger.error("Could not set input for image input stream");
        }

        int lastx = 0;
        int lasty = 0;

        int width = -1;
        int height = -1;
        IIOMetadata metadata = null;
        try {
            metadata = reader.getStreamMetadata();
        } catch (IOException e) {
            logger.error("Could not set IIOMetadata");
        }

        Color backgroundColor = null;

        if(metadata != null) {
            IIOMetadataNode globalRoot = (IIOMetadataNode) metadata.getAsTree(metadata.getNativeMetadataFormatName());

            NodeList globalColorTable = globalRoot.getElementsByTagName("GlobalColorTable");
            NodeList globalScreeDescriptor = globalRoot.getElementsByTagName("LogicalScreenDescriptor");

            if (globalScreeDescriptor != null && globalScreeDescriptor.getLength() > 0){
                IIOMetadataNode screenDescriptor = (IIOMetadataNode) globalScreeDescriptor.item(0);

                if (screenDescriptor != null){
                    width = Integer.parseInt(screenDescriptor.getAttribute("logicalScreenWidth"));
                    height = Integer.parseInt(screenDescriptor.getAttribute("logicalScreenHeight"));
                }
            }

            if (globalColorTable != null && globalColorTable.getLength() > 0){
                IIOMetadataNode colorTable = (IIOMetadataNode) globalColorTable.item(0);

                if (colorTable != null) {
                    String bgIndex = colorTable.getAttribute("backgroundColorIndex");

                    IIOMetadataNode colorEntry = (IIOMetadataNode) colorTable.getFirstChild();
                    while (colorEntry != null) {
                        if (colorEntry.getAttribute("index").equals(bgIndex)) {
                            int red = Integer.parseInt(colorEntry.getAttribute("red"));
                            int green = Integer.parseInt(colorEntry.getAttribute("green"));
                            int blue = Integer.parseInt(colorEntry.getAttribute("blue"));

                            backgroundColor = new Color(red, green, blue);
                            break;
                        }

                        colorEntry = (IIOMetadataNode) colorEntry.getNextSibling();
                    }
                }
            }
        }

        BufferedImage master = null;
        boolean hasBackround = false;

        for (int frameIndex = 0;; frameIndex++) {
            BufferedImage image;
            try{
                image = reader.read(frameIndex);
            }catch (IndexOutOfBoundsException io){
                break;
            }
            catch (IOException e) {
                break;
            }

            if (width == -1 || height == -1){
                width = image.getWidth();
                height = image.getHeight();
            }
            IIOMetadataNode root = null;
            try {
                root = (IIOMetadataNode) reader.getImageMetadata(frameIndex).getAsTree("javax_imageio_gif_image_1.0");
            } catch (IOException e) {
                // exception
            }
            IIOMetadataNode gce = (IIOMetadataNode) root.getElementsByTagName("GraphicControlExtension").item(0);
            NodeList children = root.getChildNodes();

            int delay = Integer.valueOf(gce.getAttribute("delayTime"));

            String disposal = gce.getAttribute("disposalMethod");

            if (master == null){
                master = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                master.createGraphics().setColor(backgroundColor);
                master.createGraphics().fillRect(0, 0, master.getWidth(), master.getHeight());

                hasBackround = image.getWidth() == width && image.getHeight() == height;

                master.createGraphics().drawImage(image, 0, 0, null);
            }else{
                int x = 0;
                int y = 0;

                for (int nodeIndex = 0; nodeIndex < children.getLength(); nodeIndex++){
                    Node nodeItem = children.item(nodeIndex);

                    if (nodeItem.getNodeName().equals("ImageDescriptor")){
                        NamedNodeMap map = nodeItem.getAttributes();

                        x = Integer.valueOf(map.getNamedItem("imageLeftPosition").getNodeValue());
                        y = Integer.valueOf(map.getNamedItem("imageTopPosition").getNodeValue());
                    }
                }

                if (disposal.equals("restoreToPrevious")){
                    BufferedImage from = null;
                    for (int i = frameIndex - 1; i >= 0; i--){
                        if (!frames.get(i).getDisposal().equals("restoreToPrevious") || frameIndex == 0){
                            from = frames.get(i).getImage();
                            break;
                        }
                    }

                    {
                        ColorModel model = from.getColorModel();
                        boolean alpha = from.isAlphaPremultiplied();
                        WritableRaster raster = from.copyData(null);
                        master = new BufferedImage(model, raster, alpha, null);
                    }
                }else if (disposal.equals("restoreToBackgroundColor") && backgroundColor != null){
                    if (!hasBackround || frameIndex > 1){
                        master.createGraphics().fillRect(lastx, lasty, frames.get(frameIndex - 1).getWidth(), frames.get(frameIndex - 1).getHeight());
                    }
                }
                master.createGraphics().drawImage(image, x, y, null);

                lastx = x;
                lasty = y;
            }

            {
                BufferedImage copy;

                {
                    ColorModel model = master.getColorModel();
                    boolean alpha = master.isAlphaPremultiplied();
                    WritableRaster raster = master.copyData(null);
                    copy = new BufferedImage(model, raster, alpha, null);
                }
                frames.add(new ImageFrame(copy, delay, disposal, image.getWidth(), image.getHeight()));
            }

            master.flush();
        }
        reader.dispose();

        imageFrames = frames.toArray(new ImageFrame[frames.size()]);
    }

    // convert BufferedImage to byte[]
    private byte[] toByteArray(BufferedImage bi, String format) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bi, format, baos);
        byte[] imageTobytes = baos.toByteArray();
        return imageTobytes;
    }
}
