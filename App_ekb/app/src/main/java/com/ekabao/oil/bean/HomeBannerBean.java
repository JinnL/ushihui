package com.ekabao.oil.bean;

/**
 * ${APP_NAME}  App_oil
 *  定义个 公共的 banner
 * @time 2019/1/7 14:11
 * Created by lj on 2019/1/7 14:11.
 */

public class HomeBannerBean  extends HomeInfo.BannerBean {


    /**
     * code : 6
     * id : 85
     * imgUrl : http://m.middleman.cn/upload/banner/2019-01/201901031e1767cd-c300-4391-aa82-8c1e51b61493.png
     * location : #
     * minVersion :
     * remark :
     * sort : 3
     * title : banner-鐚�3
     */

    private String code;
    private int id;
    public String imgUrl;
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
