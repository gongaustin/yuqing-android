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
import com.app.yuqing.net.bean.GetGroupsResponseBean;
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
					RongIM.getInstance().startGroupChat(GropListActivity.this, bean.getGroupId(), bean.getGroupName());
				}
			}
		});
	}
	
	private void initData() {
		pushEvent(EventCode.HTTP_GETCREATEDGROUPS);
	}
	
	@Override
	public void onEventRunEnd(Event event) {
		// TODO Auto-generated method stub
		super.onEventRunEnd(event);

		if (event.getEventCode() == EventCode.HTTP_GETCREATEDGROUPS) {
			if (event.isSuccess()) {
				GetGroupsResponseBean bean = (GetGroupsResponseBean) event.getReturnParamAtIndex(0);
				if (bean != null && bean.getData() != null && bean.getData().size() > 0) {
					adapter.updateData(bean.getData());
					for (GroupBean groupBean : bean.getData()) {
						PreManager.putString(getApplicationContext(),groupBean.getGroupId(),groupBean.getGroupName());
					}
				}
			} else {
				CommonUtils.showToast(event.getFailMessage());
			}
		}
	}
}
