package com.jisx.view.image.select;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

/**
 * Created by jsx on 2019/2/13.
 */
class GridImageAdapter extends BaseAdapter {
    enum Type {
        IMAGE(0), ADD(1);

        int index;

        Type(int i) {
            this.index = i;
        }

        public static Type getType(int index) {
            for (Type type : values()) {
                if (type.getIndex() == index) {
                    return type;
                }
            }

            return null;
        }

        public int getIndex() {
            return index;
        }
    }

    Context mContext;

    ModeType mModeType;

    ImageView.ScaleType mScaleType;

    private List<ImageModel> mImageModelList;

    private OperationClick mOperationClick;

    int radius;

    View addView;

    public GridImageAdapter(Context context, List<ImageModel> imageModelList) {
        mContext = context;
        mImageModelList = imageModelList;
    }

    @Override
    public int getCount() {
        return mImageModelList == null ? 0 : mImageModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return mImageModelList.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (mImageModelList.get(position).isAdd()) {
            return Type.ADD.getIndex();
        } else {
            return Type.IMAGE.getIndex();
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Type type = Type.getType(getItemViewType(position));

        ViewHolder viewHolder = null;
        if (convertView == null) {
            switch (type) {
                case IMAGE:
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_grid_image_show, null);
                    viewHolder = new ViewHolder(convertView);
                    break;
                case ADD:
                    convertView = addView;
                    viewHolder = new ViewHolder(convertView);
                    break;
            }

            convertView.setTag(viewHolder);
        } else {
            if (type == Type.ADD && convertView != addView) {
                convertView = addView;
                viewHolder = new ViewHolder(convertView);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
        }

        switch (type) {
            case IMAGE:
                if (mScaleType != null) {
                    viewHolder.mImageView.setScaleType(mScaleType);
                }
                if (radius > 0) {
                    RoundedCorners roundedCorners = new RoundedCorners(radius);
                    RequestOptions options = RequestOptions.bitmapTransform(roundedCorners).format(DecodeFormat.PREFER_ARGB_8888);
                    Glide.with(mContext).asBitmap().apply(options).load(mImageModelList.get(position).getFilePath()).into(viewHolder.mImageView);
                } else {
                    Glide.with(mContext).load(mImageModelList.get(position).getFilePath()).into(viewHolder.mImageView);
                }

                viewHolder.mImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOperationClick.showClick(mImageModelList.get(position), position);
                    }
                });
                viewHolder.mDeleteView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOperationClick.deleteClick(mImageModelList.get(position), position);
                    }
                });
                switch (mModeType) {
                    case SHOW:
                        viewHolder.mDeleteView.setVisibility(View.GONE);
                        break;
                    case EDIT:
                        viewHolder.mDeleteView.setVisibility(View.VISIBLE);
                        break;
                }
                break;
            case ADD:
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOperationClick.addClick(mImageModelList.get(position), position);
                    }
                });
                break;
        }

        return convertView;
    }

    protected void setModeType(ModeType modeType) {
        mModeType = modeType;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setAddView(View view) {
        addView = view;
    }

    public void setScaleType(ImageView.ScaleType type) {
        this.mScaleType = type;
    }

    private static class ViewHolder {

        RelativeLayout mRlContent;

        ImageView mImageView;

        ImageView mDeleteView;

        public ViewHolder(View convertView) {
            mImageView = convertView.findViewById(R.id.iv_img);
            mDeleteView = convertView.findViewById(R.id.iv_delete);
            mRlContent = convertView.findViewById(R.id.rl_content);
        }
    }

    protected void setOperationClick(OperationClick operationClick) {
        mOperationClick = operationClick;
    }
}
