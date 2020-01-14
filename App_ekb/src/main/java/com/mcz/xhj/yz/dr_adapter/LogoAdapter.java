package com.mcz.xhj.yz.dr_adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_bean.LogoBean;

import java.util.List;

/**
 * Created by zhulang on 2017/8/30.
 */

public class LogoAdapter extends BaseAdapter {
    private List<LogoBean> data;
    private Context context;
    private LayoutInflater inflater;

    public LogoAdapter(List<LogoBean> data, Context context) {
        this.data = data;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_home_logo,null);
        SimpleDraweeView iv_pic_logo = (SimpleDraweeView) view.findViewById(R.id.iv_pic_logo);
        TextView tv_title_logo = (TextView) view.findViewById(R.id.tv_title_logo);
        LogoBean logoBean = data.get(position);
        tv_title_logo.setText(logoBean.getTitle());
        if(logoBean.getImgUrl() != null){
            Uri uri = Uri.parse(logoBean.getImgUrl());
            iv_pic_logo.setImageURI(uri);
        }
        return view;
    }
}
