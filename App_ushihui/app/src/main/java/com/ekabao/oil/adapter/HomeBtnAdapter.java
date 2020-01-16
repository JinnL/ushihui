package com.ekabao.oil.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ekabao.oil.R;
import com.ekabao.oil.bean.HomeInfoList;
import com.ekabao.oil.ui.view.recyclerview.BaseQuickAdapter;
import com.ekabao.oil.ui.view.recyclerview.BaseViewHolder;

/**
 * desc: 油卡套餐，领取油卡，手机充值，客服中心
 * author:tonglj
 * Create date: 2020/1/10
 */
public class HomeBtnAdapter extends BaseQuickAdapter<HomeInfoList.LogoListBean,BaseViewHolder> {
    public HomeBtnAdapter() {
        super(R.layout.item_home_btn);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeInfoList.LogoListBean item) {
        helper.setText(R.id.tv_tip,item.getTitle());
        ImageView view = helper.getView(R.id.iv_img);
        Glide.with(mContext).load(item.getImgUrl()).placeholder(R.drawable.bg_home_banner_fail)
                .error(R.drawable.bg_home_banner_fail).into(view);
//        helper.setText(R.id.iv_img);
    }
}
