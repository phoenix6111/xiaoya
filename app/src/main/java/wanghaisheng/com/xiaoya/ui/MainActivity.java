package wanghaisheng.com.xiaoya.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import wanghaisheng.com.xiaoya.AppManager;
import wanghaisheng.com.xiaoya.Constants;
import wanghaisheng.com.xiaoya.R;
import wanghaisheng.com.xiaoya.ui.gaoxiao.BaseGaoxiaoFragment;
import wanghaisheng.com.xiaoya.ui.me.MeFragment;
import wanghaisheng.com.xiaoya.ui.meitu.BaseFunnyMeituFragment;
import wanghaisheng.com.xiaoya.ui.science.BaseScienceFragment;
import wanghaisheng.com.xiaoya.widget.ColorIconWithText;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener{
    private List<Fragment> mFragments = new ArrayList<>();
    private String[] fragmentTitle;
    private FragmentPagerAdapter mAdapter;

    @Bind(R.id.container)
    CoordinatorLayout container;
    @Bind(R.id.main_viewpager)
    ViewPager mPager;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.tab_science)
    ColorIconWithText tabScience;
    @Bind(R.id.tab_daily)
    ColorIconWithText tabDaily;
    @Bind(R.id.tab_movie)
    ColorIconWithText tabMovie;
    @Bind(R.id.tab_me)
    ColorIconWithText tabMe;

    private List<ColorIconWithText> mTabIndicators = new ArrayList<>();

    private static final int INDEX_ONE = 0;
    private static final int INDEX_TWO = 1;
    private static final int INDEX_THREE = 2;
    private static final int INDEX_FOUR = 3;


    @Override
    public void initView() {
//        initToolbar(mToolbar);
        fragmentTitle = new String[]{getString(R.string.str_science),getString(R.string.str_daily)
                ,getString(R.string.str_movie),getString(R.string.str_me)};

        mTabIndicators.add(tabScience);
        mTabIndicators.add(tabDaily);
        mTabIndicators.add(tabMovie);
        mTabIndicators.add(tabMe);

        setSupportActionBar(mToolbar);
        mToolbar.setTitle(fragmentTitle[0]);

        //默认第一个tab为选中状态
        tabScience.setIconAlpha(1.0f);
    }

    @Override
    public void initData() {
        mFragments.add(BaseScienceFragment.newInstance());
        //mFragments.add(BaseDailyFragment.newInstance());
//        mFragments.add(BaseBeautyMeituFragment.newInstance());
//        mFragments.add(BaseJianshuFragment.newInstance());
        //mFragments.add(MovieListFragment.newInstance());
//        mFragments.add(BaseMeiziFragment.newInstance());
        mFragments.add(BaseFunnyMeituFragment.newInstance());
        mFragments.add(BaseGaoxiaoFragment.newInstance());
        mFragments.add(MeFragment.newInstance());

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return fragmentTitle[position];
            }
        };

        mPager.addOnPageChangeListener(this);
        mPager.setAdapter(mAdapter);
    }

    @Override
    public void getDatas(Bundle savedInstanceState) {

    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return true;
    }

    @Override
    protected boolean isApplyStatusBarColor() {
        return true;
    }

    private void setupOptionsMenu() {
        ViewConfiguration conf = ViewConfiguration.get(this);
        try {
            Field menuKey = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            menuKey.setAccessible(true);
            menuKey.setBoolean(conf,false);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        //Log.d("menu icon tag","MENU opened!!!!");

        if(null!=menu && featureId==108){
            //Log.d("menu icon tag", menu.getClass().getSimpleName());
            if(menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible",Boolean.TYPE);
                    method.setAccessible(true);
                    try {
                        method.invoke(menu,true);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }

        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_search) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.tab_science,R.id.tab_daily,R.id.tab_movie,R.id.tab_me})
    public void onClick(View v) {
        //重置所有的Tab的alpha值为0
        resetTabAlpha();

        switch (v.getId()) {
            case R.id.tab_science:
                mTabIndicators.get(INDEX_ONE).setIconAlpha(1.0f);
                mPager.setCurrentItem(INDEX_ONE,false);
                break;
            case R.id.tab_daily:
                mTabIndicators.get(INDEX_TWO).setIconAlpha(1.0f);
                mPager.setCurrentItem(INDEX_TWO,false);
                break;
            case R.id.tab_movie:
                mTabIndicators.get(INDEX_THREE).setIconAlpha(1.0f);
                mPager.setCurrentItem(INDEX_THREE,false);
                break;
            case R.id.tab_me:
                mTabIndicators.get(INDEX_FOUR).setIconAlpha(1.0f);
                mPager.setCurrentItem(INDEX_FOUR,false);
                break;
        }
    }

    /**
     * 重置所有的Tab的alpha值为0
     */
    private void resetTabAlpha() {
        for(int i=0; i<mTabIndicators.size(); i++) {
            mTabIndicators.get(i).setIconAlpha(0);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //Log.d("TAG","position==>"+position+" positionOffset==>"+positionOffset);
        if(positionOffset>0) {
            ColorIconWithText left = mTabIndicators.get(position);
            ColorIconWithText right = mTabIndicators.get(position+1);
            left.setIconAlpha(1-positionOffset);
            right.setIconAlpha(positionOffset);
        }
    }

    @Override
    public void onPageSelected(int position) {
        setTitle(fragmentTitle[position]);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.AppExit(getApplicationContext());
    }

    @Override
    protected void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context,MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if(canExit()) {
            super.onBackPressed();
        }


    }

    private long lastPressTime = 0;
    private boolean canExit() {
        if(System.currentTimeMillis() - lastPressTime > Constants.exitConfirmTime) {
            lastPressTime = System.currentTimeMillis();
            Snackbar.make(container,getString(R.string.notify_exit_confirm),Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


}
