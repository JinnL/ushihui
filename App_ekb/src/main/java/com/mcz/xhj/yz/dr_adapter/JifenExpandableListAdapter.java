package com.mcz.xhj.yz.dr_adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_bean.DateRecordBean;
import com.mcz.xhj.yz.dr_bean.JifenDetailBean;
import com.mcz.xhj.yz.dr_util.stringCut;

import java.util.List;
import java.util.Map;

/**
 * Created by DELL on 2017/11/1.
 * 描述：积分明细可折叠adapter。
 */

public class JifenExpandableListAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    List<DateRecordBean> groupTitle;
    private Map<Integer, List<JifenDetailBean>> childMap;

    public JifenExpandableListAdapter(Context mContext, List<DateRecordBean> groupTitle, Map<Integer, List<JifenDetailBean>> childMap) {
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
        if (groupHolder == null) {
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
        groupHolder.tv_year_month.setText(groupTitle.get(groupPosition).getMonths());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder childHolder = null;
        if (childHolder == null) {
            childHolder = new ChildHolder();
            convertView = View.inflate(mContext, R.layout.jifen_details_item, null);
            childHolder.tv_remark = (TextView) convertView.findViewById(R.id.tv_remark);
            childHolder.tv_jifen = (TextView) convertView.findViewById(R.id.tv_jifen);
            childHolder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            convertView.setTag(childHolder);
        } else {
            childHolder = (ChildHolder) convertView.getTag();
        }

        JifenDetailBean jifenDetail = childMap.get(groupPosition).get(childPosition);
        childHolder.tv_remark.setText(jifenDetail.getRemark());
        childHolder.tv_date.setText(stringCut.getDateTimeToStringheng(jifenDetail.getAddTime()));

        if ("0".equalsIgnoreCase(childMap.get(groupPosition).get(childPosition).getType())) {  //	0=支出，1=收入
            childHolder.tv_jifen.setText("-" + childMap.get(groupPosition).get(childPosition).getPoint());
            childHolder.tv_jifen.setTextColor(mContext.getResources().getColorStateList(R.color.btn_end_color));
        } else {
            childHolder.tv_jifen.setText(childMap.get(groupPosition).get(childPosition).getPoint()+"");
            childHolder.tv_jifen.setTextColor(mContext.getResources().getColorStateList(R.color.btn_bg_color));
        }

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
        TextView tv_remark, tv_jifen, tv_date;
    }
}
