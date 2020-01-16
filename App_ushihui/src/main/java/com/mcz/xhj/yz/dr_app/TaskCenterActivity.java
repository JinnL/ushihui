package com.mcz.xhj.yz.dr_app;

import android.animation.ValueAnimator;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_adapter.TaskAdapter;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_bean.TaskBean;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.LogUtils;
import com.mcz.xhj.yz.dr_util.show_Dialog_IsLogin;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Method;
import java.util.ArrayList;
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
 * Created by Administrator on 2017/10/20
 * 描述：任务中心
 */

public class TaskCenterActivity extends BaseActivity implements TaskAdapter.OnReceiveTaskClickListener{
    @BindView(R.id.ll_taskCenter)
    LinearLayout ll_taskCenter;
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
    @BindView(R.id.tv_task_percent)
    TextView tv_task_percent;
    @BindView(R.id.task_Progress)
    ProgressBar task_Progress;
    @BindView(R.id.ptr_task)
    PtrClassicFrameLayout ptr_task;
    @BindView(R.id.title_unfinished)
    TextView title_unfinished;
    @BindView(R.id.img_unfinished)
    ImageView img_unfinished;
    @BindView(R.id.ll_unfinished)
    LinearLayout ll_unfinished;
    @BindView(R.id.title_finished)
    TextView title_finished;
    @BindView(R.id.img_finished)
    ImageView img_finished;
    @BindView(R.id.ll_finished)
    LinearLayout ll_finished;
    @BindView(R.id.lv_task)
    ListView lv_task;
    @BindView(R.id.ll_norecord)
    LinearLayout ll_norecord;

