package com.app.yuqing.net.bean;

import java.util.List;

import com.app.yuqing.bean.GroupUserBean;

public class GroupUserResponseBean extends BaseRongYunResponseBean {

	private List<GroupUserBean> users;

	public List<GroupUserBean> getUsers() {
		return users;
	}

	public void setUsers(List<GroupUserBean> users) {
		this.users = users;
	}
}
