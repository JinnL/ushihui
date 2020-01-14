package com.ekabao.oil.ui.activity.me;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ekabao.oil.R;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.ui.activity.BaseActivity;
import com.ekabao.oil.ui.view.ToastMaker;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * $desc$
 * 实名认证
 *
 * @time $data$ $time$
 * Created by Administrator on 2018/7/17.
 */

/**
 * 实名认证
 *
 * @time 2018/7/17 17:38
 * Created by lj
 */

public class RealNameActivity extends BaseActivity {
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
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_idcard)
    EditText etIdcard;
    @BindView(R.id.tv_login)
    TextView tvLogin;

    Pattern idcard = Pattern.compile("^[0-9]{15}$|^[0-9]{17}[0-9xX]$");
    Pattern banknum = Pattern.compile("^[0-9]{16}$|^[0-9]{19}$|^[0-9]{18}$");
    Pattern phone = Pattern.compile(LocalApplication.context.getResources().getString(R.string.pPhone));

    @Override
    protected int getLayoutId() {
        return R.layout.activity_real_name;
    }

    @Override
    protected void initParams() {
        titleCentertextview.setText("实名认证");

        etName.setHint(setSpannable("开户姓名", 15));

        etIdcard.setHint(setSpannable("身份证号", 15));

        etIdcard.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //判断是否是“GO”键
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etIdcard.getWindowToken(), 0);

                    if ("".equalsIgnoreCase(etName.getText().toString())) {
                        ToastMaker.showShortToast("姓名不能为空");
                    } else if (!idcard.matcher(etIdcard.getText()).matches()) {
                        ToastMaker.showShortToast("请输入正确的身份证号码");
                    } else {

                        String name = etName.getText().toString();

                        String idcard = etIdcard.getText().toString();

                        startActivityForResult(new Intent(RealNameActivity.this,
                                        FourPartActivity.class)
                                        .putExtra("name", name)
                                        .putExtra("idcard", idcard),
                                20);
                    }


                    return true;
                }

                return false;
            }
        });
    }

    @OnClick({R.id.title_leftimageview, R.id.tv_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_leftimageview:
                finish();
                break;
            case R.id.tv_login:

                if ("".equalsIgnoreCase(etName.getText().toString())) {
                    ToastMaker.showShortToast("姓名不能为空");
                } else if (!idcard.matcher(etIdcard.getText()).matches()) {
                    ToastMaker.showShortToast("请输入正确的身份证号码");
                } else {

                    String name = etName.getText().toString();

                    String idcard = etIdcard.getText().toString();

                    startActivityForResult(new Intent(RealNameActivity.this,
                                    FourPartActivity.class)
                                    .putExtra("name", name)
                                    .putExtra("idcard", idcard),
                            20);
                }
                break;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 20:
                if (resultCode == Activity.RESULT_OK) {
                    setResult(Activity.RESULT_OK);
                    finish();
                }
                break;
        }

    }

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
