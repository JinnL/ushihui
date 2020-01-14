package com.ekabao.oil.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ekabao.oil.R;
import com.ekabao.oil.bean.Product;
import com.ekabao.oil.util.LogUtils;
import com.ekabao.oil.util.StatusBarUtil;
import com.wuhenzhizao.sku.bean.Sku;
import com.wuhenzhizao.sku.bean.SkuAttribute;
import com.wuhenzhizao.sku.view.OnSkuListener;
import com.wuhenzhizao.sku.view.SkuSelectScrollView;

import java.util.List;

/**
 * Created by liufei on 2017/11/30.
 */
public class ProductSkuDialog extends Dialog {

    // @BindView(R.id.iv_sku_logo)
    ImageView ivSkuLogo;
    // @BindView(R.id.ib_sku_close)
    ImageView ibSkuClose;
    // @BindView(R.id.tv_sku_selling_price)
    TextView tvSkuSellingPrice;
    // @BindView(R.id.tv_sku_quantity)
    TextView tvSkuQuantity;
    //  @BindView(R.id.tv_sku_info)
    TextView tvSkuInfo;
    //  @BindView(R.id.scroll_sku_list)
    SkuSelectScrollView scrollSkuList;
    //  @BindView(R.id.btn_sku_quantity_minus)
    TextView btnSkuQuantityMinus;
    //  @BindView(R.id.et_sku_quantity_input)
    EditText etSkuQuantityInput;
    // @BindView(R.id.btn_sku_quantity_plus)
    TextView btnSkuQuantityPlus;
    // @BindView(R.id.btn_submit)
    Button btnSubmit;

    private View binding;
    private Context context;
    private Product product;
    private List<Sku> skuList;
    private Callback callback;

    private Sku selectedSku;
    private String priceFormat;
    private String stockQuantityFormat;

    public ProductSkuDialog(@NonNull Context context) {
        this(context, R.style.DialogNoTitleStyleTranslucentBg);
    }

    public ProductSkuDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.context = context;
        binding = LayoutInflater.from(context).inflate(R.layout.pop_goods_details, null);

        setContentView(binding);

        initView();

        Window window = this.getWindow();
        window.setWindowAnimations(R.style.dialogWindowAnim);

