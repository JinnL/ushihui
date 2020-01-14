package com.mcz.xhj.yz.dr_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mcz.xhj.R;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.umeng.analytics.MobclickAgent;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.show_Dialog_IsLogin;
import com.mcz.xhj.yz.dr_util.stringCut;
import com.mcz.xhj.yz.dr_view.CircleTextProgressbar;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import okhttp3.Call;

public class Act_SendMsg extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.title_centertextview)
    TextView title_centertextview;
    @BindView(R.id.title_righttextview)
    TextView title_righttextview;
    @BindView(R.id.title_rightimageview)
    ImageView title_rightimageview;
    @BindView(R.id.title_leftimageview)
    ImageView title_leftimageview;

    @BindView(R.id.tv_bankphone)
    TextView tv_bankphone;
    @BindView(R.id.code_et)
    EditText code_et;
    @BindView(R.id.bt_ok)
    Button bt_ok;

    @BindView(R.id.tv_red_progress_text)
    CircleTextProgressbar mTvProgressBar;
    @BindView(R.id.tv_getsmg)
    TextView tv_getsmg;
    private boolean isSend = false;
    private CircleTextProgressbar.OnCountdownProgressListener progressListener = new CircleTextProgressbar.OnCountdownProgressListener() {
        @Override
        public void onProgress(int what, int progress) {
            if (what == 1) {
                mTvProgressBar.setText(progress + "");
                if (progress == 0) {
                    tv_getsmg.setVisibility(View.VISIBLE);
                    mTvProgressBar.setVisibility(View.GONE);
                    isSend = true;
                    mTvProgressBar.setText("重新获取");
                    mTvProgressBar.setTextColor(Color.parseColor("#ec5c59"));
                }
            } else if (what == 2) {
                mTvProgressBar.setText(progress + "");
            }
            // 比如在首页，这里可以判断进度，进度到了100或者0的时候，你可以做跳过操作。
        }
    };

    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.act_sendmsg;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 4 && resultCode == 4) {//finish
            setResult(4);
            finish();
        } else if (requestCode == 4 && resultCode == 3) {
            setResult(3);
            finish();
        }
    }

    @Override
    protected void initParams() {
        title_centertextview.setText("短信验证");
        amount = getIntent().getStringExtra("amount");
        payNum = getIntent().getStringExtra("payNum");
        bankMobilePhone = getIntent().getStringExtra("bankMobilePhone");
        sendRegMsg();
        bt_ok.setOnClickListener(this);
        tv_getsmg.setOnClickListener(this);
        title_leftimageview.setOnClickListener(this);

        tv_bankphone.setText(bankMobilePhone);
        //短信验证码倒计时
        mTvProgressBar.setCountdownProgressListener(1, progressListener);
        mTvProgressBar.setProgressType(CircleTextProgressbar.ProgressType.COUNT);
        mTvProgressBar.setProgressColor(Color.parseColor("#ec5c59"));
        mTvProgressBar.setTimeMillis(60000);
        mTvProgressBar.setProgressType(CircleTextProgressbar.ProgressType.COUNT_BACK);
        mTvProgressBar.reStart();
//		code_et.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if(isSend){
//					code_et.setText("");
//					tv_getsmg.setVisibility(View.GONE);
//					mTvProgressBar.setVisibility(View.VISIBLE);
//					mTvProgressBar.reStart();
//					isSend = false;
//					codeSend = false;
//					doCashInNext();
//					MobclickAgent.onEvent(Act_SendMsg.this, "100017");
//				}
//			}
//		});
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPause(this);
    }

    Long lastClick = 0l;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//			case R.id.tv_getcode:
