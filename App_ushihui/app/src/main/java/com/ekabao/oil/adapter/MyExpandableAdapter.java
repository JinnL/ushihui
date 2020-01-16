package com.ekabao.oil.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ekabao.oil.R;

import java.util.List;

/**
 * 项目名称：trunk
 * 类描述：ExpandableListAdapter
 * 创建人：shuc
 * 创建时间：2017/2/21 13:52
 * 修改人：DELL
 * 修改时间：2017/2/21 13:52
 * 修改备注：
 */
public class MyExpandableAdapter extends BaseExpandableListAdapter {

    private List<String> groupArray;
    private List<String> childArray;
    private Context mContext;

    public MyExpandableAdapter(Context context, List<String> groupArray, List<String> childArray) {
        mContext = context;
        this.groupArray = groupArray;
        this.childArray = childArray;
    }

    @Override
    public int getGroupCount() {
        return groupArray.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupArray.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childArray.get(groupPosition);
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
        View view = convertView;
        GroupHolder holder = null;
        if (view == null) {
            holder = new GroupHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_expandlist_group, null);
            holder.groupName = (TextView) view.findViewById(R.id.tv_group_name);
            holder.arrow = (ImageView) view.findViewById(R.id.iv_arrow);
            holder.llItem = (LinearLayout) view.findViewById(R.id.ll_item);


            view.setTag(holder);
        } else {
            holder = (GroupHolder) view.getTag();
        }


        if (groupPosition % 2 == 0) {
            holder.llItem.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else {
            holder.llItem.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

        //判断是否已经打开列表
        if (isExpanded) {
            holder.arrow.setImageResource(R.drawable.find_callcenter_zhan);
        } else {
            holder.arrow.setImageResource(R.drawable.find_callcenter_you);
        }

        holder.groupName.setText(groupArray.get(groupPosition));

        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = convertView;
        ChildHolder holder = null;
        if (view == null) {
            holder = new ChildHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_expandlist, null);
            holder.childName = (TextView) view.findViewById(R.id.tv_child_name);
            view.setTag(holder);
        } else {
            holder = (ChildHolder) view.getTag();
        }
        holder.childName.setText(Html.fromHtml(childArray.get(groupPosition)));
//        holder.childName.setText(childArray.get(groupPosition));
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class GroupHolder {
        public TextView groupName;
        public ImageView arrow;
        public LinearLayout llItem;

    }

    class ChildHolder {
        public TextView childName;
    }
}
