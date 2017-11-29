package com.app.yuqing.net.httprunner;

import java.io.File;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import android.util.Log;

import com.app.yuqing.AppContext;
import com.app.yuqing.net.Event;
import com.app.yuqing.net.OKHttpUtil;
import com.app.yuqing.net.URLUtils;
import com.app.yuqing.net.bean.UpdateHeadResponseBean;
import com.app.yuqing.utils.CommonUtils;

public class UpdateHeadHttpRunner extends HttpRunner {

	@Override
	public void onEventRun(Event event) throws Exception {
		// TODO Auto-generated method stub
		File file = (File) event.getParamAtIndex(0);
		
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", "file.jpg", fileBody)
                .build();
        
        String result=OKHttpUtil.getInstance().request(URLUtils.UPDATEHEAD,requestBody,"post",false);
		
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
			UpdateHeadResponseBean bean = gson.fromJson(result, UpdateHeadResponseBean.class);
			if ("true".equals(bean.getFlag())) {
				event.setSuccess(true);
				event.addReturnParam(bean);
			} else {
				event.setSuccess(false);
			}
		}
	
	}

}
