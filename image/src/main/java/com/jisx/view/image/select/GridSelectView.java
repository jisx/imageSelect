package com.jisx.view.image.select;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jsx on 2019/2/13.
 */
public class GridSelectView extends RelativeLayout implements OperationClick {

    public static final int REQUEST_CODE_IMAGE_SELECT = 201;
    GridView gridView;

    GridImageAdapter adapter;

    public int maxLength = 5;

    ModeType mType = ModeType.EDIT;

    ImageModel addImageModel;

    List<ImageModel> mModelList;

    ImageClickListener mImageClickListener;

    public GridSelectView(Context context) {
        super(context);
        init();
    }

    public GridSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GridSelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        addImageModel = new ImageModel(true);

        gridView = new GridView(getContext());
        gridView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        gridView.setHorizontalSpacing(dip2px(10));
        gridView.setVerticalSpacing(dip2px(10));
        gridView.setNumColumns(3);
        addView(gridView);
        initAdapter();
    }

    private void initAdapter() {
        mModelList = new ArrayList<>();
        adapter = new GridImageAdapter(getContext(), mModelList);

        adapter.setOperationClick(this);
        adapter.setModeType(mType);

        switch (mType) {
            case EDIT:
                if (!mModelList.contains(addImageModel)) {
                    mModelList.add(addImageModel);
                }
                break;
            case SHOW:
                break;
        }
        gridView.setAdapter(adapter);
    }

    public void addImage(String path) {
        switch (mType) {
            case EDIT:
                mModelList.remove(addImageModel);//先移除

                if (mModelList.size() < maxLength) {
                    mModelList.add(new ImageModel(path));
                }

                if (mModelList.size() < maxLength) {
                    mModelList.add(addImageModel);//先移除
                }

                adapter.notifyDataSetChanged();

                break;
            case SHOW:


                break;
        }
    }

    public void addImages(String... list) {
        for (String path : list) {
            mModelList.add(new ImageModel(path));
        }
        checkModelList();
    }

    public void addImages(List<String> list) {
        for (String path : list) {
            mModelList.add(new ImageModel(path));
        }
        checkModelList();
    }

    private void checkModelList() {
        mModelList.remove(addImageModel);//先移除

        if (mModelList.size() > maxLength) {
            mModelList.subList(0, maxLength - 1);
        }

        switch (mType) {
            case EDIT:
                if (mModelList.size() < maxLength) {
                    mModelList.add(addImageModel);
                }
                break;
            case SHOW:
                break;
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void deleteClick(ImageModel model, int position) {
        mModelList.remove(position);
        checkModelList();
    }

    @Override
    public void addClick(ImageModel model, int position) {
        if (getContext() instanceof Activity) {
            Activity activity = (Activity) getContext();
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            activity.startActivityForResult(intent, REQUEST_CODE_IMAGE_SELECT);
        }
    }

    @Override
    public void showClick(ImageModel model, int position) {
        if (mImageClickListener != null) {
            mImageClickListener.imageClick(model.getFilePath());
        }
    }

    public void setMaxLength(int maxLength) {
        if (maxLength > 0) {
            this.maxLength = maxLength;
            checkModelList();
        }
    }

    public void setType(ModeType type) {
        mType = type;
        adapter.setModeType(mType);
        checkModelList();
    }

    public void setAddView(View view) {
        adapter.setAddView(view);
        checkModelList();
    }

    public void setRadius(int dp){
        adapter.setRadius(dip2px(dp));
        adapter.notifyDataSetChanged();
    }

    public void setScaleType(ImageView.ScaleType type){
        adapter.setScaleType(type);
        adapter.notifyDataSetChanged();
    }

    public void setImageClickListener(ImageClickListener imageClickListener) {
        mImageClickListener = imageClickListener;
    }

    public int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
