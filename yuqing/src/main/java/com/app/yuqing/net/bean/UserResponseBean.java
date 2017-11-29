package com.app.yuqing.net.bean;

import com.app.yuqing.bean.UserBean;

public class UserResponseBean extends BaseResponseBean {

	private String flag;
	private UserBean user;
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public UserBean getUser() {
		return user;
	}
	public void setUser(UserBean user) {
		this.user = user;
	}
}
