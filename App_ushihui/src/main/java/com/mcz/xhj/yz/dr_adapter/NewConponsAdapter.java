package com.mcz.xhj.yz.dr_adapter;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_bean.ConponsBean;
import com.mcz.xhj.yz.dr_util.stringCut;

import java.util.List;

/*
* 2.0版的优惠券
* */

public class NewConponsAdapter extends BaseAdapter {
    private Context context;
    private List<ConponsBean> lsct;
    private LayoutInflater inflater;
    private String flag;//产品详情选择了优惠券标识id 体验金 优惠券
    private String amount;//
    private onButtonClickL lis;
    private SpannableString ss;

    public NewConponsAdapter(Context context, List<ConponsBean> lsct, String flag, onButtonClickL lis) {
        super();
        this.context = context;
        this.lsct = lsct;
        this.flag = flag;
        this.lis = lis;
        inflater = LayoutInflater.from(context);
    }

    public NewConponsAdapter(Context context, List<ConponsBean> lsct, String flag, onButtonClickL lis, String amount) {
        super();
        this.context = context;
        this.lsct = lsct;
        this.flag = flag;
        this.lis = lis;
        this.amount = amount;
        inflater = LayoutInflater.from(context);
    }

    public interface onButtonClickL {
        void onButtonClick(View view);
    }

