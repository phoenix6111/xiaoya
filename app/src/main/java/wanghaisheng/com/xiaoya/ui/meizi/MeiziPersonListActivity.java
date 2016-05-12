package wanghaisheng.com.xiaoya.ui.meizi;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;

import butterknife.Bind;
import wanghaisheng.com.xiaoya.R;
import wanghaisheng.com.xiaoya.ui.BaseToolbarActivity;
import wanghaisheng.com.xiaoya.utils.FileHelper;

/**
 * Created by sheng on 2016/5/6.
 * 个人所有图片列表
 */
public class MeiziPersonListActivity extends BaseToolbarActivity {
//    private SwipeBackActivityHelper mHelper;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private MeiziPersonListFragment mFragment;

    public static final String ARG_COLOR = "arg_color";
    public static final String ARG_TITLE = "arg_title";
    public static final String ARG_URL = "arg_url";
    public static final String ARG_GROUPID = "arg_groupid";

    private int color;
    private String title;
    private String groupId;

    private Bundle reenterState;

    private PagerResultView pagerResultView;
    private ISaveAllImage iSaveAllImage;

    protected boolean isCollected;

    protected MenuItem mMenuItem;

    @Override
    public void getDatas(Bundle savedInstanceState) {
        color = getIntent().getIntExtra(ARG_COLOR,0);
        title = getIntent().getStringExtra(ARG_TITLE);
        groupId = getIntent().getStringExtra(ARG_GROUPID);
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
        return R.layout.activity_meizi_group_list;
    }

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    public void initView() {
        super.initView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//并没有卵用  还是从右边退出..搞不清楚
            getWindow().setReturnTransition(new Slide(Gravity.TOP));
        }

        if (title != null) {
            setTitle(title);
        }

    }

    @Override
    public void onBackPressed() {
        ActivityCompat.finishAfterTransition(this);
    }

    @Override
    public void initData() {

        mFragment = MeiziPersonListFragment.newInstance(groupId,title);
        getSupportFragmentManager().beginTransaction().replace(R.id.content, mFragment).commit();

        this.pagerResultView = mFragment;
        this.iSaveAllImage = mFragment;
    }

    @TargetApi(22)
    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        supportPostponeEnterTransition();
        reenterState = new Bundle(data.getExtras());

        pagerResultView.handlerResult(reenterState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_meizi_group, menu);
        mMenuItem = menu.findItem(R.id.menu_collect);
        updateCollectionsMenu();
        return super.onCreateOptionsMenu(menu);
    }

    protected void updateCollectionsMenu() {
        if(isCollected){
            mMenuItem.setIcon(R.mipmap.star_focus);
        }else {
            mMenuItem.setIcon(R.mipmap.start_black);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.menu_collect) {
            collect();
        } else {
            LogUtils.d("一键保存");
            if(!FileHelper.hasSDCard()) {
                Toast.makeText(getApplicationContext(), "未安装内存卡，不能下载", Toast.LENGTH_SHORT).show();
            } else {
                iSaveAllImage.saveAllImage();
            }

        }

        return true;
    }

    /**
     * 收藏该groupId对应的图片
     */
    public void collect() {

    }
}
