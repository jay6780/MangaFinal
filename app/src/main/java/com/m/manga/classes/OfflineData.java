package com.m.manga.classes;

public class OfflineData {
    private String desc;
    private String Id;
    private String title;
    private String thumbUrl;

    public OfflineData(String desc, String Id, String title,String thumbUrl){
        this.desc = desc;
        this.Id = Id;
        this.title = title;
        this.thumbUrl = thumbUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }
}
