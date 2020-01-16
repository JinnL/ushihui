package com.mcz.xhj.yz.dr_bean;

public class bean_Detail_Info {

	private String amount;
	private String tenderAccountSum;//总投资金额
	private String tender_money_distribution;//前5分得的奖励金额
	private String realName;
	private String investTime;
	private String sex ;
	private String mobilephone;
	private String idCards ;
	private Integer joinType ;

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	private Integer uid ;

//	private String bigUrl;
//	private String name;
//	private String showShort;

	
	
	

public bean_Detail_Info(String amount, String realName, String investTime,
		String sex, String mobilephone, String idCards, Integer joinType) {
	super();
	this.amount = amount;
	this.realName = realName;
	this.investTime = investTime;
	this.sex = sex;
	this.mobilephone = mobilephone;
	this.idCards = idCards;
	this.joinType = joinType;
}



//	public bean_Detail_Info(String bigUrl, String name) {
//		super();
//		this.bigUrl = bigUrl;
//		this.name = name;
//	}


	public String getTenderAccountSum() {
		return tenderAccountSum;
	}

	public void setTenderAccountSum(String tenderAccountSum) {
		this.tenderAccountSum = tenderAccountSum;
	}

	public String getTender_money_distribution() {
		return tender_money_distribution;
	}

	public void setTender_money_distribution(String tender_money_distribution) {
		this.tender_money_distribution = tender_money_distribution;
	}

	public Integer getJoinType() {
		return joinType;
	}

	public void setJoinType(Integer joinType) {
		this.joinType = joinType;
	}

	public String getIdCards() {
	return idCards;
}



public void setIdCards(String idCards) {
	this.idCards = idCards;
}



	public bean_Detail_Info() {
		super();
		// TODO Auto-generated constructor stub
	}



	public String getMobilephone() {
		return mobilephone;
	}



	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}



	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getInvestTime() {
		return investTime;
	}

	public void setInvestTime(String investTime) {
		this.investTime = investTime;
	}



	public String getSex() {
		return sex;
	}



	public void setSex(String sex) {
		this.sex = sex;
	}

}
