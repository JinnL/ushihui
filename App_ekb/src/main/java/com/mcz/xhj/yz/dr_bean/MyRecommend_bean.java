package com.mcz.xhj.yz.dr_bean;

public class MyRecommend_bean {
    private String mobilePhone;
    private String realName;
    private String regTime;
    private String isInvest;

    public MyRecommend_bean() {
        super();
        // TODO Auto-generated constructor stub
    }

    public MyRecommend_bean(String mobilePhone, String realName,
                            String regTime, String isInvest) {
        super();
        this.mobilePhone = mobilePhone;
        this.realName = realName;
        this.regTime = regTime;
        this.isInvest = isInvest;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRegTime() {
        return regTime;
    }

    public void setRegTime(String regTime) {
        this.regTime = regTime;
    }

    public String getIsInvest() {
        return isInvest;
    }

    public void setIsInvest(String isInvest) {
        this.isInvest = isInvest;
    }


}
