package com.ekabao.oil.bean;

import android.support.annotation.NonNull;

/**
 * ${APP_NAME}  App_oil
 *
 * @time 2018/11/3 17:25
 * Created by lj on 2018/11/3 17:25.
 */

public class OilCardPackageBean implements Comparable<OilCardPackageBean> {
    /**
     * activityRate : 0
     * billType : 1
     * code : CP-2018102315320273914
     * deadline : 18
     * fullName : 7.6折18个月加油套餐
     * id : 97
     * increasAmount : 100
     * interestType : 0
     * isDouble : 0
     * isHot : 1
     * leastaAmount : 100
     * lid : 1
     * maxAmount : 0
     * raiseDeadline : 0
     * rate : 0.76
     * repayType : 1
     * sid : 1
     * simpleName : 7.6折18个月加油套餐
     * startDate : 1541382446000
     * status : 5
     * tag : 限量抢购
     * type : 1
     */

    private double activityRate;
    private int billType;
    private String code;
    private int deadline;
    private String fullName;
    private int id;
    private int increasAmount;
    private int interestType;
    private int isDouble;
    private int isHot;
    private int leastaAmount;
    private int lid;
    private int maxAmount;
    private int raiseDeadline;
    private double rate;
    private int repayType;
    private int sid;
    private String simpleName;
    private long startDate;
    private int status;
    private int stock;

    private String tag;
    private int type;

    public int getSoldNum() {
        return soldNum;
    }

    public void setSoldNum(int soldNum) {
        this.soldNum = soldNum;
    }

    private int soldNum; //已售数量

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    private double amount; //运费



    //9.2折 6个月加油套餐

    public double getActivityRate() {
        return activityRate;
    }

    public void setActivityRate(double activityRate) {
        this.activityRate = activityRate;
    }

    public int getBillType() {
        return billType;
    }

    public void setBillType(int billType) {
        this.billType = billType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getDeadline() {
        return deadline;
    }

    public void setDeadline(int deadline) {
        this.deadline = deadline;
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

    public int getIncreasAmount() {
        return increasAmount;
    }

    public void setIncreasAmount(int increasAmount) {
        this.increasAmount = increasAmount;
    }

    public int getInterestType() {
        return interestType;
    }

    public void setInterestType(int interestType) {
        this.interestType = interestType;
    }

    public int getIsDouble() {
        return isDouble;
    }

    public void setIsDouble(int isDouble) {
        this.isDouble = isDouble;
    }

    public int getIsHot() {
        return isHot;
    }

    public void setIsHot(int isHot) {
        this.isHot = isHot;
    }

    public int getLeastaAmount() {
        return leastaAmount;
    }

    public void setLeastaAmount(int leastaAmount) {
        this.leastaAmount = leastaAmount;
    }

    public int getLid() {
        return lid;
    }

    public void setLid(int lid) {
        this.lid = lid;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(int maxAmount) {
        this.maxAmount = maxAmount;
    }

    public int getRaiseDeadline() {
        return raiseDeadline;
    }

    public void setRaiseDeadline(int raiseDeadline) {
        this.raiseDeadline = raiseDeadline;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getRepayType() {
        return repayType;
    }

    public void setRepayType(int repayType) {
        this.repayType = repayType;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /**
     * TreeSet集合是用来对象元素进行排序的,同样他也可以保证元素的唯一
     * 当compareTo方法返回0的时候集合中只有一个元素
     * 当compareTo方法返回正数的时候集合会怎么存就怎么取
     * 当compareTo方法返回负数的时候集合会倒序存储
     */

    @Override
    public int compareTo(@NonNull OilCardPackageBean o) {

        /*int num = this.deadline - o.deadline;
        return num == 0 ? 1 : num;*/


       // double num =  o.rate - this.rate;
        return this.rate> o.rate? 1 : -1;




    }
}
