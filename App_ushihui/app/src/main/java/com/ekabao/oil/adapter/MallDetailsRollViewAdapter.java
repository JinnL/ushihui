package com.ekabao.oil.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.ekabao.oil.R;
import com.ekabao.oil.bean.GoodsDetails;
import com.ekabao.oil.util.LogUtils;

import java.util.List;

/**
 * Created by DELL on 2017/11/22.
 */

public class MallDetailsRollViewAdapter extends StaticPagerAdapter {
    private Context context;
    private List<GoodsDetails.GalleryBean> list;

    //private String[] s = {"http://192.168.1.250:8088/upload/productPic/2017-11/8/20171118593c4c07-5847-4958-a0eb-b2b05c5f1c61.jpg","http://192.168.1.250:8088/upload/productPic/2017-11/6/20171118e159c7d9-0657-4835-807e-a7270cf42073.jpg","http://192.168.1.250:8088/upload/productPic/2017-11/7/201711189e1111da-0dea-4149-8e0a-51fd8850409c.jpg"};

    public MallDetailsRollViewAdapter(Context context, List<GoodsDetails.GalleryBean> list) {
        this.context = context;
        this.list = list;

       // LogUtils.e("轮播图RollViewAdapter");

    }

    @Override
    public View getView(ViewGroup viewGroup, int i) {

       // View itemview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_home_rollview, viewGroup, false);

       // ImageView view = (ImageView) itemview.findViewById(R.id.iv_image);

        ImageView view = new ImageView(viewGroup.getContext());
        //ImageLoader.getInstance().displayImage(list.get(i).getImgUrl(),view);


       // GlideRoundTransform glideRoundTransform = new GlideRoundTransform(context,10);
       // Glide.with(this).load(URL).centerCrop().transform(new GlideCircleTransform(this)).into(imv1);
        //只是绘制左上角和右上角圆角
        //glideRoundTransform.setExceptCorner(false, false, true, true);
      //  GlideRoundTransform transformation = new GlideRoundTransform(context,15);
        //只是绘制左上角和右上角圆角
        //transformation.setExceptCorner(false, false, true, true);
        view.setScaleType(ImageView.ScaleType.FIT_XY);
        //view.setPadding(29,0,29,0);
        //view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


        LogUtils.e("轮播图"+list.get(i).getImgUrl());

        Glide.with(context)
                .load(list.get(i).getImgUrl())

           //     .placeholder(R.drawable.bg_home_banner_fail)
                .error(R.drawable.bg_home_banner_fail)
                .centerCrop()
                //.transform(glideRoundTransform)
                .into(view);
       return view;

    }

    @Override
    public int getCount() {
        return list.size();
    }
}
