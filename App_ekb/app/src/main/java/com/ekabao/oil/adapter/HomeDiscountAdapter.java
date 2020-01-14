package com.ekabao.oil.adapter;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

import com.ekabao.oil.R;
import com.ekabao.oil.bean.HomeHostProduct;
import com.ekabao.oil.ui.view.recyclerview.BaseQuickAdapter;
import com.ekabao.oil.ui.view.recyclerview.BaseViewHolder;
import com.ekabao.oil.util.Arith;

/**
 * desc:加油福利
 * author:tonglj
 * Create date: 2020/1/10
 */
public class HomeDiscountAdapter extends BaseQuickAdapter<HomeHostProduct,BaseViewHolder> {
    public HomeDiscountAdapter() {
        super(R.layout.item_home_discount);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeHostProduct item) {
        helper.setText(R.id.tv_deadline,"例500元/月｜" + item.getDeadline() + "个月");


        SpannableStringBuilder stringBuilder=new SpannableStringBuilder(Arith.mul(item.getRate(), 10.0) + "折");
        AbsoluteSizeSpan   foregroundColorSpan=new AbsoluteSizeSpan(28,true);

        StyleSpan ab=new StyleSpan(Typeface.BOLD);
        stringBuilder.setSpan(foregroundColorSpan,0,stringBuilder.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        stringBuilder.setSpan(ab,0,stringBuilder.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        textView.setText(stringBuilder);


        helper.setText(R.id.tv_discount,stringBuilder);

        helper.setText(R.id.tv_original_price,"立省" + new Double(Arith.mul(Arith.mul(Arith.sub(1, item.getRate()), 500), item.getDeadline())).intValue() + "元");
    }
}
