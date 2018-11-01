package itc.ink.explorefuture_android.app.utils;

import android.content.Context;

import itc.ink.explorefuture_android.app.application.ExploreFutureApplication;

/**
 * Created by yangwenjiang on 2018/11/1.
 */

public class UnitConversionUtil {
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    public static int dip2px(float dpValue) {
        Context context = ExploreFutureApplication.applicationContext;
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


}