        WindowManager.LayoutParams aWmLp = window.getAttributes();
        //aWmLp.width = LocalApplication.getInstance().screenW - 200;
        // aWmLp.height = LocalApplication.getInstance().screenH / 2;//设置高度
        aWmLp.gravity = Gravity.BOTTOM;
        window.setAttributes(aWmLp);
    }

   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



      //  this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
      //  this.getWindow().setGravity(Gravity.CENTER_VERTICAL);
    }*/

    private void initView() {

        //  setContentView(getRoot());
        tvSkuSellingPrice = (TextView) binding.findViewById(R.id.tv_sku_selling_price);
        tvSkuQuantity = (TextView) binding.findViewById(R.id.tv_sku_quantity);
        tvSkuInfo = (TextView) binding.findViewById(R.id.tv_sku_info);
        btnSkuQuantityMinus = (TextView) binding.findViewById(R.id.btn_sku_quantity_minus);
        btnSkuQuantityPlus = (TextView) binding.findViewById(R.id.btn_sku_quantity_plus);

        ivSkuLogo = (ImageView) binding.findViewById(R.id.iv_sku_logo);
        ibSkuClose = (ImageView) binding.findViewById(R.id.ib_sku_close);

        etSkuQuantityInput = (EditText) binding.findViewById(R.id.et_sku_quantity_input);

        scrollSkuList = (SkuSelectScrollView) binding.findViewById(R.id.scroll_sku_list);


        btnSubmit = (Button) binding.findViewById(R.id.btn_submit);


        LogUtils.e("product.getSkus()"+ scrollSkuList);

        ibSkuClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnSkuQuantityMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantity = etSkuQuantityInput.getText().toString();
                if (TextUtils.isEmpty(quantity)) {

                    return;
                }
                int quantityInt = Integer.parseInt(quantity);
                if (quantityInt > 1) {
                    String newQuantity = String.valueOf(quantityInt - 1);
                    etSkuQuantityInput.setText(newQuantity);
                    etSkuQuantityInput.setSelection(newQuantity.length());
                    updateQuantityOperator(quantityInt - 1);
                }
            }
        });
        btnSkuQuantityPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantity = etSkuQuantityInput.getText().toString();
                if (TextUtils.isEmpty(quantity)){
                    //quantity="1";
                    etSkuQuantityInput.setText("1");
                    return;
                }
                if (TextUtils.isEmpty(quantity) || selectedSku == null) {
                    return;
                }
                int quantityInt = Integer.parseInt(quantity);
                if (quantityInt < selectedSku.getStockQuantity()) {
                    String newQuantity = String.valueOf(quantityInt + 1);
                    etSkuQuantityInput.setText(newQuantity);
                    etSkuQuantityInput.setSelection(newQuantity.length());
                    updateQuantityOperator(quantityInt + 1);
                }
            }
        });

        etSkuQuantityInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String quantity = etSkuQuantityInput.getText().toString();
                if (TextUtils.isEmpty(quantity)) {

                    ToastMaker.showShortToast("数量不能为空");
                  //
                }
            }
        });
        etSkuQuantityInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId != EditorInfo.IME_ACTION_DONE || selectedSku == null) {
                    return false;
                }
                String quantity = etSkuQuantityInput.getText().toString();
                if (TextUtils.isEmpty(quantity)) {

                    ToastMaker.showShortToast("数量不能为空");
                    return false;
                }
                int quantityInt = Integer.parseInt(quantity);
                if (quantityInt <= 1) {
                    etSkuQuantityInput.setText("1");
                    etSkuQuantityInput.setSelection(1);
                    updateQuantityOperator(1);
                } else if (quantityInt >= selectedSku.getStockQuantity()) {
                    String newQuantity = String.valueOf(selectedSku.getStockQuantity());
                    etSkuQuantityInput.setText(newQuantity);
                    etSkuQuantityInput.setSelection(newQuantity.length());
                    updateQuantityOperator(selectedSku.getStockQuantity());
                } else {
                    etSkuQuantityInput.setSelection(quantity.length());
                    updateQuantityOperator(quantityInt);
                }
                return false;
            }
        });
        // TODO: 2019/1/8
        scrollSkuList.setListener(new OnSkuListener() {
            @Override
            public void onUnselected(SkuAttribute unselectedAttribute) {
                selectedSku = null;
                // GImageLoader.displayUrl(context, ivSkuLogo, product.getMainImage());

                Glide.with(context)
                        .load(product.getMainImage())
                        .centerCrop()
                        .into(ivSkuLogo);


                tvSkuQuantity.setText(String.format(stockQuantityFormat, product.getStockQuantity()));
                String firstUnselectedAttributeName = scrollSkuList.getFirstUnelectedAttributeName();
                tvSkuInfo.setText("请选择：" + firstUnselectedAttributeName);
                btnSubmit.setEnabled(false);
                String quantity = etSkuQuantityInput.getText().toString();
                if (!TextUtils.isEmpty(quantity)) {
                    updateQuantityOperator(Integer.valueOf(quantity));
                } else {
                    updateQuantityOperator(0);
                }
            }

            @Override
            public void onSelect(SkuAttribute selectAttribute) {
                String firstUnselectedAttributeName = scrollSkuList.getFirstUnelectedAttributeName();
                tvSkuInfo.setText("请选择：" + firstUnselectedAttributeName);
            }

            @Override
            public void onSkuSelected(Sku sku) {
                selectedSku = sku;
                //GImageLoader.displayUrl(context, ivSkuLogo, selectedSku.getMainImage());
                Glide.with(context)
                        .load(product.getMainImage())
                        .centerCrop()
                        .into(ivSkuLogo);

                List<SkuAttribute> attributeList = selectedSku.getAttributes();
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < attributeList.size(); i++) {
                    if (i != 0) {
                        builder.append("　");
                    }
                    SkuAttribute attribute = attributeList.get(i);
                    builder.append("\"" + attribute.getValue() + "\"");
                }

                tvSkuSellingPrice.setText(String.format(priceFormat, selectedSku.getSellingPrice()));

                tvSkuInfo.setText("已选：" + builder.toString());
                tvSkuQuantity.setText(String.format(stockQuantityFormat, selectedSku.getStockQuantity()));
                btnSubmit.setEnabled(true);
                String quantity = etSkuQuantityInput.getText().toString();
                if (!TextUtils.isEmpty(quantity)) {
                    updateQuantityOperator(Integer.valueOf(quantity));
                } else {
                    updateQuantityOperator(0);
                }
            }
        });

        //确定
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantity = etSkuQuantityInput.getText().toString();
                if (TextUtils.isEmpty(quantity)) {
                    return;
                }
                int quantityInt = Integer.parseInt(quantity);
                if (quantityInt > 0 && quantityInt <= selectedSku.getStockQuantity()) {
                    callback.onAdded(selectedSku, quantityInt);

                    // 选中第一个sku
                    scrollSkuList.setSelectedSku(selectedSku);
                    dismiss();


                } else {
                    Toast.makeText(getContext(), "商品数量超出库存，请修改数量", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setData(final Product product, Callback callback) {
        this.product = product;
        this.skuList = product.getSkus();
        this.callback = callback;

        priceFormat = context.getString(R.string.comm_price_format);
        stockQuantityFormat = context.getString(R.string.product_detail_sku_stock);

        updateSkuData();
        updateQuantityOperator(1);
    }

    /**
     *
     *
     * */
    private void updateSkuData() {
    //    LogUtils.e("product.getSkus()"+product.getSkus().size()+ scrollSkuList);

        scrollSkuList.setSkuList(product.getSkus());

        Log.d("ProductSkuDialog",product.getSkus()+"");

        Sku firstSku = product.getSkus().get(0);
        if (firstSku.getStockQuantity() > 0) {


            selectedSku = firstSku;
            // 选中第一个sku
          //  scrollSkuList.setSelectedSku(selectedSku);
            //
          //  scrollSkuList.setfirstlineStatus();
            // GImageLoader.displayUrl(context, ivSkuLogo, selectedSku.getMainImage());

            Glide.with(context)
                    .load(product.getMainImage())
                    .centerCrop()
                    .into(ivSkuLogo);

            tvSkuSellingPrice.setText(String.format(priceFormat, product.getSellingPrice()));
            // tvSkuSellingPriceUnit.setText("/" + product.getMeasurementUnit());
            tvSkuQuantity.setText(String.format(stockQuantityFormat, selectedSku.getStockQuantity()));
            btnSubmit.setEnabled(selectedSku.getStockQuantity() > 0);
            List<SkuAttribute> attributeList = selectedSku.getAttributes();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < attributeList.size(); i++) {
                if (i != 0) {
                    builder.append("　");
                }
                SkuAttribute attribute = attributeList.get(i);
                builder.append("\"" + attribute.getValue() + "\"");
            }
            tvSkuInfo.setText("已选：" + builder.toString());
        } else {
            // GImageLoader.displayUrl(context, ivSkuLogo, product.getMainImage());
            Glide.with(context)
                    .load(product.getMainImage())
                    .centerCrop()
                    .into(ivSkuLogo);

            tvSkuSellingPrice.setText(String.format(priceFormat, product.getSellingPrice()));

            //  tvSkuSellingPriceUnit.setText("/" + product.getMeasurementUnit());

            tvSkuQuantity.setText(String.format(stockQuantityFormat, product.getStockQuantity()));

            btnSubmit.setEnabled(false);
            tvSkuInfo.setText("请选择：" + skuList.get(0).getAttributes().get(0).getKey());
        }
    }

    private void updateQuantityOperator(int newQuantity) {
        if (selectedSku == null) {
            btnSkuQuantityMinus.setEnabled(false);
            btnSkuQuantityPlus.setEnabled(false);
            etSkuQuantityInput.setEnabled(false);
        } else {
            if (newQuantity <= 1) {
                btnSkuQuantityMinus.setEnabled(false);
                btnSkuQuantityPlus.setEnabled(true);
            } else if (newQuantity >= selectedSku.getStockQuantity()) {
                btnSkuQuantityMinus.setEnabled(true);
                btnSkuQuantityPlus.setEnabled(false);
            } else {
                btnSkuQuantityMinus.setEnabled(true);
                btnSkuQuantityPlus.setEnabled(true);
            }
            etSkuQuantityInput.setEnabled(true);
        }

    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        // 解决键盘遮挡输入框问题
        Window window = getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.getDecorView().setPadding(0, 0, 0, 0);
        // KeyboardConflictCompat.assistWindow(getWindow());
        //AppUtils.transparencyBar(getWindow());

        StatusBarUtil.setStatusBarLightMode(getWindow());
    }


    public interface Callback {
        void onAdded(Sku sku, int quantity);
    }
}
