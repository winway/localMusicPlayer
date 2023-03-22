package com.example.localmusicplayer;

/**
 * @PackageName: com.example.localmusicplayer
 * @ClassName: SongBean
 * @Author: winwa
 * @Date: 2023/3/22 7:51
 * @Description:
 **/
public class SongBean {
    private String id;
    private String name;
    private String singer;
    private String album;
    private String duration;
    private String path;

    public SongBean() {
    }

    public SongBean(String id, String name, String singer, String album, String duration, String path) {
        this.id = id;
        this.name = name;
        this.singer = singer;
        this.album = album;
        this.duration = duration;
        this.path = path;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
