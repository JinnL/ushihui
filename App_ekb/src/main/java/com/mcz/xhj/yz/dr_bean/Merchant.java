package com.mcz.xhj.yz.dr_bean;

/**
 * Created by zhulang on 2017/6/21.
 * 富友支付返回的商户信息对象
 */

public class Merchant {
    private String amt;
    private String backurl;
    private String bankcard;
    private String idno;
    private String idtype;
    private String mchntcd;
    private String mchntorderid;
    private String name;
    private String sign;
    private String signtp;
    private String type;
    private String userid;
    private String version;
    private String test;

    public Merchant() {
    }

    public Merchant(String amt, String backurl, String bankcard, String idno, String idtype, String mchntcd, String mchntorderid, String name, String sign, String signtp, String type, String userid, String version, String test) {
        this.amt = amt;
        this.backurl = backurl;
        this.bankcard = bankcard;
        this.idno = idno;
        this.idtype = idtype;
        this.mchntcd = mchntcd;
        this.mchntorderid = mchntorderid;
        this.name = name;
        this.sign = sign;
        this.signtp = signtp;
        this.type = type;
        this.userid = userid;
        this.version = version;
        this.test = test;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getBackurl() {
        return backurl;
    }

    public void setBackurl(String backurl) {
        this.backurl = backurl;
    }

    public String getBankcard() {
        return bankcard;
    }

    public void setBankcard(String bankcard) {
        this.bankcard = bankcard;
    }

    public String getIdno() {
        return idno;
    }

    public void setIdno(String idno) {
        this.idno = idno;
    }

    public String getIdtype() {
        return idtype;
    }

    public void setIdtype(String idtype) {
        this.idtype = idtype;
    }

    public String getMchntcd() {
        return mchntcd;
    }

    public void setMchntcd(String mchntcd) {
        this.mchntcd = mchntcd;
    }

    public String getMchntorderid() {
        return mchntorderid;
    }

    public void setMchntorderid(String mchntorderid) {
        this.mchntorderid = mchntorderid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSigntp() {
        return signtp;
    }

    public void setSigntp(String signtp) {
        this.signtp = signtp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    @Override
    public String toString() {
        return "Merchant{" +
                "amt='" + amt + '\'' +
                ", backurl='" + backurl + '\'' +
                ", bankcard='" + bankcard + '\'' +
                ", idno='" + idno + '\'' +
                ", idtype='" + idtype + '\'' +
                ", mchntcd='" + mchntcd + '\'' +
                ", mchntorderid='" + mchntorderid + '\'' +
                ", name='" + name + '\'' +
                ", sign='" + sign + '\'' +
                ", signtp='" + signtp + '\'' +
                ", type='" + type + '\'' +
                ", userid='" + userid + '\'' +
                ", version='" + version + '\'' +
                ", test='" + test + '\'' +
                '}';
    }
}
