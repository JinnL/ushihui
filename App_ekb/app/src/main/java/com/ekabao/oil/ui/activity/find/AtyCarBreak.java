package com.ekabao.oil.ui.activity.find;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ekabao.oil.ui.activity.LoginActivity;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.ekabao.oil.R;
import com.ekabao.oil.adapter.AdapterCarBreak;
import com.ekabao.oil.bean.AtyCarBreakInfo;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.ui.activity.BaseActivity;
import com.ekabao.oil.ui.view.DialogMaker;
import com.ekabao.oil.ui.view.popupwindow.HorizontalPosition;
import com.ekabao.oil.ui.view.popupwindow.SmartPopupWindow;
import com.ekabao.oil.ui.view.popupwindow.VerticalPosition;
import com.ekabao.oil.util.GsonUtil;
import com.ekabao.oil.util.LogUtils;
import com.ekabao.oil.util.ToastUtil;
import com.ekabao.oil.util.UtilList;
import com.scwang.smartrefresh.header.BezierCircleHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 闪油侠 Oil_syx
 *
 * @time 2019/1/4 11:19
 * Created by lbj on 2019/1/4 11:19.
 */

public class AtyCarBreak extends BaseActivity implements AdapterCarBreak.CarManageListener {
    @BindView(R.id.title_leftimageview)
    ImageView titleLeftimageview;
    @BindView(R.id.title_centertextview)
    TextView titleCentertextview;
    @BindView(R.id.rv_car)
    RecyclerView rvCar;
    @BindView(R.id.tv_add_car)
    TextView tvAddCar;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.refreshLayout_head)
    BezierCircleHeader refreshLayoutHead;
    @BindView(R.id.view_line_bottom)
    View viewLineBottom;

    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
    private List<AtyCarBreakInfo.MapBean.CarListBean> carList;
    private List<AtyCarBreakInfo.MapBean.CarListBean> carBreakList = new ArrayList<>();
    private AdapterCarBreak adapterCarBreak;
    private View mPopupContentView;
    private LinearLayout llCarEdit;
    private LinearLayout llCarDelete;


    @Override
    protected int getLayoutId() {
        return R.layout.aty_car_break;
    }

    @Override
    protected void initParams() {
        viewLineBottom.setVisibility( View.GONE );
        titleCentertextview.setText( "违章查询" );
        mPopupContentView = getLayoutInflater().inflate( R.layout.pop_car_edit, null );
        llCarEdit = (LinearLayout) (mPopupContentView).findViewById( R.id.ll_car_edit );
        llCarDelete = (LinearLayout) (mPopupContentView).findViewById( R.id.ll_car_delete );

        refreshLayout.setPrimaryColors( new int[]{0xffffffff, 0xffEE4845} );
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( LocalApplication.getInstance() );
        rvCar.setLayoutManager( linearLayoutManager );
        refreshLayout.setEnableLoadMore( true );
        //开启自动加载功能（非必须）设置是否监听列表在滚动到底部时触发加载事件
        refreshLayout.setEnableAutoLoadMore( true );
        //下拉刷新
        refreshLayout.setOnRefreshListener( new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh();
                refreshLayout.setNoMoreData( false );//复原状态
                requestCarData();
            }
        } );

        requestCarData();

