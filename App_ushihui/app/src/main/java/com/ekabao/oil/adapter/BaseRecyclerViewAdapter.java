package com.ekabao.oil.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ekabao.oil.adapter.viewholder.BaseViewHolder;
import com.ekabao.oil.util.LogUtils;

import java.util.List;

/**
 * $desc$
 *
 * @time $data$ $time$
 * Created by Administrator on 2018/7/12.
 */

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    private static final String TAG = "BaseRecyclerAdapter--->";
    private static final int TYPE_HEAD = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOT = 2;
    private boolean isScrolling = true;  //false表示滑动，true表示不滑动
    private View headerView, footerView;  //头和尾的view
    private List<T> lists;   //传入的集合。
    private int totalList;
    private int itemLayoutId; //设置Layout的id。
    private Context cxt;
    private OnItemViewClickListener onItemViewClickListener;
    private OnViewClickListener onViewClickListener;

    public interface OnItemViewClickListener {
        //设置item中某个view的点击事件
        void onItemViewClick(BaseViewHolder view, int position);
        //void onItemViewClick(View view, int position);
       // void onViewClick(View view, int position);
    }

    public interface OnViewClickListener extends  OnItemViewClickListener{
        //设置item中某个view的点击事件
        void onItemViewClick(View view, int position);
        // void onViewClick(View view, int position);
    }


    public void setonItemViewClickListener(OnItemViewClickListener listener) {
        this.onItemViewClickListener = listener;
    }
    public void setonItemViewClickListener(OnViewClickListener listener) {
        this.onViewClickListener = listener;
    }

    public BaseRecyclerViewAdapter(RecyclerView view, List<T> list, int itemLayoutId) {
        this.lists = list;
        this.itemLayoutId = itemLayoutId;
        this.cxt = view.getContext();

        view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //设置如果Recycler停止滑动，去绑定更新数据。
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //表示停止滑动
                    isScrolling = true;
                    LogUtils.i(TAG + isScrolling + "停止滚动");
                } else {
                    //正在滑动
                    isScrolling = false;
                    LogUtils.i(TAG + isScrolling + "正在滚动....");
                }
                //将上面代码改写成这样。
//                isScrolling = (newState == RecyclerView.SCROLL_STATE_IDLE);
                if (isScrolling) { //为true时候表示停止滑动操作，去更新数据
                    notifyDataSetChanged();
                }

                super.onScrollStateChanged(recyclerView, newState);
            }
        });

    }

    public void setFooterView(View view) {
        this.footerView = view;
    }

    public void setHeaderView(View view) {
        this.headerView = view;
    }

    /**
     * 创建RecyclerView里面的itemView。
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEAD && headerView != null) {
            return new BaseViewHolder(headerView);
        }
        if (viewType == TYPE_FOOT && footerView != null) {
            return new BaseViewHolder(footerView);
        }
         View view = LayoutInflater.from(cxt).inflate(itemLayoutId, parent, false);

        final BaseViewHolder baseViewHolder = new BaseViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemViewClickListener != null) {
                    int position = baseViewHolder.getLayoutPosition();
                    // mOnItemClickListener.onItemClick(view, mList.get(position));
                    onItemViewClickListener.onItemViewClick(baseViewHolder, position);
                }

                if (onViewClickListener != null) {
                    int position = baseViewHolder.getLayoutPosition();
                    // mOnItemClickListener.onItemClick(view, mList.get(position));
                    onViewClickListener.onItemViewClick(baseViewHolder, position);
                }

            }
        });

        return baseViewHolder;
    }

    /**
     * item 里面的view的点击事件
     * */
    public void setViewClickListener(View view, int position) {
       // LogUtils.e("setViewClickListener");
        if (onViewClickListener != null) {
           // LogUtils.e("setViewClick");
            //int position = baseViewHolder.getLayoutPosition();
            // mOnItemClickListener.onItemClick(view, mList.get(position));
            onViewClickListener.onItemViewClick(view, position);
        }

    }
    /**
     * 绑定每一个itemView的具体数据
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (getItemViewType(position) != TYPE_ITEM) {
            //如果不是正常的Item，就不去绑定数据
            return;
        }
        LogUtils.i(TAG + "bindView---" + position);
        //抽象方法，让子类去具体的实现
        int p = getRealPosition(position);
        bindConvert(holder, p, lists.get(p), isScrolling);

       /* if (onItemViewClickListener != null) {
            onItemViewClickListener.onItemViewClick(holder, p);
        }*/
    }


    private int getRealPosition(int position) {
        return headerView == null ? position : position - 1;
    }

    public abstract void bindConvert(BaseViewHolder holder, int position, T item, boolean isScrolling);

    /**
     * 判断有多少个list.size()
     *
     * @return
     */
    @Override
    public int getItemCount() {
        if (lists == null) {
            return 0;
        }
        if (headerView != null && footerView != null) {
            //头尾都不为空
            totalList = lists.size() + 2;
        } else if (headerView == null && footerView == null) {
            //头尾都为空
            totalList = lists.size();
        } else {
            //头尾有一个不为空
            totalList = lists.size() + 1;
        }
        return totalList;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && headerView != null) {
            //表示第一个item，并且headerView不为空
            return TYPE_HEAD;
        }
        if (position + 1 == getItemCount() && footerView != null) {
            //表示最好一个item，并且footerView不为空
            return TYPE_FOOT;
        }
        return TYPE_ITEM;
    }
}