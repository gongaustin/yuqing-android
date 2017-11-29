package com.app.yuqing.net.httprunner;

import android.util.Log;

import com.app.yuqing.AppContext;
import com.app.yuqing.net.Event;
import com.app.yuqing.net.OKHttpUtil;
import com.app.yuqing.net.URLUtils;
import com.app.yuqing.net.bean.BacklogResponseBean;
import com.app.yuqing.utils.CommonUtils;

public class QueryBacklogHttpRunner extends HttpRunner {

	@Override
	public void onEventRun(Event event) throws Exception {
		// TODO Auto-generated method stub
        String result = OKHttpUtil.getInstance().request(URLUtils.QUERYBACKLOG, null, "get",false);
		
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
			BacklogResponseBean bean = gson.fromJson(result, BacklogResponseBean.class);
			event.setSuccess(true);
			event.addReturnParam(bean);
		}		
	}

}
