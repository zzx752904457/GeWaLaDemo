package githubzzx752904457.com.gewalademo.speciallayoutmanager;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import githubzzx752904457.com.gewalademo.utils.MeasUtils;

/**
 * Created by 曾曌翾 on 16-08-26.
 */

public class AutoFitStaggeredLayoutSizeCalculator {
    public interface SizeCalculatorDelegate {
        double aspectRatioForIndex(int index);
    }

    private static final int DEFAULT_MAX_ROW_HEIGHT = 600;
    private static int mMaxRowHeight = DEFAULT_MAX_ROW_HEIGHT;

    private static final int INVALID_CONTENT_WIDTH = -1;
    private int mContentWidth = INVALID_CONTENT_WIDTH;

    // When in fixed height mode and the item's width is less than this percentage, don't try to
    // fit the item, overflow it to the next row and grow the existing items.
    private static final double VALID_ITEM_SLACK_THRESHOLD = 2.0 / 3.0;

    private boolean mIsFixedHeight = false;

    private SizeCalculatorDelegate mSizeCalculatorDelegate;

    private List<Size> mSizeForChildAtPosition;
    private List<Integer> mFirstChildPositionForRow;
    private List<Integer> mRowForChildPosition;

    //判断该recyclerview是否有header
    private boolean hasHeader;
    //header的高度
    private int headerHeight;
    private Context context;

    public AutoFitStaggeredLayoutSizeCalculator(Context context, SizeCalculatorDelegate sizeCalculatorDelegate, boolean hasHeader, int headerHeight) {
        mSizeCalculatorDelegate = sizeCalculatorDelegate;
        this.hasHeader = hasHeader;
        this.headerHeight = headerHeight;
        this.context = context;

        mSizeForChildAtPosition = new ArrayList<>();
        mFirstChildPositionForRow = new ArrayList<>();
        mRowForChildPosition = new ArrayList<>();
    }

    public void setContentWidth(int contentWidth) {
        if (mContentWidth != contentWidth) {
            mContentWidth = contentWidth;
            reset();
        }
    }

    public void setMaxRowHeight(int maxRowHeight) {
        if (mMaxRowHeight != maxRowHeight) {
            mMaxRowHeight = maxRowHeight;
            reset();
        }
    }

    public void setFixedHeight(boolean fixedHeight) {
        if (mIsFixedHeight != fixedHeight) {
            mIsFixedHeight = fixedHeight;
            reset();
        }
    }

    public Size sizeForChildAtPosition(int position) {
        if (position >= mSizeForChildAtPosition.size()) {
            computeChildSizesUpToPosition(position);
        }

        return mSizeForChildAtPosition.get(position);
    }

    public int getFirstChildPositionForRow(int row) {
        if (row >= mFirstChildPositionForRow.size()) {
            computeFirstChildPositionsUpToRow(row);
        }
        return mFirstChildPositionForRow.get(row);
    }

    public int getRowForChildPosition(int position) {
        if (position >= mRowForChildPosition.size()) {
            computeChildSizesUpToPosition(position);
        }

        return mRowForChildPosition.get(position);
    }

    public void reset() {
        mSizeForChildAtPosition.clear();
        mFirstChildPositionForRow.clear();
        mRowForChildPosition.clear();
    }

    private void computeFirstChildPositionsUpToRow(int row) {
        //       less alarming though.
        while (row >= mFirstChildPositionForRow.size()) {
            computeChildSizesUpToPosition(mSizeForChildAtPosition.size() + 1);
        }
    }

