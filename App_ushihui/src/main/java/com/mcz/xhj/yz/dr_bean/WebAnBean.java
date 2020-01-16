package com.mcz.xhj.yz.dr_bean;

public class WebAnBean {
	
	private String artiId;  //文章标识号
	private String title;  //标题
	private long createTime; //创建时间

	public WebAnBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public WebAnBean(String artiId,String title,long createTime){
		super();
		this.artiId = artiId;
		this.title = title;
		this.createTime = createTime;
	}

	public String getArtiId() {
		return artiId;
	}

	public void setArtiId(String artiId) {
		this.artiId = artiId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	
}
