package me.hacket.simpleloadinglayout.widget.flowerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.hacket.simpleloadinglayout.widget.flowerview.internal.FlowerDataCalc;
import me.hacket.simpleloadinglayout.widget.flowerview.internal.FlowerViewConstant;
import me.hacket.simpleloadinglayout.widget.flowerview.internal.PetalCoordinate;

import static me.hacket.simpleloadinglayout.widget.flowerview.internal.FlowerViewConstant.Default.LOADING_RATIO;

public final class FlowerView extends View {

    private static final String TAG = FlowerViewConstant.TAG;

    private Builder mBuilder;

    private int mSize;

    private int mFinalSize;// The width when a text string is assigned. The flower is drawn by original ratio, but horizontal center at mFinalSize

    private int mPetalCount;
    private float mBackgroundCornerRadius;

    private RectF mBackgroundRect;
    private Paint mBackgroundPaint, mPetalPaint, mTextPaint;

    private List<PetalCoordinate> mPetalCoordinates;
    private int[] mPetalColors;

    private Handler mHandler;
    private int mCurrentFocusIndex;

    private String mText;
    private int mTextHeight, mTextWidth;
    private int mTextMarginTopPx;

    private boolean mIsExpandWidth;

    private Timer mTimer;
    private int mSpinCount;

    private FlowerView(Context context) {
        this(context, null, 0);
    }

