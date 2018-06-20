package com.app.yuqing.bean;

/**
 * Created by Administrator on 2018/6/20.
 */

public class UploadFileBean extends BaseBean {
    private String fileName;
    private String id;
    private String url;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
