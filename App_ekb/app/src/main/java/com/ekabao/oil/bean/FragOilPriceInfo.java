package com.ekabao.oil.bean;

/**
 * 闪油侠 Oil_syx
 *
 * @time 2019/1/8 14:03
 * Created by lbj on 2019/1/8 14:03.
 */

public class FragOilPriceInfo {

    /**
     * success : true
     * errorCode : null
     * errorMsg : null
     * map : {"oilCty":{"id":703,"city":"浙江","h92":6.42,"h95":6.83,"h98":7.48,"h0":6.04,"b90":"-","b93":6.42,"b97":6.83,"b0":6.04,"createtime":1546911142000}}
     * msg : null
     */

    private boolean success;
    private Object errorCode;
    private Object errorMsg;
    private MapBean map;
    private Object msg;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Object errorCode) {
        this.errorCode = errorCode;
    }

    public Object getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(Object errorMsg) {
        this.errorMsg = errorMsg;
    }

    public MapBean getMap() {
        return map;
    }

    public void setMap(MapBean map) {
        this.map = map;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public static class MapBean {
        /**
         * oilCty : {"id":703,"city":"浙江","h92":6.42,"h95":6.83,"h98":7.48,"h0":6.04,"b90":"-","b93":6.42,"b97":6.83,"b0":6.04,"createtime":1546911142000}
         */

        private OilCtyBean oilCty;

        public OilCtyBean getOilCty() {
            return oilCty;
        }

        public void setOilCty(OilCtyBean oilCty) {
            this.oilCty = oilCty;
        }

        public static class OilCtyBean {
            /**
             * id : 703
             * city : 浙江
             * h92 : 6.42
             * h95 : 6.83
             * h98 : 7.48
             * h0 : 6.04
             * b90 : -
             * b93 : 6.42
             * b97 : 6.83
             * b0 : 6.04
             * createtime : 1546911142000
             */

            private int id;
            private String city;
            private double h92;
            private double h95;
            private double h98;
            private double h0;
            private String b90;
            private double b93;
            private double b97;
            private double b0;
            private long createtime;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public double getH92() {
                return h92;
            }

            public void setH92(double h92) {
                this.h92 = h92;
            }

            public double getH95() {
                return h95;
            }

            public void setH95(double h95) {
                this.h95 = h95;
            }

            public double getH98() {
                return h98;
            }

            public void setH98(double h98) {
                this.h98 = h98;
            }

            public double getH0() {
                return h0;
            }

            public void setH0(double h0) {
                this.h0 = h0;
            }

            public String getB90() {
                return b90;
            }

            public void setB90(String b90) {
                this.b90 = b90;
            }

            public double getB93() {
                return b93;
            }

            public void setB93(double b93) {
                this.b93 = b93;
            }

            public double getB97() {
                return b97;
            }

            public void setB97(double b97) {
                this.b97 = b97;
            }

            public double getB0() {
                return b0;
            }

            public void setB0(double b0) {
                this.b0 = b0;
            }

            public long getCreatetime() {
                return createtime;
            }

            public void setCreatetime(long createtime) {
                this.createtime = createtime;
            }
        }
    }
}
