package com.ekabao.oil.bean;

/**
 * $desc$
 *
 * @time $data$ $time$
 * Created by Administrator on 2018/7/16.
 */

public class CapitaldetailsBean {


    /**
     * addTime : 1531288335000
     * amount : 10000.0
     * balance : 9933215.0
     * investId : 410
     * pid : 53
     * remark : 【募集中】出借【理财宝No.43-90】产品
     * status : 4
     * tradeType : 3
     * type : 0
     * uid : 57
     */

    private long addTime;
    private double amount;
    private double balance;
    private int investId;
    private int pid;
    private String remark;
    private int status;
    private int tradeType;
    private int type;
    private int uid;

    private String orderNo;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }




    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getInvestId() {
        return investId;
    }

    public void setInvestId(int investId) {
        this.investId = investId;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTradeType() {
        return tradeType;
    }

    public void setTradeType(int tradeType) {
        this.tradeType = tradeType;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
