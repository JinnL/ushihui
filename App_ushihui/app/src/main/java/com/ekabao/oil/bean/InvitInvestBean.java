package com.ekabao.oil.bean;

import java.io.Serializable;

/**
 * 项目名称：JsAppAs2.0
 * 类描述：三重好礼的好友出借
 * 创建人：shuc
 * 创建时间：2017/4/7 14:47
 * 修改人：DELL
 * 修改时间：2017/4/7 14:47
 * 修改备注：
 */
public class InvitInvestBean implements Serializable{
    private String realname;//	是	String	姓名
    private String investAmount;//	是	Double	出借金额
    private String rebateAmount;//	是	Double	返利金额
    private String investOrder;//	是	Int	出借次数
    private double status;//	是	Int	0 = 未领取 ，1 = 已领取

    private double amount;
    private String mobilePhone;
    private String rownum;//排名
    private int uid;


    public InvitInvestBean() {
        super();
    }

    public InvitInvestBean(String realname, String investAmount, String rebateAmount, String investOrder, double status) {
        super();
        this.realname = realname;
        this.investAmount = investAmount;
        this.rebateAmount = rebateAmount;
        this.investOrder = investOrder;
        this.status = status;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(String investAmount) {
        this.investAmount = investAmount;
    }

    public String getRebateAmount() {
        return rebateAmount;
    }

    public void setRebateAmount(String rebateAmount) {
        this.rebateAmount = rebateAmount;
    }

    public String getInvestOrder() {
        return investOrder;
    }

    public void setInvestOrder(String investOrder) {
        this.investOrder = investOrder;
    }

    public double getStatus() {
        return status;
    }

    public void setStatus(double status) {
        this.status = status;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
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
