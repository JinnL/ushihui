package com.mcz.xhj.yz.dr_bean;

public class WinnerBean {
	private String activityPeriods;
	private String id;
	private String isUplod;
	private String luckCodes;
	private String piid;
	private String prizeCode;
	private String prizeContent;
	private String prizeImgUrl;
	private String prizeMobile;
	private String prizeStatus;
	private String prizeHeadPhoto;
	private String prizeVideoLink;
	public WinnerBean() {
		super();
	}
	public WinnerBean(String activityPeriods, String id, String isUplod,
			String luckCodes, String piid, String prizeCode,
			String prizeContent, String prizeImgUrl, String prizeMobile,
			String prizeStatus,String prizeHeadPhoto,String prizeVideoLink) {
		super();
		this.activityPeriods = activityPeriods;
		this.id = id;
		this.isUplod = isUplod;
		this.luckCodes = luckCodes;
		this.piid = piid;
		this.prizeCode = prizeCode;
		this.prizeContent = prizeContent;
		this.prizeImgUrl = prizeImgUrl;
		this.prizeMobile = prizeMobile;
		this.prizeStatus = prizeStatus;
		this.prizeHeadPhoto = prizeHeadPhoto;
		this.prizeVideoLink = prizeVideoLink;
	}
	
	
	public String getPrizeHeadPhoto() {
		return prizeHeadPhoto;
	}
	public void setPrizeHeadPhoto(String prizeHeadPhoto) {
		this.prizeHeadPhoto = prizeHeadPhoto;
	}
	public String getPrizeVideoLink() {
		return prizeVideoLink;
	}
	public void setPrizeVideoLink(String prizeVideoLink) {
		this.prizeVideoLink = prizeVideoLink;
	}
	public String getActivityPeriods() {
		return activityPeriods;
	}
	public void setActivityPeriods(String activityPeriods) {
		this.activityPeriods = activityPeriods;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIsUplod() {
		return isUplod;
	}
	public void setIsUplod(String isUplod) {
		this.isUplod = isUplod;
	}
	public String getLuckCodes() {
		return luckCodes;
	}
	public void setLuckCodes(String luckCodes) {
		this.luckCodes = luckCodes;
	}
	public String getPiid() {
		return piid;
	}
	public void setPiid(String piid) {
		this.piid = piid;
	}
	public String getPrizeCode() {
		return prizeCode;
	}
	public void setPrizeCode(String prizeCode) {
		this.prizeCode = prizeCode;
	}
	public String getPrizeContent() {
		return prizeContent;
	}
	public void setPrizeContent(String prizeContent) {
		this.prizeContent = prizeContent;
	}
	public String getPrizeMobile() {
		return prizeMobile;
	}
	public void setPrizeMobile(String prizeMobile) {
		this.prizeMobile = prizeMobile;
	}
	public String getPrizeStatus() {
		return prizeStatus;
	}
	public void setPrizeStatus(String prizeStatus) {
		this.prizeStatus = prizeStatus;
	}
	public String getPrizeImgUrl() {
		return prizeImgUrl;
	}
	public void setPrizeImgUrl(String prizeImgUrl) {
		this.prizeImgUrl = prizeImgUrl;
	}
	
}
