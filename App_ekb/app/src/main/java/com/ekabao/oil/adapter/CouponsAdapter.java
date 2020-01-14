package com.ekabao.oil.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ekabao.oil.R;
import com.ekabao.oil.adapter.viewholder.BaseViewHolder;
import com.ekabao.oil.bean.CouponsBean;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.util.LogUtils;
import com.ekabao.oil.util.StringCut;

import java.util.List;

/**
 * 易卡宝  App
 *
 * @time 2018/7/19 17:12
 * Created by lj on 2018/7/19 17:12.
 */

public class CouponsAdapter extends BaseRecyclerViewAdapter {
    List<CouponsBean> lslbs;
    private String amount;// 限制的金额
    private int deadline = 0;//限制的时间

    public CouponsAdapter(RecyclerView view, List list, int itemLayoutId) {
        super(view, list, itemLayoutId);
        this.lslbs = list;
    }

    public CouponsAdapter(RecyclerView view, List list, int itemLayoutId, String money) {
        super(view, list, itemLayoutId);
        this.lslbs = list;
        this.amount = money;
    }

    public CouponsAdapter(RecyclerView view, List list, int itemLayoutId, String money, int deadline) {
        super(view, list, itemLayoutId);
        this.lslbs = list;
        this.amount = money;
        this.deadline = deadline;
    }


