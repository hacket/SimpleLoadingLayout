package me.hacket.simpleloadinglayout.widget.flowerview;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.WindowManager;

/**
 * FlowerUtils <br/>
 *
 * @author hacket <br/>
 * @time 2016/12/13 16:10 <br/>
 * @since v1.0
 */
public final class FlowerUtils {

    public static int getMinimumSideOfScreen(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= 13) {
            Point size = new Point();
            display.getSize(size);
            return Math.min(size.x, size.y);
        } else {
            return Math.min(display.getWidth(), display.getHeight());
        }
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

}
