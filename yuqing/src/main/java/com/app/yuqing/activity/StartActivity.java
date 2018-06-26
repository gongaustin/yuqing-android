package com.app.yuqing.activity;

import io.rong.imkit.RongIM;
import io.rong.imkit.RongIM.GroupInfoProvider;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;

import com.app.yuqing.AppContext;
import com.app.yuqing.net.Event;
import com.app.yuqing.net.EventCode;
import com.app.yuqing.net.bean.BaseResponseBean;
import com.app.yuqing.net.bean.PersonalInfoResponseBean;
import com.app.yuqing.net.bean.TokenBean;
import com.app.yuqing.net.bean.UserResponseBean;
import com.app.yuqing.utils.CommonUtils;
import com.app.yuqing.utils.PreManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

public class StartActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		 getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
	                WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		super.onCreate(savedInstanceState);
		if (Build.VERSION.SDK_INT>=23){
			showContacts();
		}else{
			init();
		}

	}

	private void init() {
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				String account = PreManager.getString(getApplicationContext(), AppContext.USER_ACCOUNT);
				String password = PreManager.getString(getApplicationContext(), AppContext.USER_KEY);
				if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
					Intent intent = new Intent(StartActivity.this,LoginActivity.class);
					startActivity(intent);
					finish();
				} else {
					pushEventNoProgress(EventCode.HTTP_LOGIN, account,password);
				}

			}
		}, 3000);
	}
	
	@Override
	public void onEventRunEnd(Event event) {
		// TODO Auto-generated method stub
		super.onEventRunEnd(event);
		if (event.getEventCode() == EventCode.HTTP_LOGIN) {
			if (event.isSuccess()) {
				UserResponseBean bean = (UserResponseBean) event.getReturnParamAtIndex(0);
				if (bean != null) {
					PreManager.putString(getApplication(),AppContext.KEY_TOKEN,bean.getData());
					pushEventNoProgress(EventCode.HTTP_GETTOKEN);
				}
			} else {
				Intent intent = new Intent(StartActivity.this,LoginActivity.class);
				startActivity(intent);
				finish();
			}
		}

		if (event.getEventCode() == EventCode.HTTP_GETTOKEN) {
			if (event.isSuccess()) {
				UserResponseBean bean = (UserResponseBean) event.getReturnParamAtIndex(0);
				if (bean != null) {
					pushEventNoProgress(EventCode.HTTP_PERSONALINFO);
					if (!TextUtils.isEmpty(bean.getData())) {
						connect(bean.getData());
					}
				}
			} else {
				Intent intent = new Intent(StartActivity.this,LoginActivity.class);
				startActivity(intent);
				finish();
			}
		}

		if (event.getEventCode() == EventCode.HTTP_PERSONALINFO) {
			if (event.isSuccess()) {
				PersonalInfoResponseBean bean = (PersonalInfoResponseBean) event.getReturnParamAtIndex(0);
				if (bean != null && bean.getData() != null) {
					PreManager.put(getApplicationContext(),AppContext.KEY_LOGINUSER,bean.getData());

					Uri headUri = Uri.parse(bean.getData().getAvatar());
					UserInfo userInfo = new UserInfo(bean.getData().getUserId(), bean.getData().getRealname(),headUri);
					RongIM.getInstance().setCurrentUserInfo(userInfo);
					RongIM.setGroupInfoProvider(new GroupInfoProvider() {

						@Override
						public Group getGroupInfo(String id) {
							// TODO Auto-generated method stub
							String name = PreManager.getString(getApplicationContext(), id);
							Group mGroup = new Group(id, name, null);
							return mGroup;
						}
					}, false);
				}
			} else {
				CommonUtils.showToast(event.getFailMessage());
			}
		}
	}
	
	/**
	 * <p>连接服务器，在整个应用程序全局，只需要调用一次，需在 {@link #init(Context)} 之后调用。</p>
	 * <p>如果调用此接口遇到连接失败，SDK 会自动启动重连机制进行最多10次重连，分别是1, 2, 4, 8, 16, 32, 64, 128, 256, 512秒后。
	 * 在这之后如果仍没有连接成功，还会在当检测到设备网络状态变化时再次进行重连。</p>
	 *
	 * @param token    从服务端获取的用户身份令牌（Token）。
	 * @param callback 连接回调。
	 * @return RongIM  客户端核心类的实例。
	 */
	private void connect(String token) {

	    if (getApplicationInfo().packageName.equals(CommonUtils.getCurProcessName(getApplicationContext()))) {

			showXProgressDialog();
	        RongIM.connect(token, new RongIMClient.ConnectCallback() {

	            /**
	             * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
	             *                  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
	             */
	            @Override
	            public void onTokenIncorrect() {
					dismissXProgressDialog();
					Intent intent = new Intent(StartActivity.this,LoginActivity.class);
					startActivity(intent);
					finish();
	            }

	            /**
	             * 连接融云成功
	             * @param userid 当前 token 对应的用户 id
	             */
	            @Override
	            public void onSuccess(String userid) {
	                Log.d("LoginActivity", "--onSuccess" + userid);
					dismissXProgressDialog();
					CommonUtils.showToast("登录成功");
	                startActivity(new Intent(StartActivity.this, MainActivity.class));
	                finish();
	            }

	            /**
	             * 连接融云失败
	             * @param errorCode 错误码，可到官网 查看错误码对应的注释
	             */
	            @Override
	            public void onError(RongIMClient.ErrorCode errorCode) {
					dismissXProgressDialog();
					Intent intent = new Intent(StartActivity.this,LoginActivity.class);
					startActivity(intent);
					finish();
	            }
	        });
	    }
	}

	private static final int BAIDU_READ_PHONE_STATE =100;

	public void showContacts(){
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
				!= PackageManager.PERMISSION_GRANTED
				|| ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
				!= PackageManager.PERMISSION_GRANTED
				|| ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
				!= PackageManager.PERMISSION_GRANTED) {
			Toast.makeText(getApplicationContext(),"没有权限,请手动开启定位权限",Toast.LENGTH_SHORT).show();
			// 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
			ActivityCompat.requestPermissions(StartActivity.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
																				Manifest.permission.ACCESS_FINE_LOCATION,
																				Manifest.permission.READ_PHONE_STATE},
					BAIDU_READ_PHONE_STATE);
		}else{
			init();
		}
	}

	//Android6.0申请权限的回调方法
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode) {
			// requestCode即所声明的权限获取码，在checkSelfPermission时传入
			case BAIDU_READ_PHONE_STATE:
				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					// 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
					init();
				} else {
					// 没有获取到权限，做特殊处理
					Toast.makeText(getApplicationContext(), "获取位置权限失败，请手动开启", Toast.LENGTH_SHORT).show();
				}
				break;
			default:
				break;
		}
	}
}
