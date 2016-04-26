package wanghaisheng.com.xiaoya.ui.setting;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import butterknife.Bind;
import butterknife.ButterKnife;
import wanghaisheng.com.xiaoya.R;
import wanghaisheng.com.xiaoya.ui.BaseActivity;

/**
 * Created by sheng on 2016/4/19.
 */
public class SettingActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;

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
    protected void initInjector() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void initUiAndListener() {
        ButterKnife.bind(this);
        initToolbar(toolbar);

        getSupportFragmentManager().beginTransaction().replace(R.id.setting_framelayout, SettingFragment.newInstance()).commit();
    }

}
