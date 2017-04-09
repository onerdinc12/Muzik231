package com.example.jean.jcplayer;

import android.support.annotation.RawRes;

import java.io.Serializable;

/**
 * Created by jean on 27/06/16.
 */

public class JcAudio implements Serializable {
    private long id;
    private String title;
    private int position;
    private String yazar;
    private String sozler;
    private String path;
    private Origin origin;



    public JcAudio(String title, String path,String yazar,String sozler, Origin origin){
        // It looks bad
        int randomNumber = path.length() + title.length();
        this.yazar = yazar;
        this.sozler = sozler;
        this.id = randomNumber;
        this.position = randomNumber;
        this.title = title;
        this.path = path;
        this.origin = origin;
    }



    public JcAudio(String title, String path, String yazar,String sozler, long id, int position, Origin origin){
        this.id = id;
        this.position = position;
        this.title = title;
        this.yazar = yazar;
        this.sozler = sozler;
        this.path = path;

        this.origin = origin;
    }

    public String getSozler() {
        return sozler;
    }

    public void setSozler(String sozler) {
        this.sozler = sozler;
    }

    public String getYazar() {
        return yazar;
    }

    public void setYazar(String yazar) {
        this.yazar = yazar;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Origin getOrigin() {
        return origin;
    }

    public void setOrigin(Origin origin) {
        this.origin = origin;
    }



    public static JcAudio createFromURL(String url) {
        return new JcAudio(url, url,url,url, Origin.URL);
    }


    public static JcAudio createFromURL(String title, String url,String yazar,String sozler) {
        return new JcAudio(title, url,yazar,sozler,Origin.URL);
    }




}