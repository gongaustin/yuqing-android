package com.app.yuqing.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/6/12.
 */

public class GroupInfoBean extends BaseBean {

    private String groupName;
    private String createTime;
    private List<GroupMemberBean> members;
    private String groupId;
    private String createUser;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public List<GroupMemberBean> getMembers() {
        return members;
    }

    public void setMembers(List<GroupMemberBean> members) {
        this.members = members;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }
}
