package com.ekabao.oil.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ekabao.oil.R;
import com.ekabao.oil.bean.ProductDetailInfo;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.util.StringCut;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2018/6/1.
 */

public class ProductInvestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ProductDetailInfo.InvestListBean> list;

    private Context ctx;

    public static final int VIEW_TYPE_ONE = 1;
    public static final int VIEW_TYPE_TWO = 2;
    private int type;  //类型 2有排行奖励的

    public ProductInvestAdapter(List<ProductDetailInfo.InvestListBean> list ,int Type) {
        this.list = list;
        this.type = Type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        switch (viewType) {
            case VIEW_TYPE_ONE:
                viewHolder = new ViewHolder1(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scoreboard1, parent, false));

                break;

            case VIEW_TYPE_TWO:
                viewHolder = new ViewHolder2(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scoreboard2, parent, false));

                break;


        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ProductDetailInfo.InvestListBean bean = list.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_ONE:

                ViewHolder1 myHolder = (ViewHolder1) holder;

                //int rank = position % 3;

                int tender_money_distribution = bean.getTender_money_distribution();
                if (position == 0) {
                    // myHolder.imgAvatar.setBackgroundResource(R.drawable.img_crown_first);
                    Glide.with(LocalApplication.context).load(R.drawable.img_crown_first).into(myHolder.imgAvatar);

                    myHolder.tvContent.setText(Html.fromHtml("本期出借满额后，第1名可获得<font color='#EE4845'>" + tender_money_distribution + "元</font>"));

                } else if (position == 1) {
                    //myHolder.imgAvatar.setBackgroundResource(R.drawable.img_crown_second);
                    Glide.with(LocalApplication.context).load(R.drawable.img_crown_second).into(myHolder.imgAvatar);

                    myHolder.tvContent.setText(Html.fromHtml("本期出借满额后，第2名可获得<font color='#EE4845'>" + tender_money_distribution + "元</font>"));

                } else if (position == 2) {

                    Glide.with(LocalApplication.context).load(R.drawable.img_crown_third).into(myHolder.imgAvatar);
                    // myHolder.imgAvatar.setBackgroundResource(R.drawable.img_crown_third);
                    myHolder.tvContent.setText(Html.fromHtml("本期出借满额后，第3名可获得<font color='#EE4845'>" + tender_money_distribution + "元</font>"));

                }

                myHolder.tvPhone.setText(bean.getMobilephone());
                myHolder.tvMoney.setText(bean.getTenderAccountSum() + "元");
                myHolder.tvTime.setText(StringCut.getDateTimeToString(bean.getInvestTime()));
                myHolder.btInvest.setVisibility(View.VISIBLE);

                if (mOnItemClickLitener != null) {
                    myHolder.btInvest.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnItemClickLitener.onItemClick(v,position);
                        }
                    });
                }

                break;
            case VIEW_TYPE_TWO:
                ViewHolder2 myHolder2 = (ViewHolder2) holder;
                myHolder2.tvNumber.setText((position +1) + "");
                myHolder2.tvPhone.setText(bean.getMobilephone());
                myHolder2.tvMoney.setText(bean.getTenderAccountSum() + "元");
                myHolder2.tvTime.setText(StringCut.getDateTimeToString(bean.getInvestTime()));
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (type==0){
            return VIEW_TYPE_TWO;
        }else {
            if (position == 0 || position == 1 || position == 2) {
                return VIEW_TYPE_ONE;
            } else {
                return VIEW_TYPE_TWO;
            }
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

     /*   void onItemClick2(int  toPosition, int position);

        void onDeleteItemClick(int id);*/
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    static class ViewHolder1 extends RecyclerView.ViewHolder {
        @BindView(R.id.img_avatar)
        CircleImageView imgAvatar;
        @BindView(R.id.tv_phone)
        TextView tvPhone;
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_money)
        TextView tvMoney;
        @BindView(R.id.bt_invest)
        Button btInvest;

        ViewHolder1(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class ViewHolder2 extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_number)
        TextView tvNumber;
        @BindView(R.id.tv_phone)
        TextView tvPhone;
        @BindView(R.id.tv_money)
        TextView tvMoney;
        @BindView(R.id.tv_time)
        TextView tvTime;

        ViewHolder2(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


}
