package githubzzx752904457.com.gewalademo.view.second;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by 曾曌翾 on 16-08-26.
 */
public class OverlapScrollView extends ScrollView {

    private int mDownY;
    private int mMoveY;
    private OverlapHeaderFrameLayout mHeader;
    private OverlapLinearLayout mBodyLayout;

    public OverlapScrollView(Context context) {
        this(context, null);
    }

    public OverlapScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OverlapScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        //如果滑动大于Header 0.7高度，可以做open动画
        if (t >= mHeader.getMeasuredHeight() * 0.7
                && mBodyLayout.getCurrentState() == mBodyLayout.STATE_DOWN) {
            mHeader.setIsHide(true);
        } else {
            mHeader.setIsHide(false);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownY = (int) ev.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                mMoveY = (int) ev.getRawY();
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mDownY = 0;
                mMoveY = 0;
                break;
        }

        int diffY = mDownY - mMoveY;
        if (diffY > 0) {//向上滑动,ScrollView处理事件
            return super.onInterceptTouchEvent(ev);
        }

        //ScrollView处于顶部并向下滑动,body布局为显示状态，事件需要给body布局
        if (getScrollY() == 0 && mBodyLayout.getCurrentState() == OverlapLinearLayout.STATE_UP) {
            return false;
        }

        //ScrollView处于顶部并向下滑动,body布局为移动状态，事件需要给body布局
        if (getScrollY() == 0 && mBodyLayout.getCurrentState() == OverlapLinearLayout.STATE_MOVE) {
            return false;
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //body布局隐藏不处理
        if (mBodyLayout.getCurrentState() == OverlapLinearLayout.STATE_DOWN) {
            return false;
        }
        return super.onTouchEvent(ev);
    }

    public void setBodyLayout(OverlapLinearLayout bodyLayout) {
        mBodyLayout = bodyLayout;
    }

    public void setHeader(OverlapHeaderFrameLayout header) {
        mHeader = header;
    }
}
