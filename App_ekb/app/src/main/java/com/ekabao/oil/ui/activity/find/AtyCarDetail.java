package com.ekabao.oil.ui.activity.find;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.ekabao.oil.R;
import com.ekabao.oil.bean.AtyCarBreakInfo;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.ui.activity.BaseActivity;
import com.scwang.smartrefresh.header.BezierCircleHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 闪油侠 Oil_syx
 *
 * @time 2019/1/7 17:09
 * Created by lbj on 2019/1/7 17:09.
 */

public class AtyCarDetail extends BaseActivity {
    @BindView(R.id.title_leftimageview)
    ImageView titleLeftimageview;
    @BindView(R.id.title_centertextview)
    TextView titleCentertextview;
    @BindView(R.id.refreshLayout_head)
    BezierCircleHeader refreshLayoutHead;
    @BindView(R.id.rv_car)
    RecyclerView rvCar;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private String carHphm;

    @Override
    protected int getLayoutId() {
        return R.layout.aty_car_detail;
    }

    @Override
    protected void initParams() {
        titleCentertextview.setText( "待处理违章" );
        List<AtyCarBreakInfo.MapBean.CarListBean.DataListBean> carDetailList = (List<AtyCarBreakInfo.MapBean.CarListBean.DataListBean>) getIntent().getSerializableExtra( "carDetail" );
        carHphm = getIntent().getStringExtra( "carHphm" );
        refreshLayout.setPrimaryColors( new int[]{0xffffffff, 0xffEE4845} );
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( LocalApplication.getInstance() );
        rvCar.setLayoutManager( linearLayoutManager );

        carAdapter carAdapter = new carAdapter( this, carDetailList );

        rvCar.setAdapter( carAdapter );
        refreshLayout.setEnableLoadMore( true );

        //下拉刷新
        refreshLayout.setOnRefreshListener( new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh();
            }
        } );

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        // TODO: add setContentView(...) invocation
        ButterKnife.bind( this );
    }

    @OnClick(R.id.title_leftimageview)
    public void onViewClicked() {
        finish();
    }

    class carAdapter extends RecyclerView.Adapter {


        private List<AtyCarBreakInfo.MapBean.CarListBean.DataListBean> carList;
        private Context context;

        public carAdapter(Context context, List<AtyCarBreakInfo.MapBean.CarListBean.DataListBean> list) {
            this.context = context;
            this.carList = list;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_car_details, parent, false );
            ViewHolder myHolder = new ViewHolder( view );

            return myHolder;

        }


        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            ViewHolder myHolder = (ViewHolder) holder;
            myHolder.tvCarName.setText( carHphm );
            //myHolder.tvCarTime.setText( StringCut.getDateTimeToStringheng( carList.get( position ).getDate() ) );
            myHolder.tvCarTime.setText( carList.get( position ).getDate() );
            myHolder.tvCarCity.setText( carList.get( position ).getWzcity() );
            myHolder.tvCarAddress.setText( carList.get( position ).getArea() );
            myHolder.tvDetail.setText( carList.get( position ).getAct() );
            myHolder.tvCarBreak.setText( "-" + carList.get( position ).getFen() );
            myHolder.tvCarMoney.setText( carList.get( position ).getMoney() );


        }

        @Override
        public int getItemCount() {
            return carList.size();
        }


        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.tv_car_name)
            TextView tvCarName;
            @BindView(R.id.tv_car_city)
            TextView tvCarCity;
            @BindView(R.id.tv_car_address)
            TextView tvCarAddress;
            @BindView(R.id.tv_detail)
            TextView tvDetail;
            @BindView(R.id.tv_car_break)
            TextView tvCarBreak;
            @BindView(R.id.tv_car_money)
            TextView tvCarMoney;
            @BindView(R.id.tv_car_time)
            TextView tvCarTime;

            ViewHolder(View itemView) {
                super( itemView );
                ButterKnife.bind( this, itemView );
            }
        }
    }
}
