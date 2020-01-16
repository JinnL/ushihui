package com.mcz.xhj.yz.dr_bean;

public class BackRecond {
	private String index;
	private String shouldPrincipal;
	private Integer status;
	private double shouldSum;
	private String residualPrincipal;
	private String date;
	private String shouldInterest;
	public BackRecond() {
		super();
		// TODO Auto-generated constructor stub
	}
	public BackRecond(String index, String shouldPrincipal, Integer status,
			double shouldSum, String residualPrincipal, String date,
			String shouldInterest) {
		super();
		this.index = index;
		this.shouldPrincipal = shouldPrincipal;
		this.status = status;
		this.shouldSum = shouldSum;
		this.residualPrincipal = residualPrincipal;
		this.date = date;
		this.shouldInterest = shouldInterest;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public String getShouldPrincipal() {
		return shouldPrincipal;
	}
	public void setShouldPrincipal(String shouldPrincipal) {
		this.shouldPrincipal = shouldPrincipal;
	}
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public double getShouldSum() {
		return shouldSum;
	}
	public void setShouldSum(double shouldSum) {
		this.shouldSum = shouldSum;
	}
	public String getResidualPrincipal() {
		return residualPrincipal;
	}
	public void setResidualPrincipal(String residualPrincipal) {
		this.residualPrincipal = residualPrincipal;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getShouldInterest() {
		return shouldInterest;
	}
	public void setShouldInterest(String shouldInterest) {
		this.shouldInterest = shouldInterest;
	}
	
	
}
