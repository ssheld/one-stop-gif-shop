package com.ssheld.onestopgifshop.dao;

import com.ssheld.onestopgifshop.model.GifMetadata;

import java.util.List;

/**
 * Author: Stephen Sheldon
 **/
public interface GifMetadataDao {
    List<GifMetadata> findAll();
    GifMetadata findById(Long id);
    void save(GifMetadata gifMetadata);
    void delete(GifMetadata gifMetadata);
}
