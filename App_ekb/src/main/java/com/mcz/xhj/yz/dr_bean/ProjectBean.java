package com.mcz.xhj.yz.dr_bean;

/**
 * Created by DELL on 2017/11/13.
 */

public class ProjectBean {
    private String title;

    public ProjectBean() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "ProjectBean{" +
                "title='" + title + '\'' +
                '}';
    }
}
