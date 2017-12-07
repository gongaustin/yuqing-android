package com.app.yuqing.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.app.yuqing.AppContext;
import com.app.yuqing.R;
import com.app.yuqing.adapter.ContactAdapter;
import com.app.yuqing.adapter.CreateGroupAdapter;
import com.app.yuqing.bean.ContactUser;
import com.app.yuqing.bean.TreeDatabean;
import com.app.yuqing.bean.UserBean;
import com.app.yuqing.net.Event;
import com.app.yuqing.net.EventCode;
import com.app.yuqing.net.bean.ContactUserResponsebean;
import com.app.yuqing.net.bean.TreeDataResponseBean;
import com.app.yuqing.net.bean.UserResponseBean;
import com.app.yuqing.utils.CommonUtils;
import com.app.yuqing.utils.PreManager;
import com.app.yuqing.view.ChangeGroupNameDialog;
import com.app.yuqing.view.BaseDialog.DialogListener;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class CreateGroupActivity extends BaseActivity {

	@ViewInject(R.id.ll_tab)
	private LinearLayout llTab;
	private ListView lvContact;
	private CreateGroupAdapter adapter;
	private List<ContactUser> userList = new ArrayList<ContactUser>();
	private List<ContactUser> cacheList = new ArrayList<ContactUser>();
	
	private List<TreeDatabean> dataList = new ArrayList<TreeDatabean>();
	
	public static final String TYPE_ADDUSER = "addUser";
	public static final String KEY_ADDUSER = "key_CreateGroupActivity_addUser";
	private String type;
	
	private ChangeGroupNameDialog changeGroupNameDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		type = getIntent().getStringExtra(KEY_ADDUSER);
		initView();
		initListener();
		getData();
	}
	
	private void initView() {
		addBack(R.id.rl_back);
		if (TYPE_ADDUSER.equals(type)) {
			setTitle("选择群成员");
		} else {
			setTitle("选择群成员");			
		}

		lvContact = (ListView) findViewById(R.id.lv_data);
		
		adapter = new CreateGroupAdapter(CreateGroupActivity.this, userList);
		lvContact.setAdapter(adapter);
		
	}
	
	private void initListener() {
		setRightTextClickListener("确定", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (cacheList.size() > 0) {
					if (TYPE_ADDUSER.equals(type)) {
						StringBuffer sbID = new StringBuffer();
						for(ContactUser cu : cacheList) {
							sbID.append(cu.getId()).append(",");
						}
						String userIds = sbID.toString();
						if (userIds.endsWith(",")) {
							userIds = userIds.substring(0, userIds.length() - 1);
						}
						Intent data = new Intent();
						data.putExtra(GroupDetailActivity.KEY_ADDRESULT, userIds);
						setResult(Activity.RESULT_OK, data);
						finish();
					} else {
						if (changeGroupNameDialog == null) {
							changeGroupNameDialog = new ChangeGroupNameDialog(CreateGroupActivity.this);
						}
						changeGroupNameDialog.setListener(new DialogListener() {
							
							@Override
							public void update(Object object) {
								String name = (String) object;
								if (!TextUtils.isEmpty(name)) {
									String masterId = "";
									String userId = "";
									String groupId = CommonUtils.getAndroidId(getApplicationContext())+System.currentTimeMillis();
									
									UserResponseBean bean = PreManager.get(getApplicationContext(), AppContext.KEY_LOGINUSER, UserResponseBean.class);
									if (bean != null) {
										masterId = bean.getUser().getId();
									}
									StringBuffer sbID = new StringBuffer();
									for(ContactUser cu : cacheList) {
										sbID.append(cu.getId()).append(",");
									}
									sbID.append(masterId);
									userId = sbID.toString();
									
									if (TextUtils.isEmpty(masterId) || TextUtils.isEmpty(userId) || TextUtils.isEmpty(groupId) || TextUtils.isEmpty(name)) {
										CommonUtils.showToast("信息不完整");
										return;
									}
									
									pushEventBlock(EventCode.HTTP_CREATEGROUP, masterId,userId,groupId,name);
								}
							}
						});
						changeGroupNameDialog.show();
					}
				} else {
					CommonUtils.showToast("请选择群成员");
				}
			}
		});
		
		lvContact.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ContactUser user = userList.get(position);
				if (user.isSelected()) {
					user.setSelected(false);
					Iterator<ContactUser> itrator = cacheList.iterator();
					while(itrator.hasNext()) {
						ContactUser cu = itrator.next();
						if (user.getId().equals(cu.getId())) {
							itrator.remove();
						}
					}
				} else {
					user.setSelected(true);
					if (!cacheList.contains(user)) {
						cacheList.add(user);
					}
				}
				adapter.notifyDataSetChanged();
			}
		});
	}
	
	private void getData() {
		pushEventNoProgress(EventCode.HTTP_TREEDATA);
	}

	private void refreshView() {
		llTab.removeAllViews();
		int index = 0;
		for(TreeDatabean bean : dataList) {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
			params.weight = 1;
			params.gravity = Gravity.CENTER_VERTICAL;

			TextView tv = new TextView(llTab.getContext());
			tv.setTextSize(getResources().getDimension(R.dimen.textsize_mini));
			tv.setTextColor(getResources().getColor(R.color.color_worktext_gray));
			tv.setText(bean.getName());
			tv.setGravity(Gravity.CENTER);
			tv.setId(index);
			index++;

			final String id = bean.getId();
			tv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					refreshClick(v.getId());
					userList.clear();
					cacheList.clear();
					adapter.notifyDataSetChanged();
					pushEventNoProgress(EventCode.HTTP_QUERYUSERBYOFFICEID, id);
				}
			});
			llTab.addView(tv,params);
		}

		if (llTab.getChildCount() != 0 && llTab.getChildAt(0) instanceof TextView) {
			((TextView)llTab.getChildAt(0)).performClick();
		}

	}

	private void refreshClick(int id) {
		System.out.println("点击ID："+id);
		int index = -1;
		for(int i = 0 ; i<llTab.getChildCount() ; i++) {
			if (llTab.getChildAt(i) instanceof  TextView) {
				if (llTab.getChildAt(i).getId() == id) {
					index = i;
					((TextView)llTab.getChildAt(i)).setTextColor(getResources().getColor(R.color.color_banner));
				}
			}
		}

		if (index != -1) {
			for(int i = 0 ; i<llTab.getChildCount() ; i++) {
				if (llTab.getChildAt(i) instanceof  TextView) {
					if (i != index) {
						((TextView)llTab.getChildAt(i)).setTextColor(getResources().getColor(R.color.color_worktext_gray));
					}
				}
			}
		}
	}
	
	@Override
	public void onEventRunEnd(Event event) {
		// TODO Auto-generated method stub
		super.onEventRunEnd(event);
		if (event.getEventCode() == EventCode.HTTP_TREEDATA) {
			if (event.isSuccess()) {
				TreeDataResponseBean bean = (TreeDataResponseBean) event.getReturnParamAtIndex(0);
				if (bean != null && bean.getData() != null && bean.getData().size() > 2) {
					dataList.clear();
					dataList.addAll(bean.getData());
					refreshView();
				}
			} else {
				CommonUtils.showToast(event.getFailMessage());
			}
		}
		if (event.getEventCode() == EventCode.HTTP_QUERYUSERBYOFFICEID) {
			if (event.isSuccess()) {
				ContactUserResponsebean bean = (ContactUserResponsebean) event.getReturnParamAtIndex(0);
				if (bean != null && bean.getData() != null) {
					for(ContactUser user : bean.getData()) {
						for(ContactUser cacheUser : cacheList) {
							if(user.getId().equals(cacheUser.getId())) {
								user.setSelected(true);
							}
						}
					}
					adapter.updateData(bean.getData());
				}
			} else {
				CommonUtils.showToast(event.getFailMessage());
			}
		}
		if (event.getEventCode() == EventCode.HTTP_CREATEGROUP) {
			if (event.isSuccess()) {
				finish();
			} else {
				CommonUtils.showToast(event.getFailMessage());
			}
		}		
	}
}
