package jameson.io.calendarview.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jameson.io.calendarview.DayItem;
import jameson.io.calendarview.OnDayClickListener;
import jameson.io.calendarview.util.CalendarUtil;

/**
 * 日历ViewPager
 *
 * Created by jameson on 1/6/16.
 */
public class CalendarViewPager extends WrapContentHeightViewPager implements OnDayClickListener {
    // 最大显示10年
    private static final int COUNT = 120;
    protected List<CalendarView> mViews;
    private Calendar mCalendar = Calendar.getInstance();
    private OnDayClickListener mListener;
    private CalendarViewPagerAdapter mAdapter;

    public CalendarViewPager(Context context) {
        super(context);
        initViews();
    }

    public CalendarViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    protected void initViews() {
    }

    public void init(OnDayClickListener listener) {
        mListener = listener;
        mViews = new ArrayList<>();
        mAdapter = new CalendarViewPagerAdapter();
        setAdapter(mAdapter);
        setCurrentItem(COUNT - 1);
    }

    @NonNull
    protected CalendarView getCalendarView() {
        return new CalendarView(getContext());
    }

    @Override
    public void onDayClick(DayItem data, int position) {
        if (mListener != null) {
            mListener.onDayClick(data, position);
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
            CalendarView calendarView = (CalendarView) object;
            container.removeView(calendarView);
            mViews.remove(calendarView);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            CalendarView calendarView = getCalendarView();
            calendarView.init(CalendarUtil.getMonthsByOffset(mCalendar, position - COUNT + 1), CalendarViewPager.this);
            calendarView.setTag(position);
            container.addView(calendarView);
            mViews.add(calendarView);
            return calendarView;
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
            //refreshState();
        }
    }

    public View getCurrentView() {
        return findViewWithTag(getCurrentItem());
    }
}
