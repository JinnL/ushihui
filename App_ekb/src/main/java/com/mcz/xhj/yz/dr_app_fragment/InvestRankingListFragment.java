package com.mcz.xhj.yz.dr_app_fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_bean.bean_Detail_Info;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.LogUtils;
import com.mcz.xhj.yz.dr_util.stringCut;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.BindView;
import cn.bleu.widget.slidedetails.SlideDetailsLayout;
import okhttp3.Call;

/**
 * Created by zhulang on 2017/8/9.
 * 描述：2.0版 普通标详情页-投资排行
 */

public class InvestRankingListFragment extends BaseFragment {
    @BindView(R.id.tv_rank_text)
    TextView tv_rank_text;
    @BindView(R.id.tv_rank)
    TextView tvRank;
    @BindView(R.id.tv_ming)
    TextView tv_ming;
    @BindView(R.id.tv_reward)
    TextView tv_reward;
    @BindView(R.id.list_ranking)
    ListView listRanking;
    @BindView(R.id.ll_norecord)
    LinearLayout ll_norecord;

    private SharedPreferences preferences;
    private String pid, uid, ptype;
    private List<bean_Detail_Info> rankList;
    private SlideDetailsLayout mSlideDetailsLayout;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mSlideDetailsLayout = (SlideDetailsLayout) activity.findViewById(R.id.slidedetails);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_invest_rankinglist;
    }

    @Override
    protected void initParams() {
        preferences = LocalApplication.getInstance().sharereferences;
        pid = getArguments().getString("pid");
        uid = getArguments().getString("uid");
        ptype = getArguments().getString("ptype");
        LogUtils.i("投资排行  pid==" + pid + " ,uid==" + uid + " ,type==" + ptype);
        getMoreDetail();

        listRanking.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (listRanking.getFirstVisiblePosition() == 0) {
                    mSlideDetailsLayout.smoothOpen(false);
                } else {
                    mSlideDetailsLayout.smoothOpen(true);
                }
                return false;
            }
        });
    }

    private void getMoreDetail() {
        showWaitDialog("加载中...", true, "");
        OkHttpUtils.post()
                .url(UrlConfig.DETAIL_INFO)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("pid", pid)
                .addParams("type", ptype)
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response) {
                        // TODO Auto-generated method stub
                        dismissDialog();
                        LogUtils.i("--->投资排行：" + response);
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
                            JSONObject map = obj.getJSONObject("map");
                            String distribution_persion_count = map.getString("distribution_persion_count");
                            String tender_money_distribution_sum = map.getString("tender_money_distribution_sum");
                            //修改文本中的部分文字颜色
                            String content = "本期累计投资总额前" + distribution_persion_count + "名可分享" + tender_money_distribution_sum + "元现金";
                            SpannableString spannableString = new SpannableString(content);
                            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF5111")), 9, 9 + (distribution_persion_count + "").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF5111")), 9 + (distribution_persion_count + "").length() + 4, 9 + (distribution_persion_count + "").length() + 4 + (tender_money_distribution_sum + "").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            tv_reward.setText(spannableString);

                            JSONArray investList = map.getJSONArray("investList");
                            rankList = JSON.parseArray(investList.toJSONString(), bean_Detail_Info.class);
                            if (rankList.size() > 0) {
                                ll_norecord.setVisibility(View.GONE);
                                getRank(rankList, tvRank);
                                listRanking.setAdapter(new RankingListAdapter(mContext, rankList));

                            } else {
                                ll_norecord.setVisibility(View.VISIBLE);
                                tv_rank_text.setText("您暂未上榜");
                            }

                        } else if ("9999".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("系统错误");
                        } else {
                            ToastMaker.showShortToast("系统错误");
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

    private void getRank(List<bean_Detail_Info> rankList, TextView tvRank) {
        if (TextUtils.isEmpty(uid)) {
            tv_rank_text.setText("请登录之后查看您的排名");
            tvRank.setVisibility(View.GONE);
            tv_ming.setVisibility(View.GONE);
        } else {
            tv_rank_text.setText("您当前排名第");
            tvRank.setVisibility(View.VISIBLE);
            tv_ming.setVisibility(View.VISIBLE);
            int scort = getScort();
            LogUtils.i("--->投资排行 X="+scort);
            if(scort >0){
                tvRank.setText(scort+"");
            }else{
                tv_rank_text.setText("您暂未上榜");
                tvRank.setVisibility(View.GONE);
                tv_ming.setVisibility(View.GONE);
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
        for (int i = 0; i < rankList.size(); i++) {
            if (rankList.get(i).getUid() == Integer.valueOf(uid)) {
                x = i + 1;
                return x;
            }
        }
        return x;
    }

    class RankingListAdapter extends BaseAdapter {
        private Context mContext;
        private List<bean_Detail_Info> rankingList;

        /*
        * 定义两种类型的item
        * */
        public static final int ITEM_TYPE_1 = 0;
        public static final int ITEM_TYPE_2 = 1;

        public RankingListAdapter(Context mContext, List<bean_Detail_Info> rankingList) {
            this.mContext = mContext;
            this.rankingList = rankingList;
        }

        @Override
        public int getCount() {
            return rankingList.size();
        }

        @Override
        public Object getItem(int position) {
            return rankingList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getViewTypeCount() {
            //因为有两种视图，所以返回2
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            int pos = position / 5;
            if (pos == 0) {
                return ITEM_TYPE_1;
            } else {
                return ITEM_TYPE_2;
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            int viewType = getItemViewType(position);
            if (convertView == null) {
                holder = new ViewHolder();
                switch (viewType) {
                    case ITEM_TYPE_1:
                        convertView = View.inflate(mContext, R.layout.item_ranking_list, null);
                        holder.ll_rangking_list = (LinearLayout) convertView.findViewById(R.id.ll_rangking_list);
                        holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                        holder.tv_invest_amount = (TextView) convertView.findViewById(R.id.tv_invest_amount);
                        holder.tv_invest_time = (TextView) convertView.findViewById(R.id.tv_invest_time);
                        holder.tv_item_reward = (TextView) convertView.findViewById(R.id.tv_item_reward);
                        holder.btn_take_position = (TextView) convertView.findViewById(R.id.btn_take_position);
                        break;
                    case ITEM_TYPE_2:
                        convertView = View.inflate(mContext, R.layout.item_ranking_list2, null);
                        holder.tv_rank_number = (TextView) convertView.findViewById(R.id.tv_rank_number);
                        holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name2);
                        holder.tv_invest_amount = (TextView) convertView.findViewById(R.id.tv_invest_amount2);
                        holder.tv_invest_time = (TextView) convertView.findViewById(R.id.tv_invest_time2);
                        break;
                    default:
                        break;
                }
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            bean_Detail_Info detail_info = rankingList.get(position);
            switch (viewType) {
                case ITEM_TYPE_1:
                    int rank = position % 5;
                    if (rank == 0) {
                        holder.ll_rangking_list.setBackgroundResource(R.mipmap.rank_first);
                    } else if (rank == 1) {
                        holder.ll_rangking_list.setBackgroundResource(R.mipmap.rank_second);
                    } else if (rank == 2) {
                        holder.ll_rangking_list.setBackgroundResource(R.mipmap.rank_third);
                    } else if (rank == 3) {
                        holder.ll_rangking_list.setBackgroundResource(R.mipmap.rank_fourth);
                    } else if (rank == 4) {
                        holder.ll_rangking_list.setBackgroundResource(R.mipmap.rank_fifth);
                    }
                    holder.tv_name.setText(detail_info.getRealName());
                    holder.tv_invest_amount.setText(detail_info.getTenderAccountSum());
                    holder.tv_invest_time.setText(stringCut.getDateTimeToStringheng(Long.parseLong(detail_info.getInvestTime())));
                    holder.tv_item_reward.setText("本期投资满标后，第" + (rank + 1) + "名可获得" + detail_info.getTender_money_distribution() + "元现金");
                    holder.btn_take_position.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mSlideDetailsLayout.smoothClose(true);

                        }
                    });
                    break;
                case ITEM_TYPE_2:

                    holder.tv_rank_number.setText(position + 1 + "");
                    holder.tv_name.setText(detail_info.getRealName());
                    holder.tv_invest_amount.setText(detail_info.getTenderAccountSum());
                    holder.tv_invest_time.setText(stringCut.getDateTimeToStringheng(Long.parseLong(detail_info.getInvestTime())));
                    break;
            }

            return convertView;
        }

        public class ViewHolder {
            private LinearLayout ll_rangking_list;
            private TextView tv_rank_number;
            private TextView tv_name;
            private TextView tv_invest_amount;
            private TextView tv_invest_time;
            private TextView tv_item_reward;
            private TextView btn_take_position;
        }
    }
}
