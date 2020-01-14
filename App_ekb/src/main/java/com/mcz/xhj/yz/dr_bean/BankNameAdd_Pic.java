package com.mcz.xhj.yz.dr_bean;

import com.mcz.xhj.R;

import java.util.HashMap;
import java.util.Map;


public class BankNameAdd_Pic {
	private String bankno;
	private Map<String, Integer> bank_Pic_Map;
	private Integer c;

	public BankNameAdd_Pic(String bankno) {
		super();
		this.bankno = bankno;
		bank_Pic_Map = new HashMap<String, Integer>();
		bank_Pic_Map.put("1", R.mipmap.bank_gongshang_add);
		bank_Pic_Map.put("2", R.mipmap.bank_nongye_add);
		bank_Pic_Map.put("3", R.mipmap.bank_jianshe_add);
		bank_Pic_Map.put("4", R.mipmap.bank_zhongguo_add);
		bank_Pic_Map.put("5", R.mipmap.bank_youzheng_add);
		bank_Pic_Map.put("13", R.mipmap.bank_zhongxin_add);
		bank_Pic_Map.put("14", R.mipmap.bank_shanghai_add);
		bank_Pic_Map.put("15", R.mipmap.bank_beijin_add);
		bank_Pic_Map.put("8", R.mipmap.bank_guangda_add);
		bank_Pic_Map.put("18", R.mipmap.bank_huaxia_add);
		bank_Pic_Map.put("9", R.mipmap.bank_guangfa_add);
		bank_Pic_Map.put("10", R.mipmap.bank_pingan_add);
		bank_Pic_Map.put("11", R.mipmap.bank_mingsheng_add);
		bank_Pic_Map.put("6", R.mipmap.bank_zhaoshang_add);
		bank_Pic_Map.put("7", R.mipmap.bank_xingye_add);
		bank_Pic_Map.put("12", R.mipmap.bank_pufa_add);
		bank_Pic_Map.put("16", R.mipmap.bank_jiaotong_add);
		bank_Pic_Map.put("17", R.mipmap.bank_lanzhou_add);

	}

	public Integer bank_Pic() {
			for (String key : bank_Pic_Map.keySet()) {
				if (bankno.equals(key)) {
					c = bank_Pic_Map.get(key);
					return c;
				}
			
				c = R.color.white ;
			}
			return c;

	}
	public Integer bank_Pic(String bankno1) {
			for (String key : bank_Pic_Map.keySet()) {
				if (bankno1.equals(key)) {
					c = bank_Pic_Map.get(key);
					return c;
				}

				c = R.color.white ;
			}
			return c;

	}

}
