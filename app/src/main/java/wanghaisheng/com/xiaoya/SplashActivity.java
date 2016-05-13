package wanghaisheng.com.xiaoya;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;
import wanghaisheng.com.xiaoya.ui.MainActivity;

/**
 * Created by sheng on 2016/4/24.
 */
public class SplashActivity extends AppCompatActivity {

    FrameLayout splash;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*setContentView(R.layout.activity_splash);
        splash = (FrameLayout) findViewById(R.id.splash);

        AlphaAnimation aa = new AlphaAnimation(0.7f, 1.0f);
        aa.setDuration(2000);
        splash.startAnimation(aa);
        aa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });*/
        Observable.timer(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

        AppManager.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.finishActivity(this);
    }
}
