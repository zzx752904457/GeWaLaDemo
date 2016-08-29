package githubzzx752904457.com.gewalademo.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

/**
 * Created by 曾曌翾 on 16-08-26.
 */
public class MeasUtils {
    public static int pxToDp(int px, Context context) {
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px,
                context.getResources().getDisplayMetrics());
    }

    public static int dpToPx(float dp, Context context) {
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }
}
