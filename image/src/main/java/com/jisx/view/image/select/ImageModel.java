package com.jisx.view.image.select;

/**
 * Created by jsx on 2019/2/13.
 */
public class ImageModel {
    private String localPath;
    private String servicePath;

    private boolean isAdd = false;

    public ImageModel() {
    }

    public ImageModel(String localPath) {
        this.localPath = localPath;
    }

    public ImageModel(boolean isAdd) {
        this.isAdd = isAdd;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getServicePath() {
        return servicePath;
    }

    public void setServicePath(String servicePath) {
        this.servicePath = servicePath;
    }

    public boolean isAdd() {
        return isAdd;
    }
}
