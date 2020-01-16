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
 * 三种类型 的消息
 * Created by Administrator on 2018/12/14.
 */

public class MediaAdapter  extends BaseRecyclerViewAdapter {
    private List<MediaBean> list;

    public MediaAdapter(RecyclerView view, List list, int itemLayoutId) {
        super(view, list, itemLayoutId);
    }

    @Override
    public void bindConvert(BaseViewHolder holder, int position, Object item, boolean isScrolling) {

        MediaBean noticeBean = list.get(position);

        holder.setText(R.id.tv_time, StringCut.getDateTimeToStringheng((noticeBean.getCreateTime())));
        holder.setText(R.id.tv_name,noticeBean.getTitle());

        ImageView view = holder.getView(R.id.iv_photo);
        Glide.with(context)
                .load(noticeBean.getLitpic())
               // .placeholder(R.drawable.bg_home_banner_fail)
                .error(R.drawable.bg_home_banner_fail)
                .centerCrop()
                //.transform(glideRoundTransform)
                .into(view);

        TextView tv_content = holder.getView(R.id.tv_content);
        tv_content.setText(Html.fromHtml(noticeBean.getContent()));
        //holder.setText(R.id.tv_content,noticeBean.getContent());
    }

  /*  @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }*/





/*extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        *//**
         * 三种item 写一半,感觉太麻烦了
         *
         * *//*

        private List<Object> list;

        private Context ctx;
        ////从那个点击进来的    1.系统消息 2 平台公告 3.媒体报道',
        private int ITEM_TYPE = 3;
        //  private int ITEM_TYPE = 2; //历史项目
        // private int ITEM_TYPE = 3; //历史项目

    public MediaAdapter(List<Object> list, Context ctx) {
            this.list = list;
            this.ctx = ctx;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //item_news    item_notice
            switch (viewType) {
                case 1:
                    //View
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);

                    ViewHolder viewHolder = new ViewHolder(view);

                    return viewHolder;

                case 2:
                    //
                    View viewTwo = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notice, parent, false);
                    TypeTwoHolder twoHolder = new TypeTwoHolder(viewTwo);
                    return twoHolder;
                case 3:
                    //
                    View viewThree = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_media, parent, false);
                    ThreeTwoHolder threeHolder = new ThreeTwoHolder(viewThree);
                    return threeHolder;

            }

            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            //根据Position去调用不同的Holder类设置数据
            Object o = list.get(position);


      *//*  switch (ITEM_TYPE) {
            case 1:
                titleCentertextview.setText("系统通知");
                break;
            case 2:
                titleCentertextview.setText("平台公告");
                break;
            case 3:
                titleCentertextview.setText("媒体报道");
                break;
            default:
                titleCentertextview.setText("消息");
                break;
        }

        if (position == list.size()) {


            TypeTwoHolder twoHolder = (TypeTwoHolder) holder;

            if (this.mOnItemClickLitener != null) {
                twoHolder.tvHistory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickLitener.onHistoryItemClick(v);
                    }
                });
            }

        } else {

            if (this.mOnItemClickLitener != null) {
                myHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickLitener.onItemClick(v, position);
                    }
                });
            }

        }*//*
        }


    *//* //返回显示Item的数量，我们集合大小只有4，但是我们要显示显示5个Item
     @Override
     public int getItemCount() {
         return list.size()+1;//我在Size的基础上+1
     }*//*
        @Override
        public int getItemViewType(int position) {
            return ITEM_TYPE;

       *//* //当position为0的时候，那个我们需要显示的是轮播图，所以我定义两个常量来标识
        if (position == list.size()) {
            //==0的情况
            return BONTOM_TYPE;
        } else {
            //！==0的时候就返回全是相同的Item
            return HEADER_TYPE;
        }*//*

        }


        @Override
        public int getItemCount() {
            return list.size() + 1;
        }


        static class ViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.tv_title)
            TextView tvTitle;
            @BindView(R.id.tv_time)
            TextView tvTime;
            @BindView(R.id.tv_content)
            TextView tvContent;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }

        static class TypeTwoHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.tv_title)
            TextView tvTitle;
            @BindView(R.id.tv_name)
            TextView tvName;
            @BindView(R.id.tv_content)
            TextView tvContent;

            TypeTwoHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }

        static class ThreeTwoHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.tv_time)
            TextView tvTime;
            @BindView(R.id.iv_photo)
            ImageView ivPhoto;
            @BindView(R.id.tv_name)
            TextView tvName;
            @BindView(R.id.tv_content)
            TextView tvContent;

            ThreeTwoHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }


        public interface OnItemClickLitener {
            void onItemClick(View view, int position);

            void onHistoryItemClick(View view);
     *//*   void onItemClick2(int  toPosition, int position);
*//*

        }

        private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


}*/
}