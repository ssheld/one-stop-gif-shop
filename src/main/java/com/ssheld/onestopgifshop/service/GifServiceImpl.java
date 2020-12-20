package com.ssheld.onestopgifshop.service;

import com.ssheld.onestopgifshop.dao.DaoException;
import com.ssheld.onestopgifshop.dao.GifDao;
import com.ssheld.onestopgifshop.model.Gif;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Author: Stephen Sheldon
 **/
@Service
public class GifServiceImpl implements GifService {

    private static Logger logger = LoggerFactory.getLogger(GifServiceImpl.class);

    @Autowired
    private GifDao gifDao;

    public List<Gif> findAll() {
        return gifDao.findAll();
    }

    public Gif findById(Long id) {
        Gif gif = null;

        try {
            gif = gifDao.findById(id);
        } catch (DaoException e) {
            // TODO - Wrap in ServiceException
        }

        return gif;
    }

    public void save(Gif gif, MultipartFile file) {

        try {
            gifDao.save(gif);
        } catch (DaoException e) {
            // TODO - Wrap in ServiceException
        }
    }

    public void delete(Gif gif) {
        try {
            gifDao.delete(gif);
        } catch (DaoException e) {
            // TODO - Wrap in ServiceException
        }
    }

    public void toggleFavorite(Gif gif) {
        gif.setFavorite(!gif.isFavorite());
        try {
            gifDao.save(gif);
        } catch (DaoException e) {
            // TODO - Wrap in ServiceException
        }
    }
}
