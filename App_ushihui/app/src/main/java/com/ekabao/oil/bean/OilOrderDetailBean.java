package com.ekabao.oil.bean;

import java.util.List;

/**
 * ${APP_NAME}  App_oil
 * 我的订单详情
 *
 * @time 2018/11/7 18:24
 * Created by lj on 2018/11/7 18:24.
 */

public class OilOrderDetailBean {


    /**
     * amount : 5000
     * payType : 1
     * fAmount : 10
     * agreementNo : 951951951951
     * fullName : 9.7折3个月加油套餐
     * id : 1077
     * type : 1
     * investTime : 1541475535000
     * rechargeList : [{"date":"2018-11-06","shouldInterest":0,"shouldPrincipal":500,"status":1},{"date":"2018-12-06","shouldInterest":0,"shouldPrincipal":500,"status":0},{"date":"2019-01-06","shouldInterest":0,"shouldPrincipal":500,"status":0}]
     */

    private double amount;
    private int payType;
    private int fAmount;
    private String agreementNo;
    private String fullName;
    private int id;
    private int type;
    private long investTime;
    private List<RechargeListBean> rechargeList;

    /**
     * cardType : 1
     * paynum : 201811071124300870355
     * cardnum : 9527952795279527
     * factAmount : 291
     * status : 1
     */

    private int cardType;
    private String paynum;
    private String cardnum;
    private String fuelId;
    private double factAmount;
    private int status;
    private String trackingName;
    private String trackingNumber;
    /**
     * amount : 30.0
     * cardId : 109
     * factAmount : 29.7
     * rate : 0.99
     */

    private int cardId;
    private double rate;
    /**
     * amount : 1200.0
     * fAmount : 20.0
     * factAmount : 940.0
     * fid : 27070
     * periods : 0
     * pid : 97
     * rate : 0.8
     * rechargeList : []
     * totalPeriods : 0
     */

    private int fid;
    private int periods;
    private int pid;
    private int totalPeriods;
    /**
     * address : 北京北京市东城区啦咯啦咯啦咯啦
     * amount : 310.0
     * cardPid : 1004
     * factAmount : 155.0
     * factInterest : 0.0
     * freight : 10.0
     * interest : 140.0
     * rate : 0.95
     * receiveName : 李建
     * receivePhone : 13183386761
     */


    private double factInterest; //退订的金额
    private double interest; //冻结的余额
    private long refundTime; //退订时间


    private String address; //地址
    private double freight; //运费
    private String receiveName;
    private String receivePhone;

    private int investId;//油卡领取详情-->套餐详情的


    public int getInvestId() {
        return investId;
    }

    public void setInvestId(int investId) {
        this.investId = investId;
    }

    public long getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(long refundTime) {
        this.refundTime = refundTime;
    }


    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }


    public double getFactInterest() {
        return factInterest;
    }

    public void setFactInterest(double factInterest) {
        this.factInterest = factInterest;
    }


    public OilOrderDetailBean() {
    }

    public OilOrderDetailBean(int amount, int payType, int fAmount, String agreementNo, String fullName, int id, int type, long investTime, List<RechargeListBean> rechargeList, int cardType, String paynum, String cardnum, int factAmount, int status) {
        this.amount = amount;
        this.payType = payType;
        this.fAmount = fAmount;
        this.agreementNo = agreementNo;
        this.fullName = fullName;
        this.id = id;
        this.type = type;
        this.investTime = investTime;
        this.rechargeList = rechargeList;
        this.cardType = cardType;
        this.paynum = paynum;
        this.cardnum = cardnum;
        this.factAmount = factAmount;
        this.status = status;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public int getFAmount() {
        return fAmount;
    }

    public void setFAmount(int fAmount) {
        this.fAmount = fAmount;
    }

    public String getAgreementNo() {
        return agreementNo;
    }

    public void setAgreementNo(String agreementNo) {
        this.agreementNo = agreementNo;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getInvestTime() {
        return investTime;
    }

    public void setInvestTime(long investTime) {
        this.investTime = investTime;
    }

    public List<RechargeListBean> getRechargeList() {
        return rechargeList;
    }

    public void setRechargeList(List<RechargeListBean> rechargeList) {
        this.rechargeList = rechargeList;
    }

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

    public String getPaynum() {
        return paynum;
    }

    public void setPaynum(String paynum) {
        this.paynum = paynum;
    }

    public String getCardnum() {
        return cardnum;
    }

    public void setCardnum(String cardnum) {
        this.cardnum = cardnum;
    }

    public double getFactAmount() {
        return factAmount;
    }

    public void setFactAmount(double factAmount) {
        this.factAmount = factAmount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getfuelId() {
        return fuelId;
    }

    public void setfuelId(String fuelId) {
        this.fuelId = fuelId;
    }

    public String getTrackingName() {
        return trackingName;
    }

    public void setTrackingName(String trackingName) {
        this.trackingName = trackingName;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public int getPeriods() {
        return periods;
    }

    public void setPeriods(int periods) {
        this.periods = periods;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getTotalPeriods() {
        return totalPeriods;
    }

    public void setTotalPeriods(int totalPeriods) {
        this.totalPeriods = totalPeriods;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public double getFreight() {
        return freight;
    }

    public void setFreight(double freight) {
        this.freight = freight;
    }


    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public String getReceivePhone() {
        return receivePhone;
    }

    public void setReceivePhone(String receivePhone) {
        this.receivePhone = receivePhone;
    }

    public static class RechargeListBean {
        /**
         * date : 2018-11-06
         * shouldInterest : 0
         * shouldPrincipal : 500
         * status : 1  1(充值状态 0未充值，1已充值，2逾期(充值失败))
         */

        private String date;
        private int shouldInterest;
        private int shouldPrincipal;
        private int status;

        public RechargeListBean() {

        }

        public RechargeListBean(String date, int shouldInterest, int shouldPrincipal, int status) {
            this.date = date;
            this.shouldInterest = shouldInterest;
            this.shouldPrincipal = shouldPrincipal;
            this.status = status;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getShouldInterest() {
            return shouldInterest;
        }

        public void setShouldInterest(int shouldInterest) {
            this.shouldInterest = shouldInterest;
        }

        public int getShouldPrincipal() {
            return shouldPrincipal;
        }

        public void setShouldPrincipal(int shouldPrincipal) {
            this.shouldPrincipal = shouldPrincipal;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
