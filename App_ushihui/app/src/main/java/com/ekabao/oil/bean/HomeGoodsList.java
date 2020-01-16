package com.ekabao.oil.bean;

import java.util.List;

/**
 * ${APP_NAME}  App_oil
 *  首页的hotList 接口
 * @time 2019/1/4 15:10
 * Created by lj on 2019/1/4 15:10.
 */

public class HomeGoodsList {


    private List<GoodsList> goodslist;
    private List<GoodsMiddlebanner> middlebanner;

    public List<GoodsList> getGoodslist() {
        return goodslist;
    }

    public void setGoodslist(List<GoodsList> goodslist) {
        this.goodslist = goodslist;
    }

    public List<GoodsMiddlebanner> getMiddlebanner() {
        return middlebanner;
    }

    public void setMiddlebanner(List<GoodsMiddlebanner> middlebanner) {
        this.middlebanner = middlebanner;
    }


}
