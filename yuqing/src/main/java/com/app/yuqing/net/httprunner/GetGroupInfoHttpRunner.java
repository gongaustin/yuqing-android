package com.app.yuqing.net.httprunner;

import android.util.Log;

import com.app.yuqing.AppContext;
import com.app.yuqing.net.Event;
import com.app.yuqing.net.OKHttpUtil;
import com.app.yuqing.net.URLUtils;
import com.app.yuqing.net.bean.BaseResponseBean;
import com.app.yuqing.net.bean.GroupInfoResponseBean;
import com.app.yuqing.utils.CommonUtils;

/**
 * Created by Administrator on 2018/6/12.
 */

public class GetGroupInfoHttpRunner extends HttpRunner {

    @Override
    public void onEventRun(Event event) throws Exception {
        String gid = (String) event.getParamAtIndex(0);

        String url = URLUtils.GETGROUPINFO+"?gid="+gid;
        String result = OKHttpUtil.getInstance().request(url, null, "get",false);

        Log.i(AppContext.LOG_NET, result);
        System.out.println("result : "+result);
        if (CommonUtils.isEmpty(result)) {
            event.setSuccess(false);
            event.setFailException(new Exception("网络错误"));
            return;
        }

        GroupInfoResponseBean bean = gson.fromJson(result, GroupInfoResponseBean.class);

        if (!bean.isSuccess()) {
            event.setSuccess(false);
            event.setFailException(new Exception(bean.getMsg()));
            event.setmErrorCode(bean.getCode());
        } else {
            event.setSuccess(true);
            event.addReturnParam(bean);
        }
    }
}
