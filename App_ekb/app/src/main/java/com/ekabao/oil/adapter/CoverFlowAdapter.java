package com.ekabao.oil.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.ekabao.oil.R;
import com.ekabao.oil.bean.OilCardBean;
import com.ekabao.oil.util.LogUtils;
import com.ekabao.oil.util.StringCut;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/17.
 */

public class CoverFlowAdapter extends PagerAdapter {
    private Context context;

    private List<OilCardBean> lsct=new ArrayList<>();

    private int type = 1;// 1 我的里面的油卡 2 充值页面里面的油卡

    public CoverFlowAdapter(Context context, List<OilCardBean> lsct, int type) {
        this.context = context;
       // addBottom(lsct);
        this.lsct = lsct;
        this.type = type;
    }

    public void addBottom(List<OilCardBean> list) {
        //if (lsct==null|position==lsct.size()+1)

       // this.lsct = list;
        lsct.clear();
        lsct.addAll(list);

        LogUtils.e("addBottom"+lsct.size());

        lsct.add(new OilCardBean(0,"",0,0,0,99,0));

       // return lsct == null | position == lsct.size();

        //return 99;
        //return Integer.MAX_VALUE;
        //当为最大值时,重新加载时会报anr异常
    }



    @Override
    public int getCount() {


       // return lsct.size() + 1;
        return lsct.size();

        //return 99;
        //return Integer.MAX_VALUE;
        //当为最大值时,重新加载时会报anr异常
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {


      //  LogUtils.e("CoverFlowAdapter"+lsct.size()+"+position-->"+position+"/"+getCount());

        View view;

        if (type == 1) {
            view = LayoutInflater.from(context).inflate(R.layout.item_cover, null);

            //ivBackground.setBackgroundResource(R.drawable.bg_preson_add_oilcard);

              /*  Glide.with(context)
                        .load( R.drawable.bg_oilcard_recharge)
                        //.placeholder(R.drawable.bg_oilcard_recharge)
                        .error(R.drawable.bg_oilcard_recharge)
                        .centerCrop()
                        .into(ivBackground);*/

        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_activity_oilcard, null);
            //ivBackground = (ImageView) view.findViewById(R.id.iv_background);
            //ivBackground.setBackgroundResource(R.drawable.bg_oilcard_recharge);
              /*  Glide.with(context)
                        .load( R.drawable.bg_preson_add_oilcard)
                        //.placeholder(R.drawable.bg_oilcard_recharge)
                        .error(R.drawable.bg_preson_add_oilcard)
                        .centerCrop()
                        .into(ivBackground);*/
        }

        ImageView ivBackground = (ImageView) view.findViewById(R.id.iv_background);
        TextView tvTag = view.findViewById(R.id.tv_tag);
        TextView tvName = (TextView) view.findViewById(R.id.tv_name);
        TextView tvCard = (TextView) view.findViewById(R.id.tv_card);
        ImageView ivMore = (ImageView) view.findViewById(R.id.iv_more);


        OilCardBean cb = lsct.get(position);
        /* position < lsct.size()*/
        LogUtils.e("CoverFlowAdapter"+lsct.size()+"+position-->"+position+" "+cb.getType());

        if (cb.getType()!=99 ) {

            ivBackground.setVisibility(View.VISIBLE);
            tvTag.setVisibility(View.VISIBLE);
            tvName.setVisibility(View.VISIBLE);
            tvCard.setVisibility(View.VISIBLE);
            ivMore.setVisibility(View.VISIBLE);


            if (type == 1) {
               // view = LayoutInflater.from(context).inflate(R.layout.item_cover, null);
                TextView tvMoney1 = (TextView) view.findViewById(R.id.tv_money1);
                TextView tvMoney2 = (TextView) view.findViewById(R.id.tv_money2);
                TextView tvTime1 = (TextView) view.findViewById(R.id.tv_time1);
                TextView tvTime2 = (TextView) view.findViewById(R.id.tv_time2);
                View line = (View) view.findViewById(R.id.line);

                line.setVisibility(View.VISIBLE);

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


            } else {
               // view = LayoutInflater.from(context).inflate(R.layout.item_activity_oilcard, null);
            }


          /*  ImageView ivBackground = (ImageView) view.findViewById(R.id.iv_background);
            ImageView ivCampany = (ImageView) view.findViewById(R.id.iv_campany);
            TextView tvName = (TextView) view.findViewById(R.id.tv_name);
            TextView tvCard = (TextView) view.findViewById(R.id.tv_card);*/

            ivBackground.setClickable(true);

            tvCard.setText(cb.getCardnum());

            int bg_background;

            if (type == 1) {
                if (cb.getType() == 1 | cb.getType() == 3) {
                    bg_background = R.drawable.bg_company1;
                } else {
                    bg_background = R.drawable.bg_campany2;
                }
            } else {
                if (cb.getType() == 1 | cb.getType() == 3) {
                    bg_background = R.drawable.bg_oilcard_1;
                } else {
                    bg_background = R.drawable.bg_oilcard_2;
                }
            }


            //油卡类型 1:中石化 2:中石油
            if (cb.getType() == 1 | cb.getType() == 3) {
                tvName.setText("中石化油卡");
                //bg_background=R.drawable.bg_oilcard_1;
            } else {
                tvName.setText("中石油油卡");
                // bg_background=R.drawable.bg_oilcard_2;
            }

            ivBackground.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(v, position);
                    }


                }
            });

           // return view;

        } else {

            LogUtils.e("222222CoverFlowAdapter"+lsct.size()+"+position-->"+position);

            //ImageView ivBackground;
            ivBackground.setVisibility(View.VISIBLE);
            tvTag.setVisibility(View.GONE);
//            ivCompany.setVisibility(View.GONE);
            tvName.setVisibility(View.GONE);
            tvCard.setVisibility(View.GONE);
            ivMore.setVisibility(View.GONE);



            if (type == 1) {
                //view = LayoutInflater.from(context).inflate(R.layout.item_cover, null);
                //ivBackground = (ImageView) view.findViewById(R.id.iv_background);

                ivBackground.setImageResource(R.drawable.bg_preson_add_oilcard);

              /*  Glide.with(context)
                        .load( R.drawable.bg_oilcard_recharge)
                        //.placeholder(R.drawable.bg_oilcard_recharge)
                        .error(R.drawable.bg_oilcard_recharge)
                        .centerCrop()
                        .into(ivBackground);*/

            } else {
               // view = LayoutInflater.from(context).inflate(R.layout.item_activity_oilcard, null);
               // ivBackground = (ImageView) view.findViewById(R.id.iv_background);
                ivBackground.setImageResource(R.drawable.bg_oilcard_recharge);

              /*  Glide.with(context)
                        .load( R.drawable.bg_preson_add_oilcard)
                        //.placeholder(R.drawable.bg_oilcard_recharge)
                        .error(R.drawable.bg_preson_add_oilcard)
                        .centerCrop()
                        .into(ivBackground);*/
            }


            ivBackground.setClickable(true);

            ivBackground.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onAddOilCardClick(v, position);
                    }

                }
            });

        }
        container.addView(view);
        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }




    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }


    private ItemClickListener itemClickListener;

    public interface ItemClickListener {
        void onItemClick(View view, int position);

        void onAddOilCardClick(View view, int position);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
