package com.ekabao.oil.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ekabao.oil.R;
import com.ekabao.oil.bean.ProductDetailInfo;

import java.util.List;

/**
 * Created by Administrator on 2018/6/7.
 */

public class ProductPicAdapter extends BaseAdapter {
    private List<ProductDetailInfo.PicListBean> proIntroduceList;

    public ProductPicAdapter(List<ProductDetailInfo.PicListBean> proIntroduceList) {
        this.proIntroduceList = proIntroduceList;
    }

    @Override
    public int getCount() {
        return proIntroduceList.size();
    }

    @Override
    public Object getItem(int position) {
        return proIntroduceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_pic, parent, false);
            //convertView = View.inflate(getActivity(), R.layout.item_product_pic, null);
            vh.title = (TextView) convertView.findViewById(R.id.tv_title);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        ProductDetailInfo.PicListBean picListBean = proIntroduceList.get(position);

        vh.title.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //下划线
        vh.title.getPaint().setAntiAlias(true);//抗锯齿

        vh.title.setText(picListBean.getName());

        return convertView;
    }

    class ViewHolder {
        private TextView title;
    }

}
