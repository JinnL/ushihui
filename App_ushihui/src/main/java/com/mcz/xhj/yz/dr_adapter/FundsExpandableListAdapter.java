package com.mcz.xhj.yz.dr_adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_bean.DateRecordBean;
import com.mcz.xhj.yz.dr_bean.TransactionDetailsBean;
import com.mcz.xhj.yz.dr_util.stringCut;

import java.util.List;
import java.util.Map;

/**
 * Created by DELL on 2017/11/1.
 * 描述：资金明细可折叠adapter。
 */

public class FundsExpandableListAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    List<DateRecordBean> groupTitle;
    private Map<Integer, List<TransactionDetailsBean>> childMap;

    public FundsExpandableListAdapter(Context mContext, List<DateRecordBean> groupTitle, Map<Integer, List<TransactionDetailsBean>> childMap) {
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
            convertView = View.inflate(mContext, R.layout.transaction_details_item, null);
            childHolder.tv_amount = (TextView) convertView.findViewById(R.id.tv_amount);
            childHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            childHolder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            convertView.setTag(childHolder);
        } else {
            childHolder = (ChildHolder) convertView.getTag();
        }

        if (childMap.get(groupPosition).get(childPosition) != null) {

            int tradeType = childMap.get(groupPosition).get(childPosition).getTradeType();//1=充值，2=提现，3=投资，4=活动,5=提现手续费，6=回款,7=体验金
            /*switch (tradeType) {
                case 1:
                    childHolder.tv_title.setText("充值");
                    break;
                case 2:
                    childHolder.tv_title.setText("提现");
                    break;
                case 3:
                    childHolder.tv_title.setText("投资");
                    break;
                case 4:
                    childHolder.tv_title.setText("活动");
                    break;
                case 5:
                    childHolder.tv_title.setText("提现手续费");
                    break;
                case 6:
                    childHolder.tv_title.setText("回款");
                    break;
                case 7:
                    childHolder.tv_title.setText("体验金");
                    break;
            }*/
            childHolder.tv_title.setText(childMap.get(groupPosition).get(childPosition).getRemark());
            childHolder.tv_date.setText(stringCut.getDateTimeToStringheng(childMap.get(groupPosition).get(childPosition).getAddTime()));

            if ("0".equalsIgnoreCase(childMap.get(groupPosition).get(childPosition).getType())) {  //	0=支出，1=收入
                childHolder.tv_amount.setText("-" + childMap.get(groupPosition).get(childPosition).getAmount());
                childHolder.tv_amount.setTextColor(mContext.getResources().getColorStateList(R.color.btn_end_color));
            } else {
                childHolder.tv_amount.setText(childMap.get(groupPosition).get(childPosition).getAmount());
                childHolder.tv_amount.setTextColor(mContext.getResources().getColorStateList(R.color.btn_bg_color));
            }
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
        TextView tv_amount, tv_title, tv_date;
    }
}
