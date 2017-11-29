package com.app.yuqing.net.httprunner;

import android.util.Log;

import com.app.yuqing.AppContext;
import com.app.yuqing.net.Event;
import com.app.yuqing.net.OKHttpUtil;
import com.app.yuqing.net.URLUtils;
import com.app.yuqing.net.bean.VersionBean;
import com.app.yuqing.utils.CommonUtils;

public class QueryLastVersionHttpRunner extends HttpRunner {

	@Override
	public void onEventRun(Event event) throws Exception {
//		String result = HttpUtils.doGetString(URLUtils.QUERYLASTVERSION,true);
        
        String result = OKHttpUtil.getInstance().request(URLUtils.QUERYLASTVERSION, null, "get",false);
		
		Log.i(AppContext.LOG_NET, result);
		System.out.println("result : "+result);
		if (CommonUtils.isEmpty(result)) {
			event.setSuccess(false);
			event.setFailException(new Exception("网络错误"));
			return;
		}
		
		if ("false".equals(result)) {
			event.setSuccess(false);
		} else {
			VersionBean bean = gson.fromJson(result, VersionBean.class);
			event.setSuccess(true);
			event.addReturnParam(bean);
		}
	}

}
