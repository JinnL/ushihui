package com.mcz.xhj.yz.dr_bean;

/**
 * Created by zhulang on 2017/8/31.
 * 2.0版 金服列表项javabean
 */

public class InvestListBean2 {
    /*
    *           "deadline": 95,
                "endDate": 1502242475000,
                "fullName": "0807发标",
                "id": 62,
                "isCash": 1,
                "isDouble": 1,
                "isHot": 1,
                "isInterest": 1,
                "maxRate": 11.80,
                "minRate": 11.80,
                "pert": 22,
                "raiseDeadline": 3,
                "startDate": 1502069675000
    * */
    private String deadline;
    private String raiseDeadline;
    private String fullName;
    private String id;
    private double maxRate;
    private double minRate;
    private double activityRate;
    private double rate;
    private double pert;
    private Integer isCash;
    private Integer isInterest;
    private Integer isDouble;
    private Integer isHot;
    private boolean roundOffFlag;
    private String tag;
    private String startDate;//预售开始时间
    private String endDate;//预售结束时间
    private String type;

    public InvestListBean2() {
    }

    public InvestListBean2(String deadline, String raiseDeadline, String fullName, String id, double maxRate, double minRate, double pert, Integer isCash, Integer isInterest, Integer isDouble, Integer isHot, String startDate, String endDate,String type) {
        this.deadline = deadline;
        this.raiseDeadline = raiseDeadline;
        this.fullName = fullName;
        this.id = id;
        this.maxRate = maxRate;
        this.minRate = minRate;
        this.pert = pert;
        this.isCash = isCash;
        this.isInterest = isInterest;
        this.isDouble = isDouble;
        this.isHot = isHot;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
    }

    public double getActivityRate() {
        return activityRate;
    }

    public void setActivityRate(double activityRate) {
        this.activityRate = activityRate;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isRoundOffFlag() {
        return roundOffFlag;
    }

    public void setRoundOffFlag(boolean roundOffFlag) {
        this.roundOffFlag = roundOffFlag;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getRaiseDeadline() {
        return raiseDeadline;
    }

    public void setRaiseDeadline(String raiseDeadline) {
        this.raiseDeadline = raiseDeadline;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getMaxRate() {
        return maxRate;
    }

    public void setMaxRate(double maxRate) {
        this.maxRate = maxRate;
    }

    public double getMinRate() {
        return minRate;
    }

    public void setMinRate(double minRate) {
        this.minRate = minRate;
    }

    public double getPert() {
        return pert;
    }

    public void setPert(double pert) {
        this.pert = pert;
    }

    public Integer getIsCash() {
        return isCash;
    }

    public void setIsCash(Integer isCash) {
        this.isCash = isCash;
    }

    public Integer getIsInterest() {
        return isInterest;
    }

    public void setIsInterest(Integer isInterest) {
        this.isInterest = isInterest;
    }

    public Integer getIsDouble() {
        return isDouble;
    }

    public void setIsDouble(Integer isDouble) {
        this.isDouble = isDouble;
    }

    public Integer getIsHot() {
        return isHot;
    }

    public void setIsHot(Integer isHot) {
        this.isHot = isHot;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
