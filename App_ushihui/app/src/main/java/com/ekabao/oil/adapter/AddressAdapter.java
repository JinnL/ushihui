package com.ekabao.oil.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ekabao.oil.R;
import com.ekabao.oil.bean.AddressBean;
import com.ekabao.oil.ui.view.ToastMaker;

import java.util.List;


/**
 * Created by DELL on 2017/10/30.
 * 描述：收货地址的adapter
 */

public class AddressAdapter extends BaseAdapter {
    private Context context;
    private List<AddressBean> list;
    private LayoutInflater inflater;
    private boolean isChecked;
    private AddressManageListener mAddressManageListener;

    public AddressAdapter(Context context, List<AddressBean> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_address, null);
            vh.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            vh.tvPhonenum = (TextView) convertView.findViewById(R.id.tv_phonenum);
            vh.tvDetailAddress = (TextView) convertView.findViewById(R.id.tv_detail_address);
            vh.cbDefaultAddress = (CheckBox) convertView.findViewById(R.id.cb_default_address);
            vh.tvDeleteAddress = (TextView) convertView.findViewById(R.id.tv_delete_address);
            vh.tvEditAddress = (TextView) convertView.findViewById(R.id.tv_edit_address);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        final AddressBean addressBean = list.get(position);
        vh.tvName.setText(addressBean.getName());
        vh.tvPhonenum.setText(addressBean.getPhone());
        vh.tvDetailAddress.setText(addressBean.getProvinceName() + addressBean.getCityName()
                + addressBean.getAreaName() + addressBean.getAddress());
        if (addressBean.getAddressDefault() == 1) {
            vh.cbDefaultAddress.setChecked(true);
        } else {
            vh.cbDefaultAddress.setChecked(false);
        }
        //接口回调
        final CheckBox cbDefaultAddress = vh.cbDefaultAddress;
       // final ViewHolder finalVh = vh;
        vh.cbDefaultAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (addressBean.getAddressDefault() == 1) {
                    ToastMaker.showShortToast("已经是默认地址了");
                    cbDefaultAddress.setChecked(true);
                } else {
                    if (mAddressManageListener != null) {
                        mAddressManageListener.onSelected(position);
                    }
                }
            }
        });
        vh.tvDeleteAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAddressManageListener != null) {
                    mAddressManageListener.onDelete(position);
                }
            }
        });
        vh.tvEditAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAddressManageListener != null) {
                    mAddressManageListener.onEdit(position);
                }
            }
        });
        return convertView;
    }

    private class ViewHolder {
        private TextView tvName;
        private TextView tvPhonenum;
        private TextView tvDetailAddress;
        private CheckBox cbDefaultAddress;
        private TextView tvDeleteAddress;
        private TextView tvEditAddress;

    }

    public interface AddressManageListener {
        void onSelected(int position);

        void onEdit(int position);

        void onDelete(int position);
    }

    public void setOnAddressManageListener(AddressManageListener listener) {
        this.mAddressManageListener = listener;
    }
}