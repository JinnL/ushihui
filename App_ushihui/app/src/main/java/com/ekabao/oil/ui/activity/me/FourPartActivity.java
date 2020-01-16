package com.ekabao.oil.ui.activity.me;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ekabao.oil.R;
import com.ekabao.oil.bean.BankNameBean;
import com.ekabao.oil.bean.CityNameBean;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.ui.activity.BaseActivity;
import com.ekabao.oil.ui.activity.WebViewActivity;
import com.ekabao.oil.ui.view.CityPick.CityPicker;
import com.ekabao.oil.ui.view.ToastMaker;
import com.ekabao.oil.ui.view.WheelRecyclerView;
import com.ekabao.oil.util.LogUtils;
import com.ekabao.oil.util.StringCut;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * $desc$
 * 四要素
 *
 * @time $data$ $time$
 * Created by Administrator on 2018/7/17.
 */

public class FourPartActivity extends BaseActivity {
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
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.tv_idcard)
    TextView tvIdcard;
    @BindView(R.id.iv_bank)
    ImageView ivBank;
    @BindView(R.id.ll_chosebank)
    LinearLayout llChosebank;
    @BindView(R.id.et_city)
    EditText etCity;
    @BindView(R.id.img_search)
    ImageView imgSearch;
    @BindView(R.id.ll_select_city)
    LinearLayout llSelectCity;
    @BindView(R.id.et_bankcard)
    EditText etBankcard;
    @BindView(R.id.et_bankName)
    EditText etBankName;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_msm)
    EditText etMsm;
    @BindView(R.id.tv_getcode)
    TextView tvGetcode;
    @BindView(R.id.tv_yuying)
    TextView tvYuying;
    @BindView(R.id.bt_ok)
    Button btOk;
    @BindView(R.id.ll_fourpartxieyi)
    LinearLayout llFourpartxieyi;
    @BindView(R.id.tv_bank_name)
    TextView tvBankName;
    @BindView(R.id.tv_city_name)
    TextView tvCityName;

    private String name;
    private String idcard;
    private CityPicker mCityPicker;
    PopupWindow popupWindow;
    private List<String> lslbs = new ArrayList<String>();
    private String bankName;

    private int cityId;
    private int count;
    private Boolean isRun;
    private int time = 1;
    Long lastClick = 0L;
    private boolean isResponse = true;
    //Pattern idcard = Pattern.compile("^[0-9]{15}$|^[0-9]{17}[0-9xX]$");
    Pattern banknum = Pattern.compile("^[0-9]{16}$|^[0-9]{19}$|^[0-9]{18}$");
    Pattern phone = Pattern.compile(LocalApplication.context.getResources().getString(R.string.pPhone));

    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
    /**
     * fromFlag标识从哪个界面过来的
     * Act_Detail_Pro 标的详情页
     */
    private String fromFlag = "";
    private String proName, proDeadline, proRate, proAmount, money, specialRate;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_fourpart;
    }

    @Override
    protected void initParams() {

        titleCentertextview.setText("加密绑卡");
        titleRighttextview.setVisibility(View.VISIBLE);
        titleRighttextview.setText("限额列表");
        titleRighttextview.setTextColor(getResources().getColor(R.color.text_blue));

        if (getIntent() != null) {
            if (getIntent().getStringExtra("flag") != null
                    && getIntent().getStringExtra("flag").equalsIgnoreCase("zhuce")) {
                /*righttext.setVisibility(View.VISIBLE);
                righttext.setText("跳过");
                leftima.setVisibility(View.GONE);*/
            }
//			else if(getIntent().getStringExtra("flag")!=null&&getIntent().getStringExtra("flag").equalsIgnoreCase("cashin")){
//				fromFlag = "CashIn";
//				money = getIntent().getStringExtra("money");
//			}
            else if (getIntent().getStringExtra("proAmount") != null
                    && !getIntent().getStringExtra("proAmount").equalsIgnoreCase("")) {
                fromFlag = "Act_Detail_Pro";
                proName = getIntent().getStringExtra("proName");
                proDeadline = getIntent().getStringExtra("proDeadline");
                proRate = getIntent().getStringExtra("proRate");
                specialRate = getIntent().getStringExtra("specialRate");
                proAmount = getIntent().getStringExtra("proAmount");
            }

        }

        Intent intent = getIntent();
        name = intent.getStringExtra("name");

        idcard = intent.getStringExtra("idcard");

        tvName.setText(name);

        tvIdcard.setText(idcard);

        etBankcard.setHint(setSpannable("输入银行卡号", 15));
        etPhone.setHint(setSpannable("输入预留手机号", 15));
        etMsm.setHint(setSpannable("输入短信验证码", 15));
        // 手机号间隔
        Watcher watcher = new Watcher("phone");
        etPhone.addTextChangedListener(watcher);
        Watcher watcher1 = new Watcher("bankcard");
        etBankcard.addTextChangedListener(watcher1);


        getBankData();


    }

    @OnClick({R.id.title_leftimageview, R.id.title_righttextview, R.id.tv_getcode,
            R.id.ll_chosebank, R.id.ll_select_city, R.id.bt_ok,R.id.ll_fourpartxieyi})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_leftimageview:
                setResult(Activity.RESULT_CANCELED);
                finish();
                break;

            case R.id.title_righttextview: //限额列表
                startActivity(new Intent(FourPartActivity.this, BankLimitActivity.class));
                break;
            case R.id.ll_chosebank: //选择银行卡
                showPopupWindowBankList(lslbs);
                break;
            case R.id.ll_fourpartxieyi: //认证协议
                startActivity(new Intent(FourPartActivity.this, WebViewActivity.class)
                        .putExtra("URL", UrlConfig.ZHIFU)
                        .putExtra("TITLE", "认证支付协议"));
                break;


            case R.id.ll_select_city:
                if (mCityPicker == null) {
                    mCityPicker = new CityPicker(this, findViewById(R.id.ll_select_city))
                            .setOnCitySelectListener(new CityPicker.OnCitySelectListener() {

                                @Override
                                public void onCitySelect(String province, int pid, String city, int cityid, String county, int cid) {
//                                    tvAddress.setText(province + city + county);
//                                    provinceid = pid;
//                                    cityId = cityid;
//                                    areaid = cid;

                                    getCityId(city.replace("市", ""));
                                    tvCityName.setText(city);

                                }
                            });
                }
                mCityPicker.show();
                break;

            case R.id.tv_getcode: //选择银行卡
                MobclickAgent.onEvent(this, UrlConfig.point + 9 + "");
                if (StringCut.space_Cut(etBankcard.getText().toString()).length() < 8) {
                    ToastMaker.showShortToast("请输入正确的银行卡号码");
                } else if (cityId == 0) {
                    ToastMaker.showShortToast("请选择城市");
                } else if (!phone.matcher(StringCut.space_Cut(etPhone.getText().toString())).matches()) {
                    ToastMaker.showShortToast("请输入正确的手机号码");
                } else {
                    sendRegMsg();
                }
                break;

            case R.id.bt_ok: //
                MobclickAgent.onEvent(this, UrlConfig.point + 10 + "");
                if (System.currentTimeMillis() - lastClick <= 1000) {
//					ToastMaker.showShortToast("点那么快干什么");
                    return;
                }
                lastClick = System.currentTimeMillis();
                if (StringCut.space_Cut(etBankcard.getText().toString()).length() < 8) {
                    ToastMaker.showShortToast("请输入正确的银行卡号码");
                } else if (cityId <= 0) {
                    ToastMaker.showShortToast("请重新查询城市");
                } else if (!phone.matcher(StringCut.space_Cut(etPhone.getText().toString())).matches()) {
                    ToastMaker.showShortToast("请输入正确的手机号码");
                } else if ("".equalsIgnoreCase(etMsm.getText().toString())) {
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
                break;

        }
    }

    /**
     * 实名认证 四要素
     */
    private void doFourPart() {
        OkHttpUtils
                .post()
                .url(UrlConfig.FOURPART)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("realName", name)
                .addParams("idCards", idcard)
                .addParams("bankNum", StringCut.space_Cut(etBankcard.getText().toString().trim()))
                .addParams("cityId", cityId + "")
                .addParams("phone", StringCut.space_Cut(etPhone.getText().toString().trim()))
                .addParams("smsCode", StringCut.space_Cut(etMsm.getText().toString().trim()))
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String response) {
                        isResponse = true;
                        dismissDialog();

                        JSONObject obj = JSON.parseObject(response);
                        LogUtils.e("--->点击实名认证fourpart " + obj);
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

                            setResult(Activity.RESULT_OK);
                            finish();

                           /* if (fromFlag.equalsIgnoreCase("Act_Detail_Pro")) {

                                startActivityForResult(new Intent(FourPartActivity.this, FourPartCashinActivity.class)
                                        .putExtra("proName", proName)//产品名称
                                        .putExtra("proDeadline", proDeadline)//出借期限
                                        .putExtra("proRate", proRate)//预期年化收益
                                        .putExtra("specialRate", specialRate)//预期年化收益
                                        .putExtra("proAmount", proAmount), 4);//出借金额
                            } else {
                                finish();
                            }*/
//						else if(fromFlag.equalsIgnoreCase("money")){
//							doCashInNext();
//						}
                        } else {
                            setResult(Activity.RESULT_CANCELED);

                            switch (Integer.parseInt(obj.getString(("errorCode")))) {
                                case 9999:
                                    ToastMaker.showShortToast("系统错误!");
                                    //startActivityForResult(new Intent(FourPartActivity.this, CashInEndActivity.class).putExtra("msgCode", "9999").putExtra("flag", "failiure_fourpart").putExtra("msg", "系统错误!"), 4);
                                    break;
                                case 1001:
                                   // startActivityForResult(new Intent(FourPartActivity.this, CashInEndActivity.class).putExtra("msgCode", "1001").putExtra("flag", "failiure_fourpart").putExtra("msg", "真实姓名不能为空!"), 4);
                                    ToastMaker.showLongToast("真实姓名不能为空!");
                                    break;
                                case 1002:
                                   // startActivityForResult(new Intent(FourPartActivity.this, CashInEndActivity.class).putExtra("msgCode", "1002").putExtra("flag", "failiure_fourpart").putExtra("msg", "身份证卡不能为空!"), 4);
                                    ToastMaker.showLongToast("身份证卡不能为空!");
                                    break;
                                case 1003:
                                    //startActivityForResult(new Intent(FourPartActivity.this, CashInEndActivity.class).putExtra("msgCode", "1003").putExtra("flag", "failiure_fourpart").putExtra("msg", "银行卡号不能为空!"), 4);
                                    ToastMaker.showLongToast("银行卡号不能为空!");
                                    break;
                                case 1004:
                                    //startActivityForResult(new Intent(FourPartActivity.this, CashInEndActivity.class).putExtra("msgCode", "1004").putExtra("flag", "failiure_fourpart").putExtra("msg", "手机号码不能为空!"), 4);
                                    ToastMaker.showLongToast("手机号码不能为空!");
                                    break;
                                case 1005:
                                   // startActivityForResult(new Intent(FourPartActivity.this, CashInEndActivity.class).putExtra("msgCode", "1005").putExtra("flag", "failiure_fourpart").putExtra("msg", "短信验证码不能为空!"), 4);
                                    ToastMaker.showLongToast("短信验证码不能为空!");
                                    break;
                                case 1006:
                                    //startActivityForResult(new Intent(FourPartActivity.this, CashInEndActivity.class).putExtra("msgCode", "1006").putExtra("flag", "failiure_fourpart").putExtra("msg", "短信验证码错误!"), 4);
                                    ToastMaker.showLongToast("短信验证码错误!");
                                    break;
                                case 1007:
                                    //startActivityForResult(new Intent(FourPartActivity.this, CashInEndActivity.class).putExtra("msgCode", "1007").putExtra("flag", "failiure_fourpart").putExtra("msg", "银行卡类型不符，请更换银行卡后重试!"), 4);
                                    ToastMaker.showLongToast("银行卡类型不符，请更换银行卡后重试!");
                                    break;
                                case 1008:
                                    //startActivityForResult(new Intent(FourPartActivity.this, CashInEndActivity.class).putExtra("msgCode", "1008").putExtra("flag", "failiure_fourpart").putExtra("msg", "此卡未开通银联在线支付功能,实名认证失败，请联系发卡银行!"), 4);
                                    ToastMaker.showLongToast("此卡未开通银联在线支付功能,实名认证失败，请联系发卡银行!");
                                    break;
                                case 1009:
                                    //startActivityForResult(new Intent(FourPartActivity.this, CashInEndActivity.class).putExtra("msgCode", "1009").putExtra("flag", "failiure_fourpart").putExtra("msg", "不支持此银行卡的验证!"), 4);
                                    ToastMaker.showLongToast("不支持此银行卡的验证!");
                                    break;
                                case 1010:
                                    //startActivityForResult(new Intent(FourPartActivity.this, CashInEndActivity.class).putExtra("msgCode", "1010").putExtra("flag", "failiure_fourpart").putExtra("msg", "免费验证次数已用完，请联系客服人员解决"), 4);
                                    ToastMaker.showLongToast("免费验证次数已用完，请联系客服人员解决");
                                    break;
                                case 1011:
                                   // startActivityForResult(new Intent(FourPartActivity.this, CashInEndActivity.class).putExtra("msgCode", "1011").putExtra("flag", "failiure_fourpart").putExtra("msg", "认证失败!"), 4);
                                    ToastMaker.showLongToast("认证失败!");
                                    break;
                                case 1012:
                                    //startActivityForResult(new Intent(FourPartActivity.this, CashInEndActivity.class).putExtra("msgCode", "1012").putExtra("flag", "failiure_fourpart").putExtra("msg", "该身份证号已认证!"), 4);
                                    ToastMaker.showLongToast("该身份证号已认证!");
                                    break;
                                case 1013:
                                    //startActivityForResult(new Intent(FourPartActivity.this, CashInEndActivity.class).putExtra("msgCode", "1013").putExtra("flag", "failiure_fourpart").putExtra("msg", "渠道不能为空!"), 4);
                                    ToastMaker.showLongToast("渠道不能为空!");
                                    break;
                                case 1014:
                                    //startActivityForResult(new Intent(FourPartActivity.this, CashInEndActivity.class).putExtra("msgCode", "1014").putExtra("flag", "failiure_fourpart").putExtra("msg", "请核对个人信息!"), 4);
                                    ToastMaker.showLongToast("请核对个人信息!");
                                    break;
                                case 1015:
                                    //startActivityForResult(new Intent(FourPartActivity.this, CashInEndActivity.class).putExtra("msgCode", "1015").putExtra("flag", "failiure_fourpart").putExtra("msg", "请核对银行卡信息!"), 4);
                                    ToastMaker.showLongToast("请核对银行卡信息!");
                                    break;
                                case 1016:
                                    //startActivityForResult(new Intent(FourPartActivity.this, CashInEndActivity.class).putExtra("msgCode", "1016").putExtra("flag", "failiure_fourpart").putExtra("msg", "该银行卡bin不支持!"), 4);
                                    ToastMaker.showLongToast("该银行卡bin不支持!");
                                    break;

                                case 1017:
                                    //startActivityForResult(new Intent(FourPartActivity.this, CashInEndActivity.class).putExtra("msgCode", "1017").putExtra("flag", "failiure_fourpart").putExtra("msg", "认证失败，系统异常请稍后再试!"), 4);
                                    ToastMaker.showLongToast("认证失败，系统异常请稍后再试!");
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

    /**
     * 获取手机验证码
     */
    private void sendRegMsg() {
        OkHttpUtils
                .post()
                .url(UrlConfig.BANKCARDMSG)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("bankNum", StringCut.space_Cut(etBankcard.getText().toString().trim()).substring(StringCut.space_Cut(etBankcard.getText().toString().trim()).length() - 4))
                .addParams("mobilePhone", StringCut.space_Cut(etPhone.getText().toString().trim()))
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        LogUtils.i("------>bankcardmsg " + response);

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

    /**
     * 选择银行
     */
    public void showPopupWindowBankList(final List<String> lsrb) {
        // 加载布局
        View layout = LayoutInflater.from(FourPartActivity.this).inflate(R.layout.pop_select_bank, null);
        // 找到布局的控件
        // 实例化popupWindow
        popupWindow = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setContentView(layout);

        final WheelRecyclerView viewById = (WheelRecyclerView) layout.findViewById(R.id.wrv_bank);
        TextView tv_exit = (TextView) (layout).findViewById(R.id.tv_exit);
        TextView tv_ok = (TextView) (layout).findViewById(R.id.tv_ok);
        viewById.setData(lsrb);

        viewById.setOnSelectListener(new WheelRecyclerView.OnSelectListener() {
            @Override
            public void onSelect(int position, String data) {
                LogUtils.e("position" + position + "/" + data);

            }
        });
        popupWindow.setAnimationStyle(R.style.CityPickerAnim);
        setBackgroundAlpha(0.4f);//设置屏幕透明度
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // popupWindow隐藏时恢复屏幕正常透明度
                setBackgroundAlpha(1.0f);
            }
        });
        tv_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        // 控制键盘是否可以获得焦点
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //	popupWindow.setBackgroundDrawable(new PaintDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        tv_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                popupWindow.dismiss();

                String s = lsrb.get(viewById.getSelected());
                bankName = s;
                tvBankName.setText(s);

                /*Drawable drawableLeft = getResources().getDrawable(
                        R.drawable.ic_launcher);

                textDrawable.setCompoundDrawablesWithIntrinsicBounds(drawableLeft,
                        null, null, null);
                textDrawable.setCompoundDrawablePadding(4);*/
                //getBankId(s);
                // ToastUtil.showToast(bankName);
            }
        });
