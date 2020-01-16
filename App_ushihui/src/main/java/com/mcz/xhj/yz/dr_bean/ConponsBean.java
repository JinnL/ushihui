package com.mcz.xhj.yz.dr_bean;

import java.io.Serializable;

public class ConponsBean implements Serializable {
	private String id;
    private double amount;
    private double enableAmount;
    private String expireDate;
    private String remark;
    private Integer type;
    private double raisedRates;
    private double multiple;
    private String source ;
    private Integer productDeadline ;
    private Integer status ;
    private String pid ;
    private String fullName;

    
	public ConponsBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ConponsBean(String id, double amount, double enableAmount,
			String expireDate, String remark, Integer type, double raisedRates,
			double multiple, String source,Integer productDeadline,String pid,String fullName,Integer status) {
		super();
		this.id = id;
		this.amount = amount;
		this.enableAmount = enableAmount;
		this.expireDate = expireDate;
		this.remark = remark;
		this.type = type;
		this.raisedRates = raisedRates;
		this.multiple = multiple;
		this.source = source;
		this.productDeadline = productDeadline;
		this.pid = pid;
		this.fullName = fullName;
		this.status = status;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public Integer getProductDeadline() {
		return productDeadline;
	}
	public void setProductDeadline(Integer productDeadline) {
		this.productDeadline = productDeadline;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public double getMultiple() {
		return multiple;
	}
	public void setMultiple(double multiple) {
		this.multiple = multiple;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public double getRaisedRates() {
		return raisedRates;
	}
	public void setRaisedRates(double raisedRates) {
		this.raisedRates = raisedRates;
	}
    
    
}
