package com.mcz.xhj.yz.dr_bean;

public class Ad_bean {
	private String id;
    private String imgUrl;
    private String remark;
    private String title;
    private String status;
    private String code;
    private String sort;
    private String location;
    private String color;
	public Ad_bean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Ad_bean(String id, String imgUrl, String remark, String title,
			String status, String code, String sort, String location,
			String color) {
		super();
		this.id = id;
		this.imgUrl = imgUrl;
		this.remark = remark;
		this.title = title;
		this.status = status;
		this.code = code;
		this.sort = sort;
		this.location = location;
		this.color = color;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
    
    
}
