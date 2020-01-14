package com.ekabao.oil.bean;

/**
 * 易卡宝  App
 *
 * @time 2018/7/27 16:34
 * Created by lj on 2018/7/27 16:34.
 */

public class BankInvest {


    /**
     * bankId : 29
     * bankName : 中国银行777
     * bankNum : 0346
     * dayQuotaJYT : 200000
     * idCards : 429********3216
     * mobilephone : 150****0346
     * realName : 仲夏爷爷
     * realVerify : 1
     * singleQuotaJYT : 50000
     * tpwdFlag : 1
     */

    private int bankId;
    private String bankName;
    private String bankNum;
    private int dayQuotaJYT;
    private String idCards;
    private String mobilephone;
    private String realName;
    private int realVerify;
    private int singleQuotaJYT;
    private int tpwdFlag;

    public int getBankId() {
        return bankId;
    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankNum() {
        return bankNum;
    }

    public void setBankNum(String bankNum) {
        this.bankNum = bankNum;
    }

    public int getDayQuotaJYT() {
        return dayQuotaJYT;
    }

    public void setDayQuotaJYT(int dayQuotaJYT) {
        this.dayQuotaJYT = dayQuotaJYT;
    }

    public String getIdCards() {
        return idCards;
    }

    public void setIdCards(String idCards) {
        this.idCards = idCards;
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getRealVerify() {
        return realVerify;
    }

    public void setRealVerify(int realVerify) {
        this.realVerify = realVerify;
    }

    public int getSingleQuotaJYT() {
        return singleQuotaJYT;
    }

    public void setSingleQuotaJYT(int singleQuotaJYT) {
        this.singleQuotaJYT = singleQuotaJYT;
    }

    public int getTpwdFlag() {
        return tpwdFlag;
    }

    public void setTpwdFlag(int tpwdFlag) {
        this.tpwdFlag = tpwdFlag;
    }
}
