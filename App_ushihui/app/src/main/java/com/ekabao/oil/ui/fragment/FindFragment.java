package com.ekabao.oil.ui.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ekabao.oil.R;
import com.ekabao.oil.adapter.FindListAdapter;
import com.ekabao.oil.bean.FragOilPriceInfo;
import com.ekabao.oil.bean.MediaBean;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.ui.activity.LoginActivity;
import com.ekabao.oil.ui.activity.MainActivity;
import com.ekabao.oil.ui.activity.NewsActivity;
import com.ekabao.oil.ui.activity.NoticeActivity;
import com.ekabao.oil.ui.activity.WebViewActivity;
import com.ekabao.oil.ui.activity.find.AtyCarBreak;
import com.ekabao.oil.ui.activity.find.AtyLocationOil;
import com.ekabao.oil.ui.activity.find.AtyOilCity;
import com.ekabao.oil.ui.view.ListInScroll;
import com.ekabao.oil.ui.view.MarqueeView;
import com.ekabao.oil.ui.view.ToastMaker;
import com.ekabao.oil.util.GsonUtil;
import com.ekabao.oil.util.LogUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.scwang.smartrefresh.header.BezierCircleHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;
import pub.devrel.easypermissions.EasyPermissions;

import static com.umeng.socialize.utils.ContextUtil.getPackageName;

/**
 * 油实惠  App
 * 主界面 发现
 *
 * @time 2018/8/31 14:59
 * Created by lj on 2018/8/31 14:59.
 */