//		popupWindow.showAsDropDown(layout);
        popupWindow.showAtLocation(layout, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 获取支持的银行列表
     */
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
                    // List<String> bankNames = JSON.parseArray(objrows.toJSONString(), String.class);
                    List<BankNameBean> bankNames = JSON.parseArray(objrows.toJSONString(), BankNameBean.class);

                    if (bankNames.size() > 0) {

                        for (BankNameBean bank : bankNames) {
                            lslbs.add(bank.getBankName());
                        }
//                        lslbs.clear();
//                        lslbs.addAll(bankNames);

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

    /**
     * 获取城市id
     */
    private int getCityId(String name) {
        //输入城市字母，向后台获取城市列表
        OkHttpUtils.post()
                .url(UrlConfig.CITYNAMELIST)
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .addParam("cityName", name)
                .build().execute(new StringCallback() {
            @Override
            public void onResponse(String response) {
                JSONObject obj = JSON.parseObject(response);
                JSONObject objmap = obj.getJSONObject("map");
                LogUtils.i("--->获取城市id " + obj);
                JSONArray objrows = objmap.getJSONArray("cityList");
                List<CityNameBean> cityList = JSON.parseArray(objrows.toJSONString(), CityNameBean.class);
                //弹出城市选择列表的对话框
                // showPopupWindowCityList(cityList);
                cityId = cityList.get(0).getCityId();

            }

            @Override
            public void onError(Call call, Exception e) {
                ToastMaker.showShortToast("获取城市列表失败，请检查网络");
                cityId = 0;
            }
        });
        return cityId;
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

            if (isChanged) {
                if (flag.equalsIgnoreCase("bankcard")) {
                    location = etBankcard.getSelectionEnd();
                } else {
                    location = etPhone.getSelectionEnd();
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
                    etBankcard.setText(str);
                    Editable etable = etBankcard.getText();
                    Selection.setSelection(etable, location);
                } else {
                    etPhone.setText(str);
                    Editable etable = etPhone.getText();
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
        tvGetcode.setEnabled(true);
        tvGetcode.setText("发送验证码");
        // tvGetcode.setBackgroundResource(R.drawable.bg_corner_kong_blue);
        tvGetcode.setTextColor(getResources().getColor(R.color.sms));
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
                        tvGetcode.setEnabled(false);
                        tvGetcode.setTextColor(getResources().getColor(R.color.sms));
                        //tvGetcode.setBackgroundResource(R.drawable.bg_corner_blackline);
//						tvGetcode.setBackgroundColor(Color.parseColor("#b5b5b5"));
                        tvGetcode.setText("" + count + "s");
                    }

                    break;
                default:
                    break;
            }
        }
    };


    /**
     * 设置EditText 的hint文字的大小
     */
    private SpannedString setSpannable(String s, int size) {
        SpannableString ss = new SpannableString(s);//定义hint的值
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(size, true);//设置字体大小 true表示单位是sp
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //  return ss2;
        return new SpannedString(ss);
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha 屏幕透明度0.0-1.0 1表示完全不透明
     */
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = FourPartActivity.this.getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        this.getWindow().setAttributes(lp);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
