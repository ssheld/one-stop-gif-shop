package com.ssheld.onestopgifshop.dao;

import com.ssheld.onestopgifshop.model.GifMetadata;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

/**
 * Author: Stephen Sheldon
 **/
@Repository
public class GifMetadataDaoImpl {
    @Autowired
    private SessionFactory sessionFactory;

    public List<GifMetadata> findAll() {
        // Open a session
        Session session = sessionFactory.openSession();

        // Create CriteriaBuilder
        CriteriaBuilder builder = session.getCriteriaBuilder();

        // Create CriteriaQuery
        CriteriaQuery<GifMetadata> criteria = builder.createQuery(GifMetadata.class);

        // Specify criteria root
        criteria.from(GifMetadata.class);

        // Execute query
        List<GifMetadata> gifMetadataList = session.createQuery(criteria).getResultList();

        // Close session
        session.close();

        return gifMetadataList;
    }

    public GifMetadata findById(Long id) {
        Session session = sessionFactory.openSession();
        GifMetadata gifMetadata = session.get(GifMetadata.class, id);
        Hibernate.initialize(gifMetadata.getKeywordList());
        session.close();
        return gifMetadata;
    }

    public void save(GifMetadata gifMetadata) {
        // Open a session
        Session session = sessionFactory.openSession();

        // Begin a transaction
        session.beginTransaction();

        // Save or update the gif metadata
        session.saveOrUpdate(gifMetadata);

        // Commit the transaction
        session.getTransaction().commit();

        // Close the session
        session.close();
    }

    public void delete(GifMetadata gifmetadata) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.delete(gifmetadata);
        session.getTransaction().commit();
        session.close();
    }
}
