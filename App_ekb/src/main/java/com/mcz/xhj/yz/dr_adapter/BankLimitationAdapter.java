package com.mcz.xhj.yz.dr_adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_bean.BankNameBean;
import com.mcz.xhj.yz.dr_bean.BankName_Pic;
import com.mcz.xhj.yz.dr_util.stringCut;

import java.util.List;

/**
 * Created by Administrator on 2017/11/6
 */

public class BankLimitationAdapter extends BaseAdapter {
    private Context mContext;
    private List<BankNameBean> list;
    private BankName_Pic bp;

    public BankLimitationAdapter(Context mContext, List<BankNameBean> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_bank_limitation, null);
            vh.iv_bank = (ImageView) convertView.findViewById(R.id.iv_bank);
            vh.tv_bankname = (TextView) convertView.findViewById(R.id.tv_bankname);
            vh.tv_quota = (TextView) convertView.findViewById(R.id.tv_quota);
            convertView.setTag(vh);
        } else {
            vh=(ViewHolder) convertView.getTag();
        }
        BankNameBean bankNameBean = list.get(position);
        if (bp == null) {
            bp = new BankName_Pic();
        }
        Integer pic = bp.bank_Pic(bankNameBean.getId());
        vh.iv_bank.setImageResource(pic);
        vh.tv_bankname.setText(bankNameBean.getBankName());
        vh.tv_quota.setText("单笔"+ stringCut.getNumKbs(bankNameBean.getSingleQuota())+"元/单日"+stringCut.getNumKbs(bankNameBean.getDayQuota())+"元");

        return convertView;
    }

    class ViewHolder {
        private ImageView iv_bank;
        private TextView tv_bankname, tv_quota;
    }
}
