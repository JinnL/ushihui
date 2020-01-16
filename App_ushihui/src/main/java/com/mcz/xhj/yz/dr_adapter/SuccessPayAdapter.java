package com.mcz.xhj.yz.dr_adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mcz.xhj.R;

import java.util.List;

/**
 * Created by DELL on 2017/11/21.
 * 描述：投资成功后，显示的文本列表
 */

public class SuccessPayAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> list;

    public SuccessPayAdapter(Context mContext, List<String> list) {
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
        if(convertView==null){
            vh= new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_success_pay,null);
            vh.iv_rank = (ImageView) convertView.findViewById(R.id.iv_rank);
            vh.tv_reward = (TextView) convertView.findViewById(R.id.tv_reward);
            convertView.setTag(vh);
        }else{
            vh=(ViewHolder) convertView.getTag();
        }

        vh.tv_reward.setText(list.get(position));
        if(position == 0){
            vh.iv_rank.setImageResource(R.mipmap.icon_reward1);
        }else if(position == 1){
            vh.iv_rank.setImageResource(R.mipmap.icon_reward2);
        }else if(position == 2){
            vh.iv_rank.setImageResource(R.mipmap.icon_reward3);
        }else if(position == 3){
            vh.iv_rank.setImageResource(R.mipmap.icon_reward4);
        }

        return convertView;
    }

    private class ViewHolder{
        private ImageView iv_rank;
        private TextView tv_reward;

    }
}
