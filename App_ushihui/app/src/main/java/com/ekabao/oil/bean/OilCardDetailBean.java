package com.ekabao.oil.bean;

/**
 * ${APP_NAME}  App_oil
 * 油卡详情
 * @time 2018/11/7 18:21
 * Created by lj on 2018/11/7 18:21.
 */

public class OilCardDetailBean {


    /**
     * lastTime : 500
     * tcljAmount : 500
     * nextAmount : 500
     * nextTime : 500
     * jsczAmount : 0
     * fuelCardDetail : {"cardnum":"9527952795279527","createtime":1541420512000,"id":12,"mobilephone":"150****0347","realname":"龚长章","type":1,"uid":5}
     * lastAmount : 500
     */

    private long lastTime;
    private int tcljAmount;
    private int nextAmount;
    private long nextTime;
    private int jsczAmount;
    private FuelCardDetailBean fuelCardDetail;
    private int lastAmount;

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    public int getTcljAmount() {
        return tcljAmount;
    }

    public void setTcljAmount(int tcljAmount) {
        this.tcljAmount = tcljAmount;
    }

    public int getNextAmount() {
        return nextAmount;
    }

    public void setNextAmount(int nextAmount) {
        this.nextAmount = nextAmount;
    }

    public long getNextTime() {
        return nextTime;
    }

    public void setNextTime(long nextTime) {
        this.nextTime = nextTime;
    }

    public int getJsczAmount() {
        return jsczAmount;
    }

    public void setJsczAmount(int jsczAmount) {
        this.jsczAmount = jsczAmount;
    }

    public FuelCardDetailBean getFuelCardDetail() {
        return fuelCardDetail;
    }

    public void setFuelCardDetail(FuelCardDetailBean fuelCardDetail) {
        this.fuelCardDetail = fuelCardDetail;
    }

    public int getLastAmount() {
        return lastAmount;
    }

    public void setLastAmount(int lastAmount) {
        this.lastAmount = lastAmount;
    }

    public static class FuelCardDetailBean {
        /**
         * cardnum : 9527952795279527
         * createtime : 1541420512000
         * id : 12
         * mobilephone : 150****0347
         * realname : 龚长章
         * type : 1
         * uid : 5
         */

        private String cardnum;
        private long createtime;
        private int id;
        private String mobilephone;
        private String realname;
        private int type;
        private int uid;

        public String getCardnum() {
            return cardnum;
        }

        public void setCardnum(String cardnum) {
            this.cardnum = cardnum;
        }

        public long getCreatetime() {
            return createtime;
        }

        public void setCreatetime(long createtime) {
            this.createtime = createtime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMobilephone() {
            return mobilephone;
        }

        public void setMobilephone(String mobilephone) {
            this.mobilephone = mobilephone;
        }

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }
    }
}
