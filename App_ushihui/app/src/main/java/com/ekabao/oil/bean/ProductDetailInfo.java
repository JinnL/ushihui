package com.ekabao.oil.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/6/7.
 */

public class ProductDetailInfo {

    private List<InvestListBean> investList;
    private List<PicListBean> picList;
    private List<String> projectList;
    /**
     * distribution_persion_count : 3
     * investList : [{"amount":8000,"factAmount":0,"factInterest":0,"id":219,"idCards":"429xxxxxxxxxxxxxxx","interest":60,"investTime":1528360319000,"investTimeLast":1528360319000,"joinType":3,"mobilephone":"159****7671","pid":25,"realName":"愚**","sex":2,"status":0,"tenderAccountSum":8000,"tender_money_distribution":50,"uid":8},{"amount":7000,"factAmount":0,"factInterest":0,"id":218,"idCards":"429xxxxxxxxxxxxxxx","interest":52.5,"investTime":1528360267000,"investTimeLast":1528360267000,"joinType":3,"mobilephone":"159****7672","pid":25,"realName":"远**","sex":1,"status":0,"tenderAccountSum":7000,"tender_money_distribution":30,"uid":2},{"amount":2000,"factAmount":0,"factInterest":0,"id":214,"idCards":"412xxxxxxxxxxxxxxx","interest":15,"investTime":1528359902000,"investTimeLast":1528359924000,"joinType":3,"mobilephone":"131****6761","pid":25,"realName":"李**","sex":1,"status":0,"tenderAccountSum":5000,"tender_money_distribution":20,"uid":6},{"amount":3000,"factAmount":0,"factInterest":0,"id":217,"idCards":"360xxxxxxxxxxxxxxx","interest":22.5,"investTime":1528360139000,"investTimeLast":1528360139000,"joinType":3,"mobilephone":"158****9310","pid":25,"realName":"叶**","sex":1,"status":0,"tenderAccountSum":3000,"uid":4},{"amount":2000,"factAmount":0,"factInterest":0,"id":216,"idCards":"429xxxxxxxxxxxxxxx","interest":15,"investTime":1528360099000,"investTimeLast":1528360099000,"joinType":3,"mobilephone":"159****7673","pid":25,"realName":"杨**","sex":1,"status":0,"tenderAccountSum":2000,"uid":1},{"amount":1000,"factAmount":0,"factInterest":0,"id":213,"idCards":"429xxxxxxxxxxxxxxx","interest":7.5,"investTime":1528354294000,"investTimeLast":1528354294000,"joinType":3,"mobilephone":"150****0346","pid":25,"realName":"仲**","status":0,"tenderAccountSum":1000,"uid":5}]
     * tender_money_distribution_sum : 100
     */

    private int distribution_persion_count;
    private String tender_money_distribution_sum;

    public int getDistribution_persion_count() {
        return distribution_persion_count;
    }

    public void setDistribution_persion_count(int distribution_persion_count) {
        this.distribution_persion_count = distribution_persion_count;
    }

    public String getTender_money_distribution_sum() {
        return tender_money_distribution_sum;
    }

    public void setTender_money_distribution_sum(String tender_money_distribution_sum) {
        this.tender_money_distribution_sum = tender_money_distribution_sum;
    }



    public List<InvestListBean> getInvestList() {
        return investList;
    }

    public void setInvestList(List<InvestListBean> investList) {
        this.investList = investList;
    }

    public List<PicListBean> getPicList() {
        return picList;
    }

    public void setPicList(List<PicListBean> picList) {
        this.picList = picList;
    }

    public List<String> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<String> projectList) {
        this.projectList = projectList;
    }

    public static class InvestListBean {
        /**
         * amount : 5000.0
         * factAmount : 0.0
         * factInterest : 0.0
         * id : 9
         * idCards : 429xxxxxxxxxxxxxxx
         * interest : 3.33
         * investTime : 1522223891000
         * joinType : 3
         * mobilephone : 159****7676
         * pid : 6
         * realName : 张**
         * sex : 1
         * status : 0
         * uid : 23
         * fid : 410
         */

        private double amount;
        private double factAmount;
        private double factInterest;
        private int id;
        private String idCards;
        private double interest;
        private long investTime;
        private int joinType;
        private String mobilephone;
        private int pid;
        private String realName;
        private int sex;
        private int status;
        private int uid;
        private int fid;
        /**
         * amount : 1000
         * factAmount : 0
         * factInterest : 0
         * interest : 7.5
         * investTimeLast : 1528354294000
         * tenderAccountSum : 1000
         */

        private double interestX;
        private long investTimeLast;
        private int tenderAccountSum;
        /**
         * amount : 8000
         * factAmount : 0
         * factInterest : 0
         * interest : 60
         * tender_money_distribution : 50
         */

        private int tender_money_distribution;


        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public double getFactAmount() {
            return factAmount;
        }

        public void setFactAmount(double factAmount) {
            this.factAmount = factAmount;
        }

        public double getFactInterest() {
            return factInterest;
        }

        public void setFactInterest(double factInterest) {
            this.factInterest = factInterest;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getIdCards() {
            return idCards;
        }

        public void setIdCards(String idCards) {
            this.idCards = idCards;
        }

        public double getInterest() {
            return interest;
        }

        public void setInterest(double interest) {
            this.interest = interest;
        }

        public long getInvestTime() {
            return investTime;
        }

        public void setInvestTime(long investTime) {
            this.investTime = investTime;
        }

        public int getJoinType() {
            return joinType;
        }

        public void setJoinType(int joinType) {
            this.joinType = joinType;
        }

        public String getMobilephone() {
            return mobilephone;
        }

        public void setMobilephone(String mobilephone) {
            this.mobilephone = mobilephone;
        }

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getFid() {
            return fid;
        }

        public void setFid(int fid) {
            this.fid = fid;
        }



        public double getInterestX() {
            return interestX;
        }

        public void setInterestX(double interestX) {
            this.interestX = interestX;
        }

        public long getInvestTimeLast() {
            return investTimeLast;
        }

        public void setInvestTimeLast(long investTimeLast) {
            this.investTimeLast = investTimeLast;
        }

        public int getTenderAccountSum() {
            return tenderAccountSum;
        }

        public void setTenderAccountSum(int tenderAccountSum) {
            this.tenderAccountSum = tenderAccountSum;
        }

        public int getTender_money_distribution() {
            return tender_money_distribution;
        }

        public void setTender_money_distribution(int tender_money_distribution) {
            this.tender_money_distribution = tender_money_distribution;
        }
    }

    public static class PicListBean {
        /**
         * bigUrl : http://192.168.1.2/upload/productPic/2018-03/6/201803281ed0003d-cb59-4f49-890f-a73fd2cef337.jpg
         * name :
         * pid : 6
         * showSort : 0
         * status : 1
         * type : 0
         */

        private String bigUrl;
        private String name;
        private int pid;
        private int showSort;
        private int status;
        private int type;

        public String getBigUrl() {
            return bigUrl;
        }

        public void setBigUrl(String bigUrl) {
            this.bigUrl = bigUrl;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public int getShowSort() {
            return showSort;
        }

        public void setShowSort(int showSort) {
            this.showSort = showSort;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
