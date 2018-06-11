package com.app.yuqing.net.bean;

import com.app.yuqing.bean.GroupInfoBean;

/**
 * Created by Administrator on 2018/6/12.
 */

public class GroupInfoResponseBean extends BaseResponseBean {

    private GroupInfoBean data;

    public GroupInfoBean getData() {
        return data;
    }

    public void setData(GroupInfoBean data) {
        this.data = data;
    }
}
