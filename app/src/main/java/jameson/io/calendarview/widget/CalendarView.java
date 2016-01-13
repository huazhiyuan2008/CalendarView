package jameson.io.calendarview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jameson.io.calendarview.OnDayClickListener;
import jameson.io.calendarview.util.CalendarUtil;
import jameson.io.calendarview.CalenderAdapter;
import jameson.io.calendarview.DayItem;
import jameson.io.calendarview.R;

/**
 * 日历View
 * <p>
 * Created by jameson on 1/6/16.
 */
public class CalendarView extends LinearLayout implements OnDayClickListener {

    protected GridView mGridView;
    protected CalenderAdapter mAdapter;
    protected LinearLayout mWeekLayout;
    protected OnDayClickListener mOnDayClickListener;

    public static int FIRST_DAY_OF_WEEK = 0;
    public static final int MAX_ITEM_COUNT_6x7 = 42; // 6行7列
    public static final int MAX_ITEM_COUNT_5x7 = 35; // 5行7列
    protected List<DayItem> mList = new ArrayList<>();

    public CalendarView(Context context) {
        super(context);
        initView();
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.calendar_view_calander, this, true);
        mGridView = (GridView) rootView.findViewById(R.id.gridView);
        mWeekLayout = (LinearLayout) rootView.findViewById(R.id.week_layout);
        renderWeek();

        mAdapter = new CalenderAdapter(getContext(), mList);
        mGridView.setAdapter(mAdapter);

        initView(rootView);
    }

    protected void initView(View rootView) {
    }

    public void init(Calendar currentCalendar, OnDayClickListener onDayClickListener) {
        mOnDayClickListener = onDayClickListener;
        mAdapter.setOnDayClickListener(this);
        mList.clear();

        Calendar preC = CalendarUtil.getPreMonthCalendar(currentCalendar);
        Calendar nextC = CalendarUtil.getNextMonthCalendar(currentCalendar);

        int dayOfWeek = CalendarUtil.getWeekdayOfMonth(currentCalendar);  // 某月第一天为星期几
        int lastDaysOfMonth = currentCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);   // 上一个月的总天数
        int preLastDaysOfMonth = preC.getActualMaximum(Calendar.DAY_OF_MONTH);  // 上一个月的总天数

//        LogUtils.d("dayOfWeek=========" + dayOfWeek);

        int minCount = dayOfWeek + lastDaysOfMonth;
        int length = (minCount - 1) / 7 * 7 + 7;
        DayItem item;
        for (int i = 0; i < length; i++) {
            item = new DayItem();
            if (i < dayOfWeek) {
                // pre month
                item.setIsInCurMonth(false);
                item.setDate(preC.get(Calendar.YEAR), preC.get(Calendar.MONTH), preLastDaysOfMonth - (dayOfWeek - i) + 1);
            } else if (i < dayOfWeek + lastDaysOfMonth) {
                // current month
                item.setIsInCurMonth(true);
                item.setDate(currentCalendar.get(Calendar.YEAR), currentCalendar.get(Calendar.MONTH), i - dayOfWeek + 1);
            } else {
                // next month
                item.setIsInCurMonth(false);
                item.setDate(nextC.get(Calendar.YEAR), nextC.get(Calendar.MONTH), i - dayOfWeek - lastDaysOfMonth + 1);
            }
            mList.add(item);
        }

        mAdapter.notifyDataSetChanged();
    }

    private void renderWeek() {
        mWeekLayout.removeAllViews();
        for (int i = 0; i < 7; i++) {
            TextView weekView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.calendar_view_calendar_week_text, mWeekLayout, false);
            weekView.setText(CalendarUtil.getWeekText(i));
            mWeekLayout.addView(weekView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));
        }
    }

    @Override
    public void postInvalidate() {
        mAdapter.notifyDataSetChanged();
        super.postInvalidate();
    }

    @Override
    public void onDayClick(DayItem data, int position) {
        if (mOnDayClickListener != null) {
            mOnDayClickListener.onDayClick(data, position);
        }
    }
}
