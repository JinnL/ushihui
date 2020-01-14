package com.mcz.xhj.yz.dr_bean;

/**
 * Created by Administrator on 2017/11/7 0007.
 */

public class DateRecordBean {
    private String time;
    private String months;//月份
    private int count;//当月记录数

    public DateRecordBean() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMonths() {
        return months;
    }

    public void setMonths(String months) {
        this.months = months;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "DateRecordBean{" +
                "time='" + time + '\'' +
                ", months='" + months + '\'' +
                ", count=" + count +
                '}';
    }
}
