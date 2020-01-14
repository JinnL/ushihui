package com.ekabao.oil.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ekabao.oil.R;
import com.ekabao.oil.bean.GoodsCategory;
import com.ekabao.oil.bean.GoodsList;
import com.ekabao.oil.bean.GoodsMiddlebanner;
import com.ekabao.oil.bean.GoodsNewList;
import com.ekabao.oil.bean.HomeBannerBean;
import com.ekabao.oil.bean.HomeHostProduct;
import com.ekabao.oil.ui.activity.MallClassifyActivity;
import com.ekabao.oil.ui.activity.MallDetailsActivity;
import com.ekabao.oil.ui.activity.WebViewActivity;
import com.ekabao.oil.util.GlideRoundTransform;
import com.ekabao.oil.util.LogUtils;
import com.ekabao.oil.util.StringCut;
import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * RecyclerView 多种布局 商城的
 * Created by lj on 2018/12/28.
 */
public class MallRecycleAdapter extends RecyclerView.Adapter {


    private final Context mContext;
    private List<HomeBannerBean> headerData;  //轮播图

    public static final int COUNT = 4;
    private int count = COUNT;

    private List<GoodsList> refreshbean;  //最下面的

    private List<HomeHostProduct> centerBean;  // 油卡充值套餐   新品上线

    private ArrayList<GoodsCategory> mHomeCategories = new ArrayList<>(); //分类按钮

    private List<GoodsMiddlebanner> middlebanner;  // 商城精选
    private List<GoodsNewList> newgoods;  //新品上线

    private int TYPE_TOP = 1;//头部布局
    private List<Integer> mHeights = new ArrayList<>();
    private int TYPE_CENTER = 2;// 油卡充值套餐   新品上线
    private int TYPE_CATEGORY = 3;//中间的四个快速入口
    private int TYPE_HEADER = 4;//每个分类的head
    private int TYPE_REFRESH = 5;//最下面的布局


    private LayoutInflater inflater;
    private RecyclerView recyclerView;

    //private MyStaggerGrildLayoutManger mystager; //流式布局
    private int type = 1;// 1 首页 2 商城首页

    public MallRecycleAdapter(Context context, int TYPE) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        //初始化各我数据源
        headerData = new ArrayList<>();
        refreshbean = new ArrayList<>();
        centerBean = new ArrayList<>();
        middlebanner = new ArrayList<>();
        newgoods = new ArrayList<>();
        type = TYPE;
    }


    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0: //轮播图
                return TYPE_TOP;
            case 1:
                return TYPE_CATEGORY;//中间的四个快速入口
