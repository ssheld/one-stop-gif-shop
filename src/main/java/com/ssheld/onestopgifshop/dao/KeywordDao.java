package com.ssheld.onestopgifshop.dao;

import com.ssheld.onestopgifshop.model.Keyword;

import java.util.List;

/**
 * Author: Stephen Sheldon
 **/
public interface KeywordDao {
    List<Keyword> findAll();
    Keyword findById(Long id);
    void save(Keyword keyword);
    void delete(Keyword keyword);
}
