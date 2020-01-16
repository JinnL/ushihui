package com.mcz.xhj.yz.dr_adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_bean.ScoreboardBean;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/10/23
 * 描述：积分排行榜adapter
 */

public class ScoreboardAdapter extends BaseAdapter {
    private Context mContext;
    private List<ScoreboardBean> scoreList;

    public ScoreboardAdapter(Context mContext, List<ScoreboardBean> scoreList) {
        this.mContext = mContext;
        this.scoreList = scoreList;
    }

    //定义2种item的布局
    public static final int ITEM_TYPE_1 = 0;
    public static final int ITEM_TYPE_2 = 1;
    @Override
    public int getCount() {
        return scoreList.size();
    }

    @Override
    public Object getItem(int position) {
        return scoreList.get(position);
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
        int pos = position / 3;
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
                   // convertView = View.inflate(mContext, R.layout.item_scoreboard1, null);
                    convertView = View.inflate(mContext, R.layout.item_scoreboard2, null);

                   // holder.ll_item1 = (LinearLayout) convertView.findViewById(R.id.ll_item1);
                   // holder.ll_award = (LinearLayout) convertView.findViewById(R.id.ll_award);
                    holder.tv_rank_number = (TextView) convertView.findViewById(R.id.tv_rank_number);
                    holder.img_avatar = (CircleImageView) convertView.findViewById(R.id.img_avatar);
                    holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                    holder.tv_jifen = (TextView) convertView.findViewById(R.id.tv_jifen);
                    holder.tv_award = (TextView) convertView.findViewById(R.id.tv_award);
                    holder.tv_value = (TextView) convertView.findViewById(R.id.tv_value);
                    break;
                case ITEM_TYPE_2:
                    convertView = View.inflate(mContext, R.layout.item_scoreboard2, null);
                    holder.tv_rank_number = (TextView) convertView.findViewById(R.id.tv_rank_number);
                    holder.img_avatar = (CircleImageView) convertView.findViewById(R.id.img_avatar);
                    holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                    holder.tv_jifen = (TextView) convertView.findViewById(R.id.tv_jifen);
                    holder.tv_award = (TextView) convertView.findViewById(R.id.tv_award);
                    holder.tv_value = (TextView) convertView.findViewById(R.id.tv_value);
                    break;
                default:
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ScoreboardBean scoreboard = scoreList.get(position);
        switch (viewType) {
            case ITEM_TYPE_1:
                int rank = position % 3;
                holder.tv_rank_number.setText(" ");
                if(rank == 0){
                   // holder.ll_item1.setBackgroundResource(R.drawable.bg_corner_yellow_light);
                    //holder.ll_award.setBackgroundResource(R.drawable.bg_corner_yellow1);
                    holder.tv_rank_number.setBackgroundResource(R.drawable.img_crown_first);
                } else if(rank == 1){
                   // holder.ll_item1.setBackgroundResource(R.drawable.bg_corner_red_light);
                   // holder.ll_award.setBackgroundResource(R.drawable.bg_corner_red1);
                    holder.tv_rank_number.setBackgroundResource(R.drawable.img_crown_second);
                }else if(rank == 2){
                   // holder.ll_item1.setBackgroundResource(R.drawable.bg_corner_blue_light);
                    //holder.ll_award.setBackgroundResource(R.drawable.bg_corner_blue);
                    holder.tv_rank_number.setBackgroundResource(R.drawable.img_crown_third);
                }

                //holder.tv_rank_number.setText("No."+(position+1));
                if(!TextUtils.isEmpty(scoreboard.getPhoto())){
                    Picasso
                            .with(mContext)
                            .load(scoreboard.getPhoto())
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .into(holder.img_avatar);
                }
                //holder.tv_name.setText(scoreboard.getRealname());
                holder.tv_name.setText(scoreboard.getMobilePhone());

                holder.tv_jifen.setText("积分："+scoreboard.getPointMonth());//当月积分
                if (!TextUtils.isEmpty(scoreboard.getGoodsName())){
                    holder.tv_award.setText(scoreboard.getGoodsName());
                }
                if(!TextUtils.isEmpty(scoreboard.getGoodsValue()+"")){
                    holder.tv_value.setText("价值: "+scoreboard.getGoodsValue().intValue()+"元");
                }
                break;

            case ITEM_TYPE_2:
                holder.tv_rank_number.setText(position+1+"");
                if(!TextUtils.isEmpty(scoreboard.getPhoto())){
                    Picasso.with(mContext).load(scoreboard.getPhoto()).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.img_avatar);
                }
                //holder.tv_name.setText(scoreboard.getRealname());
                holder.tv_name.setText(scoreboard.getMobilePhone());
                holder.tv_jifen.setText("积分："+scoreboard.getPointMonth());//当月积分
                if (!TextUtils.isEmpty(scoreboard.getGoodsName())){
                    holder.tv_award.setText(scoreboard.getGoodsName());
                }
                if(!TextUtils.isEmpty(scoreboard.getGoodsValue()+"")){
                    holder.tv_value.setText("价值: "+scoreboard.getGoodsValue().intValue()+"元");
                }
                break;
        }
        return convertView;
    }

    public class ViewHolder {
        private LinearLayout ll_item1;
        private LinearLayout ll_award;
        private CircleImageView img_avatar;
        private TextView tv_rank_number;
        private TextView tv_name;
        private TextView tv_jifen;
        private TextView tv_award;
        private TextView tv_value;
    }
}
