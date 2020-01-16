package com.mcz.xhj.yz.dr_bean;

public class ActivityBean {
	private String activityName;
	private String activityPrizes;
	private double amount;
	private Integer investId;
	private String luckCodes;
	private Integer pid;
	private String prizeCode;
	private Integer prizeStatus;
	private String productName;
	public ActivityBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ActivityBean(String activityName, String activityPrizes,
			double amount, Integer investId, String luckCodes, Integer pid,
			String prizeCode, Integer prizeStatus, String productName) {
		super();
		this.activityName = activityName;
		this.activityPrizes = activityPrizes;
		this.amount = amount;
		this.investId = investId;
		this.luckCodes = luckCodes;
		this.pid = pid;
		this.prizeCode = prizeCode;
		this.prizeStatus = prizeStatus;
		this.productName = productName;
	}
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public String getActivityPrizes() {
		return activityPrizes;
	}
	public void setActivityPrizes(String activityPrizes) {
		this.activityPrizes = activityPrizes;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public Integer getInvestId() {
		return investId;
	}
	public void setInvestId(Integer investId) {
		this.investId = investId;
	}
	public String getLuckCodes() {
		return luckCodes;
	}
	public void setLuckCodes(String luckCodes) {
		this.luckCodes = luckCodes;
	}
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	public String getPrizeCode() {
		return prizeCode;
	}
	public void setPrizeCode(String prizeCode) {
		this.prizeCode = prizeCode;
	}
	public Integer getPrizeStatus() {
		return prizeStatus;
	}
	public void setPrizeStatus(Integer prizeStatus) {
		this.prizeStatus = prizeStatus;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	
}
