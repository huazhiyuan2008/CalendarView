package jameson.io.calendarview.widget;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jameson.io.calendarview.CalenderAdapter;
import jameson.io.calendarview.DayItem;
import jameson.io.calendarview.util.CalendarUtil;

/**
 * 日历ViewPager
 *
 * Created by jameson on 1/6/16.
 */
public class CalendarViewPager extends WrapContentHeightViewPager implements CalenderAdapter.OnDayClickListener {
    // 最大显示10年
    private static final int COUNT = 120;
    private List<View> mViews;
    private Calendar mCalendar = Calendar.getInstance();
    private CalenderAdapter.OnDayClickListener mListener;

    public CalendarViewPager(Context context) {
        super(context);
        initViews();
    }

    public CalendarViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    private void initViews() {
    }

    public void init(CalenderAdapter.OnDayClickListener listener) {
        mListener = listener;
        mViews = new ArrayList<>(COUNT);
        for (int i = COUNT - 1; i >= 0; i--) {
            CalendarView view = new CalendarView(getContext());
            view.init(CalendarUtil.getMonthsByOffset(mCalendar, -i), this);
            mViews.add(view);
        }
        setAdapter(new CalendarViewPagerAdapter());
        setCurrentItem(COUNT - 1);
    }

    @Override
    public void onDayClick(DayItem data) {
        if (mListener != null) {
            mListener.onDayClick(data);
        }
    }

    /**
     * 刷新视图
     */
    private void refreshState() {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            view.postInvalidate();
        }
    }

    public class CalendarViewPagerAdapter extends PagerAdapter {

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViews.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViews.get(position));
            return mViews.get(position);
        }

        @Override
        public int getCount() {
            return COUNT;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return (view == o);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            // mCurrentView = (View) object;
            refreshState();
        }
    }

}
