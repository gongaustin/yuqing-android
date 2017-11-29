package com.app.yuqing.net.httprunner;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import android.util.Log;

import com.app.yuqing.AppContext;
import com.app.yuqing.net.Event;
import com.app.yuqing.net.OKHttpUtil;
import com.app.yuqing.net.URLUtils;
import com.app.yuqing.net.bean.UpdateClientIdResponseBean;
import com.app.yuqing.utils.CommonUtils;

public class UpdateClientIdHttpRunner extends HttpRunner {

	@Override
	public void onEventRun(Event event) throws Exception {
		String mobile = (String) event.getParamAtIndex(0);
        RequestBody body=new FormBody.Builder()
        .add("clientId",mobile).build();
        
        System.out.println("clientId:"+mobile);
        String result = OKHttpUtil.getInstance().request(URLUtils.UPDATECLIENTID, body, "post",false);
		
		Log.i(AppContext.LOG_NET, result);
		System.out.println("result : "+result);
		if (CommonUtils.isEmpty(result)) {
			event.setSuccess(false);
			event.setFailException(new Exception("网络错误"));
			return;
		}
		
		if ("false".equals(result)) {
			event.setSuccess(false);
			event.setFailException(new Exception("修改密码失败"));
		} else {
			UpdateClientIdResponseBean bean = gson.fromJson(result, UpdateClientIdResponseBean.class);
			if ("true".equals(bean.getFlag())) {
				event.setSuccess(true);
				event.addReturnParam(bean);
			} else {
				event.setSuccess(false);
				event.setFailException(new Exception("修改密码失败"));
			}
		}
	}
	
}
