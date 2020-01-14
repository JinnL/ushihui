package com.mcz.xhj.yz.dr_bean;

/**
 * 项目名称：JsAppAs2.0
 * 类描述：
 * 创建人：shuc
 * 创建时间：2017/2/23 10:55
 * 修改人：DELL
 * 修改时间：2017/2/23 10:55
 * 修改备注：
 */
public class TransactionDetailsBean {

    private String amount;//	是	double	交易金额
    private String type	;//是	Integer	0=支出，1=收入
    private long addTime	;//是	timestamp	记录发生时间
    private String remark;//	否	String	交易备注
    private int tradeType;  //1=充值，2=提现，3=投资，4=活动,5=提现手续费，6=回款,7=体验金

    public TransactionDetailsBean() {
        super();
    }

    public TransactionDetailsBean(String amount,String type,long addTime,String remark,int tradeType){
        super();
        this.amount = amount;
        this.type = type;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
}
