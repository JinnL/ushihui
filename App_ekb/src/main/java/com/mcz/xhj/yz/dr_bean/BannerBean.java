package com.mcz.xhj.yz.dr_bean;

public class BannerBean {
	private String imgUrl;
	private String location;
	private String title;
	public BannerBean() {
		super();
	}
	
	public BannerBean(String imgUrl, String location, String title
			) {
		super();
		this.imgUrl = imgUrl;
		this.location = location;
		this.title = title;
	}



	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "BannerBean{" +
				"imgUrl='" + imgUrl + '\'' +
				", location='" + location + '\'' +
				", title='" + title + '\'' +
				'}';
	}
}
