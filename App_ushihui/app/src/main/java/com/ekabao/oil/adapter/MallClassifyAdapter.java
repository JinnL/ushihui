package com.ekabao.oil.adapter;

import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ekabao.oil.R;
import com.ekabao.oil.adapter.viewholder.BaseViewHolder;
import com.ekabao.oil.bean.GoodsList;

import java.util.List;

import static com.ekabao.oil.global.LocalApplication.context;

/**
 * ${APP_NAME}  App_oil
 * 商品分类
 *
 * @time 2019/1/8 14:08
 * Created by lj on 2019/1/8 14:08.
 */

public class MallClassifyAdapter extends BaseRecyclerViewAdapter {


    private List<GoodsList> list;

    public MallClassifyAdapter(RecyclerView view, List list, int itemLayoutId) {
        super(view, list, itemLayoutId);
        this.list = list;
    }

    @Override
    public void bindConvert(BaseViewHolder holder, int position, Object item, boolean isScrolling) {

        GoodsList goodsList = list.get(position);


        holder.setText(R.id.tv_price, "￥" + goodsList.getRetailPrice());
        holder.setText(R.id.tv_name, goodsList.getName());

        // 中间加横线
        TextView tv = holder.getView(R.id.tv_original_price);
        tv.setText("￥" + goodsList.getMarketPrice());
        tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tv.getPaint().setAntiAlias(true);// 抗锯齿


        ImageView photo = holder.getView(R.id.iv_goods);
        Glide.with(context)
                .load(goodsList.getListPicUrl())
                // .placeholder(R.drawable.bg_home_banner_fail)
                .error(R.drawable.bg_home_banner_fail)
                .centerCrop()
                //.transform(glideRoundTransform)
                .into(photo);

    }

   /* @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }*/
}
