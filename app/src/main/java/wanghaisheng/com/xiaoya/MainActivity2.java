package wanghaisheng.com.xiaoya;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
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

import wanghaisheng.com.xiaoya.ui.MainActivity;
import wanghaisheng.com.xiaoya.ui.daily.BaseDailyFragment;
import wanghaisheng.com.xiaoya.ui.daily.DailyListFragment;
import wanghaisheng.com.xiaoya.ui.me.MeFragment;
import wanghaisheng.com.xiaoya.ui.movie.MovieListFragment;
import wanghaisheng.com.xiaoya.ui.science.BaseScienceFragment;
import wanghaisheng.com.xiaoya.ui.science.ScienceListFragment;
import wanghaisheng.com.xiaoya.utils.PrefsUtil;
import wanghaisheng.com.xiaoya.widget.ColorIconWithText;

/**
 * Created by sheng on 2016/4/24.
 */
public class MainActivity2 extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener{
    private List<Fragment> mFragments = new ArrayList<>();
    private String[] fragmentTitle = {"果壳","知乎","电影","设置"};
    private ViewPager mPager;
    private FragmentPagerAdapter mAdapter;
    private Toolbar mToolbar;

    private List<ColorIconWithText> mTabIndicators = new ArrayList<>();

    private static final int INDEX_ONE = 0;
    private static final int INDEX_TWO = 1;
    private static final int INDEX_THREE = 2;
    private static final int INDEX_FOUR = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        //setupOptionsMenu();
        initView();
        initDatas();
        initEvents();
        mPager.setAdapter(mAdapter);

        getSupportActionBar().setElevation(0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mToolbar.setElevation(0);
        }
    }

    /**
     *初始化事件
     */
    private void initEvents() {
        mPager.addOnPageChangeListener(this);
    }

    private void initDatas() {
        mFragments.add(BaseScienceFragment.newInstance());
        mFragments.add(BaseDailyFragment.newInstance());
        mFragments.add(MovieListFragment.newInstance());
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

    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(fragmentTitle[0]);

        mPager = (ViewPager) findViewById(R.id.main_viewpager);

        ColorIconWithText oneTab = (ColorIconWithText) findViewById(R.id.tab_science);
        mTabIndicators.add(oneTab);
        ColorIconWithText twoTab = (ColorIconWithText) findViewById(R.id.tab_daily);
        mTabIndicators.add(twoTab);
        ColorIconWithText threeTab = (ColorIconWithText) findViewById(R.id.tab_movie);
        mTabIndicators.add(threeTab);
        ColorIconWithText fourTab = (ColorIconWithText) findViewById(R.id.tab_me);
        mTabIndicators.add(fourTab);

        oneTab.setOnClickListener(this);
        twoTab.setOnClickListener(this);
        threeTab.setOnClickListener(this);
        fourTab.setOnClickListener(this);

        //默认第一个tab为选中状态
        oneTab.setIconAlpha(1.0f);

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

    @Override
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
        getSupportActionBar().setTitle(fragmentTitle[position]);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //当程序退出时，保存下面三个fragment中的变量
        SharedPreferences.Editor editor = getSharedPreferences(PrefsUtil.PREF_NAME,
                Context.MODE_MULTI_PROCESS).edit();
        editor.putBoolean(ScienceListFragment.ARG_SCIENCE_LIST_FIRST_LOAD, true);
        editor.putBoolean(MovieListFragment.ARG_MOVIE_LIST_FIRST_LOAD, true);
        editor.putBoolean(DailyListFragment.ARG_DAILY_LIST_FIRST_LOAD, true);

        editor.commit();

        AppManager.finishAllActivity();
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context,MainActivity.class);
        context.startActivity(intent);
    }
}
