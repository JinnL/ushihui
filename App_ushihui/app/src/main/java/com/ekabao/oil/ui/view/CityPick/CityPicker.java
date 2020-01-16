package com.ekabao.oil.ui.view.CityPick;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.ekabao.oil.R;
import com.ekabao.oil.ui.view.WheelRecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;



/**
 * 城市选择器
 * Created by CrazyPumPkin on 2016/12/5.
 */

public class CityPicker implements PopupWindow.OnDismissListener, View.OnClickListener {

    private OnCitySelectListener mOnCitySelectListener;

    private PopupWindow mPickerWindow;

    private View mParent;

    private WheelRecyclerView mProvinceWheel;

    private WheelRecyclerView mCityWheel;

    private WheelRecyclerView mCountyWheel;

    private Activity mContext;

    // private List<Province> mDatas;
    private List<address> mDatas; //省
    private List<address> mlist2; //市
    private List<address> mlist3; //县
    private List<String> cities;//存的市的名字

    public CityPicker(Activity context, View parent) {
        mContext = context;
        mParent = parent;
        //初始化选择器
        View pickerView = LayoutInflater.from(context).inflate(R.layout.city_picker, null);
        mProvinceWheel = (WheelRecyclerView) pickerView.findViewById(R.id.wheel_province);
        mCityWheel = (WheelRecyclerView) pickerView.findViewById(R.id.wheel_city);
        mCountyWheel = (WheelRecyclerView) pickerView.findViewById(R.id.wheel_county);
        pickerView.findViewById(R.id.tv_exit).setOnClickListener(this);
        pickerView.findViewById(R.id.tv_ok).setOnClickListener(this);

        mPickerWindow = new PopupWindow(pickerView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPickerWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        mPickerWindow.setFocusable(true);
        mPickerWindow.setAnimationStyle(R.style.CityPickerAnim);
        mPickerWindow.setOnDismissListener(this);

        initData();
    }

    /**
     * 初始化城市数据
     */
    private void initData() {
      /*  mDatas = new Gson().fromJson(getTextFromAssets(mContext, "city2.json"), new TypeToken<List<Province>>() {
        }.getType());*/

        mDatas = new Gson().fromJson(getTextFromAssets(mContext, "province.json"), new TypeToken<List<address>>() {
        }.getType());
        mlist2 = new Gson().fromJson(getTextFromAssets(mContext, "city.json"), new TypeToken<List<address>>() {
        }.getType());
        mlist3 = new Gson().fromJson(getTextFromAssets(mContext, "area.json"), new TypeToken<List<address>>() {
        }.getType());

        mProvinceWheel.setData(getProvinceNames());
        mCityWheel.setData(getCityNames(110000));
        mCountyWheel.setData(getCountyNames(110100));

        mProvinceWheel.setOnSelectListener(new WheelRecyclerView.OnSelectListener() {
            @Override
            public void onSelect(int position, String data) {
                onProvinceWheelRoll(mDatas.get(position).id);
            }
        });
        mCityWheel.setOnSelectListener(new WheelRecyclerView.OnSelectListener() {
            @Override
            public void onSelect(int position, String data) {
                String name = cities.get(position);
                int cityId = getCityId(name);
                onCityWheelRoll(cityId);
            }
        });
    }

    private void onProvinceWheelRoll(int position) {
        mCityWheel.setData(getCityNames(position));
        mCountyWheel.setData(getCountyNames(getCityId(position)));
    }

    private void onCityWheelRoll(int position) {
        mCountyWheel.setData(getCountyNames(position));
    }

    public int getCityId(int position) {
        for (address city : mlist2) {
            if (position == city.parentid) {
                return city.id;
            }
        }
        return 0;
    }

    public int getCityId(String name) {
        for (address city : mlist2) {
            if (name == city.areaname) {
                return city.id;
            }
        }
        return 0;
    }

    /**
     * 从assets下读取文本
     *
     * @param context
     * @param fileName
     * @return
     */
    public String getTextFromAssets(Context context, String fileName) {
        String result = "";
        //LogUtils.e("从assets下"+result);
        try {
            InputStream is = context.getAssets().open(fileName);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            result = new String(buffer, "utf-8");
            is.close();
        } catch (IOException e) {
            //LogUtils.e("从assets下读取文本"+e.toString());
            e.printStackTrace();
        }
       // LogUtils.e("从assets下"+result);
        return result;
    }

    /**
     * 获取省份名称列表
     *
     * @return
     */
    private List<String> getProvinceNames() {
      /*  List<String> provinces = new ArrayList<>();
        for (Province province : mDatas) {
            provinces.add(province.getAreaName());
        }
        return provinces;*/

        List<String> provinces = new ArrayList<>();
        for (address province : mDatas) {
            provinces.add(province.areaname);
        }
        return provinces;
    }

    /**
     * 获取某个省份的城市名称列表
     *
     * @param provincePos
     * @return
     */
    private List<String> getCityNames(int provincePos) {
       /* List<String> cities = new ArrayList<>();
        for (City city : mDatas.get(provincePos).getCities()) {
            cities.add(city.getAreaName());
        }
        return cities;*/
        cities = new ArrayList<>();
        for (address city : mlist2) {
            if (provincePos == city.parentid) {
                cities.add(city.areaname);
            }
        }
        return cities;
    }

    /**
     * 获取某个城市的县级区域名称列表
     *
     * @param provincePos
     * @return
     */
  /*  private List<String> getCountyNames(int provincePos, int cityPos) {
        List<String> counties = new ArrayList<>();
        if (mDatas.get(provincePos).getCities().size() > 0) {
            for (County county : mDatas.get(provincePos).getCities().get(cityPos).getCounties()) {
                counties.add(county.getAreaName());
            }
        }
        return counties;
    }*/
    private List<String> getCountyNames(int provincePos) {
        // address city = mlist2.get(provincePos);
        List<String> counties = new ArrayList<>();

        for (address county : mlist3) {
            if (county.parentid == provincePos) {
                counties.add(county.areaname);
            }
        }
        return counties;
    }

    /**
     * 弹出Window时使背景变暗
     *
     * @param alpha
     */
    private void backgroundAlpha(float alpha) {
        Window window = mContext.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = alpha;
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setAttributes(lp);
    }

    public CityPicker setOnCitySelectListener(OnCitySelectListener listener) {
        mOnCitySelectListener = listener;
        return this;
    }

    public void show() {
        backgroundAlpha(0.5f);
        mPickerWindow.showAtLocation(mParent, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void onDismiss() {
        backgroundAlpha(1f);
    }

    /**
     * 设置城市是否显示
     * */
    public void setCityVisibility(boolean isShow) {
        if (isShow){
            mCityWheel.setVisibility(View.VISIBLE);
        }else {
            mCityWheel.setVisibility(View.GONE);
        }
    }
     /**
     * 设置城市-->区 是否显示
     * */
    public void setCountyVisibility(boolean isShow) {
        if (isShow){
            mCountyWheel.setVisibility(View.VISIBLE);
        }else {
            mCountyWheel.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_ok:
                if (mOnCitySelectListener != null) {
                   /*// Province province = mDatas.get(mProvinceWheel.getSelected());
                    address province = mDatas.get(mProvinceWheel.getSelected());
                    City city = province.getCities().size() > 0 ? province.getCities().get(mCityWheel.getSelected()) : null;
                    String provinceName = province.getAreaName();
                    String cityName = city == null ? "" : city.getAreaName();
                    String countyName = city == null ? "" : city.getCounties().get(mCountyWheel.getSelected()).getAreaName();
                    mOnCitySelectListener.onCitySelect(provinceName, cityName, countyName);
                    mPickerWindow.dismiss();*/
                    address province = mDatas.get(mProvinceWheel.getSelected());
                    String provinceName = province.areaname;


                    String cityName = getCityNames(province.id).size() > 0 ? getCityNames(province.id).get(mCityWheel.getSelected()) : null;

                    int cityid = 0; //市id
                    int countyid = 0;//县id

                    String countyName = "";
                    // TODO: 2017/2/24 索引越界了 
                    for (address city : mlist2) {
                        if (cityName == city.areaname) {
                            if (getCountyNames(city.id).size() == 0) {
                                cityid = city.id;
                                countyid = 0;
                            } else {
                                countyName = cityName == null ? "" : getCountyNames(city.id).get(mCountyWheel.getSelected());
                                cityid = city.id;
                                for (address county : mlist3) {
                                    if (county.parentid == cityid) {
                                        if (county.areaname == countyName) {
                                            countyid = county.id;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    mOnCitySelectListener.onCitySelect(provinceName, province.id, cityName, cityid, countyName, countyid);

                    mPickerWindow.dismiss();


                }
                break;
            case R.id.tv_exit:
                mProvinceWheel.setData(getProvinceNames());
                mCityWheel.setData(getCityNames(110000));
                mCountyWheel.setData(getCountyNames(110100));
                mPickerWindow.dismiss();
                break;
        }
    }

    public interface OnCitySelectListener {
        void onCitySelect(String province, int pid, String city, int cityid, String county, int cid);
    }

}
