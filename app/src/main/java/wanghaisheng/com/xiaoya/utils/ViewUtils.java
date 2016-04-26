package wanghaisheng.com.xiaoya.utils;

import android.support.design.widget.CoordinatorLayout;
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
}
