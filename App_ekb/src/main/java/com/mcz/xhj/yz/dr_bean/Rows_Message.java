package com.mcz.xhj.yz.dr_bean;

public class Rows_Message {
	 private String addTime ;
	 private String content ;
	 private String id ;
	 private String isRead ;
	 private String title ;
	 private String type ;
	public Rows_Message() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Rows_Message(String addTime, String content, String id, String isRead,
			String title, String type) {
		super();
		this.addTime = addTime;
		this.content = content;
		this.id = id;
		this.isRead = isRead;
		this.title = title;
		this.type = type;
	}

	public String getAddTime() {
		return addTime;
	}
	public void setAddTime(String addTime) {
		this.addTime = addTime;
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
	public String getIsRead() {
		return isRead;
	}
	public void setIsRead(String isRead) {
		this.isRead = isRead;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	 
	 
}
