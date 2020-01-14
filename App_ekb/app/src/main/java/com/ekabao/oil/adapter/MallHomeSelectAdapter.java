package com.ekabao.oil.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ekabao.oil.R;
import com.ekabao.oil.bean.GoodsCategory;
import com.ekabao.oil.bean.GoodsMiddlebanner;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 666 on 2017/1/3.
 * 首页分类
 */
public class MallHomeSelectAdapter extends RecyclerView.Adapter {

    private Context mContext;

    private List<GoodsMiddlebanner> mHomeCategory;

    private List<GoodsCategory> mMallCategory;

    private LayoutInflater inflater;

    private int type = 1;// 1 首页 2 商城首页

    public MallHomeSelectAdapter(Context mContext, List<GoodsMiddlebanner> mHomeCategory) {
        this.mContext = mContext;
        this.mHomeCategory = mHomeCategory;
        inflater = LayoutInflater.from(mContext);
    }

  /*  public MallHomeSelectAdapter(Context mContext, List<GoodsCategory> mHomeCategory, int TYPE) {
        this.mContext = mContext;
        this.mMallCategory = mHomeCategory;
        inflater = LayoutInflater.from(mContext);
        type = TYPE;
    }*/

    @Override
    public int getItemViewType(int position) {
        return type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // LogUtils.e(type+"易商城分类onCreateViewHolder" );


        if (type == 1) {
            return new TypetypeHolder(inflater.inflate(R.layout.item_home_rv_4, null));
        } else {

            return new MalltypeHolder(inflater.inflate(R.layout.item_home_type_2, null));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        //LogUtils.e(type+"易商城分类" );
        if (type == 1) {

            final TypetypeHolder holder1 = (TypetypeHolder) holder;
            GoodsMiddlebanner homeCategory = mHomeCategory.get(position);

            Glide.with(mContext)
                    .load(homeCategory.getImgUrl())
                    //.placeholder(R.drawable.bg_home_banner_fail)
                    //.error(R.drawable.bg_home_banner_fail)
                    // .fitCenter()
                    // .centerCrop()
                    //.transform(glideRoundTransform)
                    .into(holder1.ivImage);

          //  holder1.tvName.setText(homeCategory.getTitle());

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

           // holder1.tvName.setText(homeCategory.getName());

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
        GoodsMiddlebanner homeCategory = mHomeCategory.get(position);

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
        } else {
            return mMallCategory == null ? 0 : mMallCategory.size();
        }

    }

    //中间的四个type
    public class TypetypeHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_mall)
        ImageView ivImage;

      /*  @BindView(R.id.iv_image)
        ImageView ivImage;
        @BindView(R.id.tv_name)
        TextView tvName;*/

        public TypetypeHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public class MalltypeHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_mall)
        ImageView ivImage;


        public MalltypeHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }



}
