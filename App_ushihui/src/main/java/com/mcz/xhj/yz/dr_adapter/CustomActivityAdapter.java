package com.mcz.xhj.yz.dr_adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_bean.FriendBean;
import com.mcz.xhj.yz.dr_view.CustomAdapter;

import java.util.List;

/**
 * Created by zhulang on 2017/8/25.
 * 描述：进行中的活动
 */

public class CustomActivityAdapter extends CustomAdapter{
    private Context context;
    private List<FriendBean> lsct;
    private LayoutInflater inflater;

    public CustomActivityAdapter(Context context, List<FriendBean> lsct) {
        this.context = context;
        this.lsct = lsct;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return lsct.size();
    }

    @Override
    public Object getItem(int position) {
        return lsct.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position) {
        View convertView = inflater.inflate(R.layout.item_activity_doing, null);
//        TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
//        TextView tv_time = (TextView) convertView.findViewById(R.id.tv_time);
//        TextView tv_hot = (TextView) convertView.findViewById(R.id.tv_hot);
//        LinearLayout ll_yinyin = (LinearLayout) convertView.findViewById(R.id.ll_yinyin);
        SimpleDraweeView iv_pic = (SimpleDraweeView) convertView.findViewById(R.id.iv_pic);

        FriendBean cb = lsct.get(position);
//        tv_name.setText(cb.getTitle());
//		vh.tv_time.setText("活动时间："+cb.getActivityDate());
//        tv_time.setText(cb.getActivityDate());
        cb.getAppPic();
        if(cb.getAppPic()!=null&&!cb.getAppPic().equalsIgnoreCase("")){
            Uri uri = Uri.parse(cb.getAppPic());
            iv_pic.setImageURI(uri);
        }

        /*if(cb.getIsTop()==1){
            tv_hot.setVisibility(View.VISIBLE);
        }else{
            tv_hot.setVisibility(View.GONE);
        }
        switch (cb.getStatus()) {
            case 1:
                ll_yinyin.setVisibility(View.GONE);
                break;
            case 2:
                ll_yinyin.setVisibility(View.VISIBLE);
                tv_hot.setVisibility(View.GONE);
                break;
            default:
                break;
        }*/
        return convertView;
    }
}
