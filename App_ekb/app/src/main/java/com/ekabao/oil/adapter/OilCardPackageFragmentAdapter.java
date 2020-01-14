package com.ekabao.oil.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * fragment -->水平方向滑动的
 *
 * @time 2018/11/3 17:28
 * Created by lj on 2018/11/3 17:28.
 */

public class OilCardPackageFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    ArrayList<OilCardPackageBean> list;
    int Position;
    int type;
    int pid;


    public OilCardPackageFragmentAdapter(ArrayList<OilCardPackageBean> list, int position, int type) {
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
                .inflate(R.layout.item_oilcard_package_2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder myHolder = (ViewHolder) holder;
        OilCardPackageBean item = list.get(position);
        int deadline = item.getDeadline();

        if (Position == position) {

            myHolder.clPackage.setBackgroundResource(R.drawable.bg_package_selected);
        } else {
            myHolder.clPackage.setBackgroundResource(R.drawable.bg_package_unselected);
        }

        if (type == 1) {

            String text = Arith.mul(item.getRate(), 10.0) + "";

//            SpannableString ss = new SpannableString(text);//定义hint的值
//            AbsoluteSizeSpan ass = new AbsoluteSizeSpan(35, true);//设置字体大小 true表示单位是sp
//            ss.setSpan(ass, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//            AbsoluteSizeSpan ass2 = new AbsoluteSizeSpan(18, true);//设置字体大小 true表示单位是sp
//            ss.setSpan(ass2, 2, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            myHolder.tvDiscount.setText(text);


            BigDecimal b1 = new BigDecimal(Double.toString(item.getRate()));
            BigDecimal b2 = new BigDecimal(Double.toString(10.0));

            // LogUtils.e(item.getRate() + "套餐" + (b1.multiply(b2).doubleValue()));

            // myHolder.tvDiscount.setText((b1.multiply(b2).doubleValue()) + "折");
            myHolder.tvName.setText(item.getDeadline() + "个月套餐");

            if (item.getSoldNum() != 0) {
                myHolder.tvSoldNum.setText("已售:" + item.getSoldNum());

            } else {
                //  myHolder.tvSoldNum.setText(item.getDeadline() + "个月套餐");
            }


        } else if (type == 2) {
            String text = Arith.mul(item.getRate(), 10.0) + "";
//            SpannableString ss = new SpannableString(text);//定义hint的值
//            AbsoluteSizeSpan ass = new AbsoluteSizeSpan(35, true);//设置字体大小 true表示单位是sp
//            ss.setSpan(ass, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//            AbsoluteSizeSpan ass2 = new AbsoluteSizeSpan(18, true);//设置字体大小 true表示单位是sp
//            ss.setSpan(ass2, 2, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            myHolder.tvDiscount.setText(text);


            //  myHolder.tvDiscount.setText(text);

            myHolder.tvName.setText(item.getDeadline() + "个月");


            myHolder.tvSoldNum.setText("已售:" + item.getSoldNum());

        } else if (type == 4) {


            String text = item.getLeastaAmount() + "元";

            SpannableString ss = new SpannableString(text);//定义hint的值
            AbsoluteSizeSpan ass = new AbsoluteSizeSpan(23, true);//设置字体大小 true表示单位是sp
            ss.setSpan(ass, 0, ss.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            // AbsoluteSizeSpan ass2 = new AbsoluteSizeSpan(18, true);//设置字体大小 true表示单位是sp
            //  ss.setSpan(ass2, 2, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


            myHolder.tvDiscount.setText(ss);


            myHolder.tvName.setText("支付" + Arith.mul(item.getRate(), item.getLeastaAmount()) + "元");

        } else {
            BigDecimal b1 = new BigDecimal(Double.toString(item.getRate()));
            BigDecimal b2 = new BigDecimal(item.getLeastaAmount());

            myHolder.tvDiscount.setText(item.getSimpleName());
            myHolder.tvName.setText("支付" + (b1.multiply(b2).doubleValue()) + "元");
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
        @BindView(R.id.tv_discount_title)
        TextView tvDiscountTitle;
        @BindView(R.id.tv_sold_num)
        TextView tvSoldNum;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.cl_package)
        ConstraintLayout clPackage;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
