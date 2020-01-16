package com.ekabao.oil.adapter;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
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
        //设置删除线
        TextView tvOriginalPrice = helper.getView(R.id.tv_original_price);
        tvOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);


        String  text= "3200元";
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(text);
        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(28, true);
        StyleSpan ab = new StyleSpan(Typeface.BOLD);
        stringBuilder.setSpan(absoluteSizeSpan, 0, stringBuilder.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        stringBuilder.setSpan(ab, 0, stringBuilder.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        helper.setText(R.id.tv_price, stringBuilder);
    }
}
