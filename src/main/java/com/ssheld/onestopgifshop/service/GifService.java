package com.ssheld.onestopgifshop.service;

import com.ssheld.onestopgifshop.model.Gif;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Author: Stephen Sheldon
 **/
public interface GifService {
    List<Gif> findAll();
    Gif findById(Long id) throws ServiceException;
    void save(Gif gif, MultipartFile file) throws ServiceException;
    void delete(Gif gif) throws ServiceException;
    void toggleFavorite(Gif gif) throws ServiceException;
}
