package com.ekabao.oil.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/6/1.
 */

public class Invest {

    /**
     * isSellingList : [{"amount":300000,"deadline":30,"distributionId":0,"endDate":1527138003000,"fullName":"行家优选30NO.194","id":463,"isCash":1,"isDouble":0,"isHot":0,"isInterest":0,"maxRate":10.8,"minRate":10.8,"pert":0,"raiseDeadline":3,"roundOff":1,"roundOffFlag":false,"startDate":1526965203000,"surplusAmount":300000,"tag":"加息风暴,收益高"},{"amount":300000,"deadline":30,"distributionId":0,"endDate":1527136412000,"fullName":"行家优选30NO.193","id":462,"isCash":1,"isDouble":0,"isHot":0,"isInterest":0,"maxRate":10.8,"minRate":10.8,"pert":46,"raiseDeadline":3,"roundOff":1,"roundOffFlag":false,"startDate":1526963612000,"surplusAmount":160000,"tag":"加息风暴,收益高"},{"amount":100000,"deadline":60,"distributionId":0,"endDate":1527136073000,"fullName":"行家优选60NO.81","id":431,"isCash":1,"isDouble":0,"isHot":0,"isInterest":0,"maxRate":11,"minRate":11,"pert":50,"raiseDeadline":3,"roundOff":1,"roundOffFlag":false,"startDate":1526963273000,"surplusAmount":50000,"tag":"收益高,风险可控,平台贴息"},{"amount":100000,"deadline":60,"distributionId":0,"endDate":1527069480000,"fullName":"行家优选60NO.80","id":420,"isCash":1,"isDouble":0,"isHot":0,"isInterest":0,"maxRate":11,"minRate":11,"pert":75,"raiseDeadline":3,"roundOff":1,"roundOffFlag":false,"startDate":1526896680000,"surplusAmount":25000,"tag":"收益高,风险可控,平台贴息"}]
     * newHand : [{"accept":"","activityRate":6.6,"alreadyRaiseAmount":42100,"amount":50000,"borrower":"该笔借款主要用于经营流动资金周转。","code":"CP-20180518112531998611","deadline":10,"distributionId":0,"endDate":1527128979000,"establish":1527177600000,"expireDate":1528041600000,"fullName":"新手专享NO.141","id":470,"increasAmount":100,"interestType":0,"introduce":"新手专享是小行家依据严格风控标准，挑选优质资产为用户提供的出借产品，匹配良好担保方案，降低出借风险，用户出借本息有保障。","isCash":1,"isDeductible":0,"isDouble":0,"isHot":0,"isInterest":0,"leastaAmount":100,"maxAmount":50000,"pert":84.2,"raiseDeadline":3,"rate":11.4,"repaySource":"1、公司正常经营还款 2、公司实际控制人担保代偿。","repayType":1,"roundOff":1,"roundOffFlag":false,"sid":1,"simpleName":"新手专享NO.141","startDate":1526956179000,"status":5,"surplusAmount":7900,"tag":"收益高,风险可控,周期短","type":1,"windMeasure":"11"}]
     * sysDate : 1527824379
     * willSellList : [{"amount":100000,"deadline":60,"distributionId":0,"endDate":1528083556000,"fullName":"行家优选60NO.98","id":501,"isCash":1,"isDouble":0,"isHot":0,"isInterest":0,"maxRate":11,"minRate":11,"pert":0,"raiseDeadline":3,"roundOff":1,"roundOffFlag":false,"startDate":1527910756000,"surplusAmount":100000,"tag":"收益高,风险可控,平台贴息"}]
     */

    private int sysDate;
    private List<IsSellingListBean> isSellingList;
    private List<NewHandBean> newHand; //新手标
    private List<WillSellListBean> willSellList; //预售

    public int getSysDate() {
        return sysDate;
    }

    public void setSysDate(int sysDate) {
        this.sysDate = sysDate;
    }

    public List<IsSellingListBean> getIsSellingList() {
        return isSellingList;
    }

    public void setIsSellingList(List<IsSellingListBean> isSellingList) {
        this.isSellingList = isSellingList;
    }

    public List<NewHandBean> getNewHand() {
        return newHand;
    }

    public void setNewHand(List<NewHandBean> newHand) {
        this.newHand = newHand;
    }

    public List<WillSellListBean> getWillSellList() {
        return willSellList;
    }

    public void setWillSellList(List<WillSellListBean> willSellList) {
        this.willSellList = willSellList;
    }

    public static class IsSellingListBean {





