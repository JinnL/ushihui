package com.mcz.xhj.yz.dr_adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_bean.DateRecordBean;
import com.mcz.xhj.yz.dr_bean.Rows_Message;
import com.mcz.xhj.yz.dr_util.stringCut;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/7
 * 描述：个人消息adapter
 */

public class PersonMsgExpandListAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<DateRecordBean> groupTitle;
    private Map<Integer, List<Rows_Message>> childMap;

    public PersonMsgExpandListAdapter(Context mContext, List<DateRecordBean> groupTitle, Map<Integer, List<Rows_Message>> childMap) {
        this.mContext = mContext;
        this.groupTitle = groupTitle;
        this.childMap = childMap;
    }

    @Override
    public int getGroupCount() {
        return groupTitle.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        if (childMap.get(groupPosition).size() > 0) {
            return childMap.get(groupPosition).size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupTitle.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

        if (childMap.get(groupPosition).get(childPosition) != null) {
            return childMap.get(groupPosition).get(childPosition);
        } else {
            return null;
        }
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder groupHolder = null;
        if (convertView == null) {
            groupHolder = new GroupHolder();
            convertView = View.inflate(mContext, R.layout.item_group_months, null);
            groupHolder.tv_year_month = (TextView) convertView.findViewById(R.id.tv_year_month);
            groupHolder.img_arrow = (ImageView) convertView.findViewById(R.id.img_arrow);

            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag();
        }
        if (isExpanded) {
            groupHolder.img_arrow.setImageResource(R.mipmap.arrow_d);
        } else {
            groupHolder.img_arrow.setImageResource(R.mipmap.arrow_r);
        }
        groupHolder.tv_year_month.setText(groupTitle.get(groupPosition).getTime());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder childHolder = null;
        if (convertView == null) {
            childHolder = new ChildHolder();
            convertView = View.inflate(mContext, R.layout.item_personal_message, null);
            childHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            childHolder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            childHolder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            convertView.setTag(childHolder);
        } else {
            childHolder = (ChildHolder) convertView.getTag();
        }

        Rows_Message message = childMap.get(groupPosition).get(childPosition);
        childHolder.tv_title.setText(message.getTitle());
        childHolder.tv_content.setText(message.getContent());
        childHolder.tv_date.setText(stringCut.getDateYearToString(Long.parseLong(message.getAddTime())));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // 根据方法名，此处应该表示二级条目是否可以被点击
        return true;
    }

    private class GroupHolder {
        TextView tv_year_month;
        ImageView img_arrow;
    }

    private class ChildHolder {
        TextView tv_title,tv_content, tv_date;
    }
}
