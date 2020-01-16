package com.mcz.xhj.yz.dr_bean;

/**
 * Created by DELL on 2017/11/16.
 */

public class JifenDetailBean {
    /*
    *
    * "addTime": 1510714196000,
                "id": 24,
                "point": 9,
                "remark": "投资0807发标赠送积分:9",
                "type": 1,
                "uid": 86

    * */

    private long addTime;
    private Integer id;
    private double point;
    private String remark;//交易备注"投资0807发标赠送积分:9"
    private String type	;//Integer	0=支出，1=收入
    private Integer uid;

    public JifenDetailBean() {
    }

    public JifenDetailBean(long addTime, Integer id, double point, String remark, String type, Integer uid) {
        this.addTime = addTime;
        this.id = id;
        this.point = point;
        this.remark = remark;
        this.type = type;
        this.uid = uid;
    }

    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getPoint() {
        return point;
    }

    public void setPoint(double point) {
        this.point = point;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "JifenDetailBean{" +
                "addTime=" + addTime +
                ", id=" + id +
                ", point=" + point +
                ", remark='" + remark + '\'' +
                ", type=" + type +
                ", uid=" + uid +
                '}';
    }
}
