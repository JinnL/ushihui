package com.ekabao.oil.bean;

public class Add_Bean {
	private String arti_id;
	private String artiId;
	private String create_time;
	private long createTime;
	private String ishead;
	private String sort_rank;
	private String source;
	private String summaryContents;
	private String title;
	private String writer;
	public Add_Bean(String arti_id, String create_time, String ishead,
                    String sort_rank, String source, String summaryContents,
                    String title, String writer) {
		super();
		this.arti_id = arti_id;
		this.create_time = create_time;
		this.ishead = ishead;
		this.sort_rank = sort_rank;
		this.source = source;
		this.summaryContents = summaryContents;
		this.title = title;
		this.writer = writer;
	}
	public Add_Bean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getArtiId() {
		return artiId;
	}

	public void setArtiId(String artiId) {
		this.artiId = artiId;
	}

	public String getArti_id() {
		return arti_id;
	}
	public void setArti_id(String arti_id) {
		this.arti_id = arti_id;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getIshead() {
		return ishead;
	}
	public void setIshead(String ishead) {
		this.ishead = ishead;
	}
	public String getSort_rank() {
		return sort_rank;
	}
	public void setSort_rank(String sort_rank) {
		this.sort_rank = sort_rank;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getSummaryContents() {
		return summaryContents;
	}
	public void setSummaryContents(String summaryContents) {
		this.summaryContents = summaryContents;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
}
