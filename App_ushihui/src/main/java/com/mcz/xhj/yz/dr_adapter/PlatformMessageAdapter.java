package com.mcz.xhj.yz.dr_adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mcz.xhj.yz.dr_bean.Add_Bean;
import com.mcz.xhj.yz.dr_util.stringCut;
import com.mcz.xhj.R;

import java.util.List;

/**
 * Created by Administrator on 2017/9/19 0019.
 * 描述：平台公告的adapter
 */

public class PlatformMessageAdapter extends BaseAdapter {
    private Context mContext;
    private List<Add_Bean> list;

    public PlatformMessageAdapter(Context mContext, List<Add_Bean> list) {
        this.mContext = mContext;
        this.list = list;
    }

    public void onDateChange(List<Add_Bean> lsct) {
        this.list = lsct;
        this.notifyDataSetChanged();
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
        ViewHolder holder = null;
        if(holder == null){
            convertView = View.inflate(mContext, R.layout.item_platform_message,null);
            holder = new ViewHolder();
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_type_content = (TextView) convertView.findViewById(R.id.tv_type_content);
            holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Add_Bean add_bean = list.get(position);
        //holder.tv_type_content.setText(add_bean.getTitle());
        holder.tv_title.setText(add_bean.getTitle());
        holder.tv_date.setText(stringCut.getDateTimeToStringheng((add_bean.getCreateTime())));
        return convertView;
    }

    private class ViewHolder{
        private TextView tv_title;
        private TextView tv_type_content;
        private TextView tv_date;
    }
}
