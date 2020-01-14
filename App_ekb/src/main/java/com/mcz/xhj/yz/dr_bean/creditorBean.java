package com.mcz.xhj.yz.dr_bean;

public class creditorBean {

	private String title ;
	private String content ;
	private String id ;
	private String pid ;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public creditorBean(String title, String content, String id, String pid) {
		super();
		this.title = title;
		this.content = content;
		this.id = id;
		this.pid = pid;
	}
	public creditorBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	

	
	
}