    public FlowerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Builder builder = new Builder(context)
                .sizeRatio(LOADING_RATIO)
                .text(FlowerViewConstant.Default.LOADING_TEXT);
        init(builder);
    }

    public FlowerView(Builder builder) {
        super(builder.mContext);
        init(builder);
    }

    @Deprecated
    public FlowerView(
            Context context, int size, int bgColor, float bgAlpha, float bgCornerRadius
            , int petalThickness, int petalCount, float petalAlpha, float borderPadding, float centerPadding
            , int themeColor, int fadeColor
            , String text, float textSize, int textColor, float textAlpha, int textMarginTop, boolean textExpandWidth) {
        super(context);
        mHandler = new FlowerUpdateHandler(this);
        mTextMarginTopPx = textMarginTop;
        initConfig(size, bgColor, bgAlpha, bgCornerRadius,
                petalThickness, petalCount, petalAlpha, borderPadding, centerPadding, themeColor, fadeColor,
                text, textSize, textColor, textAlpha, textExpandWidth);
    }

    private void init(@NonNull Builder mBuilder) {
        this.mBuilder = mBuilder;
        mHandler = new FlowerUpdateHandler(this);
        mTextMarginTopPx = FlowerUtils.dp2px(mBuilder.mContext, mBuilder.mTextMarginTopDp);

        int size = (int) (FlowerUtils.getMinimumSideOfScreen(mBuilder.mContext) * mBuilder.mSizeRatio);
        int petalThicknessPx = FlowerUtils.dp2px(mBuilder.mContext, mBuilder.mPetalThicknessDp);
        int textSizePx = FlowerUtils.sp2px(mBuilder.mContext, mBuilder.mTextSizeSp);
        int backgroundCornerRadiusPx = FlowerUtils.sp2px(mBuilder.mContext, mBuilder.mBackgroundCornerRadiusDp);

        initConfig(size, mBuilder.mBackgroundColor, mBuilder.mBackgroundAlpha, backgroundCornerRadiusPx, petalThicknessPx
                , mBuilder.mPetalCount, mBuilder.mPetalAlpha, mBuilder.mBorderPadding, mBuilder.mCenterPadding, mBuilder.mThemeColor, mBuilder.mFadeColor
                , mBuilder.mText, textSizePx, mBuilder.mTextColor, mBuilder.mTextAlpha, mBuilder.mTextExpandWidth);
    }

    /**
     * 初始化配置
     *
     * @param size             背景大小对比屏幕宽高ratio
     * @param bgColor          背景颜色
     * @param bgAlpha          背景alpha
     * @param bgCornerRadius   背景角度
     * @param petalThicknessPx 花瓣宽度px
     * @param petalCount       花瓣总数
     * @param petalAlpha       花瓣alpha
     * @param borderPadding    borderPadding
     * @param centerPadding    centerPadding
     * @param themeColor       themeColor
     * @param fadeColor        fadeColor
     * @param text             文字
     * @param textSize         文字字体大小
     * @param textColor        文字颜色
     * @param textAlpha        文字alpha
     * @param textExpandWidth  文字textExpandWidth
     */
    private void initConfig(
            int size, int bgColor, float bgAlpha, float bgCornerRadius
            , int petalThicknessPx, int petalCount, float petalAlpha, float borderPadding, float centerPadding
            , int themeColor, int fadeColor
            , String text, float textSize, int textColor, float textAlpha, boolean textExpandWidth) {

        mIsExpandWidth = (text != null && text.length() != 0 && textExpandWidth);

        mSize = size;
        mPetalCount = petalCount;
        mBackgroundCornerRadius = bgCornerRadius;

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setColor(bgColor);
        mBackgroundPaint.setAlpha((int) (bgAlpha * 255));

        mPetalPaint = new Paint();
        mPetalPaint.setAntiAlias(true);
        mPetalPaint.setStrokeWidth(petalThicknessPx);
        mPetalPaint.setStrokeCap(Paint.Cap.ROUND);

        if (text != null && text.length() != 0) {
            mText = text;
            mTextPaint = new Paint();
            mTextPaint.setAntiAlias(true);
            mTextPaint.setColor(textColor);
            mTextPaint.setAlpha((int) (textAlpha * 255));
            mTextPaint.setTextSize(textSize);
            Rect textMeasure = new Rect();
            mTextPaint.getTextBounds(text, 0, text.length(), textMeasure);
            mTextHeight = textMeasure.bottom - textMeasure.top;
            mTextWidth = textMeasure.right - textMeasure.left;
        } else {
            mTextMarginTopPx = 0;
        }

        if (mIsExpandWidth) {
            mBackgroundRect = new RectF(0, 0, mSize + mTextHeight + mTextMarginTopPx, mSize + mTextHeight + mTextMarginTopPx);
            mFinalSize = mSize + mTextHeight + mTextMarginTopPx;
        } else {
            mBackgroundRect = new RectF(0, 0, mSize, mSize + mTextHeight + mTextMarginTopPx);
            mFinalSize = mSize;
        }

        FlowerDataCalc calc = new FlowerDataCalc(petalCount);
        mPetalCoordinates = calc.getSegmentsCoordinates(mSize, (int) (borderPadding * mSize), (int) (centerPadding * mSize), petalCount, mFinalSize);
        mPetalColors = calc.getSegmentsColors(themeColor, fadeColor, petalCount, (int) (petalAlpha * 255));
    }

    public int getFinalSize() {
        return mFinalSize;
    }

    public void updateFocusIndex(int index) {
        mCurrentFocusIndex = index;
        mHandler.sendEmptyMessage(0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mIsExpandWidth) {
            setMeasuredDimension(mSize + mTextHeight + mTextMarginTopPx, mSize + mTextHeight + mTextMarginTopPx);
        } else {
            setMeasuredDimension(mSize, mSize + mTextHeight + mTextMarginTopPx);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRoundRect(mBackgroundRect, mBackgroundCornerRadius, mBackgroundCornerRadius, mBackgroundPaint);
        PetalCoordinate coordinate;
        for (int i = 0; i < mPetalCount; i++) {
            coordinate = mPetalCoordinates.get(i);
            int index = (mCurrentFocusIndex + i) % mPetalCount;
            mPetalPaint.setColor(mPetalColors[index]);
            canvas.drawLine(coordinate.getStartX(), coordinate.getStartY(), coordinate.getEndX(), coordinate.getEndY(), mPetalPaint);
        }
        if (mText != null) {
            canvas.drawText(mText, mFinalSize / 2 - mTextWidth / 2, mSize, mTextPaint);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        long delay = (long) (1000 / mBuilder.mSpeed);
        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int result = mSpinCount % mBuilder.mPetalCount;
                if (mBuilder.mDirection == FlowerViewConstant.PetalDirect.DIRECT_CLOCKWISE) {
                    updateFocusIndex(result);
                } else {
                    updateFocusIndex(mBuilder.mPetalCount - 1 - result);
                }
                if (result == 0) {
                    mSpinCount = 1;
                } else {
                    mSpinCount++;
                }
            }
        }, delay, delay);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        mSpinCount = 0;
    }

    private static class FlowerUpdateHandler extends Handler {
        WeakReference<FlowerView> reference;

        public FlowerUpdateHandler(FlowerView flowerView) {
            reference = new WeakReference<>(flowerView);
        }

        @Override
        public void handleMessage(Message message) {
            FlowerView flowerView = reference.get();
            if (flowerView != null) {
                flowerView.invalidate();
            }
        }
    }

    public static class Builder {

        private Context mContext;

        private int mTheme = 0; // R.style.ACPLDialog;

        private float mSizeRatio = LOADING_RATIO;
        private float mBorderPadding = FlowerViewConstant.Default.PETAL_BORDER_PADDING;
        private float mCenterPadding = FlowerViewConstant.Default.PETAL_CENTER_PADDING;

        private int mBackgroundColor = FlowerViewConstant.Default.BACKGROUND_COLOR;
        private int mThemeColor = FlowerViewConstant.Default.THEME_COLOR;
        private int mFadeColor = FlowerViewConstant.Default.FADE_COLOR;

        private int mPetalCount = FlowerViewConstant.Default.PETAL_COUNT;
        private int mPetalThicknessDp = FlowerViewConstant.Default.PETAL_THICKNESS_WIDTH_DP;
        private float mPetalAlpha = FlowerViewConstant.Default.PETAL_ALPHA;

        private float mBackgroundCornerRadiusDp = FlowerViewConstant.Default.BACKGROUND_CORNER_RADIUS_DP;
        private float mBackgroundAlpha = FlowerViewConstant.Default.BACKGROUND_ALPHA;

        private int mDirection = FlowerViewConstant.PetalDirect.DIRECT_CLOCKWISE;
        private float mSpeed = FlowerViewConstant.Default.PETAL_SPEED_IN_ONE_SECOND;

        private String mText = null;
        private int mTextColor = FlowerViewConstant.Default.TEXT_COLOR;
        private float mTextAlpha = FlowerViewConstant.Default.TEXT_ALPHA;
        private float mTextSizeSp = FlowerViewConstant.Default.TEXT_SIZE_SP;
        private int mTextMarginTopDp = FlowerViewConstant.Default.TEXT_MARGINTOP_DP;
        private boolean mTextExpandWidth = FlowerViewConstant.Default.TEXT_EXPAND_WIDTH;

        public Builder(Context context) {
            mContext = context;
        }

        public Builder(Context context, int theme) {
            mContext = context;
            mTheme = theme;
        }

        /**
         * 背景宽高相对于屏幕宽高最小值的ratio
         */
        public Builder sizeRatio(float ratio) {
            mSizeRatio = ratio;
            return this;
        }

        public Builder borderPadding(float padding) {
            mBorderPadding = padding;
            return this;
        }

        public Builder centerPadding(float padding) {
            mCenterPadding = padding;
            return this;
        }

        /**
         * 设置背景颜色
         */
        public Builder bgColor(int bgColor) {
            mBackgroundColor = bgColor;
            return this;
        }

        /**
         * 设置主题颜色
         */
        public Builder themeColor(int themeColor) {
            mThemeColor = themeColor;
            return this;
        }

        /**
         * 设置fade颜色
         */
        public Builder fadeColor(int fadeColor) {
            mFadeColor = fadeColor;
            return this;
        }

        /**
         * 设置花瓣的总数，默认12
         */
        public Builder petalCount(int petalCount) {
            mPetalCount = petalCount;
            return this;
        }

        /**
         * 设置每个花瓣宽度，单位dp，默认4dp
         */
        public Builder petalThicknessDp(int thicknessDp) {
            mPetalThicknessDp = thicknessDp;
            return this;
        }

        /**
         * 设置每个花瓣alpha值
         */
        public Builder petalAlpha(float alpha) {
            mPetalAlpha = alpha;
            return this;
        }

        /**
         * 设置背景角度半径，默认10dp
         */
        public Builder bgCornerRadiusDp(float cornerRadiusDp) {
            mBackgroundCornerRadiusDp = cornerRadiusDp;
            return this;
        }

        /**
         * 设置背景alpha，默认0.5f
         */
        public Builder bgAlpha(float alpha) {
            mBackgroundAlpha = alpha;
            return this;
        }

        /**
         * 设置花瓣转动方向 ，{@linkplain me.hacket.simpleloadinglayout.widget.flowerview.internal.FlowerViewConstant.PetalDirect
         * 花瓣转动方向} ，默认顺时针
         */
        public Builder direction(int direction) {
            mDirection = direction;
            return this;
        }

        /**
         * 设置花瓣1s内转动的次数，默认10次
         */
        public Builder speed(float speed) {
            mSpeed = speed;
            return this;
        }

        /**
         * 设置提示文本
         */
        public Builder text(String text) {
            mText = text;
            return this;
        }

        /**
         * 设置文本的字体大小，单位sp
         */
        public Builder textSize(int textSizeSp) {
            mTextSizeSp = textSizeSp;
            return this;
        }

        /**
         * 设置文本颜色
         */
        public Builder textColor(int textColor) {
            mTextColor = textColor;
            return this;
        }

        /**
         * 设置文本aplha
         */
        public Builder textAlpha(float textAlpha) {
            mTextAlpha = textAlpha;
            return this;
        }

        /**
         * 设置文本的marginTop，默认40
         */
        public Builder textMarginTopDp(int textMarginTopDp) {
            mTextMarginTopDp = textMarginTopDp;
            return this;
        }

        /**
         * 设置是否扩张宽度，默认true
         */
        public Builder isTextExpandWidth(boolean isTextExpandWidth) {
            mTextExpandWidth = isTextExpandWidth;
            return this;
        }

        public FlowerView build() {
            return new FlowerView(this);
        }

    }

}
