package com.ekabao.oil.ui.activity.find;

import android.Manifest;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.ekabao.oil.R;
import com.ekabao.oil.adapter.AdapterLocation;
import com.ekabao.oil.bean.AtyLocationInfo;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.OkHttpEngine;
import com.ekabao.oil.http.PostParams;
import com.ekabao.oil.http.SimpleHttpCallback;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.ui.activity.BaseActivity;
import com.ekabao.oil.ui.activity.OilCardPackageActivity;
import com.ekabao.oil.ui.view.popupwindow.HorizontalPosition;
import com.ekabao.oil.ui.view.popupwindow.SmartPopupWindow;
import com.ekabao.oil.ui.view.popupwindow.VerticalPosition;
import com.ekabao.oil.util.GsonUtil;
import com.ekabao.oil.util.LogUtils;
import com.ekabao.oil.util.MapUtil;
import com.ekabao.oil.util.ToastUtil;
import com.ekabao.oil.util.UtilList;
import com.scwang.smartrefresh.header.BezierCircleHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 闪油侠 Oil_syx
 *
 * @time 2019/1/2 10:30
 * Created by lbj on 2019/1/2 10:30.
 */

public class AtyLocationOil extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.title_leftimageview)
    ImageView titleLeftimageview;
    @BindView(R.id.title_centertextview)
    TextView titleCentertextview;
    @BindView(R.id.title_rightimageview)
    ImageView titleRightimageview;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.tv_buy_oil)
    TextView tvBuyOil;
    @BindView(R.id.tv_oil_choose)
    TextView tvOilChoose;
    @BindView(R.id.tv_disance)
    TextView tvDisance;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.view_distance)
    View viewDistance;
    @BindView(R.id.view_price)
    View viewPrice;
    @BindView(R.id.rv_location)
    RecyclerView rvLocation;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rl_oil)
    RelativeLayout rlOil;


    private LocationManager locationManager;
    private String locationProvider;
    private double latitude;
    private double longitude;
    String addString = null;

    List<AtyLocationInfo.DataListBean> locationList = new ArrayList<>();
    private AdapterLocation adapterLocation;
    private String oilType = "E90";
    private String orderType = "1";

    private View mPopupContentView;
    private BDLocation mlocation;

    private Double lat;
    private Double lon;
    private String oilName;


    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    private PopupWindow popupWindow;
    private boolean  isShowing ;//弹窗是否显示
    @Override
    protected int getLayoutId() {
        return R.layout.aty_location_oil;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void initParams() {
        titleCentertextview.setVisibility(View.GONE);
        titleRightimageview.setVisibility(View.VISIBLE);
        titleRightimageview.setImageResource(R.drawable.icon_refresh);

        mPopupContentView = getLayoutInflater().inflate(R.layout.pop_oil_type, null);
        LinearLayout ll93 = (LinearLayout) (mPopupContentView).findViewById(R.id.ll_93);
        LinearLayout ll90 = (LinearLayout) (mPopupContentView).findViewById(R.id.ll_90);
        LinearLayout ll97 = (LinearLayout) (mPopupContentView).findViewById(R.id.ll_97);
        LinearLayout ll0 = (LinearLayout) (mPopupContentView).findViewById(R.id.ll_0);
        ll93.setOnClickListener(this);
        ll90.setOnClickListener(this);
        ll97.setOnClickListener(this);
        ll0.setOnClickListener(this);

        tvOilChoose.setText("93#汽油");
        oilType = "E93";


        initPermission();




      /*  //获取定位服务
        locationManager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        //获取所有可用的位置提供器
        List<String> providers = locationManager.getProviders( true );

        if (providers.contains( LocationManager.GPS_PROVIDER )) {
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains( LocationManager.NETWORK_PROVIDER )) {
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else {
            Intent i = new Intent();
            i.setAction( Settings.ACTION_LOCATION_SOURCE_SETTINGS );
            startActivity( i );
            return;
        }

        if (ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            tvLocation.setText( "定位服务未开启" );
            rvLocation.setVisibility( View.GONE );
            showMissingPermissionDialog();
            return;
        }
        mlocation = locationManager.getLastKnownLocation( locationProvider );

        locationManager.requestLocationUpdates( locationProvider, 1000, 1, locationListener );

*/


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LocalApplication.getInstance());

        rvLocation.setLayoutManager(linearLayoutManager);
        adapterLocation = new AdapterLocation(locationList, this);
        rvLocation.setAdapter(adapterLocation);

        refreshLayout.setPrimaryColors(new int[]{0xffffffff, 0xffEE4845});

        //下拉刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {

                if (mlocation != null) {
                    initPermission();

                    //不为空,显示地理位置经纬度
                   // showLocation(mlocation);
                }

            }
        });

        adapterLocation.setOnItemClickLitener(new AdapterLocation.OnItemClickLitener() {


            @Override
            public void onItemClick(View view, int position) {

                lat = locationList.get(position).getLat();
                lon = locationList.get(position).getLon();
                oilName = locationList.get(position).getName();
                showLocationGo();

               /* if (!TextUtils.isEmpty(latitude + "") && !TextUtils.isEmpty(mlocation + "")) {
                    for (int i = 0; i < locationList.size(); i++) {
                        lat = locationList.get(position).getLat();
                        lon = locationList.get(position).getLon();
                        oilName = locationList.get(position).getName();
                    }
                  *//*  if (popupWindow != null) {

                        LogUtils.e(popupWindow.isShowing()+"popupWindow"+isShowing);

                        if (isShowing) {
                            LogUtils.e("popupWindowisShowing"+popupWindow.isShowing());
                            popupWindow.dismiss();
                            return;
                        }
                    }*//*

                    showLocationGo();
                }*/
            }
        });

        rvLocation.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int scollYDistance = getScollYDistance(rvLocation);

                if (scollYDistance > 1) {
                    titleCentertextview.setVisibility(View.VISIBLE);

                    tvLocation.setVisibility(View.GONE);
                    tvBuyOil.setVisibility(View.GONE);

                    if (!TextUtils.isEmpty(addString)) {
                        titleCentertextview.setText(addString);
                    } else {
                        titleCentertextview.setText("正在定位");
                    }
                } else {
                    titleCentertextview.setVisibility(View.GONE);
                    tvLocation.setVisibility(View.VISIBLE);
                    tvBuyOil.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    private void showLocation(BDLocation location) {


//        //权限判断
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            tvLocation.setText("定位服务未开启");
//            rvLocation.setVisibility(View.GONE);
//
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
//                    100);
//        } else {
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        List<Address> addList = null;
        Geocoder ge = new Geocoder(AtyLocationOil.this);
        try {
            addList = ge.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {

            e.printStackTrace();
        }
        if (addList != null && addList.size() > 0) {
            for (int i = 0; i < addList.size(); i++) {
                Address ad = addList.get(i);

                addString = ad.getAddressLine(0);
            }
        }
        if (!TextUtils.isEmpty(latitude + "") && !TextUtils.isEmpty(location + "")) {
            tvLocation.setText(addString);

            requestLocationData();
        } else {
            tvLocation.setText("定位服务未开启");
            rvLocation.setVisibility(View.GONE);
        }


        String locationStr = "维度：" + location.getLatitude()
                + "经度：" + location.getLongitude();
        LogUtils.e("andly==" + locationStr + "----" + addString);
    }

    /**
     * //定位
     */
    private void requestLocationData() {
        showWaitDialog("加载中...", true, "");
        PostParams params = new PostParams();
        HashMap<String, Object> map = params.getParams();
        map.put("lon", longitude + "");
        map.put("lat", latitude + "");
        map.put("order", orderType);
        map.put("type", oilType);
        map.put("version", UrlConfig.version);
        map.put("channel", "2");

        OkHttpEngine.create().setHeaders().post(UrlConfig.oilLocation, params, new SimpleHttpCallback() {
            @Override
            public void onLogicSuccess(String data) {
                LogUtils.e("LocationData==" + data);
                refreshLayout.finishRefresh();
                dismissDialog();
                AtyLocationInfo atyLocationInfo = GsonUtil.parseJsonToBean(data, AtyLocationInfo.class);
                List<AtyLocationInfo.DataListBean> dataList = atyLocationInfo.getDataList();


                if (!UtilList.isEmpty(dataList)) {
                    refreshLayout.finishRefresh();
                    locationList.clear();
                    locationList.addAll(dataList);
                    adapterLocation.notifyDataSetChanged();
                }

            }

            @Override
            public void onLogicError(int code, String msg) {
                ToastUtil.showToast("请检查网络");
                dismissDialog();
                refreshLayout.finishRefresh();
            }

            @Override
            public void onError(IOException e) {
                dismissDialog();
                ToastUtil.showToast("请检查网络");
                refreshLayout.finishRefresh();
            }
        });
    }

    @OnClick({R.id.title_leftimageview, R.id.title_rightimageview, R.id.tv_buy_oil, R.id.tv_oil_choose, R.id.tv_disance, R.id.tv_price})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_leftimageview:
                finish();
                break;
            case R.id.title_rightimageview:
                if (mlocation != null) {

                    initPermission();

                    //不为空,显示地理位置经纬度
                   // showLocation(mlocation);
                }
                break;
            case R.id.tv_buy_oil: //立即充油费

                startActivity(new Intent(this, OilCardPackageActivity.class));

                break;
            case R.id.tv_oil_choose:
                mPopupContentView.setVisibility(View.VISIBLE);
                SmartPopupWindow.Builder
                        .build(AtyLocationOil.this, mPopupContentView)
                        .createPopupWindow()
                        .showAtAnchorView(rlOil, VerticalPosition.BELOW, HorizontalPosition.ALIGN_LEFT);
                break;
            case R.id.tv_disance:
                viewPrice.setVisibility(View.GONE);
                viewDistance.setVisibility(View.VISIBLE);
                tvDisance.setTextColor(this.getResources().getColor(R.color.view_black));
                tvPrice.setTextColor(this.getResources().getColor(R.color.text_bb));
                orderType = "1";
                requestLocationData();
                break;
            case R.id.tv_price:
                viewPrice.setVisibility(View.VISIBLE);
                viewDistance.setVisibility(View.GONE);
                tvPrice.setTextColor(this.getResources().getColor(R.color.view_black));
                tvDisance.setTextColor(this.getResources().getColor(R.color.text_bb));
                orderType = "2";
                requestLocationData();
                break;
            default:
                break;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_90:
                mPopupContentView.setVisibility(View.GONE);
                tvOilChoose.setText("90#汽油");
                oilType = "E90";
                adapterLocation.oilType = "E90";
                requestLocationData();
                break;
            case R.id.ll_93:
                mPopupContentView.setVisibility(View.GONE);
                tvOilChoose.setText("93#汽油");
                oilType = "E93";
                adapterLocation.oilType = "E93";
                requestLocationData();
                break;
            case R.id.ll_97:
                mPopupContentView.setVisibility(View.GONE);
                tvOilChoose.setText("97#汽油");
                oilType = "E97";
                adapterLocation.oilType = "E97";
                requestLocationData();
                break;
            case R.id.ll_0:
                mPopupContentView.setVisibility(View.GONE);
                tvOilChoose.setText("0#柴油");
                oilType = "E0";
                adapterLocation.oilType = "E0";
                requestLocationData();
                break;

            default:
                break;
        }
    }


    private void showLocationGo() {
        View view = LayoutInflater.from(this).inflate(R.layout.pop_location_go, null);
        TextView tvBaiDu = (TextView) view.findViewById(R.id.tv_baidu);
        TextView tvGaoDe = (TextView) view.findViewById(R.id.tv_gaode);
        TextView tvTengXun = (TextView) view.findViewById(R.id.tv_tx);
        TextView btnCancel = (TextView) view.findViewById(R.id.btn_cancel);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);




        popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
        popupWindow.setOutsideTouchable(true);
        View parent = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        //popupWindow在弹窗的时候背景半透明
        final WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.5f;
        getWindow().setAttributes(params);

      //  isShowing=popupWindow.isShowing();

        //LogUtils.e("popupWindowisShowing"+popupWindow.isShowing());
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

             //   isShowing=popupWindow.isShowing();

                params.alpha = 1.0f;
                getWindow().setAttributes(params);
            }

        });

        View back = view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });


        tvBaiDu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MapUtil.isAvilible(AtyLocationOil.this, "com.baidu.BaiduMap")) {//传入指定应用包名

                    Intent intent = null;
                    try {
                        intent = Intent.getIntent("intent://map/direction?origin=我的位置&destination=" + oilName + "&mode=driving&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);


                } else {//未安装
                    //market为路径，id为包名
                    //显示手机上所有的market商店
                    ToastUtil.showToast("您尚未安装百度地图");
                    Uri uri = Uri.parse("market://details?id=com.baidu.BaiduMap");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }
                popupWindow.dismiss();
            }
        });

        tvGaoDe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MapUtil.isAvilible(AtyLocationOil.this, "com.autonavi.minimap")) {//传入指定应用包名

                    Intent intent = null;
                    try {
                        intent = Intent.getIntent("androidamap://route?sourceApplication=softname&sname=我的位置&dlat=" + lat + "&dlon=" + lon + "&dname=" + oilName + "&dev=0&m=0&t=1");
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);

                    // MapUtil.goToNaviActivity(AtyLocationOil.this, "test", null, lat + "", lon + "", "1", "2");

                } else {//未安装
                    //market为路径，id为包名
                    //显示手机上所有的market商店
                    ToastUtil.showToast("您尚未安装百度地图");
                    Uri uri = Uri.parse("market://details?id=com.autonavi.minimap");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }
                popupWindow.dismiss();
            }
        });
        tvTengXun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MapUtil.isAvilible(AtyLocationOil.this, "com.tencent.map")) {//传入指定应用包名

                    Intent intent = null;
                    try {
                        intent = Intent.getIntent("qqmap://map/routeplan?type=drive&from=" + "我的位置&fromcoord=" + latitude + "," + longitude + "&to=" + oilName + "&tocoord=" + lat + "," + lon + "&referer=OB4BZ-D4W3U-B7VVO-4PJWW-6TKDJ-WPB77");
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);

                    // MapUtil.goToNaviActivity(AtyLocationOil.this, "test", null, lat + "", lon + "", "1", "2");

                } else {//未安装
                    //market为路径，id为包名
                    //显示手机上所有的market商店
                    ToastUtil.showToast("您尚未安装腾讯地图");
                    Uri uri = Uri.parse("market://details?id=com.tencent.map");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }
                popupWindow.dismiss();
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);


    }


    //Android6.0申请权限的回调方法
  /*  @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );
        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case 100:
                if (grantResults.length > 0) {//grantResults 数组中存放的是授权结果
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {//同意授权
                        //授权后做一些你想做的事情，即原来不需要动态授权时做的操作
                        // showLocation(mlocation);

                    } else {//用户拒绝授权
                        //可以简单提示用户
                        Toast.makeText( this, "没有定位权限", Toast.LENGTH_SHORT ).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                            //调用 shouldShowRequestPermissionRationale() 方法

                            boolean shouldShow = shouldShowRequestPermissionRationale( permissions[0] );

                            if (!shouldShow) {// shouldShow = false

                                //需要弹出自定义对话框，引导用户去应用的设置界面手动开启权限
                                showMissingPermissionDialog();

                            }
                        }

                    }
                }
                break;
            default:
                break;
        }
    }*/


    /**
     * 申请权限
     */
    private void initPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION
                , Manifest.permission.READ_PHONE_STATE
                , Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (EasyPermissions.hasPermissions(this, perms)) {

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

    /**
     * 百度 -->定位获取位置
     */
    private void initLocation() {

        mLocationClient = new LocationClient(this.getApplicationContext());
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
            double longitude = location.getLongitude();
            //获取纬度信息
            double latitude = location.getLatitude();


            mlocation = location;

            LogUtils.e("定位的回调接口" + longitude + "/" + latitude);

            // mlocation.setLongitude(longitude);
            // mlocation.setLatitude(latitude);

            if (location != null) {
                //不为空,显示地理位置经纬度
                showLocation(mlocation);
            }
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

            final String cityName = list.get(0).getName(); //定位的城市

            addString=cityName;

            // final String cityName =  mlocation.getCity();
            //.replace("省","")
            // final String cityName =   mlocation.getProvince().replace("省","").replace("市","");
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

                tvLocation.setText(msg.obj.toString());


                //  tvAddress.setText(city);
            }
        }
    };



















    /*private void showMissingPermissionDialog() {
        // 加载布局
        View layout = LayoutInflater.from( this ).inflate( R.layout.pop_location, null );
        // 找到布局的控件
        // 实例化popupWindow
        final PopupWindow popupWindow = new PopupWindow( layout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true );
        TextView tvCancel = (TextView) (layout).findViewById( R.id.tv_cancel );
        TextView tvLocationGo = (TextView) (layout).findViewById( R.id.tv_location_go );
        TextView tvClose = (TextView) (layout).findViewById( R.id.peron_login_close );

        // 控制键盘是否可以获得焦点
        //popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable( true );
        popupWindow.setBackgroundDrawable( new ColorDrawable( 0x00000000 ) );
        popupWindow.setFocusable( true );
        layout.setOnTouchListener( new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //popupWindow.dismiss();
                return false;
            }
        } );
        // 设置popupWindow弹出窗体的背景
        setBackgroundAlpha( 0.5f );//设置屏幕透明度

//		WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        // 监听
        popupWindow.setOnDismissListener( new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // popupWindow隐藏时恢复屏幕正常透明度
                setBackgroundAlpha( 1.0f );
            }
        } );

        tvLocationGo.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent( Settings.ACTION_APPLICATION_DETAILS_SETTINGS );
                intent.setData( Uri.parse( "package:" + getPackageName() ) );
                startActivity( intent );
                popupWindow.dismiss();
            }
        } );

        tvClose.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //startActivity( new Intent( AtyLocationOil.this, AtySlowOil.class ) );
                popupWindow.dismiss();
            }
        } );
        tvCancel.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        } );


        popupWindow.showAtLocation( layout, Gravity.CENTER, 0, 0 );
    }*/

    LocationListener locationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onLocationChanged(Location location) {
            // showLocation( location );
        }
    };


    public int getScollYDistance(RecyclerView rvLocation) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) rvLocation.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        int itemHeight = firstVisiableChildView.getHeight();
        return (position) * itemHeight - firstVisiableChildView.getTop();
    }

    @SuppressWarnings("ResourceType")
    private static int makeDropDownMeasureSpec(int measureSpec) {
        int mode;
        if (measureSpec == ViewGroup.LayoutParams.WRAP_CONTENT) {
            mode = View.MeasureSpec.UNSPECIFIED;
        } else {
            mode = View.MeasureSpec.EXACTLY;
        }
        return View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(measureSpec), mode);
    }

    /**
     * 这里一定要对LocationManager进行重新设置监听
     * mgr获取provider的过程不是一次就能成功的
     * mgr.getLastKnownLocation很可能返回null
     * 如果只在initProvider()中注册一次监听则基本很难成功
     */
    @Override
    protected void onResume() {
        super.onResume();
      /*  if (ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            tvLocation.setText( "定位服务未开启" );
            rvLocation.setVisibility( View.GONE );
            showMissingPermissionDialog();
            return;
        }
        locationManager.requestLocationUpdates( locationProvider, 1000, 1, locationListener );

    */
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        /*if (locationManager != null) {
            //移除监听器
            locationManager.removeUpdates( locationListener );
        }*/
    }


}
