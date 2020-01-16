package com.ekabao.oil.adapter;

import android.support.v7.widget.RecyclerView;

import com.ekabao.oil.R;
import com.ekabao.oil.adapter.viewholder.BaseViewHolder;
import com.ekabao.oil.bean.BankNameBean;
import com.ekabao.oil.bean.BankName_Pic;
import com.ekabao.oil.util.StringCut;

import java.util.List;

/**
 * 易卡宝  App
 *
 * @time 2018/7/18 16:37
 * Created by lj on 2018/7/18 16:37.
 */

public class BankLimitAdapter extends BaseRecyclerViewAdapter {
    private List<BankNameBean> list;
    public BankLimitAdapter(RecyclerView view, List list, int itemLayoutId) {
        super(view, list, itemLayoutId);
        this.list=list;
    }

    @Override
    public void bindConvert(BaseViewHolder holder, int position, Object item, boolean isScrolling) {
        BankName_Pic bp = null;
        BankNameBean bankNameBean = list.get(position);
        if (bp == null) {
            bp = new BankName_Pic();
        }
        Integer pic = bp.bank_Pic(bankNameBean.getId());
        holder.setBitmapImage(R.id.iv_bank,pic);
        holder.setText(R.id.tv_bankname,bankNameBean.getBankName());
        holder.setText(R.id.tv_quota,"单笔"+ StringCut.getNumKbs(bankNameBean.getSingleQuota())+"元/单日"
                + StringCut.getNumKbs(bankNameBean.getDayQuota())+"元");
    }

  /*  @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }*/
}
