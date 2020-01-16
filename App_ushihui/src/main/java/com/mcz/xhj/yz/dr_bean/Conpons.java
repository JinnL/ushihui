package com.mcz.xhj.yz.dr_bean;

import java.io.Serializable;

public class Conpons implements Serializable{
	private String id;
	private String uid;
	private String type;
	private String amount;
	private String addTime;
	private String destroyTime;
	private String isUsed;
	private String useTime;
	private String remark;
	private String profitAmount;
	public Conpons() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Conpons(String id, String uid, String type, String amount,
			String addTime, String destroyTime, String isUsed, String useTime,
			String remark, String profitAmount) {
		super();
		this.id = id;
		this.uid = uid;
		this.type = type;
		this.amount = amount;
		this.addTime = addTime;
		this.destroyTime = destroyTime;
		this.isUsed = isUsed;
		this.useTime = useTime;
		this.remark = remark;
		this.profitAmount = profitAmount;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getAddTime() {
		return addTime;
	}
	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	public String getDestroyTime() {
		return destroyTime;
	}
	public void setDestroyTime(String destroyTime) {
		this.destroyTime = destroyTime;
	}
	public String getIsUsed() {
		return isUsed;
	}
	public void setIsUsed(String isUsed) {
		this.isUsed = isUsed;
	}
	public String getUseTime() {
		return useTime;
	}
	public void setUseTime(String useTime) {
		this.useTime = useTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getProfitAmount() {
		return profitAmount;
	}
	public void setProfitAmount(String profitAmount) {
		this.profitAmount = profitAmount;
	}
	
	
}
