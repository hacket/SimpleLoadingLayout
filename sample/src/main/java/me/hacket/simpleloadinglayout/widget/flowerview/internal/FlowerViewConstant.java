package me.hacket.simpleloadinglayout.widget.flowerview.internal;

import android.graphics.Color;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class FlowerViewConstant {

    public static final String TAG = "FlowerView";

    @IntDef({PetalDirect.DIRECT_CLOCKWISE, PetalDirect.DIRECT_ANTI_CLOCKWISE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PetalDirect {
        /**
         * 顺时针方向
         */
        int DIRECT_CLOCKWISE = 100;
        /**
         * 逆时针方向
         */
        int DIRECT_ANTI_CLOCKWISE = 101;
    }

    public static final int PIE_AUTO_UPDATE = 200;
    public static final int PIE_MANUAL_UPDATE = 201;

    public final static class Default {
        public static final String LOADING_TEXT = null;
        // public static final String LOADING_TEXT = "正在加载";
        public static final float LOADING_RATIO = 0.2f;

        public static final float BACKGROUND_CORNER_RADIUS_DP = 8f;
        public static final float BACKGROUND_ALPHA = 0.75f;

        public static final int BACKGROUND_COLOR = Color.BLACK;
        public static final int THEME_COLOR = Color.WHITE;
        public static final int FADE_COLOR = Color.DKGRAY;

        public static final float PETAL_BORDER_PADDING = 0.55f;
        public static final float PETAL_CENTER_PADDING = 0.27f;

        public static final int PETAL_COUNT = 12;
        public static final int PETAL_THICKNESS_WIDTH_DP = 3;
        public static final float PETAL_ALPHA = 1.0f;
        public static final float PETAL_SPEED_IN_ONE_SECOND = 10f;

        public static final int TEXT_COLOR = Color.WHITE;
        public static final int TEXT_MARGINTOP_DP = 15;
        public static final float TEXT_ALPHA = 1.0f;
        public static final float TEXT_SIZE_SP = 16.0f;
        public static final boolean TEXT_EXPAND_WIDTH = true;
    }

}
