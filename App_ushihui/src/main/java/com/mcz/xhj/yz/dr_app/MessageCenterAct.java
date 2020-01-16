package com.mcz.xhj.yz.dr_app;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mcz.xhj.R;
import com.mcz.xhj.yz.dr_application.LocalApplication;
import com.mcz.xhj.yz.dr_bean.Rows_Message;
import com.mcz.xhj.yz.dr_right_delete.SwipeMenu;
import com.mcz.xhj.yz.dr_right_delete.SwipeMenuCreator;
import com.mcz.xhj.yz.dr_right_delete.SwipeMenuItem;
import com.mcz.xhj.yz.dr_right_delete.SwipeMenuListView;
import com.mcz.xhj.yz.dr_right_delete.SwipeMenuListView.OnMenuItemClickListener;
import com.mcz.xhj.yz.dr_right_delete.SwipeMenuListView.OnSwipeListener;
import com.mcz.xhj.yz.dr_urlconfig.UrlConfig;
import com.mcz.xhj.yz.dr_util.show_Dialog_IsLogin;
import com.mcz.xhj.yz.dr_util.stringCut;
import com.mcz.xhj.yz.dr_view.ToastMaker;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import okhttp3.Call;

public class MessageCenterAct extends BaseActivity implements OnClickListener {
	@BindView(R.id.title_leftimageview)
	ImageView title_leftimageview;
	@BindView(R.id.title_centertextview)
	TextView title_centertextview;
	@BindView(R.id.title_righttextview)
	TextView title_righttextview;
	@BindView(R.id.rb_allmessage_jiaoyi)
	RadioButton rb_allmessage_jiaoyi;
	@BindView(R.id.rb_allmessage_huodong)
	RadioButton rb_allmessage_huodong;
	@BindView(R.id.rb_allmessage_xitong)
	RadioButton rb_allmessage_xitong;
	private com.mcz.xhj.yz.dr_right_delete.SwipeMenuListView lv_message;
	@BindView(R.id.lin_edit)
	LinearLayout lin_edit;
	@BindView(R.id.tv_allchoose)
	TextView tv_allchoose;
	@BindView(R.id.tv_allread)
	TextView tv_allread;
	@BindView(R.id.tv_alldelete)
	TextView tv_alldelete;
	@BindView(R.id.tv_allfinish)
	TextView tv_allfinish;
	@BindView(R.id.rl_no_nomessage)
	RelativeLayout rl_no_nomessage ;
	@BindView(R.id.ptr_invest)
	PtrClassicFrameLayout ptr_invest; // jiaxi

	private ArrayList<String> list_message_type = new ArrayList<String>(); // 消息分类
	private List<Rows_Message> rows_List = new ArrayList<Rows_Message>();// 每次加载的数据
	private List<Rows_Message> mrows_List = new ArrayList<Rows_Message>();//  每次加载的数据

	private ArrayList<Rows_Message> selectid = new ArrayList<Rows_Message>(); // 选中消息列表

	private List<String> list_message_ID = new ArrayList<String>(); // 删除消息ID

	private Adapter_list adapter_list;
	private String uid;
	private SharedPreferences preferences;

