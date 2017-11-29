package com.app.yuqing.net.httprunner;

import android.util.Log;

import com.app.yuqing.AppContext;
import com.app.yuqing.net.Event;
import com.app.yuqing.net.OKHttpUtil;
import com.app.yuqing.net.URLUtils;
import com.app.yuqing.net.bean.MenuResponseBean;
import com.app.yuqing.utils.CommonUtils;

public class MenuListHttpRunner extends HttpRunner {

	@Override
	public void onEventRun(Event event) throws Exception {
		// TODO Auto-generated method stub
		String result = OKHttpUtil.getInstance().request(URLUtils.MENULIST, null, "get",false);
		
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
			MenuResponseBean bean = gson.fromJson(result, MenuResponseBean.class);
			if ("true".equals(bean.getFlag())) {
				event.setSuccess(true);
				event.addReturnParam(bean);
			} else {
				event.setSuccess(false);
				event.setFailException(new Exception(bean.getMsg()));
			}
		}
	}

}
