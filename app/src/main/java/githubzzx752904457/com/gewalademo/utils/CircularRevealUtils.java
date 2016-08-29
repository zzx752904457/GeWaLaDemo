package githubzzx752904457.com.gewalademo.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * 动画效果
 * <p/>
 * Created by 曾曌翾 on 16/2/26.
 */
public class CircularRevealUtils {
    public interface OnRevealAnimationListener {
        void onRevealHide();

        void onAnimationFinished();

        void onRevealShow();
    }

    // 圆圈爆炸效果显示
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void animateRevealShow(
            Context context,
            View view,
            View centerView,
            int startRadius,//初始化的半径
            final OnRevealAnimationListener listener) {
        int cx = (int)(centerView.getX()+ centerView.getWidth()/2);
        int cy = MeasUtils.dpToPx(266, context);

        float finalRadius = 0;
        if (MeasUtils.getScreenWidth(context) > MeasUtils.getScreenHeight(context)) {
            finalRadius = MeasUtils.getScreenWidth(context);
        } else {
            finalRadius = MeasUtils.getScreenHeight(context);
        }

        // 设置圆形显示动画
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, startRadius, finalRadius);
        anim.setDuration(600);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                listener.onAnimationFinished();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                listener.onRevealShow();
            }
        });

        anim.start();
    }

    // 圆圈凝聚效果
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void animateRevealHide(
            Context context,
            View view,
            View centerView,
            int finalRadius,//最后的半径
            final OnRevealAnimationListener listener
    ) {
//        int cx = (centerView.getLeft() + centerView.getRight()) / 2;
//        int cy = (centerView.getTop() + centerView.getBottom()) / 2;
        int cx = (int)(centerView.getX()+ centerView.getWidth()/2);
        int cy = MeasUtils.dpToPx(266, context);
        float initialRadius = 0;
        if (MeasUtils.getScreenWidth(context) > MeasUtils.getScreenHeight(context)) {
            initialRadius = MeasUtils.getScreenWidth(context);
        } else {
            initialRadius = MeasUtils.getScreenHeight(context);
        }
        // 与入场动画的区别就是圆圈起始和终止的半径相反
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, finalRadius);
        anim.setDuration(600);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                listener.onRevealHide();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                listener.onAnimationFinished();
            }
        });
        anim.start();
    }
}
