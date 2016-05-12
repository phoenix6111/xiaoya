package wanghaisheng.com.xiaoya.navigator;

import android.content.Context;
import android.os.Bundle;

import wanghaisheng.com.xiaoya.ui.jianshu.JianshuDetailActivity;
import wanghaisheng.com.xiaoya.ui.meizi.FuliActivity;
import wanghaisheng.com.xiaoya.ui.meizi.MeiziLargePicActivity;
import wanghaisheng.com.xiaoya.ui.meizi.MeiziPersonListActivity;

public class Navigator extends BaseNavigator{

    public Navigator(){}

    public void openMeiziPersonListActivity(Context context, Bundle bundle) {
        start(context, MeiziPersonListActivity.class,bundle);
    }

    public void openMeiziLargePicActivity(Context context,Bundle bundle) {
        start(context, MeiziLargePicActivity.class,bundle);
    }

    public void openFuliActivity(Context context,Bundle bundle) {
        start(context, FuliActivity.class);
    }

    public void openJianshuDetailActivity(Context context,Bundle bundle) {
        start(context, JianshuDetailActivity.class,bundle);
    }

}
