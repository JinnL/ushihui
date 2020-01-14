package com.ekabao.oil.ui.activity.me;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fuiou.pay.bank.lib.callBack.PayCallBack;
import com.fuiou.pay.bank.lib.model.PayModel;
import com.fuiou.pay.bank.lib.util.FuiouPayUtils;
import com.ekabao.oil.R;
import com.ekabao.oil.bean.BankName_Pic;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.ui.activity.BaseActivity;
import com.ekabao.oil.ui.activity.WebViewActivity;
import com.ekabao.oil.ui.view.DialogMaker;
import com.ekabao.oil.ui.view.TimeButton;
import com.ekabao.oil.ui.view.ToastMaker;
import com.ekabao.oil.util.LogUtils;
import com.ekabao.oil.util.StringCut;
import com.ekabao.oil.util.ToastUtil;
import com.ekabao.oil.util.show_Dialog_IsLogin;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;


public class CashInActivity extends BaseActivity {
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
    //@BindView(R.id.tv_getyzm)
    //TextView tv_getyzm;


    @BindView(R.id.tv_commom_question)
    TextView tv_commom_question;
    @BindView(R.id.tv_contact_us)
    TextView tv_contact_us;
    @BindView(R.id.cb_bank)
    CheckBox cbBank;
    @BindView(R.id.ll_bank)
    LinearLayout llBank;
    @BindView(R.id.cb_cyber_bank)
    CheckBox cbCyberBank;
    @BindView(R.id.ll_cyber_bank)
    LinearLayout llCyberBank;

    private TextView tv_getYzm;//60s倒计时

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
    private boolean isBank;//是银行卡充值还是网银充值

    private String mobilephone; //银行卡里的手机好
    private boolean sendSmsSuccess = false;//支付验证码是否发送成功
    private String SmsCode = "";//短信验证码

    private double balance = 0.00;
    private double limit = 0;
    private Integer limitday = 0;
    private BankName_Pic bp;
    private SharedPreferences preferences = LocalApplication.sharereferences;
    //充值的金额
    private String amount = "0";

    private Dialog dialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_cash_in;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onEvent(CashInActivity.this, UrlConfig.point + 13 + "");
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    Long lastClick = 0L;

