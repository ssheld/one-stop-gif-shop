package com.ssheld.onestopgifshop.dao;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.ssheld.onestopgifshop.model.Gif;
import com.ssheld.onestopgifshop.utils.ServiceUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Author: Stephen Sheldon
 **/
@Repository
@PropertySource("classpath:db/s3.properties")
public class GifDaoImpl implements GifDao {

    private static Logger logger = LoggerFactory.getLogger(GifDaoImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private Environment env;

    @Autowired
    private AmazonS3 amazonS3;

    @Value("app.awsServices.bucketName")
    private String bucketName;

    public List<Gif> findAll() {
        // Open session
        Session session = sessionFactory.openSession();

        // Create CriteriaBuilder
        CriteriaBuilder builder = session.getCriteriaBuilder();

        // Create CriteriaQuery
        CriteriaQuery<Gif> criteria = builder.createQuery(Gif.class);

        // Specify criteria root
        criteria.from(Gif.class);

        // Execute query
        List<Gif> gifs = session.createQuery(criteria).getResultList();

        // Close session
        session.close();

        return gifs;
    }

    public Gif findById(Long id) throws DaoException {
        // Open session
        Session session = sessionFactory.openSession();
        Gif gif = session.get(Gif.class, id);

        gif.setBytes(findByIdInS3(String.valueOf(gif.getId())));

        // Close session
        session.close();

        return gif;
    }

    public void save(Gif gif) throws DaoException {
        // Open session
        Session session = sessionFactory.openSession();

        session.beginTransaction();
        // Save all keywords
        if (gif.getGifMetaData() != null) {
            for (int i = 0; i < gif.getGifMetaData().getKeywordList().size(); i++) {
                session.saveOrUpdate(gif.getGifMetaData().getKeywordList().get(i));
            }
            session.saveOrUpdate(gif.getGifMetaData());
        }
        session.saveOrUpdate(gif);

        saveToS3(gif.getFile(), String.valueOf(gif.getId()));

        session.getTransaction().commit();

        // Close session
        session.close();
    }

    public void delete(Gif gif) throws DaoException {
        // Open session
        Session session = sessionFactory.openSession();

        session.beginTransaction();
        session.delete(gif);
        session.getTransaction().commit();

        deleteFromS3(String.valueOf(gif.getId()));

        // Close session
        session.close();
    }

    private void saveToS3(MultipartFile multipartFile, String gifId) throws DaoException {
        bucketName = env.getProperty("app.awsServices.bucketName");

        if (multipartFile.isEmpty()) {
            return;
        }

        try {
            File file = ServiceUtils.convertMultiPartToFile(multipartFile);
            amazonS3.putObject(bucketName, gifId, file);
        } catch (AmazonServiceException e) {
            logger.error("Error uploading file with ID: " + gifId + " to Amazon S3.");
            throw new DaoException("Action: Uploading Gif to AWS S3.", "Reason: " + e.getMessage());
        } catch (IOException e) {
            logger.error("Error converting multipart file to file while uploading gif with ID: " + gifId);
            throw new DaoException(":Action: Uploading Gif to AWS S3.", "Reason: " + e.getMessage());
        }
    }

    private void deleteFromS3(String gifId) throws DaoException {
        bucketName = env.getProperty("app.awsServices.bucketName");

        try {
            amazonS3.deleteObject(bucketName, gifId);
        } catch (AmazonServiceException e) {
            logger.error("Error deleting file with ID: " + gifId + " from Amazon S3.");
            throw new DaoException("Action: Delete Gif from AWS S3.", "Reason: " + e.getMessage());
        }
    }

    private byte[] findByIdInS3(String gifId) throws DaoException {
        bucketName = env.getProperty("app.awsServices.bucketName");

        byte[] fileBytes;

        try {
            S3Object o = amazonS3.getObject(bucketName, gifId);
            fileBytes = IOUtils.toByteArray(o.getObjectContent());
        } catch (IOException e) {
            logger.error("Error retrieving GIF from S3 with ID: " + gifId);
            throw new DaoException("Action: Find Gif in S3.", "Reason: " + e.getMessage());
        }

        return fileBytes;
    }
}
