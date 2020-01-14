package com.mcz.xhj.yz.dr_bean;

import java.math.BigDecimal;

/**
 * Created by DELL on 2017/11/2.
 */

public class TaskBean {
    private int id;
    private String name;//任务名称
    private String picImg;//图片路径-未完成
    private String pic_img;//图片路径-已完成
    private int successFlag;//是否完成 1 完成奖励待领取 2 未完成
    private BigDecimal cashMoney;//现金金额
    private BigDecimal experienceMoney;//体验金金额
    private BigDecimal redpacketMoney;//红包金额
    private int investCount;//邀请人数
    private int invest_count_yes;//已邀请人数
    private int invest_parent_count_month;//当月我的邀请人邀请人数
    private int type;
    private int jobCount;//已完成任务数
    private int jobSum;//任务总数
    private int finshWhether;
    private String linkUrlApp;

    public TaskBean() {
    }

    public String getPic_img() {
        return pic_img;
    }

    public void setPic_img(String pic_img) {
        this.pic_img = pic_img;
    }

    public int getFinshWhether() {
        return finshWhether;
    }

    public void setFinshWhether(int finshWhether) {
        this.finshWhether = finshWhether;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicImg() {
        return picImg;
    }

    public void setPicImg(String picImg) {
        this.picImg = picImg;
    }

    public int getSuccessFlag() {
        return successFlag;
    }

    public void setSuccessFlag(int successFlag) {
        this.successFlag = successFlag;
    }

    public BigDecimal getCashMoney() {
        return cashMoney;
    }

    public void setCashMoney(BigDecimal cashMoney) {
        this.cashMoney = cashMoney;
    }

    public BigDecimal getExperienceMoney() {
        return experienceMoney;
    }

    public void setExperienceMoney(BigDecimal experienceMoney) {
        this.experienceMoney = experienceMoney;
    }

    public BigDecimal getRedpacketMoney() {
        return redpacketMoney;
    }

    public void setRedpacketMoney(BigDecimal redpacketMoney) {
        this.redpacketMoney = redpacketMoney;
    }

    public int getInvestCount() {
        return investCount;
    }

    public void setInvestCount(int investCount) {
        this.investCount = investCount;
    }

    public int getInvest_count_yes() {
        return invest_count_yes;
    }

    public void setInvest_count_yes(int invest_count_yes) {
        this.invest_count_yes = invest_count_yes;
    }

    public int getInvest_parent_count_month() {
        return invest_parent_count_month;
    }

    public void setInvest_parent_count_month(int invest_parent_count_month) {
        this.invest_parent_count_month = invest_parent_count_month;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getJobCount() {
        return jobCount;
    }

    public void setJobCount(int jobCount) {
        this.jobCount = jobCount;
    }

    public int getJobSum() {
        return jobSum;
    }

    public void setJobSum(int jobSum) {
        this.jobSum = jobSum;
    }

    public String getLinkUrlApp() {
        return linkUrlApp;
    }

    public void setLinkUrlApp(String linkUrlApp) {
        this.linkUrlApp = linkUrlApp;
    }

    @Override
    public String toString() {
        return "TaskBean{" +
                "name='" + name + '\'' +
                ", picImg='" + picImg + '\'' +
                ", successFlag=" + successFlag +
                ", cashMoney=" + cashMoney +
                ", experienceMoney=" + experienceMoney +
                ", redpacketMoney=" + redpacketMoney +
                ", investCount=" + investCount +
                ", invest_count_yes=" + invest_count_yes +
                ", invest_parent_count_month=" + invest_parent_count_month +
                ", type=" + type +
                ", jobCount=" + jobCount +
                ", jobSum=" + jobSum +
                '}';
    }
}
