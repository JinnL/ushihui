package com.mcz.xhj.yz.dr_bean;

/**
 * Created by zhulang on 2017/8/28.
 * 投资排行的javabean
 */

public class RankItemBean {
    private String rankNumber;
    private String name;
    private String amount;
    private String time;

    public String getRankNumber() {
        return rankNumber;
    }

    public void setRankNumber(String rankNumber) {
        this.rankNumber = rankNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "RankItemBean{" +
                "rankNumber='" + rankNumber + '\'' +
                ", name='" + name + '\'' +
                ", amount='" + amount + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
