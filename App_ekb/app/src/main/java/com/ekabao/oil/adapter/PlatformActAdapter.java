package com.ekabao.oil.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ekabao.oil.R;
import com.ekabao.oil.bean.Activity;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.ui.activity.InviteFriendsActivity;
import com.ekabao.oil.ui.activity.LoginActivity;
import com.ekabao.oil.ui.activity.WebViewActivity;
import com.ekabao.oil.util.GlideRoundTransform;
import com.ekabao.oil.util.LogUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlatformActAdapter extends RecyclerView.Adapter<PlatformActAdapter.ActivityViewHolder> {
    private Context mContext;
    private List<Activity.PageBean.RowsBean> list;
    private SharedPreferences preferences = LocalApplication.sharereferences;

    public PlatformActAdapter(Context mContext, List<Activity.PageBean.RowsBean> list) {
        this.mContext = mContext;
        this.list = list;
    }

    public void setList(List<Activity.PageBean.RowsBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public ActivityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ActivityViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_welfare_activity_2, parent, false));
    }

    @Override
    public void onBindViewHolder(ActivityViewHolder holder, final int position) {

        Activity.PageBean.RowsBean bean = list.get(position);

        holder.tvTitle.setText(bean.getTitle());
        holder.tvContent.setText(bean.getActivityDate());

        int status = bean.getStatus();

        switch (status) {
            case 1:
                holder.tvState.setBackgroundResource(R.drawable.bg_welfare_status_1);
                holder.viewFail.setVisibility(View.GONE);
                break;
            case 2:
                holder. tvState.setBackgroundResource(R.drawable.bg_welfare_status_2);
                holder. viewFail.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
        GlideRoundTransform glideRoundTransform = new GlideRoundTransform(mContext, 10);
        LogUtils.e("appPic " + bean.getAppPic());

        Glide.with(mContext).load(bean.getAppPic())
                .error(R.drawable.bg_activity_fail)
                .transform(glideRoundTransform)
                .into(holder.ivPicture);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity.PageBean.RowsBean bean = list.get(position);

                String appUrl = bean.getAppUrl();
                String value;
                if (appUrl.contains("?")) {
                    if (appUrl.substring(appUrl.indexOf("?") + 1).length() > 0) {
                        value = appUrl + "&app=true";
                    } else {
                        value = appUrl + "app=true";
                    }

                } else {
                    value = appUrl + "?app=true";
                }


                if (bean.getStatus() == 1) {

                    if (bean.getAppUrl().contains("jumpTo=3")) { //邀请好友三重礼

                        // LocalApplication.getInstance().getMainActivity().setCheckedFram(3);//返利
                        if (preferences.getString("uid", "").equalsIgnoreCase("")) {
                            //MobclickAgent.onEvent(mContext, UrlConfig.point + 36 + "");
                            mContext.startActivity(new Intent(mContext, LoginActivity.class));
                        } else {
                            mContext.startActivity(new Intent(mContext, InviteFriendsActivity.class));
                        }

                    } else if (bean.getTitle().indexOf("签到领积分") != -1) {
                        if (TextUtils.isEmpty(preferences.getString("uid", ""))) {
                            mContext.startActivity(new Intent(mContext, LoginActivity.class));
                        } else {
                            LogUtils.e("https://m.aikazj.com/register?uid="
                                    + preferences.getString("uid", "") + "&app=true&token="
                                    + preferences.getString("token", ""));
                            mContext.startActivity(new Intent(mContext, WebViewActivity.class)
                                    .putExtra("URL", "https://m.aikazj.com/register?uid="
                                            + preferences.getString("uid", "") + "&app=true&token="
                                            + preferences.getString("token", ""))
                                    .putExtra("TITLE", "签到领积分"));
                        }
                    } else {
                        mContext.startActivity(new Intent(mContext, WebViewActivity.class)
                                .putExtra("URL", value)
                                .putExtra("TITLE", bean.getTitle())
                                .putExtra("PID", bean.getId())
                                .putExtra("BANNER", "banner")
                        );
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static
    class ActivityViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_picture)
        ImageView ivPicture;
        @BindView(R.id.tv_state)
        TextView tvState;
        @BindView(R.id.view_fail)
        View viewFail;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_content)
        TextView tvContent;

        ActivityViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
