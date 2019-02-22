package com.jisx.view.image.select;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.GridView;

/**
 * Created by jsx on 2019/2/18.
 */
public class AutoHeightGridView extends GridView {
    public AutoHeightGridView(Context context) {
        super(context);
    }

    public AutoHeightGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoHeightGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightSpec;

        if(getLayoutParams().height == AbsListView.LayoutParams.WRAP_CONTENT){
            heightSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2,MeasureSpec.AT_MOST);
        }else{
            heightSpec = heightMeasureSpec;
        }

        super.onMeasure(widthMeasureSpec, heightSpec);
    }
}
