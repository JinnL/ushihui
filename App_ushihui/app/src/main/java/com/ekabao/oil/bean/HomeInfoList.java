package com.ekabao.oil.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/6/5.
 */

public class HomeInfoList {


    /**
     * banner : [{"code":"4","id":16,"imgUrl":"http://192.168.2.21/upload/banner/2018-05/201805303cd69bfd-74fa-488e-bcb0-9359f9daf43f.jpg","location":"","minVersion":"1.0.0","remark":"安卓banner1","title":"安卓banner1"},{"code":"4","id":17,"imgUrl":"http://192.168.2.21/upload/banner/2018-05/20180530bc2be201-2675-48e6-a4f2-2423d1b026a6.jpg","location":"","minVersion":"1.0.0","remark":"安卓banner2","title":"安卓banner2"},{"code":"4","id":18,"imgUrl":"http://192.168.2.21/upload/banner/2018-05/20180530d82d6303-c11b-4a7f-bc91-923320b2b06e.jpg","location":"","minVersion":"1.0.0","remark":"安卓banner3","title":"安卓banner3"},{"code":"4","id":19,"imgUrl":"http://192.168.2.21/upload/banner/2018-05/20180530d9d07f26-c811-42f0-af9b-8c4d25de9f8a.jpg","location":"","minVersion":"1.0.0","remark":"安卓banner4","title":"安卓banner4"}]
     * isNewBorrow : true
     * isSellingList : [{"activityRate":1,"amount":15000,"deadline":3,"distributionId":0,"distributionMoney":0,"endDate":1522372466000,"fullName":"理财宝No.3","id":5,"isCash":0,"isDouble":0,"isHot":1,"isInterest":1,"maxRate":8,"minRate":8,"pert":74,"raiseDeadline":3,"rate":7,"roundOff":0,"roundOffFlag":false,"startDate":1522199666000,"surplusAmount":3900},{"activityRate":0,"amount":20000,"deadline":3,"distributionId":0,"distributionMoney":0,"endDate":1522310137000,"fullName":"理财宝No.2","id":3,"isCash":0,"isDouble":0,"isHot":1,"isInterest":0,"maxRate":9,"minRate":9,"pert":56,"raiseDeadline":3,"rate":9,"roundOff":0,"roundOffFlag":false,"startDate":1522137337000,"surplusAmount":8800},{"activityRate":1,"amount":200000,"deadline":3,"distributionId":0,"distributionMoney":0,"endDate":1522199850000,"fullName":"理财宝No.4","id":6,"isCash":0,"isDouble":0,"isHot":1,"isInterest":1,"maxRate":8,"minRate":8,"pert":40,"raiseDeadline":1,"rate":7,"roundOff":0,"roundOffFlag":false,"startDate":1522199850000,"surplusAmount":119400}]
     * logoList : [{"addTime":1522052254000,"clickUrl":"14","id":4,"imgUrl":"http://192.168.2.21/upload/productPic/2018-06/4/2018060130749873-2685-4cf6-ba41-1c2aef532083.png","orders":5,"retainFir":"http://192.168.2.21:8888/myInvite","status":0,"title":"邀请好友"},{"addTime":1522052213000,"clickUrl":"13","id":3,"imgUrl":"http://192.168.2.21/upload/productPic/2018-06/3/201806016fb8c4d4-f7e0-48fc-aaa3-4d8873f21eae.png","orders":6,"retainFir":"http://192.168.2.21:8888/pointList","status":0,"title":"出借榜单"},{"addTime":1522052172000,"clickUrl":"http://192.168.2.21:8888/safe","id":2,"imgUrl":"http://192.168.2.21/upload/productPic/2018-06/2/201806016e5f5ef7-9683-450c-876e-67eaf05e9dcb.png","orders":7,"retainFir":"http://192.168.2.21:8888/safe","status":0,"title":"安全保障"},{"addTime":1522052107000,"clickUrl":"12","id":1,"imgUrl":"http://192.168.2.21/upload/productPic/2018-06/1/20180601c5bd79e1-b4ea-4049-9418-2073ed7b46d7.png","orders":8,"retainFir":"http://192.168.2.21:8888/taskCenter","status":0,"title":"任务中心"}]
     * newHand : {"accept":"","activityRate":1,"alreadyRaiseAmount":0,"amount":5000,"borrower":"该笔借款主要用于经营流动资金周转。","code":"CP-2018060114524981211","deadline":3,"distributionId":0,"endDate":1528008850000,"establish":1528041600000,"expireDate":1528300800000,"fullName":"新手专享No.2","id":23,"increasAmount":100,"interestType":0,"introduce":"该笔借款主要用于经营流动资金周转。","isCash":0,"isDeductible":0,"isDouble":0,"isHot":1,"isInterest":0,"leastaAmount":100,"maxAmount":5000,"pert":0,"raiseDeadline":3,"rate":14,"repaySource":"1.公司正常经营还款2.公司实际控制人担保代偿","repayType":1,"roundOff":0,"roundOffFlag":false,"sid":1,"simpleName":"新手专享No.2","startDate":1527836050000,"status":5,"surplusAmount":5000,"type":1,"windMeasure":"新手专享是小行家理财依据严格风控标准，挑选优质资产为用户提供的出借产品，匹配良好担保方案，用户出借本息有保障。        "}
     * showBtn : true
     */

