package wanghaisheng.com.xiaoya;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.internal.Supplier;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;
import com.squareup.leakcanary.LeakCanary;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.Properties;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import wanghaisheng.com.xiaoya.component.okhttp.OkHttpImagePipelineConfigFactory;
import wanghaisheng.com.xiaoya.di.component.AppComponent;
import wanghaisheng.com.xiaoya.di.component.DaggerAppComponent;
import wanghaisheng.com.xiaoya.di.module.AppModule;
import wanghaisheng.com.xiaoya.utils.AppConfig;
import wanghaisheng.com.xiaoya.utils.ImageUtil;

/**
 * Created by sheng on 2016/4/13.
 */
public class AppContext extends BaseApplication{

    private AppComponent appComponent;

    private static AppContext instance;

    @Inject
    OkHttpClient mOkHttpClient;

    @Inject
    ImageUtil imageUtil;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        initComponent();

        initOkHttpUtils();

        initFrescoConfig();

        LeakCanary.install(this);

        /*Thread.setDefaultUncaughtExceptionHandler(AppException
                .getAppExceptionHandler(this));*/
        //CrashHandler.getInstance().init(getApplicationContext());

        /*Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build());*/

        //为全局设置OkHttpUtils的OkHttpClient对象
        OkHttpUtils.getInstance(mOkHttpClient);
        //初始化Activity管理栈
        AppManager.init();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    private void initOkHttpUtils() {
        OkHttpUtils.getInstance(new OkHttpClient.Builder().build());
    }

    private void initComponent() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        appComponent.inject(this);
    }

   /* private void initImageLoader() {
        ImageLoaderUtils.init(this, imageUtil.getImageTmpDir(), true);
    }*/

    public AppComponent getApplicationComponent() {
        return appComponent;
    }

    private static final int MAX_HEAP_SIZE = (int) Runtime.getRuntime().maxMemory();

    public static final int MAX_DISK_CACHE_SIZE = 50 * ByteConstants.MB;
    public static final int MAX_MEMORY_CACHE_SIZE = MAX_HEAP_SIZE / 8;

    private void initFrescoConfig() {
        final MemoryCacheParams bitmapCacheParams = new MemoryCacheParams(
                MAX_MEMORY_CACHE_SIZE, // Max total size of elements in the cache
                Integer.MAX_VALUE,                     // Max entries in the cache
                MAX_MEMORY_CACHE_SIZE, // Max total size of elements in eviction queue
                Integer.MAX_VALUE,                     // Max length of eviction queue
                Integer.MAX_VALUE);
        ImagePipelineConfig config = OkHttpImagePipelineConfigFactory
                .newBuilder(this, mOkHttpClient).setProgressiveJpegConfig(new SimpleProgressiveJpegConfig())
                .setBitmapMemoryCacheParamsSupplier(
                        new Supplier<MemoryCacheParams>() {
                            public MemoryCacheParams get() {
                                return bitmapCacheParams;
                            }
                        })
                .setMainDiskCacheConfig(
                        DiskCacheConfig.newBuilder(this)
                                .setBaseDirectoryPath(getCacheDir())
                                .setBaseDirectoryName("imageCache")
                                .setMaxCacheSize(MAX_DISK_CACHE_SIZE)
                                .build()).build();
        Fresco.initialize(this, config);

    }

    /**
     * 获得当前app运行的AppContext
     *
     * @return
     */
    public static AppContext getInstance() {
        return instance;
    }

    public boolean containsProperty(String key) {
        Properties props = getProperties();
        return props.containsKey(key);
    }

    public void setProperties(Properties ps) {
        AppConfig.getAppConfig(this).set(ps);
    }

    public Properties getProperties() {
        return AppConfig.getAppConfig(this).get();
    }

    public void setProperty(String key, String value) {
        AppConfig.getAppConfig(this).set(key, value);
    }


    /**
     * 获取cookie时传AppConfig.CONF_COOKIE
     *
     * @param key
     * @return
     */
    public String getProperty(String key) {
        String res = AppConfig.getAppConfig(this).get(key);
        return res;
    }

    public void removeProperty(String... key) {
        AppConfig.getAppConfig(this).remove(key);
    }

}
