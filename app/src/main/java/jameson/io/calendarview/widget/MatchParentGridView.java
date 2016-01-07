package jameson.io.calendarview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 如果把GridView放到一个垂直方向滚动的布局中，设置其高度属性为 wrap_content ,则该GridView的高度只有一行内容
 * <p>
 * Created by jameson on 1/7/16.
 */
public class MatchParentGridView extends GridView {
    public MatchParentGridView(Context context) {
        super(context);
    }

    public MatchParentGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MatchParentGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightSpec;

        if (getLayoutParams().height == LayoutParams.WRAP_CONTENT) {
            // The great Android "hackatlon", the love, the magic.
            // The two leftmost bits in the height measure spec have
            // a special meaning, hence we can't use them to describe height.
            heightSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        } else {
            // Any other height should be respected as is.
            heightSpec = heightMeasureSpec;
        }

        super.onMeasure(widthMeasureSpec, heightSpec);
    }

}
