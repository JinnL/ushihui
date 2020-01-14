package com.mcz.xhj.yz.dr_bean;

public class Offline_Bean {
	private String titleList;
	private String h5ListBanner;
	private String id;
	public Offline_Bean() {
	}

	public Offline_Bean(String titleList, String h5ListBanner, String id) {
		this.titleList = titleList;
		this.h5ListBanner = h5ListBanner;
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitleList() {
		return titleList;
	}

	public void setTitleList(String titleList) {
		this.titleList = titleList;
	}

	public String getH5ListBanner() {
		return h5ListBanner;
	}

	public void setH5ListBanner(String h5ListBanner) {
		this.h5ListBanner = h5ListBanner;
	}
}
