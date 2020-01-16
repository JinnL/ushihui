package com.ekabao.oil.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ekabao.oil.R;
import com.ekabao.oil.adapter.viewholder.BaseViewHolder;
import com.ekabao.oil.bean.MediaBean;
import com.ekabao.oil.util.StringCut;

import java.util.List;

import static com.ekabao.oil.global.LocalApplication.context;

/**
 * $desc$
 *
 * @time $data$ $time$
 * Created by Administrator on 2018/7/12.
 */

public class NoticeAdapter extends BaseRecyclerViewAdapter {
    private List<MediaBean> list;

    public NoticeAdapter(RecyclerView view, List list, int itemLayoutId) {
        super(view, list, itemLayoutId);
        this.list = list;
    }

    /* public NoticeAdapter(RecyclerView view, List list, int itemLayoutId, List<NoticeBean> list1) {
         super(view, list, itemLayoutId);
         this.list = list1;
     }
 */
    @Override
    public void bindConvert(BaseViewHolder holder, int position, Object item, boolean isScrolling) {
        /*if(isScrolling) {
            //不滑动的时候去加载数据
        }*/
        MediaBean noticeBean = list.get(position);

        holder.setText(R.id.tv_time, StringCut.getDateTimeToStringheng((noticeBean.getCreateTime())));
        holder.setText(R.id.tv_name, noticeBean.getTitle());

        TextView view = holder.getView(R.id.tv_content);
        //getContent
        view.setText(Html.fromHtml(noticeBean.getSummaryContents()));

        // View view1 = holder.getView(R.id.iv_photo);

        if (holder.getView(R.id.iv_photo) != null) {

            ImageView photo = holder.getView(R.id.iv_photo);
            Glide.with(context)
                    .load(noticeBean.getLitpic())
                    // .placeholder(R.drawable.bg_home_banner_fail)
                    .error(R.drawable.bg_home_banner_fail)
                    .centerCrop()
                    //.transform(glideRoundTransform)
                    .into(photo);
        }

    }

 /*   @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        //NoticeViewHolder viewHolder= (NoticeViewHolder) holder;



    }*/
}
