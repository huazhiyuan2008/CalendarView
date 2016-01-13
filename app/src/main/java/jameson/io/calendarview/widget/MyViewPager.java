package jameson.io.calendarview.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import jameson.io.calendarview.util.LogUtils;
import jameson.io.calendarview.util.MeasureUtil;

/**
 * Created by jameson on 1/12/16.
 */
public class MyViewPager extends ViewPager {
    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        LogUtils.d(String.format("specModeWidth=%s, specModeHeight=%s, specSizeWidth=%s, specSizeHeight=%s",
                MeasureUtil.getSpecModeName(widthMeasureSpec), MeasureUtil.getSpecModeName(heightMeasureSpec),
                MeasureUtil.getSpecSize(widthMeasureSpec), MeasureUtil.getSpecSize(heightMeasureSpec)));
        if (mHeight > 0) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY);
        }
        LogUtils.d(String.format("specModeWidth=%s, specModeHeight=%s, specSizeWidth=%s, specSizeHeight=%s",
                MeasureUtil.getSpecModeName(widthMeasureSpec), MeasureUtil.getSpecModeName(heightMeasureSpec),
                MeasureUtil.getSpecSize(widthMeasureSpec), MeasureUtil.getSpecSize(heightMeasureSpec)));
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        LogUtils.i(String.format("changed=%s, l=%s, t=%s, r=%s, b=%s", changed, l, t, r, b));
        super.onLayout(changed, l, t, r, b);
    }

    private int mHeight = 0;

    public void setHeight(int mHeight) {
        this.mHeight = mHeight;
        requestLayout();
    }
}
