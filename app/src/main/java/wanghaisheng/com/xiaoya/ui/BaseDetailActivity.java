package wanghaisheng.com.xiaoya.ui;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;

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
public abstract class BaseDetailActivity extends BaseSwipeBackActivity implements BaseDetailView {

    @Bind(R.id.empty_layout)
    protected EmptyLayout emptyLayout;
    protected int mStoreEmptyState = -1;//保存EmptyLayout的状态信息
    @Bind(R.id.toolbar)
    protected Toolbar mToolbar;

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
        initToolbar(mToolbar);

        mActivityComponent.inject(this);

        webView = new XiaoYaWebView(this);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        webView.setLayoutParams(layoutParams);
        webView.setBackgroundColor(ContextCompat.getColor(mAppContext,R.color.window_color));
        webviewContainer.removeAllViews();
        webviewContainer.addView(webView);

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

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
    }

    public abstract void collect();

    @Override
    public void collectSuccess() {
        isCollected = true;
        updateCollectionsMenu();
        Snackbar.make(webView,"Add it to collection successful",Snackbar.LENGTH_SHORT).show();
    }

    public abstract void onReloadClick();

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
            webView.setVisibility(View.GONE);
            webView.removeAllViews();
            webView.destroy();

            webviewContainer.removeAllViews();
        }

        super.onDestroy();
    }

}
