package com.mcz.xhj.yz.dr_bean;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/11/6 0006.
 */

public class FundsBean {

    private BigDecimal balance;
    private BigDecimal carryCount;
    private BigDecimal crushCount;
    private BigDecimal freeze;
    private BigDecimal investAmount;
    private BigDecimal wprincipal;
    private BigDecimal winterest;

    public FundsBean() {
    }

    public java.math.BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(java.math.BigDecimal balance) {
        this.balance = balance;
    }

    public java.math.BigDecimal getCarryCount() {
        return carryCount;
    }

    public void setCarryCount(java.math.BigDecimal carryCount) {
        this.carryCount = carryCount;
    }

    public java.math.BigDecimal getCrushCount() {
        return crushCount;
    }

    public void setCrushCount(java.math.BigDecimal crushCount) {
        this.crushCount = crushCount;
    }

    public java.math.BigDecimal getFreeze() {
        return freeze;
    }

    public void setFreeze(java.math.BigDecimal freeze) {
        this.freeze = freeze;
    }

    public java.math.BigDecimal getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(java.math.BigDecimal investAmount) {
        this.investAmount = investAmount;
    }

    public java.math.BigDecimal getWprincipal() {
        return wprincipal;
    }

    public void setWprincipal(java.math.BigDecimal wprincipal) {
        this.wprincipal = wprincipal;
    }

    public java.math.BigDecimal getWinterest() {
        return winterest;
    }

    public void setWinterest(java.math.BigDecimal winterest) {
        this.winterest = winterest;
    }

    @Override
    public String toString() {
        return "FundsBean{" +
                "balance=" + balance +
                ", carryCount=" + carryCount +
                ", crushCount=" + crushCount +
                ", freeze=" + freeze +
                ", investAmount=" + investAmount +
                ", wprincipal=" + wprincipal +
                ", winterest=" + winterest +
                '}';
    }
}
