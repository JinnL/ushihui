package com.ekabao.oil.ui.activity.find;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.ekabao.oil.R;
import com.ekabao.oil.bean.AddressBean;
import com.ekabao.oil.bean.AtyCarCityInfo;
import com.ekabao.oil.global.LocalApplication;
import com.ekabao.oil.http.UrlConfig;
import com.ekabao.oil.http.okhttp.OkHttpUtils;
import com.ekabao.oil.http.okhttp.callback.StringCallback;
import com.ekabao.oil.ui.activity.BaseActivity;
import com.ekabao.oil.util.GsonUtil;
import com.ekabao.oil.util.LogUtils;
import com.ekabao.oil.util.ToastUtil;
import com.ekabao.oil.util.show_Dialog_IsLogin;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 油实惠  App
 * 添加收货地址
 *
 * @time 2018/8/1 17:19
 * Created by lj on 2018/8/1 17:19.
 */

public class AtyAddCar extends BaseActivity {


    @BindView(R.id.title_leftimageview)
    ImageView titleLeftimageview;
    @BindView(R.id.title_centertextview)
    TextView titleCentertextview;
    @BindView(R.id.view_line_bottom)
    View viewLineBottom;
    @BindView(R.id.rb_small)
    RadioButton rbSmall;
    @BindView(R.id.rb_big)
    RadioButton rbBig;
    @BindView(R.id.rg_break)
    RadioGroup rgBreak;
    @BindView(R.id.tv_car_name)
    TextView tvCarName;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_car_find)
    EditText etCarFind;
    @BindView(R.id.iv_car_help)
    ImageView ivCarHelp;
    @BindView(R.id.et_car_power)
    EditText etCarPower;
    @BindView(R.id.iv_car_help_power)
    ImageView ivCarHelpPower;
    @BindView(R.id.tv_save_car)
    TextView tvSaveCar;
    private SharedPreferences preferences = LocalApplication.getInstance().sharereferences;
    private AddressBean address;
    private boolean edit;

    private int hpzl = 2;

    String[] stringArea = {"京", "津", "冀", "晋", "蒙", "辽", "吉", "黑", "沪", "苏", "浙", "皖", "闽",
            "赣", "鲁", "豫", "鄂", "湘", "粤", "桂", "琼", "渝", "川", "黔", "滇", "藏", "陕",
            "甘", "青", "宁", "新", "台", "港", "澳",};
    private String substringCar;
    private String carCity;
    private String carNum;
    private int carId;
    private String carHphm;

    @Override
    protected int getLayoutId() {
        return R.layout.aty_add_car;
    }

    @Override
    protected void initParams() {
        viewLineBottom.setVisibility(View.GONE);
        edit = getIntent().getBooleanExtra("edit", false);

        if (edit) {
            titleCentertextview.setText("修改车辆信息");
        } else {
            titleCentertextview.setText("添加车辆");
        }

        carHphm = getIntent().getStringExtra("carHphm");
        String carClassno = getIntent().getStringExtra("carClassno");
        String carEngineno = getIntent().getStringExtra("carEngineno");
        int carHpzl = getIntent().getIntExtra("carHpzl", 0);
        carId = getIntent().getIntExtra("carId", 0);
        if (!TextUtils.isEmpty(carHphm)) {
            etName.setText(carHphm.substring(1, carHphm.length()));
            tvCarName.setText(carHphm.substring(0, 1));
        }
        if (!TextUtils.isEmpty(carClassno)) {
            etCarFind.setText(carClassno);
        }
        if (!TextUtils.isEmpty(carEngineno)) {
            etCarPower.setText(carEngineno);
        }

        if (carHpzl == 1) {
            rbBig.setChecked(true);

        } else {
            rbSmall.setChecked(true);
        }


        rgBreak.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_small:
                        hpzl = 2;
                        break;
                    case R.id.rb_big:
                        hpzl = 1;
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @OnClick({R.id.title_leftimageview, R.id.tv_car_name, R.id.iv_car_help, R.id.iv_car_help_power, R.id.tv_save_car})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_leftimageview:
                finish();
                break;
            case R.id.tv_car_name:
                List<String> stringList = Arrays.asList(stringArea);

                showCarDialog(stringList);
                break;
            case R.id.iv_car_help:
                showCarExample();
                break;
            case R.id.iv_car_help_power:
                showCarExample();
                break;

            case R.id.tv_save_car:
                carNum = etName.getText().toString();

                if (!isCarnumberNO(carNum)) {
                    ToastUtil.showToast("请输入正确的车牌号");
                    return;
                }
                if (TextUtils.isEmpty(etCarFind.getText().toString())) {
                    ToastUtil.showToast("请输入车辆识别代号（车架号）");
                    return;
                }
                if (TextUtils.isEmpty(etCarPower.getText().toString())) {
                    ToastUtil.showToast("请输入发动机号");
                    return;
                }

                substringCar = carNum.substring(0, 1);
                if (edit) {
                    updateCar();//修改
                } else {
                    insertCar();//新增
                }
                break;

            default:
                break;
        }
    }

    public boolean isCarnumberNO(String carnumber) {
              /*
        车牌号格式：汉字 + A-Z + 5位A-Z或0-9
       （只包括了普通车牌号，教练车和部分部队车等车牌号不包括在内）         */
        //String carnumRegex = "[\u4e00-\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}";
        String carnumRegex = "[A-Z]{1}[A-Z_0-9]{5}";
        if (TextUtils.isEmpty(carnumber)) {
            return false;
        } else {
            return carnumber.matches(carnumRegex);
        }

    }

    private void showCarExample() {
        // 加载布局
        View layout = LayoutInflater.from(this).inflate(R.layout.pop_zaiza, null);
        // 找到布局的控件
        // 实例化popupWindow
        final PopupWindow popupWindow = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        ImageView ivCar = (ImageView) (layout).findViewById(R.id.imageView1);
        ImageView ivCarClose = (ImageView) (layout).findViewById(R.id.imageView3);
        ivCar.setImageResource(R.drawable.icon_car_example);
        ivCarClose.setImageResource(R.drawable.icon_car_close);

        // 控制键盘是否可以获得焦点
        //popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setFocusable(true);
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //popupWindow.dismiss();
                return false;
            }
        });
        // 设置popupWindow弹出窗体的背景
        setBackgroundAlpha(0.4f);//设置屏幕透明度