    private boolean isLastitem = false;
    private boolean isLoading;
    private View footer;
    private LinearLayout footerlayout;
    private TextView tv_footer;
    private ProgressBar pb;
    private int page = 1;
    private int type = 1;//1=未完成，2==已完成
    private List list = new ArrayList<>();
    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
    private List<TaskBean> taskList ;
    private TaskAdapter taskAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_task_center;
    }

    @Override
    protected void initParams() {
        title_center.setText("任务中心");
        getData(type);
        if (Build.VERSION.SDK_INT < 21) {//获取当前手机版本
            toolbar.setVisibility(View.GONE);
        }
        if (LocalApplication.getInstance().getMainActivity().hasNavigationBar) {//如果有导航栏
            int virtualBarHeigh = getVirtualBarHeigh();
            LogUtils.i("虚拟按键的高度：virtualBarHeigh="+virtualBarHeigh);
            ll_taskCenter.setPadding(0,0,0,virtualBarHeigh);
        }
        String avatar_url = preferences.getString("avatar_url", "");
        if(!avatar_url.equals("")){
            Picasso.with(TaskCenterActivity.this).load(avatar_url).memoryPolicy(MemoryPolicy.NO_CACHE).into(img_avatar);
        }
        ptr_task.setPtrHandler(new PtrHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getData(type);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                // TODO Auto-generated method stub
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, lv_task, header);
            }
        });
        footer = View.inflate(TaskCenterActivity.this, R.layout.footer_layout, null);
        footerlayout = (LinearLayout) footer.findViewById(R.id.load_layout);
        pb = (ProgressBar) footer.findViewById(R.id.pb);
        tv_footer = (TextView) footer.findViewById(R.id.tv_footer);
        footerlayout.setVisibility(View.GONE);
        //lv_task.addFooterView(footer);

    }

    @OnClick({R.id.title_leftimageview, R.id.ll_unfinished,R.id.ll_finished})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_leftimageview:
                finish();
                break;

            case R.id.ll_unfinished:
                if (type == 1) {
                    return;
                }
                type = 1;
                title_unfinished.setTextColor(0XFFD33A31);
                img_unfinished.setVisibility(View.VISIBLE);
                title_finished.setTextColor(0XFFA2A5A7);
                img_finished.setVisibility(View.GONE);
                getData(type);
                break;
            case R.id.ll_finished:
                if (type == 2) {
                    return;
                }
                type = 2;
                title_unfinished.setTextColor(0XFFA2A5A7);
                img_unfinished.setVisibility(View.GONE);
                title_finished.setTextColor(0XFFD33A31);
                img_finished.setVisibility(View.VISIBLE);
                getData(type);
                break;
        }
    }

    private void getData(final int type){
        if(taskList != null){
            taskList.clear();
        }

        showWaitDialog("加载中...", true, "");
        OkHttpUtils.post().url(UrlConfig.TASKCENTER)
                .addParams("type", type+"")
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String response) {
                        dismissDialog();
                        ptr_task.refreshComplete();
                        LogUtils.i("任务中心：type=="+type+response);
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean("success")){
                            JSONObject objmap = obj.getJSONObject("map");
                            JSONObject loginMember = objmap.getJSONObject("loginMember");
                            String mobilephone = loginMember.getString("mobilephone");
                            String realName = loginMember.getString("realName");
                            if(!TextUtils.isEmpty(realName)){
                                tv_name.setText(realName);
                            }else{
                                tv_name.setText(mobilephone);
                            }
                            Integer jobCount = objmap.getInteger("jobCount");//已完成任务数
                            Integer jobSum = objmap.getInteger("jobSum");//任务总数
                            tv_task_percent.setText(jobCount+"/"+jobSum);
                            task_Progress.setMax(jobSum);
                            proAnimator(jobCount,task_Progress);
                            JSONArray list = objmap.getJSONArray("list");
                            if(taskAdapter != null){
                                taskAdapter.notifyDataSetChanged();
                            }
                            taskList = JSON.parseArray(list.toJSONString(), TaskBean.class);
                            if(taskList.size()>0){
                                ll_norecord.setVisibility(View.GONE);
                                taskAdapter = new TaskAdapter(TaskCenterActivity.this,taskList,type);
                                lv_task.setAdapter(taskAdapter);
                                taskAdapter.setOnReceiveTaskClickListener(TaskCenterActivity.this);
                            }else{
                                ll_norecord.setVisibility(View.VISIBLE);

                            }

                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        dismissDialog();
                        ptr_task.refreshComplete();
                        ToastMaker.showShortToast("请检查网络");
                    }
                });
    }

    @Override
    public void onReceiveTask(int id) {
        LogUtils.i("任务领取：jobId== "+id);
        OkHttpUtils.post().url(UrlConfig.RECEIVETASK)
                .addParams("uid", preferences.getString("uid", ""))
                .addParam("jobId",id+"")
                .addParam("version", UrlConfig.version)
                .addParam("channel", "2")
                .build().execute(new StringCallback() {
            @Override
            public void onResponse(String response) {
                LogUtils.i("任务领取："+response);
                JSONObject obj = JSON.parseObject(response);
                if (obj.getBoolean("success")){
                    ToastMaker.showShortToast("领取成功");
                    getData(1);
                } else {
                    ToastMaker.showShortToast(obj.getString("errorMsg"));
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                ToastMaker.showShortToast("请检查网络");
            }
        });
    }

    @Override
    public void onClickUrl(String linkUrlApp) {//根据linkUrlApp，跳转到指定的页面
        LogUtils.i("--->任务中心 linkUrlApp==" + linkUrlApp);
        if (linkUrlApp.equalsIgnoreCase("1")) {//邀请好友三重礼1
             LocalApplication.getInstance().getMainActivity().setCheckedFram(3);
            setResult(3);
            finish();
        } else if (linkUrlApp.equalsIgnoreCase("2")) {//我要投资(投资悦)2
            LocalApplication.getInstance().getMainActivity().setCheckedFram(2);
            setResult(3);
            finish();
        } else if (linkUrlApp.equalsIgnoreCase("3")) {//登录3
            if (preferences.getBoolean("login", false)) {
                ToastMaker.showLongToast("当前账号已登录请退出后重试");
            } else {
                TaskCenterActivity.this.startActivityForResult(new Intent(TaskCenterActivity.this, NewLoginActivity.class), 3);
            }
        } else if (linkUrlApp.equalsIgnoreCase("4")) {//注册4
            if (preferences.getBoolean("login", false)) {
                ToastMaker.showLongToast("当前账号已登录请退出后重试");
            } else {
                //TaskCenterActivity.this.startActivityForResult(new Intent(TaskCenterActivity.this, LoginQQPswAct.class).putExtra("phone",url.substring(url.indexOf("?")+7,url.length())),3);
                TaskCenterActivity.this.startActivityForResult(new Intent(TaskCenterActivity.this, NewRegisterActivity.class), 3);
            }
        } /*else if (linkUrlApp.equalsIgnoreCase("5")) {//金服详情5
            PID = url.substring(18);
            LogUtils.i("--->跳转金服详情的PID：" + PID);
            startActivity(new Intent(TaskCenterActivity.this, NewProductDetailActivity.class).putExtra("pid", PID));
            finish();
        }*/ else if (linkUrlApp.equalsIgnoreCase("6")) {//我的账户6
            LocalApplication.getInstance().getMainActivity().setCheckedFram(4);
            setResult(3);
            finish();
        } else if (linkUrlApp.equalsIgnoreCase("7")) {//充值7
            memberSetting(1);
        } else if (linkUrlApp.equalsIgnoreCase("8")) {//提现8
            memberSetting(2);
        } else if (linkUrlApp.equalsIgnoreCase("9")) {//银行卡认证9
            memberSetting(3);
        } else if (linkUrlApp.equalsIgnoreCase("10")) {//跳转微信10
            try {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setComponent(cmp);
                startActivity(intent);

                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("gongzhonghao", "xhjlc2018");
                cm.setPrimaryClip(clip);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(TaskCenterActivity.this, "检查到您手机没有安装微信，请安装后使用该功能", Toast.LENGTH_LONG).show();
            }
        } else if (linkUrlApp.equalsIgnoreCase("11")) {//优惠券11
            startActivity(new Intent(TaskCenterActivity.this, ConponsAct.class));
        } else if (linkUrlApp.equalsIgnoreCase("12")) {//任务中心12
            
        } else if (linkUrlApp.equalsIgnoreCase("13")) {//积分榜单13
            startActivity(new Intent(TaskCenterActivity.this, ScoreboardActivity.class));
        }

    }

    private void memberSetting(final int flag) {
        if (preferences.getString("uid", "").equalsIgnoreCase("")) {
            return;
        }
        showWaitDialog("请稍后...", false, "");
        OkHttpUtils
                .post()
                .url(UrlConfig.MEMBERSETTING)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        dismissDialog();
                        // TODO Auto-generated method stub
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
                            JSONObject map = obj.getJSONObject("map");
                            String isRealName = map.getString("realVerify");
                            if ("1".equals(isRealName)) {
                                if (flag == 1) {
                                    startActivity(new Intent(TaskCenterActivity.this, CashInAct.class));
                                    finish();
                                } else if (flag == 2) {
                                    startActivity(new Intent(TaskCenterActivity.this, CashOutAct.class));
                                    finish();
                                } else if (flag == 3) {
                                    startActivity(new Intent(TaskCenterActivity.this, FourPartAct.class));
                                    finish();
                                }
                            } else {
                                ToastMaker.showShortToast("您还未实名认证");
                                startActivity(new Intent(TaskCenterActivity.this, FourPartAct.class));
                                finish();
                            }
                        } else if ("9999".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("系统错误");
                        } else if ("9998".equals(obj.getString("errorCode"))) {
                            new show_Dialog_IsLogin(TaskCenterActivity.this).show_Is_Login();
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
    

    /**获取虚拟功能键高度 */
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

    /*进度条动画*/
    private void proAnimator(final int pert,final ProgressBar progressbar) {
        ValueAnimator animator = ValueAnimator.ofInt(0, pert);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int progress = (int) animation.getAnimatedValue();
                progressbar.setProgress(progress);
            }
        });
        animator.setDuration(200);
        animator.start();
    }

    @Override
    protected synchronized void onDestroy() {
        super.onDestroy();
    }

}
