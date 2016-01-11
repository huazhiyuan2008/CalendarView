package jameson.io.calendarview.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
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

    protected CalendarView getCalendarView() {
        return new ExpandCalendarView(getContext());
    }

    public void toggleCollapse() {
        if (mAnim != null && mAnim.isRunning()) return;

//        for (int i = 0; i < getChildCount(); i++) {
//            CollapseCalendarView view = (CollapseCalendarView) mViews.get(getCurrentItem());
//            view.toggleCollapse();
//        }

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
                    for (int i = 0; i < getChildCount(); i++) {
                        ExpandCalendarView view = (ExpandCalendarView) mViews.get(getCurrentItem());
                        view.closeWithoutAnim();
                    }
                }
            });
        } else {
            mIsOpen = !mIsOpen;
            mScrollable = !mScrollable;
            anim(getHeight(), mOriginHeight, new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    for (int i = 0; i < getChildCount(); i++) {
                        ExpandCalendarView view = (ExpandCalendarView) mViews.get(getCurrentItem());
                        view.openWithoutAnim();
                    }
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
        if (mOriginHeight > 0) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
