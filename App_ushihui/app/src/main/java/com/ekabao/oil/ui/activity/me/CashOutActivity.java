package com.ekabao.oil.ui.activity.me;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ekabao.oil.R;

import com.ekabao.oil.ui.activity.ForgetPswActivity;
import com.ekabao.oil.ui.view.PwdEdittext.TradePwdPopUtils;
import com.ekabao.oil.util.SecurityUtils;
import com.umeng.analytics.MobclickAgent;

import com.ekabao.oil.bean.BankName_Pic;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.ui.activity.BaseActivity;
import com.ekabao.oil.ui.activity.WebViewActivity;
import com.ekabao.oil.ui.view.DialogMaker;
import com.ekabao.oil.ui.view.ToastMaker;
import com.ekabao.oil.util.LogUtils;
import com.ekabao.oil.util.StringCut;
import com.ekabao.oil.util.show_Dialog_IsLogin;



import butterknife.BindView;
import okhttp3.Call;

public class CashOutActivity extends BaseActivity {
    @BindView(R.id.iv_bank)
    ImageView iv_bank;
    @BindView(R.id.tv_banknum)
    TextView tv_banknum;
    @BindView(R.id.et_money)
    EditText et_money;
    @BindView(R.id.tv_balance_n)
    TextView tv_balance_n;
    @BindView(R.id.tv_balance_a)
    TextView tv_balance_a;

    @BindView(R.id.bt_ok)
    @Nullable
    TextView bt_ok;
    @BindView(R.id.title_centertextview)
    TextView centertv;
    @BindView(R.id.title_leftimageview)
    ImageView leftima;
    //	@BindView(R.id.ll_pwd)
//	LinearLayout ll_pwd;
    @BindView(R.id.ll_all)
    LinearLayout ll_all;
    @BindView(R.id.tv_bankname)
    TextView tv_bankname;
    @BindView(R.id.tv_cashout_all)
    TextView tv_cashout_all;
    @BindView(R.id.tv_commom_question)
    TextView tv_commom_question;
    @BindView(R.id.tv_contact_us)
    TextView tv_contact_us;

    @Override
    protected int getLayoutId() {

        return R.layout.activity_cash_out;
    }

//	@Override
//	protected void onPause() {
//
//		super.onPause();
//		MobclickAgent.onPause(this);
//	}


    private boolean isfirstPwd = false;//是否是第一次设置密码
    String firstPwd;
    String secondPwd;

    @Override
    public void onResume() {

        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onEvent(CashOutActivity.this, UrlConfig.point + 30 + "");
    }

    @Override
    protected void onPause() {

        super.onPause();
        MobclickAgent.onPause(this);
    }

    Long lastClick = 0L;

