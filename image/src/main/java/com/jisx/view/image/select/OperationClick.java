package com.jisx.view.image.select;

/**
 * Created by jsx on 2019/2/13.
 */
interface OperationClick {
    void deleteClick(ImageModel model, int position);

    void addClick(ImageModel model, int position);

    void showClick(ImageModel model, int position);
}
