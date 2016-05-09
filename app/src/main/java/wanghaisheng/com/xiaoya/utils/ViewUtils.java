package wanghaisheng.com.xiaoya.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by sheng on 2016/4/16.
 */
public class ViewUtils {

    public static void setViewHeight(View view,int height) {
        CoordinatorLayout.LayoutParams linearParams =(CoordinatorLayout.LayoutParams) view.getLayoutParams(); //取控件textView当前的布局参数
        linearParams.height = height;// 控件的高强制设成20

        //linearParams.width = 30;// 控件的宽强制设成30

        view.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
    }

    public static  void setSystemBar(Activity context, Toolbar mToolbar, int color) {
        SystemBarTintManager tintManager = new SystemBarTintManager(context);
        tintManager.setStatusBarTintEnabled(true);
        mToolbar.setBackgroundColor(color);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            tintManager.setStatusBarTintColor(color);
        }
    }

    public static int getPaletteColor(Bitmap bitmap) {
        int color = -12417291;
        Palette p = Palette.from(bitmap).generate();
        Palette.Swatch vibrant =
                p.getVibrantSwatch();
        Palette.Swatch vibrantdark =
                p.getDarkVibrantSwatch();
        Palette.Swatch vibrantlight =
                p.getLightVibrantSwatch();
        Palette.Swatch Muted =
                p.getMutedSwatch();
        Palette.Swatch Muteddark =
                p.getDarkMutedSwatch();
        Palette.Swatch Mutedlight =
                p.getLightMutedSwatch();

        if (vibrant != null) {
            color = vibrant.getRgb();
        } else if (vibrantdark != null) {
            color = vibrantdark.getRgb();
        } else if (vibrantlight != null) {
            color = vibrantlight.getRgb();
        } else if (Muted != null) {
            color = Muted.getRgb();
        } else if (Muteddark != null) {
            color = Muteddark.getRgb();
        } else if (Mutedlight != null) {
            color = Mutedlight.getRgb();
        }
        return color;
    }
}
