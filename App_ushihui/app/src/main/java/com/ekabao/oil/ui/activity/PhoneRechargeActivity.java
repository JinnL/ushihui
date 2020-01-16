package com.ekabao.oil.ui.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ekabao.oil.R;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.ui.fragment.PhoneDataFragment;
import com.ekabao.oil.ui.fragment.PhonePackageFragment;
import com.ekabao.oil.ui.fragment.PhoneRechargeFragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhoneRechargeActivity extends BaseActivity {

    //    @BindView(R.id.title_lefttextview)
//    TextView titleLefttextview;
//    @BindView(R.id.title_leftimageview)
//    ImageView titleLeftimageview;
//    @BindView(R.id.title_centertextview)
//    TextView titleCentertextview;
//    @BindView(R.id.title_centerimageview)
//    ImageView titleCenterimageview;
//    @BindView(R.id.title_righttextview)
//    TextView titleRighttextview;
//    @BindView(R.id.title_rightimageview)
//    ImageView titleRightimageview;
//    @BindView(R.id.view_line_bottom)
//    View viewLineBottom;
//    @BindView(R.id.rl_title)
//    RelativeLayout rlTitle;
    @BindView(R.id.tv_call_charge)
    TextView tvCallCharge;
    @BindView(R.id.ll_phone_slow)
    RelativeLayout llPhoneSlow;
    @BindView(R.id.ll_phone_quick)
    LinearLayout llPhoneQuick;
    @BindView(R.id.ll_phone_liu)
    LinearLayout llPhoneLiu;
    @BindView(R.id.fragment_content)
    FrameLayout fragmentContent;
    @BindView(R.id.ll_indicator)
    LinearLayout llIndicator;
    @BindView(R.id.tv_recharge)
    TextView tvRecharge;
    @BindView(R.id.tv_data)
    TextView tvData;

    /**
     * 手机充值
     *
     * @time 2018/12/4 15:36
     * Created by lj
     */
   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_recharge);
        ButterKnife.bind(this);
    }*/
    private SharedPreferences preferences;
    private String uid;
    LinearLayout.LayoutParams param1f = new LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.MATCH_PARENT, 1.5f);
    private static String phone;

    private FragmentTransaction transaction;
    private PhonePackageFragment fragPhoneSlow;

    private PhoneRechargeFragment fragPhoneQuick;
    private PhoneDataFragment fragPhoneLiu;

    private String phoneNum;
    private int liuType = 5;
    private int currentPosition = 0;

    private String operator;
    private int position;
    private int style; //从那个入口进来

    String isChinaMobile = "^134[0-8]\\d{7}$|^(?:13[5-9]|147|15[0-27-9]|178|1703|1705|1706|18[2-478])\\d{7,8}$"; // 移动
    String isChinaUnion = "^(?:13[0-2]|145|15[56]|176|1704|1707|1708|1709|171|18[56])\\d{7,8}$"; // 联通
    String isChinaTelcom = "^(?:133|153|1700|1701|1702|177|173|18[019])\\d{7,8}$"; // 电信

    private Boolean isPay = false;//作为容器的对象

    @Override
    protected int getLayoutId() {
        return R.layout.activity_phone_recharge;
    }

    @Override
    protected void initParams() {
//        rlTitle.setBackgroundColor(getResources().getColor(R.color.primary));
//        viewLineBottom.setVisibility(View.GONE);
//        titleLeftimageview.setImageResource(R.drawable.fanhui_white);
//        titleCentertextview.setTextColor(Color.WHITE);
//        titleCentertextview.setText("手机充值");
//        titleRightimageview.setVisibility(View.VISIBLE);
//        titleRightimageview.setImageResource(R.drawable.icon_request_white);

        //LocalApplication.getInstance().setAtyPhoneMoney(this);

        preferences = LocalApplication.getInstance().sharereferences;
        uid = preferences.getString("uid", "");
        phone = preferences.getString("phone", "");

        position = getIntent().getIntExtra("position", 0);

        int fragment_type = preferences.getInt("fragment_type", 0);
        // type=0 ;1油卡套餐  2 油卡直冲 4 话费套餐 5 话费直冲  是从项目立即支付里面过来的
        if (fragment_type != 0) {
            if (fragment_type == 4) {
                position = 0;
            } else {
                position = 1;
            }
        }
//        titleRightimageview.setVisibility(View.VISIBLE);
//        titleCentertextview.setText("手机充值");


//        StringCut.space_Cut(etPhone.getText().toString().trim()).length();
//
//        if (!TextUtils.isEmpty(phone)) {
//            String formatPhone = StringCut.formatPhone(phone);
//            etPhone.setText(formatPhone);
//            etPhone.setCursorVisible(false);
//        }
//        etPhone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                etPhone.setCursorVisible(true);
//            }
//        });
//
//        formatPhoneInput(etPhone);
//        loadPhoneData();
        currentPosition = position;
        if (position == 0) {
            switchFrgment(0);//切换Fragment
        } else if (position == 1) {

            //switchFragment(1);//切换Fragment


            llIndicator.setBackgroundResource(R.drawable.bg_phone_bar_2);
            //llPhoneSlow.setBackgroundResource(R.drawable.shape_phone_normal);
            //llPhoneQuick.setBackgroundResource(R.drawable.shape_phone_checked);
            //llPhoneLiu.setBackgroundResource(R.drawable.shape_phone_normal);
            tvCallCharge.setTextColor(getResources().getColor(R.color.text_black));
            tvRecharge.setTextColor(getResources().getColor(R.color.primary));
            tvData.setTextColor(getResources().getColor(R.color.text_black));

               /* llPhoneSlow.setLayoutParams(param1f);
                llPhoneQuick.setLayoutParams(param);
                llPhoneLiu.setLayoutParams(param1f);
                LinearLayout.LayoutParams lp1 = (LinearLayout.LayoutParams) llPhoneQuick.getLayoutParams();
                lp1.leftMargin = 5;
                lp1.rightMargin = 5;
                llPhoneQuick.setLayoutParams(lp1);*/
            switchFrgment(1);//切换Fragment
            currentPosition = 1;

        } else if (position == 2) {
            switchFrgment(2);//切换Fragment
        }


    }

    @Override
    protected void onStart() {
        super.onStart();

     /*   int fragment_type = preferences.getInt("fragment_type", 0);
        // type=0 ;1油卡套餐  2 油卡直冲 4 话费套餐 5 话费直冲  是从项目立即支付里面过来的
        if (fragment_type != 0) {
            if (fragment_type == 4) {
                switchFragment(0);//切换Fragment
            } else {
                switchFragment(1);//切换Fragment
            }
        }*/
    }

