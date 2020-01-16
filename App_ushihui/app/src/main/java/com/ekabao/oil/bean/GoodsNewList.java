package com.ekabao.oil.bean;

/**
 * ${APP_NAME}  App_oil
 *  首页的下面的商品列表
 * @time 2019/1/4 15:09
 * Created by lj on 2019/1/4 15:09.
 */

public class GoodsNewList {


    /**
     * code : 3
     * id : 102
     * imgUrl : https://m.ekabao.cn/upload/banner/2019-03/20190328c6760516-f22b-4f33-ab9e-4b0cd2ef083a.png
     * location : https://m.ekabao.cn/commodity-details?id=29
     * minVersion : 1.0.0
     * remark :
     * sort : 6
     * title : 多功能台灯
     */

    private String code;
    private int id;
    private String imgUrl;
    private String location;
    private String minVersion;
    private String remark;
    private int sort;
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

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
