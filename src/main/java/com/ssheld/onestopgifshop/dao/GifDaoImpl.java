package com.ssheld.onestopgifshop.dao;

import com.ssheld.onestopgifshop.model.Gif;
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
public class GifDaoImpl implements GifDao {
    @Autowired
    private SessionFactory sessionFactory;

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

    public Gif findById(Long id) {
        // Open session
        Session session = sessionFactory.openSession();
        Gif gif = session.get(Gif.class, id);
        // Close session
        session.close();

        return gif;
    }

    public void save(Gif gif) {
        // Open session
        Session session = sessionFactory.openSession();

        session.beginTransaction();
        session.saveOrUpdate(gif);
        session.getTransaction().commit();;

        // Close session
        session.close();
    }

    public void delete(Gif gif) {
        // Open session
        Session session = sessionFactory.openSession();

        session.beginTransaction();
        session.delete(gif);
        session.getTransaction().commit();;

        // Close session
        session.close();
    }
}
