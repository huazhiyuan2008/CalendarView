package jameson.io.calendarview.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import jameson.io.calendarview.R;
import jameson.io.calendarview.util.LogUtils;

/**
 * Created by jameson on 1/8/16.
 */
public class ExpandCalendarViewPager extends CalendarViewPager {
    private static final int ANIM_TIME = 300;
    private boolean mScrollable = true;
    private boolean mIsOpen = true;
    private ValueAnimator mAnim;
    private int mOriginHeight = 0;
    private int mHeight;

    public ExpandCalendarViewPager(Context context) {
        super(context);
    }

    public ExpandCalendarViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initViews() {
        super.initViews();
        post(new Runnable() {
            @Override
            public void run() {
                if (mOriginHeight == 0) {
                    mOriginHeight = getHeight();
                    LogUtils.i(mOriginHeight + "");
                }
            }
        });

        addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                View currentView = getCurrentView();
                if (currentView != null) {
                    //LogUtils.w(currentView.getHeight() + ", " + currentView.getMeasuredHeight());
                    currentView.measure(0, 0);
                    int height = currentView.getMeasuredHeight();
                    //LogUtils.w(currentView.getHeight() + ", " + height);
                    setHeight(height);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    protected CalendarView getCalendarView() {
        return new ExpandCalendarView(getContext());
    }

    public void toggleCollapse() {
        if (mAnim != null && mAnim.isRunning()) return;

        if (mOriginHeight == 0) {
            mOriginHeight = getHeight();
            LogUtils.i(mOriginHeight + "");
        }

        if (mIsOpen) {
            mIsOpen = !mIsOpen;
            mScrollable = !mScrollable;
            int mClosedHeight = (int) getResources().getDimension(R.dimen.week_layout_height) + (int) getResources().getDimension(R.dimen.day_item_height);
            anim(getHeight(), mClosedHeight, new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    ((ExpandCalendarView) getCurrentView()).closeWithoutAnim();
                }
            });
        } else {
            mIsOpen = !mIsOpen;
            mScrollable = !mScrollable;
            anim(getHeight(), mOriginHeight, new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    ((ExpandCalendarView) getCurrentView()).openWithoutAnim();
                }
            });
        }
    }

    private void anim(int from, int to, Animator.AnimatorListener listener) {
        mAnim = ObjectAnimator.ofInt(from, to);
        mAnim.setDuration(ANIM_TIME);
        mAnim.setInterpolator(new DecelerateInterpolator());
        mAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mHeight = (int) animation.getAnimatedValue();
                requestLayout();
            }
        });
        if (listener != null) {
            mAnim.addListener(listener);
        }
        mAnim.start();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return mScrollable && super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mScrollable && super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mOriginHeight > 0 || mHeight > 0) {
            LogUtils.d(mHeight + "");
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public void setHeight(int mHeight) {
        this.mHeight = mHeight;
        this.mOriginHeight = mHeight;
        requestLayout();
    }
}
