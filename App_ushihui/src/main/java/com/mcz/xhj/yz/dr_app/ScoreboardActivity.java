package com.mcz.xhj.yz.dr_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_adapter.ScoreboardAdapter;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_bean.ScoreboardBean;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.LogUtils;
import com.mcz.xhj.yz.dr_util.show_Dialog_IsLogin;
import com.mcz.xhj.yz.dr_view.ListInScroll;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Method;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/10/23
 * 描述：积分排行榜
 */

public class ScoreboardActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    View toolbar;
    @BindView(R.id.title_leftimageview)
    ImageView titleLeftimageview;
    @BindView(R.id.title_centertextview)
    TextView title_center;
    @BindView(R.id.img_avatar)
    CircleImageView img_avatar;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_jifen_curMonth)
    TextView tvJifenCurMonth;
    @BindView(R.id.tv_current_rank)
    TextView tvCurrentRank;
    @BindView(R.id.tv_reward)
    TextView tvReward;
    @BindView(R.id.img_month)
    ImageView img_month;
    @BindView(R.id.list_scoreboard)
    ListInScroll listScoreboard;
    @BindView(R.id.ptr_scoreboard)
    PtrClassicFrameLayout ptrScoreboard;
    @BindView(R.id.sc_scroll)
    ScrollView scScroll;
    @BindView(R.id.tv_jifen_current)
    TextView tvJifenCurrent;
    @BindView(R.id.ll_limit)
    LinearLayout llLimit;
    @BindView(R.id.btn_more)
    TextView btnMore;
    @BindView(R.id.rl_bottom)
    RelativeLayout rlBottom;
    @BindView(R.id.ll_scoreboard)
    LinearLayout llScoreboard;
    @BindView(R.id.tv_telPhone)
    TextView tv_telPhone;

    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
    private List<ScoreboardBean> scoreboardList;
    private String uid;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_scoreboard;
    }

    @Override
    protected void initParams() {
        title_center.setText("积分榜单");
        getDate();
        getPlatFormInfo();
        if (LocalApplication.getInstance().getMainActivity().hasNavigationBar) {//如果有导航栏
            int virtualBarHeigh = getVirtualBarHeigh();
            LogUtils.i("虚拟按键的高度：virtualBarHeigh=" + virtualBarHeigh);
            llScoreboard.setPadding(0, 0, 0, virtualBarHeigh);
        }
        scScroll.setFocusable(false);
        scScroll.smoothScrollTo(0, 20);
        String avatar_url = preferences.getString("avatar_url", "");
        if (!avatar_url.equals("")) {
            Picasso.with(ScoreboardActivity.this).load(avatar_url).memoryPolicy(MemoryPolicy.NO_CACHE).into(img_avatar);
        }
        ptrScoreboard.setPtrHandler(new PtrHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //getDate();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                // TODO Auto-generated method stub
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });

        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalApplication.getInstance().getMainActivity().setCheckedFram(2);
                finish();
            }
        });
    }

    private void getDate() {
        showWaitDialog("加载中...", true, "");
        LogUtils.e("url"+UrlConfig.POINTSRANK+"/"+preferences.getString("uid", ""));
        OkHttpUtils.post().url(UrlConfig.POINTSRANK)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String response) {
                        dismissDialog();
                        ptrScoreboard.refreshComplete();
                        LogUtils.i("积分排行：" + response);
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean("success")) {
                            JSONObject map = obj.getJSONObject("map");
                            JSONObject loginMember = map.getJSONObject("loginMember");
                            String mobilephone = loginMember.getString("mobilephone");
                            String realName = loginMember.getString("realName");

                            StringBuilder sb = new StringBuilder(mobilephone);
                            sb.replace(3, 7, "****");
                            tv_name.setText(sb);

                           /* if(!TextUtils.isEmpty(realName)){
                                tv_name.setText(realName);
                            }else{
                                tv_name.setText(mobilephone);
                            }*/

                            JSONArray rankList = map.getJSONArray("rankList");
                            JSONObject myPoint = map.getJSONObject("myPoint");
                            //不同的月份图片,现在不用了
                           // String nowMonth = map.getString("nowMonth");
                           // changeMonthBgImage(nowMonth);

                            uid = myPoint.getString("uid");

                            String pointMonth = myPoint.getString("pointMonth");
                            if (pointMonth.equals("0")){
                                tvJifenCurMonth.setText("-");
                            }else {
                                tvJifenCurMonth.setText(pointMonth);
                            }

                            tvJifenCurrent.setText(myPoint.getString("point"));

                            if (rankList.size() > 0 ){
                                scoreboardList = JSON.parseArray(rankList.toJSONString(), ScoreboardBean.class);
                                listScoreboard.setAdapter(new ScoreboardAdapter(ScoreboardActivity.this, scoreboardList));
                                countRank(scoreboardList);
                            }else{
                                //tvCurrentRank.setText("暂无排名");
                                //tvReward.setText("无");
                                tvCurrentRank.setText("-");
                                tvReward.setText("-");
                            }
                        }else if("9998".equals(obj.getString("errorCode"))){
                           //您的登录已过期或已在其他设备登录，请重新登录
                            //LogUtils.e("errorCode");
                            new show_Dialog_IsLogin(ScoreboardActivity.this).show_Is_Login();
                            //finish();
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        //LogUtils.e("Exception"+e.toString());

                        dismissDialog();
                        ptrScoreboard.refreshComplete();
                        ToastMaker.showShortToast("请检查网络");
                    }
                });
    }

    private void getPlatFormInfo(){
        OkHttpUtils
                .post()
                .url(UrlConfig.GETPLATFORMINFO)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String response) {
                        LogUtils.i("---》客服电话"+response);
                        JSONObject obj = JSON.parseObject(response);
                        if(obj.getBoolean("success")){
                            JSONObject map = obj.getJSONObject("map");
                            String telPhone = map.getString("platForm");
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("telPhone",telPhone);
                            editor.commit();
                            if(!TextUtils.isEmpty(telPhone)){
                                tv_telPhone.setText("客服热线："+telPhone);
                            }
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        ToastMaker.showShortToast("请检查网络");
                    }
                });
    }

    private void changeMonthBgImage(String nowMonth) {
        if(nowMonth.equals("1")){
            img_month.setImageResource(R.mipmap.month_1);
        }else if(nowMonth.equals("2")){
            img_month.setImageResource(R.mipmap.month_2);
        }else if(nowMonth.equals("3")){
            img_month.setImageResource(R.mipmap.month_3);
        }else if(nowMonth.equals("4")){
            img_month.setImageResource(R.mipmap.month_4);
        }else if(nowMonth.equals("5")){
            img_month.setImageResource(R.mipmap.month_5);
        }else if(nowMonth.equals("6")){
            img_month.setImageResource(R.mipmap.month_6);
        }else if(nowMonth.equals("7")){
            img_month.setImageResource(R.mipmap.month_7);
        }else if(nowMonth.equals("8")){
            img_month.setImageResource(R.mipmap.month_8);
        }else if(nowMonth.equals("9")){
            img_month.setImageResource(R.mipmap.month_9);
        }else if(nowMonth.equals("10")){
            img_month.setImageResource(R.mipmap.month_10);
        }else if(nowMonth.equals("11")){
            img_month.setImageResource(R.mipmap.month_11);
        }else if(nowMonth.equals("12")){
            img_month.setImageResource(R.mipmap.month_12);
        }
    }

    private void countRank(List<ScoreboardBean> scoreboardList) {
        for (int i = 0; i < scoreboardList.size(); i++) {
            if(uid.equals(scoreboardList.get(i).getUid()+"")){
                tvCurrentRank.setText(i+1+"名");
                tvReward.setText(scoreboardList.get(i).getGoodsName());
                return;
            }else {
                //tvCurrentRank.setText("您暂时未上榜");
                //tvReward.setText("无");
                tvCurrentRank.setText("-");
                tvReward.setText("-");
            }
        }
    }

    private void initData() {
        //假数据.
        //scoreboardList = new ArrayList<ScoreboardBean>();
        /*for (int i = 0; i < 11; i++) {
            ScoreboardBean scoreboardBean = new ScoreboardBean();
            scoreboardBean.setName("王洛奇" + i);
            scoreboardBean.setJiFen(8890 - i + "");
            scoreboardBean.setAward("10元红包");
            scoreboardList.add(scoreboardBean);
        }*/
        //listScoreboard.setAdapter(new ScoreboardAdapter(ScoreboardActivity.this, scoreboardList));
        //setListViewHeightBasedOnChildren(listScoreboard);
    }

    /**
     * 获取虚拟功能键高度
     */
    public int getVirtualBarHeigh() {
        int vh = 0;
        WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        try {
            @SuppressWarnings("rawtypes")
            Class c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            vh = dm.heightPixels - windowManager.getDefaultDisplay().getHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vh;
    }

    @OnClick({R.id.title_leftimageview})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_leftimageview:
                finish();
                break;
        }
    }
}
