package me.hacket.library.loading;

/**
 * LoadingLayout状态接口 <br/>
 *
 * @author hacket <br/>
 * @time 2016/12/12 16:12 <br/>
 * @since v1.0
 */
public interface ILoadingStatus {

    /**
     * Loading状态
     */
    void onLoading();

    /**
     * 无网络错误状态
     */
    void onNoNetworkError();

    /**
     * 空状态
     */
    void onEmpty();

    /**
     * 错误状态
     */
    void onError();

    /**
     * 成功状态
     */
    void onSucceed();

    /**
     * Requesting状态，和{@link #onLoading()}区别是，Loading状态看不到succeed的View，Requesting状态可以看到succeed的View
     */
    void onRequesting();

}
