package com.mcz.xhj.yz.dr_bean;

public class HuikuanRedBean {
	private String mobilePhone;
	private String amount;
	private String addtime;

	public HuikuanRedBean() {
		super();
	}

	public HuikuanRedBean(String mobilePhone, String amount,String addtime) {
		this.mobilePhone = mobilePhone;
		this.amount = amount;
		this.addtime = addtime;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
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
