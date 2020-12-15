package com.ssheld.onestopgifshop.dao;

import com.ssheld.onestopgifshop.model.Keyword;
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
public class KeywordDaoImpl {
    @Autowired
    private SessionFactory sessionFactory;

    public List<Keyword> findAll() {
        // Open a session
        Session session = sessionFactory.openSession();

        // Create CriteriaBuilder
        CriteriaBuilder builder = session.getCriteriaBuilder();

        // Create CriteriaQuery
        CriteriaQuery<Keyword> criteria = builder.createQuery(Keyword.class);

        // Specify criteria root
        criteria.from(Keyword.class);

        // Execute query
        List<Keyword> keywords = session.createQuery(criteria).getResultList();

        // Close session
        session.close();

        return keywords;
    }

    public Keyword findById(Long id) {
        // Open session
        Session session = sessionFactory.openSession();
        Keyword keyword = session.get(Keyword.class, id);
        // Close session
        session.close();

        return keyword;
    }

    public void save(Keyword keyword) {
        // Open a session
        Session session = sessionFactory.openSession();

        // Begin a transaction
        session.beginTransaction();

        // Save or update the keyword
        session.saveOrUpdate(keyword);

        // Commit the transaction
        session.getTransaction().commit();

        // Close the session
        session.close();
    }

    public void delete(Keyword keyword) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.delete(keyword);
        session.getTransaction().commit();
        session.close();
    }
}
