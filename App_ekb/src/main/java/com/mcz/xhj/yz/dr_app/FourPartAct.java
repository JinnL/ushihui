package com.mcz.xhj.yz.dr_app;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mcz.xhj.R;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.umeng.analytics.MobclickAgent;
import com.mcz.xhj.yz.dr_adapter.RedListAdapter;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_bean.BankNameBean;
import com.mcz.xhj.yz.dr_bean.BankName_Pic;
import com.mcz.xhj.yz.dr_bean.CityNameBean;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.LogUtils;
import com.mcz.xhj.yz.dr_util.stringCut;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import okhttp3.Call;

/*
* 描述：实名认证界面
* */

public class FourPartAct extends BaseActivity {
    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_idcard)
    EditText et_idcard;
    @BindView(R.id.et_bankcard)
    EditText et_bankcard;
    @BindView(R.id.et_city)
    EditText et_city;
    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.et_msm)
    EditText et_msm;
    @BindView(R.id.tv_getcode)
    TextView tv_getcode;
    @BindView(R.id.tv_bankname)
    TextView tv_bankname;
    @BindView(R.id.bt_ok)
    Button bt_ok;
    @BindView(R.id.title_centertextview)
    TextView centertv;
    @BindView(R.id.title_leftimageview)
    ImageView leftima;
    @BindView(R.id.iv_xiane)
    ImageView iv_xiane;
    @BindView(R.id.iv_bank)
    ImageView iv_bank;
    @BindView(R.id.img_search)
    ImageView img_search;
    @BindView(R.id.title_righttextview)
    TextView righttext;
    @BindView(R.id.ll_chosebank)
    LinearLayout ll_chosebank;
    @BindView(R.id.ll_fourpartxieyi)
    LinearLayout ll_fourpartxieyi;

    private int count;
    private Boolean isRun;
    private int time = 1;
    private String city;
    private int cityId;
    private String cityName;

    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.act_fourpart;
    }

    Pattern idcard = Pattern.compile("^[0-9]{15}$|^[0-9]{17}[0-9xX]$");
    Pattern banknum = Pattern.compile("^[0-9]{16}$|^[0-9]{19}$|^[0-9]{18}$");
    Pattern phone = Pattern.compile("^1[3|4|5|7|8][0-9]{9}$");
    Long lastClick = 0l;
    private boolean isQudao = true;
    private String proName, proDeadline, proRate, proAmount, money, specialRate;
    /**
     * fromFlag标识从哪个界面过来的
     * Act_Detail_Pro 标的详情页
     */
    private String fromFlag = "";

    @Override
    protected void initParams() {
        centertv.setText("信息认证");
        et_phone.setText(preferences.getString("phone", ""));
        if (getIntent() != null) {
            if (getIntent().getStringExtra("flag") != null && getIntent().getStringExtra("flag").equalsIgnoreCase("zhuce")) {
                righttext.setVisibility(View.VISIBLE);
                righttext.setText("跳过");
                leftima.setVisibility(View.GONE);
            }
//			else if(getIntent().getStringExtra("flag")!=null&&getIntent().getStringExtra("flag").equalsIgnoreCase("cashin")){
//				fromFlag = "CashIn";
//				money = getIntent().getStringExtra("money");
//			}
            else if (getIntent().getStringExtra("proAmount") != null && !getIntent().getStringExtra("proAmount").equalsIgnoreCase("")) {
                fromFlag = "Act_Detail_Pro";
                proName = getIntent().getStringExtra("proName");
                proDeadline = getIntent().getStringExtra("proDeadline");
                proRate = getIntent().getStringExtra("proRate");
                specialRate = getIntent().getStringExtra("specialRate");
                proAmount = getIntent().getStringExtra("proAmount");
            }
//			else if(getIntent().getStringExtra("flag")!=null&&getIntent().getStringExtra("flag").equalsIgnoreCase("zhucequdao")){
//				isQudao = true;
//			}
        }
        leftima.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_bankname.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                getBankData();
            }
        });
        ll_fourpartxieyi.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FourPartAct.this, WebViewActivity.class).putExtra("URL", UrlConfig.ZHIFU).putExtra("TITLE", "支付认证协议"));
            }
        });
        righttext.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_xiane.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(FourPartAct.this, WebViewActivity.class)
                        .putExtra("URL", UrlConfig.XIANE)
                        .putExtra("TITLE", "银行限额")
                );
            }
        });
        // 手机号间隔
        Watcher watcher = new Watcher("phone");
        et_phone.addTextChangedListener(watcher);
        Watcher watcher1 = new Watcher("bankcard");
        et_bankcard.addTextChangedListener(watcher1);

        //查询城市
        img_search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cityName = et_city.getText().toString();
                java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("[a-zA-Z]+");
                java.util.regex.Matcher m = pattern.matcher(cityName);
                boolean isLetter = m.matches();
                if (isLetter) {
                    //输入城市字母，向后台获取城市列表
                    OkHttpUtils.post()
                            .url(UrlConfig.CITYNAMELIST)
                            .addParams("version", UrlConfig.version)
                            .addParams("channel", "2")
                            .addParam("pinyin", cityName)
                            .build().execute(new StringCallback() {
                        @Override
                        public void onResponse(String response) {
                            JSONObject obj = JSON.parseObject(response);
                            JSONObject objmap = obj.getJSONObject("map");
                            LogUtils.i("--->citynamelist " + obj);
                            JSONArray objrows = objmap.getJSONArray("cityList");
                            List<CityNameBean> cityList = JSON.parseArray(objrows.toJSONString(), CityNameBean.class);
                            //弹出城市选择列表的对话框
                            showPopupWindowCityList(cityList);

                        }

                        @Override
                        public void onError(Call call, Exception e) {
                            ToastMaker.showShortToast("获取城市列表失败，请检查网络");
                        }
                    });
                } else {
                    ToastMaker.showShortToast("请输入城市首字母查询！");
                }
            }
        });

        //timebutton和内部的混合会解决timebutton自身的不足
        tv_getcode.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(FourPartAct.this, UrlConfig.point + 9 + "");
                if ("".equalsIgnoreCase(et_name.getText().toString())) {
                    ToastMaker.showShortToast("姓名不能为空");
                } else if (!idcard.matcher(et_idcard.getText()).matches()) {
                    ToastMaker.showShortToast("请输入正确的身份证号码");
                } else if (stringCut.space_Cut(et_bankcard.getText().toString()).length() < 8) {
                    ToastMaker.showShortToast("请输入正确的银行卡号码");
                } else if (TextUtils.isEmpty(et_city.getText().toString())) {
                    ToastMaker.showShortToast("请选择城市");
                } else if (!phone.matcher(stringCut.space_Cut(et_phone.getText().toString())).matches()) {
                    ToastMaker.showShortToast("请输入正确的手机号码");
                } else {
                    sendRegMsg();
                }
            }
        });
        bt_ok.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(FourPartAct.this, UrlConfig.point + 10 + "");
                if (System.currentTimeMillis() - lastClick <= 1000) {
//					ToastMaker.showShortToast("点那么快干什么");
                    return;
                }
                lastClick = System.currentTimeMillis();
                if ("".equalsIgnoreCase(et_name.getText().toString())) {
                    ToastMaker.showShortToast("姓名不能为空");
                } else if (!idcard.matcher(et_idcard.getText()).matches()) {
                    ToastMaker.showShortToast("请输入正确的身份证号码");
                } else if (stringCut.space_Cut(et_bankcard.getText().toString()).length() < 8) {
                    ToastMaker.showShortToast("请输入正确的银行卡号码");
                } else if (cityId <= 0) {
                    ToastMaker.showShortToast("请重新查询城市");
                } else if (!phone.matcher(stringCut.space_Cut(et_phone.getText().toString())).matches()) {
                    ToastMaker.showShortToast("请输入正确的手机号码");
                } else if ("".equalsIgnoreCase(et_msm.getText().toString())) {
                    ToastMaker.showShortToast("请输入短信验证码");
                } else {
                    if (isResponse) {
                        showWaitDialog("请稍后...", false, "");
                        isResponse = false;
                        doFourPart();
                    } else {
                        ToastMaker.showShortToast("请勿重复提交");
                    }
                }
