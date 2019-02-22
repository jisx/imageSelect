package com.jisx.view.image.select;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
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

    private static final ImageView.ScaleType[] sScaleTypeArray = {
            ImageView.ScaleType.MATRIX,
            ImageView.ScaleType.FIT_XY,
            ImageView.ScaleType.FIT_START,
            ImageView.ScaleType.FIT_CENTER,
            ImageView.ScaleType.FIT_END,
            ImageView.ScaleType.CENTER,
            ImageView.ScaleType.CENTER_CROP,
            ImageView.ScaleType.CENTER_INSIDE
    };

    GridView gridView;

    GridImageAdapter adapter;

    public int maxLength = 5;

    ModeType mType = ModeType.EDIT;

    private int radius;

    private boolean isAutoHeight;

    private int numColumns;

    private int scanTypeIndex;

    private int addViewLayout;

    private int horizontalSpacing;
    private int verticalSpacing;

    ImageModel addImageModel;

    List<ImageModel> mModelList;

    ImageClickListener mImageClickListener;

    public GridSelectView(Context context) {
        super(context);
        init(null);
    }

    public GridSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public GridSelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        addImageModel = new ImageModel(true);

        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.GridSelectView);
            int mode = ta.getInt(R.styleable.GridSelectView_ModeType, 0);
            mType = mode == 0 ? ModeType.EDIT : ModeType.SHOW;
            radius = ta.getInt(R.styleable.GridSelectView_radius,0);
            isAutoHeight = ta.getBoolean(R.styleable.GridSelectView_isAutoHeight,false);
            maxLength = ta.getInt(R.styleable.GridSelectView_maxLength,0);
            numColumns = ta.getInt(R.styleable.GridSelectView_numColumns,3);
            horizontalSpacing = ta.getDimensionPixelOffset(R.styleable.GridSelectView_android_horizontalSpacing,0);
            verticalSpacing = ta.getDimensionPixelOffset(R.styleable.GridSelectView_android_verticalSpacing,0);
            scanTypeIndex = ta.getInt(R.styleable.GridSelectView_android_scaleType,3);
            addViewLayout = ta.getResourceId(R.styleable.GridSelectView_layout,R.layout.item_grid_image_add);
            ta.recycle();
        }
        gridView = new AutoHeightGridView(getContext());
        gridView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        gridView.setHorizontalSpacing(horizontalSpacing);
        gridView.setVerticalSpacing(verticalSpacing);
        gridView.setNumColumns(numColumns);

        addView(gridView);

        initAdapter();
    }

    private void initAdapter() {
        mModelList = new ArrayList<>();
        adapter = new GridImageAdapter(getContext(), mModelList);

        View view = LayoutInflater.from(getContext()).inflate(addViewLayout, null);
        adapter.setAddView(view);
        adapter.setRadius(dip2px(radius));
        adapter.setModeType(mType);
        adapter.setScaleType(sScaleTypeArray[scanTypeIndex]);

        adapter.setOperationClick(this);


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

    public void setAddView(int resId) {
        View view = LayoutInflater.from(getContext()).inflate(resId, null);
        setAddView(view);
    }

    public void setRadius(int radius) {
        this.radius = radius;
        adapter.setRadius(dip2px(radius));
        adapter.notifyDataSetChanged();
    }

    public void setScaleType(ImageView.ScaleType type) {
        adapter.setScaleType(type);
        adapter.notifyDataSetChanged();
    }

    public void isAutoHeight(boolean isAutoHeight) {
        isAutoHeight = isAutoHeight;
        if (isAutoHeight) {
            gridView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        } else {
            gridView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        }
    }

    public void setImageClickListener(ImageClickListener imageClickListener) {
        mImageClickListener = imageClickListener;
    }

    public List<String> getImageList() {
        List<String> list = new ArrayList<>();
        for (ImageModel imageModel : mModelList) {
            if (!imageModel.equals(addImageModel)) {
                list.add(imageModel.getFilePath());
            }
        }

        return list;
    }

    private int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
