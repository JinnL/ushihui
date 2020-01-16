package com.mcz.xhj.yz.dr_adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.mcz.xhj.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.mcz.xhj.yz.dr_bean.Offline_Bean;

import java.util.List;

/**
 * Created by DELL on 2017/5/3.
 */

public class OffLineAdapter extends RecyclerView.Adapter<OffLineAdapter.MyViewHolder> {
    private List<Offline_Bean> lsob;
//    private List<FriendBean> lsob;
    private Context context;
    private LayoutInflater inflater;

    private OnItemClickListener mOnItemClickLitener;
    public interface OnItemClickListener{
        void onClick( int position);
        void onLongClick( int position);
    }
    public void setOnItemClickListener(OnItemClickListener mOnItemClickLitener ){
        this.mOnItemClickLitener=mOnItemClickLitener;
    }
    public OffLineAdapter(List<Offline_Bean> lsob, Context context) {
        this.lsob = lsob;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_offline,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tv_title.setText(lsob.get(position).getTitleList());
        if(lsob.get(position).getH5ListBanner()!=null){
            Uri uri = Uri.parse(lsob.get(position).getH5ListBanner());
            holder.iv_offline.setImageURI(uri);
        }

        if(mOnItemClickLitener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onClick(position);
                }
            });
            holder. itemView.setOnLongClickListener( new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickLitener.onLongClick(position);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return lsob.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_title;
        SimpleDraweeView iv_offline;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            iv_offline = (SimpleDraweeView) itemView.findViewById(R.id.iv_offline);
        }
    }

}
