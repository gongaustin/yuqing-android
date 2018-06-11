package com.app.yuqing.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.app.yuqing.R;
import com.app.yuqing.activity.UserDetailActivity;
import com.app.yuqing.adapter.ContactAdapter;
import com.app.yuqing.bean.ContactUser;
import com.app.yuqing.bean.TreeDatabean;
import com.app.yuqing.bean.UserBean;
import com.app.yuqing.net.Event;
import com.app.yuqing.net.EventCode;
import com.app.yuqing.net.bean.ContactUserResponsebean;
import com.app.yuqing.net.bean.QueryUserResponseBean;
import com.app.yuqing.net.bean.TreeDataResponseBean;
import com.app.yuqing.utils.CommonUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

public class ContactFragment extends BaseFragment {

	private TextView tvTitle;
	private ListView lvContact;
	private ContactAdapter adapter;
	
	private List<UserBean> dataList = new ArrayList<UserBean>();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_new_contact);
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		initView();
		initListener();
		getData();
		super.onActivityCreated(savedInstanceState);
	}
	
	private void initView() {
		tvTitle = (TextView) contentView.findViewById(R.id.tv_title);
		lvContact = (ListView) contentView.findViewById(R.id.lv_data);
		
		adapter = new ContactAdapter(getActivity(), dataList);
		lvContact.setAdapter(adapter);
		
		tvTitle.setText("联系人");
	}
	
	private void initListener() {
		lvContact.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(),UserDetailActivity.class);
				intent.putExtra(UserDetailActivity.KEY_ID, dataList.get(position).getUserId());
				getActivity().startActivity(intent);
			}
		});
	}
	
	private void getData() {
		pushEventNoProgress(EventCode.HTTP_QUERYUSER);
	}
	
	@Override
	public void onEventRunEnd(Event event) {
		// TODO Auto-generated method stub
		super.onEventRunEnd(event);
		if (event.getEventCode() == EventCode.HTTP_QUERYUSER) {
			if (event.isSuccess()) {
				QueryUserResponseBean bean = (QueryUserResponseBean) event.getReturnParamAtIndex(0);
				if (bean != null && bean.getData() != null && bean.getData().size() > 2) {
					dataList.clear();
					dataList.addAll(bean.getData());
					adapter.notifyDataSetChanged();

					for(UserBean userBean : bean.getData()) {
						Uri headUri = Uri.parse(userBean.getAvatar());
						UserInfo userInfo = new UserInfo(userBean.getUserId(), userBean.getRealname(),headUri);
						RongIM.getInstance().setCurrentUserInfo(userInfo);
					}
				}
			} else {
				CommonUtils.showToast(event.getFailMessage());
			}
		}
	}
	
}
