package wanghaisheng.com.xiaoya.di.module;

import android.app.Activity;

import dagger.Module;
import dagger.Provides;
import wanghaisheng.com.xiaoya.di.scopes.PerActivity;

/**
 * Created by sheng on 2016/4/13.
 */

@Module
public class ActivityModule {

    private Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides @PerActivity
    public Activity provideActivity() {
        return activity;
    }


}
