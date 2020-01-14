package com.ekabao.oil.adapter;

import android.support.v7.widget.RecyclerView;

import com.ekabao.oil.R;
import com.ekabao.oil.adapter.viewholder.BaseViewHolder;
import com.ekabao.oil.bean.NewsBean;
import com.ekabao.oil.util.StringCut;

import java.util.List;

/**
 * $desc$
 *
 * @time $data$ $time$
 * Created by Administrator on 2018/7/13.
 */

public class NewsAdapter extends BaseRecyclerViewAdapter {
    private List<NewsBean> list;
    public NewsAdapter(RecyclerView view, List list, int itemLayoutId) {
        super(view, list, itemLayoutId);

        this.list=list;
    }

    @Override
    public void bindConvert(BaseViewHolder holder, int position, Object item, boolean isScrolling) {

        NewsBean newsBean = list.get(position);

        holder.setText(R.id.tv_time, StringCut.getDateTimeToStringheng((newsBean.getAddTime())));
        holder.setText(R.id.tv_title,newsBean.getTitle());
        holder.setText(R.id.tv_content,newsBean.getContent());

    }

   /* @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }*/
}