//    private void loadPhoneData() {
//        showWaitDialog("加载中...", true, "");
//
//        phoneNum = etPhone.getText().toString();
//        LogUtils.e("mobilephone" + formatPhoneNum(phoneNum) + UrlConfig.queryPhone);
//
//        OkHttpUtils.post().url(UrlConfig.queryPhone)
//                .addParams("uid", preferences.getString("uid", ""))
//                .addParams("mobilephone", formatPhoneNum(phoneNum))
//                .addParams("version", UrlConfig.version)
//                .addParams("channel", "2")
//                .build() //1是套餐，2是即时
//                .execute(new StringCallback() {
//                    @Override
//                    public void onResponse(String response) {
//                        LogUtils.e("phoneNumInfo==" + response);
//                        dismissDialog();
//
//                        JSONObject obj = JSON.parseObject(response);
//
//
//                        //  LogUtils.e(obj.getString("error_response")+"//"+obj.getString("code"));
//                        if (response.contains("success")) {
//                            if (!obj.getBoolean(("success"))) {
//                                if ("9998".equals(obj.getString("errorCode"))) {
//                                    //ToastMaker.showShortToast("系统异常");
//                                    new show_Dialog_IsLogin(PhoneRechargeActivity.this).show_Is_Login();
//                                    return;
//                                }
//                            }
//                        } else if (response.contains("resultcode") && obj.getInteger("resultcode") == 200) {
//
//                            PhoneNumInfo atyQueryPhoneInfo = GsonUtil.parseJsonToBean(response, PhoneNumInfo.class);
//
//                            operator = atyQueryPhoneInfo.getResult().getCompany();
//
//                            /*operator = atyQueryPhoneInfo.getPhone_info_response().getPhoneInfo()
//                                    .getOperator();*/
//                            if (!TextUtils.isEmpty(operator)) {
//                                isPay = true;
//                                tvIsQuery.setVisibility(View.GONE);
//                                if ("移动".equals(operator)) {
//                                    ivPhone.setImageResource(R.drawable.icon_yidong);
//                                    liuType = 5;
//                                } else if ("联通".equals(operator)) {
//                                    ivPhone.setImageResource(R.drawable.icon_liantong);
//                                    liuType = 6;
//                                } else if ("电信".equals(operator)) {
//                                    liuType = 7;
//                                    ivPhone.setImageResource(R.drawable.icon_dianxin);
//                                } else {
//                                    ivPhone.setImageResource(R.drawable.icon_dianxin);
//
//                                }
//                            } else {
//                                isPay = false;
//                                dismissDialog();
//                                tvIsQuery.setVisibility(View.VISIBLE);
//                            }
//
//                            if (currentPosition == 0) {
//                                switchFrgment(0);//切换Fragment
//                            } else if (currentPosition == 1) {
//                                switchFrgment(1);//切换Fragment
//                            } else if (currentPosition == 2) {
//                                switchFrgment(2);//切换Fragment
//                                //fragPhoneLiu.getPorductList();
//                            }
//                        } else {
//                            // if (response.contains("error"))
//                            ToastMaker.showShortToast("系统异常");
//                            return;
//                        }
//
//                    }
//
//                    @Override
//                    public void onError(Call call, Exception e) {
//                        dismissDialog();
//                        ToastUtil.showToast("请检查网络");
//                    }
//                });
//    }


    @OnClick({R.id.iv_back, R.id.ll_phone_slow, R.id.ll_phone_quick, R.id.ll_phone_liu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
//            case R.id.title_rightimageview:
//                startActivity(new Intent(PhoneRechargeActivity.this, CallCenterActivity.class));
//                break;
//            case R.id.et_phone:
//                break;
            case R.id.ll_phone_slow:
//                llIndicator.setBackgroundResource(R.drawable.bg_phone_bar_1);
                //llPhoneQuick.setBackgroundResource(R.drawable.shape_phone_normal);
                //llPhoneLiu.setBackgroundResource(R.drawable.shape_phone_normal);
               /* llPhoneSlow.setLayoutParams(param);
                llPhoneQuick.setLayoutParams(param1f);
                llPhoneLiu.setLayoutParams(param1f);
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) llPhoneQuick.getLayoutParams();
                lp.leftMargin = 5;
                lp.rightMargin = 5;
                llPhoneQuick.setLayoutParams(lp);*/

                tvCallCharge.setTextColor(getResources().getColor(R.color.color_3));
                tvRecharge.setTextColor(getResources().getColor(R.color.text_99));
                tvData.setTextColor(getResources().getColor(R.color.text_99));

                switchFrgment(0);//切换Fragment
                currentPosition = 0;
                break;
            case R.id.ll_phone_quick:
//                llIndicator.setBackgroundResource(R.drawable.bg_phone_bar_2);
                //llPhoneSlow.setBackgroundResource(R.drawable.shape_phone_normal);
                //llPhoneQuick.setBackgroundResource(R.drawable.shape_phone_checked);
                //llPhoneLiu.setBackgroundResource(R.drawable.shape_phone_normal);
                tvCallCharge.setTextColor(getResources().getColor(R.color.text_99));
                tvRecharge.setTextColor(getResources().getColor(R.color.color_3));
                tvData.setTextColor(getResources().getColor(R.color.text_99));

               /* llPhoneSlow.setLayoutParams(param1f);
                llPhoneQuick.setLayoutParams(param);
                llPhoneLiu.setLayoutParams(param1f);
                LinearLayout.LayoutParams lp1 = (LinearLayout.LayoutParams) llPhoneQuick.getLayoutParams();
                lp1.leftMargin = 5;
                lp1.rightMargin = 5;
                llPhoneQuick.setLayoutParams(lp1);*/
                switchFrgment(1);//切换Fragment
                currentPosition = 1;
                break;
            case R.id.ll_phone_liu:
                llIndicator.setBackgroundResource(R.drawable.bg_phone_bar_3);
                //llPhoneSlow.setBackgroundResource(R.drawable.shape_phone_normal);
                //llPhoneQuick.setBackgroundResource(R.drawable.shape_phone_normal);
                //.setBackgroundResource(R.drawable.shape_phone_checked);
             /*   llPhoneSlow.setLayoutParams(param1f);
                llPhoneQuick.setLayoutParams(param1f);
                llPhoneLiu.setLayoutParams(param);
                LinearLayout.LayoutParams lp2 = (LinearLayout.LayoutParams) llPhoneQuick.getLayoutParams();
                lp2.leftMargin = 5;
                lp2.rightMargin = 5;
                llPhoneQuick.setLayoutParams(lp2);*/

                tvCallCharge.setTextColor(getResources().getColor(R.color.text_99));
                tvRecharge.setTextColor(getResources().getColor(R.color.text_99));
                tvData.setTextColor(getResources().getColor(R.color.color_3));

                switchFrgment(2);//切换Fragment
                currentPosition = 2;
                break;
        }
    }

    /**
     * switch the fragment Fragment切换
     *
     * @param i id
     */
    public void switchFrgment(int i) {
        transaction = getSupportFragmentManager().beginTransaction();
        switch (i) {
            case 0:
                if (fragPhoneSlow == null) {
                    fragPhoneSlow = PhonePackageFragment.newInstance(phone);
                }
                transaction.replace(R.id.fragment_content, fragPhoneSlow);
                if (!TextUtils.isEmpty(operator)) {
                    fragPhoneSlow.setIsPay(true);
                } else {
                    fragPhoneSlow.setIsPay(false);
                }
                //Activity传值，通过Bundle
//                fragPhoneSlow.setReceived(etPhone.getText().toString());
                break;
            case 1:
                if (fragPhoneQuick == null) {
                    fragPhoneQuick = PhoneRechargeFragment.newInstance(phone);
                }
                transaction.replace(R.id.fragment_content, fragPhoneQuick);
                if (!TextUtils.isEmpty(operator)) {
                    fragPhoneQuick.setIsPay(true);
                } else {
                    fragPhoneQuick.setIsPay(false);
                }
                //Activity传值，通过Bundle
//                fragPhoneQuick.setReceived(etPhone.getText().toString());
                break;
            case 2:
                if (fragPhoneLiu == null) {
                    fragPhoneLiu = PhoneDataFragment.newInstance();
                }
                transaction.replace(R.id.fragment_content, fragPhoneLiu);
                if (!TextUtils.isEmpty(operator)) {
                    fragPhoneLiu.setIsPay(true);
                } else {
                    fragPhoneLiu.setIsPay(false);
                }
//                //Activity传值，通过Bundle
//                fragPhoneLiu.setReceived(etPhone.getText().toString());
                //fragPhoneLiu.setLiuType(liuType);
                break;

            default:
                break;
        }
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        fragPhoneSlow = PhonePackageFragment.newInstance(phone);
        fragPhoneQuick = PhoneRechargeFragment.newInstance(phone);
        fragPhoneLiu = PhoneDataFragment.newInstance();
        ButterKnife.bind(this);
    }

    /**
     * 格式化11位手机号码输入 xxx xxxx xxxx格式
     * 如果一直是添加：输入到第三个或第8个数字时 自动空格
     * 如果是回退情况：判断当前长度为4或9时的前一个字段是否是' ' 不是则添加
     *
     * @param editText 输入控件
     */
    public void formatPhoneInput(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                editText.setCursorVisible(true);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s == null || s.length() == 0) {
                    return;
                }
                StringBuilder sb = new StringBuilder(s.toString());
                if (before == 0) { //上一次是add时
                    if (sb.length() == 3 || sb.length() == 8) {//自动追加空格
                        sb.append(' ');
                    }
                }
                if ((sb.length() == 4 || sb.length() == 9) && sb.charAt(sb.length() - 1) != ' ') {
                    sb.insert(sb.length() - 1, ' ');
                }

                if (!sb.toString().equals(s.toString())) {
                    editText.setText(sb.toString());
                    editText.setSelection(sb.toString().length());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (formatPhoneNum(s.toString()).length() == 11) {
                    editText.setCursorVisible(false);
//                    loadPhoneData();
                } else {
                    isPay = false;
                }
            }
        });
    }

    /**
     * 去掉手机号内除数字外的所有字符
     *
     * @param phoneNum 手机号
     * @return
     */
    public String formatPhoneNum(String phoneNum) {
        String regex = "(\\+86)|[^0-9]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNum);
        return matcher.replaceAll("");
    }

    public Boolean getIsPay() {
        return isPay;
    }

    public void setIsPay(Boolean isPay) {
        this.isPay = isPay;
    }

}
