package com.ekabao.oil.bean;

/**
 * 闪油侠 Oil_syx
 *
 * @time 2019/1/7 14:09
 * Created by lbj on 2019/1/7 14:09.
 */

public class AtyCarCityInfo {

    /**
     * map : {"drJuheCarpre":{"abbr":"浙A","cityCode":"ZJ_HZ","cityName":"杭州","classa":1,"classno":6,"createtime":1546401088000,"engine":1,"engineno":6,"hphm":"浙A","id":1}}
     * success : true
     */

    private MapBean map;
    private boolean success;

    public MapBean getMap() {
        return map;
    }

    public void setMap(MapBean map) {
        this.map = map;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static class MapBean {
        /**
         * drJuheCarpre : {"abbr":"浙A","cityCode":"ZJ_HZ","cityName":"杭州","classa":1,"classno":6,"createtime":1546401088000,"engine":1,"engineno":6,"hphm":"浙A","id":1}
         */

        private DrJuheCarpreBean drJuheCarpre;

        public DrJuheCarpreBean getDrJuheCarpre() {
            return drJuheCarpre;
        }

        public void setDrJuheCarpre(DrJuheCarpreBean drJuheCarpre) {
            this.drJuheCarpre = drJuheCarpre;
        }

        public static class DrJuheCarpreBean {
            /**
             * abbr : 浙A
             * cityCode : ZJ_HZ
             * cityName : 杭州
             * classa : 1
             * classno : 6
             * createtime : 1546401088000
             * engine : 1
             * engineno : 6
             * hphm : 浙A
             * id : 1
             */

            private String abbr;
            private String cityCode;
            private String cityName;
            private int classa;
            private int classno;
            private long createtime;
            private int engine;
            private int engineno;
            private String hphm;
            private int id;

            public String getAbbr() {
                return abbr;
            }

            public void setAbbr(String abbr) {
                this.abbr = abbr;
            }

            public String getCityCode() {
                return cityCode;
            }

            public void setCityCode(String cityCode) {
                this.cityCode = cityCode;
            }

            public String getCityName() {
                return cityName;
            }

            public void setCityName(String cityName) {
                this.cityName = cityName;
            }

            public int getClassa() {
                return classa;
            }

            public void setClassa(int classa) {
                this.classa = classa;
            }

            public int getClassno() {
                return classno;
            }

            public void setClassno(int classno) {
                this.classno = classno;
            }

            public long getCreatetime() {
                return createtime;
            }

            public void setCreatetime(long createtime) {
                this.createtime = createtime;
            }

            public int getEngine() {
                return engine;
            }

            public void setEngine(int engine) {
                this.engine = engine;
            }

            public int getEngineno() {
                return engineno;
            }

            public void setEngineno(int engineno) {
                this.engineno = engineno;
            }

            public String getHphm() {
                return hphm;
            }

            public void setHphm(String hphm) {
                this.hphm = hphm;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }
        }
    }
}
