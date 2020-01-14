package com.ekabao.oil.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ekabao.oil.R;
import com.ekabao.oil.bean.Add_Bean;
import com.ekabao.oil.bean.GoodsList;
import com.ekabao.oil.bean.GoodsMiddlebanner;
import com.ekabao.oil.bean.HomeBannerBean;
import com.ekabao.oil.bean.HomeHostProduct;
import com.ekabao.oil.bean.HomeInfoList;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.ui.activity.LoginActivity;
import com.ekabao.oil.ui.activity.MainActivity;
import com.ekabao.oil.ui.activity.MallActivity;
import com.ekabao.oil.ui.activity.MallClassifyActivity;
import com.ekabao.oil.ui.activity.MallDetailsActivity;
import com.ekabao.oil.ui.activity.MallHomeActivity;
import com.ekabao.oil.ui.activity.OilCardBuyActivity;
import com.ekabao.oil.ui.activity.OilCardImmediateActivity;
import com.ekabao.oil.ui.activity.PhoneRechargeActivity;
import com.ekabao.oil.ui.activity.WebViewActivity;
import com.ekabao.oil.ui.activity.find.AtyCarBreak;
import com.ekabao.oil.ui.activity.find.AtyLocationOil;
import com.ekabao.oil.ui.activity.find.AtyOilCity;
import com.ekabao.oil.ui.activity.me.CustomerServiceActivity;
import com.ekabao.oil.ui.view.MarqueeView;
import com.ekabao.oil.util.LogUtils;
import com.ekabao.oil.util.StringCut;
import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.IconHintView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * RecyclerView 多种布局
 * Created by lj on 2018/12/28.
 */
public class HomePagerRecycleAdapter extends RecyclerView.Adapter {


    private static final String TAG = "HomepagerRecycleAdapter";
    private final Context mContext;
    private List<HomeBannerBean> headerData;  //轮播图

    private List<Add_Bean> lsAd = new ArrayList<>(); //公告
    public static final int COUNT = 7;
    private int count = COUNT;

    //private List<GoodsList> refreshbean;  //最下面的
    private List<GoodsList> refreshbean;  //最下面的

    private List<HomeHostProduct> centerBean;  // 油卡充值套餐   新品上线

    private ArrayList<HomeInfoList.LogoListBean> mHomeCategories = new ArrayList<>(); //分类按钮
    private ArrayList<HomeInfoList.LogoListBean> mHomeCategories2 = new ArrayList<>(); //八个的分类按钮

    private List<GoodsMiddlebanner> middlebanner;  // 商城精选

    private int TYPE_BANNER = 1;//头部布局
    private List<Integer> mHeights = new ArrayList<>();
    private int TYPE_NOTICE = 2;//公告
    private int TYPE_CATEGORY = 3;//中间的四个快速入口
    private int TYPE_HEADER = 4;//每个分类的head

    private int TYPE_HOT = 5;// 热门套餐
    private int TYPE_MALL = 6;//易卡宝商城
    private int TYPE_SERVICE = 7;//特色服务


    private LayoutInflater inflater;
    private RecyclerView recyclerView;
    private HotHolder newHotHolder;

    //private MyStaggerGrildLayoutManger mystager; //流式布局
    private int type = 1;// 1 首页 2 商城首页
    private SharedPreferences preferences = LocalApplication.sharereferences;

