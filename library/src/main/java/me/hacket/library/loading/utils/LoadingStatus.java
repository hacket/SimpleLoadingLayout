package me.hacket.library.loading.utils;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * loading状态<br/>
 *
 * @author hacket <br/>
 * @time 2016/12/12 16:15 <br/>
 * @since v1.0
 */
@IntDef({LoadingStatus.Reset, LoadingStatus.Loading, LoadingStatus.NoNetworkError, LoadingStatus.Empty, LoadingStatus.Error, LoadingStatus.Succeed, LoadingStatus.Reqeusting})
@Retention(RetentionPolicy.SOURCE)
public @interface LoadingStatus {
    int Reset = 0;
    int Loading = 1;
    int NoNetworkError = 2;
    int Empty = 3;
    int Error = 4;
    int Succeed = 5;
    int Reqeusting = 6;
}