    @Override
    public void bindConvert(BaseViewHolder holder, int position, Object item, boolean isScrolling) {
        CouponsBean cb = lslbs.get(position);

        ImageView ivBackground = holder.getView(R.id.iv_background);
        TextView tvMoney = holder.getView(R.id.tv_money);
        TextView tvLimitMoney = holder.getView(R.id.tv_limit_money);
        TextView tvName = holder.getView(R.id.tv_name);
        TextView tvLimitDay = holder.getView(R.id.tv_limit_day);
        TextView tvTime = holder.getView(R.id.tv_time);
        TextView tvState = holder.getView(R.id.tv_state);


        ivBackground.setBackgroundResource(R.drawable.bg_fanxian);
        //"返现红包-"
        tvName.setText(cb.getName());
        //vh.tv_wenan1.setText(cb.getRemark());//发放途径
        if (cb.getEnableAmount()==0){
            tvLimitMoney.setVisibility(View.GONE);

        }else {
            tvLimitMoney.setVisibility(View.VISIBLE);
            tvLimitMoney.setText("满" + StringCut.getNumKbs(cb.getEnableAmount()) + "元使用");

        }

//"单笔出借满"+StringCut.getNumKbs(cb.getEnableAmount())+"元，出借期限≥"+cb.getProductDeadline()+"天"

        tvLimitDay.setText("适用套餐时间≥" + cb.getProductDeadline() + "个月");

        String xian_h = "￥" + StringCut.getNumKbs(cb.getAmount());
        SpannableString ss = new SpannableString(xian_h);
        ss.setSpan(new RelativeSizeSpan(0.6f), 0, 1, TypedValue.COMPLEX_UNIT_PX);

        tvMoney.setText(xian_h);

        // tvMoney.setText("￥" + StringCut.getNumKbs(cb.getAmount()));
        if (cb.getExpireDate() != 0) {
            tvTime.setText("有效期至：" + StringCut.getDateYearToString(cb.getExpireDate()));
        } else {
            // vh.tv_dd.setText("");
            tvTime.setText("永久有效");
        }


        switch (cb.getType()) {
            case 1:// 油卡套餐
               /* ivBackground.setBackgroundResource(R.drawable.bg_fanxian);
                //"返现红包-"
                tvName.setText(cb.getName());
                //vh.tv_wenan1.setText(cb.getRemark());//发放途径
                tvLimitMoney.setText("满" + StringCut.getNumKbs(cb.getEnableAmount()) + "元使用");
//"单笔出借满"+StringCut.getNumKbs(cb.getEnableAmount())+"元，出借期限≥"+cb.getProductDeadline()+"天"

                String xian_h = "￥" + StringCut.getNumKbs(cb.getAmount());
                SpannableString ss = new SpannableString(xian_h);
                ss.setSpan(new RelativeSizeSpan(0.6f), 0, 1, TypedValue.COMPLEX_UNIT_PX);

                tvMoney.setText(xian_h);

                // tvMoney.setText("￥" + StringCut.getNumKbs(cb.getAmount()));
                if (cb.getExpireDate() != 0) {
                    tvTime.setText("有效期至：" + StringCut.getDateYearToString(cb.getExpireDate()));
                } else {
                    // vh.tv_dd.setText("");
                    tvTime.setText("永久有效");
                }*/

                tvLimitDay.setText("加油套餐≥" + cb.getProductDeadline() + "个月");


                break;

            case 2:// 油卡直冲
                // 体验 case 3:
               /* ivBackground.setBackgroundResource(R.drawable.bg_jiaxi);

                tvName.setText("加息券-" + cb.getName());
                String rate_h = StringCut.getNumKbs(cb.getRaisedRates()) + "%";
                //vh.tv_wenan1.setText( cb.getRemark());
                SpannableString sss = new SpannableString(rate_h);
                sss.setSpan(new RelativeSizeSpan(0.6f), rate_h.length() - 1, rate_h.length(), TypedValue.COMPLEX_UNIT_PX);
                tvMoney.setText(rate_h);

                tvLimitMoney.setText("满" + StringCut.getNumKbs(cb.getEnableAmount()) + "元使用");
                tvLimitDay.setText("适用出借期限≥" + cb.getProductDeadline() + "天");

                if (cb.getExpireDate() != 0) {
                    tvTime.setText("有效期至：" + StringCut.getDateYearToString(cb.getExpireDate()));
                } else {
                    // vh.tv_dd.setText("");
                    tvTime.setText("永久有效");
                }*/
                tvLimitDay.setText("油卡直冲");
                break;
            case 4:// 话费套餐

               /* ivBackground.setBackgroundResource(R.drawable.bg_fanbei);

                String text = "翻倍券-" + cb.getName();

                tvName.setText(text);


                String text1 = "收益率×" + StringCut.getNumKbs(cb.getMultiple());
                SpannableString spannableString = new SpannableString(text1);
                spannableString.setSpan(new AbsoluteSizeSpan(18, true), 0, text1.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

                tvMoney.setText(spannableString);

                tvLimitMoney.setText("满" + StringCut.getNumKbs(cb.getEnableAmount()) + "元使用");
                tvLimitDay.setText("适用出借期限≥" + cb.getProductDeadline() + "天");

                if (cb.getExpireDate() != 0) {
                    tvTime.setText("有效期至：" + StringCut.getDateYearToString(cb.getExpireDate()));
                } else {
                    // vh.tv_dd.setText("");
                    tvTime.setText("永久有效");
                }*/
                tvLimitDay.setText("话费套餐≥" + cb.getProductDeadline() + "个月");
                break;
            case 5: //"话费直冲"
                tvLimitDay.setText("话费直冲");
                break;
            default:
                tvLimitDay.setText("");
                break;


        }

        switch (cb.getStatus()) {
            case 3:// 选择优惠券的时候金额不可用
                ivBackground.setBackgroundResource(R.drawable.bg_unuse);
                tvState.setTextColor(LocalApplication.context.getResources().getColor(R.color.text_99));
//                vh.cover_tv.setVisibility(View.VISIBLE);
                break;
            case 1:// 选择优惠券的时候金额不可用
                ivBackground.setBackgroundResource(R.drawable.bg_unuse);
                tvState.setText("已使用");
                tvState.setTextColor(LocalApplication.context.getResources().getColor(R.color.text_99));
//                vh.cover_tv.setVisibility(View.VISIBLE);
                break;
            case 2:// 选择优惠券的时候金额不可用
                ivBackground.setBackgroundResource(R.drawable.bg_unuse);
//                vh.cover_tv.setVisibility(View.VISIBLE);
                tvState.setText("已过期");
                tvState.setTextColor(LocalApplication.context.getResources().getColor(R.color.text_99));
                break;
            default:
                tvState.setText("立即使用");
                //tvState.setTextColor(LocalApplication.context.getResources().getColor(R.color.text_red));
                break;
        }

        //LogUtils.e("amount"+amount+!TextUtils.isEmpty(amount));
        if (!TextUtils.isEmpty(amount)) {
            //vh.tv_tab.setVisibility(View.GONE);
            int type = cb.getType();

            //  LogUtils.e(lslbs.get(i).getEnableAmount()+"优惠券"+amount+"deadline"+deadline+"/"+lslbs.get(i).getProductDeadline()+(deadline >= lslbs.get(i).getProductDeadline()));

                /*LogUtils.e("type"+type+"amount"+amount+(Double.valueOf(amount) >= lslbs.get(i).getEnableAmount())
                        + (deadline >= lslbs.get(i).getProductDeadline()));*/

            if (type == 1 || type == 4) {
                LogUtils.e(cb.getEnableAmount()+"用" +Double.valueOf(amount)+"/"+ (Double.valueOf(amount) >= cb.getEnableAmount()));
                if (Double.valueOf(amount) >= cb.getEnableAmount() && deadline >= cb.getProductDeadline()) {
                    LogUtils.e("立即使用");
                    ivBackground.setBackgroundResource(R.drawable.bg_fanxian);
                    lslbs.get(position).setStatus(0);
                    tvState.setText("立即使用");

                } else {
                        ivBackground.setBackgroundResource(R.drawable.bg_unuse);
                        lslbs.get(position).setStatus(3);
                        tvState.setText("无法使用");
                }
            } else {
                if (Double.valueOf(amount) >= cb.getEnableAmount()) {
                    lslbs.get(position).setStatus(0);
                    tvState.setText("立即使用");
                } else {
                    ivBackground.setBackgroundResource(R.drawable.bg_unuse);
                    lslbs.get(position).setStatus(3);
                    tvState.setText("无法使用");
                }
            }


           /* for (int i = 0; i < lslbs.size(); i++) {
                //lslbs.get(position).getEnableAmount())



                if (Double.valueOf(amount) < (lslbs.get(position).getProductDeadline())) {
                    ivBackground.setBackgroundResource(R.drawable.bg_unuse);
                    lslbs.get(position).setStatus(3);
                    tvState.setText("无法使用");
                } else {
                    //  vh.ll_conpons_bg.setBackgroundResource(R.mipmap.bg_conpons_red);
                    lslbs.get(position).setStatus(0);
                    tvState.setText("立即使用");
                }
            }*/
        }

    }


}
