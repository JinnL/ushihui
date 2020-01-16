package com.mcz.xhj.yz.dr_bean;

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
}
