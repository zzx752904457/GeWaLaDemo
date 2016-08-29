package githubzzx752904457.com.gewalademo.speciallayoutmanager;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by 曾曌翾 on 16-08-26.
 */
public class SurroundingSpacingItemDecoration extends RecyclerView.ItemDecoration {
    private static final String TAG = SurroundingSpacingItemDecoration.class.getName();

    public static int DEFAULT_SPACING = 64;
    private int mSpacing;

    public SurroundingSpacingItemDecoration() {
        this(DEFAULT_SPACING);
    }

    public SurroundingSpacingItemDecoration(int spacing) {
        mSpacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (!(parent.getLayoutManager() instanceof AutoFitStaggeredLayoutManager)) {
            throw new IllegalArgumentException(String.format("The %s must be used with a %s",
                    SurroundingSpacingItemDecoration.class.getSimpleName(),
                    AutoFitStaggeredLayoutManager.class.getSimpleName()));
        }

        final AutoFitStaggeredLayoutManager layoutManager = (AutoFitStaggeredLayoutManager) parent.getLayoutManager();

        int childIndex = parent.getChildAdapterPosition(view);
        if (childIndex == RecyclerView.NO_POSITION) return;

        outRect.top    = 0;
        outRect.bottom = mSpacing;
        outRect.left   = 0;
        outRect.right  = mSpacing;

        if (isTopChild(childIndex, layoutManager)) {
            outRect.top = mSpacing;
        }

        if (isLeftChild(childIndex, layoutManager)) {
            outRect.left = mSpacing;
        }
    }

    private static boolean isTopChild(int position, AutoFitStaggeredLayoutManager layoutManager) {
        boolean isFirstViewHeader = layoutManager.isFirstViewHeader();
        if (isFirstViewHeader && position == AutoFitStaggeredLayoutManager.HEADER_POSITION) {
            return true;
        } else if (isFirstViewHeader && position > AutoFitStaggeredLayoutManager.HEADER_POSITION) {
            position -= 1;
        }

        final AutoFitStaggeredLayoutSizeCalculator sizeCalculator = layoutManager.getSizeCalculator();
        return sizeCalculator.getRowForChildPosition(position) == 0;
    }

    private static boolean isLeftChild(int position, AutoFitStaggeredLayoutManager layoutManager) {
        boolean isFirstViewHeader = layoutManager.isFirstViewHeader();
        if (isFirstViewHeader && position == AutoFitStaggeredLayoutManager.HEADER_POSITION) {
            return true;
        } else if (isFirstViewHeader && position > AutoFitStaggeredLayoutManager.HEADER_POSITION) {
            position -= 1;
        }

        final AutoFitStaggeredLayoutSizeCalculator sizeCalculator = layoutManager.getSizeCalculator();
        int rowForPosition = sizeCalculator.getRowForChildPosition(position);
        return sizeCalculator.getFirstChildPositionForRow(rowForPosition) == position;
    }
}