public class FindFragment extends BaseFragment implements View.OnClickListener {


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
    @BindView(R.id.marqueeView)
    MarqueeView marqueeView;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.rl_notice)
    LinearLayout rlNotice;
    @BindView(R.id.ib_find_gasstation)
    ImageButton ibFindGasstation;
    @BindView(R.id.ib_find_peccancy)
    ImageButton ibFindPeccancy;
    @BindView(R.id.ll_news)
    LinearLayout llNews;
    @BindView(R.id.lv_news)
    ListInScroll lvNews;
    @BindView(R.id.ib_find_safe)
    ImageButton ibFindSafe;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.fillStatusBarView)
    View mStatusBar;
    Unbinder unbinder;

    /**
     * 发现改为-->消息-->
     *
     * @time 2018/7/13 15:52
     * Created by
     */
    private static final String[] CHANNELS = new String[]{"活动消息", "系统消息"};



    private List<String> mDataList = Arrays.asList(CHANNELS);

    private SharedPreferences preferences = LocalApplication.sharereferences;

    private LocationManager locationManager;
    private String locationProvider;
    private double latitude;
    private double longitude;
    private Location location;
    String city = "北京";
    private String locality;

    String[] oilTypeNum = {"92号", "95号", "98号", "0号"};
    String[] oilType = {"汽油", "汽油", "汽油", "柴油"};
    private List<String> oilTypeNumList;
    private List<String> oilTypeList;


    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();

    private static final int SELECTADDRESS = 10001;//选择地址
    //private double longitude;//获取经度信息
    // private double latitude;  //获取纬度信息


    //  private AdapterHomeShop adapterHomeShop;

    List<String> oilPrice = new ArrayList<>();

    private List<MediaBean> mediaList = new ArrayList<MediaBean>();//2 平台公告 3.媒体报道',
    private FindListAdapter adapter;


    public static FindFragment instance() {
        FindFragment view = new FindFragment();
        return view;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_find_1;
    }

    @Override
    protected void initParams() {
        titleCentertextview.setText("发现");
        titleLeftimageview.setVisibility(View.GONE);

        titleRightimageview.setVisibility(View.VISIBLE);

        titleRightimageview.setImageResource(R.drawable.icon_find_news);
        titleRightimageview.setOnClickListener(this);

        ibFindGasstation.setOnClickListener(this);
        ibFindPeccancy.setOnClickListener(this);
        ibFindSafe.setOnClickListener(this);
        llNews.setOnClickListener(this);

        //setLocation();
        // showContacts();

        // requestOilPriceData();
        // setLocation();
        //申请定位的权限
        initPermission();
        // initLocation();


        getNotice(3);
        //下拉刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {

                requestOilPriceData();
                getNotice(3);
            }
        });

        adapter = new FindListAdapter(getActivity(), mediaList);
        lvNews.setAdapter(adapter);

        lvNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getActivity(), WebViewActivity.class)
                        .putExtra("URL", UrlConfig.WEBSITEAN + "?id=" + mediaList.get(position).getArtiId() + "&app=true")
                        .putExtra("TITLE", "媒体报道"));
            }
        });


        refreshLayout.setPrimaryColors(new int[]{0xffF4F4F4, 0xffEE4845});

        //下拉刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {

                initPermission();

                // requestOilPriceData();
                getNotice(3);
                //是否有更多数据
                refreshLayout.setNoMoreData(false);
            }
        });

        marqueeView.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, TextView textView) {
                startActivity(new Intent(getActivity(), AtyOilCity.class)
                        .putExtra("city", city)
                        .putExtra("TITLE", "媒体报道"));
            }
        });

        /*
        //getChildFragmentManager
        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(
                getChildFragmentManager(),
                preparePageInfo(), mDataList);

        viewPager.setAdapter(myFragmentPagerAdapter);
        //viewPager.setAdapter(mExamplePagerAdapter);
        ivRedNew.setVisibility(View.GONE);

        initMagicIndicator7();*/
      /*  if (preferences.getString("uid", "").equalsIgnoreCase("")) {
            // rlSystem.setVisibility(View.GONE);
            rlSystem.setClickable(false);

            tvSystem.setText("暂无相关内容");

        } else {
            rlSystem.setClickable(true);
            // rlSystem.setVisibility(View.VISIBLE);

            getNotice(1);

        }
        getNotice(2);
        getNotice(3);
        getNotice(4);

        rlSystem.setOnClickListener(this);
        rlNews.setOnClickListener(this);
        rlNotice.setOnClickListener(this);
        rlMedia.setOnClickListener(this);*/

        /**
         * 设置view高度为statusbar的高度，并填充statusbar
         */
       // View mStatusBar = view.findViewById(R.id.fillStatusBarView);

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mStatusBar.getLayoutParams();
        lp.width = LinearLayout.LayoutParams.MATCH_PARENT;
        lp.height = getStatusBar();
        mStatusBar.setLayoutParams(lp);

    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public int getStatusBar() {
        /**
         * 获取状态栏高度
         * */
        int statusBarHeight1 = -1;
        //获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight1 = getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight1;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.title_rightimageview:
                //需不需要登录啊

                if (!preferences.getString("uid", "").equalsIgnoreCase("")) {
                    startActivity(new Intent(getActivity(), NewsActivity.class)
                            // .putExtra("activity", 1)
                    );
                } else {
                    startActivity(new Intent(mContext, LoginActivity.class));
                }
                break;
            case R.id.ib_find_gasstation://附近加油

                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    initPermission();
                    //  showMissingPermissionDialog();
                } else {

                    startActivity(new Intent(getActivity(), AtyLocationOil.class));
                }

                break;
            case R.id.ib_find_peccancy://违章查询
                if (!preferences.getString("uid", "").equalsIgnoreCase("")) {
                    startActivity(new Intent(getActivity(), AtyCarBreak.class));

                    //    startActivity(new Intent(getActivity(), NewsActivity.class)
                    // .putExtra("activity", 1));

                } else {
                    startActivity(new Intent(mContext, LoginActivity.class));
                }


                break;
            case R.id.ib_find_safe://
                MainActivity activity = (MainActivity) getActivity();
                activity.switchFragment(4);
