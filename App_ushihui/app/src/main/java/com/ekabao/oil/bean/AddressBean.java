package com.ekabao.oil.bean;

import java.io.Serializable;

/**
 * Created by DELL on 2017/10/31.
 * 描述：收货地址javabean。
 */

public class AddressBean implements Serializable{
    private int id;
    private String name;
    private String phone;
    private String address;
    private String postCode;
    private int addressDefault;//1 默认 0 非默认
    /**
     * areaId : 110101
     * areaName : 东城区
     * cityId : 110100
     * cityName : 市辖区
     * provinceId : 110000
     * provinceName : 北京市
     */

    private int areaId;
    private String areaName;
    private int cityId;
    private String cityName;
    private int provinceId;
    private String provinceName;


    public AddressBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public int getAddressDefault() {
        return addressDefault;
    }

    public void setAddressDefault(int addressDefault) {
        this.addressDefault = addressDefault;
    }

    @Override
    public String toString() {
        return "AddressBean{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", postCode='" + postCode + '\'' +
                ", addressDefault=" + addressDefault +
                '}';
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
}
