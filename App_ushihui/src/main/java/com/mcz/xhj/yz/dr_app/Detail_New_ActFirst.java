package com.mcz.xhj.yz.dr_app;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mcz.xhj.R;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.umeng.analytics.MobclickAgent;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_bean.ConponsBean;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.show_Dialog_IsLogin;
import com.mcz.xhj.yz.dr_util.stringCut;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import okhttp3.Call;

public class Detail_New_ActFirst extends BaseActivity implements OnClickListener {
    @BindView(R.id.title_centertextview)
    @Nullable
    TextView title_centertextview;
    @BindView(R.id.title_righttextview)
    @Nullable
    TextView title_righttextview;
    @BindView(R.id.title_rightimageview)
    @Nullable
    ImageView title_rightimageview;
    @BindView(R.id.title_leftimageview)
    @Nullable
    ImageView title_leftimageview;
    @BindView(R.id.image_clean_psw)
    @Nullable
    LinearLayout hongbao_user;
    @BindView(R.id.tv_name_detail)
    @Nullable
    TextView tv_name_detail; // 产品名称
    @BindView(R.id.tv_rate)
    @Nullable
    TextView tv_rate; // 进度数字
    //	@BindView(R.id.guize)
//	private TextView guize; // 起息规则
    @BindView(R.id.tv_leastaAmount)
    @Nullable
    TextView tv_leastaAmount; // 起投金额(元)
    @BindView(R.id.tv_maxAmount)
    @Nullable
    TextView tv_maxAmount; // 起投金额(元)
    @BindView(R.id.tv_deadline)
    @Nullable
    TextView tv_deadline; // 产品期限
    @BindView(R.id.tv_rate_jiaxi)
    @Nullable
    TextView tv_rate_jiaxi; // 产品期限
    //	@BindView(R.id.image_tiyanjin)
//	private ImageView image_tiyanjin;
    @BindView(R.id.lv_check)
    @Nullable
    LinearLayout lv_check; // jiaxi
    @BindView(R.id.ptr_invest)
    @Nullable
    PtrClassicFrameLayout ptr_invest; // jiaxi
    @BindView(R.id.lin_shouyi)
    @Nullable
    LinearLayout lin_shouyi;
    @BindView(R.id.touzi_now)
    @Nullable
    Button touzi_now;
    @BindView(R.id.iv_rate_jiaxi_h)
    @Nullable
    ImageView iv_rate_jiaxi_h;
    @BindView(R.id.web_id)
    @Nullable
    WebView web_id;
    @BindView(R.id.tv_tishi)
    @Nullable
    TextView tv_tishi;
    @BindView(R.id.tishi_bottom_ll)
    @Nullable
    LinearLayout tishi_bottom_ll;
    private String pid, ptype;
    private SharedPreferences preferences;
    private SpannableString ss;

    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.act_detail_newfirst;
        // return R.layout.act_detail_new_noname;
    }

    @Override
    protected void initParams() {
//		title_righttextview.setVisibility(View.GONE);
        title_centertextview.setText("新手体验");
//		title_rightimageview.setVisibility(View.GONE);
        title_leftimageview.setOnClickListener(this);
        touzi_now.setOnClickListener(this);

        preferences = LocalApplication.getInstance().sharereferences;
        Intent intent = getIntent();
        Uri uri = intent.getData();
        if (uri != null) {
            pid = uri.getQueryParameter("pid");
        } else {
            pid = intent.getStringExtra("pid");
        }
//		ptype = intent.getStringExtra("ptype");
        WebSettings settings = web_id.getSettings();
        settings.setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
        settings.setSupportZoom(true);
        web_id.getSettings().setJavaScriptEnabled(true);
        web_id.getSettings().setUseWideViewPort(true);
        web_id.getSettings().setLoadWithOverviewMode(true);
        web_id.loadUrl(UrlConfig.NEWHAND);
        // 交易密码

        ptr_invest.setPtrHandler(new PtrHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                if ("".equals(preferences.getString("uid", ""))) {
                    product_Details();
                } else {
                    product_Details_Uid();
                }
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame,
                                             View content, View header) {
                // TODO Auto-generated method stub
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
//		MobclickAgent.onResume(this);
        MobclickAgent.onEvent(Detail_New_ActFirst.this, "100019");
        if ("".equals(preferences.getString("uid", ""))) {
            product_Details();
        } else {
            product_Details_Uid();
        }
    }

    //	@Override
//	protected void onPause() {
//		// TODO Auto-generated method stub
//		super.onPause();
//		MobclickAgent.onPause(this);
//	}
    private String maxAmount; // 产品投资限额
    private String leastaAmount; // 产品起投限额
    private String balance;
    private int int_last; // 100整数
    private String increasAmount; // 递增
    private String winMeasure; // 风控措施
    private String introduce;// 产品介绍
    private String deadline;
    private String endDate;
    private String rate;
    private Double hongbao, enableAmount;
    private ArrayList<ConponsBean> mlslb2; // 红包列表
    private String fid = ""; // 可用红包id
    private String rate_jiaxi = ""; // 可用红包id
    private boolean isInvested = false;
    private boolean newHandInvested = false;
    private int newStyle = 0;   //0.正常 1.投资过其他  2. 投资了新手

    private void product_Details() {
        showWaitDialog("加载中...", true, "");
        OkHttpUtils.post().url(UrlConfig.PRODUCT_DETAIL)
                .addParam("pid", pid)
                .addParam("version", UrlConfig.version)
                .addParam("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        // TODO Auto-generated method stub
                        dismissDialog();
                        ptr_invest.refreshComplete();
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
                            JSONObject map = obj.getJSONObject("map");
                            if (map.getBoolean("newHandInvested")) {
                                no_checked();
                            }
                            JSONObject info = map.getJSONObject("info");
                            tv_name_detail.setText(info.getString("fullName"));
                            rate = info.getString("rate");
                            deadline = info.getString("deadline");
                            leastaAmount = info.getString("leastaAmount");
                            maxAmount = info.getString("maxAmount");
                            increasAmount = info.getString("increasAmount");
                            rate_jiaxi = info.getString("activityRate");
                            String rate_h = stringCut.getNumKbs(Double
                                    .parseDouble(rate)) + "%";
//                            SpannableString ss = new SpannableString(rate_h);
//                            ss.setSpan(new RelativeSizeSpan(0.6f),
//                                    rate_h.length() - 1, rate_h.length(),
//                                    TypedValue.COMPLEX_UNIT_PX);
                            tv_rate.setText(rate_h);
                            tv_deadline.setText("期限" + deadline + "天 ");
                            tv_leastaAmount.setText("起投" + stringCut.getNumKbs(Double
                                    .parseDouble(info.getString("leastaAmount"))) + "元");
                            if (rate_jiaxi != null) {
                                if (Double.parseDouble(rate_jiaxi) != 0) {
                                    tv_rate_jiaxi.setVisibility(View.VISIBLE);
                                    iv_rate_jiaxi_h.setVisibility(View.VISIBLE);
                                    tv_rate_jiaxi.setText("+" + stringCut.getNumKbs(Double.parseDouble(rate_jiaxi)) + "%");
                                }
                            }
//							tv_maxAmount.setText("限投"+stringCut.getNumKbs(Double
//									.parseDouble(info.getString("maxAmount")))+"元");
                        } else if ("9999".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("系统错误");
                        } else if ("9998".equals(obj.getString("errorCode"))) {
                            new show_Dialog_IsLogin(Detail_New_ActFirst.this).show_Is_Login();
                        } else {
                            ToastMaker.showShortToast("系统错误");
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        // TODO Auto-generated method stub
                        dismissDialog();
                        ToastMaker.showShortToast("请检查网络");
                        ptr_invest.refreshComplete();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        // requestCode标示请求的标示 resultCode表示有数据
        if (requestCode == 1 && resultCode == 3) {
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.tv_xieyi_pro_zhuanrang:
                startActivity(new Intent(Detail_New_ActFirst.this, WebViewActivity.class)
                        .putExtra("URL", UrlConfig.XIEYITITLE)
                        .putExtra("TITLE", "权益转让及受让协议"));
                break;
            case R.id.title_leftimageview:
                finish();
                break;
            case R.id.touzi_now:
                if (!preferences.getBoolean("login", false)) {
                    startActivityForResult(new Intent(Detail_New_ActFirst.this, NewLoginActivity.class), 1);
//				showAlertDialog("提示", "还未登陆，请登录", new String[]{"确定登录"}, true, true, "") ;
                } else {
                    switch (newStyle) {
                        case 0:
                            MobclickAgent.onEvent(Detail_New_ActFirst.this, "100020");
                            memberSetting();
                            break;
                        case 1:
                        case 2:
                            LocalApplication.getInstance().getMainActivity().isInvest = true;
                            LocalApplication.getInstance().getMainActivity().isInvestChecked = true;
                            finish();
                            break;
                    }

//				startActivity(new Intent(Detail_New_ActFirst.this,
//						Detail_New_Act.class).putExtra("pid",
//					pid).putExtra("ptype", ptype));
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onButtonClicked(Dialog dialog, int position, Object tag) {
        // TODO Auto-generated method stub
        if (position == 0) {
            startActivity(new Intent(Detail_New_ActFirst.this, NewLoginActivity.class));
        }
    }

    // 投资
    private void product_Invest() {
        showWaitDialog("加载中...", false, "");
        OkHttpUtils
                .post()
                .url(UrlConfig.INVEST)
                .addParams("pid", pid)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("fid", fid)
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        // TODO Auto-generated method stub
                        dismissDialog();
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
                            ToastMaker.showShortToast("投资成功");
                            finish();
                        } else if ("1001".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("交易密码错误");
                        } else if ("1002".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("产品已募集完");
                        } else if ("1003".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("项目可投资金额不足");
                        } else if ("1004".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("小于起投金额");
                        } else if ("1005".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("非递增金额整数倍");
                        } else if ("1006".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("投资金额大于项目单笔投资限额");
                        } else if ("1007".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("账户可用余额不足");
                        } else if ("1008".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("已投资过产品，不能投资新手产品");
                        } else if ("1009".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("用户不存在");
                        } else if ("9998".equals(obj.getString("errorCode"))) {
                            new show_Dialog_IsLogin(Detail_New_ActFirst.this).show_Is_Login();
                        } else {
                            ToastMaker.showShortToast("系统错误");
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        // TODO Auto-generated method stub
                        dismissDialog();
                        ToastMaker.showShortToast("服务器异常");
                    }
                });
    }

    private void memberSetting() {
        showWaitDialog("加载中...", true, "");
        OkHttpUtils.post().url(UrlConfig.MEMBERSETTING)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        // TODO Auto-generated method stub
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
                            JSONObject map = obj.getJSONObject("map");
                            String realVerify = map.getString("realVerify");
                            String tpwdFlag = map.getString("tpwdFlag");
                            SharedPreferences.Editor editor = preferences
                                    .edit();
                            editor.putString("realVerify", realVerify);
                            editor.putString("tpwdFlag", tpwdFlag);
                            editor.commit();
                            startActivityForResult(new Intent(Detail_New_ActFirst.this,
                                    Detail_New_Act.class).putExtra("pid", pid)
                                    .putExtra("ptype", "1")
                                    .putExtra("tpwdFlag", tpwdFlag)
                                    .putExtra("realVerify", realVerify), 1);
                        } else if ("9998".equals(obj.getString("errorCode"))) {
                            new show_Dialog_IsLogin(Detail_New_ActFirst.this).show_Is_Login();
                        } else {
                            ToastMaker.showShortToast("系统错误");
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        // TODO Auto-generated method stub
                        dismissDialog();
                        ToastMaker.showShortToast("请检查网络");
                    }
                });
    }


    private void product_Details_Uid() {
        showWaitDialog("加载中...", true, "");
        OkHttpUtils.post().url(UrlConfig.PRODUCT_DETAIL).addParams("pid", pid)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        // TODO Auto-generated method stub
                        dismissDialog();
                        ptr_invest.refreshComplete();
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
                            JSONObject map = obj.getJSONObject("map");
                            isInvested = map.getBoolean("isInvested");
                            newHandInvested = map.getBoolean("newHandInvested");
                            if (map.getBoolean("newHandInvested")) {
                                no_checked();
                            }

                            balance = map.getString("balance");
                            JSONArray list = map.getJSONArray("couponList");
                            if (list.size() <= 0) {
                            } else {
                                mlslb2 = (ArrayList<ConponsBean>) JSON
                                        .parseArray(list.toJSONString(),
                                                ConponsBean.class);
                                fid = mlslb2.get(0).getId();
                                enableAmount = mlslb2.get(0).getEnableAmount();
                                hongbao = mlslb2.get(0).getAmount();
                            }
                            JSONObject info = map.getJSONObject("info");
                            tv_name_detail.setText(info.getString("fullName"));

                            rate = info.getString("rate");
                            deadline = info.getString("deadline");
                            leastaAmount = info.getString("leastaAmount");
                            maxAmount = info.getString("maxAmount");
                            increasAmount = info.getString("increasAmount");
                            rate_jiaxi = info.getString("activityRate");
                            if (rate_jiaxi != null) {
                                if (Double.parseDouble(rate_jiaxi) != 0) {
                                    tv_rate_jiaxi.setVisibility(View.VISIBLE);
                                    iv_rate_jiaxi_h.setVisibility(View.VISIBLE);
                                    tv_rate_jiaxi.setText("+" + stringCut.getNumKbs(Double.parseDouble(rate_jiaxi)) + "%");
                                }
                            }
                            String rate_h = stringCut.getNumKbs(Double
                                    .parseDouble(rate)) + "%";
//                            SpannableString ss = new SpannableString(rate_h);
//                            ss.setSpan(new RelativeSizeSpan(0.6f),
//                                    rate_h.length() - 1, rate_h.length(),
//                                    TypedValue.COMPLEX_UNIT_PX);
                            tv_rate.setText(rate_h);
                            tv_deadline.setText("期限" + deadline + "天 ");
                            tv_leastaAmount.setText("起投" + stringCut.getNumKbs(Double
                                    .parseDouble(info.getString("leastaAmount"))) + "元");

                            newStyle = 0;
                            tishi_bottom_ll.setVisibility(View.GONE);
                            checked();
                            if(isInvested || newHandInvested){
                                if (isInvested && !newHandInvested) {
                                    newStyle = 1;
                                    touzi_now.setText("关注其他项目");
                                    tishi_bottom_ll.setVisibility(View.VISIBLE);
                                    tv_tishi.setText("此项目为新用户专享标，仅限新用户投资");
                                } else if (newHandInvested) {
                                    tishi_bottom_ll.setVisibility(View.VISIBLE);
                                    tv_tishi.setText("您已投资新手标，每个用户仅可购买一次");
                                    touzi_now.setText("关注其他项目");
                                    newStyle = 2;
                                }
                            }

//							tv_maxAmount.setText("限投"+stringCut.getNumKbs(Double
//									.parseDouble(info.getString("maxAmount")))+"元");
                        } else if ("9999".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("系统错误");
                        } else if ("9998".equals(obj.getString("errorCode"))) {
                            new show_Dialog_IsLogin(Detail_New_ActFirst.this).show_Is_Login();
                        } else {
                            ToastMaker.showShortToast("系统错误");
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        // TODO Auto-generated method stub
                        dismissDialog();
                        ptr_invest.refreshComplete();
                        ToastMaker.showShortToast("请检查网络");
                    }
                });
    }

    private void no_checked() {
        touzi_now.setFocusable(false);
        touzi_now.setEnabled(false);
        tv_rate.setTextColor(0xffA0A0A0);
//		image_tiyanjin.setImageResource(R.drawable.label_tiyan) ;
        touzi_now.setBackgroundColor(0xffA0A0A0);
        touzi_now.setText("已完成体验");
    }

    private void checked() {
        touzi_now.setFocusable(true);
        touzi_now.setEnabled(true);
        tv_rate.setTextColor(getResources().getColor(R.color.base_title_color));
//		image_tiyanjin.setImageResource(R.drawable.label_tiyan) ;
        touzi_now.setBackgroundResource(R.drawable.bg_btn_corner);
        touzi_now.setText("立即投资");
    }
}
