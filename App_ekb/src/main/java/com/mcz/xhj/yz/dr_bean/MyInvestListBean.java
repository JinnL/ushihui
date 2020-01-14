package com.mcz.xhj.yz.dr_bean;

import java.io.Serializable;

public class MyInvestListBean implements Serializable{
     private double amount;//投资金额
     private String investTime;//投资日期
     private double factAmount;//本金
     private double factInterest;//利息
     private String deadline;//投资期限
     private double rate;//年化
     private String fullName;//名称
     private Integer repayType;//还款方式
     private String expireDate;//还款日期
     private String establish;//成立日期
	 private String interestTime;//计息日期
	 private double expireInterest;//到期收益
     private String status;//类型
     private double specialRate;//特殊加息
     private Integer uid;//用户id
     private Integer id;//投资id
     private Integer pid;//产品id
     private Integer type;//产品类型 1-新手标 2-普通标 5-体验标
     private Integer couponType;//红包类型
     private Integer prePid;//上一个产品ID
     private Integer sid;//产品对应的标的ID
     private double couponAmount;//红包金额
     private double couponRate;//红包利率
     private double multiple;//翻倍
     private Integer continuePeriod;//续投
     private String activityRate;//红包加息
	 private String productStatus; //产品状态 //0=投资中 1=待还款 2=投资失败 3=已还款
	 private int productType;//0-普通标 1-投即送 2-送iPhone7
	 private String prizeName;
	 private String periodLabel;
	 private String isPerfect;//1 = 已完善，0 = 未完善
	 private Integer prizeType;//1 = 50元话费，0 = 实物
	public MyInvestListBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public MyInvestListBean(double amount, String investTime,
			double factAmount, double factInterest, String deadline,String prizeName,
			double rate, String fullName, Integer repayType, String expireDate,int productType,
			String status, Integer uid, Integer id, Integer pid,String productStatus,String interestTime,double expireInterest,
			Integer couponType, double couponAmount, double couponRate,double multiple,Integer prizeType,String isPerfect,
			Integer prePid,Integer sid,String establish,Integer continuePeriod,String activityRate,Integer type,double specialRate,String periodLabel) {
		super();
		this.amount = amount;
		this.investTime = investTime;
		this.factAmount = factAmount;
		this.factInterest = factInterest;
		this.deadline = deadline;
		this.rate = rate;
		this.fullName = fullName;
		this.repayType = repayType;
		this.expireDate = expireDate;
		this.status = status;
		this.uid = uid;
		this.id = id;
		this.pid = pid;
		this.couponType = couponType;
		this.couponAmount = couponAmount;
		this.couponRate = couponRate;
		this.multiple = multiple;
		this.prePid = prePid;
		this.sid = sid;
		this.establish = establish;
		this.continuePeriod = continuePeriod;
		this.expireInterest = expireInterest;
		this.activityRate = activityRate;
		this.type = type;
		this.specialRate = specialRate;
		this.productStatus = productStatus;
		this.productType = productType;
		this.prizeName = prizeName;
		this.periodLabel = periodLabel;
		this.interestTime = interestTime;
		this.prizeType = prizeType;
		this.isPerfect = isPerfect;
	}

	public String getIsPerfect() {
		return isPerfect;
	}

	public void setIsPerfect(String isPerfect) {
		this.isPerfect = isPerfect;
	}

	public Integer getPrizeType() {
		return prizeType;
	}

	public void setPrizeType(Integer prizeType) {
		this.prizeType = prizeType;
	}

	public double getExpireInterest() {
		return expireInterest;
	}

	public void setExpireInterest(double expireInterest) {
		this.expireInterest = expireInterest;
	}

	public String getInterestTime() {
		return interestTime;
	}

	public void setInterestTime(String interestTime) {
		this.interestTime = interestTime;
	}

	public String getPeriodLabel() {
		return periodLabel;
	}

	public void setPeriodLabel(String periodLabel) {
		this.periodLabel = periodLabel;
	}

	public String getPrizeName() {
		return prizeName;
	}

	public void setPrizeName(String prizeName) {
		this.prizeName = prizeName;
	}

	public int getProductType() {
		return productType;
	}

	public void setProductType(int productType) {
		this.productType = productType;
	}

	public String getProductStatus() {
		return productStatus;
	}

	public void setProductStatus(String productStatus) {
		this.productStatus = productStatus;
	}

	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getActivityRate() {
		return activityRate;
	}
	public double getSpecialRate() {
		return specialRate;
	}
	public void setSpecialRate(double specialRate) {
		this.specialRate = specialRate;
	}

	public void setActivityRate(String activityRate) {
		this.activityRate = activityRate;
	}

	public Integer getContinuePeriod() {
		return continuePeriod;
	}

	public void setContinuePeriod(Integer continuePeriod) {
		this.continuePeriod = continuePeriod;
	}

	public String getEstablish() {
		return establish;
	}

	public void setEstablish(String establish) {
		this.establish = establish;
	}

	public Integer getPrePid() {
		return prePid;
	}

	public void setPrePid(Integer prePid) {
		this.prePid = prePid;
	}

	public Integer getSid() {
		return sid;
	}

	public void setSid(Integer sid) {
		this.sid = sid;
	}

	public double getMultiple() {
		return multiple;
	}

	public void setMultiple(double multiple) {
		this.multiple = multiple;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getInvestTime() {
		return investTime;
	}
	public void setInvestTime(String investTime) {
		this.investTime = investTime;
	}
	public double getFactAmount() {
		return factAmount;
	}
	public void setFactAmount(double factAmount) {
		this.factAmount = factAmount;
	}
	public double getFactInterest() {
		return factInterest;
	}
	public void setFactInterest(double factInterest) {
		this.factInterest = factInterest;
	}
	public String getDeadline() {
		return deadline;
	}
	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public Integer getRepayType() {
		return repayType;
	}
	public void setRepayType(Integer repayType) {
		this.repayType = repayType;
	}
	public String getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getCouponType() {
		return couponType;
	}
	public void setCouponType(Integer couponType) {
		this.couponType = couponType;
	}
	public double getCouponAmount() {
		return couponAmount;
	}
	public void setCouponAmount(double couponAmount) {
		this.couponAmount = couponAmount;
	}
	public double getCouponRate() {
		return couponRate;
	}
	public void setCouponRate(double couponRate) {
		this.couponRate = couponRate;
	}
	
     
     
}
