package com.ekabao.oil.ui.activity.me;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ekabao.oil.R;
import com.ekabao.oil.bean.OilCardDetailBean;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.ui.activity.BaseActivity;
import com.ekabao.oil.ui.view.DialogMaker;
import com.ekabao.oil.ui.view.ToastMaker;
import com.ekabao.oil.util.LogUtils;
import com.ekabao.oil.util.StringCut;

import butterknife.BindView;
import okhttp3.Call;

public class OilCardDetailsActivity extends BaseActivity {

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
    @BindView(R.id.iv_campany)
    ImageView ivCampany;
    @BindView(R.id.tv_company)
    TextView tvCompany;
    @BindView(R.id.tv_card)
    TextView tvCard;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;
    @BindView(R.id.rl_background)
    RelativeLayout rlBackground;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.tv_all_money)
    TextView tvAllMoney;
    @BindView(R.id.tv_immediate_money)
    TextView tvImmediateMoney;
    @BindView(R.id.tv_new_time)
    TextView tvNewTime;
    @BindView(R.id.tv_new_money)
    TextView tvNewMoney;
    @BindView(R.id.tv_next_money)
    TextView tvNextMoney;
    @BindView(R.id.tv_next_time)
    TextView tvNextTime;


    /**
     * 我的油卡-->油卡详情
     *
     * @time 2018/11/2 18:20
     * Created by lj
     */
    private int uid;
    private int fuelCardId;
    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_oil_card_details);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_oil_card_details;
    }

    @Override
    protected void initParams() {
        Intent intent = getIntent();
        uid = intent.getIntExtra("uid", 0);
        fuelCardId = intent.getIntExtra("fuelCardId", 0);
        titleCentertextview.setText("油卡详情");
        getOilCard();

        titleLeftimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // DialogMaker.showCunguanDialogTwo();
                //ToastUtil.showToast("确定删除?");
                DialogMaker.showRedSureDialog(OilCardDetailsActivity.this, "删除",
                        "确定要删除这张卡片吗？", "取消", "确定", new DialogMaker.DialogCallBack() {
                    @Override
                    public void onButtonClicked(Dialog dialog, int position, Object tag) {
                        // exit_dr();
                        deleteOilCard(fuelCardId);
                    }

                    @Override
                    public void onCancelDialog(Dialog dialog, Object tag) {

                    }
                }, "");
            }
        });
    }


    /**
     * 7.删除油卡
     */
    private void deleteOilCard(int fuelCardId) {
        LogUtils.e("我的油卡" + fuelCardId);
        // TODO: 2018/11/8  preferences.getString("uid", "")
        OkHttpUtils.post().url(UrlConfig.deleteFuelCard)
                // .addParams("uid", "5")
                .addParams("fuelcardId", fuelCardId + "")
                // .addParams("version", UrlConfig.version)
                //.addParams("channel", "2")
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        LogUtils.e("--->删除油卡：" + response);

                        dismissDialog();

                        JSONObject obj = JSON.parseObject(response);

                        if (obj.getBoolean(("success"))) {
                            setResult(RESULT_OK);
                            finish();
                            // getOilCard();

                        } else if ("9999".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("系统异常");
                        } else if ("1001".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("油卡不存在");
                        } else if ("1002".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("该油卡有未完成的充值计划");
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

    /**
     * 我的油卡
     */
    private void getOilCard() {
        LogUtils.e("我的油卡" + uid);
        // TODO: 2018/11/8  preferences.getString("uid", "")
        OkHttpUtils.post().url(UrlConfig.fuelCardDetail)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("fuelCardId", fuelCardId + "")
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        LogUtils.e("--->我的油卡：" + response);
                        dismissDialog();

                        JSONObject obj = JSON.parseObject(response);

                        if (obj.getBoolean(("success"))) {
                            JSONObject map = obj.getJSONObject("map");


                            OilCardDetailBean bean = JSON.toJavaObject(map, OilCardDetailBean.class);
                            OilCardDetailBean.FuelCardDetailBean fuelCardDetail = bean.getFuelCardDetail();
                            //油卡类型 1:中石化 2:中石油 3:公司中石化 4:公司中石油
                            if (fuelCardDetail.getType() == 1 | fuelCardDetail.getType() ==3) {
                                rlBackground.setBackgroundResource(R.drawable.bg_company1);
                                tvCompany.setText("中石化 油卡");
                                ivCampany.setImageResource(R.drawable.icon_zhongshihua);
                            } else {
                                rlBackground.setBackgroundResource(R.drawable.bg_campany2);
                                tvCompany.setText("中石油 油卡");
                                ivCampany.setImageResource(R.drawable.icon_zhongshiyou);
                            }

                            tvName.setText("持卡人   "+fuelCardDetail.getRealname());
                            tvPhone.setText("手机号   "+fuelCardDetail.getMobilephone());
                            tvCard.setText(fuelCardDetail.getCardnum());

                            tvAllMoney.setText("¥" + bean.getTcljAmount());
                            tvImmediateMoney.setText("¥" + bean.getJsczAmount());
                            tvNewMoney.setText("¥" + bean.getLastAmount());

                            if (bean.getLastTime()==0){
                                tvNewTime.setText("最新充值");
                            }else {
                                tvNewTime.setText("最新充值("+StringCut.getDates(bean.getLastTime())+")");
                            }

                            tvNextMoney.setText("¥" + bean.getNextAmount());
                            if (bean.getNextTime()==0){
                                tvNextTime.setText("下次充值");
                            }else {
                                tvNextTime.setText("下次充值("+StringCut.getDates(bean.getNextTime())+")");
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
}
