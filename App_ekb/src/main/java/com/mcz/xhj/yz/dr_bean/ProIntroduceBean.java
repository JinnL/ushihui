package com.mcz.xhj.yz.dr_bean;

import java.io.Serializable;

/**
 * Created by DELL on 2017/11/21.
 * 描述：产品介绍
 */

public class ProIntroduceBean implements Serializable {
    /*
    *           "content": "新手专享是啄米金服依据严格风控标准，挑选优质资产为用户提供的投资产品，匹配良好担保方案，用户投资本息有保障。",
                "id": 8,
                "pid": 73,
                "title": "产品介绍"
    * */

    private Integer id;
    private Integer pid;
    private String content;
    private String title;

    public ProIntroduceBean() {
    }

    public ProIntroduceBean(Integer id, Integer pid, String content, String title) {
        this.id = id;
        this.pid = pid;
        this.content = content;
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "ProIntroduceBean{" +
                "id=" + id +
                ", pid=" + pid +
                ", content='" + content + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
