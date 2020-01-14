package com.mcz.xhj.yz.dr_bean;

public class bean_Detail_Info_pic {

	private String bigUrl;
	private String name;
	private String showShort;
	private Integer type;

	public bean_Detail_Info_pic(String bigUrl, String name, String showShort,Integer type) {
		super();
		this.bigUrl = bigUrl;
		this.name = name;
		this.showShort = showShort;
		this.type = type;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getBigUrl() {
		return bigUrl;
	}

	public void setBigUrl(String bigUrl) {
		this.bigUrl = bigUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShowShort() {
		return showShort;
	}

	public void setShowShort(String showShort) {
		this.showShort = showShort;
	}

	public bean_Detail_Info_pic() {
		super();
		// TODO Auto-generated constructor stub
	}

}
