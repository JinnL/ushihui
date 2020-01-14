package com.mcz.xhj.yz.dr_bean;

import java.util.HashMap;
import java.util.Map;
import com.mcz.xhj.R;


public class BankName_Pic {
	private String bankno;
	private Map<String, Integer> bank_Pic_Map;
	private Integer c;

	public BankName_Pic() {
		super();
		bank_Pic_Map = new HashMap<String, Integer>();
		bank_Pic_Map.put("1", R.mipmap.bank_gongshang);
		bank_Pic_Map.put("2", R.mipmap.bank_nongye);
		bank_Pic_Map.put("3", R.mipmap.bank_jianshe);
		bank_Pic_Map.put("4", R.mipmap.bank_zhongguo);
		bank_Pic_Map.put("5", R.mipmap.bank_youzheng);
		bank_Pic_Map.put("13", R.mipmap.bank_zhongxin);
		bank_Pic_Map.put("14", R.mipmap.bank_shanghai);
		bank_Pic_Map.put("15", R.mipmap.bank_beijin);
		bank_Pic_Map.put("8", R.mipmap.bank_guangda);
		bank_Pic_Map.put("18", R.mipmap.bank_huaxia);
		bank_Pic_Map.put("9", R.mipmap.bank_guangfa);
		bank_Pic_Map.put("10", R.mipmap.bank_pingan);
		bank_Pic_Map.put("11", R.mipmap.bank_mingsheng);
		bank_Pic_Map.put("6", R.mipmap.bank_zhaoshang);
		bank_Pic_Map.put("7", R.mipmap.bank_xingye);
		bank_Pic_Map.put("12", R.mipmap.bank_pufa);
		bank_Pic_Map.put("16", R.mipmap.bank_jiaotong);
		bank_Pic_Map.put("17", R.mipmap.bank_lanzhou);

		bank_Pic_Map.put("21", R.mipmap.bank_gongshang);
		bank_Pic_Map.put("22", R.mipmap.bank_nongye);
		bank_Pic_Map.put("23", R.mipmap.bank_jianshe);
		bank_Pic_Map.put("24", R.mipmap.bank_mingsheng);
		bank_Pic_Map.put("25", R.mipmap.bank_youzheng);
		bank_Pic_Map.put("26", R.mipmap.bank_guangda);
		bank_Pic_Map.put("27", R.mipmap.bank_huaxia);
		bank_Pic_Map.put("28", R.mipmap.bank_zhaoshang);
		bank_Pic_Map.put("29", R.mipmap.bank_zhongguo);
		bank_Pic_Map.put("30", R.mipmap.bank_jiaotong);
		bank_Pic_Map.put("31", R.mipmap.bank_pufa);
		bank_Pic_Map.put("32", R.mipmap.bank_xingye);
		bank_Pic_Map.put("33", R.mipmap.bank_zhongxin);
		bank_Pic_Map.put("34", R.mipmap.bank_guangfa);
		bank_Pic_Map.put("35", R.mipmap.bank_pingan);

	}
//	public BankName_Pic(String bankno) {
//		super();
//		this.bankno = bankno;
//		bank_Pic_Map = new HashMap<String, Integer>();
//		bank_Pic_Map.put("1", R.mipmap.bank_gongshang);
//		bank_Pic_Map.put("2", R.mipmap.bank_nongye);
//		bank_Pic_Map.put("3", R.mipmap.bank_jianshe);
//		bank_Pic_Map.put("4", R.mipmap.bank_zhongguo);
//		bank_Pic_Map.put("5", R.mipmap.bank_youzheng);
//		bank_Pic_Map.put("13", R.mipmap.bank_zhongxin);
//		bank_Pic_Map.put("14", R.mipmap.bank_shanghai);
//		bank_Pic_Map.put("15", R.mipmap.bank_beijin);
//		bank_Pic_Map.put("8", R.mipmap.bank_guangda);
//		bank_Pic_Map.put("18", R.mipmap.bank_huaxia);
//		bank_Pic_Map.put("9", R.mipmap.bank_guangfa);
//		bank_Pic_Map.put("10", R.mipmap.bank_pingan);
//		bank_Pic_Map.put("11", R.mipmap.bank_mingsheng);
//		bank_Pic_Map.put("6", R.mipmap.bank_zhaoshang);
//		bank_Pic_Map.put("7", R.mipmap.bank_xingye);
//		bank_Pic_Map.put("12", R.mipmap.bank_pufa);
//		bank_Pic_Map.put("16", R.mipmap.bank_jiaotong);
//		bank_Pic_Map.put("17", R.mipmap.bank_lanzhou);
//
//	}

//	public Integer bank_Pic() {
//			for (String key : bank_Pic_Map.keySet()) {
//				if (bankno.equals(key)) {
//					c = bank_Pic_Map.get(key);
//					return c;
//				}
//
//				c = R.color.white ;
//			}
//			return c;
//
//	}

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
