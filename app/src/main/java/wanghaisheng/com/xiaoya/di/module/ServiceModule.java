package wanghaisheng.com.xiaoya.di.module;

import android.app.Service;

import dagger.Module;
import dagger.Provides;
import wanghaisheng.com.xiaoya.di.scopes.PerService;

/**
 * Created by sheng on 2016/4/14.
 */
@Module
public class ServiceModule {

    private Service mService;

    public ServiceModule(Service service) {
        mService = service;
    }

    @Provides
    @PerService
    public Service provideContext() {
        return mService;
    }
}
