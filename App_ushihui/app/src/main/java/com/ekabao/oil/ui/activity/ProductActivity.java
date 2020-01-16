package com.ekabao.oil.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ekabao.oil.R;
import com.ekabao.oil.adapter.ProductPicAdapter;
import com.ekabao.oil.bean.ProductDetail;
import com.ekabao.oil.bean.ProductDetailInfo;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.OkHttpEngine;
import com.ekabao.oil.http.PostParams;
import com.ekabao.oil.http.SimpleHttpCallback;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.ui.view.ListInScroll;
import com.ekabao.oil.util.GsonUtil;
import com.ekabao.oil.util.LogUtils;
import com.ekabao.oil.util.StringCut;
import com.scwang.smartrefresh.header.BezierCircleHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductActivity extends BaseActivity implements View.OnClickListener {
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
    @BindView(R.id.refreshLayout_head)
    BezierCircleHeader refreshLayoutHead;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_leastaAmount)
    TextView tvLeastaAmount;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.pb_progress)
    ProgressBar pbProgress;
    @BindView(R.id.tv_pert)
    TextView tvPert;
    @BindView(R.id.tv_surplusAmount)
    TextView tvSurplusAmount;
    @BindView(R.id.tv_interestType)
    TextView tvInterestType;
    @BindView(R.id.tv_expireDate)
    TextView tvExpireDate;
    @BindView(R.id.ll_details)
    LinearLayout llDetails;
    @BindView(R.id.tv_distributionMoney)
    TextView tvDistributionMoney;
    @BindView(R.id.ll_invite)
    LinearLayout llInvite;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.tv_wan_shouyi_new)
    TextView tvWanShouyiNew;
    @BindView(R.id.bt_invest)
    Button btInvest;
    @BindView(R.id.rl_bottom)
    LinearLayout rlBottom;
    @BindView(R.id.tv_rate)
    TextView tvRate;
    @BindView(R.id.tv_investNums)
    TextView tvInvestNums;
    @BindView(R.id.iv_principle)
    ImageView ivPrinciple;
    @BindView(R.id.tv_deadline)
    TextView tvDeadline;
    @BindView(R.id.tv_info_amount)
    TextView tvInfoAmount;
    @BindView(R.id.tv_info_interestType)
    TextView tvInfoInterestType;
    @BindView(R.id.tv_repayType)
    TextView tvRepayType;
    @BindView(R.id.tv_info_rate)
    TextView tvInfoRate;
    @BindView(R.id.tv_info_condition)
    TextView tvInfoCondition;

    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_repaySource)
    TextView tvRepaySource;
    @BindView(R.id.lv_product_pic)
    ListInScroll lvProductPic;
    @BindView(R.id.tv_summayt_1)
    TextView tvSummayt1;
    @BindView(R.id.tv_summayt_2)
    TextView tvSummayt2;
    @BindView(R.id.tv_summayt_3)
    TextView tvSummayt3;

    /**
     * 产品详情
     */
    private SharedPreferences preferences;
    private String ptype = "2"; //  产品类型(1=新手标, 2=票据安选, 3=票据优选, 4=活动产品, 5=体验标)

    private int pid;  //产品id
    private static boolean isFirstEnter = true; //第一次进入触发自动刷新

    private String balance;//当前登录用户余额
    private boolean isOldUser;//是否是老用户
    private boolean isShowLabel;//是否显示激活体验金startTime
    private int tpwdFlag;//是否设置交易密码 0=未设置，1=已设置
    private String startTime;//计息日期
    private long establishTime;//成立日期
    private long endTime;//回款日期
    private long endDate;//募集结束日期
    private String repayType = "";//还款方式
    private String tag;//活动标签
    private Double activityRate; //活动利率
    private Double rate; //利率
    private double amount; //出借总金额
    private double increasAmount; //出借金额需为  100 倍数
    private double surplusAmount; //剩余可投金额
    private double leastaAmount; //起投金额
    private double maxAmount;
    private int deadline; //出借期限
    private double pert;  //百分比
    private String fullName;//产品全称
    private String principleH5;//产品原理图
    private int distributionId;//是展示排行榜还是出借记录
    private Double interest;//预计收益
    private TextView tv_getyzm;//60s倒计时
    private Boolean isRoundOff = false;//是否显示尾单
    private String roundOffMoney;//尾单奖励
    public Bundle bundle; //动态加载fragment时可以通过fragment的setArguments()传入值
    private int investNums;//出借人数
    /*
      体验金
    */
    private double enableAmount_tiYanJin;//体验金激活金额
    private boolean is_presell_plan;//是否是预售标

    private boolean isChooseCoupon = false; //是否选择了优惠券
    private List<ProductDetailInfo.PicListBean> proIntroduceList = new ArrayList<>();
    private ProductPicAdapter productPicAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_product_detail);
        ButterKnife.bind(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_product_detail;
    }

    @Override
    protected void initParams() {
        preferences = LocalApplication.getInstance().sharereferences;

        viewLineBottom.setVisibility(View.VISIBLE);

        titleLeftimageview.setOnClickListener(this);
        llInvite.setOnClickListener(this);
        llDetails.setOnClickListener(this);
        btInvest.setOnClickListener(this);
        Intent intent = getIntent();
        bundle = new Bundle();
        pid = intent.getIntExtra("pid", 0);
        ptype = intent.getStringExtra("ptype");


        refreshLayout.setPrimaryColors(new int[]{0xffF4F4F4, 0xffEE4845});

        getMoreDetail();
        getDate();


       // tvSummayt1.setText(Html.fromHtml("已募集 :<font color='#666666'>" + (int) pert + "%</font>"));
       // tvSummayt2.setText(Html.fromHtml("已募集 :<font color='#666666'>" + (int) pert + "%</font>"));
       // tvSummayt3.setText(Html.fromHtml("已募集 :<font color='#666666'>" + (int) pert + "%</font>"));

       /* if (isFirstEnter) {
            isFirstEnter = false;
            getDate();

        } else {
            refreshLayout.autoRefresh();//第一次进入触发自动刷新，演示效果
        }*/


        //refreshLayout.setEnableAutoLoadMore(true);//开启自动加载功能（非必须）设置是否监听列表在滚动到底部时触发加载事件
        //下拉刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                // LogUtils.e("第一次进入触发自动刷新，演示效果");
                getDate();
                getMoreDetail();
            }
        });

        productPicAdapter = new ProductPicAdapter(proIntroduceList);

        lvProductPic.setAdapter(productPicAdapter);

        lvProductPic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                showPopupWindow1(proIntroduceList.get(position).getBigUrl());

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.title_leftimageview: //返回

                finish();
                break;
            case R.id.ll_details://项目详情

                Intent intent = new Intent(ProductActivity.this, ProductDetailActivity.class);
                intent.putExtra("pid", pid);
                startActivity(intent);
                break;
            case R.id.ll_invite://出借记录
                //是展示排行榜还是出借记录

                if (distributionId != 0) {
                    Intent intent2 = new Intent(ProductActivity.this, ScoreboardActivity.class);
                    intent2.putExtra("pid", pid);
                    intent2.putExtra("type", 2);
                    startActivityForResult(intent2, 11553);
                } else {
                    Intent intent2 = new Intent(ProductActivity.this, ScoreboardActivity.class);
                    intent2.putExtra("pid", pid);
                    startActivityForResult(intent2, 11553);
                }
                break;
            case R.id.bt_invest://立即出借
                if (preferences.getString("uid", "").equalsIgnoreCase("")) {
                    //MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");
                    startActivity(new Intent(ProductActivity.this, LoginActivity.class));
                } else {
                    Intent intent3 = new Intent(ProductActivity.this, InvestActivity.class);
                    intent3.putExtra("pid", pid);
                    startActivity(intent3);
                }

                //LogUtils.e("立即出借");
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11553 && resultCode == Activity.RESULT_OK) {

            if (preferences.getString("uid", "").equalsIgnoreCase("")) {
                //MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");
                startActivity(new Intent(ProductActivity.this, LoginActivity.class));
            } else {
                Intent intent3 = new Intent(ProductActivity.this, InvestActivity.class);
                intent3.putExtra("pid", pid);
                startActivity(intent3);
            }

        }
    }

    /**
     * 获取产品详情数据
     */
    private void getDate() {


        PostParams params = new PostParams();
        HashMap<String, Object> map = params.getParams();
        map.put("pid", pid + "");
        map.put("uid", preferences.getString("uid", ""));
        //map.put("uid", "46");
        map.put("version", UrlConfig.version);
        map.put("channel", "2");

        OkHttpEngine.create().setHeaders().post(UrlConfig.PRODUCT_DETAIL, params, new SimpleHttpCallback() {
            @Override
            public void onLogicSuccess(String data) {
                LogUtils.e(data);
                //dismissDialog();

                refreshLayout.finishRefresh();

                ProductDetail productDetail = GsonUtil.parseJsonToBean(data, ProductDetail.class);

                ProductDetail.InfoBean info = productDetail.getInfo();

                //principleH5 = info.getPrincipleH5();


                fullName = info.getFullName();
                rate = info.getRate();  //利率
                activityRate = info.getActivityRate();//活动利率
                deadline = info.getDeadline();//出借期限 多少天
                amount = info.getAmount();//出借总金额
                increasAmount = info.getIncreasAmount();
                surplusAmount = info.getSurplusAmount(); //剩余可投金额
                leastaAmount = info.getLeastaAmount();//起投金额
                maxAmount = info.getMaxAmount();//最多出借金额
                establishTime = info.getEstablish();//成立日期
                endTime = info.getExpireDate();  //回款日期
                endDate = info.getEndDate();  //募集结束日期
                pert = info.getPert();


                distributionId = info.getDistributionId(); //是展示排行榜还是出借记录


                isRoundOff = productDetail.isIsRoundOff();//是否显示尾单
                investNums = productDetail.getInvestNums();

                //LogUtils.e("distributionId" + distributionId);

                titleCentertextview.setText(fullName);

                principleH5 = info.getPrincipleH5(); //产品原理图
                String borrower = info.getBorrower(); //债务人概况
                String accept = info.getAccept(); //承兑方概况
                String introduce = info.getIntroduce();//产品说明
                String repaySource = info.getRepaySource();//还款来源


                bundle.putString("borrower", TextUtils.isEmpty(borrower) ? " " : borrower);
                bundle.putString("introduce", TextUtils.isEmpty(introduce) ? " " : introduce);
                bundle.putString("accept", TextUtils.isEmpty(accept) ? " " : accept);
                bundle.putString("repaySource", TextUtils.isEmpty(repaySource) ? " " : repaySource);
                bundle.putString("principleH5", TextUtils.isEmpty(borrower) ? " " : principleH5);

               /* bundle.putString("introduce", introduce);
                bundle.putString("accept", accept);
                bundle.putString("repaySource", repaySource);
                bundle.putString("principleH5", principleH5);*/


                SpannableStringBuilder sp = new SpannableStringBuilder(rate + "+" + activityRate + "%");
                //  sp.setSpan(new ForegroundColorSpan(0xFFFF0000), name.length(), str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //字体颜色
                sp.setSpan(new AbsoluteSizeSpan(42, true), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //字体大小
                tvRate.setText(sp);

                AssetManager assets = ProductActivity.this.getAssets();
                //根据路径得到Typeface
                Typeface tf = Typeface.createFromAsset(assets, "DIN Medium.ttf");
                //设置字体
                tvRate.setTypeface(tf);

                tvTime.setText(deadline + "天");
                tvLeastaAmount.setText(leastaAmount + "元");
                tvAmount.setText(StringCut.getNumKbs(amount));
                pbProgress.setProgress((int) pert);

                tvPert.setText(Html.fromHtml("已募集 :<font color='#EE4845'>" + (int) pert + "%</font>"));
                tvSurplusAmount.setText(Html.fromHtml("剩余金额 :<font color='#EE4845'>" + StringCut.getNumKbs(surplusAmount) + "</font>"));

                tvExpireDate.setText(StringCut.getDateToString(endTime));
                tvInvestNums.setText(Html.fromHtml("出借记录: <font color='#EE4845'>" + investNums
                        + "</font> 人"));
                if (isRoundOff) {
                    roundOffMoney = productDetail.getroundOffMoney();//尾单奖励  只有isRoundOff为true
                    tvWanShouyiNew.setText(Html.fromHtml("本期剩余金额 :<font color='#EE4845'>" + StringCut.getNumKbs(surplusAmount)
                            + "</font>元，尾单可得 <font color='#EE4845'>" + roundOffMoney + "</font>元现金"));
                } else {

                    tvWanShouyiNew.setText(Html.fromHtml("本期剩余金额 :<font color='#EE4845'>" + StringCut.getNumKbs(surplusAmount)
                            + "</font>元"));
                }

                if (productDetail.getDistribution_persion_count() != 0) {
                    tvDistributionMoney.setVisibility(View.VISIBLE);
                    tvDistributionMoney.setText(
                            Html.fromHtml("前<font color='#EE4845'>" + productDetail.getDistribution_persion_count()
                                    + "</font>名瓜分 <font color='#EE4845'>" + productDetail.getTender_money_distribution_sum() + "</font>元现金")
                    );

                } else {
                    tvDistributionMoney.setVisibility(View.GONE);
                }

                Glide.with(ProductActivity.this)
                        .load(principleH5)
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .into(ivPrinciple);

                int deadline = info.getDeadline();//出借期限 多少天

                tvDeadline.setText(Html.fromHtml("【项目期限】  <font color='#666666'>" + deadline + "天</font>"));
                double amount = info.getAmount();//出借总金额
                tvInfoAmount.setText(Html.fromHtml("【项目金额】  <font color='#666666'>" + StringCut.getNumKbs(amount) + "</font>"));

                //double v = info.getRate() + info.getActivityRate();
                // tvRate.setText(Html.fromHtml("近3个月年化收益  <font color='#999999'>" + (int) v + "%</font>"));


                tvContent.setText(introduce);//"项目简介"

                tvRepaySource.setText(repaySource);//还款来源


                List<ProductDetail.ExtendInfosBean> extendInfos = productDetail.getExtendInfos();

                for (ProductDetail.ExtendInfosBean bean :
                        extendInfos) {

                    if (bean.getTitle().contains("款方式")) {
                        tvRepayType.setText(Html.fromHtml("【还款方式】 <font color='#666666'>" + bean.getContent() + "</font>"));
                    }
                    if (bean.getTitle().contains("计息")) {
                        tvInfoInterestType.setText(Html.fromHtml("【计息方式】  <font color='#666666'>" + bean.getContent() + "</font>"));
                    }
                    if (bean.getTitle().contains("出借")) {
                        tvInfoCondition.setText(Html.fromHtml("【出借条件】  <font color='#666666'>" + bean.getContent() + "</font>"));
                    }

                }


            }

            @Override
            public void onLogicError(int code, String msg) {
                LogUtils.e(msg);
                refreshLayout.finishRefresh();
                //dismissDialog();
            }

            @Override
            public void onError(IOException e) {
                refreshLayout.finishRefresh();
                LogUtils.e(e.toString());
                //dismissDialog();
            }
        });














      /*  showWaitDialog("加载中...", true, "");
        OkHttpUtils.post()
                .url(UrlConfig.PRODUCT_DETAIL)
                .addParams("pid", pid)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String response) {

                        //LogPrintUtil.e("LF--->产品详情", response);
                        JSONObject obj = JSON.parseObject(response);
                        dismissDialog();
                        ptrProDetail.refreshComplete();
                        if (obj.getBoolean(("success"))) {
                            JSONObject map = obj.getJSONObject("map");
                            balance = map.getString("balance");//当前登录用户余额
                            isOldUser = map.getBoolean("isOldUser");//是否是老用户
                            isShowLabel = map.getBoolean("isShowLabel");//是否显示激活体验金
                            tpwdFlag = map.getInteger("tpwdFlag");

                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("tpwdFlag", tpwdFlag + "");
                            editor.commit();
                            //interestType = map.getInteger("interestType");
                            startTime = map.getString("nowTime");//计息日期
                            sysDate = map.getString("sysDate");//计息日期
                            JSONArray extendInfos = map.getJSONArray("extendInfos");//产品介绍
                            proIntroduceList = JSON.parseArray(extendInfos.toJSONString(), ProIntroduceBean.class);
                            bundle.putSerializable("proIntroduceList", (Serializable) proIntroduceList);
                            JSONObject info = map.getJSONObject("info");
                            principleH5 = info.getString("principleH5");//产品原理图
                            String borrower = info.getString("borrower");
                            String accept = info.getString("accept");
                            String introduce = info.getString("introduce");
                            String repaySource = info.getString("repaySource");
                            bundle.putString("borrower", borrower);
                            bundle.putString("introduce", introduce);
                            bundle.putString("accept", accept);
                            bundle.putString("repaySource", repaySource);
                            bundle.putString("principleH5", principleH5);
                            fullName = info.getString("fullName");
                            rate = info.getDouble("rate");//利率
                            activityRate = info.getDouble("activityRate");//活动利率
                            deadline = info.getString("deadline");
                            amount = info.getString("amount");//出借总金额
                            increasAmount = info.getString("increasAmount");
                            surplusAmount = info.getString("surplusAmount");//剩余可投金额
                            leastaAmount = info.getString("leastaAmount");//起投金额
                            maxAmount = info.getString("maxAmount");//最多出借金额
                            establishTime = info.getString("establish");//成立日期
                            endTime = info.getString("expireDate");//回款日期
                            endDate = info.getString("endDate");//募集结束日期
                            pert = info.getString("pert");

                            distributionId = info.getString("distributionId");//是展示排行榜还是出借记录
                            LogUtils.e("distributionId" + distributionId);
                            titleCentertextview.setText(fullName);
                            titleCentertextviewAdd.setText(fullName);
                            tv_prodetail_rate.setText(rate + "");
                            tv_prodetail_rate_activity.setText(activityRate + "");
                            tv_prodetail_deadline.setText(deadline + "天");
                            tv_prodetail_leastaAmount.setText(leastaAmount + "元");
                            tv_prodetail_balance.setText(surplusAmount + "元");
                            tv_prodetail_total.setText(amount + "元");
                            histogramView.setPercent(Integer.valueOf(stringCut.pertCut(pert)).intValue());
                            wave_view.setProgress(Integer.valueOf(stringCut.pertCut(pert)).intValue());

                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy.MM.dd");
                            String date_interest = simpleDateFormat.format(new Date(Long.valueOf(startTime)));
                            String date_establish = simpleDateFormat.format(new Date(Long.valueOf(establishTime)));
                            String date_expire = simpleDateFormat.format(new Date(Long.valueOf(endTime)));
                            //tv_day_invest.setText(date_establish);
                            tv_day_establish.setText(date_establish);
                            tv_day_returned.setText(date_expire);

                            isRoundOff = map.getBoolean("isRoundOff");//是否显示尾单
                            roundOffMoney = map.getString("roundOffMoney");//尾单奖励

                            //判断可用哪种优惠券的标签
                            if ("1".equals(info.getString("isInterest"))) {
                                tv_prodetail_interest.setVisibility(View.VISIBLE);
                            }
                            if ("1".equals(info.getString("isCash"))) {
                                tv_prodetail_red.setVisibility(View.VISIBLE);
                            }
                            if ("1".equals(info.getString("isDouble"))) {
                                tv_prodetail_double.setVisibility(View.VISIBLE);
                            }
                            if ("1".equals(info.getString("isHot"))) {
                                tv_prodetail_hot.setVisibility(View.VISIBLE);
                            }
                            if (isRoundOff) {
                                tv_prodetail_saowei.setVisibility(View.VISIBLE);
                            } else {
                                tv_prodetail_saowei.setVisibility(View.GONE);
                            }

                            tag = info.getString("tag");//活动标签(可能多个) eg:"tag": "第一项,第二项"
                            if (tag != null && !tag.equals("")) {
                                ll_tag_row2.setVisibility(View.VISIBLE);
                                String[] tags = tag.split(",");
                                if (tags.length == 1) {
                                    tv_tag1.setVisibility(View.VISIBLE);
                                    tv_tag1.setText(tags[0]);
                                } else if (tags.length == 2) {
                                    tv_tag1.setVisibility(View.VISIBLE);
                                    tv_tag2.setVisibility(View.VISIBLE);
                                    tv_tag1.setText(tags[0]);
                                    tv_tag2.setText(tags[1]);
                                } else if (tags.length == 3) {
                                    tv_tag1.setVisibility(View.VISIBLE);
                                    tv_tag2.setVisibility(View.VISIBLE);
                                    tv_tag3.setVisibility(View.VISIBLE);
                                    tv_tag1.setText(tags[0]);
                                    tv_tag2.setText(tags[1]);
                                    tv_tag3.setText(tags[2]);
                                } else if (tags.length == 4) {
                                    tv_tag1.setVisibility(View.VISIBLE);
                                    tv_tag2.setVisibility(View.VISIBLE);
                                    tv_tag3.setVisibility(View.VISIBLE);
                                    tv_tag4.setVisibility(View.VISIBLE);
                                    tv_tag1.setText(tags[0]);
                                    tv_tag2.setText(tags[1]);
                                    tv_tag3.setText(tags[2]);
                                    tv_tag4.setText(tags[3]);
                                }

                            } else {
                                ll_tag_row2.setVisibility(View.GONE);
                            }
                            //没有任何一个标签，则隐藏布局
                            if ("0".equals(info.getString("isInterest")) && "0".equals(info.getString("isCash")) && "0".equals(info.getString("isDouble")) && "0".equals(info.getString("isHot")) && TextUtils.isEmpty(tag)) {
                                ll_tag_row1.setVisibility(View.GONE);
                            }

                            //滚动的活动公告
                            JSONArray objnotice = map.getJSONArray("inProgressActivity");
                            if (objnotice != null) {
                                noticeList = JSON.parseArray(objnotice.toJSONString(), NoticeBean.class);
                                List<String> infos = new ArrayList<>();
                                for (int i = 0; i < noticeList.size(); i++) {
                                    infos.add(noticeList.get(i).getTitle());
                                }
                                ll_notice.setVisibility(View.VISIBLE);
                                marqueeView.startWithList(infos);
                                //公告跳转
                                marqueeView.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position, TextView textView) {
                                        NoticeBean noticeBean = noticeList.get(position);

                                        if (noticeBean.getAppUrl().contains("jumpTo=3")) { //邀请好友三重礼
                                            finish();
                                            LocalApplication.getInstance().getMainActivity().setCheckedFram(3);//返利
                                        } else {
                                            startActivity(new Intent(NewProductDetailActivity.this, WebViewActivity.class)
                                                    .putExtra("URL", noticeBean.getAppUrl() + "?app=true")
                                                    .putExtra("TITLE", noticeBean.getTitle())
                                                    .putExtra("AFID", noticeBean.getId() + ""));
                                        }
                                    }
                                });
                            } else {
                                ll_notice.setVisibility(View.GONE);
                            }

                            if (info.getInteger("repayType") == 1) {
                                repayType = "到期还本付息";
                            } else if (info.getInteger("repayType") == 2) {
                                repayType = "按月付息到期还本";
                            }
                            tv_prodetail_repaytype.setText(repayType);

                            //万元预计收益
                            rate_expect = Double.valueOf(NewProductDetailActivity.this.rate) / 100;
                            exRate_expect = Double.valueOf(NewProductDetailActivity.this.activityRate) / 100;
                            Double day = Double.valueOf(NewProductDetailActivity.this.deadline);
                            shouyi_wanyuan = 10000 * (rate_expect + exRate_expect) * day / 360;
                            int sy = (int) (shouyi_wanyuan * 100);
                            shouyi_wanyuan = sy / 100.0;
                            LogUtils.i("--->shouyi_wanyuan：" + shouyi_wanyuan);
                            tvWanShouyi.setText(shouyi_wanyuan + "");

                            couponList = map.getJSONArray("couponList");//优惠券列表
                            listConpons = JSON.parseArray(couponList.toJSONString(), CouponsBean.class);
                            listFavourable = map.getJSONArray("listFavourable");//体验金列表
                            listTiYanJin = JSON.parseArray(listFavourable.toJSONString(), CouponsBean.class);

                            //如果是预售标，显示活动倒计时，不能点击
                            if (is_presell_plan) {
                                ll_presell_timer.setVisibility(View.VISIBLE);
                                btProdetailInvest.setVisibility(View.GONE);
                                //btProdetailInvest.setBackgroundResource(R.drawable.bg_corner_gray);
                                btProdetailInvest.setEnabled(false);
                                btProdetailInvest.setClickable(false);

                                //活动开始时间
                                String startDate = getIntent().getStringExtra("startDate");
                                long startMillis = Long.valueOf(startDate);
                                //获取服务器当前时间秒值
                                long curMillis = Long.valueOf(sysDate);
                                time_timer = (startMillis) / 1000 - curMillis;
                                handler_timer.postDelayed(runnable, 1000);

                            } else {
                                ll_presell_timer.setVisibility(View.GONE);
                                btProdetailInvest.setVisibility(View.VISIBLE);
                                //btProdetailInvest.setBackgroundResource(R.mipmap.bg_button);
                                btProdetailInvest.setEnabled(true);
                                btProdetailInvest.setClickable(true);
                            }

                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {

                        ptrProDetail.refreshComplete();
                        dismissDialog();
                        ToastMaker.showShortToast("网络错误，请检查");
                    }
                });*/
    }

    /**
     * 获取资料清单 等列表
     */
    private void getMoreDetail() {

        PostParams params = new PostParams();
        HashMap<String, Object> map = params.getParams();
        map.put("pid", pid + "");
        map.put("uid", preferences.getString("uid", ""));
        map.put("type", 2 + "");
        map.put("version", UrlConfig.version);
        map.put("channel", "2");


        OkHttpEngine.create().setHeaders().post(UrlConfig.DETAIL_INFO, params, new SimpleHttpCallback() {
            @Override
            public void onLogicSuccess(String data) {
                LogUtils.e(data);
                //dismissDialog();


                ProductDetailInfo productDetail = GsonUtil.parseJsonToBean(data, ProductDetailInfo.class);

                List<ProductDetailInfo.PicListBean> picList = productDetail.getPicList();
                if (picList.size() > 0) {

                    proIntroduceList.clear();
                    proIntroduceList.addAll(picList);
                    productPicAdapter.notifyDataSetChanged();
                }
            }


            @Override
            public void onLogicError(int code, String msg) {
                LogUtils.e(msg);
                //dismissDialog();
            }

            @Override
            public void onError(IOException e) {
                LogUtils.e(e.toString());
                //dismissDialog();
            }
        });


    }

    private LinearLayout layout;
    private PopupWindow popupWindow;
    private ImageView iv_regist;

    public void showPopupWindow1(String url_image) {
        // 加载布局
        layout = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.pop_image_big, null);
        // 找到布局的控件
        // 实例化popupWindow
        popupWindow = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        iv_regist = (ImageView) (layout).findViewById(R.id.iv_regist);


        Glide.with(this).load(url_image).placeholder(R.drawable.bg_activity_fail)
                .error(R.drawable.bg_activity_fail).into(iv_regist);


        // 控制键盘是否可以获得焦点
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
//		popupWindow.setBackgroundDrawable(new PaintDrawable());
//		popupWindow.setOutsideTouchable(true);

//		// 设置popupWindow弹出窗体的背景
//		WindowManager manager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        // 监听
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {

                backgroundAlpha(1f);
            }
        });
        popupWindow.showAsDropDown(iv_regist);
//		backgroundAlpha(0.5f);
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     **/

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0
        this.getWindow().setAttributes(lp);
    }

    @Override
    protected synchronized void onDestroy() {
        super.onDestroy();
        isFirstEnter = true;
    }
}
