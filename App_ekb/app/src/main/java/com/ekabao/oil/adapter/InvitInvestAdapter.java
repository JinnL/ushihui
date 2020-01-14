package com.ekabao.oil.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.ekabao.oil.R;
import com.ekabao.oil.bean.InvitInvestBean;

import java.util.List;

/**
 * 项目名称：JsAppAs2.0
 * 类描述：
 * 创建人：shuc
 * 创建时间：2017/4/7 15:00
 * 修改人：DELL
 * 修改时间：2017/4/7 15:00
 * 修改备注：
 */
public class InvitInvestAdapter extends BaseAdapter{
    private Context context;
    private List<InvitInvestBean> mList;
    private LayoutInflater inflater;
    private String flag;

    public InvitInvestAdapter(Context context, List<InvitInvestBean> lsct, String flag) {
        super();
        this.context = context;
        this.mList = lsct;
        this.flag = flag;
        inflater = LayoutInflater.from(context);
    }

    public void onDateChange(List<InvitInvestBean> lsct) {
        this.mList = lsct;
        this.notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    private class ViewHolder{
        private TextView tv_name ,tv_amount,tv_shouyi;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh=null;
        if(convertView==null){
            vh=new ViewHolder();
            convertView=inflater.inflate(R.layout.invit_jinjie_item, null);
            vh.tv_name =  (TextView) convertView.findViewById(R.id.tv_name) ;
            vh.tv_amount =  (TextView) convertView.findViewById(R.id.tv_amount) ;
            vh.tv_shouyi =  (TextView) convertView.findViewById(R.id.tv_shouyi) ;
//            vh.tv_lingqu =  (TextView) convertView.findViewById(R.id.tv_lingqu) ;
            convertView.setTag(vh);
        }else{
            vh=(ViewHolder) convertView.getTag();
        }

        if(mList.get(position)!=null && TextUtils.isEmpty(flag)){
            if("1".equalsIgnoreCase(mList.get(position).getInvestOrder())){
            }else{
            }
            vh.tv_name.setText(mList.get(position).getMobilePhone());
            vh.tv_amount.setText(mList.get(position).getInvestAmount());
            vh.tv_shouyi.setText(mList.get(position).getRebateAmount());
        }
        if(flag.equals("third")){//第三重礼
            vh.tv_name.setText("No."+mList.get(position).getRownum());
            vh.tv_amount.setText(mList.get(position).getMobilePhone());
            vh.tv_shouyi.setText(mList.get(position).getAmount()+"");
        }


//        if(!TextUtils.isEmpty(mList.get(position).getRebateAmount())){
//            if(Double.parseDouble(mList.get(position).getRebateAmount()) - 0 > 0){
//                vh.tv_lingqu.setVisibility(View.VISIBLE);
//            }else {
//                vh.tv_lingqu.setVisibility(View.INVISIBLE);
//            }
//        }else{
//            vh.tv_lingqu.setVisibility(View.INVISIBLE);
//        }
//        if(mList.get(position).getStatus()>0){   //已领取
//            vh.tv_lingqu.setText("已领取");
//        }else{
//            vh.tv_lingqu.setText("未领取");
//        }
        return convertView;
    }
}
