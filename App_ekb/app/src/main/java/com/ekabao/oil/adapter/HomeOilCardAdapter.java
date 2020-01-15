package com.ekabao.oil.adapter;

import android.graphics.Paint;
import android.widget.TextView;

import com.ekabao.oil.R;
import com.ekabao.oil.ui.view.recyclerview.BaseQuickAdapter;
import com.ekabao.oil.ui.view.recyclerview.BaseViewHolder;

/**
 * desc:实体油卡
 * author:tonglj
 * Create date: 2020/1/10
 */
public class HomeOilCardAdapter extends BaseQuickAdapter<String,BaseViewHolder> {
    public HomeOilCardAdapter() {
        super(R.layout.item_oil_card);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        TextView tvOriginalPrice = helper.getView(R.id.tv_original_price);

        tvOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }
}
