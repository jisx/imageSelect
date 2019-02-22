package com.jisx.view.image.selection;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jisx.view.image.select.GridSelectView;
import com.jisx.view.image.select.ModeType;

public class MainActivity extends AppCompatActivity {


    GridSelectView mGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGridView = findViewById(R.id.gv_img);
//        mGridView.setMaxLength(5);
//        mGridView.setType(ModeType.EDIT);
//        mGridView.setRadius(10);
//        mGridView.setAddView(R.layout.item_add_view);
//        mGridView.setScaleType(ImageView.ScaleType.FIT_XY);
//        mGridView.addImages("/storage/emulated/0/DCIM/Camera/IMG_20190213_080530.jpg", "/storage/emulated/0/DCIM/Camera/IMG_20190213_080525.jpg");
//        mGridView.isAutoHeight(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GridSelectView.REQUEST_CODE_IMAGE_SELECT && data != null && data.getData() != null) {
            String path = UriUtils.getFileAbsolutePath(this, data.getData());
            mGridView.addImage(path);
        }
    }
}
