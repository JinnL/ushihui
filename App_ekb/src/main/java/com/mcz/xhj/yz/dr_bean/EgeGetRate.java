package com.mcz.xhj.yz.dr_bean;

public class EgeGetRate {
	private String pid;
	private String raisedRates;
	private String uid;
	private String fullName;
	private String type;
	public EgeGetRate() {
		super();
		// TODO Auto-generated constructor stub
	}
	public EgeGetRate(String pid, String raisedRates, String uid,
			String fullName, String type) {
		super();
		this.pid = pid;
		this.raisedRates = raisedRates;
		this.uid = uid;
		this.fullName = fullName;
		this.type = type;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getRaisedRates() {
		return raisedRates;
	}
	public void setRaisedRates(String raisedRates) {
		this.raisedRates = raisedRates;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
}
