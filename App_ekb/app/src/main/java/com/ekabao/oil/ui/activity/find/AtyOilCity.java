package com.ekabao.oil.ui.activity.find;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ekabao.oil.R;
import com.ekabao.oil.bean.FragOilPriceInfo;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.ui.activity.BaseActivity;
import com.ekabao.oil.ui.view.CityPick.CityPicker;
import com.ekabao.oil.util.GsonUtil;
import com.ekabao.oil.util.LogUtils;
import com.ekabao.oil.util.StringCut;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 闪油侠 Oil_syx
 *
 * @time 2019/1/8 16:09
 * Created by lbj on 2019/1/8 16:09.
 */

public class AtyOilCity extends BaseActivity {
    @BindView(R.id.title_leftimageview)
    ImageView titleLeftimageview;
    @BindView(R.id.title_centertextview)
    TextView titleCentertextview;
    @BindView(R.id.view_line_bottom)
    View viewLineBottom;
    @BindView(R.id.ll_oil_city)
    LinearLayout llOilCity;
    @BindView(R.id.tv_92_price)
    TextView tv92Price;
    @BindView(R.id.tv_95_price)
    TextView tv95Price;
    @BindView(R.id.tv_98_price)
    TextView tv98Price;
    @BindView(R.id.tv_0_price)
    TextView tv0Price;
    @BindView(R.id.tv_oil_time)
    TextView tvOilTime;
    @BindView(R.id.tv_oil_city)
    TextView tvOilCity;

    private String cityName="浙江";

    private CityPicker mCityPicker;

    @Override
    protected int getLayoutId() {
        return R.layout.aty_oil_city;
    }

    @Override
    protected void initParams() {
        viewLineBottom.setVisibility( View.GONE );
        titleCentertextview.setText( "今日油价" );
        cityName = getIntent().getStringExtra( "city" );
        tvOilCity.setText( cityName );

        requestOilPriceData();

    }

    private void requestOilPriceData() {
        showWaitDialog( "加载中...", true, "" );

        LogUtils.e( cityName+"city==" + cityName.replace("省","").replace("市","")  );

        OkHttpUtils
                .post()
                .url( UrlConfig.todayOilPrice )
                .addParam( "city", cityName.replace("省","").replace("市","") )
                .addParam( "version", UrlConfig.version )
                .addParam( "channel", "2" )
                .build()
                .execute( new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        dismissDialog();
                        LogUtils.e( "今日油价666==" + response );
                        FragOilPriceInfo fragOilPriceInfo = GsonUtil.parseJsonToBean( response, FragOilPriceInfo.class );
                        if (fragOilPriceInfo.isSuccess()) {
                            FragOilPriceInfo.MapBean.OilCtyBean oilCty = fragOilPriceInfo.getMap().getOilCty();
                            double h92 = oilCty.getH92();
                            double h95 = oilCty.getH95();
                            double h98 = oilCty.getH98();
                            double h0 = oilCty.getH0();
                            long createtime = oilCty.getCreatetime();


                            if (!TextUtils.isEmpty( h92 + "" )) {
                                tv92Price.setText( h92 + "" );
                            }
                            if (!TextUtils.isEmpty( h95 + "" )) {
                                tv95Price.setText( h95 + "" );
                            }
                            if (!TextUtils.isEmpty( h98 + "" )) {
                                tv98Price.setText( h98 + "" );
                            }
                            if (!TextUtils.isEmpty( h0 + "" )) {
                                tv0Price.setText( h0 + "" );
                            }
                            if (!TextUtils.isEmpty( createtime + "" )) {
                                tvOilTime.setText( "更新时间：" + StringCut.getDateTimeToString( createtime ) );
                            }


                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        dismissDialog();

                    }
                } );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        // TODO: add setContentView(...) invocation
        ButterKnife.bind( this );
    }

    @OnClick({R.id.title_leftimageview, R.id.ll_oil_city})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_leftimageview:
                finish();
                break;
            case R.id.ll_oil_city:
                /**
                 * 地址选择器
                 * */
                InputMethodManager imm = (InputMethodManager) getSystemService( Context.INPUT_METHOD_SERVICE );
                imm.hideSoftInputFromWindow( llOilCity.getWindowToken(), 0 );

                if (mCityPicker == null) {
                    // TODO: 2019/1/12 "fromCity", 
                    mCityPicker = new CityPicker( this,  findViewById( R.id.ll_oil_city ) )
                            .setOnCitySelectListener( new CityPicker.OnCitySelectListener() {

                                @Override
                                public void onCitySelect(String province, int pid, String city, int cityid, String county, int cid) {
                                    cityName = province;
                                    if (!TextUtils.isEmpty( cityName )) {
                                        tvOilCity.setText( cityName );
                                        requestOilPriceData();
                                    }
                                }
                            } );
                }

                mCityPicker.setCityVisibility(false);
                mCityPicker.setCountyVisibility(false);

                mCityPicker.show();
                break;
            default:
                break;
        }
    }
}
