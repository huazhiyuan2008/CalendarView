package jameson.io.calendarview;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import java.util.Calendar;

import jameson.io.calendarview.util.CalendarUtil;
import jameson.io.calendarview.util.ToastUtils;
import jameson.io.calendarview.widget.CalendarViewPager;

public class MainActivity extends Activity implements CalenderAdapter.OnDayClickListener {

    private TextView mTitleView;
    private Calendar mCurrentCalendar = Calendar.getInstance();
    private int mMonthOffset;
    private CalendarViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        mViewPager = (CalendarViewPager) findViewById(R.id.viewPager);
        mTitleView = (TextView) findViewById(R.id.title);
        renderTitle(mCurrentCalendar);

        mViewPager.init(this);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mMonthOffset = position + 1 - mViewPager.getAdapter().getCount();
                renderUI();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void renderUI() {
        Calendar c = CalendarUtil.getMonthsByOffset(mCurrentCalendar, mMonthOffset);
        renderTitle(c);
    }

    private void renderTitle(Calendar c) {
        mTitleView.setText(String.format("%s-%02d", c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1));
    }

    @Override
    public void onDayClick(DayItem data) {
        ToastUtils.show(this, String.format("%s-%s-%s", data.getYear(), data.getMonth() + 1, data.getDayOfMonth()));
    }
}