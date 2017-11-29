package com.app.yuqing.net.bean;

import java.util.List;

import com.app.yuqing.bean.GroupBean;

public class GroupListResponseBean extends BaseRongYunResponseBean {

	private String id;
	private List<GroupBean> groups;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<GroupBean> getGroups() {
		return groups;
	}
	public void setGroups(List<GroupBean> groups) {
		this.groups = groups;
	}
}