    @Override
    protected void initParams() {
        centertv.setText("充值");
        //限额列表 现在不需要了
        //title_righttextview.setVisibility(View.VISIBLE);
        title_righttextview.setText("限额列表");
        //et_cash.setHintTextColor(Color.parseColor("#aaaaaa"));
        if (getIntent().getStringExtra("money") != null && !"".equalsIgnoreCase(getIntent().getStringExtra("money"))) {
            et_cash.setText(getIntent().getStringExtra("money"));
            bt_ok.setBackgroundResource(R.drawable.bg_color_yellow);
//			tv_balance_a.setText(getIntent().getStringExtra("money"));
        }

        title_righttextview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CashInActivity.this, BankLimitActivity.class));
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
        //定义hint的值
        SpannableString ss = new SpannableString("请输入充值金额，不低于3元");
        //设置字体大小 true表示单位是sp
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(14, true);
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        et_cash.setHint(new SpannedString(ss));

        /*
        * 发送验证码
		* */
      /*  tv_getyzm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equalsIgnoreCase(et_cash.getText().toString())) {
                    ToastMaker.showShortToast("请输入充值金额");
                    stopTimer();
                    return;
                }
                sendFYMsg();
            }
        });*/

        et_cash.setLongClickable(false);

        Watcher watcher = new Watcher();
        et_cash.addTextChangedListener(watcher);
        setPricePoint(et_cash);

        String text;
        if (TextUtils.isEmpty(mobilephone)) {
            text = "为了确保你的资金安全已向你预留手机号发送支付验证码";
        } else {
            text = "为了确保你的资金安全已向" + mobilephone + " 发送支付验证码";
        }
        showInvestDialog(CashInActivity.this,
                "确认付款", text, "提交", callBack, "规则");

    }

    @OnClick({R.id.title_leftimageview, R.id.tv_commom_question, R.id.tv_contact_us, R.id.bt_ok,
            R.id.ll_bank, R.id.ll_cyber_bank, R.id.cb_bank, R.id.cb_cyber_bank})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.title_leftimageview:
                finish();
                break;
            case R.id.tv_commom_question:
                startActivity(new Intent(CashInActivity.this, CallCenterActivity.class));
                break;
            case R.id.tv_contact_us:
                DialogMaker.showKufuPhoneDialog(CashInActivity.this);
                break;
            //立即充值
            case R.id.bt_ok:
                MobclickAgent.onEvent(CashInActivity.this, UrlConfig.point + 14 + "");
                HideKeyboard();
                if (System.currentTimeMillis() - lastClick <= 1000) {
                    //ToastMaker.showShortToast("点那么快干什么");
                    return;
                }
                lastClick = System.currentTimeMillis();
                LogUtils.e("cbCyberBank" + !cbCyberBank.isChecked());

                if ("".equalsIgnoreCase(et_cash.getText().toString())) {
                    ToastMaker.showShortToast("请输入充值金额");
                    return;
                } else if (Double.valueOf(et_cash.getText().toString()) > limit) {
                    ToastMaker.showShortToast("金额不能大于单笔限额");
                    return;
                } else if (Double.valueOf(et_cash.getText().toString()) < 3) {
                    ToastMaker.showShortToast("金额不能小于三元");
                    return;

                } else if (!cbCyberBank.isChecked() && !cbBank.isChecked()) {
                    ToastMaker.showShortToast("请选择支付方式");
                    return;
                } else {
                    if (isResponse) {
                        showWaitDialog("处理中...", false, "");
                        isResponse = false;
                        if (isBank) {

                            dialog.show();
                            if (count <= 0) {
                                sendFYMsg();
                            }else {
                                dismissDialog();
                            }

                        } else {
                            onFuyouPay();
                        }

                    } else {
                        ToastMaker.showShortToast("请勿重复提交");
                    }
                }


                break;
            //银行卡
            case R.id.ll_bank:
            case R.id.cb_bank:
                cbBank.setChecked(true);
                cbCyberBank.setChecked(false);
                isBank = true;
                break;
            //网银支付
            case R.id.ll_cyber_bank:
            case R.id.cb_cyber_bank:
                cbBank.setChecked(false);
                cbCyberBank.setChecked(true);
                isBank = false;
                break;
            default:
                break;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //finish
        finish();
        if (requestCode == 4 && resultCode == 4) {
            //充值成功去账户
            setResult(4);

        } else if (requestCode == 4 && resultCode == 3) {
            //充值成功去出借
            setResult(3);
            finish();
        }
    }


    /**
     * 富友的网银支付
     */
    public void onFuyouPay() {

        amount = StringCut.space_Cut(et_cash.getText().toString());


        OkHttpUtils
                .post()
                .url(UrlConfig.FUCASHIN)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("amount", StringCut.space_Cut(et_cash.getText().toString()))
               // .addParams("amount", "0.01")
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String response) {
                        isResponse = true;
                        dismissDialog();
                        JSONObject obj = JSON.parseObject(response);
                        LogUtils.e("富友网银" + response);
                        if (obj.getBoolean(("success"))) {
                            JSONObject objmap = obj.getJSONObject("map");
                            //payNum = objmap.getString("payNum");
                            //bankMobilePhone = objmap.getString("bankMobilePhone");

                            PayModel payModel = new PayModel();
                            //商户后台接受支付结果的地址
                            payModel.back_notify_url = objmap.getString("back_notify_url");
                            //商品名称
                            payModel.goods_name = objmap.getString("goods_name");
                            // 商户在富友入网时生成的商户号
                            payModel.mchnt_cd = objmap.getString("mchnt_cd");

                            //客户支付订单的金额，一笔订单一个，以分为单位(不能有小数点)。不可以为零，必需符合金额标准。
                            payModel.order_amt = objmap.getLong("order_amt");

                            //商户的订单号码，需要商户生成，确保唯一性
                            payModel.order_id = objmap.getString("order_id");
                            //15位商户号+2位序号
                            payModel.app_info = payModel.mchnt_cd + "01";
                            String md5 = objmap.getString("md5");
                            payModel.md5 = md5;

                            /*String md6 = MD5.MD5(
                                    payModel.app_info + "|"
                                            + payModel.back_notify_url + "|"
                                            + payModel.client_type + "|"
                                            + payModel.goods_name + "|"
                            //                +payModel.iss_ins_cd+"|"
                                            + payModel.mchnt_cd + "|"
                                            + payModel.order_amt + "|"
                                            + payModel.order_id + "|"
                                            +payModel.rem1+"|"
                                            +payModel.rem2+"|"
                                            +payModel.rem3+"|"
                                            +payModel.ver+"|"
                                            +"u5kxmq4u517abbh7xydjntx4qnmow115"

                            );*/

                            FuiouPayUtils.setRelease(true);
                            LogUtils.e("md5" + md5);
                            //LogUtils.e("md6"+md6);

                            FuiouPayUtils.pay(CashInActivity.this, payModel, payCallBack);
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        isResponse = true;
                        dismissDialog();
                        ToastMaker.showShortToast("请检查网络!");

                    }
                });


        //IOS客户端版传：1 Android客户端版传：2  client_type 默认的是2

        //payModel.rem1 = rem1Et.getText().toString();
        //  payModel.rem2 = rem2Et.getText().toString();
        //payModel.rem3 = rem3Et.getText().toString();


    }

    /**
     * 富友网银支付回调
     */
    PayCallBack payCallBack = new PayCallBack() {

        @Override
        public void paySuccess() {
            getData();
            startActivityForResult(new Intent(CashInActivity.this, CashResultActivity.class)
                            .putExtra("flag", "cashin-success")
                            .putExtra("money", StringCut.space_Cut(et_cash.getText().toString())),
                    4);

            LogUtils.e("PayCallBack" + "paySuccess: ");
        }

        @Override
        public void payFail(String code, String msg) {
            startActivityForResult(new Intent(CashInActivity.this, CashResultActivity.class)
                            .putExtra("flag", "failiure_cashin")
                            .putExtra("money", StringCut.space_Cut(et_cash.getText().toString())),
                    4);
            LogUtils.e("PayCallBack" + code + "/" + msg);
            ToastUtil.showToast(msg);
            //Toast.makeText(MainActivity.this,msg+",code:"+code,Toast.LENGTH_LONG).show();
            //Log.d(TAG, "payFail: code="+code+",msg="+msg);
        }

//            @Override
//            public void payUnkown() {
//                Toast.makeText(MainActivity.this,"支付结果请查询后台",Toast.LENGTH_LONG).show();
//            }


    };

    /**
     * 显示两个个按钮对话框  支付 短信验证码
     */
    public void showInvestDialog(final Context context, String tile, String content, String sureStr, final DialogMaker.DialogCallBack callBack, final Object obj) {
        dialog = new Dialog(context, R.style.DialogNoTitleStyleTranslucentBg);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_invest_sms, null);
        dialog.setCanceledOnTouchOutside(false);
