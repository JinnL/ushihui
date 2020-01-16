package com.ekabao.oil.bean;

/**
 * ${APP_NAME}  App_oil
 *
 * @time 2019/1/10 17:07
 * Created by lj on 2019/1/10 17:07.
 */

public class GoodsOrderList {


    /**
     * amount : 1598.0
     * goodname : 母亲节礼物-舒适安睡组合
     * images : http://yanxuan.nosdn.127.net/1f67b1970ee20fd572b7202da0ff705d.png
     * number : 1
     * orderid : 2953
     * retail_price : 1598.0
     * status : 0
     */

    private double amount;
    private String goodname;
    private String images;
    private int number;
    private int orderid;
    private double retail_price;
    private int status;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getGoodname() {
        return goodname;
    }

    public void setGoodname(String goodname) {
        this.goodname = goodname;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    public double getRetail_price() {
        return retail_price;
    }

    public void setRetail_price(double retail_price) {
        this.retail_price = retail_price;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
