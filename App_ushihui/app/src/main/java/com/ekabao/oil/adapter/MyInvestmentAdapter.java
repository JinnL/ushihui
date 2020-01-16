package com.ekabao.oil.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.widget.TextView;

import com.ekabao.oil.R;
import com.ekabao.oil.adapter.viewholder.BaseViewHolder;
import com.ekabao.oil.bean.MyInvestListBean;
import com.ekabao.oil.util.StringCut;

import java.util.List;

/**
 * $desc$
 *
 * @time $data$ $time$
 * Created by Administrator on 2018/7/16.
 */

public class MyInvestmentAdapter extends BaseRecyclerViewAdapter {

    private List<MyInvestListBean> list;
    public MyInvestmentAdapter(RecyclerView view, List list, int itemLayoutId) {
        super(view, list, itemLayoutId);
        this.list=list;
    }

    @Override
    public void bindConvert(BaseViewHolder holder, int position, Object item, boolean isScrolling) {

        MyInvestListBean bean = list.get(position);
        holder.setText(R.id.tv_name,bean.getFullName());
        holder.setText(R.id.tv_money,StringCut.getNumKb(bean.getAmount()));

        holder.setText(R.id.tv_time, StringCut.getDateTimeToStringheng(Long.parseLong(bean.getExpireDate())));

        holder.setText(R.id.tv_fit,"预计收益"+bean.getExpireInterest());


        TextView view = holder.getView(R.id.tv_fit);

        //0=出借中 1=待还款 2=出借失败 3=已还款
        String productStatus = bean.getProductStatus();

        String status="预期收益";

        //然后直接setText()
        if("3".equalsIgnoreCase(productStatus)){
            status="已收收益";
        }

        //首先是拼接字符串
        String content = " <font color=#EE4845>" + bean.getExpireInterest() + "</font>";
        view.setText(Html.fromHtml(status+content));

    }

    /*@Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }*/
}