    public void onDateChange(List<ConponsBean> lsct) {
        this.lsct = lsct;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return lsct.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return lsct.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    private class ViewHolder {
        private TextView tv_name;
        private TextView tv_time;
        private TextView tv_money;
        private TextView tv_use;
        private TextView tv_dd;
        private TextView tv_show;
        private TextView cover_tv;
        private ImageView tv_tab;
        private LinearLayout ll_conpons_bg;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_new_conpons, null);
            vh = new ViewHolder();
            vh.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            vh.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            vh.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
            vh.tv_use = (TextView) convertView.findViewById(R.id.tv_use);
            vh.tv_dd = (TextView) convertView.findViewById(R.id.tv_dd);
            vh.tv_show = (TextView) convertView.findViewById(R.id.tv_show);
            vh.cover_tv = (TextView) convertView.findViewById(R.id.cover_tv);
            vh.tv_tab = (ImageView) convertView.findViewById(R.id.tv_tab);
            vh.ll_conpons_bg = (LinearLayout) convertView.findViewById(R.id.ll_conpons_bg);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        final LinearLayout ll_bg = (LinearLayout) convertView.findViewById(R.id.bg);
        final ConponsBean cb = lsct.get(position);

        switch (cb.getType()) {
            case 1:// 返现
                vh.ll_conpons_bg.setBackgroundResource(R.mipmap.bg_conpons_red);
                vh.tv_name.setText("返现红包-" + cb.getRemark());
                //vh.tv_wenan1.setText(cb.getRemark());//发放途径
                vh.tv_show.setText("单笔投资满" + stringCut.getNumKbs(cb.getEnableAmount()) + "元，投资期限≥" + cb.getProductDeadline() + "天");
//"单笔投资满"+stringCut.getNumKbs(cb.getEnableAmount())+"元，投资期限≥"+cb.getProductDeadline()+"天"
                String xian_h = "￥" + stringCut.getNumKbs(cb.getAmount());
                SpannableString ss = new SpannableString(xian_h);
                ss.setSpan(new RelativeSizeSpan(0.6f), 0, 1, TypedValue.COMPLEX_UNIT_PX);
                vh.tv_money.setText(ss);
//  vh.tv_money.setText("￥" + stringCut.getNumKbs(cb.getAmount()));
                if (cb.getExpireDate() != null && !cb.getExpireDate().equalsIgnoreCase("")) {
                    vh.tv_dd.setText("有效期至：");
                    vh.tv_time.setText(stringCut.getDateYearToString(Long.parseLong(cb.getExpireDate())));
                } else {
                    vh.tv_dd.setText("");
                    vh.tv_time.setText("永久有效");
                }
                break;
            case 2:// 加息
            case 3:// 体验
//			vh.bg.setBackgroundResource(R.mipmap.card_bg_red);
                if (cb.getType() == 2) {
                    vh.tv_name.setText("加息券-" + cb.getRemark());
                    vh.ll_conpons_bg.setBackgroundResource(R.mipmap.bg_conpons_jiaxi);
                    String rate_h = stringCut.getNumKbs(cb.getRaisedRates()) + "%";
                    //vh.tv_wenan1.setText( cb.getRemark());
                    SpannableString sss = new SpannableString(rate_h);
                    sss.setSpan(new RelativeSizeSpan(0.6f), rate_h.length() - 1, rate_h.length(), TypedValue.COMPLEX_UNIT_PX);
                    vh.tv_money.setText(sss);
                } else {
                    vh.tv_money.setText("￥" + stringCut.getNumKbs(cb.getAmount()));
                    vh.tv_name.setText("体验金-" + cb.getRemark());
                    vh.ll_conpons_bg.setBackgroundResource(R.mipmap.bg_experience_conpons);
                    //vh.tv_wenan1.setText(cb.getRemark());
                }
//			vh.bg.setBackgroundResource(R.mipmap.card_bg_red);
//			vh.tv_name.setText("加息券");
//			vh.tv_wenan2.setText(stringCut.getNumKbs(cb.getEnableAmount()));
                if (cb.getProductDeadline() != null) {
                    if (cb.getProductDeadline() > 0) {
                        if (cb.getPid() != null) {
//						vh.tv_qixian.setText("限用标的："+cb.getFullName());
                            vh.tv_show.setText("限用标的" + cb.getFullName() + "，投资期限≥" + cb.getProductDeadline() + "天");
//						"限用标的"+cb.getFullName()+"，投资期限≥"+cb.getProductDeadline()+"天"
                        } else {
//						vh.tv_qixian.setText("投资期限："+cb.getProductDeadline() + "天以上"+"（含"+cb.getProductDeadline()+"天）");
                            vh.tv_show.setText("单笔投资满" + stringCut.getNumKbs(cb.getEnableAmount()) + "元，投资期限≥" + cb.getProductDeadline() + "天");
//						"单笔投资满"+stringCut.getNumKbs(cb.getEnableAmount())+"元，投资期限≥"+cb.getProductDeadline()+"天"
                        }
                    } else if (cb.getProductDeadline() == 0) {
                        vh.tv_show.setText("无限制");
                    }
//			String rate_h = stringCut.getNumKbs(cb.getRaisedRates()) + "%";
//			SpannableString sss = new SpannableString(rate_h);
                }
//			sss.setSpan(new RelativeSizeSpan(0.6f), rate_h.length() - 1,
//					rate_h.length(), TypedValue.COMPLEX_UNIT_PX);
//			vh.tv_money.setText("+"+rate_h);
                if (cb.getExpireDate() != null && !cb.getExpireDate().equalsIgnoreCase("")) {
                    vh.tv_dd.setText("有效期至：");
                    if (cb.getPid() != null) {
                        vh.tv_time.setText("满标过期");
                    } else {
                        vh.tv_time.setText(stringCut.getDateYearToString(Long.parseLong(cb.getExpireDate())));
                    }
                } else {
                    vh.tv_dd.setText("");
                    vh.tv_time.setText("永久有效");
                }
                break;
            case 4:// 翻倍券
                vh.ll_conpons_bg.setBackgroundResource(R.mipmap.bg_conpons_fanbei);
                vh.tv_name.setText("翻倍券-" + cb.getRemark());
                //vh.tv_wenan1.setText(cb.getRemark());//发放途径
                String jin_hh = stringCut.getNumKbs(cb.getMultiple()) + "倍";
                if (cb.getProductDeadline() != null) {
                    if (cb.getProductDeadline() > 0) {
//					vh.tv_qixian.setText("投资期限："+cb.getProductDeadline() + "天以上"+"（含"+cb.getProductDeadline()+"天）");
                        vh.tv_show.setText("基础利率翻" + jin_hh + "，投资期限≥" + cb.getProductDeadline() + "天");
                    } else if (cb.getProductDeadline() == 0) {
                        vh.tv_show.setText("无限制");
                    }
                }
                SpannableString sssss = new SpannableString(jin_hh);
                sssss.setSpan(new RelativeSizeSpan(0.6f), jin_hh.length() - 1, jin_hh.length(), TypedValue.COMPLEX_UNIT_PX);
                vh.tv_money.setText(sssss);
//                vh.tv_money.setText(jin_hh);
                if (cb.getExpireDate() != null && !cb.getExpireDate().equalsIgnoreCase("")) {
                    vh.tv_dd.setText("有效期至：");
                    vh.tv_time.setText(stringCut.getDateYearToString(Long.parseLong(cb
                            .getExpireDate())));
                } else {
                    vh.tv_dd.setText("");
                    vh.tv_time.setText("永久有效");
                }
                break;
//		case 3:// 体验
//			vh.bg.setBackgroundResource(R.mipmap.card_bg);
//			vh.tv_name.setText("体验金");
//			vh.tv_wenan1.setText("红包作用：增加投资本金");
//			vh.tv_wenan3.setVisibility(View.VISIBLE);
//			vh.textView1.setVisibility(View.VISIBLE);
//			vh.tv_wenan2.setText(stringCut.getNumKbs(cb.getEnableAmount()));
//			if (cb.getProductDeadline() != null) {
//				if (cb.getProductDeadline() > 0) {
//					vh.tv_qixian.setText("产品期限≥" + cb.getProductDeadline() + "天且产品可用体验金");
//				} else if (cb.getProductDeadline() == 0) {
//					vh.tv_qixian.setText("无限制");
//				}
//			}
//			String jin_h = stringCut.getNumKbs(cb.getAmount()) + "元";
//			SpannableString ssss = new SpannableString(jin_h);
//			ssss.setSpan(new RelativeSizeSpan(0.6f), jin_h.length() - 1,
//					jin_h.length(), TypedValue.COMPLEX_UNIT_PX);
//			vh.tv_money.setText(ssss);
//			vh.tv_dd.setText("");
//			vh.tv_time.setText("仅限新手标");
//			break;

            default:
                break;
        }
        switch (cb.getStatus()) {
            case 3:// 选择优惠券的时候金额不可用
                vh.ll_conpons_bg.setBackgroundResource(R.mipmap.conpons_off);
//                vh.cover_tv.setVisibility(View.VISIBLE);
                break;
            case 1:// 选择优惠券的时候金额不可用
                vh.ll_conpons_bg.setBackgroundResource(R.mipmap.conpons_off);
                vh.tv_use.setText("已使用");
//                vh.cover_tv.setVisibility(View.VISIBLE);
                break;
            case 2:// 选择优惠券的时候金额不可用
                vh.ll_conpons_bg.setBackgroundResource(R.mipmap.conpons_off);
//                vh.cover_tv.setVisibility(View.VISIBLE);
                vh.tv_use.setText("已过期");
                break;
            default:
//                vh.cover_tv.setVisibility(View.GONE);
                break;
        }
        if (flag.equals("tiyanjin")) {
            //vh.tv_tab.setVisibility(View.GONE);
            for (int i = 0; i < lsct.size(); i++) {
                if (Double.valueOf(amount) < (lsct.get(position).getEnableAmount())) {
                    vh.ll_conpons_bg.setBackgroundResource(R.mipmap.conpons_off);//不可用
                    lsct.get(position).setStatus(3);
                    vh.tv_use.setText("立即激活");
                } else {
                    vh.ll_conpons_bg.setBackgroundResource(R.mipmap.bg_experience_conpons);
                    lsct.get(position).setStatus(0);
                    vh.tv_use.setText("立即激活");
                }
            }
        }
        return convertView;
    }
}
