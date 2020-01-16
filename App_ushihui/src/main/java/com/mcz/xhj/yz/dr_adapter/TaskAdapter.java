package com.mcz.xhj.yz.dr_adapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_bean.TaskBean;
import com.mcz.xhj.yz.dr_view.CustomShareBoard;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2017/10/23 0023.
 * 描述：任务中心adapter
 */

public class TaskAdapter extends BaseAdapter {
    private Activity activity;
    private List<TaskBean> list;
    private int type;
    private LayoutInflater inflater;
    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;

    public TaskAdapter(Activity activity, List<TaskBean> list,int type) {
        this.activity = activity;
        this.list = list;
        this.type = type;
        this.inflater = LayoutInflater.from(activity);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if(vh == null){
            vh = new ViewHolder();
            convertView=inflater.inflate(R.layout.item_task, null);
            vh.img_avatar = (ImageView) convertView.findViewById(R.id.img_avatar);
            vh.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            vh.ll_jindu = (LinearLayout) convertView.findViewById(R.id.ll_jindu);
            vh.tv_complete_jindu = (TextView) convertView.findViewById(R.id.tv_complete_jindu);
            vh.tv_complete_total = (TextView) convertView.findViewById(R.id.tv_complete_total);
            vh.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            vh.tv_use = (TextView) convertView.findViewById(R.id.tv_use);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder) convertView.getTag();
        }
        final TaskBean task = list.get(position);
        String content = "";
        BigDecimal redpacketMoney = task.getRedpacketMoney();//红包
        BigDecimal experienceMoney = task.getExperienceMoney();//体验金
        BigDecimal cashMoney = task.getCashMoney();//（送的）现金金额
        int invest_count_yes = task.getInvest_count_yes();//已邀请人数
        int investCount = task.getInvestCount();//邀请人数
        int invest_parent_count_month = task.getInvest_parent_count_month();//当月我的邀请人数

