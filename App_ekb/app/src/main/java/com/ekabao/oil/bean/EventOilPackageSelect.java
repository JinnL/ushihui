package com.ekabao.oil.bean;

/**
 * ${APP_NAME}  App_akzj
 * 首页 加油热门套餐点击事件
 * @time 2019/3/19 17:23
 * Created by lj on 2019/3/19 17:23.
 */

public class EventOilPackageSelect {
    int pid;
    int money;
    String userName;


    public EventOilPackageSelect() {
    }

    public EventOilPackageSelect(int pid, int money, String userName) {
        this.pid = pid;
        this.money = money;
        this.userName = userName;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
