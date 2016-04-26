package wanghaisheng.com.xiaoya.di.module;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import wanghaisheng.com.xiaoya.utils.CacheHelper;
import wanghaisheng.com.xiaoya.utils.ConfigHelper;
import wanghaisheng.com.xiaoya.utils.DataCleanHelper;
import wanghaisheng.com.xiaoya.utils.FileHelper;
import wanghaisheng.com.xiaoya.utils.FormatHelper;
import wanghaisheng.com.xiaoya.utils.ImageUtil;
import wanghaisheng.com.xiaoya.utils.NetWorkHelper;
import wanghaisheng.com.xiaoya.utils.OkHttpHelper;
import wanghaisheng.com.xiaoya.utils.PrefsUtil;
import wanghaisheng.com.xiaoya.utils.RequestHelper;
import wanghaisheng.com.xiaoya.utils.ResourceHelper;
import wanghaisheng.com.xiaoya.utils.SecurityHelper;
import wanghaisheng.com.xiaoya.utils.SettingPrefHelper;
import wanghaisheng.com.xiaoya.utils.ShareHelper;
import wanghaisheng.com.xiaoya.utils.StringHelper;
import wanghaisheng.com.xiaoya.utils.ToastHelper;
import wanghaisheng.com.xiaoya.utils.ToastUtil;

/**
 * Created by sheng on 2016/4/14.
 */

@Module
public class HelperModule {
    @Provides
    @Singleton
    FormatHelper provideFormatHelper() {
        return new FormatHelper();
    }

    @Provides
    @Singleton
    FileHelper provideFileHelper(Context context) {
        return new FileHelper(context);
    }

    @Provides
    @Singleton
    SecurityHelper provideSecurityHelper() {
        return new SecurityHelper();
    }


    @Provides
    @Singleton
    NetWorkHelper provideNetWorkHelper(Context mContext) {
        return new NetWorkHelper(mContext);
    }


    @Provides
    @Singleton
    RequestHelper provideRequestHelper(SecurityHelper securityHelper, Context context, SettingPrefHelper mSettingPrefHelper) {
        return new RequestHelper(securityHelper, context, mSettingPrefHelper);
    }

    @Provides
    @Singleton
    OkHttpHelper provideOkHttpHelper(OkHttpClient okHttpClient) {
        return new OkHttpHelper(okHttpClient);
    }


    @Provides
    @Singleton
    SettingPrefHelper provideSettingPrefHelper(Context context) {
        return new SettingPrefHelper(context);
    }


    @Provides
    @Singleton
    ResourceHelper provideResourceHelper() {
        return new ResourceHelper();
    }


    @Provides
    @Singleton
    DataCleanHelper provideDataCleanHelper(Context context) {
        return new DataCleanHelper(context);
    }

    @Provides
    @Singleton
    ConfigHelper provideConfigHelper(SettingPrefHelper mSettingPrefHelper) {
        return new ConfigHelper(mSettingPrefHelper);
    }

    @Provides
    @Singleton
    ToastHelper provideToastHelper(Context mContext) {
        return new ToastHelper(mContext);
    }

    @Provides
    @Singleton
    CacheHelper provideCacheHelper(Context mContext, FormatHelper mFormatHelper) {
        return new CacheHelper(mContext, mFormatHelper);
    }

    @Provides
    @Singleton
    ShareHelper provideShareHelper(Context mContext) {
        return new ShareHelper(mContext);
    }

    @Provides
    @Singleton
    StringHelper provideStringHelper(Context mContext, ToastHelper mToastHelper) {
        return new StringHelper(mContext, mToastHelper);
    }

    @Provides
    @Singleton
    ImageUtil provideImageUtil(Context mContext) {
        return new ImageUtil(mContext);
    }

    @Provides
    @Singleton
    PrefsUtil providePrefsUtil(Context context) {
        return new PrefsUtil(context);
    }

    @Provides
    @Singleton
    ToastUtil provideToastUtil(Context context) {
        return new ToastUtil(context);
    }
}
