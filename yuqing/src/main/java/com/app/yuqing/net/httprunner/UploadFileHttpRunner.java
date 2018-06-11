package com.app.yuqing.net.httprunner;

import android.util.Log;

import com.app.yuqing.AppContext;
import com.app.yuqing.net.Event;
import com.app.yuqing.net.OKHttpUtil;
import com.app.yuqing.net.URLUtils;
import com.app.yuqing.net.bean.BaseResponseBean;
import com.app.yuqing.utils.CommonUtils;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2018/6/11.
 */

public class UploadFileHttpRunner extends HttpRunner {

    @Override
    public void onEventRun(Event event) throws Exception {
        File file = (File) event.getParamAtIndex(0);

        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", "file", fileBody)
                .build();

        String result=OKHttpUtil.getInstance().request(URLUtils.UPLOADFILE,requestBody,"post",false);

        Log.i(AppContext.LOG_NET, result);
        System.out.println("result : "+result);
        if (CommonUtils.isEmpty(result)) {
            event.setSuccess(false);
            event.setFailException(new Exception("网络错误"));
            return;
        }

        BaseResponseBean bean = gson.fromJson(result,BaseResponseBean.class);

        if (!bean.isSuccess()) {
            event.setSuccess(false);
            event.setFailException(new Exception(bean.getMsg()));
        } else {
            event.setSuccess(true);
            event.addReturnParam(bean);
        }
    }
}
