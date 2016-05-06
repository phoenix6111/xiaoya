package wanghaisheng.com.xiaoya.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.apkfuns.logutils.LogUtils;

import java.lang.reflect.Field;

import javax.inject.Inject;

import butterknife.Bind;
import wanghaisheng.com.xiaoya.R;
import wanghaisheng.com.xiaoya.presenter.base.BaseDetailView;
import wanghaisheng.com.xiaoya.ui.empty.EmptyLayout;
import wanghaisheng.com.xiaoya.utils.ShareHelper;
import wanghaisheng.com.xiaoya.widget.XiaoYaWebView;

/**
 * Created by sheng on 2016/4/15.
 */
public abstract class BaseDetailActivity extends BaseSwipeBackActivity implements XiaoYaWebView.XiaoYaWebViewCallBack,BaseDetailView {

    @Bind(R.id.empty_layout)
    protected EmptyLayout emptyLayout;
    protected int mStoreEmptyState = -1;//保存EmptyLayout的状态信息

    protected XiaoYaWebView webView;
    @Bind(R.id.webview_container)
    protected FrameLayout webviewContainer;

    @Inject
    ShareHelper shareHelper;
    protected boolean isCollected;

    protected MenuItem mMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setConfigCallback((WindowManager)getApplicationContext().getSystemService(Context.WINDOW_SERVICE));

        mActivityComponent.inject(this);

        webView = new XiaoYaWebView(this);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        webView.setLayoutParams(layoutParams);
        webView.setBackgroundColor(ContextCompat.getColor(mAppContext,R.color.window_color));
        webviewContainer.removeAllViews();
        webviewContainer.addView(webView);

        webView.setCallBack(this);

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                setTitle(title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    hideLoading();
                }
            }
        });

        emptyLayout = (EmptyLayout) findViewById(R.id.empty_layout);
        emptyLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onReloadClick();
            }
        });
    }

    public boolean checkNetWork() {
        if(!netWorkHelper.isAvailableNetwork()) {
            error(ERROR_TYPE_NETWORK,null);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void hideLoading() {
        emptyLayout.dismiss();
        webviewContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoading() {
        emptyLayout.setNetworkLoading();
        webviewContainer.setVisibility(View.GONE);
    }

    @Override
    public void error(int errorType,String errMsg) {
        switch (errorType) {
            case ERROR_TYPE_NETWORK:
                emptyLayout.setNetworkError();
                break;
            case ERROR_TYPE_NODATA:
                if(!TextUtils.isEmpty(errMsg)) {
                    emptyLayout.setNoDataContent(errMsg);
                }
                emptyLayout.setNodata();
                break;
            case ERROR_TYPE_NODATA_ENABLE_CLICK:
                if(!TextUtils.isEmpty(errMsg)) {
                    emptyLayout.setNoDataContent(errMsg);
                }
                emptyLayout.setNodataEnableClick();
                break;
            default:
                if(!TextUtils.isEmpty(errMsg)) {
                    emptyLayout.setNoDataContent(errMsg);
                }
                emptyLayout.setNodata();
                break;
        }
        webviewContainer.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);
        mMenuItem = menu.findItem(R.id.menu_collect);
        updateCollectionsMenu();
        return super.onCreateOptionsMenu(menu);
    }

    protected void updateCollectionsMenu() {
        if(isCollected){
            mMenuItem.setIcon(R.mipmap.star_focus);
        }else {
            mMenuItem.setIcon(R.mipmap.start_black);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.menu_share) {
            shareHelper.share(this,"分享到朋友圈");
        } else {
            //presenter.collectStory(story.getId());
            collect();
        }

        return true;
    }

    public abstract void collect();

    @Override
    public void collectSuccess() {
        isCollected = true;
        updateCollectionsMenu();
        Snackbar.make(webView,"Add it to collection successful",Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onFinish() {
        //setTitle(webView.getTitle());
        //this.hideLoading();
    }

    public abstract void onReloadClick();

    @Override
    public void onError(WebResourceError error) {
        LogUtils.v("web view onerror.................");
        error(ERROR_TYPE_NODATA_ENABLE_CLICK,null);
    }

    public void uncollectSuccess() {
        isCollected = false;
        updateCollectionsMenu();
    }

    public void updateCollectionFlag(boolean ifCollected) {
        this.isCollected = ifCollected;
        updateCollectionsMenu();
    }

    @Override
    protected void onDestroy() {
        if(webView!=null) {
            webView.removeCallBack();
            webView.removeAllViews();
            webView.setVisibility(View.GONE);
            webView.destroy();
            webView.releaseAllWebViewCallback();
//            webView = null;
            webviewContainer.removeView(webView);
        }

        setConfigCallback(null);

        super.onDestroy();
    }

    public void setConfigCallback(WindowManager windowManager) {
        try {
            Field field = WebView.class.getDeclaredField("mWebViewCore");
            field = field.getType().getDeclaredField("mBrowserFrame");
            field = field.getType().getDeclaredField("sConfigCallback");
            field.setAccessible(true);
            Object configCallback = field.get(null);

            if (null == configCallback) {
                return;
            }

            field = field.getType().getDeclaredField("mWindowManager");
            field.setAccessible(true);
            field.set(configCallback, windowManager);
        } catch(Exception e) {
        }
    }
}
