package me.hacket.library.loading.widget;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import me.hacket.library.R;
import me.hacket.library.loading.utils.LoadingUtils;

/**
 * 全局配置，对所有使用到的地方有效 <br/>
 *
 * @author hacket <br/>
 * @time 2016/12/12 15:27 <br/>
 * @since v1.0
 */
public class LoadingConfig {

    public static final String TAG = "LoadingConfig";

    private static volatile LoadingConfig sInstance = null;
    private final Context mContext;

    String emptyStr = "";
    String errorStr = "";
    String netwrokStr = "";
    String reloadBtnStr = "";
    int emptyImgId;
    int errorImgId;
    int networkImgId;
    int reloadBtnId;
    int tipTextSize;
    int buttonTextSize;
    int tipTextColor;
    int buttonTextColor;
    int buttonWidth;
    int buttonHeight;

    @LayoutRes
    int loadingViewLayoutResId;
    @LayoutRes
    int emptyViewLayoutResId;
    @LayoutRes
    int errorViewLayoutResId;
    @LayoutRes
    int noNetworkViewLayoutResId;

    int backgroundColor;

    private LoadingConfig(Context context) {
        this.mContext = context;
        if (sInstance != null) {
            throw new IllegalStateException("the instance is not null");
        }
        init();
    }

    public static LoadingConfig getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LoadingConfig.class) {
                if (sInstance == null) {
                    sInstance = new LoadingConfig(context);
                }
            }
        }
        return sInstance;
    }

    private void init() {
        if (TextUtils.isEmpty(emptyStr)) {
            emptyStr = LoadingUtils.getString(mContext, R.string.loading_layout_emptystr);
        }
        if (TextUtils.isEmpty(errorStr)) {
            errorStr = LoadingUtils.getString(mContext, R.string.loading_layout_errorstr);
        }
        if (TextUtils.isEmpty(netwrokStr)) {
            netwrokStr = LoadingUtils.getString(mContext, R.string.loading_layout_not_netwrokstr);
        }
        if (TextUtils.isEmpty(reloadBtnStr)) {
            reloadBtnStr = LoadingUtils.getString(mContext, R.string.loading_layout_reloadbtnstr);
        }
        if (emptyImgId == 0) {
            emptyImgId = R.mipmap.loading_empty;
        }
        if (errorImgId == 0) {
            errorImgId = R.mipmap.loading_error;
        }
        if (networkImgId == 0) {
            networkImgId = R.mipmap.loading_no_network;
        }
        if (reloadBtnId == 0) {
            reloadBtnId = R.drawable.selector_btn_back_gray;
        }
        if (tipTextSize == 0) {
            tipTextSize = 14;
        }
        if (buttonTextSize == 0) {
            buttonTextSize = 14;
        }
        if (tipTextColor == 0) {
            tipTextColor = R.color.base_text_color_light;
        }
        if (buttonTextColor == 0) {
            buttonTextColor = R.color.base_text_color_light;
        }
        if (buttonWidth == 0) {
            buttonWidth = -1;
        }
        if (buttonHeight == 0) {
            buttonHeight = -1;
        }
        if (backgroundColor == 0) {
            backgroundColor = R.color.base_loading_background;
        }
    }

    public LoadingConfig setErrorText(@NonNull String errorStr) {
        this.errorStr = errorStr;
        return this;
    }

    public LoadingConfig setEmptyText(@NonNull String emptyStr) {
        this.emptyStr = emptyStr;
        return this;
    }

    public LoadingConfig setNoNetworkText(@NonNull String netwrokStr) {
        this.netwrokStr = netwrokStr;
        return this;
    }

    public LoadingConfig setReloadButtonText(@NonNull String reloadBtnStr) {
        this.reloadBtnStr = reloadBtnStr;
        return this;
    }

    /**
     * 设置所有提示文本的字体大小
     */
    public LoadingConfig setAllTipTextSize(int tipTextSize) {
        this.tipTextSize = tipTextSize;
        return this;
    }

    /**
     * 设置所有提示文本的字体颜色
     */
    public LoadingConfig setAllTipTextColor(@ColorRes int tipTextColor) {
        this.tipTextColor = tipTextColor;
        return this;
    }

    public LoadingConfig setReloadButtonTextSize(int buttonTextSize) {
        this.buttonTextSize = buttonTextSize;
        return this;
    }

    public LoadingConfig setReloadButtonTextColor(@ColorRes int buttonTextColor) {
        this.buttonTextColor = buttonTextColor;
        return this;
    }

    public LoadingConfig setReloadButtonBackgroundResource(@DrawableRes int reloadBtnId) {
        this.reloadBtnId = reloadBtnId;
        return this;
    }

    public LoadingConfig setReloadButtonWidthAndHeight(int buttonWidth, int buttonHeight) {
        this.buttonWidth = buttonWidth;
        this.buttonHeight = buttonHeight;
        return this;
    }

    public LoadingConfig setErrorImage(@DrawableRes int errorImgId) {
        this.errorImgId = errorImgId;
        return this;
    }

    public LoadingConfig setEmptyImage(@DrawableRes int emptyImgId) {
        this.emptyImgId = emptyImgId;
        return this;
    }

    public LoadingConfig setNoNetworkImage(@DrawableRes int id) {
        this.networkImgId = id;
        return this;
    }

    public LoadingConfig setLoadingViewLayout(@LayoutRes int loadingLayoutId) {
        this.loadingViewLayoutResId = loadingLayoutId;
        return this;
    }

    public LoadingConfig setEmptyViewLayout(@LayoutRes int emptyViewLayoutResId) {
        this.emptyViewLayoutResId = emptyViewLayoutResId;
        return this;
    }

    public LoadingConfig setErrorViewLayout(@LayoutRes int errorViewLayoutResId) {
        this.errorViewLayoutResId = errorViewLayoutResId;
        return this;
    }

    public LoadingConfig setNoNetworkViewLayout(@LayoutRes int noNetworkViewLayoutResId) {
        this.noNetworkViewLayoutResId = noNetworkViewLayoutResId;
        return this;
    }

    public LoadingConfig setAllPageBackgroundColor(@ColorRes int backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

}
