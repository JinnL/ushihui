package com.ekabao.oil.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ekabao.oil.R;
import com.ekabao.oil.adapter.ProductInvestAdapter;
import com.ekabao.oil.bean.ProductDetailInfo;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.OkHttpEngine;
import com.ekabao.oil.http.PostParams;
import com.ekabao.oil.http.SimpleHttpCallback;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.ui.view.DialogMaker;
import com.ekabao.oil.util.GsonUtil;
import com.ekabao.oil.util.LogUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScoreboardActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.title_leftimageview)
    ImageView titleLeftimageview;
    @BindView(R.id.title_centertextview)
    TextView titleCentertextview;
    @BindView(R.id.title_righttextview)
    TextView titleRighttextview;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_distributionMoney)
    TextView tvDistributionMoney;
    @BindView(R.id.rv_scoreboard)
    RecyclerView rvScoreboard;


    private SharedPreferences preferences;

    private int pid;  //产品id
    private int type;  //类型 2有排行奖励的

    private List<ProductDetailInfo.InvestListBean> proInvestList =new ArrayList<>();
    private ProductInvestAdapter productInvestAdapter;
    private int distribution_persion_count;
    private String tender_money_distribution_sum="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);
        ButterKnife.bind(this);


        titleLeftimageview.setOnClickListener(this);
        titleRighttextview.setOnClickListener(this);

        preferences = LocalApplication.sharereferences;

        Intent intent = getIntent();
        pid = intent.getIntExtra("pid", 0);
        type= intent.getIntExtra("type", 0);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LocalApplication.getInstance());
        rvScoreboard.setLayoutManager(linearLayoutManager);

        productInvestAdapter = new ProductInvestAdapter(proInvestList,type);

        rvScoreboard.setAdapter(productInvestAdapter);


        getMoreDetail();

        productInvestAdapter.setOnItemClickLitener(new ProductInvestAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                //我要抢位
                setResult(Activity.RESULT_OK);
                finish();
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.title_leftimageview: //返回

                finish();
                break;
            case R.id.title_righttextview: //规则
                String text="本期项目累计出借额前"+distribution_persion_count+"名可分享"+tender_money_distribution_sum+"元现金奖励，将于本项目成立后1小时内转入账户余额，如出借额相同则以最先达成时间者为优先";
                DialogMaker.showOneBtDialog(ScoreboardActivity.this,
                        "排行榜规则", text, "我知道了", callBack, "规则");
                // finish();

                break;

        }


    }

    DialogMaker.DialogCallBack callBack = new DialogMaker.DialogCallBack() {
        @Override
        public void onButtonClicked(Dialog dialog, int position, Object tag) {

        }

        @Override
        public void onCancelDialog(Dialog dialog, Object tag) {

        }
    };
    /**
     * 获取资料清单 等列表
     */
    private void getMoreDetail() {

        PostParams params = new PostParams();
        HashMap<String, Object> map = params.getParams();
        map.put("pid", pid + "");
        //map.put("uid", preferences.getString("uid", ""));
        map.put("uid", "46");
        map.put("type", 2 + "");
        map.put("version", UrlConfig.version);
        map.put("channel", "2");


        OkHttpEngine.create().setHeaders().post(UrlConfig.DETAIL_INFO, params, new SimpleHttpCallback() {
            @Override
            public void onLogicSuccess(String data) {
                LogUtils.e(data);
                //dismissDialog();


                ProductDetailInfo productDetail = GsonUtil.parseJsonToBean(data, ProductDetailInfo.class);

                //List<ProductDetailInfo.PicListBean> picList = productDetail.getPicList();
                distribution_persion_count = productDetail.getDistribution_persion_count();

                if (!TextUtils.isEmpty(productDetail.getTender_money_distribution_sum())){
                   // LogUtils.e("排行榜规则"+productDetail.getTender_money_distribution_sum());
                    tender_money_distribution_sum = productDetail.getTender_money_distribution_sum();
                }

                //修改文本中的部分文字颜色
             /*   String content = "本期累计出借总额前" + distribution_persion_count + "名可分享" + tender_money_distribution_sum + "元现金";
                SpannableString spannableString = new SpannableString(content);
                spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF5111")), 9, 9 + (distribution_persion_count + "").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF5111")), 9 + (distribution_persion_count + "").length() + 4, 9 + (distribution_persion_count + "").length() + 4 + (tender_money_distribution_sum + "").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                tvDistributionMoney.setText(spannableString);*/

                if (type==0){
                    tvDistributionMoney.setVisibility(View.GONE);
                }else {
                    tvDistributionMoney.setVisibility(View.VISIBLE);
                    tvDistributionMoney.setText("本期累计出借总额前"+ distribution_persion_count
                            +"名可分享"+ tender_money_distribution_sum +"元现金");
                }

                List<ProductDetailInfo.InvestListBean> investList = productDetail.getInvestList();


                if (investList.size() > 0) {

                    proInvestList.clear();
                    proInvestList.addAll(investList);
                    getRank(investList, tvDistributionMoney);
                    productInvestAdapter.notifyDataSetChanged();
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

    private void getRank(List<ProductDetailInfo.InvestListBean> rankList, TextView tvRank) {
        if (TextUtils.isEmpty(preferences.getString("uid", ""))) {
            tvName.setText("请登录之后查看您的排名");
            //tvRank.setVisibility(View.GONE);

        } else {
            int scort = getScort();
            LogUtils.e("--->出借排行 X="+scort);
            if(scort >0){
                tvName.setText("您当前排名第"+scort+"位");
            }else{
                tvName.setText("您还未出借本项目");
            }
            /*//遍历
            for (int i = 0; i < rankList.size(); i++) {
                if (rankList.get(i).getUid() == Integer.valueOf(uid)) {
                    tvRank.setText(i + 1 + "");
                } else {
                    tvRank.setText(0 + "");
                }
            }*/
        }
    }

    private int getScort(){
        int x = 0;
        for (int i = 0; i < proInvestList.size(); i++) {
            if (proInvestList.get(i).getUid() == Integer.valueOf(preferences.getString("uid", ""))) {
                x = i + 1;
                return x;
            }
        }
        return x;
    }

}