//            case 2:
//                return TYPE_HEADER;//每个分类的head
            case 2:
                return TYPE_CENTER;
            case 3:
                return TYPE_HEADER;
            default:
                return TYPE_REFRESH;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_TOP) {
            View viewtop = inflater.inflate(R.layout.item_mall_banner, parent, false);
            return new MallTopsliderHolder(viewtop);
        } else if (viewType == TYPE_HEADER) {
            //每个分类的head
            View view2 = inflater.inflate(R.layout.item_home_headerview, parent, false);
            return new TypeheadHolder(view2);
        } else if (viewType == TYPE_CENTER) {
            //中间head下面的布局 6个
            // TODO: 2019/1/4   新品上线
//            View view = inflater.inflate(R.layout.item_mall_go, parent, false);
//            return new TypetypeHolder2(view);

            View view = inflater.inflate(R.layout.item_mall_center, parent, false);
            return new TypeCenterHolder(view);

        } else if (viewType == TYPE_CATEGORY) {
            View view = inflater.inflate(R.layout.item_home_type_rv, parent, false);
            return new TypetypeHolder(view);
        } else {
            //最下面的布局
            View inflate = inflater.inflate(R.layout.item_mall_home_2, parent, false);
            return new TypeRefresh(inflate);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MallTopsliderHolder && headerData.size() != 0) {
            //&& ((TypeTopsliderHolder) holder).linearLayout.getChildCount() == 0
            //如果是TypeTopsliderHolder， 并且header有数据，并且TypeTopsliderHolder的linearLayout没有子view（因为这个布局只出现一次，如果他没有子view，
            // 也就是他是第一次加载，才加载数据）
            initslider(((MallTopsliderHolder) holder), headerData);
            //加载头部数据源
        } else if (holder instanceof TypetypeHolder && mHomeCategories.size() != 0) {
            initcategory(((TypetypeHolder) holder));
            //加载四个category数据源
        } else if (holder instanceof TypeheadHolder) {
            //加载heade数据源（其实这里可以每个head单独设置，因为有的需求head去各式各样）
            initTop(((TypeheadHolder) holder), position);
        } else if (holder instanceof TypeSelect && middlebanner.size() != 0) {
            //加载中间head下面的数据源
            initMiddlebanner(((TypeSelect) holder));
        } else if (holder instanceof TypetypeHolder2 && newgoods.size() != 0) {
            //新品上线 go
//            initCenterBean(((TypetypeHolder2) holder));
        } else if (holder instanceof TypeRefresh && refreshbean.size() != 0) {
            //加载瀑布流数据源
            Log.d("refreshBean.size ", " pos   " + position);
            initrefreshdata(((TypeRefresh) holder), position - COUNT);
        }
    }

    /**
     * 加载头部数据源  轮播图
     */
    private void initslider(MallTopsliderHolder holder, final List<HomeBannerBean> data) {
        //设置播放时间间隔
        holder.rpvBanner.setPlayDelay(5000);
        //设置透明度
        holder.rpvBanner.setAnimationDurtion(500);
        //设置圆点指示器颜色
        holder.rpvBanner.setHintView(new ColorPointHintView(mContext, 0xFFFF0000, Color.WHITE));
        //设置适配器
        RollViewAdapter adapter = new RollViewAdapter(holder.rpvBanner,mContext, data);

        holder.rpvBanner.setAdapter(adapter);

        adapter.notifyDataSetChanged();
        //   LogUtils.e("易商城banneradapter"+headerData.size() );


        //轮播图点击
        holder.rpvBanner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                HomeBannerBean bean = data.get(position);
                LogUtils.e("易商城banneradapter" + bean.getLocation());

                if (bean.getLocation() == null || bean.getLocation().equalsIgnoreCase("")) {
                    return;
                }


                if (bean.getTitle().indexOf("注册送礼") != -1) {
                    mContext.startActivity(new Intent(mContext, WebViewActivity.class)
                            .putExtra("URL", bean.getLocation() + "&app=true")
                            .putExtra("TITLE", bean.getTitle())
                            .putExtra("HTM", "立即注册")
                            //  .putExtra("PID", pid)
                            .putExtra("BANNER", "banner")
                    );
                } else if (bean.getLocation().indexOf("id") != -1) {

                    final String str = "id=";
                    Intent intent3 = new Intent(mContext, MallDetailsActivity.class);
                    intent3.putExtra("pid", Integer.parseInt(bean.getLocation().substring(bean.getLocation().lastIndexOf(str) + str.length())));
                    mContext.startActivity(intent3);
                } else {
                    mContext.startActivity(new Intent(mContext, WebViewActivity.class)
                            .putExtra("URL", bean.getLocation() + "&app=true")
                            .putExtra("TITLE", bean.getTitle())
                            //  .putExtra("PID", pid)
                            .putExtra("BANNER", "banner")
                    );
                }


            }
        });
    }

    /**
     * 加载四个category数据源  分类
     */
    private void initcategory(TypetypeHolder holder) {
        LogUtils.e("分类" + mHomeCategories.size());
        //  holder.rvtype.setLayoutManager(new GridLayoutManager(mContext, mHomeCategories.size()));
        // int spancount = mHomeCategories.size() > 4 ? 5 : 4;
        int spancount = 4;

        holder.rvtype.setLayoutManager(new GridLayoutManager(mContext, spancount));

        TypeCategoryAdapter categoryAdapter = new TypeCategoryAdapter(mContext, mHomeCategories, 3);

        holder.rvtype.setAdapter(categoryAdapter);

        // TODO: 2018/12/27
        categoryAdapter.setOnTypeItemClickListener(new TypeCategoryAdapter.OnTypeItemClickListener() {
            @Override
            public void onTypeItemClick(View view, int position) {

                mContext.startActivity(new Intent(mContext, MallClassifyActivity.class)
                        //  .putExtra("is_presell_plan", true)
                        .putExtra("name", mHomeCategories.get(position).getName())
                        .putExtra("cid", mHomeCategories.get(position).getId())
                );

                //   Toast.makeText(mContext, mHomeCategories.get(position).getTitle(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    /**
     * 加载瀑布流数据源  最下面的列表
     */
    private void initrefreshdata(TypeRefresh holder, final int position) {

        Log.d("refreshBean.size   ", "   " + refreshbean.size());
        final GoodsList bean = refreshbean.get(position);
        holder.tvName.setText(bean.getName());
        holder.tvNumber.setText("已售" + bean.getSellVolume() + "件");
        holder.tvPrice.setText("￥" + StringCut.getNumKb(bean.getRetailPrice()));
//        GlideRoundTransform glideRoundTransform = new GlideRoundTransform(mContext, 5);
        Glide.with(mContext)
                .load(bean.getPrimaryPicUrl())
                //.placeholder(R.drawable.bg_home_banner_fail)
                //.error(R.drawable.bg_home_banner_fail)
                // .centerCrop()
                //.transform(glideRoundTransform)
                .into(holder.ivGoods);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(mContext, MallDetailsActivity.class);

                intent3.putExtra("pid", bean.getId());
                mContext.startActivity(intent3);
            }
        });
    }

    //加载中间head下面的数据源
    private void initCenterBean(TypetypeHolder2 holder) {


        LogUtils.e(getrimaryPicUrl(0) + "商城精选//////" + newgoods.size());
        Glide.with(mContext)
                .load(getrimaryPicUrl(0))
                .into(holder.ivGoods1);

        Glide.with(mContext)
                .load(getrimaryPicUrl(1))
                .into(holder.ivGoods2);
        Glide.with(mContext)
                .load(getrimaryPicUrl(2))
                .into(holder.ivGoods3);
        Glide.with(mContext)
                .load(getrimaryPicUrl(3))
                .into(holder.ivGoods4);
        Glide.with(mContext)
                .load(getrimaryPicUrl(4))
                .into(holder.ivGoods5);
//        Glide.with(mContext)
//                .load(getrimaryPicUrl(5))
//                .into(holder.ivGoods6);
//        Glide.with(mContext)
//                .load(getrimaryPicUrl(6))
//                .into(holder.ivGoods7);

        holder.ivGoods1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent3 = new Intent(mContext, MallDetailsActivity.class);
                intent3.putExtra("pid", getGoodsId(0));
                mContext.startActivity(intent3);

            }
        });
        holder.ivGoods2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(mContext, MallDetailsActivity.class);
                intent3.putExtra("pid", getGoodsId(1));
                mContext.startActivity(intent3);
            }
        });
        holder.ivGoods3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(mContext, MallDetailsActivity.class);
                intent3.putExtra("pid", getGoodsId(2));
                mContext.startActivity(intent3);
            }
        });
        holder.ivGoods4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(mContext, MallDetailsActivity.class);
                intent3.putExtra("pid", getGoodsId(3));
                mContext.startActivity(intent3);
            }
        });
        holder.ivGoods5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(mContext, MallDetailsActivity.class);
                intent3.putExtra("pid", getGoodsId(4));
                mContext.startActivity(intent3);
            }
        });
