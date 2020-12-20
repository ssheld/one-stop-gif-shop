package com.ssheld.onestopgifshop.dao;

import com.ssheld.onestopgifshop.model.Gif;

import java.util.List;

/**
 * Author: Stephen Sheldon
 **/
public interface GifDao {
    List<Gif> findAll();
    Gif findById(Long id) throws DaoException;
    void save(Gif gif) throws DaoException;
    void delete(Gif gif) throws DaoException;
}