//					startActivity(new Intent(FourPartAct.this, Act_Fourpart_Cashin.class)
//							.putExtra("proName",proName)//产品名称
//							.putExtra("proDeadline",proDeadline)//投资期限
//							.putExtra("proRate",proRate)//预期年化收益
//							.putExtra("proAmount",proAmount));//投资金额

            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 4 && resultCode == 4) {
            setResult(4);
            finish();
        } else if (requestCode == 4 && resultCode == 3) {
            setResult(3);
            finish();
        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onEvent(FourPartAct.this, UrlConfig.point + 8 + "");
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void onButtonClicked(Dialog dialog, int position, Object tag) {
        super.onButtonClicked(dialog, position, tag);
        if (position == 1) {
            if (tag.equals("!s")) {
                startActivity(new Intent(this, Detail_Tiyan.class));
                finish();
            } else {
                LocalApplication.getInstance().getMainActivity().isInvest = true;
                LocalApplication.getInstance().getMainActivity().isInvestChecked = true;
                finish();
            }
        } else {
            if (tag.equals("s")) {
                startActivity(new Intent(this, ConponsAct.class));
                finish();
            } else {
                startActivity(new Intent(this, ConponsActTiyan.class));
                finish();
            }
        }
    }

    private List<BankNameBean> lslbs = new ArrayList<BankNameBean>();

    private void getBankData() {
        showWaitDialog("请稍后...", false, "");
        OkHttpUtils.post()
                .url(UrlConfig.BANKNAMELIST)
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {

            @Override
            public void onResponse(String result) {
                dialog.dismiss();
                JSONObject obj = JSON.parseObject(result);
                if (obj.getBoolean("success")) {
                    JSONObject objmap = obj.getJSONObject("map");
                    JSONArray objrows = objmap.getJSONArray("bankQuotaList");
                    lslbs = JSON.parseArray(objrows.toJSONString(), BankNameBean.class);
                    if (lslbs.size() > 0) {
                        showPopupWindowBankList(lslbs, 1, "");
                    }
                } else if ("9998".equals(obj.getString("errorCode"))) {
                    finish();
//					new show_Dialog_IsLogin(getActivity()).show_Is_Login() ;
                } else {
                    ToastMaker.showShortToast("服务器异常");
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                dialog.dismiss();
                ToastMaker.showShortToast("请检查网络");
            }
        });

    }


    private View layout;
    private PopupWindow popupWindow;
    private RedListAdapter rlAdapter;
    private String bankid = "";

    public void showPopupWindowBankList(final List<BankNameBean> lsrb, int money, String flag) {
        // 加载布局
        layout = LayoutInflater.from(this).inflate(R.layout.pop_chose_bank, null);
        // 找到布局的控件
        // 实例化popupWindow
        popupWindow = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        popupWindow.setContentView(layout);
        TextView iv_close = (TextView) (layout).findViewById(R.id.iv_close);
        ListView lv_bankname = (ListView) (layout).findViewById(R.id.lv_bankname);
        View view = (layout).findViewById(R.id.view);
        TextView tv_cash_out = (TextView) (layout).findViewById(R.id.tv_cash_out);
//		rlAdapter = new RedListAdapter(FourPartAct.this, lsrb);
        lv_bankname.setAdapter(new BankListAdapter(lsrb, ""));
        lv_bankname.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                bankid = lsrb.get(position).getId();
                tv_bankname.setText(lsrb.get(position).getBankName());
                if (bp == null) {
                    bp = new BankName_Pic();
                }
                if (bp == null) {
                    bp = new BankName_Pic();
                }
                Integer pic = bp.bank_Pic(lsrb.get(position).getId());
                iv_bank.setImageResource(pic);
                popupWindow.dismiss();
            }
        });
        iv_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        // 控制键盘是否可以获得焦点
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
//				popupWindow.setBackgroundDrawable(new PaintDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        iv_close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
//		popupWindow.showAsDropDown(layout);
        popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
    }


    public void showPopupWindowCityList(final List<CityNameBean> cityList) {
        // 加载布局
        layout = LayoutInflater.from(this).inflate(R.layout.pop_choose_city, null);
        // 找到布局的控件
        // 实例化popupWindow
        popupWindow = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        popupWindow.setContentView(layout);
        TextView tv_close = (TextView) (layout).findViewById(R.id.tv_close);
        ListView lv_bankname = (ListView) (layout).findViewById(R.id.lv_bankname);
        ArrayList<String> cityNameList = new ArrayList<>();
        for (CityNameBean cityNameBean : cityList) {
            cityNameList.add(cityNameBean.getCity());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(FourPartAct.this, android.R.layout.simple_list_item_1, cityNameList);
        lv_bankname.setAdapter(adapter);
        lv_bankname.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                city = cityList.get(position).getCity();
                cityId = cityList.get(position).getCityId();
                et_city.setText(city);
                et_city.setSelection(city.length());
                popupWindow.dismiss();
            }
        });
        tv_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        // 控制键盘是否可以获得焦点
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
//				popupWindow.setBackgroundDrawable(new PaintDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
//		popupWindow.showAsDropDown(layout);
        popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
    }

    private boolean isResponse = true;

    private void doFourPart() {
        OkHttpUtils
                .post()
                .url(UrlConfig.FOURPART)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("realName", stringCut.space_Cut(et_name.getText().toString().trim()))
                .addParams("idCards", stringCut.space_Cut(et_idcard.getText().toString().trim()))
                .addParams("bankNum", stringCut.space_Cut(et_bankcard.getText().toString().trim()))
                .addParams("cityId", cityId + "")
                .addParams("phone", stringCut.space_Cut(et_phone.getText().toString().trim()))
                .addParams("smsCode", stringCut.space_Cut(et_msm.getText().toString().trim()))
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
                        LogUtils.i("--->点击实名认证fourpart " + obj);
                        if (obj.getBoolean(("success"))) {
                            JSONObject objmap = obj.getJSONObject("map");
//					String isCps = objmap.getString("isCps");  // 0：非s用户，1：s用户
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("realVerify", "1");
                            editor.commit();
//					if(isQudao){
//						if(isCps.equalsIgnoreCase("1")){
//							showAlertDialog("认证成功","您共获得15888元体验金和388元现金红包", new String[] { "查看", "去赚钱" }, false, false, "s");
//						}else if(isCps.equalsIgnoreCase("0")){
//							showAlertDialog("认证成功","您共获得15888元体验金和388元现金红包", new String[] { "查看", "去赚钱" }, false, false, "!s");
//						}
//					}else{
                            ToastMaker.showShortToast("认证成功!");
                            if (fromFlag.equalsIgnoreCase("Act_Detail_Pro")) {
                                startActivityForResult(new Intent(FourPartAct.this, Act_Fourpart_Cashin.class)
                                        .putExtra("proName", proName)//产品名称
                                        .putExtra("proDeadline", proDeadline)//投资期限
                                        .putExtra("proRate", proRate)//预期年化收益
                                        .putExtra("specialRate", specialRate)//预期年化收益
                                        .putExtra("proAmount", proAmount), 4);//投资金额
                            } else {
                                finish();
                            }
//						else if(fromFlag.equalsIgnoreCase("money")){
//							doCashInNext();
//						}
                        } else {
                            switch (Integer.parseInt(obj.getString(("errorCode")))) {
                                case 9999:
                                    ToastMaker.showShortToast("系统错误!");
                                    startActivityForResult(new Intent(FourPartAct.this, CashIn_End.class).putExtra("msgCode", "9999").putExtra("flag", "failiure_fourpart").putExtra("msg", "系统错误!"), 4);
                                    break;
                                case 1001:
                                    startActivityForResult(new Intent(FourPartAct.this, CashIn_End.class).putExtra("msgCode", "1001").putExtra("flag", "failiure_fourpart").putExtra("msg", "真实姓名不能为空!"), 4);
                                    break;
                                case 1002:
                                    startActivityForResult(new Intent(FourPartAct.this, CashIn_End.class).putExtra("msgCode", "1002").putExtra("flag", "failiure_fourpart").putExtra("msg", "身份证卡不能为空!"), 4);
                                    break;
                                case 1003:
                                    startActivityForResult(new Intent(FourPartAct.this, CashIn_End.class).putExtra("msgCode", "1003").putExtra("flag", "failiure_fourpart").putExtra("msg", "银行卡号不能为空!"), 4);
                                    break;
                                case 1004:
                                    startActivityForResult(new Intent(FourPartAct.this, CashIn_End.class).putExtra("msgCode", "1004").putExtra("flag", "failiure_fourpart").putExtra("msg", "手机号码不能为空!"), 4);
                                    break;
                                case 1005:
                                    startActivityForResult(new Intent(FourPartAct.this, CashIn_End.class).putExtra("msgCode", "1005").putExtra("flag", "failiure_fourpart").putExtra("msg", "短信验证码不能为空!"), 4);
                                    break;
                                case 1006:
                                    startActivityForResult(new Intent(FourPartAct.this, CashIn_End.class).putExtra("msgCode", "1006").putExtra("flag", "failiure_fourpart").putExtra("msg", "短信验证码错误!"), 4);
                                    break;
                                case 1007:
                                    startActivityForResult(new Intent(FourPartAct.this, CashIn_End.class).putExtra("msgCode", "1007").putExtra("flag", "failiure_fourpart").putExtra("msg", "银行卡类型不符，请更换银行卡后重试!"), 4);
                                    break;
                                case 1008:
                                    startActivityForResult(new Intent(FourPartAct.this, CashIn_End.class).putExtra("msgCode", "1008").putExtra("flag", "failiure_fourpart").putExtra("msg", "此卡未开通银联在线支付功能,实名认证失败，请联系发卡银行!"), 4);
                                    break;
                                case 1009:
                                    startActivityForResult(new Intent(FourPartAct.this, CashIn_End.class).putExtra("msgCode", "1009").putExtra("flag", "failiure_fourpart").putExtra("msg", "不支持此银行卡的验证!"), 4);
                                    break;
                                case 1010:
                                    startActivityForResult(new Intent(FourPartAct.this, CashIn_End.class).putExtra("msgCode", "1010").putExtra("flag", "failiure_fourpart").putExtra("msg", "免费验证次数已用完，请联系客服人员解决"), 4);
                                    break;
                                case 1011:
                                    startActivityForResult(new Intent(FourPartAct.this, CashIn_End.class).putExtra("msgCode", "1011").putExtra("flag", "failiure_fourpart").putExtra("msg", "认证失败!"), 4);
                                    break;
                                case 1012:
                                    startActivityForResult(new Intent(FourPartAct.this, CashIn_End.class).putExtra("msgCode", "1012").putExtra("flag", "failiure_fourpart").putExtra("msg", "该身份证号已认证!"), 4);
                                    break;
                                case 1013:
                                    startActivityForResult(new Intent(FourPartAct.this, CashIn_End.class).putExtra("msgCode", "1013").putExtra("flag", "failiure_fourpart").putExtra("msg", "渠道不能为空!"), 4);
                                    break;
                                case 1014:
                                    startActivityForResult(new Intent(FourPartAct.this, CashIn_End.class).putExtra("msgCode", "1014").putExtra("flag", "failiure_fourpart").putExtra("msg", "请核对个人信息!"), 4);
                                    break;
                                case 1015:
                                    startActivityForResult(new Intent(FourPartAct.this, CashIn_End.class).putExtra("msgCode", "1015").putExtra("flag", "failiure_fourpart").putExtra("msg", "请核对银行卡信息!"), 4);
                                    break;
                                case 1016:
                                    startActivityForResult(new Intent(FourPartAct.this, CashIn_End.class).putExtra("msgCode", "1016").putExtra("flag", "failiure_fourpart").putExtra("msg", "该银行卡bin不支持!"), 4);
                                    break;
                                case 1017:
                                    startActivityForResult(new Intent(FourPartAct.this, CashIn_End.class).putExtra("msgCode", "1017").putExtra("flag", "failiure_fourpart").putExtra("msg", "认证失败，系统异常请稍后再试!"), 4);
                                    break;

                                default:
                                    ToastMaker.showShortToast("系统错误!");
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

//	private String payNum = "";
//	private void doCashInNext() {
//		OkHttpUtils
//				.post()
//				.url(UrlConfig.CASHINNEXT)
//				.addParams("uid", preferences.getString("uid", ""))
//				.addParams("amount", stringCut.space_Cut(money))
////				.addParams("smsCode", stringCut.space_Cut(code_et.getText().toString()))
//				.addParams("version", UrlConfig.version)
//				.addParams("channel", "2")
//				.build()
//				.execute(new StringCallback() {
//					@Override
//					public void onResponse(String response) {
//						isResponse = true;
//						dismissDialog();
//						// TODO Auto-generated method stub
//						JSONObject obj = JSON.parseObject(response);
//						if (obj.getBoolean(("success"))) {
//							JSONObject objmap = obj.getJSONObject("map");
//							payNum = objmap.getString("payNum");
//							startActivityForResult(new Intent(FourPartAct.this,Act_SendMsg.class)
//									.putExtra("payNum",payNum)
//									.putExtra("amount",stringCut.space_Cut(money)),4
//							);
//						} else {
//							switch (Integer.parseInt(obj.getString(("errorCode")))) {
//								case 9999:
//									ToastMaker.showShortToast("系统错误");
//									break;
//								case 1001:
//									ToastMaker.showShortToast("充值金额有误");
//									break;
//								case 1002:
//									ToastMaker.showShortToast("系统错误，请稍后重试");
//									break;
//								case 1003:
//									ToastMaker.showShortToast("超过限额，请修改金额后重试");
//									break;
//								case 9998:
//									finish();
//									new show_Dialog_IsLogin(FourPartAct.this).show_Is_Login();
//									break;
//								default:
//									break;
//							}
//						}
//					}
//
//					@Override
//					public void onError(Call call, Exception e) {
//						isResponse = true;
//						dismissDialog();
//						ToastMaker.showShortToast("请检查网络!");
//
//					}
//				});
//
//	}

    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;

    //获取手机验证码
    private void sendRegMsg() {
        OkHttpUtils
                .post()
                .url(UrlConfig.BANKCARDMSG)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("bankNum", stringCut.space_Cut(et_bankcard.getText().toString().trim()).substring(stringCut.space_Cut(et_bankcard.getText().toString().trim()).length() - 4))
                .addParams("mobilePhone", stringCut.space_Cut(et_phone.getText().toString().trim()))
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        LogUtils.i("------>bankcardmsg " + response);
                        // TODO Auto-generated method stub
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
                            ToastMaker.showShortToast("验证码已发送");
                            time();
                        } else {
                            if ("1002".equals(obj.getString("errorCode"))) {
                                ToastMaker.showShortToast("当天短信发送超过限制");
                                stopTimer();
                            } else if ("1001".equals(obj.getString("errorCode"))) {
                                ToastMaker.showShortToast("手机号码有误");
                                stopTimer();
                            } else if ("1003".equals(obj.getString("errorCode"))) {
                                ToastMaker.showShortToast("短信发送失败");
                                stopTimer();
                            } else if ("1111".equals(obj.getString("errorCode"))) {
                                ToastMaker.showShortToast("手机号码被锁，请联系客服");
                                stopTimer();
                            } else if ("9999".equals(obj.getString("errorCode"))) {
                                ToastMaker.showShortToast("系统异常");
                                stopTimer();
                            } else {
                                ToastMaker.showShortToast("系统异常");
                                stopTimer();
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
                .url(UrlConfig.BANKCARDMSG)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("type", "2")
                .addParams("bankNum", et_bankcard.getText().toString().substring(et_bankcard.getText().toString().length() - 4))
                .addParams("mobilePhone", stringCut.space_Cut(et_phone.getText().toString().trim()))
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
                                ToastMaker.showShortToast("每个手机号当天只能发送6条");
                            } else if ("8888".equals(obj.getString("errorCode"))) {
                                ToastMaker.showShortToast("操作频繁请您稍后再试");
                            } else if ("1001".equals(obj.getString("errorCode"))) {
                                ToastMaker.showShortToast("手机号码有误");
                            } else if ("1003".equals(obj.getString("errorCode"))) {
                                ToastMaker.showShortToast("短信发送失败");
                            } else if ("9999".equals(obj.getString("errorCode"))) {
                                ToastMaker.showShortToast("系统异常");
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

    //手机号码间隔
    class Watcher implements TextWatcher {
        int beforeTextLength = 0;
        int onTextLength = 0;
        boolean isChanged = false;

        int location = 0;// 记录光标的位置
        private char[] tempChar;
        private StringBuffer buffer = new StringBuffer();
        int konggeNumberB = 0;
        private String flag = "";

        public Watcher(String flag) {
            super();
            this.flag = flag;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            onTextLength = s.length();
            buffer.append(s.toString());
            if (onTextLength == beforeTextLength || isChanged) {
                isChanged = false;
                return;
            }
            isChanged = true;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub
            beforeTextLength = s.length();
            if (buffer.length() > 0) {
                buffer.delete(0, buffer.length());
            }
            konggeNumberB = 0;
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == ' ') {
                    konggeNumberB++;
                }
            }

        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            if (isChanged) {
                if (flag.equalsIgnoreCase("bankcard")) {
                    location = et_bankcard.getSelectionEnd();
                } else {
                    location = et_phone.getSelectionEnd();
                }
                int index = 0;
                while (index < buffer.length()) {
                    if (buffer.charAt(index) == ' ') {
                        buffer.deleteCharAt(index);
                    } else {
                        index++;
                    }
                }

                index = 0;
                int konggeNumberC = 0;
                while (index < buffer.length()) {
                    if (flag.equalsIgnoreCase("bankcard")) {
                        if ((index == 4 || index == 9 || index == 14 || index == 19)) {
                            buffer.insert(index, ' ');
                            konggeNumberC++;
                        }
                    } else {
                        if ((index == 3 || index == 8)) {
                            buffer.insert(index, ' ');
                            konggeNumberC++;
                        }
                    }
                    index++;
                }
                if (konggeNumberC > konggeNumberB) {
                    location += (konggeNumberC - konggeNumberB);
                }

                tempChar = new char[buffer.length()];
                buffer.getChars(0, buffer.length(), tempChar, 0);
                String str = buffer.toString();
                if (location > str.length()) {
                    location = str.length();
                } else if (location < 0) {
                    location = 0;
                }
                if (flag.equalsIgnoreCase("bankcard")) {
                    et_bankcard.setText(str);
                    Editable etable = et_bankcard.getText();
                    Selection.setSelection(etable, location);
                } else {
                    et_phone.setText(str);
                    Editable etable = et_phone.getText();
                    Selection.setSelection(etable, location);
                }
                isChanged = false;
            }
        }
    }

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
        tv_getcode.setEnabled(true);
        tv_getcode.setText("发送验证码");
        tv_getcode.setBackgroundResource(R.drawable.bg_corner_kong_blue);
        tv_getcode.setTextColor(0xFF20A3F9);
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
                        tv_getcode.setEnabled(false);
                        tv_getcode.setTextColor(0xffcccccc);
                        tv_getcode.setBackgroundResource(R.drawable.bg_corner_blackline);
//						tv_getcode.setBackgroundColor(Color.parseColor("#b5b5b5"));
                        tv_getcode.setText("发送(" + count + ")秒");
                    }

                    break;
                default:
                    break;
            }
        }
    };

    private BankName_Pic bp;

    class BankListAdapter extends BaseAdapter {
        private List<BankNameBean> lsct;
        private LayoutInflater inflater;
        private String url = "";

        public BankListAdapter(List<BankNameBean> lsct, String url) {
            this.lsct = lsct;
            this.url = url;
            inflater = LayoutInflater.from(FourPartAct.this);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return lsct.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return lsct.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        private class ViewHolder {
            private ImageView iv_bankpic;
            private ImageView image_checked;
            private TextView tv_bankname;
            private TextView tv_xiane;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_bankname, null);
                vh = new ViewHolder();
                vh.iv_bankpic = (ImageView) convertView.findViewById(R.id.iv_bankpic);
                vh.image_checked = (ImageView) convertView.findViewById(R.id.image_checked);
                vh.tv_bankname = (TextView) convertView.findViewById(R.id.tv_bankname);
                vh.tv_xiane = (TextView) convertView.findViewById(R.id.tv_xiane);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            BankNameBean bb = lsct.get(position);
//			Integer pic = new BankNameAdd_Pic(bb.getId()).bank_Pic();
//			vh.iv_bankpic.setImageResource(pic);
            if (bp == null) {
                bp = new BankName_Pic();
            }
            Integer pic = bp.bank_Pic(bb.getId());
            vh.iv_bankpic.setImageResource(pic);
            vh.tv_bankname.setText(bb.getBankName());
            vh.tv_xiane.setText(bb.getSingleQuota() / 10000 + "万/" + bb.getDayQuota() / 10000 + "万");
            if (bb.getId().equalsIgnoreCase(bankid)) {
                vh.image_checked.setVisibility(View.VISIBLE);
            } else {
                vh.image_checked.setVisibility(View.GONE);
            }
            return convertView;
        }
    }

}
