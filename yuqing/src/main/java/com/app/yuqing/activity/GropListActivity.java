package com.app.yuqing.activity;

import io.rong.imkit.RongIM;

import java.util.ArrayList;
import java.util.List;

import com.app.yuqing.AppContext;
import com.app.yuqing.R;
import com.app.yuqing.adapter.GroupAdapter;
import com.app.yuqing.bean.GroupBean;
import com.app.yuqing.net.Event;
import com.app.yuqing.net.EventCode;
import com.app.yuqing.net.bean.GroupListResponseBean;
import com.app.yuqing.net.bean.UserResponseBean;
import com.app.yuqing.utils.CommonUtils;
import com.app.yuqing.utils.PreManager;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class GropListActivity extends BaseActivity {

	@ViewInject(R.id.lv_group)
	private ListView lvGroup;
	
	private GroupAdapter adapter;
	private List<GroupBean> dataList = new ArrayList<GroupBean>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
		initListener();
		initData();
	}
	
	private void initView() {
		addBack(R.id.rl_back);
		setTitle("群列表");
		
		adapter = new GroupAdapter(GropListActivity.this, dataList);
		lvGroup.setAdapter(adapter);
	}
	
	private void initListener() {
		lvGroup.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				GroupBean bean = dataList.get(position);
				if (bean != null && !TextUtils.isEmpty(bean.getGroupId())) {
					RongIM.getInstance().startGroupChat(GropListActivity.this, bean.getGroupId(), bean.getGroupId());					
				}
			}
		});
	}
	
	private void initData() {
		UserResponseBean bean = PreManager.get(getApplicationContext(), AppContext.KEY_LOGINUSER, UserResponseBean.class);
		if (bean != null && bean.getUser() != null && !TextUtils.isEmpty(bean.getUser().getId())) {
			pushEvent(EventCode.HTTP_QUERYGROUP, bean.getUser().getId());			
		}
	}
	
	@Override
	public void onEventRunEnd(Event event) {
		// TODO Auto-generated method stub
		super.onEventRunEnd(event);
		if (event.getEventCode() == EventCode.HTTP_QUERYGROUP) {
			if (event.isSuccess()) {
				GroupListResponseBean bean = (GroupListResponseBean) event.getReturnParamAtIndex(0);
				if (bean != null && bean.getGroups() != null) {
					adapter.updateData(bean.getGroups());
				}
			} else {
				CommonUtils.showToast(event.getFailMessage());
			}
		}
	}
}
