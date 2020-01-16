package com.ekabao.oil.adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.ekabao.oil.R;
import com.ekabao.oil.bean.AtyCarBreakInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by DELL on 2017/10/30.
 * 描述：收货地址的adapter
 */

public class AdapterCarBreak extends RecyclerView.Adapter<AdapterCarBreak.ViewHolder> {

    private Context context;
    private List<AtyCarBreakInfo.MapBean.CarListBean> listCar;
    public CarManageListener mCarManageListener;

    public AdapterCarBreak(Context context, List<AtyCarBreakInfo.MapBean.CarListBean> list) {
        this.context = context;
        this.listCar = list;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_car_break, parent, false );
        ViewHolder viewHolder = new ViewHolder( view );

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final ViewHolder viewHolder = holder;
        AtyCarBreakInfo.MapBean.CarListBean carListBean = listCar.get( position );
        int pending = carListBean.getPending();
        int deductPoints = carListBean.getDeductPoints();
        int fine = carListBean.getFine();
        String hphm = carListBean.getHphm();
        int hpzl = carListBean.getHpzl();

        AssetManager assets = context.getAssets();
        //根据路径得到Typeface
        Typeface tf = Typeface.createFromAsset( assets, "DIN Medium.ttf" );

        //设置字体
        viewHolder.tvRecord.setTypeface( tf );
        viewHolder.tvNumber.setTypeface( tf );
        viewHolder.tvMoney.setTypeface( tf );

        SpannableStringBuilder sp = new SpannableStringBuilder( pending + "条" );
        //  sp.setSpan(new ForegroundColorSpan(0xFFFF0000), name.length(), str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //字体颜色
        sp.setSpan( new AbsoluteSizeSpan( 25, true ), 0, sp.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE ); //字体大小
        viewHolder.tvRecord.setText( sp );

        SpannableStringBuilder spNumber = new SpannableStringBuilder( "-" + deductPoints + "分" );
        //  sp.setSpan(new ForegroundColorSpan(0xFFFF0000), name.length(), str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //字体颜色
        spNumber.setSpan( new AbsoluteSizeSpan( 25, true ), 0, spNumber.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE ); //字体大小
        viewHolder.tvNumber.setText( spNumber );


        SpannableStringBuilder spMoney = new SpannableStringBuilder( fine + "元" );
        //  sp.setSpan(new ForegroundColorSpan(0xFFFF0000), name.length(), str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //字体颜色
        spMoney.setSpan( new AbsoluteSizeSpan( 25, true ), 0, spMoney.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE ); //字体大小
        viewHolder.tvMoney.setText( spMoney );

        //1大车 2小车
        if (hpzl == 1) {
            viewHolder.ivCar.setImageResource( R.drawable.icon_car_big );
        } else {
            viewHolder.ivCar.setImageResource( R.drawable.icon_car_small );
        }
        if (!TextUtils.isEmpty( hphm )) {
            viewHolder.tvCarNum.setText( hphm );
        } else {
            viewHolder.tvCarNum.setText( "暂无" );
        }

        //接口回调
        viewHolder.llCar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mCarManageListener != null) {
                    mCarManageListener.onSelected( position );
                }
            }
        } );
//        viewHolder.ivCarEdit.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mCarManageListener != null) {
//                    mCarManageListener.onDelete( position );
//                }
//            }
//        } );

        viewHolder.ivCarEdit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCarManageListener != null) {
                    mCarManageListener.onEdit( viewHolder.ivCarEdit,position );
                }
            }
        } );

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return listCar.size();
    }


    public interface CarManageListener {
        void onSelected(int position);

        void onEdit(ImageView ivCarEdit, int position);

        //void onDelete(int position);
    }

    public void setOnCarManageListener(CarManageListener listener) {
        this.mCarManageListener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_car)
        ImageView ivCar;
        @BindView(R.id.tv_car_num)
        TextView tvCarNum;
        @BindView(R.id.iv_car_edit)
        ImageView ivCarEdit;
        @BindView(R.id.tv_record)
        TextView tvRecord;
        @BindView(R.id.tv_number)
        TextView tvNumber;
        @BindView(R.id.tv_money)
        TextView tvMoney;

        @BindView(R.id.ll_car)
        LinearLayout llCar;

        ViewHolder(View itemView) {
            super( itemView );
            ButterKnife.bind( this, itemView );
        }
    }
}