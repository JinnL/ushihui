package com.mcz.xhj.yz.dr_bean;

/**
 * 项目名称：JsAppAs2.0
 * 类描述：体验标红包bean
 * 创建人：shuc
 * 创建时间：2017/2/22 18:03
 * 修改人：DELL
 * 修改时间：2017/2/22 18:03
 * 修改备注：
 */
public class TiyanConponsBean {
    private double amount;  //体验金金额
    private String name;//
    private String remark;//来源
    private String addtime;//添加时间
    private String expireDate;//有效时间
    private String type;//卷类型
    private String status;//状态 0：未使用  1：已使用 2：已过期
    private String source;//100=未激活99=激活&体验金，其他都为激活
    private double enableAmount;//体验金启用金额

    public TiyanConponsBean() {
        super();
    }
    public TiyanConponsBean(double amount,String remark,String addtime ,String type,String status,String source) {
        super();
        this.amount = amount;
        this.remark = remark;
        this.addtime = addtime;
        this.type = type;
        this.status = status;
        this.source = source;
    }

    public TiyanConponsBean(double amount, String remark, String addtime, String type, String status, String source, double enableAmount) {
        this.amount = amount;
        this.remark = remark;
        this.addtime = addtime;
        this.type = type;
        this.status = status;
        this.source = source;
        this.enableAmount = enableAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public double getEnableAmount() {
        return enableAmount;
    }

    public void setEnableAmount(double enableAmount) {
        this.enableAmount = enableAmount;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }
}
