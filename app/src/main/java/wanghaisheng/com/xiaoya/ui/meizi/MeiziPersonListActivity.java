package wanghaisheng.com.xiaoya.ui.meizi;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.apkfuns.logutils.LogUtils;

import butterknife.Bind;
import wanghaisheng.com.xiaoya.R;
import wanghaisheng.com.xiaoya.ui.BaseSwipeBackActivity;
import wanghaisheng.com.xiaoya.widget.swipeback.SwipeBackActivityHelper;

/**
 * Created by sheng on 2016/5/6.
 * 个人所有图片列表
 */
public class MeiziPersonListActivity extends BaseSwipeBackActivity {
    private SwipeBackActivityHelper mHelper;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private MeiziPersonListFragment mFragment;

    public static final String ARG_COLOR = "arg_color";
    public static final String ARG_TITLE = "arg_title";
    public static final String ARG_URL = "arg_url";
    public static final String ARG_GROUPID = "arg_groupid";

    private int color;
    private String title;
    private String url;
    private String groupId;

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

    @Override
    public int getLayoutId() {
        return R.layout.base_content_toolbar_layout;
    }

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    public void initView() {
        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();

        initToolBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void initData() {
        color = getIntent().getIntExtra(ARG_COLOR,0);
        title = getIntent().getStringExtra(ARG_TITLE);
        url = getIntent().getStringExtra(ARG_URL);
        groupId = getIntent().getStringExtra(ARG_GROUPID);

        LogUtils.d("meizipersonlistactivity.....groupId.."+groupId);

        mFragment = MeiziPersonListFragment.newInstance(url,groupId);
        getSupportFragmentManager().beginTransaction().replace(R.id.content, mFragment).commit();
    }
}