//                activity.resetTabState();

                break;
            case R.id.ll_news:// 公司动态
                startActivity(new Intent(getActivity(), NoticeActivity.class)
                        .putExtra("activity", 3));
                break;
        }
    }


    /**
     * 申请权限
     */
    private void initPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION
                , Manifest.permission.READ_PHONE_STATE
                , Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (EasyPermissions.hasPermissions(getActivity(), perms)) {

            //定位
            initLocation();

        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "因为功能需要，需要使用相关权限，请允许",
                    100, perms);

        }
    }


    /**
     * 权限的结果回调函数
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //把申请权限的回调交由EasyPermissions处理
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, new EasyPermissions.PermissionCallbacks() {
            @Override
            public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
                //LogUtil.e("获取成功的权限" + perms);
                initLocation();
            }

            @Override
            public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

            }

            @Override
            public void onRequestPermissionsResult(int i, @NonNull String[] strings, @NonNull int[] ints) {

            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    /**
     * 百度 -->定位获取位置
     */
    private void initLocation() {

        mLocationClient = new LocationClient(getActivity().getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数

        //定位选项
        LocationClientOption option = new LocationClientOption();
        /**
         * coorType - 取值有3个：
         * 返回国测局经纬度坐标系：gcj02
         * 返回百度墨卡托坐标系 ：bd09
         * 返回百度经纬度坐标系 ：bd09ll
         */
        option.setCoorType("bd09ll");
        //设置是否需要地址信息，默认为无地址
        option.setIsNeedAddress(true);
        //设置是否需要返回位置语义化信息，可以在BDLocation.getLocationDescribe()中得到数据，ex:"在天安门附近"， 可以用作地址信息的补充
        option.setIsNeedLocationDescribe(true);
        //设置是否需要返回位置POI信息，可以在BDLocation.getPoiList()中得到数据
        option.setIsNeedLocationPoiList(true);
        /**
         * 设置定位模式
         * Battery_Saving
         * 低功耗模式
         * Device_Sensors
         * 仅设备(Gps)模式
         * Hight_Accuracy
         * 高精度模式
         */
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //设置是否打开gps进行定位
        option.setOpenGps(true);
        //设置扫描间隔，单位是毫秒 当<1000(1s)时，定时定位无效
        option.setScanSpan(5000);

        //设置 LocationClientOption
        mLocationClient.setLocOption(option);

        mLocationClient.start();
    }

    /**
     * 定位的回调接口
     */
    public class MyLocationListener implements BDLocationListener {


        @Override
        public void onReceiveLocation(BDLocation location) {


            //获取经度信息
            longitude = location.getLongitude();
            //获取纬度信息
            latitude = location.getLatitude();

        /*    SharePrefUtil.create(getContext()).saveString("longitude", longitude + "");
            SharePrefUtil.create(getContext()).saveString("latitude", latitude + "");

            //String s = Url.selectNearProject + Md5Help.selectNearProject(longitude + "", latitude + "");
            String s = Url.selectNearProject + Md5Help.selectNearProject("120.1827572344", "30.2232695271");
            //String s = Url.selectNearProject + Md5Help.selectNearProject( "",  "");
            //String s = Url.selectNearProject +  Md5Help.getkey();
            LogUtil.e(s);
            OkHttpEngine.create().post(s, callback);*/


            //获取定位结果
            StringBuffer sb = new StringBuffer(256);

            sb.append("time : ");
            sb.append(location.getTime());    //获取定位时间

            sb.append("\nerror code : ");
            sb.append(location.getLocType());    //获取类型类型

            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());    //获取纬度信息

            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());    //获取经度信息

            sb.append("\nradius : ");
            sb.append(location.getRadius());    //获取定位精准度

            if (location.getLocType() == BDLocation.TypeGpsLocation) {

                // GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());    // 单位：公里每小时

                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());    //获取卫星数

                sb.append("\nheight : ");
                sb.append(location.getAltitude());    //获取海拔高度信息，单位米

                sb.append("\ndirection : ");
                sb.append(location.getDirection());    //获取方向信息，单位度

                sb.append("\naddr : ");
                sb.append(location.getAddrStr());    //获取地址信息

                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {

                // 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());    //获取地址信息

                sb.append("\noperationers : ");
                sb.append(location.getOperators());    //获取运营商信息

                sb.append("\ndescribe : ");
                sb.append("网络定位成功");

            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {

                // 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");

            } else if (location.getLocType() == BDLocation.TypeServerError) {

                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");

            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {

                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");

            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {

                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");

            }

            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());    //位置语义化信息

            List<Poi> list = location.getPoiList();    // POI数据

            LogUtils.e("定位的1" + location.getCity());


            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");

                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }


            mLocationClient.stop();

            // final String cityName = list.get(0).getName(); //定位的城市

            // final String cityName =  location.getCity();
            //.replace("省","")
            final String cityName = location.getProvince().replace("省", "").replace("市", "");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message msg = handler.obtainMessage();
                    msg.what = 0;
                    msg.obj = cityName;
                    handler.sendMessage(msg);
                }
            }).start();


            LogUtils.e("定位的" + cityName);
            LogUtils.e("BaiduLocationApiDem" + sb.toString());
        }

        /**
         * time : 2017-05-23 16:53:52
         * error code : 161
         * latitude : 30.222967
         * lontitude : 120.182702
         * radius : 40.0
         * addr : 中国浙江省杭州市上城区复兴商圈钱江路39号
         * operationers : 0
         * describe : 网络定位成功
         * locationdescribe : 在凤凰城附近
         * poilist size = : 5
         * poi= : 14409975503059231831 凤凰城 0.99
         * poi= : 9986971132987653463 华夏银行(江城支行) 0.99
         * poi= : 1263672630813130751 爱丁堡假日酒店 0.99
         * poi= : 17949150667696242687 凤凰中心 0.99
         * poi= : 17670167789924515839 中国农业银行(复兴支行) 0.99
         */

    }

    Handler handler = new Handler() {  //更新城市
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {

                tvAddress.setText(msg.obj.toString());
                city = msg.obj.toString();
                getOilprice();
                //  tvAddress.setText(city);
            }
        }
    };


    private void requestOilPriceData() {
        //获取定位服务
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        //获取所有可用的位置提供器
        List<String> providers = locationManager.getProviders(true);

        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION
                , Manifest.permission.READ_PHONE_STATE
                , Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else {
            Intent i = new Intent();
            i.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(i);
            return;
        }


        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            city = "浙江";
        } else {
            //有定位权限
            location = locationManager.getLastKnownLocation(locationProvider);
            LogUtils.e("1");
            if (location != null) {

                //不为空,显示地理位置经纬度
                latitude = location.getLatitude();
                longitude = location.getLongitude();

                List<Address> addList = null;
                Geocoder ge = new Geocoder(getActivity());
                try {
                    addList = ge.getFromLocation(latitude, longitude, 1);
                } catch (IOException e) {

                    e.printStackTrace();
                }
                if (addList != null && addList.size() > 0) {
                    for (int i = 0; i < addList.size(); i++) {
                        Address ad = addList.get(i);
                        locality = ad.getAddressLine(0);
                    }
                }

                String locationStr = "维度：" + location.getLatitude()
                        + "经度：" + location.getLongitude();
                LogUtils.e("andly==" + locationStr + "----" + locality.substring(0, 2));
                if (!TextUtils.isEmpty(locality)) {
                    city = locality.substring(0, 2);
                } else {
                    city = "浙江";
                }
                LogUtils.e("1");

            }


        }

        LogUtils.e("城市" + city);

        tvAddress.setText(city);

        getOilprice();

    }


    private void getOilprice() {
        OkHttpUtils
                .post()
                .url(UrlConfig.todayOilPrice)
                .addParam("city", city)
                .addParam("version", UrlConfig.version)
                .addParam("channel", "2")
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        dismissDialog();
                        if (refreshLayout != null) {
                            RefreshState state = refreshLayout.getState();
                            if (state == RefreshState.Refreshing) {
                                refreshLayout.finishRefresh();
                            }
                        }
                        LogUtils.e("今日油价==" + response);
                        FragOilPriceInfo fragOilPriceInfo = GsonUtil.parseJsonToBean(response, FragOilPriceInfo.class);
                        if (fragOilPriceInfo.isSuccess()) {
                            FragOilPriceInfo.MapBean.OilCtyBean oilCty = fragOilPriceInfo.getMap().getOilCty();
                            double h92 = oilCty.getH92();
                            double h95 = oilCty.getH95();
                            double h98 = oilCty.getH98();
                            double h0 = oilCty.getH0();
                            List<String> info = new ArrayList<>();
                            info.add("92#汽油 " + h92 + "元/升");
                            info.add("95#汽油 " + h95 + "元/升");
                            info.add("98#汽油 " + h98 + "元/升");
                            info.add("0#柴油油 " + h0 + "元/升");


                            if (info.size() != 0) {
                                oilPrice.addAll(info);
                            }
                            marqueeView.startWithList(oilPrice);
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        dismissDialog();

                        refreshLayout.finishRefresh();
                    }
                });
    }

    //没有定位权限的弹框
    private void showMissingPermissionDialog() {
        // 加载布局
        View layout = LayoutInflater.from(getActivity()).inflate(R.layout.pop_location, null);
        // 找到布局的控件
        // 实例化popupWindow
        final PopupWindow popupWindow = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        TextView tvCancel = (TextView) (layout).findViewById(R.id.tv_cancel);
        TextView tvLocationGo = (TextView) (layout).findViewById(R.id.tv_location_go);
        TextView tvClose = (TextView) (layout).findViewById(R.id.peron_login_close);

        // 控制键盘是否可以获得焦点
        //popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setFocusable(true);
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //popupWindow.dismiss();
                return false;
            }
        });
        // 设置popupWindow弹出窗体的背景
        setBackgroundAlpha(0.5f);//设置屏幕透明度

