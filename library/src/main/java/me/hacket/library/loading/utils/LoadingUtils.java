package me.hacket.library.loading.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

/**
 * Loading工具 <br/>
 *
 * @author hacket <br/>
 * @time 2016/12/12 11:38 <br/>
 * @since v1.0
 */
public class LoadingUtils {

    public static Drawable getDrawble(@NonNull Context conetxt, @DrawableRes int id) {
        return ContextCompat.getDrawable(conetxt, id);
    }

    public static int getColor(@NonNull Context conetxt, @ColorRes int id) {
        return ContextCompat.getColor(conetxt, id);
    }

    public static String getString(@NonNull Context conetxt, @StringRes int id) {
        return conetxt.getResources().getString(id);
    }

    public static int sp2px(@NonNull Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics()
                .scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int dp2px(@NonNull Context context, int dip) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    public static <T extends View> T findViewById(@NonNull View v, @IdRes int id) {
        return (T) v.findViewById(id);
    }

    @SuppressLint("NewApi")
    public static int getScreenWidth(Context context) {
        int screenWidth = 0;
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = manager.getDefaultDisplay();

        int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion >= Build.VERSION_CODES.HONEYCOMB_MR2) {//  sdk version>=API3
            Point outSize = new Point();
            defaultDisplay.getSize(outSize);
            screenWidth = outSize.x;
        } else {//  <api13
            screenWidth = defaultDisplay.getWidth();
        }
        return screenWidth;
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public static int getScreenHeight(Context context) {
        int screenHeight = 0;
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = manager.getDefaultDisplay();

        int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion >= Build.VERSION_CODES.HONEYCOMB_MR2) {//  sdk version>=API3
            Point outSize = new Point();
            defaultDisplay.getSize(outSize);
            screenHeight = outSize.y;
        } else {//  <api13
            screenHeight = defaultDisplay.getHeight();
        }
        return screenHeight;
    }

}