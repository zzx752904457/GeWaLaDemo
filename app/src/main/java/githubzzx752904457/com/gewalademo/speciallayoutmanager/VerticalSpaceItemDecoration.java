package githubzzx752904457.com.gewalademo.speciallayoutmanager;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int mVerticalSpaceHeight;

    public VerticalSpaceItemDecoration(int mVerticalSpaceHeight) {
        this.mVerticalSpaceHeight = mVerticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int childIndex = parent.getChildAdapterPosition(view);
        if (childIndex != 0) {
            outRect.bottom = mVerticalSpaceHeight;
        }
    }
}