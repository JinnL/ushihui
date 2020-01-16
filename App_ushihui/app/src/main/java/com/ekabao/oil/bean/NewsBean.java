package com.ekabao.oil.bean;

/**
 * $desc$
 *
 * @time $data$ $time$
 * Created by Administrator on 2018/7/13.
 */

public class NewsBean {

    /**
     * addTime : 1531471191000
     * content : 尊敬的用户，您出借的产品理财宝No.17-60天已成功回款10150.00元，其中本金10000.00元，收益150.00元，祝您在易卡宝收益多多！
     * id : 945
     * isRead : 0
     * puId : 0
     * ruId : 57
     * status : 0
     * title : 回款通知
     * type : 1
     */

    private long addTime;
    private String content;
    private int id;
    private int isRead;
    private int puId;
    private int ruId;
    private int status;
    private String title;
    private int type;

    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public int getPuId() {
        return puId;
    }

    public void setPuId(int puId) {
        this.puId = puId;
    }

    public int getRuId() {
        return ruId;
    }

    public void setRuId(int ruId) {
        this.ruId = ruId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
