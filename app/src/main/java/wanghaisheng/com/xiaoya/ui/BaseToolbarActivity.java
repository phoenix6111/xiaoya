package wanghaisheng.com.xiaoya.ui;

import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import wanghaisheng.com.xiaoya.R;
import wanghaisheng.com.xiaoya.utils.ViewUtils;

/**
 * Created by sheng on 2016/5/9.
 */
public abstract class BaseToolbarActivity extends BaseActivity{
    public static final String ARG_COLOR = "arg_color";
    protected int mColor;
    protected Toolbar mToolbar;
    protected TextSwitcher mTextSwitcher;

    @Override
    public void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        mTextSwitcher = (TextSwitcher) findViewById(R.id.toolbar_title);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 22) {
                    supportFinishAfterTransition();
                } else {
                    finish();
                }
            }
        });
        mColor = getIntent().getIntExtra(ARG_COLOR, ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
        ViewUtils.setSystemBar(this, mToolbar, mColor);
        initTitle();
    }

    private void initTitle() {
        mTextSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                final TextView textView = new TextView(BaseToolbarActivity.this);
                textView.setTextAppearance(BaseToolbarActivity.this, R.style.WebTitle);
                textView.setSingleLine(true);
                textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                textView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        textView.setSelected(true);
                    }
                }, 1738);
                return textView;
            }
        });
        mTextSwitcher.setInAnimation(this, android.R.anim.fade_in);
        mTextSwitcher.setOutAnimation(this, android.R.anim.fade_out);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        mTextSwitcher.setText(title);
    }
}
