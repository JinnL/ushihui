package com.ekabao.oil.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.ekabao.oil.R;
import com.ekabao.oil.bean.InviteRank;

import java.util.List;

/**
 * 项目名称：JsAppAs2.0
 * 邀请返现三重礼  第三个
 */
public class InviteRankAdapter extends BaseAdapter {
    private Context context;
    private List<InviteRank> mList;
    private LayoutInflater inflater;
    private String flag;

    public InviteRankAdapter(Context context, List<InviteRank> lsct, String flag) {
        super();
        this.context = context;
        this.mList = lsct;
        this.flag = flag;
        inflater = LayoutInflater.from(context);
    }

    public void onDateChange(List<InviteRank> lsct) {
        this.mList = lsct;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }



    private class ViewHolder {
        private TextView  tvRownum,tvName, tvAmount, tvMoney;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_invite_rank, null);
            vh.tvRownum = (TextView) convertView.findViewById(R.id.tv_rownum);
            vh.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            vh.tvAmount = (TextView) convertView.findViewById(R.id.tv_amount);
            vh.tvMoney =  (TextView) convertView.findViewById(R.id.tv_money) ;
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        InviteRank inviteRank = mList.get(position);
        vh.tvRownum.setText(inviteRank.getRownum());
        vh.tvName.setText(inviteRank.getMobilePhone());
        vh.tvAmount.setText(inviteRank.getInvestCount()+"");
        vh.tvMoney.setText(inviteRank.getAmount()+"");
       /* if (mList.get(position) != null && TextUtils.isEmpty(flag)) {
            if ("1".equalsIgnoreCase(mList.get(position).getInvestOrder())) {
            } else {
            }
            vh.tv_name.setText(mList.get(position).getRealname());
            vh.tv_amount.setText(mList.get(position).getInvestAmount());
            vh.tv_shouyi.setText(mList.get(position).getRebateAmount());
        }
        if (flag.equals("third")) {//第三重礼
            vh.tv_name.setText("No." + mList.get(position).getRownum());
            vh.tv_amount.setText(mList.get(position).getMobilePhone());
            vh.tv_shouyi.setText(mList.get(position).getAmount() + "");
        }*/


//        if(!TextUtils.isEmpty(mList.get(position).getRebateAmount())){
//            if(Double.parseDouble(mList.get(position).getRebateAmount()) - 0 > 0){
//                vh.tv_lingqu.setVisibility(View.VISIBLE);
//            }else {
//                vh.tv_lingqu.setVisibility(View.INVISIBLE);
//            }
//        }else{
//            vh.tv_lingqu.setVisibility(View.INVISIBLE);
//        }
//        if(mList.get(position).getStatus()>0){   //已领取
//            vh.tv_lingqu.setText("已领取");
//        }else{
//            vh.tv_lingqu.setText("未领取");
//        }
        return convertView;
    }


}
