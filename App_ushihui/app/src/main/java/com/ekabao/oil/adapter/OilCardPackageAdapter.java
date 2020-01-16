package com.ekabao.oil.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ekabao.oil.R;
import com.ekabao.oil.bean.OilCardPackageBean;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.util.Arith;

import java.math.BigDecimal;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * ${APP_NAME}  App_oil
 *
 * @time 2018/11/3 17:28
 * Created by lj on 2018/11/3 17:28.
 */

public class OilCardPackageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    ArrayList<OilCardPackageBean> list;
    int Position;
    int type;
    int pid;

    public OilCardPackageAdapter(ArrayList<OilCardPackageBean> list, int position, int type) {
        this.list = list;
        this.Position = position;
        this.type = type;
    }

    public void setPosition(int position) {
        this.Position = position;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_oilcard_package, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder myHolder = (ViewHolder) holder;
        OilCardPackageBean item = list.get(position);
        int deadline = item.getDeadline();


        if (Position == position) {
            //shape_oilcard_checked

            myHolder.clBackground.setBackgroundResource(R.drawable.bg_oil_card_home_selected);
            myHolder.tvDiscount.setTextColor(LocalApplication.context.getResources().getColor(R.color.white));
            myHolder.tvDiscount.setBackgroundResource(R.drawable.bg_oil_package_selected);
            //#FFFFFF FFC2C7
        } else {
            //shape_oilcard_check
            myHolder.clBackground.setBackgroundResource(R.drawable.bg_oil_card_home);
            myHolder.tvDiscount.setTextColor(LocalApplication.context.getResources().getColor(R.color.text_black));
            myHolder.tvDiscount.setBackgroundResource(R.color.line_f6);


          /*  if (deadline==12||deadline==18){
                myHolder.ivIcon.setVisibility(View.VISIBLE);
                myHolder.ivIcon.setImageResource(R.drawable.icon_oil_package_activity);

            }*/
        }

        if (type == 1) {
            //LogUtils.e("getActivityRate"+item.getActivityRate());
            if (item.getActivityRate() == 0) {
                myHolder.tvDiscount.setText(Arith.mul(item.getRate(), 10.0) + "折");

            } else {
                if (Position == position) {
                    myHolder.tvDiscount.setTextColor(LocalApplication.context.getResources().getColor(R.color.white));
                    myHolder.tvOlddiscount.setTextColor(Color.parseColor("#FFC2C7"));
                } else {
                    myHolder.tvDiscount.setTextColor(0xffFF6571);
                    myHolder.tvOlddiscount.setTextColor(Color.parseColor("#999999"));
                }

                // 中间加横线
                myHolder.tvOlddiscount.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                myHolder.tvOlddiscount.getPaint().setAntiAlias(true);// 抗锯齿

                myHolder.tvDiscount.setText(Arith.mul(item.getRate(), 10.0) + "折");
                myHolder.tvOlddiscount.setText(Arith.mul(item.getActivityRate(), 10.0) + "折");
            }


            BigDecimal b1 = new BigDecimal(Double.toString(item.getRate()));
            BigDecimal b2 = new BigDecimal(Double.toString(10.0));

            // LogUtils.e(item.getRate() + "套餐" + (b1.multiply(b2).doubleValue()));

            // myHolder.tvDiscount.setText((b1.multiply(b2).doubleValue()) + "折");

            if (item.getSoldNum() != 0) {
                myHolder.tvMonth.setText(item.getDeadline() + "个月,已售" + item.getSoldNum());
            } else {
                myHolder.tvMonth.setText(item.getSimpleName());
            }


            //int deadline = item.getDeadline();
            if (deadline == 12 || deadline == 18) {
                if (Position == position) {
                    myHolder.ivIcon.setVisibility(View.VISIBLE);
                    myHolder.ivIcon.setImageResource(R.drawable.icon_oil_package_activity_yellow);

                } else {
                    myHolder.ivIcon.setVisibility(View.VISIBLE);
                    myHolder.ivIcon.setImageResource(R.drawable.icon_oil_package_activity);

                }

            } else if (deadline == 24) {
                myHolder.ivIcon.setVisibility(View.VISIBLE);

                myHolder.ivIcon.setImageResource(R.drawable.icon_oil_package_limit);
            } else {
                myHolder.ivIcon.setVisibility(View.GONE);
            }

        } else if (type == 2) {
            myHolder.ivIcon.setVisibility(View.GONE);

            myHolder.tvDiscount.setText(Arith.mul(item.getRate(), 10.0) + "折");

            myHolder.tvMonth.setText(item.getDeadline() + "个月");

        } else if (type == 4) {


            String text = item.getLeastaAmount() + "元";

            SpannableString ss = new SpannableString(text);//定义hint的值
            AbsoluteSizeSpan ass = new AbsoluteSizeSpan(23, true);//设置字体大小 true表示单位是sp
            ss.setSpan(ass, 0, ss.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            // AbsoluteSizeSpan ass2 = new AbsoluteSizeSpan(18, true);//设置字体大小 true表示单位是sp
            //  ss.setSpan(ass2, 2, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


            myHolder.tvDiscount.setText(ss);
            myHolder.tvOlddiscount.setVisibility(View.GONE);

            myHolder.tvMonth.setText("支付" + Arith.mul(item.getRate(), item.getLeastaAmount()) + "元");

        } else {
            BigDecimal b1 = new BigDecimal(Double.toString(item.getRate()));
            BigDecimal b2 = new BigDecimal(item.getLeastaAmount());

            myHolder.tvDiscount.setText(item.getSimpleName());
            myHolder.tvMonth.setText("支付" + (b1.multiply(b2).doubleValue()) + "元");
        }


        /*BigDecimal b1 = new BigDecimal(Double.toString(item.getRate()));
        BigDecimal b2 = new BigDecimal(Double.toString(10.0));

       // LogUtils.e(item.getRate() + "套餐" + (b1.multiply(b2).doubleValue()));

        myHolder.tvDiscount.setText((b1.multiply(b2).doubleValue()) + "折");
        myHolder.tvMonth.setText(item.getSimpleName());*/

        if (mOnItemClickLitener != null) {
            myHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(myHolder.itemView, position);
                }
            });
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public interface OnItemClickLitener {
        void onItemClick(View view, int positon);
    }

    OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_discount)
        TextView tvDiscount;
        @BindView(R.id.tv_month)
        TextView tvMonth;
        @BindView(R.id.tv_olddiscount)
        TextView tvOlddiscount;
        @BindView(R.id.cl_background)
        ConstraintLayout clBackground;
        @BindView(R.id.iv_icon)
        ImageView ivIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
