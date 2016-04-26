package wanghaisheng.com.xiaoya.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import javax.inject.Inject;

import butterknife.ButterKnife;
import wanghaisheng.com.xiaoya.AppContext;
import wanghaisheng.com.xiaoya.AppManager;
import wanghaisheng.com.xiaoya.R;
import wanghaisheng.com.xiaoya.di.component.ActivityComponent;
import wanghaisheng.com.xiaoya.di.component.DaggerActivityComponent;
import wanghaisheng.com.xiaoya.di.module.ActivityModule;
import wanghaisheng.com.xiaoya.utils.ImageUtil;
import wanghaisheng.com.xiaoya.utils.NetWorkHelper;
import wanghaisheng.com.xiaoya.utils.ResourceHelper;
import wanghaisheng.com.xiaoya.utils.SettingPrefHelper;
import wanghaisheng.com.xiaoya.utils.StatusBarUtil;
import wanghaisheng.com.xiaoya.utils.ToastUtil;

/**
 * Created by sheng on 2016/4/13.
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Inject
    ResourceHelper mResourceHelper;
    @Inject
    SettingPrefHelper mSettingPrefHelper;
    @Inject
    protected ImageUtil imageUtil;
    @Inject
    protected NetWorkHelper netWorkHelper;
    @Inject
    protected ToastUtil toastUtil;

    protected AppContext mAppContext;
    private static final String TAG = "BaseActivity";

    protected ActivityComponent mActivityComponent;

    protected Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        mActivityComponent = DaggerActivityComponent.builder()
                                .activityModule(new ActivityModule(this))
                                .appComponent(((AppContext)getApplication()).getAppComponent())
                                .build();
        mActivityComponent.inject(this);

        initTheme();
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        mAppContext = (AppContext) getApplicationContext();

        setTranslucentStatus(isApplyStatusBarTranslucency());
        setStatusBarColor(isApplyStatusBarColor());

        initInjector();

        getDatas(savedInstanceState);

        initUiAndListener();

        checkNetwork();

        AppManager.getAppManager().addActivity(this);
    }

    public abstract void getDatas(Bundle savedInstanceState);

    private void initTheme() {
        if (mSettingPrefHelper.getNightModel()) {
            int theme;

            try {
                theme = getPackageManager().getActivityInfo(getComponentName(), 0).theme;
            } catch (PackageManager.NameNotFoundException e) {
                return;
            }
            if (theme == R.style.AppTheme) {
                theme = R.style.NightTheme;
            } else if (theme == R.style.NightTheme) {
                theme = R.style.AppTheme;
            }
            setTheme(theme);
        }
    }

    private void checkNetwork() {
        if(!netWorkHelper.isAvailableNetwork()) {
            //Toast.makeText(this,"无网络连接",Toast.LENGTH_LONG).show();
            toastUtil.showToast("无网络连接");
        }
    }

    /**
     * is applyStatusBarTranslucency
     *
     * @return
     */
    protected abstract boolean isApplyStatusBarTranslucency();

    /**
     * set status bar translucency
     *
     * @param on
     */
    protected void setTranslucentStatus(boolean on) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if (on) {
                winParams.flags |= bits;
            } else {
                winParams.flags &= ~bits;
            }
            win.setAttributes(winParams);
        }
    }

    protected abstract boolean isApplyStatusBarColor();

    /**
     * use SystemBarTintManager
     */
    public void setStatusBarColor(boolean on) {
        if (on) {
            StatusBarUtil.setColor(this, mResourceHelper.getThemeColor(this), 0);
        }
    }

    protected void initToolbar(Toolbar toolbar) {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        //this.mToolbar = toolbar;
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }


    public void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }


    public int getStatusBarHeight() {
        return mResourceHelper.getStatusBarHeight(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        AppManager.getAppManager().finishActivity(this);
    }

    protected abstract void initInjector();

    //get layout file id
    protected abstract int getLayoutId();

    /**
     * init UI && Listener
     */
    public abstract void initUiAndListener();

    protected <T extends View> T findById(int id) {
        return (T) findViewById(id);
    }

    protected void startActivity(Context mContext, Activity activity) {
        Intent intent = new Intent(mContext,activity.getClass());
        mContext.startActivity(intent);
    }
}