    public HomePagerRecycleAdapter(Context context, int TYPE) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        //初始化各我数据源
        headerData = new ArrayList<>();
        refreshbean = new ArrayList<>();
        centerBean = new ArrayList<>();
        middlebanner = new ArrayList<>();
        type = TYPE;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_BANNER) {
            //头部轮播图
            if (type == 1) {
                View viewtop = inflater.inflate(R.layout.item_home_mall_banner, parent, false);
                return new TypeTopsliderHolder(viewtop);
            } else {
                View viewtop = inflater.inflate(R.layout.item_mall_banner, parent, false);
                return new MallTopsliderHolder(viewtop);
            }
        } else if (viewType == TYPE_CATEGORY) {
            //四个快速入口的holder
            View view = inflater.inflate(R.layout.item_home_type_rv, parent, false);
            return new CategoryHolder(view);

        } else if (viewType == TYPE_NOTICE) {
            //公告
            return new NoticeHolder(inflater.inflate(R.layout.item_home_mall_notice, parent, false));

        } else if (viewType == TYPE_HEADER) {
            //每个分类的head
            View view2 = inflater.inflate(R.layout.item_home_headerview2, parent, false);
            return new HeadHolder(view2);

        } else if (viewType == TYPE_HOT) {
            // 热门套餐
            View view = inflater.inflate(R.layout.item_home_hot, parent, false);
            newHotHolder = new HotHolder(view);
            return newHotHolder;
        } else if (viewType == TYPE_MALL) {
            return new MallHolder(inflater.inflate(R.layout.item_home_mall, parent, false));
        } else {
            //特色服务
            Log.d("HomePagerRecyclerView", "  typeHolder");
            View view = inflater.inflate(R.layout.item_mall_home_2, parent, false);
            return new TypeRefresh(view);
//            return new TypeSelect(inflater.inflate(R.layout.item_home_type_rv_2, parent, false));
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TypeTopsliderHolder && headerData.size() != 0) {
            //&& ((TypeTopsliderHolder) holder).linearLayout.getChildCount() == 0
            //如果是TypeTopsliderHolder， 并且header有数据，并且TypeTopsliderHolder的linearLayout没有子view（因为这个布局只出现一次，如果他没有子view，
            // 也就是他是第一次加载，才加载数据）
            initslider(((TypeTopsliderHolder) holder), headerData);
            //加载头部数据源
        } else if (holder instanceof CategoryHolder && centerBean.size() != 0) {
            //加载四个category数据源
            initCategory(((CategoryHolder) holder));

        } else if (holder instanceof TypeRefresh && centerBean.size() != 0 && mHomeCategories2.size() == 0) {

            //加载八个category数据源  分类
            Log.d(TAG, "   size   " + mHomeCategories2.size() + "      " + position);
//            initCategory2(((TypetypeHolder2) holder));

            initRefreshData(((TypeRefresh) holder), position - TYPE_SERVICE);
        } else if (holder instanceof NoticeHolder && lsAd.size() != 0) {
            //公告
            initNotice(((NoticeHolder) holder), lsAd);

        } else if (holder instanceof HeadHolder) {
            //加载heade数据源（其实这里可以每个head单独设置，因为有的需求head去各式各样）
            initHead(((HeadHolder) holder), position);
        } else if (holder instanceof TypeSelect && middlebanner.size() != 0) {
            // 商城精选 TypeSelect
//            initCategory2((TypetypeHolder2) holder);
//            initMiddlebanner(((TypeSelect) holder));

        } else if (holder instanceof TypeHotHolder && centerBean.size() != 0) {

            //加载 油卡套餐的 热门套餐
            Log.d(TAG, "   centerbean in bind view holder   " + centerBean.size());

            initCenterBean(((HotHolder) holder));
//            initNewCenterBean((HotHolder) holder);
        } else if (holder instanceof MallHolder) {
            initMall((MallHolder) holder);
        } else if (holder instanceof TypeRefresh && refreshbean.size() != 0) {
            //加载瀑布流数据源  REFRESHPOSITION
            // TODO: 2019/3/19
//            initRefreshData(((TypeRefresh) holder), position - TYPE_HEADER - 1);

        }

    }


    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0: //轮播图
                return TYPE_BANNER;
            case 1:
                return TYPE_CATEGORY;//中间的四个快速入口
            case 2:
                return TYPE_NOTICE; //公告
            case 3:
                return TYPE_HEADER;//每个分类的head 热门套餐
            case 4:
                return TYPE_HOT;
            case 5:
                return TYPE_HEADER;//每个分类的head 商城精选
            case 6:
                return TYPE_MALL;//易卡宝商城
            default:
                return TYPE_SERVICE;
        }

    }


    @Override
    public int getItemCount() {
        return count;
    }


    private void initMall(MallHolder holder) {
        holder.ivHotRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, MallActivity.class));
            }
        });
    }

    /**
     * 加载头部数据源  轮播图
     */
    private void initslider(TypeTopsliderHolder holder, final List<HomeBannerBean> data) {

        LogUtils.e("加载头部数据源  轮播图");
        //设置适配器
        RollViewAdapter adapter = new RollViewAdapter(holder.rpvBanner, mContext, data);
        holder.rpvBanner.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        //设置播放时间间隔
        holder.rpvBanner.setPlayDelay(5000);
        //设置透明度
        holder.rpvBanner.setAnimationDurtion(500);
        //设置圆点指示器颜色
        //holder.rpvBanner.setHintView(new ColorPointHintView(mContext, 0xFFFF0000, Color.WHITE));

        holder.rpvBanner.setHintView(new IconHintView(mContext, R.drawable.bg_banner_selected, R.drawable.bg_banner_unselected));

        holder.rpvBanner.setHintPadding(0, (int) (mContext.getResources().getDimension(R.dimen.dp_4)), 0, (int) (mContext.getResources().getDimension(R.dimen.dp_4)));

        //轮播图点击
        holder.rpvBanner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                HomeBannerBean bean = data.get(position);
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
    private void initCategory(CategoryHolder holder) {
        int spanCount = mHomeCategories.size() > 4 ? 5 : 4;

        holder.rvtype.setLayoutManager(new GridLayoutManager(mContext, spanCount));

        TypeCategoryAdapter categoryAdapter = new TypeCategoryAdapter(mContext, mHomeCategories);

        holder.rvtype.setAdapter(categoryAdapter);

        // TODO: 2018/12/27
        categoryAdapter.setOnTypeItemClickListener(new TypeCategoryAdapter.OnTypeItemClickListener() {
            @Override
            public void onTypeItemClick(View view, int position) {
                // TODO: 2019/1/7   clickUrl 
                //TextUtils.

                if (mHomeCategories.get(position).getTitle().contains("油卡直充")) {
                    mContext.startActivity(new Intent(mContext, OilCardImmediateActivity.class)
                            //  .putExtra("is_presell_plan", true)
                            // .putExtra("startDate", isSellingList.get(0).getStartDate())
                            //.putExtra("pid", bean1.getId())
                            .putExtra("money", 1000));


                } else if (mHomeCategories.get(position).getTitle().contains("领")) {

                    mContext.startActivity(new Intent(mContext, OilCardBuyActivity.class)
                            //  .putExtra("is_presell_plan", true)
                            // .putExtra("startDate", isSellingList.get(0).getStartDate())
                            //.putExtra("pid", bean1.getId())
                            .putExtra("money", 1000));
                } else if (mHomeCategories.get(position).getTitle().contains("手机")) {
                    if (preferences.getString("uid", "").equalsIgnoreCase("")) {
                        //MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");
                        // startActivity(new Intent(mContext, LoginActivity.class));
                        mContext.startActivity(new Intent(mContext, LoginActivity.class));
                    } else {
                        mContext.startActivity(new Intent(mContext, PhoneRechargeActivity.class));
                    }
                } else if (mHomeCategories.get(position).getTitle().contains("油卡套餐")) {

                   /* MainActivity activity = (MainActivity) mContext.getActivity();
                    activity.setOilFragment(homeHostProduct.get(position).getId(), 500, homeHostProduct.get(position).getDeadline());
                    activity.switchFragment(1);
                    activity.resetTabState();*/
                    if (onItemClickListener != null) {

                        onItemClickListener.onTypeItemClick(view, 999);
                    }
                   /* mContext.startActivity(new Intent(mContext, OilCardPackageActivity.class)
                            //  .putExtra("is_presell_plan", true)
                            // .putExtra("startDate", isSellingList.get(0).getStartDate())
                            //.putExtra("pid", bean1.getId())
                            //.putExtra("money", 1000)
                    );*/

                } else if (mHomeCategories.get(position).getTitle().contains("商城")) {
                    mContext.startActivity(new Intent(mContext, MallHomeActivity.class)
                            //  .putExtra("is_presell_plan", true)
                            // .putExtra("startDate", isSellingList.get(0).getStartDate())
                            //.putExtra("pid", bean1.getId())
                            .putExtra("money", 1000));

                } else if (mHomeCategories.get(position).getTitle().contains("客服中心")) {
                    mContext.startActivity(new Intent(mContext, CustomerServiceActivity.class));
                }

                //  Toast.makeText(mContext, mHomeCategories.get(position).getTitle(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    /**
     * 公告
     */
    private void initNotice(NoticeHolder holder, final List<Add_Bean> data) {
        List<String> info = new ArrayList<>();
        for (int i = 0; i < lsAd.size(); i++) {
            info.add(lsAd.get(i).getTitle().toString());
        }
      /*  List note = RandomValue.getNote();
        List<String> info = new ArrayList<>();
        info.addAll(note);*/
        holder.marqueeView.startWithList(info);


        holder.marqueeView.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, TextView textView) {
                LogUtils.e("marqueeView" + position);

                mContext.startActivity(new Intent(mContext, WebViewActivity.class)
                        .putExtra("URL", UrlConfig.WEBSITEAN + "?app=true&id=" + lsAd.get(position).getArti_id())
                        .putExtra("TITLE", "平台公告"));
            }
        });

    }

    /**
     * 加载八个category数据源  分类
     */
    private void initCategory2(TypetypeHolder2 holder) {

        //  holder.rvtype.setLayoutManager(new GridLayoutManager(mContext, mHomeCategories.size()));
        // int spancount = mHomeCategories.size() > 4 ? 5 : 4;

        int spanCount = 2;

        holder.rvtype.setLayoutManager(new GridLayoutManager(mContext, spanCount));
        mHomeCategories2 = new ArrayList<>();

        mHomeCategories2.add(new HomeInfoList.LogoListBean(2, R.drawable.ic_home_service_1, "新人福利", "送1088红包"));
        mHomeCategories2.add(new HomeInfoList.LogoListBean(3, R.drawable.ic_home_service_2, "邀请好友", "送加油红包"));
        mHomeCategories2.add(new HomeInfoList.LogoListBean(4, R.drawable.ic_home_service_3, "安全保障", ""));
        mHomeCategories2.add(new HomeInfoList.LogoListBean(8, R.drawable.ic_home_service_4, "关于我们", ""));

        Log.d("HomePagerViewHolder", " mHomeCategories.size  " + mHomeCategories2.size());
        TypeCategoryAdapter categoryAdapter = new TypeCategoryAdapter(mContext, 2, this.mHomeCategories2);

        holder.rvtype.setAdapter(categoryAdapter);

        categoryAdapter.setOnTypeItemClickListener(new TypeCategoryAdapter.OnTypeItemClickListener() {
            @Override
            public void onTypeItemClick(View view, int position) {

                HomeInfoList.LogoListBean logoListBean = mHomeCategories2.get(position);

                switch (logoListBean.getId()) {
                    case 1://特惠商城

                        /*MainActivity activity = (MainActivity) mContext.getActivity();
                        //activity.setOilFragment(homeHostProduct.get(position).getId(), 500, homeHostProduct.get(position).getDeadline());
                        activity.switchFragment(1);
                        activity.resetTabState();*/
                        if (onItemClickListener != null) {

                            onItemClickListener.onTypeItemClick(view, 666);
                        }
                        //  mContext.startActivity(new Intent(mContext, MallHomeActivity.class));
                        break;
                    case 2://新人福利
                        mContext.startActivity(new Intent(mContext, WebViewActivity.class)
                                .putExtra("URL", "https://m.ekabao.cn/oilCardWelfare?upgrade=1&app=true")
                                .putExtra("TITLE", "新人福利")
                                // .putExtra("PID", pid)
                                .putExtra("BANNER", "banner")
                        );
                        break;
                    case 3://邀请好友
                        mContext.startActivity(new Intent(mContext, WebViewActivity.class)
                                .putExtra("URL", "https://m.ekabao.cn/invitation?upgrade=1&app=true")
                                .putExtra("TITLE", "邀请好友")
                                // .putExtra("PID", pid)
                                .putExtra("BANNER", "banner")
                        );

                        break;
                    case 4://安全保障
                        mContext.startActivity(new Intent(mContext, WebViewActivity.class)
                                .putExtra("URL", "https://m.ekabao.cn/activitycenter?upgrade=1&app=true")
                                .putExtra("TITLE", "安全保障")
                                .putExtra("BANNER", "banner")
                        );
                        //mContext.startActivity(new Intent(mContext, MallHomeActivity.class));
                        break;
                    case 5://附近油站
                        mContext.startActivity(new Intent(mContext, AtyLocationOil.class));
                        break;
                    case 6://违章查询

                        mContext.startActivity(new Intent(mContext, AtyCarBreak.class));
                        break;
                    case 7://今日油价
                        mContext.startActivity(new Intent(mContext, AtyOilCity.class)
                                .putExtra("city", "浙江")
                        );
                        break;
                    case 8://关于我们
                        mContext.startActivity(new Intent(mContext, WebViewActivity.class)
                                .putExtra("URL", UrlConfig.SAFE)
                                .putExtra("TITLE", "关于我们")
                                .putExtra("noWebChrome", "aboutMe"));
                        //  mContext.startActivity(new Intent(mContext, AboutActivity.class));

                        break;


                }


            }
        });


    }

    /**
     * 加载瀑布流数据源  最下面的列表
     */
    private void initRefreshData(TypeRefresh holder, final int position) {
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

    /**
     * 热门套餐
     * TypetypeHolder2
     */
    private void initCenterBean(HotHolder holder) {

        if (centerBean.size() > 4) {
            centerBean = centerBean.subList(0, 4);
        }

        final OilCardPackageHomeAdapter adapter = new OilCardPackageHomeAdapter(centerBean, 0, 1);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2, LinearLayoutManager.VERTICAL, false);
//        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                int type = adapter.getItemViewType(position);
//                switch (type) {
//                    case OilCardPackageHomeAdapter.VIEW_FIRST:
//                        return 2;
//                    case OilCardPackageHomeAdapter.VIEW_ELSE:
//                        return 1;
//                }
//                return 1;
//            }
//        });

        holder.rcvPackage.setLayoutManager(gridLayoutManager);

        holder.rcvPackage.setAdapter(adapter);
        adapter.setPosition(0);
        adapter.setOnItemClickLitener(new OilCardPackageHomeAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int positon) {

                if (onItemClickListener != null) {

                    onItemClickListener.onTypeItemClick(view, positon);
                }
            }
        });
        holder.ivHotLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) mContext;
                activity.setCheckedFram(2);
            }
        });
        holder.ivHotRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, WebViewActivity.class)
                        .putExtra("URL", "https://m.ekabao.cn/oilCardWelfare?upgrade=1&app=true")
                        .putExtra("TITLE", "新人福利")
                        // .putExtra("PID", pid)
                        .putExtra("BANNER", "banner")
                );
            }
        });

        holder.ivHotInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, WebViewActivity.class)
                        .putExtra("URL", "https://m.ekabao.cn/invitation?upgrade=1&app=true")
                        .putExtra("TITLE", "邀请好友")
                        // .putExtra("PID", pid)
                        .putExtra("BANNER", "banner")
                );
            }
        });

    }

    private void initNewCenterBean(HotHolder hotHolder) {
//        Log.d(TAG, "centerBean  " + centerBean.size());
//        hotHolder.tvRate1.setText(getRate(centerBean.get(0).getRate()) + "");
//        hotHolder.tvRate2.setText(getRate(centerBean.get(1).getRate()) + "");
//        hotHolder.tvRate3.setText(getRate(centerBean.get(2).getRate()) + "");
//        hotHolder.tvRate4.setText(getRate(centerBean.get(3).getRate()) + "");
//        hotHolder.tvName1.setText("折加油套餐/" + centerBean.get(0).getDeadline() + "个月");
//        hotHolder.tvName2.setText("折套餐/" + centerBean.get(1).getDeadline() + "个月");
//        hotHolder.tvName3.setText("折套餐/" + centerBean.get(2).getDeadline() + "个月");
//        hotHolder.tvName4.setText("折套餐/" + centerBean.get(3).getDeadline() + "个月");
//        hotHolder.tvExample1.setText("例充值500元/月");
//        hotHolder.tvExample2.setText("例充值500元/月");
//        hotHolder.tvExample3.setText("例充值500元/月");
//        hotHolder.tvExample4.setText("例充值500元/月");
//        hotHolder.tvDiscount1.setText("立省" + new Double(Arith.mul(Arith.mul(Arith.sub(1, centerBean.get(0).getRate()), 500), centerBean.get(0).getDeadline())).intValue() + "元");
//        hotHolder.tvDiscount2.setText("立省" + new Double(Arith.mul(Arith.mul(Arith.sub(1, centerBean.get(1).getRate()), 500), centerBean.get(1).getDeadline())).intValue() + "元");
//        hotHolder.tvDiscount3.setText("省" + new Double(Arith.mul(Arith.mul(Arith.sub(1, centerBean.get(2).getRate()), 500), centerBean.get(2).getDeadline())).intValue() + "元");
//        hotHolder.tvDiscount4.setText("省" + new Double(Arith.mul(Arith.mul(Arith.sub(1, centerBean.get(3).getRate()), 500), centerBean.get(3).getDeadline())).intValue() + "元");
//
//        hotHolder.clPackage1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selectPackage(0);
//            }
//        });
//        hotHolder.clPackage2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selectPackage(1);
//            }
//        });
//        hotHolder.clPackage3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selectPackage(2);
//            }
//        });
//        hotHolder.clPackage4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selectPackage(3);
//            }
//        });
    }


    private void selectPackage(int position) {
        int money = 500;
        SharedPreferences.Editor edit = preferences.edit();
        edit.putInt("oid_pid", centerBean.get(position).getId());
        edit.putInt("oid_money", money);
        // edit.putInt("oid_pid",homeHostProduct.get(position).getId());
        edit.commit();
        LogUtils.e("setOnCenterItemClickListener+" + centerBean.get(position).getId());

        MainActivity activity = (MainActivity) mContext;
        //activity.setOilFragment(homeHostProduct.get(position).getId(), 500, homeHostProduct.get(position).getDeadline());
//                        activity.switchFragment(1);
        activity.setCheckedFram(2);
    }

    private String getRate(double rate) {
        DecimalFormat format = new DecimalFormat("0.0");
        BigDecimal multiply = new BigDecimal(10).multiply(new BigDecimal(rate));
        return format.format(multiply) + "";
    }

    public OnCenterItemClickListener onItemClickListener;

    public interface OnCenterItemClickListener {
        void onTypeItemClick(View view, int position);

    }

    public void setOnCenterItemClickListener(OnCenterItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    //特色服务
    private void initMiddlebanner(TypeSelect holder) {

        //  holder.rvtype.setLayoutManager(new GridLayoutManager(mContext, mHomeCategories.size()));
        int spanCount = 2;

        holder.rvtype.setPadding(0, 0, (int) mContext.getResources().getDimension(R.dimen.dp_15), 0);
        holder.rvtype.setLayoutManager(new GridLayoutManager(mContext, spanCount));

        final List<GoodsMiddlebanner> list = new ArrayList<>();

        Log.d(TAG, "middleBanner,size    " + middlebanner.size());
        if (middlebanner.size() > 4) {

            list.add(middlebanner.get(middlebanner.size() - 4));
            list.add(middlebanner.get(middlebanner.size() - 3));
            list.add(middlebanner.get(middlebanner.size() - 2));
            list.add(middlebanner.get(middlebanner.size() - 1));
        } else {
            list.addAll(middlebanner);
        }


        MallHomeSelectAdapter categoryAdapter = new MallHomeSelectAdapter(mContext, list);

        holder.rvtype.setAdapter(categoryAdapter);

        final String str = "id=";
        categoryAdapter.setOnTypeItemClickListener(new MallHomeSelectAdapter.OnTypeItemClickListener() {
            @Override
            public void onTypeItemClick(View view, int position) {

                mContext.startActivity(new Intent(mContext, MallClassifyActivity.class)
                        //  .putExtra("is_presell_plan", true)
                        .putExtra("name", list.get(position).getTitle())
                        .putExtra("cid", Integer.parseInt(list.get(position).getLocation().substring(list.get(position).getLocation().lastIndexOf(str) + str.length())))
                );

            }
        });

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


    /***
     * 加载heade数据源（其实这里可以每个head单独设置，因为有的需求head去各式各样）
     * */
    private void initHead(HeadHolder holder, int position) {
        // 热门套餐 套餐多多 折扣多多
        //商城精选 海量商品 任你选择
        //限时活动 超值折扣 限时抢购

        switch (position) {
            case 3:
                holder.tvTitle.setText("加油福利");
                break;
            case 5:
                holder.tvTitle.setText("车主商城");
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
    public void setheaderbean(HomeInfoList headerbean) {
        headerData = headerbean.getBanner();
        notifyDataSetChanged();
    }

    /**
     * 公告
     */

    public void setNotice(List<Add_Bean> notice) {

        lsAd.addAll(notice);
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

    public void setCenterBean(List<HomeHostProduct> refreshBean) {
        centerBean.clear();
        centerBean.addAll(refreshBean);
        //centerBean = refreshBean;
        // count++;
        notifyDataSetChanged();
        initCenterBean(newHotHolder);
    }


    //分类的
    public void setCategoryBean(ArrayList<HomeInfoList.LogoListBean> homeCategories) {
        mHomeCategories = homeCategories;
        //  count++;
        notifyDataSetChanged();

    }

    // 商城精选
    public void setMiddlebanner(List<GoodsMiddlebanner> homeCategories) {
        middlebanner = homeCategories;
        LogUtils.e("middlebanner商城精选" + middlebanner.size());
        //  count++;
        notifyDataSetChanged();

    }


    /**
     * 头部Viewpager viewholder  轮播图的
     */
    public class TypeTopsliderHolder extends RecyclerView.ViewHolder {

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
    }

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


    public class HeadHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rl_top)
        RelativeLayout rlTop;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.iv_more)
        ImageView ivMore;
        @BindView(R.id.iv_hot)
        ImageView ivHot;
        @BindView(R.id.iv_hot_right)
        ImageView ivHotRight;

        public HeadHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 中间的四个type 分类
     */
    public class CategoryHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rv_homepageradapter_artist)
        RecyclerView rvtype;


        public CategoryHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 中间的八个type 分类
     */
    public class TypetypeHolder2 extends RecyclerView.ViewHolder {

        @BindView(R.id.rv_homepageradapter_artist)
        RecyclerView rvtype;

        public TypetypeHolder2(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 热门套餐
     */
    public class TypeHotHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rv_homepageradapter_artist)
        RecyclerView rvtype;


        public TypeHotHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 热门套餐
     */
    public class HotHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_hot_left)
        ImageView ivHotLeft;
        @BindView(R.id.rcv_package)
        RecyclerView rcvPackage;
        @BindView(R.id.iv_hot_red)
        ImageView ivHotRed;
        @BindView(R.id.iv_hot_invite)
        ImageView ivHotInvite;

        public HotHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 商城精选
     */
    static class TypeSelect extends RecyclerView.ViewHolder {

        @BindView(R.id.rv_homepageradapter_artist)
        RecyclerView rvtype;


        TypeSelect(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 加载瀑布流数据源  最下面的列表
     */
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

    /**
     * 公告
     */
    static class NoticeHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.marqueeView)
        MarqueeView marqueeView;
        @BindView(R.id.tv_notice)
        TextView tvNotice;
        @BindView(R.id.cl_notice)
        ConstraintLayout clNotice;

        NoticeHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class MallHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_hot_red)
        ImageView ivHotRed;

        MallHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
