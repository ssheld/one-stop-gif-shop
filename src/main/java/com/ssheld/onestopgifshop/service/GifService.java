package com.ssheld.onestopgifshop.service;

import com.ssheld.onestopgifshop.model.Gif;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Author: Stephen Sheldon
 **/
public interface GifService {
    List<Gif> findAll();
    Gif findById(Long id);
    void save(Gif gif, MultipartFile file);
    void delete(Gif gif);
    void toggleFavorite(Gif gif);
}