//				code_et.setText("");
////				if(payNum!=null&&!payNum.equalsIgnoreCase("")){
//					codeSend = false;
//					doCashInNext();
//					MobclickAgent.onEvent(Act_SendMsg.this, "100017");
////				}else{
////					ToastMaker.showLongToast("您还未创建订单");
////				}
//				break;
            case R.id.tv_getsmg:
                if (isSend) {
                    code_et.setText("");
                    tv_getsmg.setVisibility(View.GONE);
                    mTvProgressBar.setVisibility(View.VISIBLE);
                    mTvProgressBar.reStart();
                    isSend = false;
                    codeSend = false;
                    doCashInNext();
                }
                break;
            case R.id.title_leftimageview:
                finish();
                break;
            case R.id.bt_ok:
                MobclickAgent.onEvent(Act_SendMsg.this, UrlConfig.point + 16 + "");
                if (System.currentTimeMillis() - lastClick <= 1000) {
//					ToastMaker.showShortToast("点那么快干什么");
                    return;
                }
                lastClick = System.currentTimeMillis();
//				if(!codeSend){
//					ToastMaker.showLongToast("请发送验证码");
//					return;
//				}
                if (payNum == null || payNum.equalsIgnoreCase("")) {
                    ToastMaker.showLongToast("请再次发送验证码");
                } else if (code_et.getText().toString().equalsIgnoreCase("")) {
                    ToastMaker.showLongToast("验证码不能为空");
                } else if (code_et.getText().toString().length() < 6) {
                    ToastMaker.showLongToast("验证码必须为6位数字");
                } else {
                    codeSend = false;
                    if (isResponse) {
                        showWaitDialog("处理中...", false, "");
                        isResponse = false;
                        doCashIn();
                    } else {
                        ToastMaker.showShortToast("请勿重复提交");
                    }
                }
                break;
            default:
                break;
        }
    }

    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
    private String payNum = "";
    private String amount;
    private String bankMobilePhone;
    private boolean codeSend = false;//用于判断是否发送了手机验证码

    /**
     * 带着订单号发送验证码1对1关系
     */
    private void sendRegMsg() {
        MobclickAgent.onEvent(Act_SendMsg.this, UrlConfig.point + 15 + "");
        OkHttpUtils
                .post()
                .url(UrlConfig.CASHINMSG)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("type", "1")
                .addParams("payNum", payNum)
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        // TODO Auto-generated method stub
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
                            ToastMaker.showShortToast("验证码已发送");
                            codeSend = true;
                        } else {
                            if ("1002".equals(obj.getString("errorCode"))) {
                                ToastMaker.showShortToast("短信发送失败");
//								doCashInNext();
                            } else if ("9999".equals(obj.getString("errorCode"))) {
                                ToastMaker.showShortToast("系统异常");
                            } else if ("8888".equals(obj.getString("errorCode"))) {
                                ToastMaker.showShortToast("请勿频繁操作");
                            } else if ("9998".equals(obj.getString("errorCode"))) {
                                finish();
                                new show_Dialog_IsLogin(Act_SendMsg.this).show_Is_Login();
                            } else {
                                ToastMaker.showShortToast("系统异常");
                            }
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        ToastMaker.showShortToast("请检查网络!");

                    }
                });
    }

    /**
     * 创建订单
     */
    private boolean isResponse = true;

    private void doCashInNext() {
        OkHttpUtils
                .post()
                .url(UrlConfig.CASHINNEXT)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("amount", amount)
//		.addParams("smsCode", stringCut.space_Cut(code_et.getText().toString()))
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String response) {
                        isResponse = true;
                        dismissDialog();
                        // TODO Auto-generated method stub
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
//						ToastMaker.showShortToast("订单创建成功");
                            JSONObject objmap = obj.getJSONObject("map");
                            payNum = objmap.getString("payNum");
                            codeSend = false;//每次创建订单重置
//							showPopupWindow();
                            sendRegMsg();
                        } else {
                            if ("XTWH".equals(obj.getString("errorCode"))) {
                                startActivity(new Intent(Act_SendMsg.this, WebViewActivity.class)
                                        .putExtra("URL", UrlConfig.WEIHU)
                                        .putExtra("TITLE", "系统维护"));
                                return;
                            }
                            switch (Integer.parseInt(obj.getString(("errorCode")))) {
                                case 9999:
                                    ToastMaker.showShortToast("系统错误");
                                    break;
                                case 1001:
                                    ToastMaker.showShortToast("充值金额有误");
                                    break;
                                case 1002:
                                    ToastMaker.showShortToast("系统错误，请稍后重试");
                                    break;
                                case 1003:
                                    ToastMaker.showShortToast("超过限额，请修改金额后重试");
                                    break;
                                case 9998:
                                    finish();
                                    new show_Dialog_IsLogin(Act_SendMsg.this).show_Is_Login();
                                    break;
                                default:
                                    break;
                            }
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        isResponse = true;
                        dismissDialog();
                        ToastMaker.showShortToast("请检查网络!");

                    }
                });

    }

    /**
     * 充值
     */
    private void doCashIn() {
        OkHttpUtils
                .post()
                .url(UrlConfig.CASHIN)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("payNum", payNum)
//		.addParams("amount", stringCut.space_Cut(et_cash.getText().toString()))
                .addParams("smsCode", stringCut.space_Cut(code_et.getText().toString()))
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        isResponse = true;
                        dismissDialog();
                        // TODO Auto-generated method stub
                        JSONObject obj = JSON.parseObject(response);
                        JSONObject objmap = obj.getJSONObject("map");
                        if (obj.getBoolean(("success"))) {
                            startActivityForResult(new Intent(Act_SendMsg.this, CashIn_End.class)
                                            .putExtra("flag", "cashin-success")
                                            .putExtra("money", amount)
                                            .putExtra("payTime", objmap.getString("payTime"))
                                            .putExtra("confirmTime", objmap.getString("confirmTime"))
                                            .putExtra("paySuccessTime", objmap.getString("paySuccessTime"))
                                    , 4);
//							String amount = objmap.getString("amount");
//							String src = objmap.getString("src");
//							if(src!=null){
//								showPopupWindowNew(src);
//							}else{
//								finish();
//							}
                        } else {
                            if ("XTWH".equals(obj.getString("errorCode"))) {
                                startActivity(new Intent(Act_SendMsg.this, WebViewActivity.class)
                                        .putExtra("URL", UrlConfig.WEIHU)
                                        .putExtra("TITLE", "系统维护"));
                                return;
                            }
                            switch (Integer.parseInt(obj.getString(("errorCode")))) {
                                case 9999:
                                    ToastMaker.showLongToast("系统错误");
                                    payNum = "";
                                    break;
                                case 1003:
                                    ToastMaker.showLongToast("验证码错误");
                                    codeSend = true;
                                    break;
                                case 1004:
                                    JSONObject objmaping = obj.getJSONObject("map");
                                    startActivityForResult(
                                            new Intent(Act_SendMsg.this, CashIn_End.class).putExtra("flag", "cashin-progress")
                                                    .putExtra("msg", "处理中,请查看交易信息")
                                                    .putExtra("money", amount)
                                                    .putExtra("payTime", objmaping.getString("payTime"))
                                                    .putExtra("confirmTime", objmaping.getString("confirmTime")),
                                            4);
                                    break;
                                case 1040:
                                    startActivityForResult(new Intent(Act_SendMsg.this, CashIn_End.class).putExtra("flag", "failiure_cashin").putExtra("msg", "余额不足").putExtra("msgCode", "1040"), 4);
                                    payNum = "";
                                    break;
                                case 1045:
                                    startActivityForResult(new Intent(Act_SendMsg.this, CashIn_End.class).putExtra("flag", "failiure_cashin").putExtra("msg", "银行账户余额不足").putExtra("msgCode", "1045"), 4);
                                    payNum = "";
                                    break;
                                case 1005:
                                    ToastMaker.showLongToast("系统错误，请稍后重试");
                                    payNum = "";
                                    break;
                                case 1006:
                                    startActivityForResult(new Intent(Act_SendMsg.this, CashIn_End.class).putExtra("msgCode", "1006").putExtra("flag", "failiure_cashin").putExtra("msg", "超出单卡号累计交易次数限制"), 4);
                                    payNum = "";
                                    break;
                                case 1007:
                                    startActivityForResult(new Intent(Act_SendMsg.this, CashIn_End.class).putExtra("msgCode", "1007").putExtra("flag", "failiure_cashin").putExtra("msg", "超出银行授信额度"), 4);
                                    payNum = "";
                                    break;
                                case 1008:
                                    startActivityForResult(new Intent(Act_SendMsg.this, CashIn_End.class).putExtra("msgCode", "1008").putExtra("flag", "failiure_cashin").putExtra("msg", "超过用户在银行设置的限额"), 4);
                                    payNum = "";
                                    break;
                                case 1009:
                                    startActivityForResult(new Intent(Act_SendMsg.this, CashIn_End.class).putExtra("msgCode", "1009").putExtra("flag", "failiure_cashin").putExtra("msg", "持卡人身份证验证失败"), 4);
                                    payNum = "";
                                    break;
                                case 1010:
                                    startActivityForResult(new Intent(Act_SendMsg.this, CashIn_End.class).putExtra("msgCode", "1010").putExtra("flag", "failiure_cashin").putExtra("msg", "对不起，您累计交易支付金额超出单笔限额"), 4);
                                    payNum = "";
                                    break;
                                case 1011:
                                    startActivityForResult(new Intent(Act_SendMsg.this, CashIn_End.class).putExtra("msgCode", "1011").putExtra("flag", "failiure_cashin").putExtra("msg", "对不起，您累计交易支付金额超出当日限额"), 4);
                                    payNum = "";
                                    break;
                                case 1012:
                                    startActivityForResult(new Intent(Act_SendMsg.this, CashIn_End.class).putExtra("msgCode", "1012").putExtra("flag", "failiure_cashin").putExtra("msg", "对不起，您累计交易支付金额超出当月限额"), 4);
                                    payNum = "";
                                    break;
                                case 1013:
                                    startActivityForResult(new Intent(Act_SendMsg.this, CashIn_End.class).putExtra("msgCode", "1013").putExtra("flag", "failiure_cashin").putExtra("msg", "非法用户号"), 4);
                                    payNum = "";
                                    break;
                                case 1014:
                                    startActivityForResult(new Intent(Act_SendMsg.this, CashIn_End.class).putExtra("msgCode", "1014").putExtra("flag", "failiure_cashin").putExtra("msg", "该卡暂不支持支付，请更换其他银行卡重试"), 4);
                                    payNum = "";
                                    break;
                                case 1015:
                                    startActivityForResult(new Intent(Act_SendMsg.this, CashIn_End.class).putExtra("msgCode", "1015").putExtra("flag", "failiure_cashin").putExtra("msg", "该卡暂不支持支付，请稍后再试"), 4);
                                    payNum = "";
                                    break;
                                case 1016:
                                    startActivityForResult(new Intent(Act_SendMsg.this, CashIn_End.class).putExtra("msgCode", "1016").putExtra("flag", "failiure_cashin").putExtra("msg", "交易超时"), 4);
                                    payNum = "";
                                    break;
                                case 1017:
                                    startActivityForResult(new Intent(Act_SendMsg.this, CashIn_End.class).putExtra("msgCode", "1017").putExtra("flag", "failiure_cashin").putExtra("msg", "交易金额不能大于最大限额"), 4);
                                    payNum = "";
                                    break;
                                case 1018:
                                    startActivityForResult(new Intent(Act_SendMsg.this, CashIn_End.class).putExtra("msgCode", "1018").putExtra("flag", "failiure_cashin").putExtra("msg", "交易金额不能低于最小限额"), 4);
                                    payNum = "";
                                    break;
                                case 1019:
                                    startActivityForResult(new Intent(Act_SendMsg.this, CashIn_End.class).putExtra("msgCode", "1019").putExtra("flag", "failiure_cashin").putExtra("msg", "交易金额超过渠道当月限额"), 4);
                                    payNum = "";
                                    break;
                                case 1020:
                                    startActivityForResult(new Intent(Act_SendMsg.this, CashIn_End.class).putExtra("msgCode", "1020").putExtra("flag", "failiure_cashin").putExtra("msg", "交易金额为空"), 4);
                                    payNum = "";
                                    break;
                                case 1021:
                                    startActivityForResult(new Intent(Act_SendMsg.this, CashIn_End.class).putExtra("msgCode", "1021").putExtra("flag", "failiure_cashin").putExtra("msg", "交易金额有误"), 4);
                                    payNum = "";
                                    break;
                                case 1022:
                                    startActivityForResult(new Intent(Act_SendMsg.this, CashIn_End.class).putExtra("msgCode", "1022").putExtra("flag", "failiure_cashin").putExtra("msg", "交易失败，风险受限"), 4);
                                    payNum = "";
                                    break;
                                case 1023:
                                    startActivityForResult(new Intent(Act_SendMsg.this, CashIn_End.class).putExtra("msgCode", "1023").putExtra("flag", "failiure_cashin").putExtra("msg", "交易失败，详情请咨询您的发卡行"), 4);
                                    payNum = "";
                                    break;
                                case 1024:
                                    startActivityForResult(new Intent(Act_SendMsg.this, CashIn_End.class).putExtra("msgCode", "1024").putExtra("flag", "failiure_cashin").putExtra("msg", "金额格式有误"), 4);
                                    payNum = "";
                                    break;
                                case 1025:
                                    startActivityForResult(new Intent(Act_SendMsg.this, CashIn_End.class).putExtra("msgCode", "1025").putExtra("flag", "failiure_cashin").putExtra("msg", "仅支持个人银行卡支付"), 4);
                                    payNum = "";
                                    break;
                                case 1026:
                                    startActivityForResult(new Intent(Act_SendMsg.this, CashIn_End.class).putExtra("msgCode", "1026").putExtra("flag", "failiure_cashin").putExtra("msg", "您的银行卡不支持该业务，请与发卡行联系"), 4);
                                    payNum = "";
                                    break;
                                case 1027:
                                    startActivityForResult(new Intent(Act_SendMsg.this, CashIn_End.class).putExtra("msgCode", "1027").putExtra("flag", "failiure_cashin").putExtra("msg", "请核对个人身份证信息"), 4);
                                    payNum = "";
                                    break;
                                case 1028:
                                    startActivityForResult(new Intent(Act_SendMsg.this, CashIn_End.class).putExtra("msgCode", "1028").putExtra("flag", "failiure_cashin").putExtra("msg", "请核对您的订单号"), 4);
                                    payNum = "";
                                    break;
                                case 1029:
                                    startActivityForResult(new Intent(Act_SendMsg.this, CashIn_End.class).putExtra("msgCode", "1029").putExtra("flag", "failiure_cashin").putExtra("msg", "请核对您的个人信息"), 4);
                                    payNum = "";
                                    break;
                                case 1030:
                                    startActivityForResult(new Intent(Act_SendMsg.this, CashIn_End.class).putExtra("msgCode", "1030").putExtra("flag", "failiure_cashin").putExtra("msg", "请核对您的银行卡信息"), 4);
                                    payNum = "";
                                    break;
                                case 1031:
                                    startActivityForResult(new Intent(Act_SendMsg.this, CashIn_End.class).putExtra("msgCode", "1031").putExtra("flag", "failiure_cashin").putExtra("msg", "请核对您的银行信息"), 4);
                                    payNum = "";
                                    break;
                                case 1032:
                                    startActivityForResult(new Intent(Act_SendMsg.this, CashIn_End.class).putExtra("msgCode", "1032").putExtra("flag", "failiure_cashin").putExtra("msg", "请核对您的银行预留手机号"), 4);
                                    payNum = "";
                                    break;
                                case 1033:
                                    startActivityForResult(new Intent(Act_SendMsg.this, CashIn_End.class).putExtra("msgCode", "1033").putExtra("flag", "failiure_cashin").putExtra("msg", "未开通无卡支付或交易超过限额，详情请咨询您的发卡行"), 4);
                                    payNum = "";
                                    break;
                                case 1034:
                                    startActivityForResult(new Intent(Act_SendMsg.this, CashIn_End.class).putExtra("msgCode", "1034").putExtra("flag", "failiure_cashin").putExtra("msg", "信息错误，请核对"), 4);
                                    payNum = "";
                                    break;
                                case 1035:
                                    startActivityForResult(new Intent(Act_SendMsg.this, CashIn_End.class).putExtra("msgCode", "1035").putExtra("flag", "failiure_cashin").putExtra("msg", "银行户名不能为空"), 4);
                                    payNum = "";
                                    break;
                                case 1036:
                                    startActivityForResult(new Intent(Act_SendMsg.this, CashIn_End.class).putExtra("msgCode", "1036").putExtra("flag", "failiure_cashin").putExtra("msg", "银行卡未开通银联在线支付，请向银行咨询"), 4);
                                    payNum = "";
                                    break;
                                case 1037:
                                    startActivityForResult(new Intent(Act_SendMsg.this, CashIn_End.class).putExtra("msgCode", "1037").putExtra("flag", "failiure_cashin").putExtra("msg", "银行名称无效"), 4);
                                    payNum = "";
                                    break;
                                case 1038:
                                    startActivityForResult(new Intent(Act_SendMsg.this, CashIn_End.class).putExtra("msgCode", "1038").putExtra("flag", "failiure_cashin").putExtra("msg", "银行系统繁忙，交易失败，请稍后再提交"), 4);
                                    payNum = "";
                                    break;
                                case 1039:
                                    startActivityForResult(new Intent(Act_SendMsg.this, CashIn_End.class).putExtra("msgCode", "1039").putExtra("flag", "failiure_cashin").putExtra("msg", "银行账号不能为空"), 4);
                                    payNum = "";
                                    break;
                                case 1041:
                                    startActivityForResult(new Intent(Act_SendMsg.this, CashIn_End.class).putExtra("msgCode", "1041").putExtra("flag", "failiure_cashin").putExtra("msg", "证件号错误，请核实"), 4);
                                    payNum = "";
                                    break;
                                case 1042:
                                    startActivityForResult(new Intent(Act_SendMsg.this, CashIn_End.class).putExtra("msgCode", "1042").putExtra("flag", "failiure_cashin").putExtra("msg", "证件号码不能为空"), 4);
                                    payNum = "";
                                    break;
                                case 1043:
                                    startActivityForResult(new Intent(Act_SendMsg.this, CashIn_End.class).putExtra("msgCode", "1043").putExtra("flag", "failiure_cashin").putExtra("msg", "证件类型与卡号不符"), 4);
                                    payNum = "";
                                    break;
                                case 1044:
                                    startActivityForResult(new Intent(Act_SendMsg.this, CashIn_End.class).putExtra("msgCode", "1044").putExtra("flag", "failiure_cashin").putExtra("msg", "银行账户余额不足"), 4);
                                    payNum = "";
                                    break;
                                case 9998:
                                    finish();
                                    new show_Dialog_IsLogin(Act_SendMsg.this).show_Is_Login();
                                    break;

                                default:
                                    payNum = "";
                                    break;
                            }
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
//				call.cancel();
                        isResponse = true;
                        dismissDialog();
                        payNum = "";
                        ToastMaker.showShortToast("请检查网络!");
                    }
                });

    }

}
