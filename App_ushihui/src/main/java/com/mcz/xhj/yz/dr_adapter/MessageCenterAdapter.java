package com.mcz.xhj.yz.dr_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_bean.Rows_Message;
import com.mcz.xhj.yz.dr_util.stringCut;

import java.util.List;

/**
 * 项目名称：JsAppAs2.0
 * 类描述：
 * 创建人：shuc
 * 创建时间：2017/2/24 11:55
 * 修改人：DELL
 * 修改时间：2017/2/24 11:55
 * 修改备注：
 */
public class MessageCenterAdapter extends BaseExpandableListAdapter {

    private List<Rows_Message> list;
    private Context mContext;

    public MessageCenterAdapter(Context context, List<Rows_Message> list){
        mContext = context;
        this.list = list;
    }
    public void onDateChange(List<Rows_Message> lsct) {
        this.list = lsct;
        this.notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return list.get(groupPosition).getContent();
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
        if(view == null){
            holder = new GroupHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.message_center_group, null);
            holder.groupName = (TextView)view.findViewById(R.id.tv_group_name);
            holder.tv_date = (TextView)view.findViewById(R.id.tv_date);
            holder.arrow = (ImageView)view.findViewById(R.id.iv_arrow);
            view.setTag(holder);
        }else{
            holder = (GroupHolder)view.getTag();
        }

        //判断是否已经打开列表
        if(isExpanded){
            holder.arrow.setImageResource(R.mipmap.find_callcenter_shou);
        }else{
            holder.arrow.setImageResource(R.mipmap.find_callcenter_zhan);
        }

        holder.groupName.setText(list.get(groupPosition).getTitle());
        holder.tv_date.setText(stringCut.getDateYearToString(Long.parseLong(list.get(groupPosition).getAddTime())));
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = convertView;
        ChildHolder holder = null;
        if(view == null){
            holder = new ChildHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.expandlist_item, null);
            holder.childName = (TextView)view.findViewById(R.id.tv_child_name);
            view.setTag(holder);
        }else{
            holder = (ChildHolder)view.getTag();
        }
        holder.childName.setText(list.get(groupPosition).getContent());
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class GroupHolder{
        public TextView groupName,tv_date;
        public ImageView arrow;
    }

    class ChildHolder{
        public TextView childName;
    }
}
