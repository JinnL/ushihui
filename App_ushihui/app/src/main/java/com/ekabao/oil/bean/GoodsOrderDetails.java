package com.ekabao.oil.bean;

/**
 * ${APP_NAME}  App_oil
 *
 * @time 2019/1/10 19:00
 * Created by lj on 2019/1/10 19:00.
 */

public class GoodsOrderDetails {

    /**
     * channel : 2
     * id : 2952
     * orderDetail : {"address":"北京北京市东城区啦咯啦咯啦咯啦","amount":153408,"goodname":"母亲节礼物-舒适安睡组合","images":"http://yanxuan.nosdn.127.net/1f67b1970ee20fd572b7202da0ff705d.png","investTime":1547106270000,"number":1,"orderid":2952,"paynum":"201901101544304316062224","receiveName":"李建","receivePhone":"13183386761","retail_price":1598,"status":0}
     * token : 2dc7d42d-ff2d-4604-b427-08b1aeccd542
     * version : 2.1.2
     */

    private String channel;
    private String id;
    private OrderDetailBean orderDetail;
    private String token;
    private String version;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public OrderDetailBean getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(OrderDetailBean orderDetail) {
        this.orderDetail = orderDetail;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public static class OrderDetailBean {
        /**
         * address : 北京北京市东城区啦咯啦咯啦咯啦
         * amount : 153408.0
         * goodname : 母亲节礼物-舒适安睡组合
         * images : http://yanxuan.nosdn.127.net/1f67b1970ee20fd572b7202da0ff705d.png
         * investTime : 1547106270000
         * number : 1
         * orderid : 2952
         * paynum : 201901101544304316062224
         * receiveName : 李建
         * receivePhone : 13183386761
         * retail_price : 1598.0
         * status : 0
         */

        private String address;
        private double amount;
        private String goodname;
        private String images;
        private long investTime;



        private long audittime;
        private int number;
        private int orderid;
        private String paynum;
        private String receiveName;
        private String receivePhone;
        private double retail_price;
        private int status;
        private int fuelId;
        /**
         * amount : 2500.0
         * factAmount : 298.01
         * fuelId : 35
         * interest : 2201.99
         * retail_price : 500.0
         */

        private double factAmount;
        private double interest;

        public int getFuelId() {
            return fuelId;
        }

        public void setFuelId(int fuelId) {
            this.fuelId = fuelId;
        }

        /**
         * amount : 30.0
         * retail_price : 2598.0
         * trackingName : 1111
         * trackingNumber : 1111
         */

        private String trackingName;
        private String trackingNumber;
        /**
         * amount : 30.0
         * retail_price : 2598.0
         * value : 1200g,白色,XL
         */

        private String value;
        /**
         * amount : 30.0
         * pid : 6
         * retail_price : 2598.0
         */

        private int pid;





        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getGoodname() {
            return goodname;
        }

        public void setGoodname(String goodname) {
            this.goodname = goodname;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public long getInvestTime() {
            return investTime;
        }

        public void setInvestTime(long investTime) {
            this.investTime = investTime;
        }

        public long getAudittime() {
            return audittime;
        }

        public void setAudittime(long audittime) {
            this.audittime = audittime;
        }
        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public int getOrderid() {
            return orderid;
        }

        public void setOrderid(int orderid) {
            this.orderid = orderid;
        }

        public String getPaynum() {
            return paynum;
        }

        public void setPaynum(String paynum) {
            this.paynum = paynum;
        }

        public String getReceiveName() {
            return receiveName;
        }

        public void setReceiveName(String receiveName) {
            this.receiveName = receiveName;
        }

        public String getReceivePhone() {
            return receivePhone;
        }

        public void setReceivePhone(String receivePhone) {
            this.receivePhone = receivePhone;
        }

        public double getRetail_price() {
            return retail_price;
        }

        public void setRetail_price(double retail_price) {
            this.retail_price = retail_price;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getTrackingName() {
            return trackingName;
        }

        public void setTrackingName(String trackingName) {
            this.trackingName = trackingName;
        }

        public String getTrackingNumber() {
            return trackingNumber;
        }

        public void setTrackingNumber(String trackingNumber) {
            this.trackingNumber = trackingNumber;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public double getFactAmount() {
            return factAmount;
        }

        public void setFactAmount(double factAmount) {
            this.factAmount = factAmount;
        }

        public double getInterest() {
            return interest;
        }

        public void setInterest(double interest) {
            this.interest = interest;
        }
    }
}
