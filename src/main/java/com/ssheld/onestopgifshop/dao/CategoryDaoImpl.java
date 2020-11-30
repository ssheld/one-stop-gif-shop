package com.ssheld.onestopgifshop.dao;

import com.ssheld.onestopgifshop.model.Category;
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
public class CategoryDaoImpl implements CategoryDao {
    @Autowired
    private SessionFactory sessionFactory;

    public List<Category> findAll() {
        // Open a session
        Session session = sessionFactory.openSession();

        // Create CriteriaBuilder
        CriteriaBuilder builder = session.getCriteriaBuilder();

        // Create CriteriaQuery
        CriteriaQuery<Category> criteria = builder.createQuery(Category.class);

        // Specify criteria root
        criteria.from(Category.class);

        // Execute query
        List<Category> categories = session.createQuery(criteria).getResultList();

        // Close session
        session.close();

        return categories;
    }

    public Category findById(Long id) {
        Session session = sessionFactory.openSession();
        Category category = session.get(Category.class, id);
        Hibernate.initialize(category.getGifs());
        session.close();
        return category;
    }

    public void save(Category category) {
        // Open a session
        Session session = sessionFactory.openSession();

        // Begin a transaction
        session.beginTransaction();

        // Save or update the category
        session.saveOrUpdate(category);

        // Commit the transaction
        session.getTransaction().commit();

        // Close the session
        session.close();
    }

    public void delete(Category category) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.delete(category);
        session.getTransaction().commit();
        session.close();
    }
}

