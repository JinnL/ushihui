package com.ekabao.oil.bean;

/**
 * ${APP_NAME}  App_oil
 *  .我的订单
 * @time 2018/11/7 18:23
 * Created by lj on 2018/11/7 18:23.
 */

public class OilOrdersbean {

    /**
     * amount : 5000
     * fullName : 9.7折3个月加油套餐
     * id : 1076
     * investTime : 1541473696000
     * type : 1
     * status : 1
     */

    private double amount;
    private String fullName;
    private int id;
    private long investTime;
    private int type;
    private int status;

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

    private int cardType;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getInvestTime() {
        return investTime;
    }

    public void setInvestTime(long investTime) {
        this.investTime = investTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