//        holder.ivGoods6.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent3 = new Intent(mContext, MallDetailsActivity.class);
//                intent3.putExtra("pid", getGoodsId(5));
//                mContext.startActivity(intent3);
//            }
//        });
//        holder.ivGoods7.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent3 = new Intent(mContext, MallDetailsActivity.class);
//                intent3.putExtra("pid", getGoodsId(6));
//                mContext.startActivity(intent3);
//            }
//        });

    }

    private String getrimaryPicUrl(int position) {
        if (newgoods.size() == 0) {
            return "";
        }
        if (position > newgoods.size() - 1) {
            return "";
        } else {
            return newgoods.get(position).getImgUrl();
        }
    }

    private int getGoodsId(int position) {
        if (newgoods.size() == 0) {
            return 0;
        }
        if (position > newgoods.size() - 1) {
            return 0;
        } else {
            final String str = "id=";
            //newgoods.get(position).getId()
            return Integer.parseInt(newgoods.get(position).getLocation().substring(newgoods.get(position).getLocation().lastIndexOf(str) + str.length()));
        }
    }

    //商城精选
    private void initMiddlebanner(TypeSelect holder) {


        //取列表的前三个

       /* final GoodsMiddlebanner middle1 = getMiddlebanner(0, 2);
        final GoodsMiddlebanner middle2 = getMiddlebanner(1, 1);
        final GoodsMiddlebanner middle3 = getMiddlebanner(2, 0);*/
        final GoodsMiddlebanner middle1 = middlebanner.get(0);
        final GoodsMiddlebanner middle2 = middlebanner.get(1);
        final GoodsMiddlebanner middle3 = middlebanner.get(2);

        GlideRoundTransform glideRoundTransform = new GlideRoundTransform(mContext, 5);

        Glide.with(mContext)
                .load(middle1.getImgUrl())
                //.placeholder(R.drawable.bg_home_banner_fail)
                //.error(R.drawable.bg_home_banner_fail)
                //.centerCrop()
                .transform(glideRoundTransform)
                .into(holder.ibMall1);
        LogUtils.e(middle1.getImgUrl() + "商城精选//////" + middlebanner.size());
        Glide.with(mContext)
                .load(middle2.getImgUrl())
                //.placeholder(R.drawable.bg_home_banner_fail)
                //.error(R.drawable.bg_home_banner_fail)
                .centerCrop()
                .transform(glideRoundTransform)
                .into(holder.ibPackage1);
        Glide.with(mContext)
                .load(middle3.getImgUrl())
                //.placeholder(R.drawable.bg_home_banner_fail)
                //.error(R.drawable.bg_home_banner_fail)
                .centerCrop()
                .transform(glideRoundTransform)
                .into(holder.ibPackage2);

        final String str = "id=";
        holder.ibMall1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent3 = new Intent(mContext, MallDetailsActivity.class);
                intent3.putExtra("pid", Integer.parseInt(middle1.getLocation().substring(middle1.getLocation().lastIndexOf(str) + str.length())));
                mContext.startActivity(intent3);
            }
        });
        holder.ibPackage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(mContext, MallDetailsActivity.class);

                intent3.putExtra("pid", Integer.parseInt(middle2.getLocation().substring(middle2.getLocation().lastIndexOf(str) + str.length())));
                mContext.startActivity(intent3);
            }
        });
        holder.ibPackage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(mContext, MallDetailsActivity.class);

                intent3.putExtra("pid", Integer.parseInt(middle3.getLocation().substring(middle3.getLocation().lastIndexOf(str) + str.length())));
                mContext.startActivity(intent3);
            }
        });

      /*  holder.ibMall1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(mContext, MallDetailsActivity.class);

                intent3.putExtra("pid", middle1.getId());
                mContext.startActivity(intent3);
            }
        });
        holder.ibPackage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(mContext, MallDetailsActivity.class);

                intent3.putExtra("pid", middle2.getId());
                mContext.startActivity(intent3);
            }
        });
        holder.ibPackage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(mContext, MallDetailsActivity.class);

                intent3.putExtra("pid", middle3.getId());
                mContext.startActivity(intent3);
            }
        });*/
       /* holder.rvtype.setLayoutManager(new GridLayoutManager(mContext, 2));

        TypeHistoryAdapter centerAdapter = new TypeHistoryAdapter(mContext, centerBean);


        holder.rvtype.setAdapter(centerAdapter);*/


    }

    private GoodsMiddlebanner getMiddlebanner(int sort, int positon) {

        for (int i = 0; i < middlebanner.size(); i++) {
            GoodsMiddlebanner middlebannerBean = middlebanner.get(i);
            if (middlebannerBean.getSort() == sort) {
                return middlebannerBean;
            }
        }
        return middlebanner.get(positon);
    }


    //加载heade数据源（其实这里可以每个head单独设置，因为有的需求head去各式各样）
    private void initTop(TypeheadHolder holder, int position) {
        switch (position) {
//            case 2:
//                holder.ivMore.setVisibility(View.GONE);
//                holder.ivHot.setImageResource(R.drawable.ic_hand_pick_text);
//                setLayoutWidth(holder.ivHot, (int) mContext.getResources().getDimension(R.dimen.dp_60));
//                holder.ivHotRight.setImageResource(R.drawable.ic_hand_pick_icon);
//                setLayoutWidth(holder.ivHotRight, (int) mContext.getResources().getDimension(R.dimen.dp_65));
//
//                break;
            case 3:
                holder.ivHot.setImageResource(R.drawable.ic_hot_sale_text);
                holder.ivHotRight.setImageResource(R.drawable.ic_hot_icon);
                setLayoutWidth(holder.ivHot, (int) mContext.getResources().getDimension(R.dimen.dp_60));
                setLayoutWidth(holder.ivHotRight, (int) mContext.getResources().getDimension(R.dimen.dp_35));
                holder.ivMore.setVisibility(View.GONE);
                break;

        }
    }

    /**
     * 设置ImageView的宽度
     *
     * @param view
     * @param width
     */
    private void setLayoutWidth(View view, int width) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = width;
        view.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        return count;
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        this.recyclerView = recyclerView;

      /*  流式布局
       RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof MyStaggerGrildLayoutManger) {
            mystager = ((MyStaggerGrildLayoutManger) layoutManager);

        }*/
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                //如果快速滑动， 不加载图片
                if (newState == 2) {
                    Glide.with(mContext).pauseRequests();
                } else {
                    Glide.with(mContext).resumeRequests();

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            }
        });
    }


    /**
     * 添加数据
     */
    public void setheaderbean(List<HomeBannerBean> headerbean) {

        headerData = headerbean;

        notifyDataSetChanged();


    }

    public void setRefreshBean(List<GoodsList> refreshBean, boolean flagFirst) {

        if (flagFirst) {
            refreshbean.clear();
            this.count = COUNT;
            //recyclerView.smoothScrollToPosition(count + 1);//加载完以后向上滚动3个条目
        }
        //  refreshbean.clear();

        // this.count = 6;

        refreshbean.addAll(refreshBean);
        // int count1 = this.count;
        this.count += refreshBean.size();
        notifyDataSetChanged();
      /*  if (!flagFirst) {
            recyclerView.smoothScrollToPosition(count1 + 2);//加载完以后向上滚动3个条目
        }*/

    }

    public void setNewGoods(List<GoodsNewList> refreshBean) {
        newgoods.clear();
        newgoods.addAll(refreshBean);

        // int count1 = this.count;
        //this.count += refreshBean.size();
        notifyDataSetChanged();
      /*  if (!flagFirst) {
            recyclerView.smoothScrollToPosition(count1 + 2);//加载完以后向上滚动3个条目
        }*/

    }


    public void setCenterBean(List<HomeHostProduct> refreshBean) {
        centerBean = refreshBean;
        // count++;
        notifyDataSetChanged();
    }


    //分类的
    public void setCategoryBean(ArrayList<GoodsCategory> homeCategories) {
        mHomeCategories = homeCategories;
        //  count++;
        notifyDataSetChanged();


    }

    // 商城精选
    public void setMiddlebanner(List<GoodsMiddlebanner> homeCategories) {
        middlebanner = homeCategories;
        //  count++;
        notifyDataSetChanged();

    }


    /**
     * 头部Viewpager viewholder  轮播图的
     */
  /*  public class TypeTopsliderHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rpv_banner)
        RollPagerView rpvBanner;
        @BindView(R.id.marqueeView)
        MarqueeView marqueeView;
        @BindView(R.id.tv_notice)
        TextView tvNotice;
        @BindView(R.id.rl_notice)
        LinearLayout rlNotice;

        public TypeTopsliderHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }*/

    /**
     * 头部Viewpager viewholder  商城的不带公告的 轮播图的
     */
    public class MallTopsliderHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rpv_banner)
        RollPagerView rpvBanner;

        public MallTopsliderHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    public class TypeheadHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rl_top)
        RelativeLayout rltop;
        @BindView(R.id.iv_ashomeitem_left)
        TextView ivAshomeitemLeft;
        @BindView(R.id.iv_more)
        ImageView ivMore;
        @BindView(R.id.iv_hot)
        ImageView ivHot;
        @BindView(R.id.iv_hot_right)
        ImageView ivHotRight;


        public TypeheadHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

          /*  hview.setMoreclicklistenser(new AsHomepageHeaderView.MoreclickListenser() {
                @Override
                public void setmoreclicklistenser() {
                }
            });*/
        }
    }

    /**
     * 中间的四个type 分类
     */
    public class TypetypeHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rv_homepageradapter_artist)
        RecyclerView rvtype;


        public TypetypeHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public class TypeCenterHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv)
        ImageView iv;

        public TypeCenterHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 加载中间head下面的数据源
     */
    public class TypetypeHolder2 extends RecyclerView.ViewHolder {
        /*@BindView(R.id.rv_homepageradapter_artist)
        RecyclerView rvtype;*/

        @BindView(R.id.iv_goods_1)
        ImageView ivGoods1;
        @BindView(R.id.iv_goods_2)
        ImageView ivGoods2;
        @BindView(R.id.iv_goods_3)
        ImageView ivGoods3;
        @BindView(R.id.iv_goods_4)
        ImageView ivGoods4;
        @BindView(R.id.iv_goods_5)
        ImageView ivGoods5;
//        @BindView(R.id.iv_goods_6)
//        ImageView ivGoods6;
//        @BindView(R.id.iv_goods_7)
//        ImageView ivGoods7;


        @BindView(R.id.iv_go)
        ImageButton iv_go;

        public TypetypeHolder2(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 商城精选
     */
    static class TypeSelect extends RecyclerView.ViewHolder {
        @BindView(R.id.ib_mall_1)
        ImageButton ibMall1;
        @BindView(R.id.strut)
        View strut;
        @BindView(R.id.ib_package_1)
        ImageButton ibPackage1;
        @BindView(R.id.ib_package_2)
        ImageButton ibPackage2;

        TypeSelect(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 加载瀑布流数据源  最下面的列表
     */
    static class TypeRefresh extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_goods)
        ImageView ivGoods;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.tv_number)
        TextView tvNumber;

        TypeRefresh(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


}