    private boolean isNewBorrow;
    private NewHandBean newHand;
    private int showBtn;
    private List<HomeBannerBean> banner;
    private List<IsSellingListBean> isSellingList;


    private List<LogoListBean> logoList;



    public boolean isIsNewBorrow() {
        return isNewBorrow;
    }

    public void setIsNewBorrow(boolean isNewBorrow) {
        this.isNewBorrow = isNewBorrow;
    }

    public NewHandBean getNewHand() {
        return newHand;
    }

    public void setNewHand(NewHandBean newHand) {
        this.newHand = newHand;
    }

    public int isShowBtn() {
        return showBtn;
    }

    public void setShowBtn(int showBtn) {
        this.showBtn = showBtn;
    }

    public List<HomeBannerBean> getBanner() {
        return banner;
    }

    public void setBanner(List<HomeBannerBean> banner) {
        this.banner = banner;
    }

    public List<IsSellingListBean> getIsSellingList() {
        return isSellingList;
    }

    public void setIsSellingList(List<IsSellingListBean> isSellingList) {
        this.isSellingList = isSellingList;
    }

    public List<LogoListBean> getLogoList() {
        return logoList;
    }

    public void setLogoList(List<LogoListBean> logoList) {
        this.logoList = logoList;
    }

    public static class NewHandBean {
        /**
         * accept :
         * activityRate : 1.0
         * alreadyRaiseAmount : 0.0
         * amount : 5000.0
         * borrower : 该笔借款主要用于经营流动资金周转。
         * code : CP-2018060114524981211
         * deadline : 3
         * distributionId : 0
         * endDate : 1528008850000
         * establish : 1528041600000
         * expireDate : 1528300800000
         * fullName : 新手专享No.2
         * id : 23
         * increasAmount : 100.0
         * interestType : 0
         * introduce : 该笔借款主要用于经营流动资金周转。
         * isCash : 0
         * isDeductible : 0
         * isDouble : 0
         * isHot : 1
         * isInterest : 0
         * leastaAmount : 100.0
         * maxAmount : 5000.0
         * pert : 0.0
         * raiseDeadline : 3
         * rate : 14.0
         * repaySource : 1.公司正常经营还款2.公司实际控制人担保代偿
         * repayType : 1
         * roundOff : 0
         * roundOffFlag : false
         * sid : 1
         * simpleName : 新手专享No.2
         * startDate : 1527836050000
         * status : 5
         * surplusAmount : 5000.0
         * type : 1
         * windMeasure : 新手专享是小行家理财依据严格风控标准，挑选优质资产为用户提供的出借产品，匹配良好担保方案，用户出借本息有保障。
         */

        private String accept;
        private double activityRate;
        private double alreadyRaiseAmount;
        private double amount;
        private String borrower;
        private String code;
        private int deadline;
        private int distributionId;
        private long endDate;
        private long establish;
        private long expireDate;
        private String fullName;
        private int id;
        private double increasAmount;
        private int interestType;
        private String introduce;
        private int isCash;
        private int isDeductible;
        private int isDouble;
        private int isHot;
        private int isInterest;
        private double leastaAmount;
        private double maxAmount;
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
        private double surplusAmount;
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

        public double getAlreadyRaiseAmount() {
            return alreadyRaiseAmount;
        }