        /**
         * amount : 300000
         * deadline : 30
         * distributionId : 0
         * endDate : 1527138003000
         * fullName : 行家优选30NO.194
         * id : 463
         * isCash : 1
         * isDouble : 0
         * isHot : 0
         * isInterest : 0
         * maxRate : 10.8
         * minRate : 10.8
         * pert : 0
         * raiseDeadline : 3
         * roundOff : 1
         * roundOffFlag : false
         * startDate : 1526965203000
         * surplusAmount : 300000
         * tag : 加息风暴,收益高
         */

        private int amount;
        private int deadline;
        private int distributionId;
        private long endDate;
        private String fullName;
        private int id;
        private int isCash;
        private int isDouble;
        private int isHot;
        private int isInterest;
        private double maxRate;
        private double minRate;
        private double pert;
        private int raiseDeadline;
        private int roundOff;
        private boolean roundOffFlag;
        private long startDate;
        private int surplusAmount;
        private String tag;
        /**
         * activityRate : 1
         * distributionMoney : 0
         * maxRate : 8
         * minRate : 8
         * rate : 7
         */

        private double activityRate;
        private int distributionMoney;
       /* @SerializedName("maxRate")
        private int maxRateX;
        @SerializedName("minRate")
        private int minRateX;*/
        private double rate;

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public int getDeadline() {
            return deadline;
        }

        public void setDeadline(int deadline) {
            this.deadline = deadline;
        }

        public int getDistributionId() {
            return distributionId;
        }

        public void setDistributionId(int distributionId) {
            this.distributionId = distributionId;
        }

        public long getEndDate() {
            return endDate;
        }

