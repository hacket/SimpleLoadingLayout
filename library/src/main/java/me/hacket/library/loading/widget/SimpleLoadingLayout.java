package me.hacket.library.loading.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import me.hacket.library.R;
import me.hacket.library.loading.ILoadingStatus;
import me.hacket.library.loading.utils.LoadingStatus;
import me.hacket.library.loading.utils.LoadingUtils;

/**
 * SimpleLoadingLayout <br/>
 *
 * @author hacket <br/>
 * @time 2016/12/12 11:39 <br/>
 * @since v1.0
 */
public class SimpleLoadingLayout extends FrameLayout implements ILoadingStatus {

    private static final String TAG = LoadingConfig.TAG;

    private static final int DEFAULT_LAYOUT_IDS_VALUE = 0;

    private Context mContext;
    @LoadingStatus
    private int mCurrentState = -1;

    private View mLoadingPageView;
    private View mErrorPageView;
    private View mEmptyPageView;
    private View mNoNetworkPageView;

    private ImageView mIvErrorImg;
    private ImageView mIvEmptyImg;
    private ImageView mIvNetworkImg;

    private TextView mTvErrorText;
    private TextView mTvEmptyText;
    private TextView mTvNetworkText;

    private TextView mTvErrorReloadBtn;
    private TextView mTvNetworkReloadBtn;

    private View mContentView;

    private OnReloadListener mOnReloadListener;
    /**
     * 是否一开始显示contentview，默认不显示
     */
    private boolean isFirstVisible;

    LoadingConfig mLoadingConfig;

    @LayoutRes
    int mEmptyViewLayoutResId;
    @LayoutRes
    int mErrorViewLayoutResId;
    @LayoutRes
    int mLoadingViewLayoutResId;
    @LayoutRes
    int mNoNetworkViewLayoutResId;

    private LayoutInflater mLayoutInflater;

    private boolean isInterceptTouchEvent = false;

