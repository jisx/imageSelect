package com.jisx.view.image.select;

/**
 * Created by jsx on 2019/2/14.
 */
public interface ImageClickListener {
    /**
     * 优先返回本地路径
     * @param path
     */
    public void imageClick(String path);
}
