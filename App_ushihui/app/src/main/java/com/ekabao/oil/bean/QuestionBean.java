package com.ekabao.oil.bean;

/**
 * Created by Administrator on 2017/11/6 0006.
 */

public class QuestionBean {
    private String title;
    private String content;

    public QuestionBean() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "QuestionBean{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
