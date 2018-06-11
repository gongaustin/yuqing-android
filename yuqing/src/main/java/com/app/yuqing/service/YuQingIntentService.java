package com.app.yuqing.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;

import com.app.yuqing.AppContext;
import com.app.yuqing.MyApplication;
import com.app.yuqing.R;
import com.app.yuqing.activity.UrlWebClientActivity;
import com.app.yuqing.bean.MessageContentDataBean;
import com.app.yuqing.bean.MessageDataBean;
import com.app.yuqing.net.AndroidEventManager;
import com.app.yuqing.net.EventCode;
import com.app.yuqing.net.bean.UserResponseBean;
import com.app.yuqing.utils.CommonUtils;
import com.app.yuqing.utils.PreManager;
import com.google.gson.Gson;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTNotificationMessage;
import com.igexin.sdk.message.GTTransmitMessage;

public class YuQingIntentService extends GTIntentService{

	private Gson gson = new Gson();
	@Override
	public void onReceiveClientId(Context arg0, String arg1) {
		// TODO Auto-generated method stub
		System.out.println("onReceiveClientId:"+arg1);
		PreManager.saveClientId(arg0.getApplicationContext(),arg1);
		AndroidEventManager.getInstance().pushEvent(EventCode.HTTP_UPDATECLIENTID, arg1);
	}

	@Override
	public void onReceiveCommandResult(Context arg0, GTCmdMessage arg1) {
		// TODO Auto-generated method stub
		System.out.println("onReceiveCommandResult:"+arg1);
		CommonUtils.showToast("onReceiveCommandResult:"+arg1);
	}

	@Override
	public void onNotificationMessageArrived(Context context, GTNotificationMessage gtNotificationMessage) {
		System.out.println("onNotificationMessageArrived:"+gtNotificationMessage.getContent());
	}

	@Override
	public void onNotificationMessageClicked(Context context, GTNotificationMessage gtNotificationMessage) {
		System.out.println("onNotificationMessageClicked:"+gtNotificationMessage.getContent());
	}

	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
		// TODO Auto-generated method stub

		String appid = msg.getAppid();
		String taskid = msg.getTaskId();
		String messageid = msg.getMessageId();
		byte[] payload = msg.getPayload();
		String pkg = msg.getPkgName();
		String cid = msg.getClientId();

		// 第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
		boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
		Log.d(TAG, "call sendFeedbackMessage = " + (result ? "success" : "failed"));

		Log.d(TAG, "onReceiveMessageData -> " + "appid = " + appid + "\ntaskid = " + taskid + "\nmessageid = " + messageid + "\npkg = " + pkg
				+ "\ncid = " + cid);

		if (payload == null) {
			Log.e(TAG, "receiver payload = null");
		} else {
			String data = new String(payload);
			Log.d(TAG, "receiver payload = " + data);
			System.out.println("onReceiveMessageData:"+data);
			if(!TextUtils.isEmpty(data)) {
				MessageDataBean mdb = gson.fromJson(data,MessageDataBean.class);
				if (mdb != null) {
					sendMessage(mdb);
				}
			}
		}

	}

	@Override
	public void onReceiveOnlineState(Context arg0, boolean arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReceiveServicePid(Context arg0, int arg1) {
		// TODO Auto-generated method stub
		System.out.println("onReceiveServicePid:"+arg1);
		CommonUtils.showToast("onReceiveCommandResult:"+arg1);
	}

	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
	private void sendMessage(MessageDataBean mdb) {
		if (mdb == null ) return;
		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Intent dataIntent = new Intent(this,UrlWebClientActivity.class);
		dataIntent.putExtra(UrlWebClientActivity.KEY_URL,mdb.getUrl());
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, dataIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		Notification notification = new Notification.Builder(this)
				.setSmallIcon(R.drawable.logo)//设置小图标
				.setContentTitle(mdb.getTitle())
				.setContentText(mdb.getContent())
				.setAutoCancel(true)
				.setContentIntent(pendingIntent)
				.setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE)
				.build();

		notificationManager.notify(0, notification);

	}
}