//        lvCar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                setResult(Activity.RESULT_OK, new Intent().putExtra("position", position));
////                finish();
//            }
//        });

    }

    private void requestCarData() {
        showWaitDialog( "加载中...", true, "" );
        //LogUtils.e(preferences.getString( "uid", "" ));

        OkHttpUtils.post()
                .url( UrlConfig.carBreak )
                .addParams( "uid", preferences.getString( "uid", "" ) )
                .addParams( "version", UrlConfig.version )
                .addParams( "channel", "2" )
                .build().execute( new StringCallback() {


            @Override
            public void onResponse(String response) {
                dismissDialog();
                LogUtils.i( "违章车辆==：" + response );
                JSONObject obj = JSON.parseObject( response );
                if (obj.getBoolean( "success" )) {
                    AtyCarBreakInfo atyCarBreakInfo = GsonUtil.parseJsonToBean( response, AtyCarBreakInfo.class );
                    carList = atyCarBreakInfo.getMap().getCarList();
                    if (!UtilList.isEmpty( carList )) {
                        carBreakList.clear();
                        carBreakList.addAll( carList );
                        adapterCarBreak = new AdapterCarBreak( AtyCarBreak.this, carBreakList );
                        adapterCarBreak.setOnCarManageListener( AtyCarBreak.this );
                        rvCar.setAdapter( adapterCarBreak );
                    } else {

                    }
                }

            }

            @Override
            public void onError(Call call, Exception e) {
                dismissDialog();
                ToastUtil.showToast( "请检查网络" );
            }
        } );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        // TODO: add setContentView(...) invocation
        ButterKnife.bind( this );
    }

    @OnClick({R.id.title_leftimageview, R.id.tv_add_car})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_leftimageview:
                finish();
                break;
            case R.id.tv_add_car:
                if (!preferences.getString("uid", "").equalsIgnoreCase("")) {

                    startActivity( new Intent( this, AtyAddCar.class ).putExtra( "edit", false ) );

                } else {
                    startActivity(new Intent(this, LoginActivity.class));
                }


                break;
            default:
                break;
        }
    }

    @Override
    public void onSelected(int position) {
        List<AtyCarBreakInfo.MapBean.CarListBean.DataListBean> dataList = carBreakList.get( position ).getDataList();

        if (!UtilList.isEmpty( dataList )) {
            startActivity( new Intent( this, AtyCarDetail.class )
                    .putExtra( "carHphm", carBreakList.get( position ).getHphm() )
                    .putExtra( "carDetail", (Serializable) dataList ) );
        } else {

            ToastUtil.showToast( "暂无数据" );
        }


    }

    @Override
    public void onEdit(final ImageView ivCarEdit, final int position) {
        mPopupContentView.setVisibility( View.VISIBLE );

        ivCarEdit.setImageResource( R.drawable.icon_car_up );
        SmartPopupWindow.Builder
                .build( AtyCarBreak.this, mPopupContentView )
                .createPopupWindow()
                .showAtAnchorView( ivCarEdit, VerticalPosition.BELOW, HorizontalPosition.ALIGN_LEFT );

        llCarDelete.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupContentView.setVisibility( View.GONE );
                ivCarEdit.setImageResource( R.drawable.icon_car_down );
                DialogMaker.showRedSureDialog( AtyCarBreak.this, "删除", "是否确认删除？", "取消", "确定", new DialogMaker.DialogCallBack() {
                    @Override
                    public void onButtonClicked(Dialog dialog, int position, Object tag) {
                        deleteCar( position );
                        adapterCarBreak.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelDialog(Dialog dialog, Object tag) {

                    }
                }, "" );

            }
        } );

        llCarEdit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupContentView.setVisibility( View.GONE );
                ivCarEdit.setImageResource( R.drawable.icon_car_down );
                AtyCarBreakInfo.MapBean.CarListBean carListBean = carBreakList.get( position );
                startActivity( new Intent( AtyCarBreak.this, AtyAddCar.class ).
                        putExtra( "carHphm", carListBean.getHphm() ).
                        putExtra( "carId", carListBean.getId() ).
                        putExtra( "carHpzl", carListBean.getHpzl() ).
                        putExtra( "carClassno", carListBean.getClassno() ).
                        putExtra( "carEngineno", carListBean.getEngineno() )
                        .putExtra( "edit", true ) );
            }
        } );
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestCarData();
    }


    /*删除车辆*/
    private void deleteCar(final int position) {
        showWaitDialog( "加载中...", true, "" );
        OkHttpUtils.post()
                .url( UrlConfig.carDelete )
                .addParams( "uid", preferences.getString( "uid", "" ) )
                .addParams( "id", carBreakList.get( position ).getId() + "" )
                .addParams( "version", UrlConfig.version )
                .addParams( "channel", "2" )
                .build().execute( new StringCallback() {
            @Override
            public void onResponse(String response) {
                dismissDialog();
                JSONObject obj = JSON.parseObject( response );
                if (obj.getBoolean( "success" )) {
                    ToastUtil.showToast( "删除车辆成功" );
                    requestCarData();
                } else if ("9998".equals( obj.getString( "errorCode" ) )) {
                    ToastUtil.showToast( "系统异常" );
                } else if ("9999".equals( obj.getString( "errorCode" ) )) {
                    ToastUtil.showToast( "系统异常" );
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                dismissDialog();
                ToastUtil.showToast( "请检查网络" );
            }
        } );
    }
}
