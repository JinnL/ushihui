package com.mcz.xhj.yz.dr_adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.mcz.xhj.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.mcz.xhj.yz.dr_bean.MeiTiBean;
import com.mcz.xhj.yz.dr_util.stringCut;

import java.util.List;

/**
 * 项目名称：JsAppAs2.0
 * 类描述：媒体报道
 * 创建人：shuc
 * 创建时间：2017/2/22 10:28
 * 修改人：DELL
 * 修改时间：2017/2/22 10:28
 * 修改备注：
 */
public class MeiTiAdapter extends BaseAdapter {
    private Context context;
    private List<MeiTiBean> lsct;
    private LayoutInflater inflater;

    public MeiTiAdapter(Context context, List<MeiTiBean> lsct) {
        super();
        this.context = context;
        this.lsct = lsct;
        inflater = LayoutInflater.from(context);
    }

    public void onDateChange(List<MeiTiBean> lsct) {
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
        private TextView tv_title, tv_date;
        private SimpleDraweeView sv_meiti;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = inflater.inflate(R.layout.find_meiti_item, null);
            vh.tv_date = (TextView) convertView
                    .findViewById(R.id.tv_date);
            vh.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            vh.sv_meiti = (SimpleDraweeView)convertView.findViewById(R.id.sv_meiti);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.tv_title.setText(lsct.get(position).getTitle());
        vh.tv_date.setText(stringCut.getDateTimeToStringheng(lsct.get(
                position).getCreateTime()));
        if(lsct.get(position).getLitpic()!=null && !"".equalsIgnoreCase(lsct.get(position).getLitpic())){
            Uri uri = Uri.parse(lsct.get(position).getLitpic());
            vh.sv_meiti.setImageURI(uri);
        }
        return convertView;
    }

}
