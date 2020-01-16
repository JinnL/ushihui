package com.mcz.xhj.yz.dr_bean;

/**
 * 项目名称：js_app
 * 类描述：  status bean
 * 创建人：shuc
 * 创建时间：2017/1/11 17:07
 * 修改人：DELL
 * 修改时间：2017/1/11 17:07
 * 修改备注：
 */
public class YearEndStatusBean {
    private double status;

    public YearEndStatusBean() {
        super();
    }

    public YearEndStatusBean(double status){
        this.status = status;
    }

    public double getStatus() {
        return status;
    }

    public void setStatus(double status) {
        this.status = status;
    }
}