        if(task.getPicImg() != null){
            Picasso.with(activity).load(task.getPicImg()).into(vh.img_avatar);
        }
        if(task.getPic_img() != null){
            Picasso.with(activity).load(task.getPic_img()).into(vh.img_avatar);
        }
        vh.tv_title.setText(task.getName());
        if(redpacketMoney != null && experienceMoney != null){
            content = redpacketMoney + "新手红包+" + experienceMoney +"金服体验金";
            //修改文本中的部分文字颜色
            SpannableString spannableString = new SpannableString(content);
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF5111")),0,(redpacketMoney+"").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF5111")),(redpacketMoney+"").length()+5,(redpacketMoney+"").length()+5+(experienceMoney+"").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            vh.tv_content.setText(spannableString);

        }else if(redpacketMoney == null && experienceMoney != null){
            content = experienceMoney +"金服体验金";
            //修改文本中的部分文字颜色
            SpannableString spannableString = new SpannableString(content);
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF5111")),0,(experienceMoney+"").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            vh.tv_content.setText(spannableString);

        } else if(redpacketMoney != null && experienceMoney == null){
            content = redpacketMoney + "新手红包";
            //修改文本中的部分文字颜色
            SpannableString spannableString = new SpannableString(content);
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF5111")),0,(redpacketMoney+"").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            vh.tv_content.setText(spannableString);

        } else if(cashMoney != null){
            content = cashMoney +"元现金";
            //修改文本中的部分文字颜色
            SpannableString spannableString = new SpannableString(content);
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF5111")),0,(cashMoney+"").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            vh.tv_content.setText(spannableString);
        }

        if(task.getType() == 0){
            if(task.getSuccessFlag() == 1){
                vh.tv_use.setText("领取");
                vh.tv_use.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(receiveTaskClickListener != null){
                            receiveTaskClickListener.onReceiveTask(task.getId());
                        }
                    }
                });
            }else{
                vh.tv_use.setText("去认证");
                vh.tv_use.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(receiveTaskClickListener != null){
                            receiveTaskClickListener.onClickUrl(task.getLinkUrlApp());
                        }
                    }
                });
            }

        }else if(task.getType() == 2){
            if(task.getSuccessFlag() == 1){
                vh.tv_use.setText("领取");
                vh.tv_use.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(receiveTaskClickListener != null){
                            receiveTaskClickListener.onReceiveTask(task.getId());
                        }
                    }
                });
            }else{
                vh.tv_use.setText("去投资");
                vh.tv_use.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(receiveTaskClickListener != null){
                            receiveTaskClickListener.onClickUrl(task.getLinkUrlApp());
                        }
                    }
                });
            }

        }else if(task.getType() == 3){
            if(task.getSuccessFlag() == 1){
                vh.tv_use.setText("领取");
                vh.tv_use.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(receiveTaskClickListener != null){
                            receiveTaskClickListener.onReceiveTask(task.getId());
                        }

                    }
                });
            }else{
                vh.tv_use.setText("去投资");
                vh.tv_use.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(receiveTaskClickListener != null){
                            receiveTaskClickListener.onClickUrl(task.getLinkUrlApp());
                        }
                    }
                });
            }

        }else if(task.getType() == 4){
            if(task.getSuccessFlag() == 1){
                vh.tv_use.setText("领取");
                vh.tv_use.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(receiveTaskClickListener != null){
                            receiveTaskClickListener.onReceiveTask(task.getId());
                        }
                    }
                });
            }else{
                vh.tv_use.setText("去投资");
                vh.tv_use.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(receiveTaskClickListener != null){
                            receiveTaskClickListener.onClickUrl(task.getLinkUrlApp());
                        }
                    }
                });
            }

        }else if(task.getType() == 5){
            if(task.getSuccessFlag() == 1){
                vh.tv_use.setText("领取");
                vh.tv_use.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(receiveTaskClickListener != null){
                            receiveTaskClickListener.onReceiveTask(task.getId());
                        }
                    }
                });
            }else{
                vh.tv_use.setText("去投资");
                vh.tv_use.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(receiveTaskClickListener != null){
                            receiveTaskClickListener.onClickUrl(task.getLinkUrlApp());
                        }
                    }
                });
            }

        }else if(task.getType() == 6){
            if(task.getSuccessFlag() == 1){
                vh.tv_use.setText("领取");
                vh.tv_use.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(receiveTaskClickListener != null){
                            receiveTaskClickListener.onReceiveTask(task.getId());
                        }
                    }
                });
            }else{
                vh.tv_use.setText("去邀请");
                vh.tv_use.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(receiveTaskClickListener != null){
                            receiveTaskClickListener.onClickUrl(task.getLinkUrlApp());
                        }
                    }
                });
            }

        }else if(task.getType() == 7){
            if(task.getSuccessFlag() == 1){
                vh.tv_use.setText("领取");
                vh.tv_use.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(receiveTaskClickListener != null){
                            receiveTaskClickListener.onReceiveTask(task.getId());
                        }
                    }
                });
            }else{
                vh.tv_use.setText("去邀请");
                vh.tv_use.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(receiveTaskClickListener != null){
                            receiveTaskClickListener.onClickUrl(task.getLinkUrlApp());
                        }
                    }
                });
            }

        }else if(task.getType() == 8){
            vh.ll_jindu.setVisibility(View.VISIBLE);
            vh.tv_complete_jindu.setText(invest_count_yes+"");
            vh.tv_complete_total.setText(investCount+"");
            if(task.getSuccessFlag() == 1){
                vh.tv_use.setText("领取");
                vh.tv_use.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(receiveTaskClickListener != null){
                            receiveTaskClickListener.onReceiveTask(task.getId());
                        }
                    }
                });
            }else{
                vh.tv_use.setText("去邀请");
                vh.tv_use.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(receiveTaskClickListener != null){
                            receiveTaskClickListener.onClickUrl(task.getLinkUrlApp());
                        }
                    }
                });
            }

        }else if(task.getType() == 9){
            vh.ll_jindu.setVisibility(View.VISIBLE);
            vh.tv_complete_jindu.setText(invest_count_yes+"");
            vh.tv_complete_total.setText(investCount+"");
            if(task.getSuccessFlag() == 1){
                vh.tv_use.setText("领取");
                vh.tv_use.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(receiveTaskClickListener != null){
                            receiveTaskClickListener.onReceiveTask(task.getId());
                        }
                    }
                });
            }else{
                vh.tv_use.setText("去邀请");
                vh.tv_use.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(receiveTaskClickListener != null){
                            receiveTaskClickListener.onClickUrl(task.getLinkUrlApp());
                        }
                    }
                });
            }
        }

        if(type == 2){
            vh.tv_use.setBackgroundResource(R.drawable.shape_rectangle_gray_empty_15);
            vh.tv_use.setTextColor(0xFFfbfcfd);
            vh.tv_use.setText("已完成");
            vh.tv_use.setClickable(false);
            vh.tv_title.setTextColor(0xFFA2A5A7);
            SpannableString spannableString = new SpannableString(content);
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#DBDBDB")),0,content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            vh.tv_content.setText(spannableString);
        }

        return convertView;
    }

    private void postShare(String url) {
        CustomShareBoard shareBoard = new CustomShareBoard(activity, "", url, "zhengchang", "");
        shareBoard.showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    private class ViewHolder {
        private ImageView img_avatar;
        private TextView tv_title,tv_content,tv_use,tv_complete_jindu,tv_complete_total;
        private LinearLayout ll_jindu;
    }

    public interface OnReceiveTaskClickListener{
        public void onReceiveTask(int id);
        public void onClickUrl(String linkUrlApp);
    }

    private OnReceiveTaskClickListener receiveTaskClickListener;

    public void setOnReceiveTaskClickListener(OnReceiveTaskClickListener listener){
        this.receiveTaskClickListener = listener;
    }
}
