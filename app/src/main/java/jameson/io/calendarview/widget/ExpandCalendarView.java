package jameson.io.calendarview.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jameson.io.calendarview.CalendarAdapter;
import jameson.io.calendarview.DayItem;
import jameson.io.calendarview.OnDayClickListener;
import jameson.io.calendarview.R;
import jameson.io.calendarview.util.CalendarUtil;
import jameson.io.calendarview.util.LogUtils;

/**
 * Created by jameson on 1/7/16.
 */
public class ExpandCalendarView extends CalendarView {
    private GridView mSelectedGridView;
    private CalendarAdapter mSelectedAdapter;
    private List<DayItem> mSelectedList = new ArrayList<>();
    private int mTopOffset = 0;
    private int mSelectedItemHeight = 0;
    private int mSelectedPos = 0;
    private boolean mIsOpen = true;
    private static final int ANIM_TIME = 300;
    private int mGridViewOriginHeight = 0;
    private ValueAnimator mCloseAnim;
    private ValueAnimator mOpenAnim;

    public ExpandCalendarView(Context context) {
        super(context);
    }

    public ExpandCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpandCalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initView(View rootView) {
//        mTopOffset = (int) getResources().getDimension(R.dimen.week_layout_height);
//        mSelectedItemHeight = (int) getResources().getDimension(R.dimen.day_item_height);
//        LogUtils.d("mTopOffset======" + mTopOffset + ", " + mSelectedItemHeight);

        mSelectedGridView = (GridView) rootView.findViewById(R.id.selected_gridView);

        mSelectedAdapter = new CalendarAdapter(getContext(), mSelectedList);
        mSelectedGridView.setAdapter(mSelectedAdapter);

        mGridView.post(new Runnable() {
            @Override
            public void run() {
                mGridViewOriginHeight = mGridView.getHeight();
            }
        });
    }

    @Override
    public void init(Calendar currentCalendar, OnDayClickListener onDayClickListener) {
        super.init(currentCalendar, onDayClickListener);
        int dayOfWeek = CalendarUtil.getWeekdayOfMonth(currentCalendar);  // 某月第一天为星期几

        mSelectedPos = dayOfWeek + currentCalendar.get(Calendar.DAY_OF_MONTH) - 1;
        renderSelectedGridView(mSelectedPos, true);
    }

    private void renderSelectedGridView(final int mSelectedPos, boolean isInit) {
        if (isInit) {
            mSelectedGridView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    renderSelectedGridView(mSelectedPos);
                }
            }, 200);
        }
    }

    private void renderSelectedGridView(final int mSelectedPos) {
        this.mSelectedPos = mSelectedPos;

        mSelectedList.clear();
        final int start = mSelectedPos / 7 * 7;
        for (int i = start; i < start + 7; i++) {
            mSelectedList.add(mList.get(i));
        }
        mSelectedAdapter.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(DayItem data, int position) {
                ExpandCalendarView.this.onDayClick(data, position + start);
            }
        });
        mSelectedAdapter.notifyDataSetChanged(mSelectedList);

        if (!mIsOpen) return;
        if (mSelectedPos - mGridView.getFirstVisiblePosition() > mGridView.getChildCount() - 1) {
            //LogUtils.w(mSelectedPos + ", " + mGridView.getFirstVisiblePosition() + ", " + mGridView.getChildCount());
            return;
        }
        View mSelectedView = mGridView.getChildAt(mSelectedPos - mGridView.getFirstVisiblePosition());
        LogUtils.d("mSelectedView.getTop()====" + mSelectedView.getTop());

        renderSelectedGridViewTop(mSelectedView.getTop());
    }

    @Override
    public void onDayClick(DayItem data, int position) {
        super.onDayClick(data, position);
        renderSelectedGridView(position);
    }

    public void toggleCollapse() {
        if ((mCloseAnim != null && mCloseAnim.isRunning()) || (mOpenAnim != null && mOpenAnim.isRunning())) return;

        if (mGridViewOriginHeight == 0) {
            mGridViewOriginHeight = mGridView.getHeight();
        }

        if (mIsOpen) {
            mSelectedItemHeight = (int) getResources().getDimension(R.dimen.day_item_height);
            animClose(mSelectedItemHeight);
        } else {
            animOpen(mGridViewOriginHeight);
        }

        mIsOpen = !mIsOpen;
    }

    private void animClose(final int to) {
        final int from = mGridView.getHeight();
        mTopOffset = (int) getResources().getDimension(R.dimen.week_layout_height);
        final int selectedBottom = mSelectedGridView.getBottom() - mTopOffset;
        final int selectedHeight = mSelectedGridView.getHeight();
        LogUtils.d(String.format("animClose======from=%s, to=%s, selectedBottom=%s, selectedHeight=%s", from, to, selectedBottom, selectedHeight));
        mCloseAnim = ObjectAnimator.ofInt(from, to);
        mCloseAnim.setDuration(ANIM_TIME);
        mCloseAnim.setInterpolator(new DecelerateInterpolator());
        mCloseAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                if (value <= selectedBottom) {
                    renderSelectedGridViewTop(value - to);
                }
                setGridViewHeight(value);
            }
        });
        mCloseAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                closeWithoutAnim();
            }
        });
        mCloseAnim.start();
    }

    private void animOpen(int to) {
        final int from = mGridView.getHeight();
        mOpenAnim = ObjectAnimator.ofInt(from, to);
        mOpenAnim.setDuration(ANIM_TIME);
        mCloseAnim.setInterpolator(new DecelerateInterpolator());
        LogUtils.d(String.format("animOpen*******from=%s, to=%s", from, to));
        mOpenAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                setGridViewHeight(value);
            }
        });
        mOpenAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mSelectedGridView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                openWithoutAnim();
            }
        });
        mOpenAnim.start();
    }

    private void setGridViewHeight(int value) {
        mGridView.getLayoutParams().height = value;
        mGridView.requestLayout();
    }

    private void renderSelectedGridViewTop(int top) {
        mTopOffset = 0;//(int) getResources().getDimension(R.dimen.week_layout_height);
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mSelectedGridView.getLayoutParams();
        lp.topMargin = top + mTopOffset;
        mSelectedGridView.setLayoutParams(lp);
    }

    public void openWithoutAnim() {
        mIsOpen = true;
        mSelectedGridView.setVisibility(View.INVISIBLE);
//        setGridViewHeight(mGridViewOriginHeight);
    }

    public void closeWithoutAnim() {
        mIsOpen = false;
        // renderSelectedGridView(mSelectedPos);
        renderSelectedGridViewTop(0);
        mSelectedGridView.setVisibility(View.VISIBLE);
        ObjectAnimator anim = ObjectAnimator.ofFloat(mSelectedGridView, "alpha", 0f, 1f);
        anim.start();
//        mSelectedItemHeight = (int) getResources().getDimension(R.dimen.day_item_height);
//        setGridViewHeight(mSelectedItemHeight);
    }
}
