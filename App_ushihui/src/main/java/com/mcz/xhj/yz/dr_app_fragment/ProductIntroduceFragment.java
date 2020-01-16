package com.mcz.xhj.yz.dr_app_fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.interfaces.DraweeHierarchy;
import com.facebook.drawee.view.DraweeView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_bean.ProIntroduceBean;
import com.mcz.xhj.yz.dr_bean.bean_Detail_Info_pic;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.LogUtils;
import com.mcz.xhj.yz.dr_view.GridViewInScrollView;
import com.mcz.xhj.yz.dr_view.ListInScroll;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bleu.widget.slidedetails.SlideDetailsLayout;
import okhttp3.Call;

/**
 * Created by zhulang on 2017/8/8.
 * 描述：2.0版 标详情页-产品介绍
 */

public class ProductIntroduceFragment extends BaseFragment {
    @BindView(R.id.sv_pic)
    ScrollView sv_pic;
    @BindView(R.id.iv_yuanli)
    SimpleDraweeView iv_yuanli;
    @BindView(R.id.gv_frag_company_picture)
    GridViewInScrollView gvFragCompanyPicture;
    @BindView(R.id.gv_frag_pro_picture)
    GridViewInScrollView gvFragProPicture;
    @BindView(R.id.lv_product_introduce)
    ListInScroll lv_product_introduce;
    @BindView(R.id.tv_borrower)
    TextView tv_borrower;
    @BindView(R.id.tv_accept)
    TextView tv_accept;
    @BindView(R.id.tv_repaySource)
    TextView tv_repaySource;

