package com.mcz.xhj.yz.dr_bean;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/10/26
 * 描述：积分排行榜javabean
 */
/*
*               "goodsName": "iPhone X",
                "goodsType": 0,
                "goodsValue": 100.00,
                "id": 5,
                "mobilePhone": "50****038",
                "needPoint": 0,
                "photo": "http://192.168.1.250:8088/upload/userPic//42.jpg",
                "point": 280,
                "realname": "欧衡",
                "uid": 42
* */


public class ScoreboardBean {


    private int uid;
    private String realname;
    private String photo;//头像地址
    private String goodsName;//商品名字
    private int goodsType;//商品类型0实物1虚拟
    private BigDecimal goodsValue;//商品价值
    private int needPoint;//需要积分
    private int point;//总积分
    private int pointMonth;//当月积分
    private String goodsPic;//商品图片路径
    /**
     * goodsValue : 698.0
     * id : 255
     * mobilePhone : 151****688
     */

    private String mobilePhone;

    public ScoreboardBean() {
    }

    public ScoreboardBean(int uid, String realname, String photo, String goodsName, int goodsType, BigDecimal goodsValue, int needPoint, int point, int pointMonth, String goodsPic) {
        this.uid = uid;
        this.realname = realname;
        this.photo = photo;
        this.goodsName = goodsName;
        this.goodsType = goodsType;
        this.goodsValue = goodsValue;
        this.needPoint = needPoint;
        this.point = point;
        this.pointMonth = pointMonth;
        this.goodsPic = goodsPic;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(int goodsType) {
        this.goodsType = goodsType;
    }

    public BigDecimal getGoodsValue() {
        return goodsValue;
    }

    public void setGoodsValue(BigDecimal goodsValue) {
        this.goodsValue = goodsValue;
    }

    public int getNeedPoint() {
        return needPoint;
    }

    public void setNeedPoint(int needPoint) {
        this.needPoint = needPoint;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getPointMonth() {
        return pointMonth;
    }

    public void setPointMonth(int pointMonth) {
        this.pointMonth = pointMonth;
    }

    public String getGoodsPic() {
        return goodsPic;
    }

    public void setGoodsPic(String goodsPic) {
        this.goodsPic = goodsPic;
    }

    @Override
    public String toString() {
        return "ScoreboardBean{" +
                "realname='" + realname + '\'' +
                ", photo='" + photo + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", goodsType=" + goodsType +
                ", goodsValue=" + goodsValue +
                ", needPoint=" + needPoint +
                ", point=" + point +
                ", pointMonth=" + pointMonth +
                ", goodsPic='" + goodsPic + '\'' +
                '}';
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }
}
