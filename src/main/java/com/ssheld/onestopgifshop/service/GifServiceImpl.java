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
        List<Gif> gifs = gifDao.findAll();

        return gifDao.findAll();
    }

    public Gif findById(Long id) throws ServiceException {
        Gif gif = null;

        try {
            gif = gifDao.findById(id);
        } catch (DaoException e) {
            throw new ServiceException("GifService.findById", "GifService failed to find file by ID.");
        }

        return gif;
    }

    public void save(Gif gif, MultipartFile file) throws ServiceException {

        try {
            gifDao.save(gif);
        } catch (DaoException e) {
            throw new ServiceException("GifService.save", "GifService Failed to save file.");
        }
    }

    public void delete(Gif gif) throws ServiceException {
        try {
            gifDao.delete(gif);
        } catch (DaoException e) {
            throw new ServiceException("GifService.delete", "GifService failed to delete file.");
        }
    }

    public void toggleFavorite(Gif gif) throws ServiceException {
        gif.setFavorite(!gif.isFavorite());
        try {
            gifDao.save(gif);
        } catch (DaoException e) {
            throw new ServiceException("GifService.toggleFavorite", "GifService failed to toggle favorite.");
        }
    }
}
