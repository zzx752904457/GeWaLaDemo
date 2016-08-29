package githubzzx752904457.com.gewalademo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import githubzzx752904457.com.gewalademo.utils.MeasUtils;
import githubzzx752904457.com.gewalademo.speciallayoutmanager.AutoFitStaggeredLayoutSizeCalculator;

/**
 * Created by 曾曌翾 on 2016/8/26.
 */
public class InAdapter extends BaseRecyclerAdapter implements AutoFitStaggeredLayoutSizeCalculator.SizeCalculatorDelegate {


    private Context mContext;
    private List<Integer> resList;
    private List<Double> widthHeightList;

    public InAdapter(List<Integer> resList, List<Double> widthHeightList, Context context) {
        this.mContext = context;
        this.resList = resList;
        this.widthHeightList = widthHeightList;
        addDatas(resList);
    }

    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        return new MyHolder(imageView);
    }

    @Override
    public void onBind(RecyclerView.ViewHolder viewHolder, int realPosition, Object data) {
        if (viewHolder instanceof MyHolder) {
            MyHolder myHolder = (MyHolder) viewHolder;
            Picasso.with(mContext)
                    .load(resList.get(realPosition))
                    .into(myHolder.mImageView);
        }
    }

    @Override
    public double aspectRatioForIndex(int index) {
        if (index >= getItemCount()) return 1.0;
        //因为有header，所以第一条要返回header的宽高比
        if (index == 0){
            return MeasUtils.getScreenWidth(mContext)%MeasUtils.dpToPx(266, mContext);
        }
        return widthHeightList.get((index-1) % widthHeightList.size());
    }

    class MyHolder extends RecyclerView.ViewHolder {

        private ImageView mImageView;

        public MyHolder(ImageView itemView) {
            super(itemView);
            mImageView = itemView;
        }

    }


}

