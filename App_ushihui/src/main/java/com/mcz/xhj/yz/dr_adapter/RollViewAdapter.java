package com.mcz.xhj.yz.dr_adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.mcz.xhj.yz.dr_bean.BannerBean;

import java.util.List;

/**
 * Created by DELL on 2017/11/22.
 */

public class RollViewAdapter extends StaticPagerAdapter {
    private Context context;
    private List<BannerBean> list;
    //private String[] s = {"http://192.168.1.250:8088/upload/productPic/2017-11/8/20171118593c4c07-5847-4958-a0eb-b2b05c5f1c61.jpg","http://192.168.1.250:8088/upload/productPic/2017-11/6/20171118e159c7d9-0657-4835-807e-a7270cf42073.jpg","http://192.168.1.250:8088/upload/productPic/2017-11/7/201711189e1111da-0dea-4149-8e0a-51fd8850409c.jpg"};

    public RollViewAdapter(Context context, List<BannerBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(ViewGroup viewGroup, int i) {
        ImageView view = new ImageView(viewGroup.getContext());
        ImageLoader.getInstance().displayImage(list.get(i).getImgUrl(),view);
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return view;
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
