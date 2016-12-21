package me.hacket.simpleloadinglayout.demo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.hacket.simpleloadinglayout.R;

public class PageActivity extends AppCompatActivity {

    @BindView(R.id.root_layout)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.tl_tablyout)
    TabLayout mTablayout;

    @BindView(R.id.vp_content)
    ViewPager mViewPager;

    @BindView(R.id.fab_btn)
    FloatingActionButton mFab;
    private String[] mTabTitls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        loadDatas();
    }

    private void loadDatas() {
        initTitles();
        initToolbar();
        bindViews();
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setTitleTextColor(Color.BLACK);
        mToolbar.setTitle("SimpleLoadingLayout");
    }

    private void initTitles() {
        mTabTitls = new String[]{
                "tab1", "tab2", "tab3"
        };
    }

    private void bindViews() {
        SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(), this, mTabTitls);
        mViewPager.setAdapter(adapter);
        mTablayout.setTabMode(TabLayout.MODE_FIXED);
        mTablayout.setupWithViewPager(mViewPager);
        mTablayout.getTabAt(1).select();
    }

}
