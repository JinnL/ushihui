package com.ekabao.oil.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ekabao.oil.R;

import java.util.List;

/**
 * ${APP_NAME}  App_oil
 *
 * @time 2018/11/2 16:12
 * Created by lj on 2018/11/2 16:12.
 */

public class MultiplePagerAdapter extends PagerAdapter {
    /*@Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return false;
    }*/

    private List<Integer> mList;
    private LayoutInflater layoutInflater;
    private Context context;
    public MultiplePagerAdapter(Context context, List<Integer> list) {
        super();
        this.mList = list;
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * 页面宽度所占ViewPager测量宽度的权重比例，默认为1
     */
    @Override
    public float getPageWidth(int position) {
        return (float) 0.90;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        ((ViewPager) container).removeView(view);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.item_cover,null);

        final ImageView imageView = (ImageView) view.findViewById(R.id.image_cover);
// ContextCompat.getDrawable(context,R.drawable.bg_person)
//
        Glide.with(context)
                .load(R.drawable.bg_person)
                .placeholder(R.drawable.bg_home_banner_fail)
                .error(R.drawable.bg_home_banner_fail)
                .centerCrop()
               //.transform(glideRoundTransform)
                .into(imageView);
        // 自己实现
        container.addView(view);
        return view;
    }

}
