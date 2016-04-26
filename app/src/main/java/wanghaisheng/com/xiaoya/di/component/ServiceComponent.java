package wanghaisheng.com.xiaoya.di.component;

import android.app.Service;

import dagger.Component;
import wanghaisheng.com.xiaoya.di.scopes.PerService;
import wanghaisheng.com.xiaoya.di.module.ServiceModule;

/**
 * Created by sheng on 2016/4/14.
 */

@PerService
@Component(modules = {ServiceModule.class},dependencies = AppComponent.class)
public interface ServiceComponent {

    Service getServiceContext();

}
