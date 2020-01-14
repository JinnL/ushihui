package com.mcz.xhj.yz.dr_bean;

/**
 * Created by zhulang on 2017/6/22.
 * 城市列表的bean
 */

public class CityNameBean {
    private String city;
    private int cityId;
    private int id;
    private String pinyin;

    public CityNameBean() {
    }

    public CityNameBean(String city, int cityId, int id, String pinyin) {
        this.city = city;
        this.cityId = cityId;
        this.id = id;
        this.pinyin = pinyin;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    @Override
    public String toString() {
        return "CityNameBean{" +
                "city='" + city + '\'' +
                ", cityId=" + cityId +
                ", id=" + id +
                ", pinyin='" + pinyin + '\'' +
                '}';
    }
}
