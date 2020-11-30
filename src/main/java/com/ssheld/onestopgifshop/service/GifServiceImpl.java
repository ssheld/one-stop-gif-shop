package com.ssheld.onestopgifshop.service;

import com.ssheld.onestopgifshop.dao.GifDao;
import com.ssheld.onestopgifshop.model.Gif;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Author: Stephen Sheldon
 **/
@Service
public class GifServiceImpl implements GifService {
    @Autowired
    private GifDao gifDao;

    public List<Gif> findAll() {
        return gifDao.findAll();
    }

    public Gif findById(Long id) {
        return gifDao.findById(id);
    }

    public void save(Gif gif, MultipartFile file) {
        try {
            gif.setBytes(file.getBytes());
            gifDao.save(gif);
        } catch (IOException e) {
            System.out.println("Unable to get byte array from uploaded file.");
        }
    }

    public void delete(Gif gif) {
        gifDao.delete(gif);
    }

    public void toggleFavorite(Gif gif) {
        gif.setFavorite(!gif.isFavorite());
        gifDao.save(gif);
    }
}
