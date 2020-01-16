package com.ekabao.oil.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ekabao.oil.R;
import com.ekabao.oil.bean.HomeHostProduct;
import com.ekabao.oil.util.Arith;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * ${APP_NAME}  App_oil
 *
 * @time 2018/11/3 17:28
 * Created by lj on 2018/11/3 17:28.
 */

public class OilCardPackageHomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    List<HomeHostProduct> list;
    int Position;
    int type;
    int pid;

    public static final int VIEW_FIRST = 0;
    public static final int VIEW_ELSE = 1;

    public OilCardPackageHomeAdapter(List<HomeHostProduct> list, int position, int type) {
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
//        if (viewType == VIEW_FIRST) {
//            View view = LayoutInflater.from(context)
//                    .inflate(R.layout.item_home_rv_5, parent, false);
//            return new FirstViewHolder(view);
//        } else {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_home_rv_3, parent, false);
        return new ViewHolder(view);
//        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        HomeHostProduct bean1 = list.get(position);

        int deadline = bean1.getDeadline();
        Log.d("OilCard   ", bean1.getLeastaAmount() + "   " + bean1.getDeadline() + "    " + bean1.getActivityRate());
        final ViewHolder myHolder = (ViewHolder) holder;

        myHolder.tvDeadline.setText("例500元/月｜" + deadline + "个月");

        myHolder.tvDiscount.setText(Arith.mul(bean1.getRate(), 10.0) + "");

        myHolder.tvDexrease.setText("立省" + new Double(Arith.mul(Arith.mul(Arith.sub(1, bean1.getRate()), 500), bean1.getDeadline())).intValue() + "元");
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

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_FIRST;
        } else {
            return VIEW_ELSE;
        }
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int positon);
    }

    OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_discount)
        TextView tvDiscount;
        @BindView(R.id.tv_deadline)
        TextView tvDeadline;
        @BindView(R.id.cl_package)
        ConstraintLayout clPackage;
        @BindView(R.id.tv_decrease)
        TextView tvDexrease;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
