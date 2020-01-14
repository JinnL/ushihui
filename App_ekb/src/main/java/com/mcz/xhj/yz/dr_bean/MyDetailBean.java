package com.mcz.xhj.yz.dr_bean;

public class MyDetailBean {
	private String addTime;
    private double amount;
    private Integer status;
    private Integer tradeType;
    private Integer type;
	public MyDetailBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public MyDetailBean(String addTime, double amount, Integer status,
			Integer tradeType, Integer type) {
		super();
		this.addTime = addTime;
		this.amount = amount;
		this.status = status;
		this.tradeType = tradeType;
		this.type = type;
	}
	public String getAddTime() {
		return addTime;
	}
	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getTradeType() {
		return tradeType;
	}
	public void setTradeType(Integer tradeType) {
		this.tradeType = tradeType;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
    
}
