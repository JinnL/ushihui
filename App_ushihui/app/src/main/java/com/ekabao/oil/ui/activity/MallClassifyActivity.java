package com.ekabao.oil.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ekabao.oil.R;
import com.ekabao.oil.adapter.BaseRecyclerViewAdapter;
import com.ekabao.oil.adapter.MallClassifyAdapter;
import com.ekabao.oil.adapter.viewholder.BaseViewHolder;
import com.ekabao.oil.bean.GoodsCategory;
import com.ekabao.oil.bean.GoodsList;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.ui.view.ToastMaker;
import com.ekabao.oil.util.LogUtils;
import com.scwang.smartrefresh.header.BezierCircleHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class MallClassifyActivity extends BaseActivity {

    @BindView(R.id.title_lefttextview)
    TextView titleLefttextview;
    @BindView(R.id.title_leftimageview)
    ImageView titleLeftimageview;
    @BindView(R.id.title_centertextview)
    TextView titleCentertextview;
    @BindView(R.id.title_centerimageview)
    ImageView titleCenterimageview;
    @BindView(R.id.title_righttextview)
    TextView titleRighttextview;
    @BindView(R.id.title_rightimageview)
    ImageView titleRightimageview;
    @BindView(R.id.view_line_bottom)
    View viewLineBottom;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;

    @BindView(R.id.refreshLayout_head)
    BezierCircleHeader refreshLayoutHead;
    @BindView(R.id.rv_mall)
    RecyclerView rvMall;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.tv_classify)
    TextView tvClassify;
    @BindView(R.id.tv_sort)
    TextView tvSort;
    @BindView(R.id.iv_classify)
    ImageView ivClassify;
    @BindView(R.id.ll_classify)
    LinearLayout llClassify;
    @BindView(R.id.iv_sort)
    ImageView ivSort;
    @BindView(R.id.ll_sort)
    LinearLayout llSort;
    @BindView(R.id.line)
    View line;


    private int cid;
    private int order = 0; //1：商品金额降序  2：商品金额升序
    private List<String> slists = new ArrayList<String>() {{
        add("默认排序");
        add("价格由高到低");
        add("价格由低到高");
    }}; //商品

    private String name;
    private SharedPreferences preferences = LocalApplication.sharereferences;
    int pageon = 1;//当前页
    int total;  //返回总共多少个
    int totalPage; //返回总共多少页
    private ArrayList<GoodsCategory> categoryList = new ArrayList<>(); //分类


    private List<GoodsList> lists = new ArrayList<GoodsList>(); //商品

    private MallClassifyAdapter mallClassifyAdapter;

    private int type = 1; // 默认排序 2



    /**
     * 商品分类
     *
     * @time 2018/12/26 16:22
     * Created by
     */

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mall_classify;
    }

    @Override
    protected void initParams() {
        Intent intent = getIntent();

        cid = intent.getIntExtra("cid", 0);
        name = intent.getStringExtra("name");

        titleCentertextview.setText(name);

        tvClassify.setText(name);


        getList();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rvMall.setLayoutManager(gridLayoutManager);


        mallClassifyAdapter = new MallClassifyAdapter(rvMall, lists, R.layout.item_mall_classify);
        rvMall.setAdapter(mallClassifyAdapter);


        mallClassifyAdapter.setonItemViewClickListener(new BaseRecyclerViewAdapter.OnItemViewClickListener() {
            @Override
            public void onItemViewClick(BaseViewHolder view, int position) {



                Intent intent3 = new Intent(MallClassifyActivity.this, MallDetailsActivity.class);
                intent3.putExtra("pid", lists.get(position).getId());
                startActivity(intent3);
            }
        });

        refreshLayout.setPrimaryColors(new int[]{0xffF4F4F4, 0xffEE4845});
        //开启自动加载功能（非必须）设置是否监听列表在滚动到底部时触发加载事件
        refreshLayout.setEnableAutoLoadMore(true);
        //下拉刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {

                pageon = 1;
                LogUtils.e("pageon+" + pageon + "totalPage" + totalPage);
                getList();
                //是否有更多数据
                refreshLayout.setNoMoreData(false);
            }
        });
        //加载更多
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {

               /* if (pageon >= totalPage) {

                    // refreshLayout.finishLoadMore();
                    refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件

                } else {

                } */
               //getInitData();
                    getList();
                    refreshLayout.finishLoadMore();

                    LogUtils.e("pageon+" + pageon + "totalPage" + totalPage);

            }
        });
    }

    @OnClick({R.id.ll_classify, R.id.ll_sort, R.id.title_leftimageview})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_leftimageview:
                finish();
                break;
            case R.id.ll_classify:
                type = 1;
                showPopupWindowLogin(type);
                tvClassify.setTextColor(Color.parseColor("#FF623D"));
                tvSort.setTextColor(Color.parseColor("#444444"));
                ivClassify.setImageResource(R.drawable.icon_arrow_up_primary);
                ivSort.setImageResource(R.drawable.icon_arrow_down_black);


                break;
            case R.id.ll_sort:
                type = 2;
                showPopupWindowLogin(type);

                tvSort.setTextColor(Color.parseColor("#FF623D"));
                tvClassify.setTextColor(Color.parseColor("#444444"));
                ivSort.setImageResource(R.drawable.icon_arrow_up_primary);
                ivClassify.setImageResource(R.drawable.icon_arrow_down_black);

                break;
        }

    }


    private PopupWindow popupWindow;
    private View layout;

    public void showPopupWindowLogin(final int type) {
        // 加载布局
        layout = LayoutInflater.from(this).inflate(R.layout.pop_select_mall, null);
        // 找到布局的控件
        // 实例化popupWindow
        popupWindow = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);


        // 控制键盘是否可以获得焦点
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return false;
            }
        });
        WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;

        int[] location = new  int[2] ;
        line.getLocationInWindow(location);

        //LogUtils.e("高度"+"height"+height+"location"+location[1]);

        popupWindow.setHeight(height-location[1]-1);

        // 设置popupWindow弹出窗体的背景
        //setBackgroundAlpha(0.4f);//设置屏幕透明度
        // 监听
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // popupWindow隐藏时恢复屏幕正常透明度
                // setBackgroundAlpha(1.0f);
                tvClassify.setTextColor(Color.parseColor("#444444"));
                tvSort.setTextColor(Color.parseColor("#444444"));
                ivClassify.setImageResource(R.drawable.icon_arrow_down_black);
                ivSort.setImageResource(R.drawable.icon_arrow_down_black);
            }
        });

        // popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);

        popupWindow.showAsDropDown(line);


        ListView listView = (ListView) (layout).findViewById(R.id.lv_cycle);
        final listAdapter listAdapter = new listAdapter(this, categoryList);
        listView.setAdapter(listAdapter);
        if (type == 1) {
            listAdapter.setList(categoryList);
        } else {
            listAdapter.setsList(slists);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (type == 1) {

                    cid = categoryList.get(position).getId();

                    tvClassify.setText(categoryList.get(position).getName());
                    titleCentertextview.setText(categoryList.get(position).getName());

                } else {

                    order = position;

                    tvSort.setText(slists.get(position));
                }
                pageon=1;
                refreshLayout.setNoMoreData(false);
                getList();

                popupWindow.dismiss();
            }
        });

    }

    /**
     * /首页的 下面的商城
     * *
     */
    private void getList() {

        LogUtils.e("cid"+cid+"order"+order+"pageon"+pageon);
        //showWaitDialog("加载中...", true, "");
        OkHttpUtils.post().url(UrlConfig.shopCategory)
                .addParams("order", order + "")
                .addParams("cid", cid + "")
                .addParams("page", pageon + "")
                .addParams("rows", "10")
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        LogUtils.e("商城分类--->" + response);

                        dismissDialog();

                        if (refreshLayout != null) {
                            RefreshState state = refreshLayout.getState();
                            if (state == RefreshState.Refreshing) {
                                refreshLayout.finishRefresh();
                            }
                        }

                        JSONObject obj = JSON.parseObject(response);

                        if (obj.getBoolean(("success"))) {
                            JSONObject map = obj.getJSONObject("map");

                           // JSONObject page = map.getJSONObject("page");
                           // totalPage =  map.getJSONObject("goodslist").getInteger("totalPage");

                            if (map.getJSONArray("category") != null) {

                                JSONArray arr = map.getJSONArray("category");
                                List<GoodsCategory> category = JSON.parseArray(arr.toJSONString(), GoodsCategory.class);
                                //  homepagerRecycleAdapter.setCenterBean(homeHostProduct);
                                categoryList.clear();
                                categoryList.addAll(category);

                                //   homepagerRecycleAdapter.setCategoryBean((ArrayList<GoodsCategory>) categoryList);

                            }

                            if (map.getJSONArray("goodList") != null) {
                                JSONArray arr = map.getJSONArray("goodList");
                                List<GoodsList> goodslist = JSON.parseArray(arr.toJSONString(), GoodsList.class);

                                if (pageon == 1) {

                                    lists.clear();
                                }

                                if(goodslist.size()<10){

                                    refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件

                                }else {
                                    pageon++;
                                }

                                lists.addAll(goodslist);
                                mallClassifyAdapter.notifyDataSetChanged();
                                //  homepagerRecycleAdapter.setCenterBean(homeHostProduct);
                                //  homepagerRecycleAdapter.setRefreshBean(goodslist,true);
                                refreshLayout.finishLoadMore();
                            }





                           /* homepagerRecycleAdapter.setheaderbean(homeInfo);
                            homepagerRecycleAdapter.setCenterBean(homeInfo);

*/
                        /*    List<HomeInfoList.BannerBean> banner = homeInfo.getBanner();

                            if (banner.size() > 0) {
                                lsad.clear();
                                lsad.addAll(banner);
                                adapter.notifyDataSetChanged();
                            }*/


                        } else if ("9999".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("系统异常");
                        } else if ("9998".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("系统异常");
//                            new show_Dialog_IsLogin(MessageCenterActivity.this).show_Is_Login();
                        } else {
                            ToastMaker.showShortToast("系统异常");
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        dismissDialog();
                        ToastMaker.showShortToast("请检查网络");

                        RefreshState state = refreshLayout.getState();
                        if (state == RefreshState.Refreshing) {
                            refreshLayout.finishRefresh();
                        }
                    }
                });


    }


    class listAdapter extends BaseAdapter {

        List<GoodsCategory> list;
        List<String> slist;
        Context context;
        int type = 1;

        public listAdapter(Context context, List<GoodsCategory> list) {
            this.list = list;
            this.context = context;
        }

        public listAdapter(Context context, List<String> list, int Type) {
            this.slist = list;
            this.context = context;
        }

        public void setList(List<GoodsCategory> list) {
            type = 1;
            this.list = list;
        }

        public void setsList(List<String> list) {
            type = 2;
            this.slist = list;
        }


        @Override
        public int getCount() {
            if (type == 1) {
                return list.size();
            } else {
                return slist.size();
            }
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            // View inflate = LayoutInflater.from(context).inflate(R.layout.item_order_details_dialog, parent, false);
            ViewHolder vh = null;
            if (convertView == null) {
                //convertView = inflater.inflate(R.layout.item_project, null);
                convertView = LayoutInflater.from(context).inflate(R.layout.item_mall_classify_pop, parent, false);

                vh = new ViewHolder(convertView);

                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            if (type == 1) {
                GoodsCategory goodsCategory = list.get(position);
                if (cid == goodsCategory.getId()) {
                    vh.tvName.setTextColor(Color.parseColor("#FF623D"));
                    vh.ivStatus.setVisibility(View.VISIBLE);
                } else {
                    vh.tvName.setTextColor(Color.parseColor("#444444"));
                    vh.ivStatus.setVisibility(View.GONE);
                }

                vh.tvName.setText(goodsCategory.getName());

            } else {

                String s = slist.get(position);

                if (order == position) {
                    vh.tvName.setTextColor(Color.parseColor("#FF623D"));
                    vh.ivStatus.setVisibility(View.VISIBLE);
                } else {
                    vh.tvName.setTextColor(Color.parseColor("#444444"));
                    vh.ivStatus.setVisibility(View.GONE);
                }
                vh.tvName.setText(s);

            }

            return convertView;
        }

        class ViewHolder {
            @BindView(R.id.tv_name)
            TextView tvName;
            @BindView(R.id.tv_status)
            ImageView ivStatus;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_mall_classify);
        ButterKnife.bind(this);
    }


    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha 屏幕透明度0.0-1.0 1表示完全不透明
     */
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = this.getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        this.getWindow().setAttributes(lp);
    }

}
