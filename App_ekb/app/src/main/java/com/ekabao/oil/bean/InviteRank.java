package com.ekabao.oil.bean;

/**
 * Created by Administrator on 2018/7/10.
 * 好友邀请排行榜
 */

public class InviteRank {

    /**
     * amount : 477.52
     * investCount : 6
     * mobilePhone : 150****0346
     * rownum : 1
     * uid : 5
     */

    private double amount;
    private int investCount;
    private String mobilePhone;
    private String rownum;
    private int uid;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getInvestCount() {
        return investCount;
    }

    public void setInvestCount(int investCount) {
        this.investCount = investCount;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getRownum() {
        return rownum;
    }

    public void setRownum(String rownum) {
        this.rownum = rownum;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
