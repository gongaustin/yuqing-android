package com.app.yuqing.net.bean;

import com.app.yuqing.bean.UserOldBean;

public class UserDetailResponseBean extends BaseResponseBean {

	private String flag;
	private UserOldBean data;
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public UserOldBean getData() {
		return data;
	}
	public void setData(UserOldBean data) {
		this.data = data;
	}
}
