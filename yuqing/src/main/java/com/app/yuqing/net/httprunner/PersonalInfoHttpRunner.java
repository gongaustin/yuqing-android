package com.app.yuqing.net.httprunner;

import android.util.Log;

import com.app.yuqing.AppContext;
import com.app.yuqing.net.Event;
import com.app.yuqing.net.OKHttpUtil;
import com.app.yuqing.net.URLUtils;
import com.app.yuqing.net.bean.BacklogResponseBean;
import com.app.yuqing.net.bean.PersonalInfoResponseBean;
import com.app.yuqing.utils.CommonUtils;

/**
 * Created by Administrator on 2018/6/4.
 */

public class PersonalInfoHttpRunner extends HttpRunner {

    @Override
    public void onEventRun(Event event) throws Exception {
        String result = OKHttpUtil.getInstance().request(URLUtils.PERSONALINFO, null, "get",false);

        Log.i(AppContext.LOG_NET, result);
        System.out.println("result : "+result);
        if (CommonUtils.isEmpty(result)) {
            event.setSuccess(false);
            event.setFailException(new Exception("网络错误"));
            return;
        }

        PersonalInfoResponseBean bean = gson.fromJson(result, PersonalInfoResponseBean.class);
        if (!bean.isSuccess()) {
            event.setSuccess(false);
            event.setmErrorCode(bean.getCode());
        } else {
            event.setSuccess(true);
            event.addReturnParam(bean);
        }
    }
}
