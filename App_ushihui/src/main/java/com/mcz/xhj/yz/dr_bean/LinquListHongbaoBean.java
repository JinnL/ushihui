package com.mcz.xhj.yz.dr_bean;

public class LinquListHongbaoBean {
	private int deadline;
	private double amount;
	private double investAmount;
	private double rate;
	private double profitAmount;
	private double activityRate;

	public LinquListHongbaoBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LinquListHongbaoBean(int deadline, double amount, double investAmount, double rate, double profitAmount,double activityRate) {
		this.deadline = deadline;
		this.amount = amount;
		this.investAmount = investAmount;
		this.rate = rate;
		this.profitAmount = profitAmount;
		this.activityRate = activityRate;
	}

	public double getActivityRate() {
		return activityRate;
	}

	public void setActivityRate(double activityRate) {
		this.activityRate = activityRate;
	}

	public int getDeadline() {
		return deadline;
	}

	public void setDeadline(int deadline) {
		this.deadline = deadline;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getInvestAmount() {
		return investAmount;
	}

	public void setInvestAmount(double investAmount) {
		this.investAmount = investAmount;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public double getProfitAmount() {
		return profitAmount;
	}

	public void setProfitAmount(double profitAmount) {
		this.profitAmount = profitAmount;
	}
}
