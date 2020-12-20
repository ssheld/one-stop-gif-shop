package com.ssheld.onestopgifshop.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Author: Stephen Sheldon
 **/
public class ServiceUtils {

    /**
     * Convert a Multipart File to a File. Used for AWS S3 uploads
     * @param file          The Multipart file to be converted.
     * @return              The converted multipart file in File format.
     * @throws IOException
     */
    public static File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
}
