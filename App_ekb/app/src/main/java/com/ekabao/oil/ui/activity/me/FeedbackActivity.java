package com.ekabao.oil.ui.activity.me;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ekabao.oil.R;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.ui.activity.BaseActivity;
import com.ekabao.oil.ui.view.ToastMaker;
import com.ekabao.oil.util.LogUtils;
import com.ekabao.oil.util.ToastUtil;

import butterknife.BindView;
import okhttp3.Call;

public class FeedbackActivity extends BaseActivity implements View.OnClickListener {

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
    @BindView(R.id.et_enter)
    EditText etEnter;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.bt_add)
    Button btAdd;

    private CharSequence temp;
    private SharedPreferences preferences = LocalApplication.sharereferences;
   // LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_feedback);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void initParams() {

        titleCentertextview.setText("意见反馈");

        titleLeftimageview.setOnClickListener(this);
        btAdd.setOnClickListener(this);
        btAdd.setClickable(false);

        etEnter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 0) {
                    btAdd.setClickable(true);
                    btAdd.setTextColor(getResources().getColorStateList(R.color.white));
                } else {
                    btAdd.setClickable(false);
                    btAdd.setTextColor(getResources().getColorStateList(R.color.text_99));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
             /*   String str = "<font color='#d83131'>" +(200 - s.length()) + "</font>"+"/200";
                //String str="默认颜色: <font color='#2e2e2e'>"+info.TotalMoney+"红颜色</font>";
                tvNum.setText(Html.fromHtml(str));*/

                if (temp.length() - 1 >= 200) {
                    ToastUtil.showToast("你输入的字数已经达到了限制！");
                    String str = "<font color='#d83131'>" + (200 - s.length()) + "</font>" + "/200";
                    tvNum.setText(Html.fromHtml(str));
                    etEnter.setClickable(false);
                } else {
                    tvNum.setText(200 - s.length() + "/200");
                    etEnter.setClickable(true);
                }
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_leftimageview:
                finish();
                break;
            case R.id.bt_add:

                //dialog = new LoadingDialog(this, "上传中...");
                //显示Dialog
                //dialog.show();

                String trim = etEnter.getText().toString().trim();

                add(trim);
                break;

        }
    }

    /**
     * 反馈
     */
    protected void add(String content) {

        showWaitDialog("提交中...", true, "");

        OkHttpUtils.post().url(UrlConfig.FEEDBACK)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("content", content)
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {

                        //refreshLayout.finishRefresh();

                        LogUtils.e("--->反馈：" + response);

                        dismissDialog();
                        //ptrInvest.refreshComplete();
                        JSONObject obj = JSON.parseObject(response);

                        if (obj.getBoolean(("success"))) {
                           ToastMaker.showShortToast("提交成功!");
                           finish();

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