    private SharedPreferences preferences;
    private String pid, ptype, principleH5;
    private List<bean_Detail_Info_pic> lsad = new ArrayList<>();
    private List<String> projectBeanList = new ArrayList<>();
    private List<ProIntroduceBean> proIntroduceList;
    private String borrower;
    private String introduce;
    private String repaySource;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_product_introduce;
    }

    private SlideDetailsLayout mSlideDetailsLayout;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mSlideDetailsLayout = (SlideDetailsLayout) activity.findViewById(R.id.slidedetails);
    }

    @Override
    protected void initParams() {
        preferences = LocalApplication.getInstance().sharereferences;
        pid = getArguments().getString("pid");
        ptype = getArguments().getString("ptype");
        introduce = getArguments().getString("introduce");
        principleH5 =  getArguments().getString("principleH5");
        borrower = getArguments().getString("borrower");
        repaySource = getArguments().getString("repaySource");
        tv_borrower.setText(borrower);
        tv_accept.setText(introduce);
        tv_repaySource.setText(repaySource);
        LogUtils.i("--->产品原理图principleH5: "+principleH5+" ,pid："+pid+" ,ptype："+ptype);
        proIntroduceList = (List<ProIntroduceBean>) getArguments().getSerializable("proIntroduceList");
      //  LogUtils.i("----->proIntroduceList：" + proIntroduceList.toString());

        if(proIntroduceList.size() > 0){
            lv_product_introduce.setAdapter(new ProductAdapter(proIntroduceList));
        }

        if(principleH5!=null){
            Uri uri = Uri.parse(principleH5);
            iv_yuanli.setImageURI(uri);
        } else {
            iv_yuanli.setBackgroundResource(R.mipmap.default_yuanli);
        }

        getMoreDetail();

        gvFragProPicture.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub

                showPopupWindow1(lsad.get(position).getBigUrl());
            }
        });

        sv_pic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (sv_pic.getScrollY() == 0) {
//					mSlideDetailsLayout.isMove = false;
                    mSlideDetailsLayout.smoothOpen(false);
//					mSlideDetailsLayout.setIsMove(false);
                } else {
//					mSlideDetailsLayout.isMove = true;
                    mSlideDetailsLayout.smoothOpen(true);
//					mSlideDetailsLayout.setIsMove(true);
                }

                return false;
            }
        });
    }

    private void getMoreDetail() {
        showWaitDialog("加载中...", true, "");
        OkHttpUtils.post()
                .url(UrlConfig.DETAIL_INFO)
                .addParams("uid", preferences.getString("uid", ""))
                .addParams("pid", pid)
                .addParams("type", 2+"")
                .addParams("version", UrlConfig.version)
                .addParams("channel", "2").build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String response) {
                        // TODO Auto-generated method stub
                        dismissDialog();
                        LogUtils.i("--->产品介绍 " + response);
                        JSONObject obj = JSON.parseObject(response);
                        if (obj.getBoolean(("success"))) {
                            JSONObject map = obj.getJSONObject("map");
                            JSONArray projectList = map.getJSONArray("projectList");
                            JSONArray picList = map.getJSONArray("picList");
                            projectBeanList = JSON.parseArray(projectList.toJSONString(), String.class);
                            lsad = JSON.parseArray(picList.toJSONString(), bean_Detail_Info_pic.class);
                            if (projectBeanList.size() > 0) {
                                LogUtils.e("projectBeanList.size"+projectBeanList.size());
                                gvFragCompanyPicture.setAdapter(new MyAdapter(projectBeanList));
                            }
                            if (lsad.size() > 0) {
                                LogUtils.e("lsad.size"+lsad.size());
                                gvFragProPicture.setAdapter(new lv_adapter(lsad));
                            }
                        } else if ("9999".equals(obj.getString("errorCode"))) {
                            ToastMaker.showShortToast("系统错误");
                        } else {
                            ToastMaker.showShortToast("系统错误");
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        // TODO Auto-generated method stub
                        dismissDialog();
                        ToastMaker.showShortToast("网络错误，请检查");
                    }
                });
    }


    private class MyAdapter extends BaseAdapter {
        private List<String> list;

        public MyAdapter(List<String> list) {
            this.list = list;
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
            convertView = View.inflate(mContext, R.layout.item_company, null);
            TextView tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            tv_title.setText(list.get(position));

            return convertView;
        }
    }

    private class ProductAdapter extends BaseAdapter{
        private List<ProIntroduceBean> proIntroduceList;

        public ProductAdapter(List<ProIntroduceBean> proIntroduceList) {
            this.proIntroduceList = proIntroduceList;
        }

        @Override
        public int getCount() {
            return proIntroduceList.size();
        }

        @Override
        public Object getItem(int position) {
            return proIntroduceList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh = null;
            if (convertView == null) {
                vh = new ViewHolder();
                convertView = View.inflate(getActivity(), R.layout.adapter_introduce, null);
                vh.title = (TextView) convertView.findViewById(R.id.title);
                vh.content = (TextView) convertView.findViewById(R.id.content);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            ProIntroduceBean proIntroduceBean = proIntroduceList.get(position);
            vh.title.setText(proIntroduceBean.getTitle());
            vh.content.setText(proIntroduceBean.getContent());
            return convertView;
        }

        class ViewHolder{
            private TextView title, content;
        }

    }

    private class lv_adapter extends BaseAdapter {
        private List<bean_Detail_Info_pic> al;

        public lv_adapter(List<bean_Detail_Info_pic> al) {
            this.al = al;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return al.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return al.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh = null;
            if (convertView == null) {
                vh = new ViewHolder();
                convertView = View.inflate(getActivity(), R.layout.adapter_frag_prodetail_picture, null);
                vh.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                vh.image = (SimpleDraweeView) convertView.findViewById(R.id.image);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }

            vh.tv_name.setText(al.get(position).getName());
            Uri uri = Uri.parse(al.get(position).getBigUrl());
            vh.image.setImageURI(uri);
            return convertView;
        }

    }

    private LinearLayout layout;
    private PopupWindow popupWindow;
    private SimpleDraweeView iv_regist;

    public void showPopupWindow1(String url_image) {
        // 加载布局
        layout = (LinearLayout) LayoutInflater.from(getContext()).inflate(
                R.layout.dialog_image_big, null);
        // 找到布局的控件
        // 实例化popupWindow
        popupWindow = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        iv_regist = (SimpleDraweeView) (layout).findViewById(R.id.iv_regist);
        Uri uri = Uri.parse(url_image);
        DraweeView<DraweeHierarchy> mDraweeView = new DraweeView<>(getContext());
        iv_regist.setAspectRatio(1f);
        int width = 110, height = 110;
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(width, height))
                .build();
        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setOldController(mDraweeView.getController())
                .setImageRequest(request)
                .build();
        iv_regist.setController(controller);

        iv_regist.setImageURI(uri);
        // 控制键盘是否可以获得焦点
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
//		popupWindow.setBackgroundDrawable(new PaintDrawable());
//		popupWindow.setOutsideTouchable(true);

//		// 设置popupWindow弹出窗体的背景
//		WindowManager manager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        // 监听
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                // TODO Auto-generated method stub
                backgroundAlpha(1f);
            }
        });
        popupWindow.showAsDropDown(iv_regist);
//		backgroundAlpha(0.5f);
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     **/

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }

    private class ViewHolder {
        private TextView title, tv_name;
        private View adapter_view;
        private SimpleDraweeView image;
    }

}
