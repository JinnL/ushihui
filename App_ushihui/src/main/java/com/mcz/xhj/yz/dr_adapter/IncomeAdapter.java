package com.mcz.xhj.yz.dr_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_bean.IncomeBean;
import com.mcz.xhj.yz.dr_util.stringCut;

import java.util.List;

/**
 * 项目名称：JsAppAs2.0
 * 类描述：累计收益
 * 创建人：shuc
 * 创建时间：2017/2/23 10:54
 * 修改人：DELL
 * 修改时间：2017/2/23 10:54
 * 修改备注：TransactionDetailsBean
 */
public class IncomeAdapter extends BaseAdapter{
    private Context context;
    private List<IncomeBean> lsct;
    private LayoutInflater inflater;
    public IncomeAdapter(Context context, List<IncomeBean> lsct) {
        super();
        this.context = context;
        this.lsct = lsct;
        inflater = LayoutInflater.from(context);
    }

    public void onDateChange(List<IncomeBean> lsct) {
        this.lsct = lsct;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return lsct.size();
    }

    @Override
    public Object getItem(int position) {
        return lsct.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView tv_amount,tv_title,tv_date;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = inflater.inflate(R.layout.income_fragment_item,null);
            vh.tv_amount = (TextView) convertView.findViewById(R.id.tv_amount);
            vh.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            vh.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        int tradeType = lsct.get(position).getTradeType();//1=充值，2=提现，3=投资，4=活动,5=提现手续费，6=回款,7=体验金
        switch (tradeType){
            case 1:
                vh.tv_title.setText("充值");
                break;
            case 2:
                vh.tv_title.setText("提现");
                break;
            case 3:
                vh.tv_title.setText("投资");
                break;
            case 4:
                vh.tv_title.setText("活动");
                break;
            case 5:
                vh.tv_title.setText("提现手续费");
                break;
            case 6:
                vh.tv_title.setText("回款");
                break;
            case 7:
                vh.tv_title.setText("体验金");
                break;
        }
        vh.tv_date.setText(stringCut.getDateTimeToStringheng(lsct.get(position).getAddTime()));
        vh.tv_amount.setText(lsct.get(position).getAmount()+"");

        return convertView;
    }
}