    private void computeChildSizesUpToPosition(int lastPosition) {
        if (mContentWidth == INVALID_CONTENT_WIDTH) {
            throw new RuntimeException("您尚未设置mContentWidth");
        }

        if (mSizeCalculatorDelegate == null) {
            throw new RuntimeException("您尚未设置mSizeCalculatorDelegate");
        }

        int firstUncomputedChildPosition = mSizeForChildAtPosition.size();
        int row = mRowForChildPosition.size() > 0
                ? mRowForChildPosition.get(mRowForChildPosition.size() - 1) + 1 : 0;

        double currentRowAspectRatio = 0.0;
        List<Double> itemAspectRatios = new ArrayList<>();
        int currentRowHeight = mIsFixedHeight ? mMaxRowHeight : Integer.MAX_VALUE;

        int currentRowWidth = 0;
        int pos = firstUncomputedChildPosition;
        //当RecyclerView有header的时候
        if (!hasHeader) {
            while (pos < lastPosition || (mIsFixedHeight ? currentRowWidth <= mContentWidth : currentRowHeight > mMaxRowHeight)) {
                double posAspectRatio = mSizeCalculatorDelegate.aspectRatioForIndex(pos);
                currentRowAspectRatio += posAspectRatio;
                itemAspectRatios.add(posAspectRatio);

                currentRowWidth = calculateWidth(currentRowHeight, currentRowAspectRatio);
                if (!mIsFixedHeight) {
                    currentRowHeight = calculateHeight(mContentWidth, currentRowAspectRatio);
                }

                boolean isRowFull = mIsFixedHeight ? currentRowWidth > mContentWidth : currentRowHeight <= mMaxRowHeight;
                if (isRowFull) {
                    int rowChildCount = itemAspectRatios.size();
                    mFirstChildPositionForRow.add(pos - rowChildCount + 1);

                    int[] itemSlacks = new int[rowChildCount];
                    if (mIsFixedHeight) {
                        itemSlacks = distributeRowSlack(currentRowWidth, rowChildCount, itemAspectRatios);

                        if (!hasValidItemSlacks(itemSlacks, itemAspectRatios)) {
                            int lastItemWidth = calculateWidth(currentRowHeight,
                                    itemAspectRatios.get(itemAspectRatios.size() - 1));
                            currentRowWidth -= lastItemWidth;
                            rowChildCount -= 1;
                            itemAspectRatios.remove(itemAspectRatios.size() - 1);

                            itemSlacks = distributeRowSlack(currentRowWidth, rowChildCount, itemAspectRatios);
                        }
                    }

                    int availableSpace = mContentWidth;
                    for (int i = 0; i < rowChildCount; i++) {
                        int itemWidth = calculateWidth(currentRowHeight, itemAspectRatios.get(i)) - itemSlacks[i];
                        itemWidth = Math.min(availableSpace, itemWidth);

                        mSizeForChildAtPosition.add(new Size(itemWidth, currentRowHeight));
                        mRowForChildPosition.add(row);

                        availableSpace -= itemWidth;
                    }

                    itemAspectRatios.clear();
                    currentRowAspectRatio = 0.0;
                    row++;
                }

                pos++;
            }
        } else {//RecyclerView没有header的时候
            while (pos < lastPosition || (mIsFixedHeight ? currentRowWidth <= mContentWidth : currentRowHeight > mMaxRowHeight)) {
                if (pos > 0) {
                    double posAspectRatio = mSizeCalculatorDelegate.aspectRatioForIndex(pos);
                    currentRowAspectRatio += posAspectRatio;
                    itemAspectRatios.add(posAspectRatio);

                    currentRowWidth = calculateWidth(currentRowHeight, currentRowAspectRatio);
                    if (!mIsFixedHeight) {
                        currentRowHeight = calculateHeight(mContentWidth, currentRowAspectRatio);
                    }

                    boolean isRowFull = mIsFixedHeight ? currentRowWidth > mContentWidth : currentRowHeight <= mMaxRowHeight;
                    if (isRowFull) {
                        int rowChildCount = itemAspectRatios.size();
                        mFirstChildPositionForRow.add(pos - rowChildCount + 1);

                        int[] itemSlacks = new int[rowChildCount];
                        if (mIsFixedHeight) {
                            itemSlacks = distributeRowSlack(currentRowWidth, rowChildCount, itemAspectRatios);

                            if (!hasValidItemSlacks(itemSlacks, itemAspectRatios)) {
                                int lastItemWidth = calculateWidth(currentRowHeight,
                                        itemAspectRatios.get(itemAspectRatios.size() - 1));
                                currentRowWidth -= lastItemWidth;
                                rowChildCount -= 1;
                                itemAspectRatios.remove(itemAspectRatios.size() - 1);

                                itemSlacks = distributeRowSlack(currentRowWidth, rowChildCount, itemAspectRatios);
                            }
                        }

                        int availableSpace = mContentWidth;
                        for (int i = 0; i < rowChildCount; i++) {
                            int itemWidth = calculateWidth(currentRowHeight, itemAspectRatios.get(i)) - itemSlacks[i];
                            itemWidth = Math.min(availableSpace, itemWidth);

                            mSizeForChildAtPosition.add(new Size(itemWidth, currentRowHeight));
                            mRowForChildPosition.add(row);

                            availableSpace -= itemWidth;
                        }

                        itemAspectRatios.clear();
                        currentRowAspectRatio = 0.0;
                        row++;
                    }
                } else {
                    //为header设置宽高
                    double posAspectRatio = mSizeCalculatorDelegate.aspectRatioForIndex(pos);
                    itemAspectRatios.add(posAspectRatio);

                    currentRowWidth = MeasUtils.getScreenWidth(context);
                    if (!mIsFixedHeight) {
                        currentRowHeight = headerHeight;;
                    }
                    int rowChildCount = itemAspectRatios.size();
                    mFirstChildPositionForRow.add(pos - rowChildCount + 1);


                    int availableSpace = mContentWidth;
                    for (int i = 0; i < rowChildCount; i++) {
                        int itemWidth = currentRowWidth;
                        itemWidth = Math.min(availableSpace, itemWidth);

                        mSizeForChildAtPosition.add(new Size(itemWidth, currentRowHeight));
                        mRowForChildPosition.add(row);

                        availableSpace -= itemWidth;
                    }

                    itemAspectRatios.clear();
                    currentRowAspectRatio = 0.0;
                    row++;
                }
                pos++;
            }
        }
    }

    private int[] distributeRowSlack(int rowWidth, int rowChildCount, List<Double> itemAspectRatios) {
        return distributeRowSlack(rowWidth - mContentWidth, rowWidth, rowChildCount, itemAspectRatios);
    }

    private int[] distributeRowSlack(int rowSlack, int rowWidth, int rowChildCount, List<Double> itemAspectRatios) {
        int itemSlacks[] = new int[rowChildCount];

        for (int i = 0; i < rowChildCount; i++) {
            double itemWidth = mMaxRowHeight * itemAspectRatios.get(i);
            itemSlacks[i] = (int) (rowSlack * (itemWidth / rowWidth));
        }

        return itemSlacks;
    }

    private boolean hasValidItemSlacks(int[] itemSlacks, List<Double> itemAspectRatios) {
        for (int i = 0; i < itemSlacks.length; i++) {
            int itemWidth = (int) (itemAspectRatios.get(i) * mMaxRowHeight);
            if (!isValidItemSlack(itemSlacks[i], itemWidth)) {
                return false;
            }
        }

        return true;
    }

    private boolean isValidItemSlack(int itemSlack, int itemWidth) {
        return (itemWidth - itemSlack) / (double) itemWidth > VALID_ITEM_SLACK_THRESHOLD;
    }

    private int calculateWidth(int itemHeight, double aspectRatio) {
        return (int) Math.ceil(itemHeight * aspectRatio);
    }

    private int calculateHeight(int itemWidth, double aspectRatio) {
        return (int) Math.ceil(itemWidth / aspectRatio);
    }
}