	private boolean isLastitem = false;
	private View footer;
	private LinearLayout footerlayout;
	private TextView tv_footer;
	private ProgressBar pb;
	private boolean isLoading;

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.act_messagecenter;
	}

	@Override
	protected void initParams() {
		// TODO Auto-generated method stub
		title_leftimageview.setVisibility(View.VISIBLE);
		title_righttextview.setVisibility(View.GONE);
		title_centertextview.setText("消息记录");
		title_righttextview.setText("编辑");

		list_message_type.add("全部消息");
		list_message_type.add("系统消息");
		list_message_type.add("活动消息");
		list_message_type.add("交易记录");

		lv_message = (com.mcz.xhj.yz.dr_right_delete.SwipeMenuListView) findViewById(R.id.lv_message);
		footer = View.inflate(MessageCenterAct.this, R.layout.footer_layout, null);
		footerlayout = (LinearLayout) footer.findViewById(R.id.load_layout);
		pb = (ProgressBar) footer.findViewById(R.id.pb);
		tv_footer = (TextView) footer.findViewById(R.id.tv_footer);
		footerlayout.setVisibility(View.GONE);
		lv_message.addFooterView(footer, null, false);
		lv_message.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
								 int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if (firstVisibleItem + visibleItemCount == totalItemCount) {
					isLastitem = true;
				}else{
					isLastitem = false;
				}
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if (isLastitem && scrollState == SCROLL_STATE_IDLE) {
					if (!isLoading) {
						isLoading = true;
						footerlayout.setVisibility(View.VISIBLE);
						getMessage() ;
					}
				}
			}
		});
		ptr_invest.setPtrHandler(new PtrHandler() {

			@Override
			public void onRefreshBegin(PtrFrameLayout frame) {
				pageOn = 1  ;
				getMessage() ;
			}

			@Override
			public boolean checkCanDoRefresh(PtrFrameLayout frame,
											 View content, View header) {
				// TODO Auto-generated method stub
				return PtrDefaultHandler.checkContentCanBePulledDown(frame, lv_message, header) ;
			}
		});
		rb_allmessage_jiaoyi.setOnClickListener(this);
		rb_allmessage_huodong.setOnClickListener(this);
		rb_allmessage_xitong.setOnClickListener(this);
		tv_allchoose.setOnClickListener(this);
		tv_allread.setOnClickListener(this);
		tv_alldelete.setOnClickListener(this);
		tv_allfinish.setOnClickListener(this);
		title_righttextview.setOnClickListener(this);
		title_leftimageview.setOnClickListener(this);
		preferences = LocalApplication.getInstance().sharereferences;
		uid = preferences.getString("uid", "");
		getMessage(); // 获得消息

		initRightDelete();

	}

	private void initRightDelete() {

		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {

				SwipeMenuItem deleteItem = new SwipeMenuItem(
						getApplicationContext());
				// set item background
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
						0x3F, 0x25)));
				// set item width
				deleteItem.setWidth(dp2px(90));
				// set a icon
				deleteItem.setIcon(R.mipmap.ic_delete);
				// add to menu
				menu.addMenuItem(deleteItem);
			}
		};
		((SwipeMenuListView) lv_message).setMenuCreator(creator);
		lv_message.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public void onMenuItemClick(int position, SwipeMenu menu, int index) {
				switch (index) {
					case 0:
						list_message_ID.add(rows_List.get(position).getId());
						rows_List.remove(position);
						delMsg();
						selectid.clear();
						adapter_list = new Adapter_list(MessageCenterAct.this, false);
						lv_message.setAdapter(adapter_list);
						break;
				}
			}
		});
		lv_message.setOnSwipeListener(new OnSwipeListener() {

			@Override
			public void onSwipeStart(int position) {
				// swipe start
			}

			@Override
			public void onSwipeEnd(int position) {
				// swipe end
			}
		});

	}

	int i = 0;
	private int allchoose = 0; // 全选次数

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.title_leftimageview:
				finish() ;
				break;
			case R.id.tv_allfinish:
				isMulChoice = false;
				selectid.clear();
				rb_allmessage_huodong.setText("编辑");
				lin_edit.setVisibility(View.GONE);
				adapter_list = new Adapter_list(MessageCenterAct.this, false);
				lv_message.setAdapter(adapter_list);
				break;
			case R.id.tv_alldelete:
				isMulChoice = true;
				for (int i = 0; i < selectid.size(); i++) {
					for (int j = 0; j < rows_List.size(); j++) {
						if (selectid.get(i).equals(rows_List.get(j))) {
							list_message_ID.add(rows_List.get(j).getId());
							rows_List.remove(j);
						}
					}
				}
				delMsg();
				selectid.clear();
				adapter_list = new Adapter_list(MessageCenterAct.this, false);
				lv_message.setAdapter(adapter_list);
				break;
			case R.id.tv_allread:
				// 去处重复数据
				ArrayList bl = new ArrayList();
				for (int i = 0; i < selectid.size(); i++) {
					if (!bl.contains(selectid.get(i))) {
						bl.add(selectid.get(i));
					}
				}
				selectid = bl;
				for (int i = 0; i < selectid.size(); i++) {
					for (int j = 0; j < rows_List.size(); j++) {
						if (selectid.get(i).equals(rows_List.get(j))) {
							list_message_ID.add(rows_List.get(j).getId());

						}
					}
				}
				// 遍历获得选取的数据
				isMulChoice = true;
				updateUnReadMsg();
				// StringBuffer sb = new StringBuffer();
				// for (int i = 0; i < selectid.size(); i++) {
				// sb.append(selectid.get(i));
				// }
				// String newStr = sb.toString();

				Toast.makeText(MessageCenterAct.this,
						"选了那些？" + list_message_ID.size(), Toast.LENGTH_LONG).show();
				break;
			// 全选
			case R.id.tv_allchoose:
				switch (allchoose % 2) {
					case 0:
						tv_alldelete.setTextColor(Color.parseColor("#FE7634"));
						tv_allchoose.setText("取消全选");
						isMulChoice = true;
						selectid.clear();
						// layout.setVisibility(View.VISIBLE);
						// for (int allchoose = 0; allchoose < title_lv.size();
						// allchoose++) {
						// lv_adapter.visiblecheck.put(allchoose, CheckBox.VISIBLE);
						// }
						lin_edit.setVisibility(View.VISIBLE);
						adapter_list = new Adapter_list(MessageCenterAct.this, true);
						lv_message.setAdapter(adapter_list);
						allchoose++;
						break;
					case 1:
						tv_alldelete.setTextColor(Color.parseColor("#D2D2D2"));
						tv_allchoose.setText("全选");
						isMulChoice = true;
						selectid.clear();
						list_message_ID.clear();
						adapter_list = new Adapter_list(MessageCenterAct.this, false);
						lv_message.setAdapter(adapter_list);
						allchoose++;
						break;

					default:
						break;
				}
				break;
			// 消息列表
			case R.id.rb_allmessage_xitong:
				message_Check = 1;
				pageOn = 1  ;
				rows_List.clear();
				getMessage();
