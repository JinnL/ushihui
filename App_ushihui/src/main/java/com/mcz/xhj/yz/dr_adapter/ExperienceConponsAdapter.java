package com.mcz.xhj.yz.dr_adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_bean.TiyanConponsBean;
import com.mcz.xhj.yz.dr_util.stringCut;

import java.util.List;

/**
 * 项目名称：小行家
 * 类描述：体验金红包列表item2.0
 *
 */
public class ExperienceConponsAdapter extends BaseAdapter {
    private Context context;
    private List<TiyanConponsBean> lsct;
    private LayoutInflater inflater;
    public ExperienceConponsAdapter(Context context, List<TiyanConponsBean> lsct) {
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
        private LinearLayout ll_conpons_bg;
        private TextView tv_name,tv_amount,tv_jihuo,tv_date,tv_dd;
        private TextView btn_use;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_experience_conpons,null);
            vh.ll_conpons_bg = (LinearLayout) convertView.findViewById(R.id.ll_conpons_bg);
            vh.tv_dd = (TextView) convertView.findViewById(R.id.tv_dd);
            vh.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            vh.tv_amount = (TextView) convertView.findViewById(R.id.tv_amount);
            vh.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            vh.tv_jihuo = (TextView) convertView.findViewById(R.id.tv_jihuo);
            vh.btn_use = (TextView) convertView.findViewById(R.id.tv_use);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        TiyanConponsBean cb = lsct.get(position);
        vh.tv_amount.setText(stringCut.getDoubleKb(lsct.get(position).getAmount())+"元");
        vh.tv_name.setText(lsct.get(position).getName());
        vh.tv_jihuo.setText("单笔投资≥"+stringCut.getNumKbs(lsct.get(position).getEnableAmount())+"元");
        vh.tv_date.setText(stringCut.getDateYearToString(Long.parseLong(lsct.get(position).getAddtime())));
        if (lsct.get(position).getExpireDate() != null && !cb.getExpireDate().equalsIgnoreCase("")) {
            vh.tv_dd.setText("有效期至：");
            vh.tv_date.setText(stringCut.getDateYearToString(Long.parseLong(cb.getExpireDate())));
        } else {
            vh.tv_dd.setText("");
            vh.tv_date.setText("永久有效");
        }
        if("0".equalsIgnoreCase(lsct.get(position).getStatus())){
            //vh.btn_use.setBackgroundResource(R.drawable.bg_color_yellow);
            vh.ll_conpons_bg.setBackgroundResource(R.mipmap.bg_experience_conpons);
            vh.btn_use.setText("立即使用");
            if(!TextUtils.isEmpty(lsct.get(position).getSource())){
                if("100".equalsIgnoreCase(lsct.get(position).getSource())){
                    vh.btn_use.setText("立即激活");
                    vh.tv_jihuo.setVisibility(View.VISIBLE);
                }else if("99".equalsIgnoreCase(lsct.get(position).getSource())){
                    vh.tv_jihuo.setVisibility(View.VISIBLE);
                }else{
                    //vh.tv_jihuo.setVisibility(View.GONE);
                }
            }else{
                vh.tv_jihuo.setVisibility(View.GONE);
            }
        }else if("1".equalsIgnoreCase(lsct.get(position).getStatus())){
            //vh.btn_use.setBackgroundResource(R.drawable.bg_corner_grey_tiyan);
            vh.btn_use.setText("已使用");
            vh.ll_conpons_bg.setBackgroundResource(R.mipmap.conpons_off);
        }else if("2".equalsIgnoreCase(lsct.get(position).getStatus())){
            //vh.btn_use.setBackgroundResource(R.drawable.bg_corner_grey_tiyan);
            vh.btn_use.setText("已过期");
            vh.ll_conpons_bg.setBackgroundResource(R.mipmap.conpons_off);
        }

        return convertView;
    }
}