    @Override
    protected void initParams() {
        et_money.setLongClickable(false);
//		ll_pwd.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				startActivityForResult(new Intent(CashOutActivity.this, TransactionPswAct.class));
//			}
//		});
        et_money.setHintTextColor(Color.parseColor("#aaaaaa"));
        centertv.setText("提现");
        leftima.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getData(1);
        setPricePoint(et_money);
        tv_commom_question.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CashOutActivity.this, CallCenterActivity.class));
            }
        });
        tv_contact_us.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogMaker.showKufuPhoneDialog(CashOutActivity.this);
            }
        });
        bt_ok.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (System.currentTimeMillis() - lastClick <= 1000) {
//					ToastMaker.showShortToast("点那么快干什么");
                    return;
                }
                lastClick = System.currentTimeMillis();
                if ("".equalsIgnoreCase(et_money.getText().toString())) {
                    ToastMaker.showShortToast("金额不能为空!");
                } else if (Double.valueOf(et_money.getText().toString()) > 5000000) {
                    ToastMaker.showShortToast("金额不能大于单笔限额!");
                } else if (Double.valueOf(et_money.getText().toString()) > balance) {
                    ToastMaker.showShortToast("金额不能大于账户余额!");
                } else if ("0".equalsIgnoreCase(et_money.getText().toString().substring(0, 1))) {
                    ToastMaker.showShortToast("金额不能小于一元!");
                } else {
                    if (Double.valueOf(tv_balance_n.getText().toString()) == 2) {
                        if (Double.valueOf(et_money.getText().toString()) - 1 < Double.valueOf(tv_balance_n.getText().toString())) {
                            ToastMaker.showShortToast("金额不能小于三元!");
                        } else {
                            getData(2);
                        }
                    } else {
                        getData(2);
                    }
                }
            }
        });
        tv_cashout_all.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                et_money.setText(balance + "");
                et_money.setSelection(et_money.getText().length());
            }
        });

        Watcher watcher = new Watcher();
        et_money.addTextChangedListener(watcher);
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
            if (s.length() > 0 && !et_money.getText().toString().equalsIgnoreCase("")) {
                //bt_ok.setBackgroundResource(R.drawable.bg_color_yellow);
            } else {
                //bt_ok.setBackgroundResource(R.drawable.bg_corner_gray);
            }
        }
    }

    public void setPricePoint(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
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
//    						tv_balance_a.setText("0.00");
                    tv_balance_a.setText(balance + "");
                } else if ("0".equals(s.toString()) || ".".equals(s.toString())) {
                    et_money.setText("");
                } else {
                    if (s.toString().contains(".")) {
                        if (s.length() - 1 - s.toString().indexOf(".") > 2) {

                        } else {
                            if (istip == 1) {
                                double cout = balance - Double.valueOf(s.toString()) - 2;
                                if (cout <= 0) {
                                    cout = 0;
                                }
                                tv_balance_a.setText(StringCut.getNumKb(cout));
                            } else {
                                double cout = balance - Double.valueOf(s.toString());
                                if (cout <= 0) {
                                    cout = 0;
                                }
                                tv_balance_a.setText(StringCut.getNumKb(cout));
                            }
                        }
                    } else {
                        if (istip == 1) {
                            double cout = balance - Double.valueOf(s.toString());
//		                			double cout = balance-Double.valueOf(s.toString())-2;
                            if (cout <= 0) {
                                cout = 0;
                            }
                            tv_balance_a.setText(StringCut.getNumKb(cout));
                        } else {
                            double cout = balance - Double.valueOf(s.toString());
                            if (cout <= 0) {
                                cout = 0;
                            }
                            tv_balance_a.setText(StringCut.getNumKb(cout));
                        }
                    }
                }

            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 4 && resultCode == 4) {//finish
            finish();
        }
    }

    private void doCashOut(Integer s, String pwd) {
        showWaitDialog("请稍后...", false, "");
        OkHttpUtils
                .post()
                .url(UrlConfig.CASHOUT)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("amount", StringCut.space_Cut(et_money.getText().toString()))
                .addParams("tpw", SecurityUtils.MD5AndSHA256(StringCut.space_Cut(pwd)))
                .addParams("isChargeFlag", s + "")
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        dismissDialog();

                        LogUtils.i("提现："+response);
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
                            //ToastMaker.showShortToast("提现成功!");
                            JSONObject objmap = obj.getJSONObject("map");
                            long withdrawalsTime = objmap.getLong("withdrawalsTime");
                            startActivityForResult(new Intent(CashOutActivity.this, CashInEndActivity.class).putExtra("flag", "cashout-success").putExtra("withdrawalsTime", withdrawalsTime).putExtra("money", StringCut.space_Cut(et_money.getText().toString())), 4);
                            finish();
                        } else {
                            if ("XTWH".equals(obj.getString("errorCode"))) {
                                startActivity(new Intent(CashOutActivity.this, WebViewActivity.class)
                                        .putExtra("URL", UrlConfig.WEIHU)
                                        .putExtra("TITLE", "系统维护"));
                                return;
                            }
                            switch (Integer.parseInt(obj.getString(("errorCode")))) {
                                case 9999:
                                    startActivityForResult(new Intent(CashOutActivity.this, CashInEndActivity.class).putExtra("msgCode", "9999").putExtra("flag", "failiure_cashout").putExtra("msg", "系统繁忙"), 4);
                                    break;
                                case 1001:
                                    startActivityForResult(new Intent(CashOutActivity.this, CashInEndActivity.class).putExtra("msgCode", "1001").putExtra("flag", "failiure_cashout").putExtra("msg", "提现金额不能小于3块"), 4);
                                    break;
                                case 1002:
                                    startActivityForResult(new Intent(CashOutActivity.this, CashInEndActivity.class).putExtra("msgCode", "1002").putExtra("flag", "failiure_cashout").putExtra("msg", "交易密码不能为空"), 4);
                                    break;
                                case 1003:
                                    pop.tv_tips.setText("交易密码错误");
                                    break;
                                case 2001:
                                    pop.tv_tips.setText("连续输错三次密码，交易密码锁定一小时！请稍后再试或点击忘记密码");
//						showAlertDialog("提示", "连续输错三次密码，交易密码锁定一小时！请稍后再试或点击忘记密码。", new String[] { "稍后再试","忘记密码" }, true, true, "three_time");
                                    break;
                                case 1004:
                                    startActivityForResult(new Intent(CashOutActivity.this, CashInEndActivity.class).putExtra("msgCode", "1004").putExtra("flag", "failiure_cashout").putExtra("msg", "余额不足"), 4);
                                    break;
                                case 1005:
                                    startActivityForResult(new Intent(CashOutActivity.this, CashInEndActivity.class).putExtra("msgCode", "1005").putExtra("flag", "failiure_cashout").putExtra("msg", "交易失败,请再次申请"), 4);
                                    break;
                                case 1006:
//						ToastMaker.showShortToast("处理中,请查看交易信息");
                                    JSONObject objmap = obj.getJSONObject("map");
                                    startActivityForResult(new Intent(CashOutActivity.this, CashInEndActivity.class).putExtra("flag", "cashout-progress")
                                                    .putExtra("payTime", objmap.getString("withdrawalsTime"))
                                                    .putExtra("confirmTime", objmap.getString("confirmTime"))
                                                    .putExtra("money", StringCut.space_Cut(et_money.getText().toString())),
                                            4);
                                    finish();
                                    break;
                                case 1007:
                                    startActivityForResult(new Intent(CashOutActivity.this, CashInEndActivity.class).putExtra("msgCode", "1007").putExtra("flag", "failiure_cashout").putExtra("msg", "该笔需要收取手续费"), 4);
                                    break;
                                case 1008:
                                    startActivityForResult(new Intent(CashOutActivity.this, CashInEndActivity.class).putExtra("msgCode", "1008").putExtra("flag", "failiure_cashout").putExtra("msg", "该笔不需要收取手续费"), 4);
                                    break;
                                case 1009:
                                    startActivityForResult(new Intent(CashOutActivity.this, CashInEndActivity.class).putExtra("msgCode", "1008").putExtra("flag", "failiure_cashout").putExtra("msg", "渠道为空"), 4);
                                    break;
                                case 9998:
                                    finish();
                                    new show_Dialog_IsLogin(CashOutActivity.this).show_Is_Login();
                                    break;
                                case 2002:
                                    startActivityForResult(new Intent(CashOutActivity.this, CashInEndActivity.class).putExtra("msgCode", "2002").putExtra("flag", "failiure_cashout").putExtra("msg", "返现或体验金收益需完成一次真实出借后才可提现"), 4);
                                    break;
                                default:
                                    break;
                            }
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        call.cancel();
                        dismissDialog();
                        ToastMaker.showShortToast("请检查网络!");

                    }
                });

    }

    @Override
    public void onButtonClicked(Dialog dialog, int position, Object tag) {

        super.onButtonClicked(dialog, position, tag);
        if (position == 1) {
            startActivity(new Intent(CashOutActivity.this, ForgetPswActivity.class).putExtra("isFrom", 1));
        }
    }

    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
    private double balance = 0.00;
    private Integer istip = 0;
    private TradePwdPopUtils pop;
    private BankName_Pic bp;

    private void getData(final int i) {
        showWaitDialog("请稍后...", false, "");
        OkHttpUtils
                .post()
                .url(UrlConfig.CASHOUTDETAIL)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        dismissDialog();

                        LogUtils.i("--->cashOutdetail " + response);
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
                            JSONObject objmap = obj.getJSONObject("map");
                            String bankid = objmap.getString("bankCode");
                            String banknum = objmap.getString("bankNum");
                            balance = objmap.getDoubleValue("funds");
                            String ispwd = objmap.getString("tpwdFlag");
//					limit = objmap.getString("quota");
                            istip = objmap.getInteger("isChargeFlag");
//					tv_limit.setText("单笔限额"+limit+"元");
//					tv_balance.setText(StringCut.getNumKb(balance)+"元");
                            tv_bankname.setText(objmap.getString("bankName"));
                            tv_banknum.setText("尾号" + banknum);
                            if (bp == null) {
                                bp = new BankName_Pic();
                            }
                            Integer pic = bp.bank_Pic(bankid);
//					Integer pic = new BankName_Pic(bankid).bank_Pic();
                            iv_bank.setImageResource(pic);
                            if (istip == 1) {
                                tv_balance_n.setText("2.00");
                            } else {
                                tv_balance_n.setText("0.00");
                            }
                            if (i == 2) {
                                MobclickAgent.onEvent(CashOutActivity.this, "100023");
                                if ("0".equals(ispwd)) {
                                    pop = new TradePwdPopUtils();
                                    pop.setCallBackTradePwd(new TradePwdPopUtils.CallBackTradePwd() {

                                        @Override
                                        public void callBaceTradePwd(String pwd) {
                                            pop.tv_tips.setText("");
                                            if (!isfirstPwd) {
                                                firstPwd = pwd;
                                                isfirstPwd = true;
                                                pop.tv_pwd1.setText("✔");
                                                pop.tv_pwd_line.setBackgroundColor(Color.parseColor("#fb5b6d"));
                                            } else {
                                                secondPwd = pwd;
                                                if (firstPwd.equalsIgnoreCase(secondPwd)) {
                                                    pop.tv_pwd2.setBackgroundResource(R.drawable.bg_corner_red_yuan);
                                                    pop.tv_tips.setText("");
                                                    //去设置支付密码
                                                    sendFirstTpwdCode(pwd);
                                                } else {
                                                    pop.tv_tips.setText("您输入的确认密码和之前不一致");
                                                }
                                            }
                                        }
                                    });
                                    pop.showPopWindow(CashOutActivity.this, CashOutActivity.this, ll_all);
                                    if (istip == 1) {
                                        pop.tv_cashtip.setVisibility(View.VISIBLE);
                                    } else {
                                        pop.tv_cashtip.setVisibility(View.GONE);
                                    }
                                    pop.ll_invest_money.setVisibility(View.GONE);
                                    pop.ll_pwd_title.setVisibility(View.VISIBLE);
                                    pop.ll_pwd.setVisibility(View.VISIBLE);
                                    pop.iv_key_close.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            pop.popWindow.dismiss();
                                        }
                                    });
                                    pop.forget_pwd.setOnClickListener(new OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            pop.popWindow.dismiss();
                                            startActivity(new Intent(CashOutActivity.this, ForgetPswActivity.class).putExtra("isFrom", 1));
                                        }
                                    });
                                } else {
                                    pop = new TradePwdPopUtils();
                                    pop.setCallBackTradePwd(new TradePwdPopUtils.CallBackTradePwd() {

                                        @Override
                                        public void callBaceTradePwd(String pwd) {
                                            MobclickAgent.onEvent(CashOutActivity.this, UrlConfig.point + 31 + "");
                                            doCashOut(istip, pwd);
                                        }
                                    });
                                    pop.showPopWindow(CashOutActivity.this, CashOutActivity.this, ll_all);
                                    if (istip == 1) {
                                        pop.tv_cashtip.setVisibility(View.VISIBLE);
                                    } else {
                                        pop.tv_cashtip.setVisibility(View.GONE);
                                    }
                                    pop.tv_key_title.setText("提现(元)");
                                    pop.ll_invest_money.setVisibility(View.VISIBLE);
                                    pop.tv_key_money.setText(et_money.getText().toString());
                                    pop.iv_key_close.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            pop.popWindow.dismiss();
                                        }
                                    });
                                    pop.forget_pwd.setOnClickListener(new OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            pop.popWindow.dismiss();
                                            startActivity(new Intent(CashOutActivity.this, ForgetPswActivity.class).putExtra("isFrom", 1));
                                        }
                                    });
                                }
                            } else {
                                tv_balance_a.setText(StringCut.getNumKb(balance));
                            }
                        } else if ("9998".equals(obj.getString("errorCode"))) {
                            finish();
                            new show_Dialog_IsLogin(CashOutActivity.this).show_Is_Login();
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

    //设置交易密码
    private void sendFirstTpwdCode(final String pwd) {
        showWaitDialog("加载中...", false, "");
        OkHttpUtils.post().url(UrlConfig.UPDATETPWDBYSMS)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("tpwd", SecurityUtils.MD5AndSHA256(firstPwd))
//				.addParams("smsCode", code_et.getText().toString().trim())
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        setResult(5, new Intent());

                        dismissDialog();
                        pop.popWindow.dismiss();
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
                            ToastMaker.showShortToast("交易密码设置成功");
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("tpwdFlag", "1");
                            editor.commit();
                            MobclickAgent.onEvent(CashOutActivity.this, UrlConfig.point + 31 + "");
                            doCashOut(istip, pwd);
                        } else if ("1001".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("验证码错误");
                        } else if ("1002".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("密码为空");
                        } else if ("1003".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("交易密码不合法");
                        } else if ("9999".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("系统错误");
                        } else if ("9998".equals(obj.getString("errorCode"))) {
                            finish();
                            new show_Dialog_IsLogin(CashOutActivity.this).show_Is_Login();
                        } else {
                            ToastMaker.showShortToast("系统错误");
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
