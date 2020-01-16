package com.ekabao.oil.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ekabao.oil.R;
import com.ekabao.oil.bean.GoodsCategory;
import com.ekabao.oil.bean.HomeInfoList;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 666 on 2017/1/3.
 * 首页分类
 */
public class TypeCategoryAdapter extends RecyclerView.Adapter {

    private Context mContext;

    private List<HomeInfoList.LogoListBean> mHomeCategory;
    private List<GoodsCategory> mMallCategory;

    private LayoutInflater inflater;
    private int type = 1;// 1 首页  2 首页的下面

    public TypeCategoryAdapter(Context mContext, List<HomeInfoList.LogoListBean> mHomeCategory) {
        this.mContext = mContext;
        this.mHomeCategory = mHomeCategory;
        inflater = LayoutInflater.from(mContext);
    }

    //首页的八个分类
    public TypeCategoryAdapter(Context mContext, int TYPE, List<HomeInfoList.LogoListBean> mHomeCategory) {
        this.mContext = mContext;
        this.mHomeCategory = mHomeCategory;
        inflater = LayoutInflater.from(mContext);
        type = TYPE;
    }

    public TypeCategoryAdapter(Context mContext, List<GoodsCategory> mHomeCategory, int TYPE) {
        this.mContext = mContext;
        this.mMallCategory = mHomeCategory;
        inflater = LayoutInflater.from(mContext);
        type = TYPE;
    }


    @Override
    public int getItemViewType(int position) {
        return type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // LogUtils.e(type+"易商城分类onCreateViewHolder" );


        if (type == 1) {
            return new TypetypeHolder(inflater.inflate(R.layout.item_home_type_1, null));
        } else if (type == 2) {
            return new Typetype2Holder(inflater.inflate(R.layout.item_home_rv_2, null));
        } else {
            return new MalltypeHolder(inflater.inflate(R.layout.item_home_type_2, null));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        //LogUtils.e(type+"易商城分类" );
        if (type == 1) {
            final TypetypeHolder holder1 = (TypetypeHolder) holder;
            HomeInfoList.LogoListBean homeCategory = mHomeCategory.get(position);

            Glide.with(mContext)
                    .load(homeCategory.getImgUrl())
                    .into(holder1.ivImage);

//            if(position == 0){
//                holder1.ivImage.setImageResource(R.drawable.bg_home_top_package);
//            }else if(position == 1){
//                holder1.ivImage.setImageResource(R.drawable.bg_home_top_direct);
//            }else if(position == 2){
//                holder1.ivImage.setImageResource(R.drawable.bg_home_top_card);
//            }else if(position == 3){
//                holder1.ivImage.setImageResource(R.drawable.bg_home_top_phone);
//            }else if(position == 4 ){
//                holder1.ivImage.setImageResource(R.drawable.icon_person_phone);
//            }
            holder1.tvName.setText(homeCategory.getTitle());

            holder1.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onTypeItemClick(holder1.itemView, position);
                    }
                }
            });

        } else if (type == 2) {

            final Typetype2Holder holder1 = (Typetype2Holder) holder;
            HomeInfoList.LogoListBean homeCategory = mHomeCategory.get(position);

//            Glide.with(mContext)
//                    .load(homeCategory.getOrders())
//                    .into(holder1.ivImage);

            Drawable drawable = mContext.getResources().getDrawable(
                    homeCategory.getOrders());
            // / 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, (int) mContext.getResources().getDimension(R.dimen.dp_20),
                    (int) mContext.getResources().getDimension(R.dimen.dp_20));
            holder1.tvName.setCompoundDrawables(drawable, null, null, null);

            holder1.tvName.setText(homeCategory.getTitle());
            if (TextUtils.isEmpty(homeCategory.getNote())) {
                holder1.tvNews.setVisibility(View.GONE);
            } else {
                holder1.tvNews.setVisibility(View.VISIBLE);
                holder1.tvNews.setText(homeCategory.getNote());
            }

            holder1.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onTypeItemClick(holder1.itemView, position);
                    }
                }
            });

        } else {
            //LogUtils.e("易商城分类"+mMallCategory.size() );

            final MalltypeHolder holder1 = (MalltypeHolder) holder;
            GoodsCategory homeCategory = mMallCategory.get(position);
            // LogUtils.e(homeCategory.getIconUrl()+"////"+homeCategory.getName());
            Glide.with(mContext)
                    .load(homeCategory.getIconUrl())
                    //.placeholder(R.drawable.bg_home_banner_fail)
                    //.error(R.drawable.bg_home_banner_fail)
                    .centerCrop()
                    //.transform(glideRoundTransform)
                    .into(holder1.ivImage);

            holder1.tvName.setText(homeCategory.getName());

            holder1.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onTypeItemClick(holder1.itemView, position);
                    }
                }
            });
        }
    }

   /* @Override
    public void onBindViewHolder(final TypetypeHolder holder, final int position) {
        HomeInfoList.LogoListBean homeCategory = mHomeCategory.get(position);

        Glide.with(mContext)
                .load(homeCategory.getImgUrl())
                //.placeholder(R.drawable.bg_home_banner_fail)
                //.error(R.drawable.bg_home_banner_fail)
                .centerCrop()
                //.transform(glideRoundTransform)
                .into(holder.ivImage);

        holder.tvName.setText(homeCategory.getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onTypeItemClick(holder.itemView, position);
                }
            }
        });

    }*/

    public OnTypeItemClickListener onItemClickListener;

    public interface OnTypeItemClickListener {
        void onTypeItemClick(View view, int position);

    }

    public void setOnTypeItemClickListener(OnTypeItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        if (type == 1) {
            return mHomeCategory == null ? 0 : mHomeCategory.size();
        } else if (type == 2) {
            return mHomeCategory == null ? 0 : mHomeCategory.size();
        } else {
            return mMallCategory == null ? 0 : mMallCategory.size();
        }

    }

    //中间的四个type
    public class TypetypeHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_image)
        ImageView ivImage;
        @BindView(R.id.tv_name)
        TextView tvName;

        public TypetypeHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public class Typetype2Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_news)
        TextView tvNews;
        @BindView(R.id.tv_title)
        TextView tvName;

        public Typetype2Holder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    public class MalltypeHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_image)
        ImageView ivImage;
        @BindView(R.id.tv_name)
        TextView tvName;

        public MalltypeHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


}
