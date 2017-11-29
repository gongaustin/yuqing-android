package com.app.yuqing.net.bean;

import com.app.yuqing.bean.UserBean;

public class UserDetailResponseBean extends BaseResponseBean {

	private String flag;
	private UserBean data;
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public UserBean getData() {
		return data;
	}
	public void setData(UserBean data) {
		this.data = data;
	}
}
