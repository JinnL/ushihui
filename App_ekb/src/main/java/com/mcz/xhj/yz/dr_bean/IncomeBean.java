package com.mcz.xhj.yz.dr_bean;

/**
 * 项目名称：JsAppAs2.0
 * 类描述：累计收益
 * 创建人：shuc
 * 创建时间：2017/2/23 19:21
 * 修改人：DELL
 * 修改时间：2017/2/23 19:21
 * 修改备注：
 */
public class IncomeBean {
    private long addTime;//	是	Date	收益时间
    private String remark;//	是	String	收益描述
    private double amount;//	是	double	收益金额
    private int tradeType;  //1=充值，2=提现，3=投资，4=活动,5=提现手续费，6=回款,7=体验金

    public IncomeBean() {
        super();
    }

    public IncomeBean( long addTime,String remark,double amount,int tradeType) {
        super();
        this.addTime = addTime;
        this.remark = remark;
        this.tradeType = tradeType;
    }

    public int getTradeType() {
        return tradeType;
    }

    public void setTradeType(int tradeType) {
        this.tradeType = tradeType;
    }

    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
