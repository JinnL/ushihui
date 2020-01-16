package com.mcz.xhj.yz.dr_bean;

public class InvestListBean {

    private String amount;
    private String deadline;
    private String fullName;
    private String id;
    private String leastaAmount;
    private double pert;
    private double rate;
    private double maxAmount;
    private double surplusAmount;
    private String activityRate;
    private String startDate;
    private Integer status;

    private Integer isCash;
    private Integer isDeductible;
    private Integer isInterest;
    private Integer isDouble;
    private Integer isHot;
    private Integer isEgg;
    private boolean isRoundOff = false;//扫尾
    private String tag;//活动标签
    private String type;
    private String accept;
    private String billType;
    private String atid;
    private String maxActivityCoupon;
    private String prizeId;
    private String investSendLabel;

    private boolean realverify; // 用户是否实名
    private int prid; // 预约规则id
    private String name;// 预约规则名称
    private boolean isReservation; // 活动标是否可以预约下一期
    private boolean isInvested;  //是否投资过其他
    private boolean newHandInvested; //是否投资过新手标

    private double maxRate;
    private double minRate;
    private String minDeadline;

    public InvestListBean() {
        super();
        // TODO Auto-generated constructor stub
    }

    public InvestListBean(String amount, String deadline, String fullName,
                          String id, String leastaAmount, double pert, double rate,
                          double maxAmount, String startDate, Integer status, Integer isCash,
                          Integer isDeductible, Integer isInterest, String type,
                          String accept, Integer isDouble, Integer isHot, String billType,
                          String atid, String maxActivityCoupon, Integer isEgg,
                          boolean isReservation, boolean realverify, int prid, String name, String activityRate,
                          boolean isInvested, boolean newHandInvested,String prizeId,String investSendLabel
    ) {
        super();
        this.amount = amount;
        this.deadline = deadline;
        this.fullName = fullName;
        this.id = id;
        this.leastaAmount = leastaAmount;
        this.pert = pert;
        this.rate = rate;
        this.maxAmount = maxAmount;
        this.startDate = startDate;
        this.status = status;
        this.isCash = isCash;
        this.isDeductible = isDeductible;
        this.isInterest = isInterest;
        this.type = type;
        this.accept = accept;
        this.isDouble = isDouble;
        this.isHot = isHot;
        this.billType = billType;
        this.atid = atid;
        this.maxActivityCoupon = maxActivityCoupon;
        this.isEgg = isEgg;
        this.isReservation = isReservation;
        this.realverify = realverify;
        this.prid = prid;
        this.name = name;
        this.pert = pert;
        this.activityRate = activityRate;
        this.isInvested = isInvested;
        this.newHandInvested = newHandInvested;
        this.prizeId = prizeId;
        this.investSendLabel = investSendLabel;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isRoundOff() {
        return isRoundOff;
    }

    public void setIsRoundOff(boolean isRoundOff) {
        this.isRoundOff = isRoundOff;
    }

    public String getMinDeadline() {
        return minDeadline;
    }

    public void setMinDeadline(String minDeadline) {
        this.minDeadline = minDeadline;
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

    public double getSurplusAmount() {
        return surplusAmount;
    }

    public void setSurplusAmount(double surplusAmount) {
        this.surplusAmount = surplusAmount;
    }

    public String getInvestSendLabel() {
        return investSendLabel;
    }

    public void setInvestSendLabel(String investSendLabel) {
        this.investSendLabel = investSendLabel;
    }

    public boolean getIsInvested() {
        return isInvested;
    }

    public String getPrizeId() {
        return prizeId;
    }

    public void setPrizeId(String prizeId) {
        this.prizeId = prizeId;
    }

    public void setIsInvested(boolean invested) {
        isInvested = invested;
    }

    public boolean getNewHandInvested() {
        return newHandInvested;
    }

    public void setNewHandInvested(boolean newHandInvested) {
        this.newHandInvested = newHandInvested;
    }

    public String getActivityRate() {
        return activityRate;
    }

    public void setActivityRate(String activityRate) {
        this.activityRate = activityRate;
    }

    public boolean getRealverify() {
        return realverify;
    }

    public void setRealverify(boolean realverify) {
        this.realverify = realverify;
    }

    public int getPrid() {
        return prid;
    }

    public void setPrid(int prid) {
        this.prid = prid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsReservation() {
        return isReservation;
    }

    public void setIsReservation(boolean isReservation) {
        this.isReservation = isReservation;
    }

    public Integer getIsEgg() {
        return isEgg;
    }

    public void setIsEgg(Integer isEgg) {
        this.isEgg = isEgg;
    }

    public String getMaxActivityCoupon() {
        return maxActivityCoupon;
    }

    public void setMaxActivityCoupon(String maxActivityCoupon) {
        this.maxActivityCoupon = maxActivityCoupon;
    }

    public String getAtid() {
        return atid;
    }

    public void setAtid(String atid) {
        this.atid = atid;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public Integer getIsHot() {
        return isHot;
    }

    public void setIsHot(Integer isHot) {
        this.isHot = isHot;
    }

    public Integer getIsDouble() {
        return isDouble;
    }

    public void setIsDouble(Integer isDouble) {
        this.isDouble = isDouble;
    }

    public String getAccept() {
        return accept;
    }

    public void setAccept(String accept) {
        this.accept = accept;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getIsCash() {
        return isCash;
    }

    public void setIsCash(Integer isCash) {
        this.isCash = isCash;
    }

    public Integer getIsDeductible() {
        return isDeductible;
    }

    public void setIsDeductible(Integer isDeductible) {
        this.isDeductible = isDeductible;
    }

    public Integer getIsInterest() {
        return isInterest;
    }

    public void setIsInterest(Integer isInterest) {
        this.isInterest = isInterest;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getLeastaAmount() {
        return leastaAmount;
    }

    public void setLeastaAmount(String leastaAmount) {
        this.leastaAmount = leastaAmount;
    }

    public double getPert() {
        return pert;
    }

    public void setPert(double pert) {
        this.pert = pert;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

}
