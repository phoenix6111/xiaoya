package wanghaisheng.com.xiaoya.ui.meizi;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import wanghaisheng.com.xiaoya.R;
import wanghaisheng.com.xiaoya.ui.BaseSwipeBackActivity;

/**
 * Created by sheng on 2016/5/8.
 */
public class MeiziLargePicActivity extends BaseSwipeBackActivity {
    public static final String ARG_INEX = "arg_index";
    public static final String ARG_GROUPID = "arg_groupid";
    public static final String ARG_URLS = "arg_urls";

    private int index;
    private String groupId;
    private ArrayList<String> urls = new ArrayList<>();

    @Bind(R.id.large_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    PagerAdapter pagerAdapter;

    @Override
    public void getDatas(Bundle savedInstanceState) {
        Intent intent = getIntent();
        this.index = intent.getIntExtra(ARG_INEX,0);
        this.groupId = intent.getStringExtra(ARG_GROUPID);
        this.urls = intent.getStringArrayListExtra(ARG_URLS);
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return true;
    }

    @Override
    protected boolean isApplyStatusBarColor() {
        return true;
    }

    @Override
    protected void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_large_pic;
    }

    @Override
    public void initView() {
        //initToolbar(mToolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supportFinishAfterTransition();
            }
        });
        setTitle("妹子");

//        supportPostponeEnterTransition();//延缓执行 然后在fragment里面的控件加载完成后start
    }

    @Override
    public void initData() {
//        LogUtils.d("print urls..............");
//        LogUtils.d(urls);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(),urls,groupId);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(index);

        if (Build.VERSION.SDK_INT >= 22) {
            //这个可以看做个管道  每次进入和退出的时候都会进行调用  进入的时候获取到前面传来的共享元素的信息
            //退出的时候 把这些信息传递给前面的activity
            //同时向sharedElements里面put view,跟对view添加transitionname作用一样
            setEnterSharedElementCallback(new SharedElementCallback() {
                @Override
                public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                    String url = urls.get(viewPager.getCurrentItem());
                    MeiziLargePicFragment fragment = (MeiziLargePicFragment) pagerAdapter.instantiateItem(viewPager, viewPager.getCurrentItem());
                    sharedElements.clear();
                    sharedElements.put(url, fragment.getSharedElement());
                }
            });
        }
    }

    @TargetApi(22)
    @Override
    public void supportFinishAfterTransition() {
        Intent data = new Intent();
        data.putExtra(ARG_INEX, viewPager.getCurrentItem());
        setResult(RESULT_OK, data);
        super.supportFinishAfterTransition();
    }
}
