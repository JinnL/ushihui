package com.mcz.xhj.yz.dr_bean;

/**
 * Created by DELL on 2017/11/22.
 */

public class NoticeBean {
    private String id;
    private String title;
    private String appUrl;

    public NoticeBean() {
    }

    public NoticeBean(String id, String title, String appUrl) {
        this.id = id;
        this.title = title;
        this.appUrl = appUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    @Override
    public String toString() {
        return "NoticeBean{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", appUrl='" + appUrl + '\'' +
                '}';
    }
}
