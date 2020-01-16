package com.ekabao.oil.bean;

import android.support.annotation.NonNull;

/**
 * ${APP_NAME}  App_oil
 *
 * @time 2019/1/4 15:55
 * Created by lj on 2019/1/4 15:55.
 */

public class HomeHostProduct implements Comparable<HomeHostProduct> {


    /**
     * activityRate : 0.89
     * code : CP-2018102315320273914
     * deadline : 18
     * fullName : 18个月加油套餐
     * id : 98
     * leastaAmount : 100.0
     * rate : 0.73
     * simpleName : 18个月加油套餐
     * status : 5
     */

    private double activityRate;
    private String code;
    private int deadline;
    private String fullName;
    private int id;
    private double leastaAmount;
    private double rate;
    private String simpleName;
    private int status;

    public double getActivityRate() {
        return activityRate;
    }

    public void setActivityRate(double activityRate) {
        this.activityRate = activityRate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getDeadline() {
        return deadline;
    }

    public void setDeadline(int deadline) {
        this.deadline = deadline;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLeastaAmount() {
        return leastaAmount;
    }

    public void setLeastaAmount(double leastaAmount) {
        this.leastaAmount = leastaAmount;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int compareTo(@NonNull HomeHostProduct o) {

        int num = o.deadline - this.deadline;
        return num == 0 ? 1 : num;
    }
}
