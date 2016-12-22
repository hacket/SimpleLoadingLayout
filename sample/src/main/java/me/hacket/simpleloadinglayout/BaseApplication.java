package me.hacket.simpleloadinglayout;

import android.app.Application;

import me.hacket.library.loading.widget.LoadingConfig;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initLoading();
    }

    private void initLoading() {
        LoadingConfig.getInstance(this)
                .setEmptyText("暂无数据")
                .setNoNetworkText("网络出错, 请触屏幕重新加载")
                .setErrorText("服务器错误，稍后重试")
                .setLoadingViewLayout(R.layout.loading_layout_flower)
                .setNoNetworkImage(R.drawable.icon_home_error)
                .setErrorImage(R.drawable.icon_home_error);
    }

}
