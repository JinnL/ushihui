package com.mcz.xhj.yz.dr_adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_bean.WebAnBean;
import com.mcz.xhj.yz.dr_util.stringCut;

public class WebAnAdapter extends BaseAdapter {
    private Context context;
    private List<WebAnBean> lsct;
    private LayoutInflater inflater;

    public WebAnAdapter(Context context, List<WebAnBean> lsct) {
        super();
        this.context = context;
        this.lsct = lsct;
        inflater = LayoutInflater.from(context);
    }

    public void onDateChange(List<WebAnBean> lsct) {
        this.lsct = lsct;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return lsct.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return lsct.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    private class ViewHolder {
        private TextView title_tv, create_time_tv;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = inflater.inflate(R.layout.web_an_item, null);
            vh.create_time_tv = (TextView) convertView.findViewById(R.id.create_time_tv);
            vh.title_tv = (TextView) convertView.findViewById(R.id.title_tv);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.title_tv.setText(lsct.get(position).getTitle());
        vh.create_time_tv.setText(stringCut.getDateYearToString(lsct.get(position).getCreateTime()));
        return convertView;
    }

}
