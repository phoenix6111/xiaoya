package wanghaisheng.com.xiaoya.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wanghaisheng.com.xiaoya.R;
import wanghaisheng.com.xiaoya.presenter.base.BaseDetailView;
import wanghaisheng.com.xiaoya.utils.ShareHelper;
import wanghaisheng.com.xiaoya.widget.ProgressBarCircularIndeterminate;
import wanghaisheng.com.xiaoya.widget.XiaoYaWebView;

/**
 * Created by sheng on 2016/4/15.
 */
public abstract class BaseDetailActivity extends BaseSwipeBackActivity implements XiaoYaWebView.XiaoYaWebViewCallBack,BaseDetailView {
    @Bind(R.id.tvLoading)
    protected TextView tvLoading;
    @Bind(R.id.progress_container)
    protected LinearLayout progressContainer;
    @Bind(R.id.rlProgress)
    protected RelativeLayout rlProgress;
    @Bind(R.id.rlError)
    protected RelativeLayout rlError;
    @Bind(R.id.tvError)
    protected TextView tvError;
    @Bind(R.id.progress_view)
    protected ProgressBarCircularIndeterminate progressBar;
//    @Bind(R.id.webview)
    protected XiaoYaWebView webView;
    @Bind(R.id.scrollView)
    protected NestedScrollView scrollView;
    @Bind(R.id.btnReload)
    protected Button reloadBtn;

    @Inject
    ShareHelper shareHelper;
    protected boolean isCollected;

    protected MenuItem mMenuItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initUiAndListener() {
        mActivityComponent.inject(this);
        ButterKnife.bind(this);

        webView = new XiaoYaWebView(this);
        NestedScrollView.LayoutParams layoutParams = new NestedScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        webView.setLayoutParams(layoutParams);
        webView.setBackgroundColor(ContextCompat.getColor(mAppContext,R.color.window_color));
        scrollView.removeAllViews();
        scrollView.addView(webView);

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

        initUIAndDatas();
    }

    public abstract void initUIAndDatas();

    @Override
    public void showLoading() {
        rlProgress.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);
        rlError.setVisibility(View.GONE);
    }

    public boolean checkNetWork() {
        if(!netWorkHelper.isAvailableNetwork()) {
            onError("无网络连接，请连接后重试。。");
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void hideLoading() {
        rlProgress.setVisibility(View.GONE);
        scrollView.setVisibility(View.VISIBLE);
        rlError.setVisibility(View.GONE);
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
            mMenuItem.setIcon(R.mipmap.star_normal);
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

    @OnClick(R.id.btnReload)
    public void onClick(Button button) {
        webView.reload();
    }

    @Override
    public void onError(String error) {
        rlProgress.setVisibility(View.GONE);
        scrollView.setVisibility(View.GONE);
        rlError.setVisibility(View.VISIBLE);
        tvError.setText(error);
    }

    @Override
    public void onError(WebResourceError error) {
        LogUtils.v("web view onerror.................");
        onError("加载错误，请重试");
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
        }
        super.onDestroy();

    }
}
