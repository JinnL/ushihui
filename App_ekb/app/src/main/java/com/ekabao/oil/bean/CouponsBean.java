package com.ekabao.oil.bean;

import java.io.Serializable;

public class CouponsBean implements Serializable {


	/**
	 * activityId : 71
	 * addtime : 1531723933000
	 * amount : 108.0
	 * code : HB-20180716143634
	 * enableAmount : 1000.0
	 * expireDate : 1535093007000
	 * id : 822
	 * name : 注册红包108
	 * productDeadline : 90
	 * profitAmount : 0.0
	 * remark : 系统发送
	 * source : 0
	 * status : 0
	 * type : 1
	 * uid : 5
	 * usedTime : 1531724002000
	 */

	private int activityId;
	private long addtime;
	private double amount;
	private String code;
	private double enableAmount;
	private long expireDate;
	private int id;
	private String name;
	private int productDeadline;
	private double profitAmount;
	private String remark;
	private int source;
	private int status;
	private int type;
	private int uid;
	private long usedTime;

	/**
	 * enableAmount : 300000.0
	 * maxAmount : 1000000.0
	 * profitAmount : 0.0
	 * raisedRates : 20.35
	 * userKey : 1
	 */


	private double maxAmount;
	private double raisedRates;
	private int userKey;
	/**
	 * enableAmount : 2000.0
	 * multiple : 3.0
	 * profitAmount : 0.0
	 */

	private double multiple;

	public int getActivityId() {
		return activityId;
	}

	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}

	public long getAddtime() {
		return addtime;
	}

	public void setAddtime(long addtime) {
		this.addtime = addtime;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public double getEnableAmount() {
		return enableAmount;
	}

	public void setEnableAmount(double enableAmount) {
		this.enableAmount = enableAmount;
	}

	public long getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(long expireDate) {
		this.expireDate = expireDate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getProductDeadline() {
		return productDeadline;
	}

	public void setProductDeadline(int productDeadline) {
		this.productDeadline = productDeadline;
	}

	public double getProfitAmount() {
		return profitAmount;
	}

	public void setProfitAmount(double profitAmount) {
		this.profitAmount = profitAmount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public long getUsedTime() {
		return usedTime;
	}

	public void setUsedTime(long usedTime) {
		this.usedTime = usedTime;
	}



	public double getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(double maxAmount) {
		this.maxAmount = maxAmount;
	}


	public double getRaisedRates() {
		return raisedRates;
	}

	public void setRaisedRates(double raisedRates) {
		this.raisedRates = raisedRates;
	}

	public int getUserKey() {
		return userKey;
	}

	public void setUserKey(int userKey) {
		this.userKey = userKey;
	}

	public double getMultiple() {
		return multiple;
	}

	public void setMultiple(double multiple) {
		this.multiple = multiple;
	}
}
