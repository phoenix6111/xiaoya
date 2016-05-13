package wanghaisheng.com.xiaoya.di.module;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import wanghaisheng.com.xiaoya.component.okhttp.HttpLoggingInterceptor;
import wanghaisheng.com.xiaoya.navigator.Navigator;


/**
 * Created by sheng on 2016/4/13.
 */
@Module
public class AppModule {

    Context context;

    //全局context，使用application context
    public AppModule(Context context) {
        this.context = context;
    }

    @Provides @Singleton
    public Context provideContext() {
        return context;
    }

    @Provides @Singleton
    public Navigator provideNavigator() {
        return new Navigator();
    }

    @Provides
    @Singleton//提供全局OkHttpClient
    public OkHttpClient provideOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(20 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout(20 * 1000, TimeUnit.MILLISECONDS);
//        builder.networkInterceptors().add(new StethoInterceptor());
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(logging);
        /*
        builder.addInterceptor(mCookieInterceptor);*/
        return builder.build();
    }


}
