package com.ekabao.oil.bean;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/11/6 0006.
 */

public class FundsBean {

    /**
     * {
     "balance": 9933320.00,
     "carryCount": 0.00,
     "crushCount": 0.00,
     "freeze": 47000.00,
     "investAmount": 20000.00,
     "investProfit": 320.00,
     "spreadProfit": 0.00,
     "uid": 57,
     "winterest": 155.00,
     "wpenalty": 0.00,
     "wprincipal": 20000.00
     }
     `crushcount`  '冲值总额',
     `carrycount`  '提现总额',
     `investAmount`  '出借总额',
     `balance`  '可用余额',
     `freeze`  '冻结金额',
     `wprincipal`  '待收本金',
     `winterest`  '待收利息',
     `wpenalty`  '待收罚息 (改为代收补偿金)',
     `investProfit`  '出借获得收益',
     `spreadProfit`  '推广收益',
     `fuiou_crushcount`  '恒丰充值总额',
     `fuiou_carrycount`  '恒丰提现总额',
     `fuiou_investAmount`  '恒丰出借总额',
     `fuiou_balance`  '恒丰可用余额',
     `fuiou_freeze`  '恒丰冻结金额',
     `fuiou_wprincipal`  '恒丰待收本金',
     `fuiou_winterest`  '恒丰待收利息',
     `fuiou_wpenalty`  '代收补偿金',
     `fuiou_investProfit`  '恒丰出借获得收益',
     `fuiou_spreadProfit`  '恒丰推广收益',

    '会员资金表';

     * */

    private BigDecimal balance; //可用余额
    private BigDecimal carryCount; //提现总额
    private BigDecimal crushCount; //冲值总额
    private BigDecimal freeze; //冻结金额
    private BigDecimal investAmount; //出借总额
    private BigDecimal wprincipal; //待收本金
    private BigDecimal winterest; //待收利息
    /**
     * balance : 9933320.0
     * carryCount : 0.0
     * crushCount : 0.0
     * freeze : 47000.0
     * investAmount : 20000.0
     * investProfit : 320.0
     * spreadProfit : 0.0
     * uid : 57
     * winterest : 155.0
     * wpenalty : 0.0
     * wprincipal : 20000.0
     */

    private double investProfit; //恒丰出借获得收益
    private double spreadProfit; //恒丰推广收益
    private int uid;



    public FundsBean() {
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getCarryCount() {
        return carryCount;
    }

    public void setCarryCount(BigDecimal carryCount) {
        this.carryCount = carryCount;
    }

    public BigDecimal getCrushCount() {
        return crushCount;
    }

    public void setCrushCount(BigDecimal crushCount) {
        this.crushCount = crushCount;
    }

    public BigDecimal getFreeze() {
        return freeze;
    }

    public void setFreeze(BigDecimal freeze) {
        this.freeze = freeze;
    }

    public BigDecimal getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(BigDecimal investAmount) {
        this.investAmount = investAmount;
    }

    public BigDecimal getWprincipal() {
        return wprincipal;
    }

    public void setWprincipal(BigDecimal wprincipal) {
        this.wprincipal = wprincipal;
    }

    public BigDecimal getWinterest() {
        return winterest;
    }

    public void setWinterest(BigDecimal winterest) {
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

    public double getInvestProfit() {
        return investProfit;
    }

    public void setInvestProfit(double investProfit) {
        this.investProfit = investProfit;
    }

    public double getSpreadProfit() {
        return spreadProfit;
    }

    public void setSpreadProfit(double spreadProfit) {
        this.spreadProfit = spreadProfit;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }


}
