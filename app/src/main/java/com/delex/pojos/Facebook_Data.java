package com.delex.pojos;

import java.io.Serializable;

/**
 * Created by embed on 15/5/17.
 */

public class Facebook_Data implements Serializable {

    private String is_silhouette;
    private String url;

    public String getIs_silhouette() {
        return is_silhouette;
    }

    public void setIs_silhouette(String is_silhouette) {
        this.is_silhouette = is_silhouette;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
