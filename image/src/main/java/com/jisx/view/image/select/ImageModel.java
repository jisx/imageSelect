package com.jisx.view.image.select;

/**
 * Created by jsx on 2019/2/13.
 */
class ImageModel {
    private String filePath;

    private boolean isAdd = false;

    public ImageModel() {
    }

    public ImageModel(String localPath) {
        this.filePath = localPath;
    }

    public ImageModel(boolean isAdd) {
        this.isAdd = isAdd;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isAdd() {
        return isAdd;
    }
}
