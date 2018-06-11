package com.app.yuqing.bean;

public class GroupBean extends BaseBean {

	private String groupId;
	private String userId;
	private String id;
	private String isNewRecord;
	private String groupName;
	private String createUser;
	private String createTime;

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIsNewRecord() {
		return isNewRecord;
	}
	public void setIsNewRecord(String isNewRecord) {
		this.isNewRecord = isNewRecord;
	}
}
