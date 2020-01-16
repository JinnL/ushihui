package com.ekabao.oil.bean;

import java.util.List;

/**
 * 闪油侠 Oil_syx
 *
 * @time 2019/1/2 16:36
 * Created by lbj on 2019/1/2 16:36.
 */

public class AtyLocationInfo {

    private List<DataListBean> dataList;

    public List<DataListBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<DataListBean> dataList) {
        this.dataList = dataList;
    }

    public static class DataListBean {
        /**
         * area : 310020
         * fwlsmc : 加油卡,便利店,加油卡充值业务
         * address : 浙江省杭州市江干区采荷支路7号西南
         * areaname : 浙江省 杭州市 江干区
         * distance : 207
         * exhaust : 国Ⅴ
         * discount : 非打折加油站
         * lon : 120.20398582885
         * brandname : 中石化
         * type : 其他
         * price : {"E97":"7.14","E0":"6.34","E90":"-","E93":"6.72"}
         * name : 中石化采荷加油站
         * id : 3192
         * position : 120.19742,30.250671
         * lat : 30.256452876451
         */

        private String area;
        private String fwlsmc;
        private String address;
        private String areaname;
        private int distance;
        private String exhaust;
        private String discount;
        private Double lon;
        private String brandname;
        private String type;
        private PriceBean price;
        private String name;
        private String id;
        private String position;
        private Double lat;

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getFwlsmc() {
            return fwlsmc;
        }

        public void setFwlsmc(String fwlsmc) {
            this.fwlsmc = fwlsmc;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAreaname() {
            return areaname;
        }

        public void setAreaname(String areaname) {
            this.areaname = areaname;
        }

        public int getDistance() {
            return distance;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        public String getExhaust() {
            return exhaust;
        }

        public void setExhaust(String exhaust) {
            this.exhaust = exhaust;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public Double getLon() {
            return lon;
        }

        public void setLon(Double lon) {
            this.lon = lon;
        }

        public String getBrandname() {
            return brandname;
        }

        public void setBrandname(String brandname) {
            this.brandname = brandname;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public PriceBean getPrice() {
            return price;
        }

        public void setPrice(PriceBean price) {
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public Double getLat() {
            return lat;
        }

        public void setLat(Double lat) {
            this.lat = lat;
        }

        public static class PriceBean {
            /**
             * E97 : 7.14
             * E0 : 6.34
             * E90 : -
             * E93 : 6.72
             */

            private String E97;
            private String E0;
            private String E90;
            private String E93;

            public String getE97() {
                return E97;
            }

            public void setE97(String E97) {
                this.E97 = E97;
            }

            public String getE0() {
                return E0;
            }

            public void setE0(String E0) {
                this.E0 = E0;
            }

            public String getE90() {
                return E90;
            }

            public void setE90(String E90) {
                this.E90 = E90;
            }

            public String getE93() {
                return E93;
            }

            public void setE93(String E93) {
                this.E93 = E93;
            }
        }
    }
}
