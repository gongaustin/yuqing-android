package com.app.yuqing.net.bean;

import com.app.yuqing.bean.UploadFileBean;

/**
 * Created by Administrator on 2018/6/20.
 */

public class UploadFileResponseBean extends BaseResponseBean {

    private UploadFileBean data;

    public UploadFileBean getData() {
        return data;
    }

    public void setData(UploadFileBean data) {
        this.data = data;
    }
}
