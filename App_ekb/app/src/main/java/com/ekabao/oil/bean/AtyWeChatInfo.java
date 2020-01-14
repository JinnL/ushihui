package com.ekabao.oil.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 闪油侠 Oil_syx
 *
 * @time 2018/11/13 15:50
 * Created by lbj on 2018/11/13 15:50.
 */

public class AtyWeChatInfo {


    /**
     * wechatMap : {"appid":"wxe9eb5b004527b1ca","noncestr":"yP95qdp4RG7ghOFd9f891g6v33jYEk","out_trade_no":"20181113172545043078109","package":"Sign=WXPay","package_id":"prepay_id=wx131725457221660947dc7b723408983497","packageid":"wx131725457221660947dc7b723408983497","partnerid":"1518645191","paySign":"FB35C275B7E0B5C9F9048993936A9673","prepayid":"wx131725457221660947dc7b723408983497","timestamp":"1542101145"}
     */

    private WechatMapBean wechatMap;

    public WechatMapBean getWechatMap() {
        return wechatMap;
    }

    public void setWechatMap(WechatMapBean wechatMap) {
        this.wechatMap = wechatMap;
    }

    public static class WechatMapBean {
        /**
         * appid : wxe9eb5b004527b1ca
         * noncestr : yP95qdp4RG7ghOFd9f891g6v33jYEk
         * out_trade_no : 20181113172545043078109
         * package : Sign=WXPay
         * package_id : prepay_id=wx131725457221660947dc7b723408983497
         * packageid : wx131725457221660947dc7b723408983497
         * partnerid : 1518645191
         * paySign : FB35C275B7E0B5C9F9048993936A9673
         * prepayid : wx131725457221660947dc7b723408983497
         * timestamp : 1542101145
         */

        private String appid;
        private String noncestr;
        private String out_trade_no;
        @SerializedName("package")
        private String packageX;
        private String package_id;
        private String packageid;
        private String partnerid;
        private String paySign;
        private String prepayid;
        private String timestamp;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getOut_trade_no() {
            return out_trade_no;
        }

        public void setOut_trade_no(String out_trade_no) {
            this.out_trade_no = out_trade_no;
        }

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getPackage_id() {
            return package_id;
        }

        public void setPackage_id(String package_id) {
            this.package_id = package_id;
        }

        public String getPackageid() {
            return packageid;
        }

        public void setPackageid(String packageid) {
            this.packageid = packageid;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPaySign() {
            return paySign;
        }

        public void setPaySign(String paySign) {
            this.paySign = paySign;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
    }
}
