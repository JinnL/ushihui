package com.mcz.xhj.yz.dr_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mcz.xhj.R;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.facebook.drawee.view.SimpleDraweeView;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.show_Dialog_IsLogin;
import com.mcz.xhj.yz.dr_util.stringCut;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import okhttp3.Call;

/**
 * 礼品预约
 * Created by shuc on 2016/12/22.
 */

public class Act_Gift_Reservation extends BaseActivity implements View.OnClickListener{
    @BindView(R.id.title_centertextview)
    TextView title_centertextview;
    @BindView(R.id.title_righttextview)
    TextView title_righttextview;
    @BindView(R.id.title_rightimageview)
    ImageView title_rightimageview;
    @BindView(R.id.title_leftimageview)
    ImageView title_leftimageview;

    @BindView(R.id.tv_amount)
    TextView tv_amount;
    @BindView(R.id.tv_shouyi)
    TextView  tv_shouyi;
    @BindView(R.id.reservation_bt_ok)
    Button reservation_bt_ok;
    @BindView(R.id.gift_pic)
    SimpleDraweeView gift_pic;
    @BindView(R.id.tv_prize_name)
    TextView tv_prize_name;
    @BindView(R.id.tv_prize_simplename)
    TextView tv_prize_simplename;

    private String pid;
    private String ppid ;  //奖品id
    private String reserAmount ; //预约金额
    private String prize_name;
    private String simpleName;
    private String h5ImgUrlH;
    private String rate ;
    private String activityRate;
    private String deadline ;
    @Override
    protected int getLayoutId() {
        return R.layout.gift_reservation;
    }

    @Override
    protected void initParams() {
        title_centertextview.setText("礼品预约");
        title_leftimageview.setOnClickListener(this);
        reservation_bt_ok.setOnClickListener(this);
        Intent intent = getIntent();
        Uri uri = intent.getData();
        if (uri != null) {
            ppid = uri.getQueryParameter("ppid");
        } else {
            ppid = getIntent().getStringExtra("ppid");
        }
        selectProductPrize();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.title_leftimageview :
                finish();
                break;
            case R.id.reservation_bt_ok :
                //提交预约
                insertPrizeInfo();
                break;
        }
    }

    //得到奖品详情
    public void selectProductPrize(){
        showWaitDialog("加载中...", true, "");
        OkHttpUtils.post().url(UrlConfig.SELECTPRODUCTPRIZE)
                .addParams("id", ppid)
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        // TODO Auto-generated method stub
                        JSONObject obj = JSON.parseObject(response);
                        dismissDialog();
                        if (obj.getBoolean(("success"))) {
                            JSONObject objmap = obj.getJSONObject("map");
                            if(objmap.size() == 0){
                                return;
                            }
                            JSONObject prize = objmap.getJSONObject("prize");
                            rate = prize.getString("rate");
                            activityRate = prize.getString("activityRate");
                            deadline = prize.getString("deadline");
                            h5ImgUrlH = prize.getString("h5ImgUrlH") ;
                            prize_name = prize.getString("name");
                            simpleName = prize.getString("simpleName");
                            ppid = prize.getString("id");

                            if(h5ImgUrlH!=null && !"".equalsIgnoreCase(h5ImgUrlH)){
                                Uri uri = Uri.parse(h5ImgUrlH);
                                gift_pic.setImageURI(uri);
                            }
                            reserAmount  = stringCut.getNumKb(Double.parseDouble(prize.getString("amount")));

                            tv_prize_name.setText(prize_name);
                            tv_prize_simplename.setText(simpleName);
                            tv_amount.setText(reserAmount);
                            double rateAll = stringCut.getDoubleFromJsonObject(prize,"rate")+stringCut.getDoubleFromJsonObject(prize,"activityRate");
                            tv_shouyi.setText(Money_Get(rateAll));

                        } else if ("9999".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("系统异常");
                        } else if ("9998".equals(obj.getString("errorCode"))) {
                            new show_Dialog_IsLogin(Act_Gift_Reservation.this)
                                    .show_Is_Login();
                        } else {
                            ToastMaker.showShortToast("系统异常");
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

    //添加预约
    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
    public void  insertPrizeInfo(){
        showWaitDialog("加载中...", true, "");
        OkHttpUtils.post().url(UrlConfig.INSERT_PRIZEINFO)
                .addParams("ppid", ppid)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        // TODO Auto-generated method stub
                        JSONObject obj = JSON.parseObject(response);
                        dismissDialog();
                        if (obj.getBoolean(("success"))) {
                            ToastMaker.showShortToast("预约成功");
                            finish();
                        } else if ("9999".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("系统异常");
                        } else if ("9998".equals(obj.getString("errorCode"))) {
                            new show_Dialog_IsLogin(Act_Gift_Reservation.this).show_Is_Login();
                        } else {
                            ToastMaker.showShortToast("系统异常");
                        }
                    }
                    @Override
                    public void onError(Call call, Exception e) {
                        // TODO Auto-generated method stub
                        dismissDialog();
                        ToastMaker.showShortToast("网络错误，请检查");
                    }
                });
    }

    public String Money_Get(double rateDouble){
        return stringCut.getNumKbs((Double.parseDouble(stringCut.douHao_Cut(reserAmount)))
                * (rateDouble / 360 / 100)
                * Double.parseDouble(deadline));
    }
}
