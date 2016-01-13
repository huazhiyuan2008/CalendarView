package jameson.io.calendarview;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jameson.io.calendarview.util.LogUtils;
import jameson.io.calendarview.widget.ExpandCalendarViewPager;
import jameson.io.calendarview.widget.WrapContentHeightViewPager;

/**
 * Created by jameson on 1/11/16.
 */
public class WrapActivity extends Activity {

    private WrapContentHeightViewPager mViewPager;
    private ExpandCalendarViewPager mExpandCalendarViewPager;
    private List<View> mViews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrap);

        initView();
        initCalendarView();
    }

    private void initCalendarView() {
        mExpandCalendarViewPager = (ExpandCalendarViewPager) findViewById(R.id.expandCalendarViewPager);
        mExpandCalendarViewPager.init(null);
    }

    private void initView() {
        mViewPager = (WrapContentHeightViewPager) findViewById(R.id.viewPager);

        for (int i = 0; i < 5; i++) {
            View shortView = LayoutInflater.from(this).inflate(R.layout.view_short, null, false);
            View longView = LayoutInflater.from(this).inflate(R.layout.view_long, null, false);
            mViews.add(shortView);
            mViews.add(longView);
        }

        final MyPagerAdapter adapter = new MyPagerAdapter();
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                LogUtils.d(position + "");

                LinearLayout currentView = (LinearLayout) mViewPager.findViewWithTag(position);
                if (currentView != null) {
                    LogUtils.i(((TextView)((LinearLayout)adapter.getCurrentView()).getChildAt(0)).getText().toString());
                    LogUtils.i(((TextView) currentView.getChildAt(0)).getText().toString());
                    LogUtils.w(currentView.getMeasuredHeight() + "");
                    currentView.measure(0, 0);
                    int height = currentView.getMeasuredHeight();
                    LogUtils.w(height + "");
                    mViewPager.setHeight(height);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(0);
    }

    public class MyPagerAdapter extends PagerAdapter {
        private View mCurrentView;

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViews.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViews.get(position));
            mViews.get(position).setTag(position);
            return mViews.get(position);
        }

        @Override
        public int getCount() {
            return mViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return (view == o);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
             mCurrentView = (View) object;
            // refreshState();
        }

        public View getCurrentView() {
            return mCurrentView;
        }
    }
}
