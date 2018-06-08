package com.app.yuqing.bean;

/**
 * Created by Administrator on 2018/6/8.
 */

public class VersionBean extends BaseBean {
    private String updateInstall;
    private String updateId;
    private String appId;
    private String downloadUrl;
    private String updateLog;
    private String newVersion;

    public String getUpdateInstall() {
        return updateInstall;
    }

    public void setUpdateInstall(String updateInstall) {
        this.updateInstall = updateInstall;
    }

    public String getUpdateId() {
        return updateId;
    }

    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getUpdateLog() {
        return updateLog;
    }

    public void setUpdateLog(String updateLog) {
        this.updateLog = updateLog;
    }

    public String getNewVersion() {
        return newVersion;
    }

    public void setNewVersion(String newVersion) {
        this.newVersion = newVersion;
    }
}
