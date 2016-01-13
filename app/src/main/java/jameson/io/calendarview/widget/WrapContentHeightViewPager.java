/*
 * WrapContentHeightViewPager      2015-07-13
 * Copyright (c) 2015 hujiang Co.Ltd. All right reserved(http://www.hujiang.com).
 * 
 */
package jameson.io.calendarview.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import jameson.io.calendarview.util.LogUtils;

/**
 * 支持android:layout_height="wrap_content", 自适应高度ViewPager
 * <p>
 * Created by jameson on 1/6/16.
 */
public class WrapContentHeightViewPager extends ViewPager {

    private int mHeight;

    /**
     * Constructor
     *
     * @param context the context
     */
    public WrapContentHeightViewPager(Context context) {
        super(context);
    }

    /**
     * Constructor
     *
     * @param context the context
     * @param attrs   the attribute set
     */
    public WrapContentHeightViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mHeight > 0) {
            LogUtils.d(mHeight + "");
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }

        int specHeightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (specHeightMode != MeasureSpec.EXACTLY) {
            int height = 0;
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                int h = child.getMeasuredHeight();
                if (h > height) height = h;
            }

            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setHeight(int mHeight) {
        this.mHeight = mHeight;
        requestLayout();
    }
}