//		WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        // 监听
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // popupWindow隐藏时恢复屏幕正常透明度
                setBackgroundAlpha(1.0f);
            }
        });

        tvLocationGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                popupWindow.dismiss();
            }
        });

        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupWindow.dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });


        popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
    }


    // 获取通知列表
    private void getNotice(final int num) {

        //1.系统消息 2 平台公告/3  3.媒体报道'/22,

        String notice = UrlConfig.NOTICE;
        int type = 1;

        switch (num) {
            case 1:
                notice = UrlConfig.GETMESSAGE;
                break;
            case 2:
                notice = UrlConfig.WEB_AN;
                type = 14;
                break;
            case 3:
                notice = UrlConfig.WEB_AN;
                type = 22;
                break;
            case 4:
                notice = UrlConfig.WEB_AN;
                type = 18;
                break;

            default:

                break;
        }
        // showWaitDialog("加载中...", true, "");

        OkHttpUtils.post().url(notice)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("type", type + "")
                .addParams("proId", type + "")
                .addParams("pageOn", "1")
                .addParams("pageSize", "5")
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {

                        //refreshLayout.finishRefresh();

                        // LogUtils.e("--->消息：" + response);

                        dismissDialog();
                        //ptrInvest.refreshComplete();
                        JSONObject obj = JSON.parseObject(response);

                        if (obj.getBoolean(("success"))) {
                            JSONObject map = obj.getJSONObject("map");
                            JSONArray arr = map.getJSONObject("page").getJSONArray("rows");

                            JSONObject page = map.getJSONObject("page");
                            //totalPage = page.getInteger("totalPage");

                            // LogUtils.e("arr.get(0)" + arr.get(0).toString());


                            if (arr.size() <= 0) {


                            } else {
                                // mrows_List.clear();
                                String title = JSON.parseObject(arr.get(0).toString()).getString("title");
                                //LogUtils.e("arr.get(0)" + title);
                                if (title == null) {
                                    title = "暂无相关内容";
                                }

                                List<MediaBean> mrowsList = JSON.parseArray(arr.toJSONString(), MediaBean.class);

                                mediaList.clear();
                                mediaList.addAll(mrowsList);
                                adapter.notifyDataSetChanged();

                             /*   switch (num) {
                                    case 1:
                                      *//*  List<NewsBean> mrows_List = JSON.parseArray(arr.toJSONString(), NewsBean.class);
                                        tvSystem.setText(mrows_List.get(0).getTitle());*//*
                                        tvSystem.setText(title);
                                        Boolean isRead = JSON.parseObject(arr.get(0).toString()).getBoolean("isRead");
                                        //isRead=false;
                                        if (!isRead){
                                            ivRedNew.setVisibility(View.VISIBLE);
                                        }else {
                                            ivRedNew.setVisibility(View.GONE);
                                        }

                                       *//* MainActivity activity = (MainActivity) getActivity();
                                        activity.setMainRedCircle(isRead);*//*

                                        break;
                                    case 2:
                                        //  List<MediaBean> List1 = JSON.parseArray(arr.toJSONString(), MediaBean.class);
                                        // tvNotice.setText(List1.get(0).getTitle());
                                        tvNotice.setText(title);
                                        break;
                                    case 3:
                                        // List<MediaBean> List2 = JSON.parseArray(arr.toJSONString(), MediaBean.class);
                                        //  tvMedia.setText(List2.get(0).getTitle());
                                        tvMedia.setText(title);
                                        break;
                                    case 4:
                                        //List<MediaBean> List3 = JSON.parseArray(arr.toJSONString(), MediaBean.class);
                                        // tvNews.setText(List3.get(0).getTitle());

                                        tvNews.setText(title);
                                        break;
                                    default:
                                        break;
                                }*/


                            }

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
                    }
                });
    }


    private ArrayList<BaseFragment> preparePageInfo() {
        ArrayList<BaseFragment> list = new ArrayList<>();
        // 1.系统消息 2 活动消息 3.交易消息',
        list.add(NewsFragment.newInstance(2)); //2 活动消息
        // list.add(NewsFragment.newInstance(3));
        list.add(NewsFragment.newInstance(1)); //1.系统消息
        return list;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        //unbinder = ButterKnife.bind(this, rootView);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //  unbinder.unbind();
        unbinder.unbind();
    }


    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha 屏幕透明度0.0-1.0 1表示完全不透明
     */
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        getActivity().getWindow().setAttributes(lp);
    }
}
