package githubzzx752904457.com.gewalademo.view.second;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * Created by 曾曌翾 on 16-08-26.
 */
public class OverlapHeaderFrameLayout extends FrameLayout {

  private Scroller mScroller;
  private boolean mHide;
  private OverlapScrollView mScrollRoot;

  public OverlapHeaderFrameLayout(Context context) {
    this(context, null);
  }

  public OverlapHeaderFrameLayout(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public OverlapHeaderFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  public boolean isHide() {
    return mHide;
  }

  public void setIsHide(boolean isHide) {
    mHide = isHide;
  }

  private void init() {
    mScroller = new Scroller(getContext());
  }

  public void open() {
    if (!mHide) {
      return;
    }
    //如果ScrollView跟随RecycleView移动大于Header 0.7距离,让ScrollView回到顶部
    mScrollRoot.scrollTo(0, 0);
    //做Header Open动画
    scrollTo(0, getMeasuredHeight());
    mScroller.startScroll(0, getMeasuredHeight(), 0, -getMeasuredHeight(), 1000);
    invalidate();
  }

  @Override public void computeScroll() {
    if (mScroller.computeScrollOffset()) {
      scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
      postInvalidate();
    }
  }

  public void setScrollRoot(OverlapScrollView scrollRoot) {
    mScrollRoot = scrollRoot;
  }
}