//		dialog.setCancelable(isCanCancelabel);
        dialog.setContentView(contentView);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER_VERTICAL);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        // TODO: 2018/8/24
        // dialog.show();

        TextView tv_title = (TextView) contentView.findViewById(R.id.tv_title);
        TextView tv_content = (TextView) contentView.findViewById(R.id.tv_content);
        TextView tv_sure = (TextView) contentView.findViewById(R.id.tv_sure);
        final EditText et_sms = (EditText) contentView.findViewById(R.id.et_sms);
        tv_getYzm = (TextView) contentView.findViewById(R.id.tv_send_sms);
        TextView tv_cacel = (TextView) contentView.findViewById(R.id.tv_cacel);

        tv_title.setText(tile);
        tv_content.setText(content);
        tv_sure.setText(sureStr);

        tv_getYzm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.onButtonClicked(dialog, 1, obj);
                //dialog.dismiss();
            }
        });
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = et_sms.getText().toString();
                callBack.onButtonClicked(dialog, 0, s);
                //dialog.dismiss();
            }
        });
        tv_cacel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.onCancelDialog(dialog, null);
                dialog.dismiss();
            }
        });
    }

    /**
     * 银行卡充值的dialog 回调
     */
    DialogMaker.DialogCallBack callBack = new DialogMaker.DialogCallBack() {
        @Override
        public void onButtonClicked(Dialog dialog, int position, Object tag) {
            LogUtils.e("position" + position + "tag" + tag.toString());

            //
            if (position == 1) {
                sendFYMsg();
            } else {

               /* if (!sendSmsSuccess) {
                    sendFYMsg();
                    return;
                }*/

                String sms = tag.toString();
                SmsCode = sms;
                String cashInAmount = preferences.getString("cashInAmount", "");

                if (!StringCut.space_Cut(et_cash.getText().toString()).equals(cashInAmount)) {
                    //防止发送短信前后的金额不一样
                    ToastMaker.showShortToast("充值金额发生变化，请重新获取短信验证码");
                    isResponse = true;
                    return;
                } else if (SmsCode.equalsIgnoreCase("")) {
                    ToastMaker.showLongToast("验证码不能为空");
                    isResponse = true;
                    return;
                } else if (SmsCode.length() < 6) {
                    ToastMaker.showLongToast("验证码必须为6位数字");
                    isResponse = true;
                    return;
                } else {
                    doCashInApi();
                }

                //dialog.dismiss();
            }

        }

        @Override
        public void onCancelDialog(Dialog dialog, Object tag) {
            isResponse = true;
            // LogUtils.e("tag"+tag.toString());

        }
    };

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
                    //tv_balance_a.setText(StringCut.getNumKb(balance));
                } else if ("0".equals(s.toString()) || ".".equals(s.toString())) {
                    et_cash.setText("");
                } else {
                    if (s.toString().contains(".")) {
                        if (s.length() - 1 - s.toString().indexOf(".") > 2) {

                        } else {
                            double cout = balance + Double.valueOf(s.toString());
                            //tv_balance_a.setText(StringCut.getNumKb(cout));
                        }
                    } else {
                        double cout = balance + Double.valueOf(s.toString());
                        // tv_balance_a.setText(StringCut.getNumKb(cout));
                    }
                }

            }

        });

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
            /*if (s.length() > 0 && !et_cash.getText().toString().equalsIgnoreCase("")) {
                bt_ok.setBackgroundResource(R.color.red);
            } else {
                bt_ok.setBackgroundResource(R.drawable.bg_corner_gray);
            }*/
        }
    }

    /**
     * api充值
     */
    private void doCashInApi() {
        OkHttpUtils
                .post()
                .url(UrlConfig.APIRECHGE)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("amount", StringCut.space_Cut(et_cash.getText().toString()))
                //.addParams("verifyCode", StringCut.space_Cut(et_yzm.getText().toString()))
                .addParams("verifyCode", SmsCode)
                .addParams("systemOrders", systemOrders)
                .addParams("channel", "2")
                .addParams("source", "app")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String response) {
                        isResponse = true;
                        dismissDialog();
                        LogUtils.i("--->api充值 " + response);
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
                            //ToastMaker.showShortToast("订单创建成功");

                            dialog.dismiss();

                            JSONObject objmap = obj.getJSONObject("map");
                            startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class)
                                    .putExtra("flag", "cashin-success")
                                    .putExtra("money", StringCut.space_Cut(et_cash.getText().toString())), 4);
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
                        isResponse = true;
                        dismissDialog();
                        ToastMaker.showShortToast("请检查网络!");
                    }
                });
    }


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
                            mobilephone = objmap.getString("mobilePhone");
                            tv_quota.setText("单笔" + StringCut.getNumKbs(limit) + "元/单日" + StringCut.getNumKbs(limitday) + "元");

                            if (getIntent().getStringExtra("money") != null && !"".equalsIgnoreCase(getIntent().getStringExtra("money"))) {
                                tv_balance_a.setText(StringCut.getNumKb(balance + Double.parseDouble(getIntent().getStringExtra("money"))));
                            } else {
                                tv_balance_a.setText(StringCut.getNumKb(balance));
                            }
                            tv_banknum.setText("尾号" + banknum);
                            if (bp == null) {
                                bp = new BankName_Pic();
                            }
                            Integer pic = bp.bank_Pic(bankid);
