package com.ekabao.oil.bean;


import java.io.Serializable;
import java.util.List;

/**
 * 闪油侠 Oil_syx
 *
 * @time 2019/1/4 16:10
 * Created by lbj on 2019/1/4 16:10.
 */

public class AtyCarBreakInfo {


    /**
     * map : {"carList":[{"city":"ZJ_SXX","classno":"295239","createtime":1546503379000,"data":"[{\"date\":\"2018-08-18 17:05:30\",\"area\":\"京福线1581公里900米\",\"archiveno\":\"\",\"act\":\"驾驶中型以上载客载货汽车、危险物品运输车辆以外的其他机动车行驶超过规定时速10%未达20%的\",\"code\":\"1352\",\"money\":\"200\",\"handled\":\"0\",\"wzcity\":\"\",\"fen\":\"3\"}]","dataList":[{"date":"2018-08-18 17:05:30","area":"京福线1581公里900米","archiveno":"","act":"驾驶中型以上载客载货汽车、危险物品运输车辆以外的其他机动车行驶超过规定时速10%未达20%的","code":"1352","money":"200","handled":"0","wzcity":"","fen":"3"}],"deductPoints":3,"engineno":"668905","fine":200,"hphm":"浙DPF705","hpzl":2,"id":4,"isDelete":0,"pending":1,"uid":402,"updatetime":1546913717000},{"city":"AH_HEFEI","classno":"123456","createtime":1546848360000,"data":"","deductPoints":3,"engineno":"96584","fine":200,"hphm":"皖A9X987","hpzl":2,"id":14,"isDelete":0,"pending":1,"uid":402,"updatetime":1546913717000}]}
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
        private List<CarListBean> carList;

        public List<CarListBean> getCarList() {
            return carList;
        }

        public void setCarList(List<CarListBean> carList) {
            this.carList = carList;
        }

        public static class CarListBean {
            /**
             * city : ZJ_SXX
             * classno : 295239
             * createtime : 1546503379000
             * data : [{"date":"2018-08-18 17:05:30","area":"京福线1581公里900米","archiveno":"","act":"驾驶中型以上载客载货汽车、危险物品运输车辆以外的其他机动车行驶超过规定时速10%未达20%的","code":"1352","money":"200","handled":"0","wzcity":"","fen":"3"}]
             * dataList : [{"date":"2018-08-18 17:05:30","area":"京福线1581公里900米","archiveno":"","act":"驾驶中型以上载客载货汽车、危险物品运输车辆以外的其他机动车行驶超过规定时速10%未达20%的","code":"1352","money":"200","handled":"0","wzcity":"","fen":"3"}]
             * deductPoints : 3
             * engineno : 668905
             * fine : 200
             * hphm : 浙DPF705
             * hpzl : 2
             * id : 4
             * isDelete : 0
             * pending : 1
             * uid : 402
             * updatetime : 1546913717000
             */

            private String city;
            private String classno;
            private long createtime;
            private String data;
            private int deductPoints;
            private String engineno;
            private int fine;
            private String hphm;
            private int hpzl;
            private int id;
            private int isDelete;
            private int pending;
            private int uid;
            private long updatetime;
            private List<DataListBean> dataList;

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getClassno() {
                return classno;
            }

            public void setClassno(String classno) {
                this.classno = classno;
            }

            public long getCreatetime() {
                return createtime;
            }

            public void setCreatetime(long createtime) {
                this.createtime = createtime;
            }

            public String getData() {
                return data;
            }

            public void setData(String data) {
                this.data = data;
            }

            public int getDeductPoints() {
                return deductPoints;
            }

            public void setDeductPoints(int deductPoints) {
                this.deductPoints = deductPoints;
            }

            public String getEngineno() {
                return engineno;
            }

            public void setEngineno(String engineno) {
                this.engineno = engineno;
            }

            public int getFine() {
                return fine;
            }

            public void setFine(int fine) {
                this.fine = fine;
            }

            public String getHphm() {
                return hphm;
            }

            public void setHphm(String hphm) {
                this.hphm = hphm;
            }

            public int getHpzl() {
                return hpzl;
            }

            public void setHpzl(int hpzl) {
                this.hpzl = hpzl;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getIsDelete() {
                return isDelete;
            }

            public void setIsDelete(int isDelete) {
                this.isDelete = isDelete;
            }

            public int getPending() {
                return pending;
            }

            public void setPending(int pending) {
                this.pending = pending;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public long getUpdatetime() {
                return updatetime;
            }

            public void setUpdatetime(long updatetime) {
                this.updatetime = updatetime;
            }

            public List<DataListBean> getDataList() {
                return dataList;
            }

            public void setDataList(List<DataListBean> dataList) {
                this.dataList = dataList;
            }

            public static class DataListBean implements Serializable {
                /**
                 * date : 2018-08-18 17:05:30
                 * area : 京福线1581公里900米
                 * archiveno :
                 * act : 驾驶中型以上载客载货汽车、危险物品运输车辆以外的其他机动车行驶超过规定时速10%未达20%的
                 * code : 1352
                 * money : 200
                 * handled : 0
                 * wzcity :
                 * fen : 3
                 */

                private String date;
                private String area;
                private String archiveno;
                private String act;
                private String code;
                private String money;
                private String handled;
                private String wzcity;
                private String fen;

                @Override
                public String toString() {
                    return "DataListBean{" +
                            "date='" + date + '\'' +
                            ", area='" + area + '\'' +
                            ", act='" + act + '\'' +
                            ", money='" + money + '\'' +
                            ", wzcity='" + wzcity + '\'' +
                            ", fen=" + fen +
                            '}';
                }

                public String getDate() {
                    return date;
                }

                public void setDate(String date) {
                    this.date = date;
                }

                public String getArea() {
                    return area;
                }

                public void setArea(String area) {
                    this.area = area;
                }

                public String getArchiveno() {
                    return archiveno;
                }

                public void setArchiveno(String archiveno) {
                    this.archiveno = archiveno;
                }

                public String getAct() {
                    return act;
                }

                public void setAct(String act) {
                    this.act = act;
                }

                public String getCode() {
                    return code;
                }

                public void setCode(String code) {
                    this.code = code;
                }

                public String getMoney() {
                    return money;
                }

                public void setMoney(String money) {
                    this.money = money;
                }

                public String getHandled() {
                    return handled;
                }

                public void setHandled(String handled) {
                    this.handled = handled;
                }

                public String getWzcity() {
                    return wzcity;
                }

                public void setWzcity(String wzcity) {
                    this.wzcity = wzcity;
                }

                public String getFen() {
                    return fen;
                }

                public void setFen(String fen) {
                    this.fen = fen;
                }
            }
        }
    }
}
