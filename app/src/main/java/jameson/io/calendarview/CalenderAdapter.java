package jameson.io.calendarview;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

/**
 * Created by jameson on 1/6/16.
 */
public class CalenderAdapter extends HBaseAdapter<DayItem> {
    // static保证只有一个被选中
    private static Calendar mSelectedCalendar;
    private OnDayClickListener mOnDayClickListener;
    // 超出本月的时间是否显示
    private boolean mIsOutMonthHide = false;

    public CalenderAdapter(Context context, List<DayItem> list) {
        super(context, list);
        mSelectedCalendar = Calendar.getInstance();
    }

    @Override
    public int onNewItemViewRes() {
        return R.layout.calendar_item_day;
    }

    @Override
    public void onBindItemView(View convertView, final DayItem data, final int position, ViewGroup parent) {
        TextView dayView = (TextView) convertView.findViewById(R.id.dayOfMonth);

        dayView.setText(data.getDayOfMonth() + "");
        if (!data.isInCurMonth()) {
            dayView.setTextColor(Color.BLUE);
            dayView.setVisibility(mIsOutMonthHide ? View.INVISIBLE : View.VISIBLE);
        } else {
            dayView.setTextColor(getContext().getResources().getColorStateList(R.color.calendar_date_number_text_color));
            dayView.setVisibility(View.VISIBLE);
        }

        if (isCurrentDay(data) && !isSelected(data)) {
            dayView.setTextColor(Color.RED);
        }

        dayView.setSelected(isSelected(data));
        dayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedCalendar.set(data.getYear(), data.getMonth(), data.getDayOfMonth());
                notifyDataSetChanged();
                if (mOnDayClickListener != null) {
                    mOnDayClickListener.onDayClick(data, position);
                }
            }
        });
    }

    private boolean isCurrentDay(DayItem data) {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DAY_OF_MONTH) == data.getDayOfMonth() && c.get(Calendar.MONTH) == data.getMonth() && c.get(Calendar.YEAR) == data.getYear();
    }

    private boolean isSelected(DayItem data) {
        Calendar c = mSelectedCalendar;
        return c.get(Calendar.DAY_OF_MONTH) == data.getDayOfMonth() && c.get(Calendar.MONTH) == data.getMonth() && c.get(Calendar.YEAR) == data.getYear();
    }

    public OnDayClickListener getOnDayClickListener() {
        return mOnDayClickListener;
    }

    public void setOnDayClickListener(OnDayClickListener onDayClickListener) {
        mOnDayClickListener = onDayClickListener;
    }

    public boolean isOutMonthHide() {
        return mIsOutMonthHide;
    }

    public void setIsOutMonthHide(boolean isOutMonthHide) {
        mIsOutMonthHide = isOutMonthHide;
    }

}
