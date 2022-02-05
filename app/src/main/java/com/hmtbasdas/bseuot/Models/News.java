package com.hmtbasdas.bseuot.Models;

import java.io.Serializable;

public class News implements Serializable {
    private final String newsID;
    private final int newsEYE;
    private final String newsTITLE;
    private final String newsDESC;
    private final String newsDATE;
    private final String newsIMAGE;
    private final Boolean newsSTATUS;

    public News(String newsID, int newsEYE, String newsTITLE, String newsDESC, String newsDATE, String newsIMAGE, Boolean newsSTATUS) {
        this.newsID = newsID;
        this.newsEYE = newsEYE;
        this.newsTITLE = newsTITLE;
        this.newsDESC = newsDESC;
        this.newsDATE = newsDATE;
        this.newsIMAGE = newsIMAGE;
        this.newsSTATUS = newsSTATUS;
    }

    public String getNewsID() {
        return newsID;
    }

    public int getNewsEYE() {
        return newsEYE;
    }

    public String getNewsTITLE() {
        return newsTITLE;
    }

    public String getNewsDESC() {
        return newsDESC;
    }

    public String getNewsDATE() {
        return newsDATE;
    }

    public String getNewsIMAGE() {
        return newsIMAGE;
    }

    public Boolean getNewsSTATUS() {
        return newsSTATUS;
    }
}