        public void setEndDate(long endDate) {
            this.endDate = endDate;
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

        public int getIsCash() {
            return isCash;
        }

        public void setIsCash(int isCash) {
            this.isCash = isCash;
        }

        public int getIsDouble() {
            return isDouble;
        }

        public void setIsDouble(int isDouble) {
            this.isDouble = isDouble;
        }

        public int getIsHot() {
            return isHot;
        }

        public void setIsHot(int isHot) {
            this.isHot = isHot;
        }

        public int getIsInterest() {
            return isInterest;
        }

        public void setIsInterest(int isInterest) {
            this.isInterest = isInterest;
        }

        public double getMaxRate() {
            return maxRate;
        }

        public void setMaxRate(double maxRate) {
            this.maxRate = maxRate;
        }

        public double getMinRate() {
            return minRate;
        }

        public void setMinRate(double minRate) {
            this.minRate = minRate;
        }

        public double getPert() {
            return pert;
        }

        public void setPert(double pert) {
            this.pert = pert;
        }

        public int getRaiseDeadline() {
            return raiseDeadline;
        }

        public void setRaiseDeadline(int raiseDeadline) {
            this.raiseDeadline = raiseDeadline;
        }

        public int getRoundOff() {
            return roundOff;
        }

        public void setRoundOff(int roundOff) {
            this.roundOff = roundOff;
        }

        public boolean isRoundOffFlag() {
            return roundOffFlag;
        }

        public void setRoundOffFlag(boolean roundOffFlag) {
            this.roundOffFlag = roundOffFlag;
        }

        public long getStartDate() {
            return startDate;
        }

        public void setStartDate(long startDate) {
            this.startDate = startDate;
        }

        public int getSurplusAmount() {
            return surplusAmount;
        }

        public void setSurplusAmount(int surplusAmount) {
            this.surplusAmount = surplusAmount;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public double getActivityRate() {
            return activityRate;
        }

        public void setActivityRate(double activityRate) {
            this.activityRate = activityRate;
        }

        public int getDistributionMoney() {
            return distributionMoney;
        }

        public void setDistributionMoney(int distributionMoney) {
            this.distributionMoney = distributionMoney;
        }



        public double getRate() {
            return rate;
        }

        public void setRate(double rate) {
            this.rate = rate;
        }
    }

    public static class NewHandBean {
        /**
         * accept :
         * activityRate : 6.6
         * alreadyRaiseAmount : 42100
         * amount : 50000
         * borrower : 该笔借款主要用于经营流动资金周转。
         * code : CP-20180518112531998611
         * deadline : 10
         * distributionId : 0
         * endDate : 1527128979000
         * establish : 1527177600000
         * expireDate : 1528041600000
         * fullName : 新手专享NO.141
         * id : 470
         * increasAmount : 100
         * interestType : 0
         * introduce : 新手专享是小行家依据严格风控标准，挑选优质资产为用户提供的出借产品，匹配良好担保方案，降低出借风险，用户出借本息有保障。
         * isCash : 1
         * isDeductible : 0
         * isDouble : 0
         * isHot : 0
         * isInterest : 0
         * leastaAmount : 100
         * maxAmount : 50000
         * pert : 84.2
         * raiseDeadline : 3
         * rate : 11.4
         * repaySource : 1、公司正常经营还款 2、公司实际控制人担保代偿。
         * repayType : 1
         * roundOff : 1
         * roundOffFlag : false
         * sid : 1
         * simpleName : 新手专享NO.141
         * startDate : 1526956179000
         * status : 5
         * surplusAmount : 7900
         * tag : 收益高,风险可控,周期短
         * type : 1
         * windMeasure : 11
         */

        private String accept;
        private double activityRate;
        private int alreadyRaiseAmount;
        private int amount;
        private String borrower;
        private String code;
        private int deadline;
        private int distributionId;
        private long endDate;
        private long establish;
        private long expireDate;
        private String fullName;
        private int id;
        private int increasAmount;
        private int interestType;
        private String introduce;
        private int isCash;
        private int isDeductible;
        private int isDouble;
        private int isHot;
        private int isInterest;
        private int leastaAmount;
        private int maxAmount;
        private double pert;
        private int raiseDeadline;
        private double rate;
        private String repaySource;
        private int repayType;
        private int roundOff;
        private boolean roundOffFlag;
        private int sid;
        private String simpleName;
        private long startDate;
        private int status;
        private int surplusAmount;
        private String tag;
        private int type;
        private String windMeasure;

        public String getAccept() {
            return accept;
        }

        public void setAccept(String accept) {
            this.accept = accept;
        }

        public double getActivityRate() {
            return activityRate;
        }

        public void setActivityRate(double activityRate) {
            this.activityRate = activityRate;
        }

        public int getAlreadyRaiseAmount() {
            return alreadyRaiseAmount;
        }

        public void setAlreadyRaiseAmount(int alreadyRaiseAmount) {
            this.alreadyRaiseAmount = alreadyRaiseAmount;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getBorrower() {
            return borrower;
        }

        public void setBorrower(String borrower) {
            this.borrower = borrower;
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

        public int getDistributionId() {
            return distributionId;
        }

        public void setDistributionId(int distributionId) {
            this.distributionId = distributionId;
        }

        public long getEndDate() {
            return endDate;
        }

        public void setEndDate(long endDate) {
            this.endDate = endDate;
        }

        public long getEstablish() {
            return establish;
        }

        public void setEstablish(long establish) {
            this.establish = establish;
        }

        public long getExpireDate() {
            return expireDate;
        }

        public void setExpireDate(long expireDate) {
            this.expireDate = expireDate;
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

        public int getIncreasAmount() {
            return increasAmount;
        }

        public void setIncreasAmount(int increasAmount) {
            this.increasAmount = increasAmount;
        }

        public int getInterestType() {
            return interestType;
        }

        public void setInterestType(int interestType) {
            this.interestType = interestType;
        }

        public String getIntroduce() {
            return introduce;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
        }

        public int getIsCash() {
            return isCash;
        }

        public void setIsCash(int isCash) {
            this.isCash = isCash;
        }

        public int getIsDeductible() {
            return isDeductible;
        }

        public void setIsDeductible(int isDeductible) {
            this.isDeductible = isDeductible;
        }

        public int getIsDouble() {
            return isDouble;
        }

        public void setIsDouble(int isDouble) {
            this.isDouble = isDouble;
        }

        public int getIsHot() {
            return isHot;
        }

        public void setIsHot(int isHot) {
            this.isHot = isHot;
        }

        public int getIsInterest() {
            return isInterest;
        }

        public void setIsInterest(int isInterest) {
            this.isInterest = isInterest;
        }

        public int getLeastaAmount() {
            return leastaAmount;
        }

        public void setLeastaAmount(int leastaAmount) {
            this.leastaAmount = leastaAmount;
        }

        public int getMaxAmount() {
            return maxAmount;
        }

        public void setMaxAmount(int maxAmount) {
            this.maxAmount = maxAmount;
        }

        public double getPert() {
            return pert;
        }

        public void setPert(double pert) {
            this.pert = pert;
        }

        public int getRaiseDeadline() {
            return raiseDeadline;
        }

        public void setRaiseDeadline(int raiseDeadline) {
            this.raiseDeadline = raiseDeadline;
        }

        public double getRate() {
            return rate;
        }

        public void setRate(double rate) {
            this.rate = rate;
        }

        public String getRepaySource() {
            return repaySource;
        }

        public void setRepaySource(String repaySource) {
            this.repaySource = repaySource;
        }

        public int getRepayType() {
            return repayType;
        }

        public void setRepayType(int repayType) {
            this.repayType = repayType;
        }

        public int getRoundOff() {
            return roundOff;
        }

        public void setRoundOff(int roundOff) {
            this.roundOff = roundOff;
        }

        public boolean isRoundOffFlag() {
            return roundOffFlag;
        }

        public void setRoundOffFlag(boolean roundOffFlag) {
            this.roundOffFlag = roundOffFlag;
        }

        public int getSid() {
            return sid;
        }

        public void setSid(int sid) {
            this.sid = sid;
        }

        public String getSimpleName() {
            return simpleName;
        }

        public void setSimpleName(String simpleName) {
            this.simpleName = simpleName;
        }

        public long getStartDate() {
            return startDate;
        }

        public void setStartDate(long startDate) {
            this.startDate = startDate;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getSurplusAmount() {
            return surplusAmount;
        }

        public void setSurplusAmount(int surplusAmount) {
            this.surplusAmount = surplusAmount;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getWindMeasure() {
            return windMeasure;
        }

        public void setWindMeasure(String windMeasure) {
            this.windMeasure = windMeasure;
        }
    }

    public static class WillSellListBean {
        /**
         * amount : 100000
         * deadline : 60
         * distributionId : 0
         * endDate : 1528083556000
         * fullName : 行家优选60NO.98
         * id : 501
         * isCash : 1
         * isDouble : 0
         * isHot : 0
         * isInterest : 0
         * maxRate : 11
         * minRate : 11
         * pert : 0
         * raiseDeadline : 3
         * roundOff : 1
         * roundOffFlag : false
         * startDate : 1527910756000
         * surplusAmount : 100000
         * tag : 收益高,风险可控,平台贴息
         */

        private int amount;
        private int deadline;
        private int distributionId;
        private long endDate;
        private String fullName;
        private int id;
        private int isCash;
        private int isDouble;
        private int isHot;
        private int isInterest;
        private double maxRate;
        private double minRate;
        private int pert;
        private int raiseDeadline;
        private int roundOff;
        private boolean roundOffFlag;
        private long startDate;
        private int surplusAmount;
        private String tag;

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public int getDeadline() {
            return deadline;
        }

        public void setDeadline(int deadline) {
            this.deadline = deadline;
        }

        public int getDistributionId() {
            return distributionId;
        }

        public void setDistributionId(int distributionId) {
            this.distributionId = distributionId;
        }

        public long getEndDate() {
            return endDate;
        }

        public void setEndDate(long endDate) {
            this.endDate = endDate;
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

        public int getIsCash() {
            return isCash;
        }

        public void setIsCash(int isCash) {
            this.isCash = isCash;
        }

        public int getIsDouble() {
            return isDouble;
        }

        public void setIsDouble(int isDouble) {
            this.isDouble = isDouble;
        }

        public int getIsHot() {
            return isHot;
        }

        public void setIsHot(int isHot) {
            this.isHot = isHot;
        }

        public int getIsInterest() {
            return isInterest;
        }

        public void setIsInterest(int isInterest) {
            this.isInterest = isInterest;
        }

        public double getMaxRate() {
            return maxRate;
        }

        public void setMaxRate(double maxRate) {
            this.maxRate = maxRate;
        }

        public double getMinRate() {
            return minRate;
        }

        public void setMinRate(double minRate) {
            this.minRate = minRate;
        }

        public int getPert() {
            return pert;
        }

        public void setPert(int pert) {
            this.pert = pert;
        }

        public int getRaiseDeadline() {
            return raiseDeadline;
        }

        public void setRaiseDeadline(int raiseDeadline) {
            this.raiseDeadline = raiseDeadline;
        }

        public int getRoundOff() {
            return roundOff;
        }

        public void setRoundOff(int roundOff) {
            this.roundOff = roundOff;
        }

        public boolean isRoundOffFlag() {
            return roundOffFlag;
        }

        public void setRoundOffFlag(boolean roundOffFlag) {
            this.roundOffFlag = roundOffFlag;
        }

        public long getStartDate() {
            return startDate;
        }

        public void setStartDate(long startDate) {
            this.startDate = startDate;
        }

        public int getSurplusAmount() {
            return surplusAmount;
        }

        public void setSurplusAmount(int surplusAmount) {
            this.surplusAmount = surplusAmount;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }
    }
}
