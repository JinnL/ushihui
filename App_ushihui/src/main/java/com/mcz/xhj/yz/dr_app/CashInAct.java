package com.mcz.xhj.yz.dr_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fuiou.pay.FyPay;
import com.fuiou.pay.FyPayCallBack;
import com.mcz.xhj.R;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.umeng.analytics.MobclickAgent;
import com.mcz.xhj.yz.dr_adapter.AdAdapter;
import com.mcz.xhj.yz.dr_app.find.CallCenterActivity;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_bean.Add_Bean;
import com.mcz.xhj.yz.dr_bean.BankName_Pic;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.LogUtils;
import com.mcz.xhj.yz.dr_util.show_Dialog_IsLogin;
import com.mcz.xhj.yz.dr_util.stringCut;
import com.mcz.xhj.yz.dr_view.DialogMaker;
import com.mcz.xhj.yz.dr_view.TimeButton;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.XML;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;


public class CashInAct extends BaseActivity {
    @BindView(R.id.iv_bank)
    ImageView iv_bank;
    @BindView(R.id.tv_banknum)
    TextView tv_banknum;
    //	@BindView(R.id.tv_limit)
//	TextView tv_limit;
    @BindView(R.id.et_cash)
    EditText et_cash;
    @BindView(R.id.tv_balance_a)
    TextView tv_balance_a;
    @BindView(R.id.tv_tip_limit)
    TextView tv_tip_limit;
    @BindView(R.id.tv_tip_limitday)
    TextView tv_tip_limitday;
    @BindView(R.id.tv_yuying)
    TextView tv_yuying;
    @BindView(R.id.bt_ok)
    TextView bt_ok;
    @BindView(R.id.tv_notice)
    TextView tv_notice;
    @BindView(R.id.title_centertextview)
    TextView centertv;
    @BindView(R.id.title_leftimageview)
    ImageView leftima;
    @BindView(R.id.title_righttextview)
    TextView title_righttextview;
    @BindView(R.id.ll_popcash)
    RelativeLayout ll_popcash;
    @BindView(R.id.ll_empty1)
    LinearLayout ll_empty1;
    @BindView(R.id.tv_bankname)
    TextView tv_bankname;
    @BindView(R.id.tv_quota)
    TextView tv_quota;

    @BindView(R.id.et_yzm)
    EditText et_yzm;
    @BindView(R.id.tv_getyzm)
    TextView tv_getyzm;

    @BindView(R.id.tv_commom_question)
    TextView tv_commom_question;
    @BindView(R.id.tv_contact_us)
    TextView tv_contact_us;

    private Button bt_cashok;
    private TextView tv_cashmonoy;
    private TimeButton tv_getcode;
    private Button bt_cancel;
    private View layout;
    private PopupWindow popupWindow;
    private PopupWindow popupWindowNew;
    //	private String code= "";//用于存储用户输入的短信验证码防止重复提交
    private boolean codeSend = false;//用于判断是否发送了手机验证码
    private String systemOrders;//富友短信发送产生的订单号
    private String bankid;

    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.act_cashin;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onEvent(CashInAct.this, UrlConfig.point + 13 + "");
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPause(this);
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

    class Watcher implements TextWatcher {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0 && !et_cash.getText().toString().equalsIgnoreCase("")) {
                bt_ok.setBackgroundResource(R.mipmap.bg_button);
            } else {
                bt_ok.setBackgroundResource(R.drawable.bg_corner_gray);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

    }

    Long lastClick = 0l;

