package com.app.yuqing.activity;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

import java.io.File;
import java.util.ArrayList;

import com.app.yuqing.AppContext;
import com.app.yuqing.MyApplication;
import com.app.yuqing.R;
import com.app.yuqing.bean.GroupBean;
import com.app.yuqing.bean.PersonalBean;
import com.app.yuqing.fragment.BaseFragment;
import com.app.yuqing.fragment.ContactFragment;
import com.app.yuqing.fragment.MeFragment;
import com.app.yuqing.fragment.MessageFragment;
import com.app.yuqing.fragment.WorkFragment;
import com.app.yuqing.fragment.WorkNewFragment;
import com.app.yuqing.net.Event;
import com.app.yuqing.net.EventCode;
import com.app.yuqing.net.bean.GroupListResponseBean;
import com.app.yuqing.net.bean.VersionResponseBean;
import com.app.yuqing.utils.CommonUtils;
import com.app.yuqing.utils.PreManager;
import com.app.yuqing.view.BaseDialog.DialogListener;
import com.app.yuqing.view.VersionUpdateDialog;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;

public class MainActivity extends BaseActivity {

	@ViewInject(R.id.message_icon)
	private RadioButton rbMessage;
	@ViewInject(R.id.work_icon)
	private RadioButton rbWork;
	@ViewInject(R.id.contact_icon)
	private RadioButton rbContact;
	@ViewInject(R.id.me_icon)
	private RadioButton rbMe;
	@ViewInject(R.id.tv_msg1)
	private TextView tvMsg;
	
	@ViewInject(R.id.container)
	private FrameLayout container;
	
	private FragmentManager fragmentManager;
	
	private MessageFragment messageFragment;
//	private WorkFragment workFragment;
	private WorkNewFragment workNewFragment;
	private ContactFragment contactFragment;
	private MeFragment meFragment;
	
	private ArrayList<BaseFragment> fragments = new ArrayList<BaseFragment>();
	
	private VersionUpdateDialog versionUpdateDialog;
	public static final String KEY_INDEX = "key_mainActivity_index";
	public static final String INDEX_WORK = "value_mainActivity_work";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initFragment();
		initView();
		getData();
		RongIM.setConnectionStatusListener(new RongIMClient.ConnectionStatusListener() {
			@Override
			public void onChanged(ConnectionStatus connectionStatus) {
				if (connectionStatus == ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT) {
					CommonUtils.showToast("被其它设备登录");
					Intent intent = new Intent(MainActivity.this,LoginActivity.class);
					startActivity(intent);
					MyApplication.instance.exit();
				}
			}
		});
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		String index = intent.getStringExtra(KEY_INDEX);
		if (!TextUtils.isEmpty(index) && index.equals(INDEX_WORK)) {
			rbWork.performClick();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		int count = RongIM.getInstance().getTotalUnreadCount();
		if (count > 0) {
			tvMsg.setVisibility(View.VISIBLE);
			tvMsg.setText(count + "");
		} else {
			tvMsg.setVisibility(View.GONE);
		}
	}
	
	private void initFragment() {
		fragmentManager = getSupportFragmentManager();
		
		messageFragment = new MessageFragment();
//		workFragment = new WorkFragment();
		workNewFragment = new WorkNewFragment();

		Bundle bundle = new Bundle();
		bundle.putString(WorkNewFragment.KEY_URL,AppContext.APP_URL);
		workNewFragment.setArguments(bundle);

		contactFragment = new ContactFragment();
		meFragment = new MeFragment();
		fragments.add(messageFragment);
		fragments.add(workNewFragment);
		fragments.add(contactFragment);
		fragments.add(meFragment);
		
		FragmentTransaction ft = fragmentManager.beginTransaction();
        for(BaseFragment fragment : fragments) {
            ft.add(R.id.container, fragment);
        }
        ft.commit();
	}
	
