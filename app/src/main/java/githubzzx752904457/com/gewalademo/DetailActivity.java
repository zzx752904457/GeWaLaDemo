package githubzzx752904457.com.gewalademo;

import android.annotation.TargetApi;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import githubzzx752904457.com.gewalademo.adapter.BaseRecyclerAdapter;
import githubzzx752904457.com.gewalademo.adapter.InAdapter;
import githubzzx752904457.com.gewalademo.data.Constants;
import githubzzx752904457.com.gewalademo.speciallayoutmanager.AutoFitStaggeredLayoutManager;
import githubzzx752904457.com.gewalademo.speciallayoutmanager.VerticalSpaceItemDecoration;
import githubzzx752904457.com.gewalademo.utils.CircularRevealUtils;
import githubzzx752904457.com.gewalademo.utils.MeasUtils;
import githubzzx752904457.com.gewalademo.view.second.OverlapHeaderFrameLayout;
import githubzzx752904457.com.gewalademo.view.second.OverlapLinearLayout;
import githubzzx752904457.com.gewalademo.view.second.OverlapScrollView;

public class DetailActivity extends AppCompatActivity implements BaseRecyclerAdapter.OnItemClickListener {

    @Bind(R.id.recycle_in)
    RecyclerView recycleIn;
    @Bind(R.id.rlOpen)
    RelativeLayout rlOpen;
    @Bind(R.id.fl_header)
    OverlapHeaderFrameLayout flHeader;
    @Bind(R.id.ll_body)
    OverlapLinearLayout llBody;
    @Bind(R.id.scroll_root)
    OverlapScrollView scrollRoot;
    @Bind(R.id.introduce)
    LinearLayout introduce;
    @Bind(R.id.content0)
    RelativeLayout content0;
    @Bind(R.id.content1)
    FrameLayout content1;
    @Bind(R.id.content2)
    FrameLayout content2;
    @Bind(R.id.content3)
    FrameLayout content3;
    @Bind(R.id.content4)
    FrameLayout content4;
    @Bind(R.id.content5)
    FrameLayout content5;
    @Bind(R.id.tv_header)
    TextView tv_header;
    @Bind(R.id.layout_in)
    FrameLayout layout_in;
    @Bind(R.id.logo)
    ImageView logo;


    private List<Integer> mList = new ArrayList<>();
    private InAdapter mAdapter;
    private Toast toast;
    private final int[] mImageResIds = Constants.IMAGES;
    private final List<Double> mImageAspectRatios = new ArrayList<>();

    @OnClick(R.id.rlOpen)
    void rlOpen() {
        flHeader.open();
        llBody.open();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setupEnterAnimation(); // 入场动画
            setUpExitAnimation();//退出动画
        } else {
            initView();
        }
        initData();
        intListener();
    }

    private void initView() {
        scrollRoot.post(new Runnable() {
            @Override
            public void run() {
                introduce.setVisibility(View.VISIBLE);
                content0.setVisibility(View.VISIBLE);
                content1.setVisibility(View.VISIBLE);
                content2.setVisibility(View.VISIBLE);
                content3.setVisibility(View.VISIBLE);
                content4.setVisibility(View.VISIBLE);
                content5.setVisibility(View.VISIBLE);
                tv_header.setVisibility(View.VISIBLE);
            }
        });
    }

    private void intListener() {
        mAdapter.setOnItemClickListener(this);

        recycleIn.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                scrollRoot.scrollBy(dx, dy);
            }
        });
    }

    private void initData() {
        for (int x = 0; x < 500; x++) {
            mList.add(mImageResIds[x % mImageResIds.length]);
        }
        //得出图片宽高比的集合
        calculateImageAspectRatios();
        //初始化adapter
        mAdapter = new InAdapter(mList, mImageAspectRatios, this);
        //设置layoutmanager
        AutoFitStaggeredLayoutManager layoutManager = new AutoFitStaggeredLayoutManager(this, mAdapter, true, MeasUtils.dpToPx(266, this));
        recycleIn.setLayoutManager(layoutManager);
        //设置间隔
        recycleIn.addItemDecoration(new VerticalSpaceItemDecoration(3));

        recycleIn.setAdapter(mAdapter);

        //设置头布局
        View header = LayoutInflater.from(this).inflate(R.layout.layout_header, recycleIn, false);
        mAdapter.setHeaderView(header);

        scrollRoot.setBodyLayout(llBody);
        scrollRoot.setHeader(flHeader);
        flHeader.setScrollRoot(scrollRoot);
        llBody.setScrollRootHeader(flHeader);
        llBody.setInRecycleView(recycleIn);
    }

    //退出动画
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setUpExitAnimation(){
        Transition transition = TransitionInflater.from(this)
                .inflateTransition(R.transition.arc_motion);
        getWindow().setSharedElementExitTransition(transition);
    }

    // 自定义入场动画，图片会有个弧线效果
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupEnterAnimation() {
        Transition transition = TransitionInflater.from(this)
                .inflateTransition(R.transition.arc_motion);
        getWindow().setSharedElementEnterTransition(transition);
        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {

            }

            @Override
            public void onTransitionEnd(Transition transition) {
                //平移结束之后启动圆圈爆炸效果
                transition.removeListener(this);
                CircularRevealUtils.animateRevealShow(
                        DetailActivity.this,
                        scrollRoot,
                        logo,
                        logo.getWidth() / 2,
                        new CircularRevealUtils.OnRevealAnimationListener() {
                            @Override
                            public void onRevealHide() {
                            }

                            @Override
                            public void onAnimationFinished() {
                                layout_in.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onRevealShow() {
                                initView();
                            }
                        });
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });
    }

    @Override
    public void onItemClick(View itemView, int position, Object data) {
        if (toast == null) {
            toast = Toast.makeText(this, "图片" + position, Toast.LENGTH_SHORT);
        } else {
            toast.setText("图片" + position);
        }
        toast.show();
    }

    //得出每张图片宽高比的集合
    private void calculateImageAspectRatios() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        for (int i = 0; i < mImageResIds.length; i++) {
            BitmapFactory.decodeResource(getResources(), mImageResIds[i], options);
            mImageAspectRatios.add(options.outWidth / (double) options.outHeight);
        }
    }

    // 退出事件
    @Override
    public void onBackPressed() {
        //如果进入的是第二层页面，则先显示第一层页面
        if (llBody.isHide()) {
            flHeader.open();
            llBody.open();
        } else {
            CircularRevealUtils.animateRevealHide(
                    DetailActivity.this,
                    scrollRoot,
                    logo,
                    logo.getWidth() / 2,
                    new CircularRevealUtils.OnRevealAnimationListener() {
                        @Override
                        public void onRevealHide() {
                            layout_in.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationFinished() {
                            //2016/8/29 图片还原到原来的位置还会有一点卡顿，这应该是android自己的问题，格瓦拉的退出动画也没有回到原位
                            defaultBackPressed();
                            //若要自定义圆圈聚拢之后的动画，则调用下面两个方法
//                            finish();
//                            overridePendingTransition();
                        }

                        @Override
                        public void onRevealShow() {

                        }
                    });
        }

    }

    // 默认回退
    private void defaultBackPressed() {
        super.onBackPressed();
    }
}