//		WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        // 监听
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // popupWindow隐藏时恢复屏幕正常透明度
                setBackgroundAlpha(1.0f);
            }
        });

        ivCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupWindow.dismiss();
            }
        });
        ivCarClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupWindow.dismiss();
            }
        });


        popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
    }

    private void showCarDialog(final List<String> stringList) {
        final Dialog dialog = new Dialog(this, R.style.DialogNoTitleStyleTranslucentBg);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_car_area, null);
        dialog.setCanceledOnTouchOutside(false);
//		dialog.setCancelable(isCanCancelabel);
        dialog.setContentView(contentView);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();

        TextView tvSure = (TextView) contentView.findViewById(R.id.tv_car_sure);

        RecyclerView rvCarArea = (RecyclerView) contentView.findViewById(R.id.rv_car_area);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 6);
        rvCarArea.setLayoutManager(gridLayoutManager);
        final carAdapter carAdapter = new carAdapter(this, 0, stringList);

        rvCarArea.setAdapter(carAdapter);

        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });

        carAdapter.setOnItemClickLitener(new carAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int positon) {
                carCity = stringList.get(positon);
                tvCarName.setText(stringList.get(positon));
                carAdapter.setPosition(positon);
                carAdapter.notifyDataSetChanged();
            }
        });
    }

    /*编辑*/
    private void updateCar() {
        showWaitDialog("加载中...", true, "");
        carCity = carHphm.substring(0, 1);
        substringCar = carHphm.substring(1, 2);

        LogUtils.e("hphm" + carCity + "+" + substringCar);

        OkHttpUtils.post().url(UrlConfig.cityTypeByCar)
                .addParams("uid", preferences.getString("uid", ""))
                .addParam("hphm", carCity + substringCar)
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {
            @Override
            public void onResponse(String response) {
                dismissDialog();
                LogUtils.i("查询城市参==" + response);

                JSONObject obj = JSON.parseObject(response);
                if (obj.getBoolean("success")) {

                    AtyCarCityInfo atyCarCityInfo = GsonUtil.parseJsonToBean(response, AtyCarCityInfo.class);
                    String cityCode = atyCarCityInfo.getMap().getDrJuheCarpre().getCityCode();
                    if (!TextUtils.isEmpty(cityCode)) {

                        OkHttpUtils.post()
                                .url(UrlConfig.carEdit)
                                .addParams("id", carId + "")
                                .addParams("uid", preferences.getString("uid", ""))
                                .addParams("city", cityCode)
                                .addParams("hphm", carCity + carNum)
                                .addParams("hpzl", hpzl + "")
                                .addParams("engineno", etCarPower.getText().toString())
                                .addParams("classno", etCarFind.getText().toString())
                                .addParams("version", UrlConfig.version)
                                .addParams("channel", "2")
                                .build().execute(new StringCallback() {
                            @Override
                            public void onResponse(String response) {
                                dismissDialog();
                                JSONObject obj = JSON.parseObject(response);
                                if (obj.getBoolean("success")) {
                                    ToastUtil.showToast("修改车辆成功");
                                    finish();
                                } else if ("9998".equals(obj.getString("errorCode"))) {
                                    new show_Dialog_IsLogin(AtyAddCar.this).show_Is_Login();
                                    finish();
                                } else if ("9999".equals(obj.getString("errorCode"))) {
                                    ToastUtil.showToast("系统异常");
                                }
                            }

                            @Override
                            public void onError(Call call, Exception e) {
                                dismissDialog();
                                ToastUtil.showToast("请检查网络");
                            }
                        });

                    }
                } else {
                    ToastUtil.showToast("系统异常");
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                dismissDialog();
                ToastUtil.showToast("请检查网络");
            }
        });


    }

    private void insertCar() {
        showWaitDialog("加载中...", true, "");

        LogUtils.e("hphm" + carCity + "+" + substringCar);

        OkHttpUtils.post().url(UrlConfig.cityTypeByCar)
                .addParams("uid", preferences.getString("uid", ""))
                .addParam("hphm", carCity + substringCar)
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2")
                .build().execute(new StringCallback() {
            @Override
            public void onResponse(String response) {
                dismissDialog();
                LogUtils.i("查询城市参==" + response);

                JSONObject obj = JSON.parseObject(response);
                if (obj.getBoolean("success")) {


                    AtyCarCityInfo atyCarCityInfo = GsonUtil.parseJsonToBean(response, AtyCarCityInfo.class);
                    String cityCode = atyCarCityInfo.getMap().getDrJuheCarpre().getCityCode();

                    if (!TextUtils.isEmpty(cityCode)) {

                        OkHttpUtils.post()
                                .url(UrlConfig.addCar)
                                .addParams("uid", preferences.getString("uid", ""))
                                .addParams("city", cityCode)
                                .addParams("hphm", carCity + carNum)
                                .addParams("hpzl", hpzl + "")
                                .addParams("engineno", etCarPower.getText().toString())
                                .addParams("classno", etCarFind.getText().toString())
                                .addParams("version", UrlConfig.version)
                                .addParams("channel", "2")
                                .build().execute(new StringCallback() {
                            @Override
                            public void onResponse(String response) {
                                dismissDialog();
                                LogUtils.i("新增车辆：==" + response);
                                JSONObject obj = JSON.parseObject(response);
                                if (obj.getBoolean("success")) {
                                    ToastUtil.showToast("添加车辆成功");
                                    finish();
                                } else if ("1001".equals(obj.getString("errorCode"))) {
                                    ToastUtil.showToast("其他用户已绑定");
                                } else if ("9998".equals(obj.getString("errorCode"))) {
                                    new show_Dialog_IsLogin(AtyAddCar.this).show_Is_Login();
                                    finish();
                                } else if ("9999".equals(obj.getString("errorCode"))) {
                                    ToastUtil.showToast("系统异常");
                                }
                            }

                            @Override
                            public void onError(Call call, Exception e) {
                                dismissDialog();
                                ToastUtil.showToast("请检查网络");
                            }
                        });

                    }
                } else if ("9998".equals(obj.getString("errorCode"))) {
                    new show_Dialog_IsLogin(AtyAddCar.this).show_Is_Login();
                    finish();
                } else if ("9999".equals(obj.getString("errorCode"))) {
                    ToastUtil.showToast("系统异常");
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                dismissDialog();
                ToastUtil.showToast("请检查网络");
            }
        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    static class carAdapter extends RecyclerView.Adapter {

        private List<String> carList;
        private Context context;
        int Position;

        public carAdapter(Context context, int position, List<String> list) {
            this.context = context;
            this.Position = position;
            this.carList = list;
        }

        public void setPosition(int position) {
            this.Position = position;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car_area, parent, false);
            ViewHolder myHolder = new ViewHolder(view);

            return myHolder;

        }


        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            final ViewHolder myHolder = (ViewHolder) holder;
            myHolder.tvAreaName.setText(carList.get(position));
            if (mOnItemClickLitener != null) {
                myHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickLitener.onItemClick(myHolder.itemView, position);
                    }
                });
            }
            if (Position == position) {
                myHolder.llCar.setBackgroundResource(R.drawable.shape_checked_gray);
                myHolder.tvAreaName.setTextColor(context.getResources().getColor(R.color.white));
            } else {
                myHolder.llCar.setBackgroundResource(R.drawable.shape_bg_gray);
            }

        }

        @Override
        public int getItemCount() {
            return carList.size();
        }

        public interface OnItemClickLitener {
            void onItemClick(View view, int positon);
        }

        OnItemClickLitener mOnItemClickLitener;

        public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
            this.mOnItemClickLitener = mOnItemClickLitener;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.tv_area_name)
            TextView tvAreaName;
            @BindView(R.id.ll_car)
            LinearLayout llCar;

            ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
