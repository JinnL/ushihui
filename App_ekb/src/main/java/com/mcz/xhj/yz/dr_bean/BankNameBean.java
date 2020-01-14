package com.mcz.xhj.yz.dr_bean;

public class BankNameBean {
	private String id;
    private double singleQuota;//单笔限额
    private double dayQuota;//单日限额
    private String bankName;

	public BankNameBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BankNameBean(String id, double singleQuota, double dayQuota, String bankName) {
		this.id = id;
		this.singleQuota = singleQuota;
		this.dayQuota = dayQuota;
		this.bankName = bankName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public double getSingleQuota() {
		return singleQuota;
	}

	public void setSingleQuota(double singleQuota) {
		this.singleQuota = singleQuota;
	}

	public double getDayQuota() {
		return dayQuota;
	}

	public void setDayQuota(double dayQuota) {
		this.dayQuota = dayQuota;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
}