//					vh.iv_bankpic.setImageResource(pic);
//					Integer pic = new BankName_Pic(bankid).bank_Pic();
                            iv_bank.setImageResource(pic);
                            tv_tip_limit.append("银行充值单笔限额" + StringCut.getNumKbs(limit) + "元");
                            if (limitday == 0) {
                                tv_tip_limitday.append("银行充值单日无限额");
                            } else {
                                tv_tip_limitday.append("银行充值单日限额" + StringCut.getNumKbs(limitday) + "元");
                            }
                            //Notice();//通知信息

                        } else if ("9998".equals(obj.getString("errorCode"))) {
                            finish();
                            new show_Dialog_IsLogin(CashInActivity.this).show_Is_Login();
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


    @Override
    protected void onDestroy() {


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

                super.onLoadingComplete(imageUri, view, loadedImage);
                iv_newurl.setImageBitmap(loadedImage);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view,
                                        FailReason failReason) {

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
                startActivity(new Intent(CashInActivity.this, MeWelfareActivity.class));
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
//		.addParams("amount", StringCut.space_Cut(et_cash.getText().toString()))
                .addParams("smsCode", StringCut.space_Cut(et_yzm.getText().toString()))
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        isResponse = true;
                        dismissDialog();

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
                                    new show_Dialog_IsLogin(CashInActivity.this).show_Is_Login();
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
                .addParams("amount", StringCut.space_Cut(et_cash.getText().toString()))
//		.addParams("smsCode", StringCut.space_Cut(code_et.getText().toString()))
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String response) {
                        isResponse = true;
                        dismissDialog();

                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
//					ToastMaker.showShortToast("订单创建成功");
                            JSONObject objmap = obj.getJSONObject("map");
                            payNum = objmap.getString("payNum");
                            bankMobilePhone = objmap.getString("bankMobilePhone");
                            codeSend = false;//每次创建订单重置
//					showPopupWindow();
                           /* startActivityForResult(new Intent(CashInActivity.this, Act_SendMsg.class)
                                    .putExtra("payNum", payNum)
                                    .putExtra("bankMobilePhone", bankMobilePhone)
                                    .putExtra("amount", StringCut.space_Cut(et_cash.getText().toString())), 4
                            );*/
                        } else {
                            if ("XTWH".equals(obj.getString("errorCode"))) {
                                startActivity(new Intent(CashInActivity.this, WebViewActivity.class)
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
                                    new show_Dialog_IsLogin(CashInActivity.this).show_Is_Login();
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


    //获取富友手机验证码
    private void sendFYMsg() {
        showWaitDialog("正在加载中...", false, "");
        OkHttpUtils
                .post()
                .url(UrlConfig.FYCASHINMSG)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("amount", StringCut.space_Cut(et_cash.getText().toString()))
                .addParams("bankId", bankid)
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String response) {
                        isResponse = true;
                        LogUtils.i("--->充值发送FYMsg " + response);
                        dismissDialog();
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
                            ToastMaker.showShortToast("验证码已发送");
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("cashInAmount", StringCut.space_Cut(et_cash.getText().toString()));
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
                            new show_Dialog_IsLogin(CashInActivity.this).show_Is_Login();
                        } else {
                            ToastMaker.showShortToast("系统异常");
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
                                new show_Dialog_IsLogin(CashInActivity.this).show_Is_Login();
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
                                new show_Dialog_IsLogin(CashInActivity.this).show_Is_Login();

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
            tv_cashmonoy.setText(StringCut.getNumKbs(d));
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
        tv_getYzm.setEnabled(true);
        tv_getYzm.setText("发送");
        tv_getYzm.setBackgroundResource(R.drawable.bg_corner_kong_blue);
        tv_getYzm.setTextColor(0xFF20A3F9);
        isRun = false;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10:
                    if (count <= 0) {
                        time = 1;
                        stopTimer();
                    } else {
                        tv_getYzm.setEnabled(false);
                        tv_getYzm.setTextColor(getResources().getColor(R.color.sms));
                        tv_getYzm.setBackgroundResource(R.color.sms_ss);
                        tv_getYzm.setText("" + count + "s");
                    }

                    break;
                default:
                    break;
            }
        }
    };
    //富友支付接口
   /* private void doCashInFyNext() {
        OkHttpUtils
                .post()
                .url(UrlConfig.CASHINNEXTFY)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("amount", StringCut.space_Cut(et_cash.getText().toString()))
                .addParams("channel", "2")
                .addParams("source", "app")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(final String response) {
                        isResponse = true;
                        dismissDialog();

                        LogUtils.i("--->CASHINNEXTFY " + response);
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
                            //ToastMaker.showShortToast("订单创建成功");
                            JSONObject objmap = obj.getJSONObject("map");

                            FyPay.setDev(false);//测试环境
                            //FyPay.setDev(true);//生产环境
                            FyPay.init(CashInActivity.this);
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
                            int i = FyPay.pay(CashInActivity.this, bundle, new FyPayCallBack() {

                                @Override
                                public void onPayComplete(String arg0, String arg1, Bundle arg2) {

                                    LogUtils.i("fuiou----------rspCode:" + arg0.toString());
                                    LogUtils.i("fuiou----------rspDesc:" + arg1.toString());
                                    LogUtils.i("fuiou----------extraData:" + arg2.toString());
                                    if (arg0.equals("0001")) {
                                        //System.out.println("支付失败");
                                        startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", arg0).putExtra("flag", "failiure_cashin").putExtra("msg", arg1), 4);
                                    }

                                }

                                @Override
                                public void onPayBackMessage(String arg0) {

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
                                            startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class)
                                                    .putExtra("flag", "cashin-success")
                                                    .putExtra("money", amount), 4);
                                        } else {
                                            switch (responsecode) {
                                                case "0001":
                                                    startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "0020":
                                                    startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "0021":
                                                    startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "0030":
                                                    startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "1004":
                                                    startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "1005":
                                                    startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "1008":
                                                    startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "1010":
                                                    startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "1040":
                                                    startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "1096":
                                                    startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "5185":
                                                    startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "8110":
                                                    startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "8143":
                                                    startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "9999":
                                                    startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "10FC":
                                                    startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "10FD":
                                                    startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "10FE":
                                                    startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "10M1":
                                                    startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "10M2":
                                                    startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "10SM":
                                                    startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "51B3":
                                                    startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "10013":
                                                    startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "100000":
                                                    startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "100004":
                                                    startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "100005":
                                                    startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "100011":
                                                    startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "100012":
                                                    startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "100013":
                                                    startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "100014":
                                                    startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "100015":
                                                    startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "100017":
                                                    startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "100018":
                                                    startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "100019":
                                                    startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "100020":
                                                    startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "100029":
                                                    startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "100030":
                                                    startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "100039":
                                                    startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "100040":
                                                    startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "100041":
                                                    startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "100042":
                                                    startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "100043":
                                                    startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "100044":
                                                    startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "100045":
                                                    startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "100046":
                                                    startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "100048":
                                                    startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "100049":
                                                    startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "100050":
                                                    startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "200001":
                                                    startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "200002":
                                                    startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "999992":
                                                    startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "999993":
                                                    startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "999997":
                                                    startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "999998":
                                                    startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
                                                    break;
                                                case "999999":
                                                    startActivityForResult(new Intent(CashInActivity.this, CashInEndActivity.class).putExtra("msgCode", responsecode).putExtra("flag", "failiure_cashin").putExtra("msg", responsemsg), 4);
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
                                startActivity(new Intent(CashInActivity.this, WebViewActivity.class)
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
                                    new show_Dialog_IsLogin(CashInActivity.this).show_Is_Login();
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

    }*/



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

   /* private List<Add_Bean> lsAd = new ArrayList<>();
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

    }*/
}