package com.ekabao.oil.bean;

public class FriendBean {
	private Integer id;
	private Integer status;
	private String activityDate;
	private String appPic;
	private String appUrl;
	private Integer isTop;
	private String title;
	public FriendBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FriendBean(Integer id, Integer status, String activityDate, String appPic, String appUrl, Integer isTop, String title) {
		this.id = id;
		this.status = status;
		this.activityDate = activityDate;
		this.appPic = appPic;
		this.appUrl = appUrl;
		this.isTop = isTop;
		this.title = title;
	}

	public String getActivityDate() {
		return activityDate;
	}

	public void setActivityDate(String activityDate) {
		this.activityDate = activityDate;
	}

	public String getAppPic() {
		return appPic;
	}

	public void setAppPic(String appPic) {
		this.appPic = appPic;
	}

	public String getAppUrl() {
		return appUrl;
	}

	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}

	public Integer getIsTop() {
		return isTop;
	}

	public void setIsTop(Integer isTop) {
		this.isTop = isTop;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
}
