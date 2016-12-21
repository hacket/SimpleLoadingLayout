package me.hacket.simpleloadinglayout.demo;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yatatsu.autobundle.AutoBundle;
import com.yatatsu.autobundle.AutoBundleField;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.hacket.library.loading.utils.LoadingStatus;
import me.hacket.library.loading.widget.SimpleLoadingLayout;
import me.hacket.simpleloadinglayout.R;

public class PageFragment extends Fragment implements PageAdapter.OnItemClickListener {

    public static final String ARG_PAGE = "ARG_PAGE";
    private static final String TAG = "hacket";
    private int mPage;

    @BindView(R.id.rv_page)
    RecyclerView mRecyclerView;

    @BindView(R.id.loading_layout)
    SimpleLoadingLayout mLoading;

    @AutoBundleField
    int page;

    @AutoBundleField(required = false)
    @LoadingStatus
    int currentStatus = LoadingStatus.Loading;

    private ArrayList<String> list;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        AutoBundle.bind(this);
    }

    private int getLayoutId() {
        return R.layout.fragment_page;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, view);
        initViews(view);
        setListeners();
        bindViews();
        return view;
    }

    private void initViews(@NonNull View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_page);

        list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            list.add(i + "_test");
        }

        //设置布局管理器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        PageAdapter mAdapter = new PageAdapter(getActivity(), list);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setListeners() {
        mLoading.setOnReloadListener(new SimpleLoadingLayout.OnReloadListener() {
            @Override
            public void onReload(View v, @SimpleLoadingLayout.ReloadBtnType int reloadBtnType) {
                switch (reloadBtnType) {
                    case SimpleLoadingLayout.ReloadBtnType.RELOAD_BTN_TYPE_ERROR:
                        Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                        break;
                    case SimpleLoadingLayout.ReloadBtnType.RELOAD_BTN_TYPE_NO_NETWORK:
                        Toast.makeText(getContext(), "no network", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void bindViews() {

        Log.i(TAG, "bindViews,currentStatus:" + currentStatus + " " + this.getClass().hashCode());

//        // mLoading.setLoadingViewLayout(R.layout.loading_test_layout);
//        FlowerView loadingView = new FlowerView.Builder(getContext())
//                .sizeRatio(0.25f)
//                .text("正在加载中...")
//                .build();
//
//        int topHeight = LoadingUtils.dp2px(getContext(), 45);
//        int loadingViewHeight = loadingView.getFinalSize();
//        mLoading.setLoadingView(loadingView, 250, loadingViewHeight);

        mLoading.setStatus(currentStatus);
    }

    @OnClick({R.id.btn_success, R.id.btn_empty, R.id.btn_error, R.id.btn_no_network, R.id.btn_loading, R.id.btn_requesting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_success:
                mLoading.onSucceed();
                break;
            case R.id.btn_empty:
                mLoading.onEmpty();
                break;
            case R.id.btn_error:
                mLoading.onError();
                break;
            case R.id.btn_no_network:
                mLoading.onNoNetworkError();
                break;
            case R.id.btn_loading:
                mLoading.onLoading();
                break;
            case R.id.btn_requesting:
                mLoading.onRequesting();
                break;
            default:
                mLoading.onEmpty();
                break;
        }
        currentStatus = mLoading.getCurrentStatus();
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(getContext(), "onItemClick:" + list.get(position).toString(), Toast.LENGTH_SHORT).show();
    }

}
