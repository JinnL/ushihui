package com.mcz.xhj.yz.dr_bean;

/**
 * Created by zhulang on 2017/8/30.
 */

public class LogoBean {
    private String imgUrl;
    private String clickUrl;
    private String title;

    public LogoBean() {
    }

    public LogoBean(String imgUrl, String clickUrl, String title) {
        this.imgUrl = imgUrl;
        this.clickUrl = clickUrl;
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getClickUrl() {
        return clickUrl;
    }

    public void setClickUrl(String clickUrl) {
        this.clickUrl = clickUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "LogoBean{" +
                "imgUrl='" + imgUrl + '\'' +
                ", clickUrl='" + clickUrl + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
