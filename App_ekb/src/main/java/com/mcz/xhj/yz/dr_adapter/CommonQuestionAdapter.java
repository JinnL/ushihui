package com.mcz.xhj.yz.dr_adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_bean.QuestionBean;

import java.util.List;

/**
 * Created by Administrator on 2017/11/6 0006.
 */

public class CommonQuestionAdapter extends BaseAdapter {
    public Context mContext;
    public List<QuestionBean> list;

    public CommonQuestionAdapter(Context mContext, List<QuestionBean> list) {
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
        if(convertView == null){
            vh = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_common_question, null);
            vh.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
            vh.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            vh.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        QuestionBean questionBean = list.get(position);
        vh.tv_num.setText("Q"+(position+1));
        vh.tv_title.setText(questionBean.getTitle());
        vh.tv_content.setText(questionBean.getContent());
        return convertView;
    }

    private class ViewHolder{
        private TextView tv_num,tv_title,tv_content;
    }

}
