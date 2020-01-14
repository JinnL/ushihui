package com.mcz.xhj.yz.dr.psw_style_util;

/**
 * 键盘model
 * @author AHF
 *
 */
public class KeyModel {

	private Integer code;
	private String lable;
	
	public KeyModel(Integer code,String lable){
		this.code = code;
		this.lable = lable;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getLable() {
		return lable;
	}

	public void setLable(String lable) {
		this.lable = lable;
	}
	
	
}
