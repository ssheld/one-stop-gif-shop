package com.ssheld.onestopgifshop.dao;

import com.ssheld.onestopgifshop.model.Gif;

import java.util.List;

/**
 * Author: Stephen Sheldon
 **/
public interface GifDao {
    List<Gif> findAll();
    Gif findById(Long id);
    void save(Gif gif);
    void delete(Gif gif);
}