package com.ekabao.oil.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ekabao.oil.R;
import com.ekabao.oil.adapter.viewholder.BaseViewHolder;
import com.ekabao.oil.bean.OilCardBean;

import java.util.List;

/**
 * 易卡宝  App
 *
 * @time 2018/7/19 17:12
 * Created by lj on 2018/7/19 17:12.
 */

public class OilCardAdapter extends BaseRecyclerViewAdapter {
    List<OilCardBean> lslbs;
    private String amount;//

    public OilCardAdapter(RecyclerView view, List list, int itemLayoutId) {
        super(view, list, itemLayoutId);
        this.lslbs = list;
    }

    public OilCardAdapter(RecyclerView view, List list, int itemLayoutId, String money) {
        super(view, list, itemLayoutId);
        this.lslbs = list;
        this.amount = money;
    }


    @Override
    public void bindConvert(final BaseViewHolder holder, final int position, Object item, boolean isScrolling) {
        OilCardBean cb = lslbs.get(position);

        RelativeLayout rlBackground = holder.getView(R.id.rl_background);
        ImageView ivCampany = holder.getView(R.id.iv_campany);
        ImageView ivDelete = holder.getView(R.id.iv_delete);
        TextView tvName = holder.getView(R.id.tv_name);
        TextView tvCard = holder.getView(R.id.tv_card);

        ivDelete.setClickable(true);
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // onItemViewClickListener.onItemViewClick();
                // OnItemViewClickListener
                setViewClickListener(v, position);
                // setonItemViewClickListener(this);
            }
        });

        //油卡类型 1:中石化 2:中石油
        //油卡类型 1:中石化 2:中石油 3:公司中石化 4:公司中石油
        switch (cb.getType()) {
            case 1:
            case 3:// 返现

                rlBackground.setBackgroundResource(R.drawable.bg_oilcard1);
                tvName.setText("中石化 油卡");
                ivCampany.setImageResource(R.drawable.icon_zhongshihua);
                tvCard.setText(cb.getCardnum());

                break;

            case 2:// 加息
            case 4:// 加息
                rlBackground.setBackgroundResource(R.drawable.bg_oilcard2);
                tvName.setText("中石油 油卡");
                ivCampany.setImageResource(R.drawable.icon_zhongshiyou);
                tvCard.setText(cb.getCardnum());
                break;
            default:
                //ivCampany.setImageResource(R.drawable.icon_zhongshiyou);

                tvCard.setText(cb.getCardnum());
                break;

        }

    }


}
