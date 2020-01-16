package com.mcz.xhj.yz.dr_adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_bean.FriendBean;
import com.mcz.xhj.yz.dr_util.LogUtils;
import com.mcz.xhj.yz.dr_view.coverflow.PageItemClickListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.List;

/**
 * Created by Administrator on 2018/4/17.
 */

public class CoverFlowAdapter extends PagerAdapter {
    private Context context;

    private List<FriendBean> lsct;

    public CoverFlowAdapter(Context context, List<FriendBean> lsct) {
        this.context = context;
        this.lsct = lsct;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_cover,null);
        final ImageView imageView = (ImageView) view.findViewById(R.id.image_cover);
        //FriendBean cb = lsct.get(position);

        int newposition =  position % lsct.size();

        if (newposition>=lsct.size()){
            newposition=lsct.size()-1;
        }
        FriendBean cb = lsct.get(newposition);

        Uri uri = Uri.parse(cb.getAppPic());
        //LogUtils.e("uri"+uri);
       // imageView.setImageURI(uri);
       // imageView.setImageDrawable(getResources().getDrawable(DemoData.covers[position]));




        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);


        DisplayImageOptions options = new DisplayImageOptions.Builder()
               .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
                .displayer(new RoundedBitmapDisplayer(8))//是否设置为圆角，弧度为多少
//      .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
                .build();//构建完成


        ImageLoader.getInstance().displayImage(cb.getAppPic(),imageView,options);

     /*   ImageLoader.getInstance().loadImage(cb.getAppPic(), new SimpleImageLoadingListener() {

            @Override
            public void onLoadingComplete(String imageUri, View view,
                                          Bitmap loadedImage) {
                // TODO Auto-generated method stub
                super.onLoadingComplete(imageUri, view, loadedImage);
                imageView.setImageBitmap(loadedImage);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view,
                                        FailReason failReason) {
                // TODO Auto-generated method stub
                super.onLoadingFailed(imageUri, view, failReason);
                //imageView.dismiss();
            }

        });*/

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(v,position);
            }
        });
        container.addView(view);
        return view;
    }





    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    @Override
    public int getCount() {
        return lsct.size();
        //return 99;
        //return Integer.MAX_VALUE;
        //当为最大值时,重新加载时会报anr异常
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }



    private ItemClickListener itemClickListener;

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
