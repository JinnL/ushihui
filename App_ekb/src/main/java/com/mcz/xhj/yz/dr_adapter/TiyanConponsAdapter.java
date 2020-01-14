package com.mcz.xhj.yz.dr_adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_bean.TiyanConponsBean;
import com.mcz.xhj.yz.dr_util.stringCut;

import java.util.List;

/**
 * 项目名称：JsAppAs2.0
 * 类描述：体验金红包列表item
 * 创建人：shuc
 * 创建时间：2017/2/22 17:51
 * 修改人：DELL
 * 修改时间：2017/2/22 17:51
 * 修改备注：
 */
public class TiyanConponsAdapter extends BaseAdapter {
    private Context context;
    private List<TiyanConponsBean> lsct;
    private LayoutInflater inflater;
    public TiyanConponsAdapter(Context context, List<TiyanConponsBean> lsct) {
        super();
        this.context = context;
        this.lsct = lsct;
        inflater = LayoutInflater.from(context);
    }

    public void onDateChange(List<TiyanConponsBean> lsct) {
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
        private TextView tv_laiyuan,tv_amount,tv_jihuo;
        private TextView btn_use;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = inflater.inflate(R.layout.tiyan_conpons_list_item,null);
            vh.tv_laiyuan = (TextView) convertView.findViewById(R.id.tv_laiyuan);
            //vh.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            vh.tv_amount = (TextView) convertView.findViewById(R.id.tv_amount);
            vh.tv_jihuo = (TextView) convertView.findViewById(R.id.tv_jihuo);
            vh.btn_use = (TextView) convertView.findViewById(R.id.tv_use);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        vh.tv_amount.setText("体验金"+stringCut.getNumKbs(lsct.get(position).getAmount())+"元");
        vh.tv_jihuo.setText("投资≥"+stringCut.getNumKbs(lsct.get(position).getEnableAmount())+"元");
        //vh.tv_date.setText(stringCut.getDateYearToString(Long.parseLong(lsct.get(position).getAddtime()))+" 获得");
        if("0".equalsIgnoreCase(lsct.get(position).getStatus())){
            vh.btn_use.setBackgroundResource(R.drawable.bg_color_yellow);
            vh.btn_use.setText("立即使用");
            if(!TextUtils.isEmpty(lsct.get(position).getSource())){
                if("100".equalsIgnoreCase(lsct.get(position).getSource())){
                    vh.btn_use.setText("立即激活");
                    vh.tv_jihuo.setVisibility(View.VISIBLE);
                }else if("99".equalsIgnoreCase(lsct.get(position).getSource())){
                    vh.tv_jihuo.setVisibility(View.VISIBLE);
                }else{
                    vh.tv_jihuo.setVisibility(View.GONE);
                }
            }else{
                vh.tv_jihuo.setVisibility(View.GONE);
            }
        }else if("1".equalsIgnoreCase(lsct.get(position).getStatus())){
            vh.btn_use.setBackgroundResource(R.drawable.bg_corner_grey_tiyan);
            vh.btn_use.setText("已使用");
        }else if("2".equalsIgnoreCase(lsct.get(position).getStatus())){
            vh.btn_use.setBackgroundResource(R.drawable.bg_corner_grey_tiyan);
            vh.btn_use.setText("已过期");
        }

        if(!TextUtils.isEmpty(lsct.get(position).getSource())){
            if("100".equalsIgnoreCase(lsct.get(position).getSource())||"99".equalsIgnoreCase(lsct.get(position).getSource())){
                vh.tv_jihuo.setVisibility(View.VISIBLE);
                vh.tv_laiyuan.setText("类型：新手体验金");
            }else{
                vh.tv_jihuo.setVisibility(View.GONE);
                vh.tv_laiyuan.setText("类型：普通体验金");
            }
        }else{
            vh.tv_jihuo.setVisibility(View.GONE);
            vh.tv_laiyuan.setText("类型：普通体验金");
        }


        return convertView;
    }
}