    public SimpleLoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        parseAttrs(attrs);
        initViews();
    }

    public SimpleLoadingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        parseAttrs(attrs);
        initViews();
    }

    public SimpleLoadingLayout(Context context) {
        super(context);
        init(context);
        initViews();
    }

    private void init(Context context) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mLoadingConfig = LoadingConfig.getInstance(mContext);
    }

    private void parseAttrs(AttributeSet attrs) {
        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.SimpleLoadingLayout);
        try {
            isFirstVisible = a.getBoolean(R.styleable.SimpleLoadingLayout_ll_isFirstVisible, false);
            mLoadingViewLayoutResId = a.getResourceId(R.styleable.SimpleLoadingLayout_ll_loading_view, DEFAULT_LAYOUT_IDS_VALUE);
            mEmptyViewLayoutResId = a.getResourceId(R.styleable.SimpleLoadingLayout_ll_empty_view, DEFAULT_LAYOUT_IDS_VALUE);
            mErrorViewLayoutResId = a.getResourceId(R.styleable.SimpleLoadingLayout_ll_error_view, DEFAULT_LAYOUT_IDS_VALUE);
            mNoNetworkViewLayoutResId = a.getResourceId(R.styleable.SimpleLoadingLayout_ll_no_network_view, DEFAULT_LAYOUT_IDS_VALUE);
        } finally {
            a.recycle();
        }
    }

    private void initViews() {

        if (mLoadingViewLayoutResId == DEFAULT_LAYOUT_IDS_VALUE) {
            mLoadingViewLayoutResId = mLoadingConfig.loadingViewLayoutResId;
        }
        if (mEmptyViewLayoutResId == DEFAULT_LAYOUT_IDS_VALUE) {
            mEmptyViewLayoutResId = mLoadingConfig.emptyViewLayoutResId;
        }
        if (mErrorViewLayoutResId == DEFAULT_LAYOUT_IDS_VALUE) {
            mErrorViewLayoutResId = mLoadingConfig.errorViewLayoutResId;
        }
        if (mNoNetworkViewLayoutResId == DEFAULT_LAYOUT_IDS_VALUE) {
            mNoNetworkViewLayoutResId = mLoadingConfig.noNetworkViewLayoutResId;
        }

        // loading
        if (mLoadingViewLayoutResId == DEFAULT_LAYOUT_IDS_VALUE) {
            mLoadingPageView = mLayoutInflater.inflate(R.layout.loading_widget_loading_page, this, false);
            mLoadingPageView.setBackgroundColor(LoadingUtils.getColor(mContext, mLoadingConfig.backgroundColor));
        } else {
            mLoadingPageView = mLayoutInflater.inflate(mLoadingViewLayoutResId, this, false);
        }

        // empty
        if (mEmptyViewLayoutResId == DEFAULT_LAYOUT_IDS_VALUE) {
            mEmptyPageView = mLayoutInflater.inflate(R.layout.loading_widget_empty_page, this, false);
            mEmptyPageView.setBackgroundColor(LoadingUtils.getColor(mContext, mLoadingConfig.backgroundColor));

            mTvEmptyText = LoadingUtils.findViewById(mEmptyPageView, R.id.empty_text);
            mTvEmptyText.setText(mLoadingConfig.emptyStr);
            mTvEmptyText.setTextSize(mLoadingConfig.tipTextSize);
            mTvEmptyText.setTextColor(LoadingUtils.getColor(mContext, mLoadingConfig.tipTextColor));

            mIvEmptyImg = LoadingUtils.findViewById(mEmptyPageView, R.id.empty_img);
            mIvEmptyImg.setImageResource(mLoadingConfig.emptyImgId);
        } else {
            mEmptyPageView = mLayoutInflater.inflate(mEmptyViewLayoutResId, this, false);
        }

        // error
        if (mErrorViewLayoutResId == DEFAULT_LAYOUT_IDS_VALUE) {
            mErrorPageView = mLayoutInflater.inflate(R.layout.loading_widget_error_page, this, false);
            mErrorPageView.setBackgroundColor(LoadingUtils.getColor(mContext, mLoadingConfig.backgroundColor));

            mTvErrorText = LoadingUtils.findViewById(mErrorPageView, R.id.error_text);
            mTvErrorText.setText(mLoadingConfig.errorStr);
            mTvErrorText.setTextSize(mLoadingConfig.tipTextSize);
            mTvErrorText.setTextColor(LoadingUtils.getColor(mContext, mLoadingConfig.tipTextColor));

            mIvErrorImg = LoadingUtils.findViewById(mErrorPageView, R.id.error_img);
            mIvErrorImg.setImageResource(mLoadingConfig.errorImgId);

            mTvErrorReloadBtn = LoadingUtils.findViewById(mErrorPageView, R.id.error_reload_btn);
            mTvErrorReloadBtn.setBackgroundResource(mLoadingConfig.reloadBtnId);
            mTvErrorReloadBtn.setText(mLoadingConfig.reloadBtnStr);
            mTvErrorReloadBtn.setTextSize(mLoadingConfig.buttonTextSize);
            mTvErrorReloadBtn.setTextColor(LoadingUtils.getColor(mContext, mLoadingConfig.buttonTextColor));
            if (mLoadingConfig.buttonHeight != -1) {
                mTvErrorReloadBtn.setHeight(LoadingUtils.dp2px(mContext, mLoadingConfig.buttonHeight));
            }
            if (mLoadingConfig.buttonWidth != -1) {
                mTvErrorReloadBtn.setWidth(LoadingUtils.dp2px(mContext, mLoadingConfig.buttonWidth));
            }
            mTvErrorReloadBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnReloadListener != null) {
                        mOnReloadListener.onReload(v, ReloadBtnType.RELOAD_BTN_TYPE_ERROR);
                    }
                }
            });
        } else {
            mErrorPageView = mLayoutInflater.inflate(mErrorViewLayoutResId, this, false);
        }

        // no_network
        if (mNoNetworkViewLayoutResId == DEFAULT_LAYOUT_IDS_VALUE) {
            mNoNetworkPageView = mLayoutInflater.inflate(R.layout.loading_widget_nonetwork_page, this, false);
            mNoNetworkPageView.setBackgroundColor(LoadingUtils.getColor(mContext, mLoadingConfig.backgroundColor));

            mTvNetworkText = LoadingUtils.findViewById(mNoNetworkPageView, R.id.no_network_text);
            mTvNetworkText.setText(mLoadingConfig.netwrokStr);
            mTvNetworkText.setTextSize(mLoadingConfig.tipTextSize);
            mTvNetworkText.setTextColor(LoadingUtils.getColor(mContext, mLoadingConfig.tipTextColor));

            mIvNetworkImg = LoadingUtils.findViewById(mNoNetworkPageView, R.id.no_network_img);
            mIvNetworkImg.setImageResource(mLoadingConfig.networkImgId);

            mTvNetworkReloadBtn = LoadingUtils.findViewById(mNoNetworkPageView, R.id.no_network_reload_btn);
            mTvNetworkReloadBtn.setBackgroundResource(mLoadingConfig.reloadBtnId);
            mTvNetworkReloadBtn.setText(mLoadingConfig.reloadBtnStr);
            mTvNetworkReloadBtn.setTextSize(mLoadingConfig.buttonTextSize);
            mTvNetworkReloadBtn.setTextColor(LoadingUtils.getColor(mContext, mLoadingConfig.buttonTextColor));
            if (mLoadingConfig.buttonHeight != -1) {
                mTvNetworkReloadBtn.setHeight(LoadingUtils.dp2px(mContext, mLoadingConfig.buttonHeight));
            }
            if (mLoadingConfig.buttonWidth != -1) {
                mTvNetworkReloadBtn.setWidth(LoadingUtils.dp2px(mContext, mLoadingConfig.buttonWidth));
            }
            mTvNetworkReloadBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnReloadListener != null) {
                        mOnReloadListener.onReload(v, ReloadBtnType.RELOAD_BTN_TYPE_NO_NETWORK);
                    }
                }
            });
        } else {
            mNoNetworkPageView = mLayoutInflater.inflate(mNoNetworkViewLayoutResId, this, false);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 1) {
            throw new IllegalStateException("LoadingLayout can host only one direct child");
        }
        mContentView = this.getChildAt(0);

        addAllViews();

        boolean inEditMode = isInEditMode();
        if (!isFirstVisible && !inEditMode) {
            onReset();
        } else {
            onSucceed();
        }
    }

    private void addAllViews() {
        this.bindView(mLoadingPageView);
        this.bindView(mNoNetworkPageView);
        this.bindView(mEmptyPageView);
        this.bindView(mErrorPageView);
    }

    private void bindView(View view) {
        addView(view, buildCenterLayoutParams());
    }

    private void bindView(View view, LayoutParams params) {
        if (view == null) {
            return;
        }
        if (view.getParent() != null) {
            this.removeView(view);
        }
        if (params == null) {
            params = buildCenterLayoutParams();
        }
        addView(view, params);
    }

    public void setStatus(@LoadingStatus int status) {

        if (mCurrentState == status) {
            return;
        }

        this.mCurrentState = status;

        switch (status) {
            case LoadingStatus.Succeed:
                isInterceptTouchEvent = false;
                mContentView.setVisibility(View.VISIBLE);
                mEmptyPageView.setVisibility(View.INVISIBLE);
                mErrorPageView.setVisibility(View.INVISIBLE);
                mNoNetworkPageView.setVisibility(View.INVISIBLE);
                mLoadingPageView.setVisibility(View.INVISIBLE);
                break;
            case LoadingStatus.Loading:
                isInterceptTouchEvent = true;
                mContentView.setVisibility(View.INVISIBLE);
                mEmptyPageView.setVisibility(View.INVISIBLE);
                mErrorPageView.setVisibility(View.INVISIBLE);
                mNoNetworkPageView.setVisibility(View.INVISIBLE);
                mLoadingPageView.setVisibility(View.VISIBLE);
                break;
            case LoadingStatus.Empty:
                isInterceptTouchEvent = false;
                mContentView.setVisibility(View.INVISIBLE);
                mEmptyPageView.setVisibility(View.VISIBLE);
                mErrorPageView.setVisibility(View.INVISIBLE);
                mNoNetworkPageView.setVisibility(View.INVISIBLE);
                mLoadingPageView.setVisibility(View.INVISIBLE);
                break;
            case LoadingStatus.Error:
                isInterceptTouchEvent = false;
                mContentView.setVisibility(View.INVISIBLE);
                mLoadingPageView.setVisibility(View.INVISIBLE);
                mEmptyPageView.setVisibility(View.INVISIBLE);
                mErrorPageView.setVisibility(View.VISIBLE);
                mNoNetworkPageView.setVisibility(View.INVISIBLE);
                break;
            case LoadingStatus.NoNetworkError:
                isInterceptTouchEvent = false;
                mContentView.setVisibility(View.INVISIBLE);
                mLoadingPageView.setVisibility(View.INVISIBLE);
                mEmptyPageView.setVisibility(View.INVISIBLE);
                mErrorPageView.setVisibility(View.INVISIBLE);
                mNoNetworkPageView.setVisibility(View.VISIBLE);
                break;
            case LoadingStatus.Reqeusting:
                isInterceptTouchEvent = true;
                mContentView.setVisibility(View.VISIBLE);
                mLoadingPageView.setVisibility(View.VISIBLE);
                mEmptyPageView.setVisibility(View.INVISIBLE);
                mErrorPageView.setVisibility(View.INVISIBLE);
                mNoNetworkPageView.setVisibility(View.INVISIBLE);
                break;
            case LoadingStatus.Reset:
            default:
                isInterceptTouchEvent = false;
                mContentView.setVisibility(View.INVISIBLE);
                mLoadingPageView.setVisibility(View.INVISIBLE);
                mEmptyPageView.setVisibility(View.INVISIBLE);
                mErrorPageView.setVisibility(View.INVISIBLE);
                mNoNetworkPageView.setVisibility(View.INVISIBLE);
                break;
        }
    }

    /**
     * 返回当前状态 {@link LoadingStatus}
     */
    @LoadingStatus
    public int getCurrentStatus() {
        return mCurrentState;
    }

    /**
     * 设置Empty状态提示文本，仅对当前所在的地方有效
     */
    public SimpleLoadingLayout setEmptyText(String text) {
        mTvEmptyText.setText(text);
        return this;
    }

    /**
     * 设置Error状态提示文本，仅对当前所在的地方有效
     */
    public SimpleLoadingLayout setErrorText(String text) {
        mTvErrorText.setText(text);
        return this;
    }

    /**
     * 设置No_Network状态提示文本，仅对当前所在的地方有效
     */
    public SimpleLoadingLayout setNoNetworkText(String text) {
        mTvNetworkText.setText(text);
        return this;
    }

    /**
     * 设置Empty状态显示图片，仅对当前所在的地方有效
     */
    public SimpleLoadingLayout setEmptyImage(@DrawableRes int id) {
        mIvEmptyImg.setImageResource(id);
        return this;
    }

    /**
     * 设置Error状态显示图片，仅对当前所在的地方有效
     */
    public SimpleLoadingLayout setErrorImage(@DrawableRes int id) {
        mIvErrorImg.setImageResource(id);
        return this;
    }

    /**
     * 设置No_Network状态显示图片，仅对当前所在的地方有效
     */
    public SimpleLoadingLayout setNoNetworkImage(@DrawableRes int id) {
        mIvNetworkImg.setImageResource(id);
        return this;
    }

    /**
     * 设置Empty状态提示文本的字体大小，仅对当前所在的地方有效
     */
    public SimpleLoadingLayout setEmptyTextSize(int sp) {
        mTvEmptyText.setTextSize(sp);
        return this;
    }

    /**
     * 设置Error状态提示文本的字体大小，仅对当前所在的地方有效
     */
    public SimpleLoadingLayout setErrorTextSize(int sp) {
        mTvErrorText.setTextSize(sp);
        return this;
    }

    /**
     * 设置No_Network状态提示文本的字体大小，仅对当前所在的地方有效
     */
    public SimpleLoadingLayout setNoNetworkTextSize(int sp) {
        mTvNetworkText.setTextSize(sp);
        return this;
    }

    /**
     * 设置Empty状态图片的显示与否，仅对当前所在的地方有效
     */
    public SimpleLoadingLayout setEmptyImageVisible(boolean bool) {
        if (bool) {
            mIvEmptyImg.setVisibility(View.VISIBLE);
        } else {
            mIvEmptyImg.setVisibility(View.INVISIBLE);
        }
        return this;
    }

    /**
     * 设置Error状态图片的显示与否，仅对当前所在的地方有效
     */
    public SimpleLoadingLayout setErrorImageVisible(boolean bool) {
        if (bool) {
            mIvErrorImg.setVisibility(View.VISIBLE);
        } else {
            mIvErrorImg.setVisibility(View.INVISIBLE);
        }
        return this;
    }

    /**
     * 设置No_Network状态图片的显示与否，仅对当前所在的地方有效
     */
    public SimpleLoadingLayout setNoNetworkImageVisible(boolean bool) {
        if (bool) {
            mIvNetworkImg.setVisibility(View.VISIBLE);
        } else {
            mIvNetworkImg.setVisibility(View.INVISIBLE);
        }
        return this;
    }

    /**
     * 设置ReloadButton的文本，仅对当前所在的地方有效
     */
    public SimpleLoadingLayout setReloadButtonText(String text) {
        mTvErrorReloadBtn.setText(text);
        mTvNetworkReloadBtn.setText(text);
        return this;
    }

    /**
     * 设置ReloadButton的文本字体大小，仅对当前所在的地方有效
     */
    public SimpleLoadingLayout setReloadButtonTextSize(int sp) {
        mTvErrorReloadBtn.setTextSize(sp);
        mTvNetworkReloadBtn.setTextSize(sp);
        return this;
    }

    /**
     * 设置ReloadButton的文本颜色，仅对当前所在的地方有效
     */
    public SimpleLoadingLayout setReloadButtonTextColor(@ColorRes int id) {
        mTvErrorReloadBtn.setTextColor(LoadingUtils.getColor(mContext, id));
        mTvNetworkReloadBtn.setTextSize(LoadingUtils.getColor(mContext, id));
        return this;
    }

    /**
     * 设置ReloadButton的背景，仅对当前所在的地方有效
     */
    public SimpleLoadingLayout setReloadButtonBackgroundResource(@DrawableRes int id) {
        mTvErrorReloadBtn.setBackgroundResource(id);
        mTvNetworkReloadBtn.setBackgroundResource(id);
        return this;
    }

    public SimpleLoadingLayout setLoadingViewLayout(@LayoutRes int loadingLayoutId) {
        this.mLoadingViewLayoutResId = loadingLayoutId;
        setLoadingView(mLayoutInflater.inflate(mLoadingViewLayoutResId, this, false));
        return this;
    }

    public SimpleLoadingLayout setLoadingView(@NonNull View loadingPageView) {
        this.removeView(mLoadingPageView);
        mLoadingPageView = loadingPageView;
        bindView(mLoadingPageView, buildCenterLayoutParams());
        return this;
    }

    /**
     * 设置loadingView，并考虑居中
     *
     * @param loadingPageView   loadingView
     * @param topHeight         loadingLayout距离顶部高度
     * @param loadingViewHeight loadingView高度
     */
    public SimpleLoadingLayout setLoadingView(@NonNull View loadingPageView, int topHeight, int loadingViewHeight) {
        this.removeView(mLoadingPageView);
        mLoadingPageView = loadingPageView;
        bindView(mLoadingPageView, buildLoadingLayoutParams(topHeight, loadingViewHeight));
        return this;
    }

    public SimpleLoadingLayout setEmptyViewLayout(@LayoutRes int emptyViewLayoutResId) {
        this.mEmptyViewLayoutResId = emptyViewLayoutResId;
        return this;
    }

    public SimpleLoadingLayout setEmptyView(@NonNull View emptyPageView) {
        this.removeView(mEmptyPageView);
        mEmptyPageView = emptyPageView;
        bindView(mEmptyPageView, buildCenterLayoutParams());
        return this;
    }

    public SimpleLoadingLayout setErrorViewLayout(@LayoutRes int errorViewLayoutResId) {
        this.mErrorViewLayoutResId = errorViewLayoutResId;
        return this;
    }

    public SimpleLoadingLayout setErrorView(@NonNull View errorPageView) {
        this.removeView(mErrorPageView);
        mErrorPageView = errorPageView;
        bindView(mErrorPageView, buildCenterLayoutParams());
        return this;
    }

    public SimpleLoadingLayout setNoNetworkViewLayout(@LayoutRes int noNetworkViewLayoutResId) {
        this.mNoNetworkViewLayoutResId = noNetworkViewLayoutResId;
        return this;
    }

    public SimpleLoadingLayout setNoNetworkView(@NonNull View noNetworkPageView) {
        this.removeView(mNoNetworkPageView);
        mNoNetworkPageView = noNetworkPageView;
        bindView(mNoNetworkPageView, buildCenterLayoutParams());
        return this;
    }

    private FrameLayout.LayoutParams buildLoadingLayoutParams(int topHeight, int loadingViewHeight) {
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER_HORIZONTAL | Gravity.TOP);
        int loadingViewTopMargin = LoadingUtils.getScreenHeight(mContext) / 2 - (topHeight + loadingViewHeight / 2);
        params.topMargin = loadingViewTopMargin;
        return params;
    }

    private FrameLayout.LayoutParams buildCenterLayoutParams() {
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER);
        return params;
    }

    /**
     * 获取LoadingPageView
     */
    public View getLoadingView() {
        return mLoadingPageView;
    }

    /**
     * 获取EmptyView
     */
    public View getEmptyView() {
        return mEmptyPageView;
    }

    /**
     * 获取ErrorView
     */
    public View getErrorView() {
        return mErrorPageView;
    }

    /**
     * 获取NoNetworkPageView
     */
    public View getNoNetworkPageView() {
        return mNoNetworkPageView;
    }

    @Override
    public void onLoading() {
        setStatus(LoadingStatus.Loading);
    }

    @Override
    public void onNoNetworkError() {
        setStatus(LoadingStatus.NoNetworkError);
    }

    @Override
    public void onEmpty() {
        setStatus(LoadingStatus.Empty);
    }

    @Override
    public void onError() {
        setStatus(LoadingStatus.Error);
    }

    @Override
    public void onSucceed() {
        setStatus(LoadingStatus.Succeed);
    }

    void onReset() {
        setStatus(LoadingStatus.Reset);
    }

    @Override
    public void onRequesting() {
        setStatus(LoadingStatus.Reqeusting);
    }

    /**
     * 设置ReloadButton的监听器
     */
    public SimpleLoadingLayout setOnReloadListener(OnReloadListener listener) {
        this.mOnReloadListener = listener;
        return this;
    }

    public interface OnReloadListener {
        void onReload(View v, @ReloadBtnType int reloadBtnType);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isInterceptTouchEvent || super.onInterceptTouchEvent(ev);
    }

    @IntDef({ReloadBtnType.RELOAD_BTN_TYPE_ERROR, ReloadBtnType.RELOAD_BTN_TYPE_NO_NETWORK})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ReloadBtnType {
        int RELOAD_BTN_TYPE_ERROR = 1;
        int RELOAD_BTN_TYPE_NO_NETWORK = 2;
    }

}
