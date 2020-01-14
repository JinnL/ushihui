package com.ekabao.oil.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ekabao.oil.R;
import com.ekabao.oil.bean.OilCardBean;
import com.ekabao.oil.util.StringCut;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;

import java.util.List;

/**
 * Created by DELL on 2017/11/22.
 */

public class RollViewPersonAdapter extends StaticPagerAdapter {
    private Context context;
    private List<OilCardBean> list;

    //private String[] s = {"http://192.168.1.250:8088/upload/productPic/2017-11/8/20171118593c4c07-5847-4958-a0eb-b2b05c5f1c61.jpg","http://192.168.1.250:8088/upload/productPic/2017-11/6/20171118e159c7d9-0657-4835-807e-a7270cf42073.jpg","http://192.168.1.250:8088/upload/productPic/2017-11/7/201711189e1111da-0dea-4149-8e0a-51fd8850409c.jpg"};

    public RollViewPersonAdapter(Context context, List<OilCardBean> list) {
        this.context = context;
        this.list = list;

        // LogUtils.e("轮播图RollViewAdapter");

    }

    @Override
    public View getView(ViewGroup viewGroup, int i) {

        View itemview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_person_rollview, viewGroup, false);


        TextView tvName = (TextView) itemview.findViewById(R.id.tv_name);
        TextView tvCard = (TextView) itemview.findViewById(R.id.tv_card_num);

        TextView tvMoney1 = (TextView) itemview.findViewById(R.id.tv_money1);
        TextView tvMoney2 = (TextView) itemview.findViewById(R.id.tv_money2);
        TextView tvTime1 = (TextView) itemview.findViewById(R.id.tv_time1);
        TextView tvTime2 = (TextView) itemview.findViewById(R.id.tv_time2);

        LinearLayout llMoney = (LinearLayout) itemview.findViewById(R.id.ll_money);
        ImageButton btLogin = (ImageButton) itemview.findViewById(R.id.bt_login);


        OilCardBean cb = list.get(i);

        if (cb.getType() != 99) {


            llMoney.setVisibility(View.VISIBLE);
            btLogin.setVisibility(View.GONE);


            long nextTime = cb.getLastTime();

            tvMoney1.setText("￥" + cb.getLastAmount());
            if (nextTime == 0) {
                tvTime1.setText("最新充值");
            } else {
                tvTime1.setText("最新充值(" + StringCut.getDateToString(cb.getLastTime()) + ")");
            }

            tvMoney2.setText("￥" + cb.getNextAmount());

            long lastTime = cb.getNextTime();
            if (lastTime == 0) {
                tvTime2.setText("下次充值");
            } else {
                tvTime2.setText("下次充值(" + StringCut.getDateToString(lastTime) + ")");
            }



          /*  ImageView ivBackground = (ImageView) view.findViewById(R.id.iv_background);
            ImageView ivCampany = (ImageView) view.findViewById(R.id.iv_campany);
            TextView tvName = (TextView) view.findViewById(R.id.tv_name);
            TextView tvCard = (TextView) view.findViewById(R.id.tv_card);*/



            tvCard.setText(cb.getCardnum());

            Drawable image;
            //油卡类型 1:中石化 2:中石油
            if (cb.getType() == 1 | cb.getType() == 3) {
                tvName.setText("中石化 油卡");
                image= context.getResources().getDrawable(R.drawable.icon_zhongshihua);
            } else {
                tvName.setText("中石油 油卡");
                // bg_background=R.drawable.bg_oilcard_2;
                image= context.getResources().getDrawable(R.drawable.icon_zhongshiyou);
            }

           // Drawable image = context.getResources().getDrawable(R.drawable.icon_phone_recharge);
            image.setBounds(0, 0, image.getMinimumWidth(), image.getMinimumHeight());//非常重要，必须设置，否则图片不会显示
            tvName.setCompoundDrawables(image, null, null, null);



        } else {

            llMoney.setVisibility(View.GONE);
            btLogin.setVisibility(View.VISIBLE);

        }

        /* position < lsct.size()*/
        // LogUtils.e("CoverFlowAdapter"+lsct.size()+"+position-->"+position+" "+cb.getType());


        // ImageView view = (ImageView) itemview.findViewById(R.id.iv_image);

      /*  ImageView view = new ImageView(viewGroup.getContext());
        //ImageLoader.getInstance().displayImage(list.get(i).getImgUrl(),view);


        // GlideRoundTransform glideRoundTransform = new GlideRoundTransform(context,10);
        // Glide.with(this).load(URL).centerCrop().transform(new GlideCircleTransform(this)).into(imv1);
        //只是绘制左上角和右上角圆角
        //glideRoundTransform.setExceptCorner(false, false, true, true);
        //  GlideRoundTransform transformation = new GlideRoundTransform(context,15);
        //只是绘制左上角和右上角圆角
        //transformation.setExceptCorner(false, false, true, true);
        view.setScaleType(ImageView.ScaleType.FIT_XY);
        //view.setPadding(29,0,29,0);
        //view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


        LogUtils.e("轮播图" + list.get(i).getImgUrl());

        Glide.with(context)
                .load(list.get(i).getImgUrl())

                //     .placeholder(R.drawable.bg_home_banner_fail)
                // .error(R.drawable.bg_home_banner_fail)
                //.centerCrop()
                //.transform(glideRoundTransform)
                .into(view);*/



        return itemview;

    }

    @Override
    public int getCount() {
        return list.size();
    }
}