        public void setAlreadyRaiseAmount(double alreadyRaiseAmount) {
            this.alreadyRaiseAmount = alreadyRaiseAmount;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
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

        public double getIncreasAmount() {
            return increasAmount;
        }

        public void setIncreasAmount(double increasAmount) {
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

        public double getLeastaAmount() {
            return leastaAmount;
        }

        public void setLeastaAmount(double leastaAmount) {
            this.leastaAmount = leastaAmount;
        }

        public double getMaxAmount() {
            return maxAmount;
        }

        public void setMaxAmount(double maxAmount) {
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

        public double getSurplusAmount() {
            return surplusAmount;
        }

        public void setSurplusAmount(double surplusAmount) {
            this.surplusAmount = surplusAmount;
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

    /*public static class BannerBean {
        *//**
         * code : 4
         * id : 16
         * imgUrl : http://192.168.2.21/upload/banner/2018-05/201805303cd69bfd-74fa-488e-bcb0-9359f9daf43f.jpg
         * location :
         * minVersion : 1.0.0
         * remark : 安卓banner1
         * title : 安卓banner1
         *//*

        private String code;
        private int id;
        private String imgUrl;
        private String location;
        private String minVersion;
        private String remark;
        private String title;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getMinVersion() {
            return minVersion;
        }

        public void setMinVersion(String minVersion) {
            this.minVersion = minVersion;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }*/

    public static class IsSellingListBean {
        /**
         * activityRate : 1.0
         * amount : 15000.0
         * deadline : 3
         * distributionId : 0
         * distributionMoney : 0
         * endDate : 1522372466000
         * fullName : 理财宝No.3
         * id : 5
         * isCash : 0
         * isDouble : 0
         * isHot : 1
         * isInterest : 1
         * maxRate : 8.0
         * minRate : 8.0
         * pert : 74
         * raiseDeadline : 3
         * rate : 7.0
         * roundOff : 0
         * roundOffFlag : false
         * startDate : 1522199666000
         * surplusAmount : 3900.0
         * tag : 加息风暴,收益高
         */

        private double activityRate;
        private double amount;
        private int deadline;
        private int distributionId;
        private int distributionMoney;
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
        private double rate;
        private int roundOff;
        private boolean roundOffFlag;
        private long startDate;
        private double surplusAmount;
        private String tag;
        public double getActivityRate() {
            return activityRate;
        }

        public void setActivityRate(double activityRate) {
            this.activityRate = activityRate;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
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

        public int getDistributionMoney() {
            return distributionMoney;
        }

        public void setDistributionMoney(int distributionMoney) {
            this.distributionMoney = distributionMoney;
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

        public double getRate() {
            return rate;
        }

        public void setRate(double rate) {
            this.rate = rate;
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

        public double getSurplusAmount() {
            return surplusAmount;
        }

        public void setSurplusAmount(double surplusAmount) {
            this.surplusAmount = surplusAmount;
        }
        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }
    }

    public static class LogoListBean {
        /**
         * addTime : 1522052254000
         * clickUrl : 14
         * id : 4
         * imgUrl : http://192.168.2.21/upload/productPic/2018-06/4/2018060130749873-2685-4cf6-ba41-1c2aef532083.png
         * orders : 5
         * retainFir : http://192.168.2.21:8888/myInvite
         * status : 0
         * title : 邀请好友
         */

        private long addTime;
        private String clickUrl;
        private int id;
        private String imgUrl;
        private int orders;
        private String retainFir;
        private int status;
        private String title;
        private String note;




        public LogoListBean() {
        }

        public LogoListBean(int id, int orders, String title,String note) {
            this.id = id;
            this.orders = orders;
            this.title = title;
            this.note = note;
        }

        public long getAddTime() {
            return addTime;
        }

        public void setAddTime(long addTime) {
            this.addTime = addTime;
        }

        public String getClickUrl() {
            return clickUrl;
        }

        public void setClickUrl(String clickUrl) {
            this.clickUrl = clickUrl;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public int getOrders() {
            return orders;
        }

        public void setOrders(int orders) {
            this.orders = orders;
        }

        public String getRetainFir() {
            return retainFir;
        }

        public void setRetainFir(String retainFir) {
            this.retainFir = retainFir;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }
    }
}
