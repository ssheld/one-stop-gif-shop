package com.ssheld.onestopgifshop.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Stephen Sheldon
 **/
@Entity
public class GifMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(fetch = FetchType.EAGER)
    List<Keyword> keywordList;

    public GifMetadata() {
        this.keywordList = new ArrayList<>();
    }

    public List<Keyword> getKeywordList() {
        return keywordList;
    }

    public void setKeywordList(List<Keyword> keywordList) {
        this.keywordList = keywordList;
    }
}
