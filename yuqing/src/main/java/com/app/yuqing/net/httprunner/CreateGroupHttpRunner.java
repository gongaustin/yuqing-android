package com.app.yuqing.net.httprunner;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import android.util.Log;

import com.app.yuqing.AppContext;
import com.app.yuqing.net.Event;
import com.app.yuqing.net.OKHttpUtil;
import com.app.yuqing.net.URLUtils;
import com.app.yuqing.net.bean.BaseResponseBean;
import com.app.yuqing.net.bean.BaseRongYunResponseBean;
import com.app.yuqing.net.bean.TokenBean;
import com.app.yuqing.net.bean.UpdatePhoneResponseBean;
import com.app.yuqing.utils.CommonUtils;

public class CreateGroupHttpRunner extends HttpRunner {

	@Override
	public void onEventRun(Event event) throws Exception {
		// TODO Auto-generated method stub

		String masterId = (String) event.getParamAtIndex(0);
		String userId = (String) event.getParamAtIndex(1);
		String groupId = (String) event.getParamAtIndex(2);
		String groupName = (String) event.getParamAtIndex(3);
		
		System.out.println("masterId:"+masterId);
		System.out.println("userId:"+userId);
		System.out.println("groupId:"+groupId);
		System.out.println("groupName:"+groupName);
		
        RequestBody body=new FormBody.Builder()
        .add("userId",userId)
        .add("groupId",groupId)
        .add("groupName",groupName)
        .add("masterId",masterId).build();
        
        String result = OKHttpUtil.getInstance().request(URLUtils.CREATEGROUP, body, "post",false);
		
		Log.i(AppContext.LOG_NET, result);
		System.out.println("result : "+result);
		if (CommonUtils.isEmpty(result)) {
			event.setSuccess(false);
			event.setFailException(new Exception("网络错误"));
			return;
		}
		
		if ("false".equals(result)) {
			event.setSuccess(false);
			event.setFailException(new Exception("创建失败"));
		} else {
			BaseRongYunResponseBean bean = gson.fromJson(result, BaseRongYunResponseBean.class);
			if (bean != null) {
				if (checkRongYun(bean)) {
					event.setSuccess(true);
					event.addReturnParam(bean);
					return;
				}
			}
			event.setSuccess(false);
		}
	
	
	}

	
}
