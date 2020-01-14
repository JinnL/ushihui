package com.mcz.xhj.yz.dr_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_bean.MyInvestListBean;
import com.mcz.xhj.yz.dr_util.stringCut;

import java.util.List;

/**
 * 项目名称：JsAppAs2.0
 * 类描述：我的投资
 * 创建人：shuc
 * 创建时间：2017/2/24 17:11
 * 修改人：DELL
 * 修改时间：2017/2/24 17:11
 * 修改备注：
 */
public class InvestmentAdapter extends BaseAdapter{
    private Context context;
    private List<MyInvestListBean> lsct;
    private LayoutInflater inflater;

    public InvestmentAdapter(Context context,List<MyInvestListBean> lsct){
        super();
        this.context = context;
        this.lsct = lsct;
        inflater = LayoutInflater.from(context);
    }

    public void onDateChange(List<MyInvestListBean> lsct) {
        this.lsct = lsct;
        this.notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return lsct.size();
    }

    @Override
    public Object getItem(int i) {
        return lsct.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder= null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView=inflater.inflate(R.layout.me_investment_item, null);
            viewHolder.tv_name = (TextView)convertView.findViewById(R.id.tv_name);
            viewHolder.tv_status = (TextView)convertView.findViewById(R.id.tv_status);
            viewHolder.tv_daoqishouyi = (TextView)convertView.findViewById(R.id.tv_daoqishouyi);
            viewHolder.tv_nianhua = (TextView)convertView.findViewById(R.id.tv_nianhua);
            viewHolder.tv_touzijine = (TextView)convertView.findViewById(R.id.tv_touzijine);
            viewHolder.tv_huikuanriqi = (TextView)convertView.findViewById(R.id.tv_huikuanriqi);
            viewHolder.tv_huodong_h = (TextView)convertView.findViewById(R.id.tv_huodong_h);
            viewHolder.tv_tip = (TextView)convertView.findViewById(R.id.tv_tip);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder) convertView.getTag();
        }

        viewHolder.tv_name.setText(lsct.get(i).getFullName());
        viewHolder.tv_daoqishouyi.setText(stringCut.getNumKb(lsct.get(i).getExpireInterest()));
        viewHolder.tv_nianhua.setText(lsct.get(i).getRate()+"");

        if(lsct.get(i).getActivityRate()!=null){
            if(Double.parseDouble(lsct.get(i).getActivityRate())>0){
                viewHolder.tv_huodong_h.setVisibility(View.VISIBLE);
                viewHolder.tv_huodong_h.setText("+"+lsct.get(i).getActivityRate()+"%");
            }else{
                viewHolder.tv_huodong_h.setVisibility(View.GONE);
            }
        }else{
            viewHolder.tv_huodong_h.setVisibility(View.GONE);
        }
        viewHolder.tv_touzijine.setText(stringCut.getNumKb(lsct.get(i).getAmount()));
        if(lsct.get(i).getExpireDate()!=null&&!"".equalsIgnoreCase(lsct.get(i).getExpireDate())){
            viewHolder.tv_huikuanriqi.setVisibility(View.VISIBLE);
            viewHolder.tv_huikuanriqi.setText(stringCut.getDateYearToString(Long.parseLong(lsct.get(i).getExpireDate())));
        }else{
            viewHolder.tv_huikuanriqi.setVisibility(View.GONE);
        }
        if(lsct.get(i).getPeriodLabel()!=null){
            viewHolder.tv_tip.setVisibility(View.VISIBLE);
            viewHolder.tv_tip.setText(lsct.get(i).getPeriodLabel());
        }else{
            viewHolder.tv_tip.setVisibility(View.GONE);
        }
        //0=投资中 1=待还款 2=投资失败 3=已还款
        String productStatus = lsct.get(i).getProductStatus();
        if("1".equalsIgnoreCase(productStatus)){
            viewHolder.tv_status.setText("待还款");
        }else if("3".equalsIgnoreCase(productStatus)){
            viewHolder.tv_status.setText("已还款");
        }else if("0".equalsIgnoreCase(productStatus)){
            viewHolder.tv_status.setText("投资中");
            viewHolder.tv_daoqishouyi.setText("--");
        }else if("2".equalsIgnoreCase(productStatus)){
            viewHolder.tv_status.setText("投资失败");
        }

        return  convertView;
    }

    public class ViewHolder{
        TextView tv_name,tv_status,tv_daoqishouyi,tv_nianhua,tv_touzijine,tv_huikuanriqi,tv_huodong_h,tv_tip;
    }
}
