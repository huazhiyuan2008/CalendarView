package jameson.io.calendarview;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import jameson.io.calendarview.util.LogUtils;
import jameson.io.calendarview.util.ScreenUtil;
import jameson.io.calendarview.widget.MyViewPager;

/**
 * Created by jameson on 1/12/16.
 */
public class MeasureAnimActivity extends Activity {

    private MyViewPager mViewPager;
    private List<View> mViews = new ArrayList<>();
    private View mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure_anim);

        initView();
    }

    private void initView() {
        mViewPager = (MyViewPager) findViewById(R.id.viewPager);
        mView = findViewById(R.id.view);

        for (int i = 0; i < 5; i++) {
            View shortView = LayoutInflater.from(this).inflate(R.layout.view_short, null, false);
            View longView = LayoutInflater.from(this).inflate(R.layout.view_long, null, false);
            mViews.add(longView);
            mViews.add(shortView);
        }

        final MyPagerAdapter adapter = new MyPagerAdapter();
        mViewPager.setAdapter(adapter);
    }

    private boolean mIsOpen = true;

    public void toggleMeasure(View view) {
        mView.setVisibility(View.GONE);
        View animView = mViewPager;
        mIsOpen = !mIsOpen;
        int from = animView.getHeight();
        if (mIsOpen) {
            animMeasure(animView, from, ScreenUtil.dip2px(this, 400));
        } else {
            animMeasure(animView, from, ScreenUtil.dip2px(this, 50));
        }
    }

    public void toggle(View view) {
        mView.setVisibility(View.GONE);
        View animView = mViewPager;
        mIsOpen = !mIsOpen;
        int from = animView.getHeight();
        if (mIsOpen) {
            anim(animView, from, ScreenUtil.dip2px(this, 300));
        } else {
            anim(animView, from, ScreenUtil.dip2px(this, 50));
        }
    }

    int mCount = 0;
    private void anim(final View view, int from, int to) {
        mViewPager.setHeight(0);
        mCount = 0;
        final ValueAnimator anim = ObjectAnimator.ofInt(from, to);
        anim.setDuration(300);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
                lp.height = value;
                view.setLayoutParams(lp);
                LogUtils.w(++mCount + "");
            }
        });

        anim.start();
    }

    private void animMeasure(final View view, int from, int to) {
        mCount = 0;
        final ValueAnimator anim = ObjectAnimator.ofInt(from, to);
        anim.setDuration(300);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                mViewPager.setHeight(value);
                LogUtils.w(++mCount + "");
            }
        });

        anim.start();
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
