package com.ekabao.oil.bean;

/**
 * ${APP_NAME}  App_oil
 * 我的油卡
 * @time 2018/11/7 18:20
 * Created by lj on 2018/11/7 18:20.
 */

public class OilCardBean {


    /**
     * lastTime : 500
     * cardnum : 9527952795279527
     * nextAmount : 500
     * nextTime : 500
     * id : 12
     * type : 1
     * lastAmount : 500
     */

    private long lastTime;
    private String cardnum;
    private int nextAmount;
    private long nextTime;
    private int id;
    private int type;
    private int lastAmount;

    public OilCardBean() {

    }

    public OilCardBean(long lastTime, String cardnum, int nextAmount, long nextTime, int id, int type, int lastAmount) {
        this.lastTime = lastTime;
        this.cardnum = cardnum;
        this.nextAmount = nextAmount;
        this.nextTime = nextTime;
        this.id = id;
        this.type = type;
        this.lastAmount = lastAmount;
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    public String getCardnum() {
        return cardnum;
    }

    public void setCardnum(String cardnum) {
        this.cardnum = cardnum;
    }

    public int getNextAmount() {
        return nextAmount;
    }

    public void setNextAmount(int nextAmount) {
        this.nextAmount = nextAmount;
    }

    public long getNextTime() {
        return nextTime;
    }

    public void setNextTime(long nextTime) {
        this.nextTime = nextTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLastAmount() {
        return lastAmount;
    }

    public void setLastAmount(int lastAmount) {
        this.lastAmount = lastAmount;
    }
}