	private void initView() {
		rbMessage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pageChanged(0);
			}
		});
		
		rbWork.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pageChanged(1);
			}
		});
		
		rbContact.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pageChanged(2);
			}
		});
		
		rbMe.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pageChanged(3);
			}
		});		
		
		rbMessage.performClick();
	}	
	
	private void pageChanged(int index) {
		showFragment(index);
		switch (index)
		{
	        case 0:
	        	rbMessage.setSelected(true);
	        	rbWork.setSelected(false);
	        	rbContact.setSelected(false);
	        	rbMe.setSelected(false);
	            break;
	        case 1:
	        	rbMessage.setSelected(false);
	        	rbWork.setSelected(true);
	        	rbContact.setSelected(false);
	        	rbMe.setSelected(false);
	            break;
	        case 2:
	        	rbMessage.setSelected(false);
	        	rbWork.setSelected(false);
	        	rbContact.setSelected(true);
	        	rbMe.setSelected(false);
	            break;
	        case 3:
	        	rbMessage.setSelected(false);
	        	rbWork.setSelected(false);
	        	rbContact.setSelected(false);
	        	rbMe.setSelected(true);
	            break;	 
        }
	}
	
    public void showFragment(int position) {
        hideFragment();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.show(fragments.get(position));
        ft.commit();
    }
    
    public void hideFragment() {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        for(BaseFragment fragment : fragments) {
            if(fragment != null)
                ft.hide(fragment);
        }
        ft.commit();
    }
    
    public void getData() {
    	pushEventNoProgress(EventCode.HTTP_QUERYUPGRAGE,AppContext.APPID,CommonUtils.getVersion());
    }
    
    @Override
    public void onEventRunEnd(Event event) {
    	// TODO Auto-generated method stub
    	super.onEventRunEnd(event);
    	if (event.getEventCode() == EventCode.HTTP_QUERYUPGRAGE) {
			if (event.isSuccess()) {
				final VersionResponseBean bean = (VersionResponseBean) event.getReturnParamAtIndex(0);
				if (bean != null && bean.getData() != null) {
					if (!CommonUtils.checkVersionIsSame(bean.getData().getNewVersion())) {
						meFragment.tvVersion.setText("有新版本");
                        meFragment.tvVersion.setTextColor(Color.BLUE);
						if (versionUpdateDialog == null) {
							versionUpdateDialog = new VersionUpdateDialog(MainActivity.this);
						}
						versionUpdateDialog.setData(bean.getData().getUpdateLog());
						versionUpdateDialog.setListener(new DialogListener() {
							
							@Override
							public void update(Object object) {
								if ("true".equals((String)object)) {
//									Intent intent = new Intent(Intent.ACTION_VIEW);
//									intent.setData(Uri.parse(bean.getData().getDownloadUrl()));
//									startActivity(intent);
									downLoadAPK(MainActivity.this,bean.getData().getDownloadUrl());
								}
							}
						});
						versionUpdateDialog.show();
					} else {
						meFragment.tvVersion.setText("已经是最新版本");
                        meFragment.tvVersion.setTextColor(Color.BLACK);
					}
				}
			} else {
				CommonUtils.showToast(event.getFailMessage());
			}
		}
    	
		if (event.getEventCode() == EventCode.HTTP_QUERYGROUP) {
			if (event.isSuccess()) {
				GroupListResponseBean bean = (GroupListResponseBean) event.getReturnParamAtIndex(0);
				if (bean != null && bean.getGroups() != null) {
					StringBuffer sbf = new StringBuffer();
					for(GroupBean gb : bean.getGroups()) {
						PreManager.putString(getApplicationContext(), gb.getGroupId(), gb.getGroupName());
						sbf.append(gb.getGroupId()).append(",");
					}
					String groupId = sbf.toString();
					if (groupId.endsWith(",")) {
						groupId = groupId.substring(0, groupId.length() - 1);
					}
//					if (loginBean != null && loginBean.getUser() != null && !TextUtils.isEmpty(loginBean.getUser().getId())) {
//						pushEvent(EventCode.HTTP_SYNC, groupId,loginBean.getUser().getId());
//					}
				}
			} else {
				CommonUtils.showToast(event.getFailMessage());
			}
		}
		
		if (event.getEventCode() == EventCode.HTTP_SYNC) {
			if (event.isSuccess()) {
				CommonUtils.showToast("刷新成功");
			} else {
				CommonUtils.showToast(event.getFailMessage());
			}
		}
    }
    
	private Long backCode = 0l;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if((keyCode == KeyEvent.KEYCODE_BACK)){
			if((System.currentTimeMillis() - backCode < 2000)){
				exitProgram();
			}else{
				backCode = System.currentTimeMillis();
				CommonUtils.showToast("再点一次退出程序");
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void exitProgram(){
		Intent intent=new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		this.startActivity(intent);
	}

	/**
	 * 下载新版本
	 *
	 * @param context
	 * @param url
	 */
	public void downLoadAPK(Context context, String url) {

		if (TextUtils.isEmpty(url)) {
			return;
		}

		try {
			String serviceString = Context.DOWNLOAD_SERVICE;
			DownloadManager downloadManager = (DownloadManager) context.getSystemService(serviceString);

			Uri uri = Uri.parse(url);
			DownloadManager.Request request = new DownloadManager.Request(uri);
			request.allowScanningByMediaScanner();
			request.setVisibleInDownloadsUi(true);
			request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
			request.setMimeType("application/vnd.android.package-archive");

			File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/yuqing/","yuqing.apk");
			if (file.exists()){
				file.delete();
			} else if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			request.setDestinationInExternalPublicDir(Environment.getExternalStorageDirectory().getAbsolutePath()+"/yuqing/", "yuqing.apk");
			long refernece = downloadManager.enqueue(request);
			PreManager.put(MainActivity.this.getApplicationContext(),AppContext.KEY_REFERNECE,refernece);
		} catch (Exception exception) {
			CommonUtils.showToast("更新失败");
		}

	}
}
