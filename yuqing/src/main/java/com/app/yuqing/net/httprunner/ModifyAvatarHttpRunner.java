package com.app.yuqing.net.httprunner;

import android.util.Log;

import com.app.yuqing.AppContext;
import com.app.yuqing.net.Event;
import com.app.yuqing.net.OKHttpUtil;
import com.app.yuqing.net.URLUtils;
import com.app.yuqing.net.bean.BaseResponseBean;
import com.app.yuqing.utils.CommonUtils;

/**
 * Created by Administrator on 2018/6/11.
 */

public class ModifyAvatarHttpRunner extends HttpRunner {

    @Override
    public void onEventRun(Event event) throws Exception {
        String newAvatarUrl = (String) event.getParamAtIndex(0);

        String url = URLUtils.MODIFYAVATAR+"?newAvatarUrl="+newAvatarUrl;
        String result = OKHttpUtil.getInstance().request(url, null, "get",false);

        Log.i(AppContext.LOG_NET, result);
        System.out.println("result : "+result);
        if (CommonUtils.isEmpty(result)) {
            event.setSuccess(false);
            event.setFailException(new Exception("网络错误"));
            return;
        }

        BaseResponseBean bean = gson.fromJson(result, BaseResponseBean.class);

        if (!bean.isSuccess()) {
            event.setSuccess(false);
            event.setFailException(new Exception(bean.getMsg()));
            event.setmErrorCode(bean.getCode());
        } else {
            event.setSuccess(true);
            event.addReturnParam(bean);
            event.addReturnParam(newAvatarUrl);
        }

    }
}
