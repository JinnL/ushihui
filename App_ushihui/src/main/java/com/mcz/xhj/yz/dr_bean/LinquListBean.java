package com.mcz.xhj.yz.dr_bean;

public class LinquListBean {
	private String mobilePhone;
	private String amount;

	public LinquListBean() {
		super();
	}

	public LinquListBean(String mobilePhone, String amount) {
		this.mobilePhone = mobilePhone;
		this.amount = amount;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}
}
