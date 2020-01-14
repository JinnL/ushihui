package com.mcz.xhj.yz.dr_bean;

public class RedListBean {
	private double amount;
    private double enableAmount;
    private String expireDate;
    private String id;
    private Integer productDeadline;
	public RedListBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public RedListBean(double amount, double enableAmount, String expireDate,
			String id, Integer productDeadline) {
		super();
		this.amount = amount;
		this.enableAmount = enableAmount;
		this.expireDate = expireDate;
		this.id = id;
		this.productDeadline = productDeadline;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public double getEnableAmount() {
		return enableAmount;
	}
	public void setEnableAmount(double enableAmount) {
		this.enableAmount = enableAmount;
	}
	public String getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getProductDeadline() {
		return productDeadline;
	}
	public void setProductDeadline(Integer productDeadline) {
		this.productDeadline = productDeadline;
	}
    
    
}