//			showPopupWindow(findViewById(R.id.view_line));
				break;
			// 消息列表
			case R.id.rb_allmessage_huodong:
				message_Check = 2;
				pageOn = 1  ;
				rows_List.clear();
				getMessage();
				// showPopupWindow(findViewById(R.id.view_line));
				break;
			// 消息列表
			case R.id.rb_allmessage_jiaoyi:
				message_Check = 3;
				pageOn = 1  ;
				rows_List.clear();
				getMessage();
				// showPopupWindow(findViewById(R.id.view_line));
				break;
			// 编辑
			case R.id.title_righttextview:
				switch (i % 2) {
					case 0:
						isMulChoice = true;
						selectid.clear();
						title_righttextview.setText("取消");
						// layout.setVisibility(View.VISIBLE);
						for (int i = 0; i < rows_List.size(); i++) {
							adapter_list.visiblecheck.put(i, CheckBox.VISIBLE);
						}
						lin_edit.setVisibility(View.VISIBLE);
						adapter_list = new Adapter_list(MessageCenterAct.this, false);
						lv_message.setAdapter(adapter_list);
						i++;
						break;
					case 1:
						isMulChoice = false;
						selectid.clear();
						title_righttextview.setText("编辑");
						lin_edit.setVisibility(View.GONE);
						adapter_list = new Adapter_list(MessageCenterAct.this, false);
						lv_message.setAdapter(adapter_list);
						i++;
						break;

					default:
						break;
				}
				break;

			default:
				break;
		}
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}
	public void loadComplete() {
		isLoading = false;
		footerlayout.setVisibility(View.GONE);
	}
	// 消息的下拉框
	private LinearLayout layout;
	private ListView lv_dialog_message;
	private PopupWindow popupWindow;
	private int message_Check = 3;

	public void showPopupWindow(View parent) {
		// 加载布局
		layout = (LinearLayout) LayoutInflater.from(MessageCenterAct.this)
				.inflate(R.layout.dialog_message, null);
		// 找到布局的控件
		lv_dialog_message = (ListView) layout
				.findViewById(R.id.lv_dialog_message);
		// 设置适配器
		lv_dialog_message.setAdapter(new adapter());
		// 实例化popupWindow
		popupWindow = new PopupWindow(layout, LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT, true);
		// 控制键盘是否可以获得焦点
		popupWindow.setFocusable(true);
		// 设置popupWindow弹出窗体的背景
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		@SuppressWarnings("deprecation")
		// 获取xoff
				int xpos = manager.getDefaultDisplay().getWidth() / 2
				- popupWindow.getWidth() / 2;
		// xoff,yoff基于anchor的左下角进行偏移。
		// 监听
		lv_dialog_message.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				// 关闭popupWindow
				message_Check = arg2;
				rb_allmessage_jiaoyi.setText(list_message_type.get(arg2));
				rows_List.clear();
				getMessage();
				popupWindow.dismiss();
				popupWindow = null;
			}
		});
		layout.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				popupWindow.dismiss();
				return true;
			}
		});
		popupWindow.showAsDropDown(parent, xpos, 0);
	}

	// 消息 类型的是配器
	class adapter extends BaseAdapter {
		@Override
		public int getCount() {
			return list_message_type.size();
		}

		@Override
		public Object getItem(int position) {
			return list_message_type.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(MessageCenterAct.this,
						R.layout.messagetype_text, null);
				TextView tv = (TextView) convertView.findViewById(R.id.tv_text);
				LinearLayout lin_text = (LinearLayout) convertView
						.findViewById(R.id.lin_text);
				tv.setText(list_message_type.get(position));
				if (position == message_Check) {
					lin_text.setBackgroundColor(Color.parseColor("#7F7F7F"));
				}
			}
			return convertView;
		}



	}

	// 消息列表适配器
	private boolean isMulChoice = false; // 是否多选

	class Adapter_list extends BaseAdapter {
		private LayoutInflater inflater = null;
		private HashMap<Integer, View> mView;
		public HashMap<Integer, Integer> visiblecheck;// 用来记录是否显示checkBox
		public HashMap<Integer, Boolean> ischeck;
		private Context context;
		private Boolean allchoose;
		private List<Rows_Message> lsct;
		ViewHolder vh = null;
		public Adapter_list(Context context, Boolean allchoose ) {
			super();
			this.context = context;
			this.allchoose = allchoose;
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mView = new HashMap<Integer, View>();
			visiblecheck = new HashMap<Integer, Integer>();
			ischeck = new HashMap<Integer, Boolean>();
			if (isMulChoice) {
				for (int i = 0; i < rows_List.size(); i++) {
					ischeck.put(i, false);
					visiblecheck.put(i, CheckBox.VISIBLE);
				}
			} else {
				for (int i = 0; i < rows_List.size(); i++) {
					ischeck.put(i, false);
					visiblecheck.put(i, CheckBox.GONE);
				}
			}
		}
		public void onDateChange(List<Rows_Message> lsct) {
			this.lsct = lsct;
			this.notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return rows_List.size();
		}

		@Override
		public Object getItem(int position) {
			return rows_List.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView( int position, View convertView,
							 ViewGroup parent) {
			if (convertView == null) {
				vh = new ViewHolder();
				convertView = inflater.inflate(R.layout.message_listcheck, null);
				// TextView tv_message_detail = (TextView)
				// view.findViewById(R.id.tv_message_detail);
				vh.tv_message_detail = (TextView) convertView
						.findViewById(R.id.tv_message_detail);
				vh.tv_message_time = (TextView) convertView
						.findViewById(R.id.tv_message_time);
				vh.image_message_type = (ImageView) convertView
						.findViewById(R.id.image_message_type);
				vh.ceb = (CheckBox) convertView.findViewById(R.id.check);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			vh.tv_message_detail.setText(rows_List.get(position).getContent());
//				tv_message_detail.setText("啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊");
			vh.tv_message_time.setText(stringCut.getDateTimeToStringheng(Long
					.parseLong(rows_List.get(position).getAddTime())));
			if ("1".equals(rows_List.get(position).getIsRead())) {
				switch (Integer.parseInt(rows_List.get(position).getType())) {
					case 1:
						vh.image_message_type
								.setImageResource(R.mipmap.icon_system);
						break;
					case 2:
						vh.image_message_type
								.setImageResource(R.mipmap.icon_event);
						break;
					case 3:
						vh.image_message_type
								.setImageResource(R.mipmap.icon_transaction);
						break;
					default:
						break;
				}

			} else if ("0".equals(rows_List.get(position).getIsRead())) {
				switch (Integer.parseInt(rows_List.get(position).getType())) {
					case 1:
						vh.image_message_type
								.setImageResource(R.mipmap.icon_system_o);
						break;
					case 2:
						vh.image_message_type
								.setImageResource(R.mipmap.icon_event_o);
						break;
					case 3:
						vh.image_message_type
								.setImageResource(R.mipmap.icon_transaction_o);
						break;
					default:
						break;
				}
			}
			// tv_message_detail.setText(list_messag.get(position));

//				vh.ceb.setChecked(ischeck.get(position));
//				vh.ceb.setVisibility(visiblecheck.get(position));
			vh.ceb.setVisibility(View.GONE);

			if (allchoose) {
				for (int i = 0; i < rows_List.size(); i++) {
					selectid.add(rows_List.get(position));
					vh.ceb.setChecked(true);
				}
			} else {
				for (int i = 0; i < rows_List.size(); i++) {
					selectid.remove(rows_List.get(position));
					vh.ceb.setChecked(false);
				}
			}

//				if (isMulChoice) {
//					convertView.setOnClickListener(new OnClickListener() {
//						Boolean flag = true;
//
//						public void onClick(View v) {
//							// TODO Auto-generated method stub
//							if (isMulChoice) {
//								if (vh.ceb.isChecked()) {
//									vh.ceb.setChecked(false);
//									selectid.remove(rows_List.get(position));
//									System.out.println(selectid.size());
//								} else {
//									vh.ceb.setChecked(true);
//									selectid.add(rows_List.get(position));
//								}
//								if (selectid.size() > 0) {
//									tv_alldelete.setTextColor(Color
//											.parseColor("#FE7634"));
//								} else {
//									tv_alldelete.setTextColor(Color
//											.parseColor("#D2D2D2"));
//								}
//
//								// action_aaa.setText("共选择了" + title_lv.size() +
//								// "项");
//							} else {
//								if (flag) {
//									flag = false;
//									vh.tv_message_detail.setEllipsize(null); // 展开
//									vh.tv_message_detail.setSingleLine(flag);
//								} else {
//									flag = true;
//									vh.tv_message_detail.setLines(2);
//									vh.tv_message_detail
//											.setEllipsize(TextUtils.TruncateAt
//													.valueOf("END")); // 收缩
//								}
//							}
//						}
//					});
//				}

			return convertView;
		}

	}

	private class ViewHolder {
		private TextView tv_message_detail;
		private TextView tv_message_time;
		private ImageView image_message_type;
		private CheckBox ceb;
	}
	private int pageOn = 1 ;
	// 获取消息列表
	private void getMessage() {
		showWaitDialog("加载中...", true, "");
		OkHttpUtils.post().url(UrlConfig.GETMESSAGE).addParams("uid", uid)
				.addParams("type", String.valueOf(message_Check))
				.addParams("pageOn", pageOn+"").addParams("pageSize", "10")
				.addParams("version", UrlConfig.version)
				.addParams("channel", "2").build()
				.execute(new StringCallback() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						dismissDialog();
						ptr_invest.refreshComplete();
						JSONObject obj = JSON.parseObject(response);

						if (obj.getBoolean(("success"))) {
							JSONObject map = obj.getJSONObject("map");
							JSONArray arr = map.getJSONObject("page")
									.getJSONArray("rows");
							if (arr.size() > 0) {
								loadComplete();
							} else {
								tv_footer.setText("全部加载完毕");
								footerlayout.setVisibility(View.VISIBLE);
								pb.setVisibility(View.GONE);
							}
							if (pageOn == 1) {
								rows_List.clear();
							}
							if(arr.size() <= 0){
								if (pageOn == 1) {
									rl_no_nomessage.setVisibility(View.VISIBLE) ;
									lv_message.setVisibility(View.GONE) ;
								}
							}else{
								mrows_List.clear();
								rl_no_nomessage.setVisibility(View.GONE) ;
								lv_message.setVisibility(View.VISIBLE) ;
								mrows_List = JSON.parseArray(arr.toJSONString(),
										Rows_Message.class);
								rows_List.addAll(mrows_List);
								if (adapter_list == null) {
									adapter_list = new Adapter_list(
											MessageCenterAct.this, false);
									lv_message.setAdapter(adapter_list);
								}else{
									adapter_list.onDateChange(rows_List);
								}
							}

							pageOn++;
						} else if ("9999".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("系统异常");
						} else if ("9998".equals(obj.getString("errorCode"))) {
							new show_Dialog_IsLogin(MessageCenterAct.this).show_Is_Login() ;
						}  else {
							ToastMaker.showShortToast("系统异常");
						}
					}

					@Override
					public void onError(Call call, Exception e) {
						// TODO Auto-generated method stub
						dismissDialog();
						ToastMaker.showShortToast("请检查网络");
					}
				});
	}

	// 消息标记为已读
	private void updateUnReadMsg() {
		showWaitDialog("加载中...", false, "");
		OkHttpUtils.post().url(UrlConfig.UPDATEUNREADMSG).addParams("uid", uid)
				.addParams("ids", stringCut.listToString(list_message_ID))
				.addParams("version", UrlConfig.version)
				.addParams("channel", "2").build()
				.execute(new StringCallback() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						dismissDialog();
						JSONObject obj = JSON.parseObject(response);
						if (obj.getBoolean(("success"))) {
							ToastMaker.showShortToast("消息标记为已读");

						} else if ("9999".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("系统异常");
						} else if ("9998".equals(obj.getString("errorCode"))) {
							new show_Dialog_IsLogin(MessageCenterAct.this).show_Is_Login() ;
						}  else {
							ToastMaker.showShortToast("系统异常");
						}
					}

					@Override
					public void onError(Call call, Exception e) {
						ToastMaker.showShortToast("请检查网络");
						// TODO Auto-generated method stub
						e.printStackTrace();
					}
				});
	}

	// 消息删除消息
	private void delMsg() {
		showWaitDialog("加载中...", false, "");
		OkHttpUtils.post().url(UrlConfig.DELMSG).addParams("uid", uid)
				.addParams("ids", stringCut.listToString(list_message_ID))
				.addParams("version", UrlConfig.version)
				.addParams("channel", "2").build()
				.execute(new StringCallback() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						dismissDialog();
						JSONObject obj = JSON.parseObject(response);
						if (obj.getBoolean(("success"))) {
							ToastMaker.showShortToast("消息删除成功");
						} else if ("1001".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("删除的消息ID为空");
						}  else if ("9999".equals(obj.getString("errorCode"))) {
							ToastMaker.showShortToast("系统异常");
						} else if ("9998".equals(obj.getString("errorCode"))) {
							new show_Dialog_IsLogin(MessageCenterAct.this).show_Is_Login() ;
						}  else {
							ToastMaker.showShortToast("系统异常");
						}
					}

					@Override
					public void onError(Call call, Exception e) {
						// TODO Auto-generated method stub
						dismissDialog();
						ToastMaker.showShortToast("请检查网络");
						e.printStackTrace();
					}
				});
	}
}
