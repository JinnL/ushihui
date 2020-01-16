package com.ekabao.oil.bean;


import com.ekabao.oil.R;

import java.util.HashMap;
import java.util.Map;


public class BankName_Pic {
    private String bankno;
    private Map<String, Integer> bank_Pic_Map;
    private Map<String, Integer> bank_bg_Map;
    private Map<String, Integer> bank_Pic_white_Map;
    private Integer c;

    public BankName_Pic() {
        super();
        bank_Pic_Map = new HashMap<String, Integer>();
        bank_Pic_Map.put("1", R.drawable.bank_gongshang);
        bank_Pic_Map.put("2", R.drawable.bank_nongye);
        bank_Pic_Map.put("3", R.drawable.bank_jianshe);
        bank_Pic_Map.put("4", R.drawable.bank_zhongguo);
        bank_Pic_Map.put("5", R.drawable.bank_youzheng);
        bank_Pic_Map.put("13", R.drawable.bank_zhongxin);
        bank_Pic_Map.put("14", R.drawable.bank_shanghai);
        bank_Pic_Map.put("15", R.drawable.bank_beijin);
        bank_Pic_Map.put("8", R.drawable.bank_guangda);
        bank_Pic_Map.put("18", R.drawable.bank_huaxia);
        bank_Pic_Map.put("9", R.drawable.bank_guangfa);
        bank_Pic_Map.put("10", R.drawable.bank_pingan);
        bank_Pic_Map.put("11", R.drawable.bank_mingsheng);
        bank_Pic_Map.put("6", R.drawable.bank_zhaoshang);
        bank_Pic_Map.put("7", R.drawable.bank_xingye);
        bank_Pic_Map.put("12", R.drawable.bank_pufa);
        bank_Pic_Map.put("16", R.drawable.bank_jiaotong);
        bank_Pic_Map.put("17", R.drawable.bank_lanzhou);

        bank_Pic_Map.put("21", R.drawable.bank_gongshang);
        bank_Pic_Map.put("22", R.drawable.bank_nongye);
        bank_Pic_Map.put("23", R.drawable.bank_jianshe);
        bank_Pic_Map.put("24", R.drawable.bank_mingsheng);
        bank_Pic_Map.put("25", R.drawable.bank_youzheng);
        bank_Pic_Map.put("26", R.drawable.bank_guangda);
        bank_Pic_Map.put("27", R.drawable.bank_huaxia);
        bank_Pic_Map.put("28", R.drawable.bank_zhaoshang);
        bank_Pic_Map.put("29", R.drawable.bank_zhongguo);
        bank_Pic_Map.put("30", R.drawable.bank_jiaotong);
        bank_Pic_Map.put("31", R.drawable.bank_pufa);
        bank_Pic_Map.put("32", R.drawable.bank_xingye);
        bank_Pic_Map.put("33", R.drawable.bank_zhongxin);
        bank_Pic_Map.put("34", R.drawable.bank_guangfa);
        bank_Pic_Map.put("35", R.drawable.bank_pingan);
        bank_Pic_Map.put("36", R.drawable.bank_shanghai);

        bank_bg_Map = new HashMap<String, Integer>();

        bank_bg_Map.put("1", R.drawable.bg_gongshang);
        bank_bg_Map.put("3", R.drawable.bg_jianshe);
        bank_bg_Map.put("4", R.drawable.bg_zhongguo);
        bank_bg_Map.put("5", R.drawable.bg_youzheng);
        bank_bg_Map.put("14", R.drawable.bg_shanghai);
        bank_bg_Map.put("8", R.drawable.bg_guangda);
        bank_bg_Map.put("18", R.drawable.bg_huaxia);
        bank_bg_Map.put("9", R.drawable.bg_guangfa);
        bank_bg_Map.put("10", R.drawable.bg_pingan);
        bank_bg_Map.put("7", R.drawable.bg_xingye);
        bank_bg_Map.put("12", R.drawable.bg_pufa);
        bank_bg_Map.put("16", R.drawable.bg_jiaotong);

        bank_bg_Map.put("21", R.drawable.bg_gongshang);
        bank_bg_Map.put("23", R.drawable.bg_jianshe);
        bank_bg_Map.put("25", R.drawable.bg_youzheng);
        bank_bg_Map.put("26", R.drawable.bg_guangda);
        bank_bg_Map.put("27", R.drawable.bg_huaxia);
        bank_bg_Map.put("29", R.drawable.bg_zhongguo);
        bank_bg_Map.put("30", R.drawable.bg_jiaotong);
        bank_bg_Map.put("31", R.drawable.bg_pufa);
        bank_bg_Map.put("32", R.drawable.bg_xingye);
        bank_bg_Map.put("34", R.drawable.bg_guangfa);
        bank_bg_Map.put("35", R.drawable.bg_pingan);
        bank_bg_Map.put("36", R.drawable.bg_shanghai);

        bank_Pic_white_Map = new HashMap<String, Integer>();

        bank_Pic_white_Map.put("1", R.drawable.bank_w_gongshang);
        bank_Pic_white_Map.put("3", R.drawable.bank_w_jianshe);
        bank_Pic_white_Map.put("4", R.drawable.bank_w_zhongguo);
        bank_Pic_white_Map.put("5", R.drawable.bank_w_youzheng);
        bank_Pic_white_Map.put("14", R.drawable.bank_w_shanghai);
        bank_Pic_white_Map.put("8", R.drawable.bank_w_guangda);
        bank_Pic_white_Map.put("18", R.drawable.bank_w_huaxia);
        bank_Pic_white_Map.put("9", R.drawable.bank_w_guangfa);
        bank_Pic_white_Map.put("10", R.drawable.bank_w_pingan);
        bank_Pic_white_Map.put("7", R.drawable.bank_w_xingye);
        bank_Pic_white_Map.put("12", R.drawable.bank_w_pufa);
        bank_Pic_white_Map.put("16", R.drawable.bank_w_jiaotong);

        bank_Pic_white_Map.put("21", R.drawable.bank_w_gongshang);
        bank_Pic_white_Map.put("23", R.drawable.bank_w_jianshe);
        bank_Pic_white_Map.put("25", R.drawable.bank_w_youzheng);
        bank_Pic_white_Map.put("26", R.drawable.bank_w_guangda);
        bank_Pic_white_Map.put("27", R.drawable.bank_w_huaxia);
        bank_Pic_white_Map.put("29", R.drawable.bank_w_zhongguo);
        bank_Pic_white_Map.put("30", R.drawable.bank_w_jiaotong);
        bank_Pic_white_Map.put("31", R.drawable.bank_w_pufa);
        bank_Pic_white_Map.put("32", R.drawable.bank_w_xingye);
        bank_Pic_white_Map.put("34", R.drawable.bank_w_guangfa);
        bank_Pic_white_Map.put("35", R.drawable.bank_w_pingan);
        bank_Pic_white_Map.put("36", R.drawable.bank_w_shanghai);




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

            c = R.color.white;
        }
        return c;

    }

    /**
     * 我的银行卡里的 背景
     */
    public Integer bank_bg(String bankno1) {
        for (String key : bank_bg_Map.keySet()) {
            if (bankno1.equals(key)) {
                c = bank_bg_Map.get(key);
                return c;
            }

            c = R.color.white;
        }
        return c;

    }

     public Integer bank_Pic_white(String bankno1) {
        for (String key : bank_Pic_white_Map.keySet()) {
            if (bankno1.equals(key)) {
                c = bank_Pic_white_Map.get(key);
                return c;
            }
            c = R.color.white;
        }
        return c;

    }



}
