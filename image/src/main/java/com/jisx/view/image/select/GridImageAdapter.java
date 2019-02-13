package com.jisx.view.image.select;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by jsx on 2019/2/13.
 */
class GridImageAdapter<T extends ImageModel> extends BaseAdapter {

    ModeType mModeType;

    enum Type {
        IMAGE(0), ADD(1);

        int index;

        Type(int i) {
            this.index = i;
        }

        public static Type getType(int index){
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

    private List<T> mImageModelList;

    private OperationClick mOperationClick;

    public GridImageAdapter(Context context, List<T> imageModelList) {
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
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_grid_image_add, null);
                    viewHolder = new ViewHolder(convertView);
                    break;
            }

            convertView.setTag(viewHolder);
        } else {
            if (convertView.getTag() != null && convertView.getTag() instanceof ViewHolder) {
                viewHolder = (ViewHolder) convertView.getTag();
            }
        }

        switch (type) {
            case IMAGE:
                viewHolder.mImageView.setImageBitmap(BitmapFactory.decodeFile(mImageModelList.get(position).getLocalPath()));
                viewHolder.mImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOperationClick.showClick(mImageModelList.get(position),position);
                    }
                });
                viewHolder.mDeleteView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOperationClick.deleteClick(mImageModelList.get(position),position);
                    }
                });
                switch (mModeType){
                    case SHOW:
                        viewHolder.mDeleteView.setVisibility(View.GONE);
                        break;
                    case EDIT:
                        viewHolder.mDeleteView.setVisibility(View.VISIBLE);
                        break;
                }
                break;
            case ADD:
                viewHolder.mImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOperationClick.addClick(mImageModelList.get(position),position);
                    }
                });
                break;
        }

        return convertView;
    }

    public List<T> getImageList() {
        return mImageModelList;
    }

    protected void setModeType(ModeType modeType) {
        mModeType = modeType;
    }

    private static class ViewHolder {

        ImageView mImageView;

        ImageView mDeleteView;

        public ViewHolder(View convertView) {
            mImageView = convertView.findViewById(R.id.iv_img);
            mDeleteView = convertView.findViewById(R.id.iv_delete);
        }
    }

    protected void setOperationClick(OperationClick operationClick) {
        mOperationClick = operationClick;
    }
}