    @Override
    protected void initParams() {
        centertv.setText("充值");
        et_cash.setHintTextColor(Color.parseColor("#aaaaaa"));
        if (getIntent().getStringExtra("money") != null && !"".equalsIgnoreCase(getIntent().getStringExtra("money"))) {
            et_cash.setText(getIntent().getStringExtra("money"));
            bt_ok.setBackgroundResource(R.drawable.bg_color_yellow);
//			tv_balance_a.setText(getIntent().getStringExtra("money"));
        }
        leftima.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_righttextview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CashInAct.this, BankLimitationActivity.class));
            }
        });
        tv_yuying.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (LocalApplication.time == 1) {
                    tv_getcode.startTime();
                    sendYuyinMsg();
                    LocalApplication.time = 2;
                } else {
                    ToastMaker.showShortToast("操作频繁请您稍后再试");
                }

            }
        });
        getData();
        Watcher watcher = new Watcher();
        et_cash.addTextChangedListener(watcher);
        tv_commom_question.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CashInAct.this, CallCenterActivity.class));
            }
        });
        tv_contact_us.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogMaker.showKufuPhoneDialog(CashInAct.this);
            }
        });
        /*
		* 发送验证码
		* */
        tv_getyzm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equalsIgnoreCase(et_cash.getText().toString())) {
                    ToastMaker.showShortToast("请输入充值金额");
                    stopTimer();
                    return;
                }
                sendFYMsg();
            }
        });
		/*
		* 立即充值
		*/
        bt_ok.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(CashInAct.this, UrlConfig.point + 14 + "");
                HideKeyboard();
                if (System.currentTimeMillis() - lastClick <= 1000) {
//					ToastMaker.showShortToast("点那么快干什么");
                    return;
                }
                lastClick = System.currentTimeMillis();
                String cashInAmount = preferences.getString("cashInAmount", "");
                if ("".equalsIgnoreCase(et_cash.getText().toString())) {
                    ToastMaker.showShortToast("请输入充值金额");
                    return;
                } else if (Double.valueOf(et_cash.getText().toString()) > limit) {
                    ToastMaker.showShortToast("金额不能大于单笔限额");
                    return;
                } else if (Double.valueOf(et_cash.getText().toString()) < 3) {
                    ToastMaker.showShortToast("金额不能小于三元");
                    return;
                } else if (!stringCut.space_Cut(et_cash.getText().toString()).equals(cashInAmount)) {//防止发送短信前后的金额不一样
                    ToastMaker.showShortToast("充值金额发生变化，请重新获取短信验证码");
                    return;
                } else if (et_yzm.getText().toString().equalsIgnoreCase("")) {
                    ToastMaker.showLongToast("验证码不能为空");
                    return;
                } else if (et_yzm.getText().toString().length() < 6) {
                    ToastMaker.showLongToast("验证码必须为6位数字");
                    return;
                } else {
                    if (isResponse) {
                        showWaitDialog("处理中...", false, "");
                        isResponse = false;
                        doCashInApi();
                    } else {
                        ToastMaker.showShortToast("请勿重复提交");
                    }
                }
            }
        });
        et_cash.setLongClickable(false);
        setPricePoint(et_cash);
    }

    public void setPricePoint(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        editText.setText(s);
                        editText.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    editText.setText(s);
                    editText.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        editText.setText(s.subSequence(0, 1));
//                                        editText.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if ("".equals(s.toString())) {
                    tv_balance_a.setText(stringCut.getNumKb(balance));
                } else if ("0".equals(s.toString()) || ".".equals(s.toString())) {
                    et_cash.setText("");
                } else {
                    if (s.toString().contains(".")) {
                        if (s.length() - 1 - s.toString().indexOf(".") > 2) {

                        } else {
                            double cout = balance + Double.valueOf(s.toString());
                            tv_balance_a.setText(stringCut.getNumKb(cout));
                        }
                    } else {
                        double cout = balance + Double.valueOf(s.toString());
                        tv_balance_a.setText(stringCut.getNumKb(cout));
                    }
                }

            }

        });

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub

        if (tv_getcode != null) {
            tv_getcode.onDestroy();
        }
        super.onDestroy();
    }

    @SuppressLint("NewApi")
    public void showPopupWindowNew(String url) {
        // 加载布局
        layout = (RelativeLayout) LayoutInflater.from(this).inflate(
                R.layout.dialog_newfinish, null);
        // 找到布局的控件
        // 实例化popupWindow
        popupWindowNew = new PopupWindow(layout, LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT, true);
        final ImageView iv_newurl = (ImageView) layout.findViewById(R.id.iv_newurl);
        TextView tv_touzi = (TextView) (layout).findViewById(R.id.tv_touzi);
        ImageLoader.getInstance().loadImage(url, new SimpleImageLoadingListener() {

            @Override
            public void onLoadingComplete(String imageUri, View view,
                                          Bitmap loadedImage) {
                // TODO Auto-generated method stub
                super.onLoadingComplete(imageUri, view, loadedImage);
                iv_newurl.setImageBitmap(loadedImage);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view,
                                        FailReason failReason) {
                // TODO Auto-generated method stub
                super.onLoadingFailed(imageUri, view, failReason);
                popupWindowNew.dismiss();
            }

        });
        // 控制键盘是否可以获得焦点
        popupWindowNew.setBackgroundDrawable(new BitmapDrawable());
        // popupWindow.setBackgroundDrawable(new PaintDrawable());
        popupWindowNew.setOutsideTouchable(true);
        popupWindowNew.setFocusable(true);
        // // 设置popupWindow弹出窗体的背景
        // WindowManager manager = (WindowManager)
        // mContext.getSystemService(Context.WINDOW_SERVICE);
        // 监听
        tv_touzi.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(CashInAct.this, ConponsAct.class));
                finish();
            }
        });
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                finish();
//				popupWindow.dismiss();
                return true;
            }
        });
        popupWindowNew.showAsDropDown(iv_newurl);
    }

    private boolean isResponse = true;

    private void doCashIn() {
        OkHttpUtils
                .post()
                .url(UrlConfig.CASHIN)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("payNum", payNum)
//		.addParams("amount", stringCut.space_Cut(et_cash.getText().toString()))
                .addParams("smsCode", stringCut.space_Cut(et_yzm.getText().toString()))
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
                            popupWindow.dismiss();
                            ToastMaker.showLongToast("充值成功");
                            JSONObject objmap = obj.getJSONObject("map");
//					String amount = objmap.getString("amount");
                            String src = objmap.getString("src");
                            if (src != null) {
                                showPopupWindowNew(src);
                            } else {
                                finish();
                            }
                        } else {
                            switch (Integer.parseInt(obj.getString(("errorCode")))) {
                                case 9999:
                                    ToastMaker.showLongToast("系统错误");
                                    popupWindow.dismiss();
                                    break;
//					case 1001:
//						ToastMaker.showLongToast("充值金额有误");
//						break;
//					case 1002:
//						ToastMaker.showLongToast("验证码不能为空");
//						break;
                                case 1003:
                                    ToastMaker.showLongToast("验证码错误");
                                    codeSend = true;
                                    break;
                                case 1004:
                                    ToastMaker.showLongToast("处理中,请查看交易信息");
                                    finish();
                                    break;
                                case 1040:
                                    ToastMaker.showLongToast("余额不足");
                                    popupWindow.dismiss();
                                    break;
                                case 1045:
                                    ToastMaker.showLongToast("银行账户余额不足");
                                    popupWindow.dismiss();
                                    break;
                                case 1005:
                                    ToastMaker.showLongToast("系统错误，请稍后重试");
                                    popupWindow.dismiss();
                                    break;
                                case 1006:
                                    ToastMaker.showLongToast("超出单卡号累计交易次数限制");
                                    popupWindow.dismiss();
                                    break;
                                case 1007:
                                    ToastMaker.showLongToast("超出银行授信额度");
                                    popupWindow.dismiss();
                                    break;
                                case 1008:
                                    ToastMaker.showLongToast("超过用户在银行设置的限额");
                                    popupWindow.dismiss();
                                    break;
                                case 1009:
                                    ToastMaker.showLongToast("持卡人身份证验证失败");
                                    popupWindow.dismiss();
                                    break;
                                case 1010:
                                    ToastMaker.showLongToast("对不起，您累计交易支付金额超出单笔限额");
                                    popupWindow.dismiss();
                                    break;
                                case 1011:
                                    ToastMaker.showLongToast("对不起，您累计交易支付金额超出当日限额");
                                    popupWindow.dismiss();
                                    break;
                                case 1012:
                                    ToastMaker.showLongToast("对不起，您累计交易支付金额超出当月限额");
                                    popupWindow.dismiss();
                                    break;
                                case 1013:
                                    ToastMaker.showLongToast("非法用户号");
                                    popupWindow.dismiss();
                                    break;
                                case 1014:
                                    ToastMaker.showLongToast("该卡暂不支持支付，请更换其他银行卡重试");
                                    popupWindow.dismiss();
                                    break;
                                case 1015:
                                    ToastMaker.showLongToast("该卡暂不支持支付，请稍后再试");
                                    popupWindow.dismiss();
                                    break;
                                case 1016:
                                    ToastMaker.showLongToast("交易超时");
                                    popupWindow.dismiss();
                                    break;
                                case 1017:
                                    ToastMaker.showLongToast("交易金额不能大于最大限额");
                                    popupWindow.dismiss();
                                    break;
                                case 1018:
                                    ToastMaker.showLongToast("交易金额不能低于最小限额");
                                    popupWindow.dismiss();
                                    break;
                                case 1019:
                                    ToastMaker.showLongToast("交易金额超过渠道当月限额");
                                    popupWindow.dismiss();
                                    break;
                                case 1020:
                                    ToastMaker.showLongToast("交易金额为空");
                                    popupWindow.dismiss();
                                    break;
                                case 1021:
                                    ToastMaker.showLongToast("交易金额有误");
                                    popupWindow.dismiss();
                                    break;
                                case 1022:
                                    ToastMaker.showLongToast("交易失败，风险受限");
                                    popupWindow.dismiss();
                                    break;
                                case 1023:
                                    ToastMaker.showLongToast("交易失败，详情请咨询您的发卡行");
                                    popupWindow.dismiss();
                                    break;
                                case 1024:
                                    ToastMaker.showLongToast("金额格式有误");
                                    popupWindow.dismiss();
                                    break;
                                case 1025:
                                    ToastMaker.showLongToast("仅支持个人银行卡支付");
                                    popupWindow.dismiss();
                                    break;
                                case 1026:
                                    ToastMaker.showLongToast("您的银行卡不支持该业务，请与发卡行联系");
                                    popupWindow.dismiss();
                                    break;
                                case 1027:
                                    ToastMaker.showLongToast("请核对个人身份证信息");
                                    popupWindow.dismiss();
                                    break;
                                case 1028:
                                    ToastMaker.showLongToast("请核对您的订单号");
                                    popupWindow.dismiss();
                                    break;
                                case 1029:
                                    ToastMaker.showLongToast("请核对您的个人信息");
                                    popupWindow.dismiss();
                                    break;
                                case 1030:
                                    ToastMaker.showLongToast("请核对您的银行卡信息");
                                    popupWindow.dismiss();
                                    break;
                                case 1031:
                                    ToastMaker.showLongToast("请核对您的银行信息");
                                    popupWindow.dismiss();
                                    break;
                                case 1032:
                                    ToastMaker.showLongToast("请核对您的银行预留手机号");
                                    popupWindow.dismiss();
                                    break;
                                case 1033:
                                    ToastMaker.showLongToast("未开通无卡支付或交易超过限额，详情请咨询您的发卡行");
                                    popupWindow.dismiss();
                                    break;
                                case 1034:
                                    ToastMaker.showLongToast("信息错误，请核对");
                                    popupWindow.dismiss();
                                    break;
                                case 1035:
                                    ToastMaker.showLongToast("银行户名不能为空");
                                    popupWindow.dismiss();
                                    break;
                                case 1036:
                                    ToastMaker.showLongToast("银行卡未开通银联在线支付，请向银行咨询");
                                    popupWindow.dismiss();
                                    break;
                                case 1037:
                                    ToastMaker.showLongToast("银行名称无效");
                                    popupWindow.dismiss();
                                    break;
                                case 1038:
                                    ToastMaker.showLongToast("银行系统繁忙，交易失败，请稍后再提交");
                                    popupWindow.dismiss();
                                    break;
                                case 1039:
                                    ToastMaker.showLongToast("银行账号不能为空");
                                    popupWindow.dismiss();
                                    break;
                                case 1041:
                                    ToastMaker.showLongToast("证件号错误，请核实");
                                    popupWindow.dismiss();
                                    break;
                                case 1042:
                                    ToastMaker.showLongToast("证件号码不能为空");
                                    popupWindow.dismiss();
                                    break;
                                case 1043:
                                    ToastMaker.showLongToast("证件类型与卡号不符");
                                    popupWindow.dismiss();
                                    break;
                                case 1044:
                                    ToastMaker.showLongToast("银行账户余额不足");
                                    popupWindow.dismiss();
                                    break;
                                case 9998:
                                    finish();
                                    new show_Dialog_IsLogin(CashInAct.this).show_Is_Login();
                                    break;

                                default:
                                    popupWindow.dismiss();
                                    break;
                            }
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
//				call.cancel();
                        isResponse = true;
                        dismissDialog();
                        popupWindow.dismiss();
                        ToastMaker.showShortToast("请检查网络!");

                    }
                });

    }

    private String payNum = "";
    private String bankMobilePhone = "";

    //金运通充值接口
    private void doCashInNext() {
        OkHttpUtils
                .post()
                .url(UrlConfig.CASHINNEXT)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("amount", stringCut.space_Cut(et_cash.getText().toString()))
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
//					ToastMaker.showShortToast("订单创建成功");
                            JSONObject objmap = obj.getJSONObject("map");
                            payNum = objmap.getString("payNum");
                            bankMobilePhone = objmap.getString("bankMobilePhone");
                            codeSend = false;//每次创建订单重置
//					showPopupWindow();
                            startActivityForResult(new Intent(CashInAct.this, Act_SendMsg.class)
                                    .putExtra("payNum", payNum)
                                    .putExtra("bankMobilePhone", bankMobilePhone)
                                    .putExtra("amount", stringCut.space_Cut(et_cash.getText().toString())), 4
                            );
                        } else {
                            if ("XTWH".equals(obj.getString("errorCode"))) {
                                startActivity(new Intent(CashInAct.this, WebViewActivity.class)
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
                                    new show_Dialog_IsLogin(CashInAct.this).show_Is_Login();
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


    /*
    * api充值
    * */
    private void doCashInApi() {
        OkHttpUtils
                .post()
                .url(UrlConfig.APIRECHGE)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("amount", stringCut.space_Cut(et_cash.getText().toString()))
                .addParams("verifyCode", stringCut.space_Cut(et_yzm.getText().toString()))
                .addParams("systemOrders", systemOrders)
                .addParams("channel", "2")
                .addParams("source", "app")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String response) {
                        dismissDialog();
                        LogUtils.i("--->api充值 " + response);
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
                            //ToastMaker.showShortToast("订单创建成功");
                            JSONObject objmap = obj.getJSONObject("map");
                            startActivityForResult(new Intent(CashInAct.this, CashIn_End.class)
                                    .putExtra("flag", "cashin-success")
                                    .putExtra("money", stringCut.space_Cut(et_cash.getText().toString())), 4);
                        } else {

                            if ("1001".equals(obj.getString("errorCode"))) {
                                ToastMaker.showShortToast("金额为空/金额不能小于1");
                            } else if ("8888".equals(obj.getString("errorCode"))) {
                                ToastMaker.showShortToast("请重新获取短信验证码");
                            } else if ("9999".equals(obj.getString("errorCode"))) {
                                ToastMaker.showShortToast("系统异常");
                            } else {
                                ToastMaker.showShortToast(obj.getString("errorMsg"));
                            }
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        dismissDialog();
                        ToastMaker.showShortToast("请检查网络!");
                    }
                });
    }


    //富友支付接口
    private void doCashInFyNext() {
        OkHttpUtils
                .post()
                .url(UrlConfig.CASHINNEXTFY)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("amount", stringCut.space_Cut(et_cash.getText().toString()))
                .addParams("channel", "2")
                .addParams("source", "app")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(final String response) {
                        isResponse = true;
                        dismissDialog();
                        // TODO Auto-generated method stub
                        LogUtils.i("--->CASHINNEXTFY " + response);
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
                            //ToastMaker.showShortToast("订单创建成功");
                            JSONObject objmap = obj.getJSONObject("map");

                            FyPay.setDev(false);//测试环境
                            //FyPay.setDev(true);//生产环境
                            FyPay.init(CashInAct.this);
                            Bundle bundle = new Bundle();
                            bundle.putString(UrlConfig.MCHNT_CD, objmap.getString("MCHNTCD"));
                            bundle.putString(UrlConfig.MCHNT_ORDER_ID, objmap.getString("MCHNTORDERID"));
                            bundle.putString(UrlConfig.MCHNT_USER_ID, objmap.getString("USERID"));
                            bundle.putString(UrlConfig.MCHNT_AMT, objmap.getString("AMT"));
                            bundle.putString(UrlConfig.MCHNT_BANK_NUMBER, objmap.getString("BANKCARD"));
                            bundle.putString(UrlConfig.MCHNT_BACK_URL, objmap.getString("BACKURL"));
                            bundle.putString(UrlConfig.MCHNT_USER_NAME, objmap.getString("NAME"));
                            bundle.putString(UrlConfig.MCHNT_USER_IDNU, objmap.getString("IDNO"));
                            bundle.putString(UrlConfig.MCHNT_USER_IDCARD_TYPE, objmap.getString("IDTYPE"));
                            bundle.putString(UrlConfig.MCHNT_SDK_SIGNTP, objmap.getString("SIGNTP"));
                            bundle.putString(UrlConfig.MCHNT_SING_KEY, objmap.getString("SIGN"));
                            bundle.putString(UrlConfig.MCHNT_SDK_VERSION, objmap.getString("VERSION"));
                            bundle.putString(UrlConfig.MCHNT_SDK_TYPE, objmap.getString("TYPE"));
                            int i = FyPay.pay(CashInAct.this, bundle, new FyPayCallBack() {

                                @Override
                                public void onPayComplete(String arg0, String arg1, Bundle arg2) {
                                    // TODO Auto-generated method stub
                                    LogUtils.i("fuiou----------rspCode:" + arg0.toString());
                                    LogUtils.i("fuiou----------rspDesc:" + arg1.toString());
                                    LogUtils.i("fuiou----------extraData:" + arg2.toString());
                                    if (arg0.equals("0001")) {
                                        //System.out.println("支付失败");
                                        startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", arg0).putExtra("flag", "failiure_cashin").putExtra("msg", arg1), 4);
                                    }

                                }

                                @Override
                                public void onPayBackMessage(String arg0) {
                                    // TODO Auto-generated method stub
                                    LogUtils.i("fuiou----------extraData2:" + arg0.toString());
                                    String xml = arg0;
                                    try {
                                        org.json.JSONObject jsonObject = XML.toJSONObject(xml);
                                        org.json.JSONObject response = jsonObject.getJSONObject("RESPONSE");
                                        String responsecode = response.getString("RESPONSECODE");
                                        Integer code = Integer.valueOf(responsecode);
                                        String responsemsg = response.getString("RESPONSEMSG");
                                        String amount = response.getString("AMT");
                                        amount = Integer.valueOf(amount) / 100 + "";

                                        System.out.println("fuiou..........responsecode=" + responsecode);
                                        System.out.println("fuiou..........responsemsg=" + responsemsg);
                                        System.out.println("fuiou..........amount=" + amount);
                                        if (responsecode.equals("0000")) {
                                            //System.out.println("支付成功");
                                            startActivityForResult(new Intent(CashInAct.this, CashIn_End.class)
                                                    .putExtra("flag", "cashin-success")
                                                    .putExtra("money", amount), 4);
                                        } else {
                                            switch (responsecode) {
                                                case "0001":
                                                    startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "0020":
                                                    startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "0021":
                                                    startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "0030":
                                                    startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "1004":
                                                    startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "1005":
                                                    startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "1008":
                                                    startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "1010":
                                                    startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "1040":
                                                    startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "1096":
                                                    startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "5185":
                                                    startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "8110":
                                                    startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "8143":
                                                    startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "9999":
                                                    startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "10FC":
                                                    startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "10FD":
                                                    startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "10FE":
                                                    startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "10M1":
                                                    startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "10M2":
                                                    startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "10SM":
                                                    startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "51B3":
                                                    startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "10013":
                                                    startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "100000":
                                                    startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "100004":
                                                    startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "100005":
                                                    startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "100011":
                                                    startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "100012":
                                                    startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "100013":
                                                    startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "100014":
                                                    startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "100015":
                                                    startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "100017":
                                                    startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "100018":
                                                    startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "100019":
                                                    startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "100020":
                                                    startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "100029":
                                                    startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "100030":
                                                    startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "100039":
                                                    startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "100040":
                                                    startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "100041":
                                                    startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "100042":
                                                    startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "100043":
                                                    startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "100044":
                                                    startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "100045":
                                                    startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "100046":
                                                    startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "100048":
                                                    startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "100049":
                                                    startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "100050":
                                                    startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "200001":
                                                    startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "200002":
                                                    startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "999992":
                                                    startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "999993":
                                                    startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "999997":
                                                    startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "999998":
                                                    startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "999999":
                                                    startActivityForResult(new Intent(CashInAct.this, CashIn_End.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                default:
                                                    break;
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            });

                        } else {
                            if ("XTWH".equals(obj.getString("errorCode"))) {
                                startActivity(new Intent(CashInAct.this, WebViewActivity.class)
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
                                    new show_Dialog_IsLogin(CashInAct.this).show_Is_Login();
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



	/*private Merchant xmlToBean(String arg0) {
		*//*XStream xStream=new XStream();
//		stream.alias("person1", Person.class);//注解已经配置,不需要
//      stream.alias("address1", Address.class);
//		xStream.aliasField("username", Person.class, "name");

		xStream.addImplicitCollection(Person.class, "addressList");
		xStream.setMode(XStream.NO_REFERENCES);
//		 xs.useAttributeFor(Person.class, "name");
		xStream.addImplicitCollection(Person.class, "addressList");

		xStream.processAnnotations(new Class[]{Person.class,Address.class});
		Person person = (Person)xStream.fromXML(xml);
		System.out.println(person);*//*
	}*/

    private List<Add_Bean> lsAd = new ArrayList<>();
    private AdAdapter adapter;

    private void Notice() {
        OkHttpUtils
                .post()
                .url(UrlConfig.NOTICE)
                .addParam("limit", "1")
                .addParam("proId", "16")
                .addParam("version", UrlConfig.version)
                .addParam("channel", "2")
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        // TODO Auto-generated method stub
                        JSONObject obj = JSON.parseObject(response);
                        JSONObject objmap = obj.getJSONObject("map");
                        JSONArray objnotice = objmap.getJSONArray("urgentNotice");
                        if (objnotice != null) {
                            lsAd = JSON.parseArray(objnotice.toJSONString(), Add_Bean.class);
                            showAlertDialog("紧急公告", "    " + lsAd.get(0).getTitle().toString(), new String[]{"确定"}, true, true, "");
//					List<String> info = new ArrayList<>();
//					for (int i = 0; i < lsAd.size(); i++) {
//						info.add(lsAd.get(i).getTitle().toString());
//					}
                        }
//				if (obj.getBoolean(("success"))) {
//					JSONObject objmap = obj.getJSONObject("map");
//					JSONObject objnotice = objmap.getJSONObject("urgentNotice");
//					if(objnotice!=null){
//					String summaryContents = objnotice.getString("summaryContents");
//					if(summaryContents!=null&&!summaryContents.equalsIgnoreCase("")){
//						summaryContents = summaryContents.replace("\r\n", "");
//						showAlertDialog("紧急公告", "    "+summaryContents, new String[]{"确定"}, true, true, "");
//					}
//					}
//				} 
                    }

                    @Override
                    public void onError(Call call, Exception e) {
//				ToastMaker.showShortToast("请检查网络!");

                    }
                });

    }

    private double balance = 0.00;
    private double limit = 0;
    private Integer limitday = 0;
    private BankName_Pic bp;
    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;

    private void getData() {
        showWaitDialog("正在加载中...", false, "");
        OkHttpUtils
                .post()
                .url(UrlConfig.CASHINDETAIL)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        // TODO Auto-generated method stub
                        dismissDialog();
                        JSONObject obj = JSON.parseObject(response);
                        LogUtils.i("--->CASHINDETAIL " + obj);
                        if (obj.getBoolean(("success"))) {
                            JSONObject objmap = obj.getJSONObject("map");
                            JSONArray arr = objmap.getJSONArray("sysArticleList");
                            if (arr.size() != 0) {
                                JSONObject objNotice = arr.getJSONObject(0);
                                String n = objNotice.getString("summaryContents");
                                tv_notice.setText(n);
                                ll_empty1.setVisibility(View.GONE);
                                bt_ok.setVisibility(View.GONE);
                                tv_notice.setVisibility(View.VISIBLE);
                            }
                            bankid = objmap.getString("bankCode");
                            String banknum = objmap.getString("bankNum");
                            balance = objmap.getDoubleValue("funds");
                            limit = objmap.getInteger("singleQuota");
                            limitday = objmap.getInteger("dayQuota");
                            tv_bankname.setText(objmap.getString("bankName"));
                            tv_quota.setText("单笔" + stringCut.getNumKbs(limit) + "元/单日" + stringCut.getNumKbs(limitday) + "元");

                            if (getIntent().getStringExtra("money") != null && !"".equalsIgnoreCase(getIntent().getStringExtra("money"))) {
                                tv_balance_a.setText(stringCut.getNumKb(balance + Double.parseDouble(getIntent().getStringExtra("money"))));
                            } else {
                                tv_balance_a.setText(stringCut.getNumKb(balance));
                            }
                            tv_banknum.setText("尾号" + banknum);
                            if (bp == null) {
                                bp = new BankName_Pic();
                            }
                            Integer pic = bp.bank_Pic(bankid);
//					vh.iv_bankpic.setImageResource(pic);
//					Integer pic = new BankName_Pic(bankid).bank_Pic();
                            iv_bank.setImageResource(pic);
                            tv_tip_limit.append("银行充值单笔限额" + stringCut.getNumKbs(limit) + "元");
                            if (limitday == 0) {
                                tv_tip_limitday.append("银行充值单日无限额");
                            } else {
                                tv_tip_limitday.append("银行充值单日限额" + stringCut.getNumKbs(limitday) + "元");
                            }
//					Notice();//通知信息

                        } else if ("9998".equals(obj.getString("errorCode"))) {
                            finish();
                            new show_Dialog_IsLogin(CashInAct.this).show_Is_Login();
                        } else {
                            ToastMaker.showShortToast("系统异常!");
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        dismissDialog();
                        ToastMaker.showShortToast("请检查网络!");

                    }
                });

    }

    //获取富友手机验证码
    private void sendFYMsg() {
        showWaitDialog("正在加载中...", false, "");
        OkHttpUtils
                .post()
                .url(UrlConfig.FYCASHINMSG)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("amount", stringCut.space_Cut(et_cash.getText().toString()))
                .addParams("bankId", bankid)
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String response) {
                        LogUtils.i("--->充值发送FYMsg " + response);
                        dismissDialog();
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
                            ToastMaker.showShortToast("验证码已发送");
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("cashInAmount", stringCut.space_Cut(et_cash.getText().toString()));
                            editor.commit();
                            time();
                            JSONObject map = obj.getJSONObject("map");
                            systemOrders = map.getString("systemOrders");
                        } else if ("3002".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast(obj.getString("errorMsg"));
                            stopTimer();
                        } else if ("3003".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast(obj.getString("errorMsg"));
                            stopTimer();
                        } else if ("1111".equals(obj.getString("errorCode"))) {
                            stopTimer();
                            ToastMaker.showShortToast("手机号码被锁，请联系客服");
                        } else if ("9998".equals(obj.getString("errorCode"))) {
                            finish();
                            new show_Dialog_IsLogin(CashInAct.this).show_Is_Login();
                        } else {
                            ToastMaker.showShortToast("系统异常");
                        }

                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        dismissDialog();
                        ToastMaker.showShortToast("请检查网络!");
                    }
                });
    }

    //获取手机验证码
    private void sendRegMsg() {
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
                                popupWindow.dismiss();
                            } else if ("9999".equals(obj.getString("errorCode"))) {
                                ToastMaker.showShortToast("系统异常");
                            } else if ("8888".equals(obj.getString("errorCode"))) {
                                ToastMaker.showShortToast("请勿频繁操作");
                            } else if ("9998".equals(obj.getString("errorCode"))) {
                                finish();
                                new show_Dialog_IsLogin(CashInAct.this).show_Is_Login();
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

    //获取语音验证码
    private void sendYuyinMsg() {
        OkHttpUtils
                .post()
                .url(UrlConfig.CASHINMSG)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("type", "2")
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        // TODO Auto-generated method stub
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
                            ToastMaker.showShortToast("获取成功请留意您的电话");
                        } else {
                            if ("1002".equals(obj.getString("errorCode"))) {
                                ToastMaker.showShortToast("短信发送失败，请重新发送");
                            } else if ("8888".equals(obj.getString("errorCode"))) {
                                ToastMaker.showShortToast("操作频繁请您稍后再试");
                            } else if ("9999".equals(obj.getString("errorCode"))) {
                                ToastMaker.showShortToast("系统异常");
                            } else if ("9998".equals(obj.getString("errorCode"))) {
                                finish();
                                new show_Dialog_IsLogin(CashInAct.this).show_Is_Login();

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
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    public void showPopupWindow() {
//			layout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.pop_cashin, null);
        // 控制键盘是否可以获得焦点
//			popupWindow.setFocusable(true);
//			popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        backgroundAlpha(0.5f);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        // 设置popupWindow弹出窗体的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        WindowManager manager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        // 获取xoff
//			int xpos = manager.getDefaultDisplay().getWidth() / 2- popupWindow.getWidth() / 2;
        // xoff,yoff基于anchor的左下角进行偏移。
        popupWindow.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
                et_yzm.setText("");
            }
        });
//			layout.setOnTouchListener(new View.OnTouchListener() {
//				@Override
//				public boolean onTouch(View v, MotionEvent event) {
//					popupWindow.dismiss();
//					return true;
//				}
//			});
        if (et_cash.getText().toString() != null && !et_cash.getText().toString().equalsIgnoreCase("")) {
            double d = Double.valueOf(et_cash.getText().toString());
            tv_cashmonoy.setText(stringCut.getNumKbs(d));
        }
        if (!popupWindow.isShowing()) {
            popupWindow.showAtLocation(ll_popcash, Gravity.CENTER, 0, 0);
        }
    }

    public void HideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken()
                    , InputMethodManager.HIDE_NOT_ALWAYS);

        }
    }

    private int count;
    private Boolean isRun;
    private int time = 1;

    // 计时器
    public void time() {
        count = 60;
        isRun = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (isRun) {
                    count--;
                    handler.sendEmptyMessage(10);
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                    }
                }
            }
        }).start();
    }

    public void stopTimer() {
        tv_getyzm.setEnabled(true);
        tv_getyzm.setText("获取验证码");
        tv_getyzm.setBackgroundResource(R.drawable.bg_corner_kong_blue);
        tv_getyzm.setTextColor(0xFF20A3F9);
        isRun = false;
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10:
                    if (count <= 0) {
                        time = 1;
                        stopTimer();
                    } else {
                        tv_getyzm.setEnabled(false);
                        tv_getyzm.setTextColor(0xffcccccc);
                        tv_getyzm.setBackgroundResource(R.drawable.bg_corner_blackline);
                        tv_getyzm.setText("发送(" + count + ")秒");
                    }

                    break;
                default:
                    break;
            }
        }
    };
}