package com.app.yuqing.service;

import android.content.Context;

import com.app.yuqing.net.AndroidEventManager;
import com.app.yuqing.net.EventCode;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;

public class YuQingIntentService extends GTIntentService{

	@Override
	public void onReceiveClientId(Context arg0, String arg1) {
		// TODO Auto-generated method stub
		System.out.println("onReceiveClientId:"+arg1);
		
		AndroidEventManager.getInstance().pushEvent(EventCode.HTTP_UPDATECLIENTID, arg1);
	}

	@Override
	public void onReceiveCommandResult(Context arg0, GTCmdMessage arg1) {
		// TODO Auto-generated method stub
		System.out.println("onReceiveCommandResult:"+arg1);
	}

	@Override
	public void onReceiveMessageData(Context arg0, GTTransmitMessage arg1) {
		// TODO Auto-generated method stub
		System.out.println("onReceiveMessageData:"+arg1);
	}

	@Override
	public void onReceiveOnlineState(Context arg0, boolean arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReceiveServicePid(Context arg0, int arg1) {
		// TODO Auto-generated method stub
		System.out.println("onReceiveServicePid:"+arg1);
	}

}
