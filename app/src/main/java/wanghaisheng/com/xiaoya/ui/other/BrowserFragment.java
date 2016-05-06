package wanghaisheng.com.xiaoya.ui.other;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebView;
import android.widget.ProgressBar;

import java.util.logging.Logger;

import javax.inject.Inject;

import butterknife.Bind;
import wanghaisheng.com.xiaoya.R;
import wanghaisheng.com.xiaoya.ui.BaseFragment;
import wanghaisheng.com.xiaoya.ui.empty.EmptyLayout;
import wanghaisheng.com.xiaoya.utils.NetWorkHelper;
import wanghaisheng.com.xiaoya.utils.SettingPrefHelper;
import wanghaisheng.com.xiaoya.widget.XiaoYaWebView;

/**
 * Created by sheng on 2016/4/21.
 */
public class BrowserFragment extends BaseFragment implements XiaoYaWebView.XiaoYaWebViewCallBack {

    private Logger logger = Logger.getLogger(BrowserFragment.class.getSimpleName());

    public static final int ERROR_TYPE_NETWORK = 1;
    public static final int ERROR_TYPE_NODATA = 2;
    public static final int ERROR_TYPE_NODATA_ENABLE_CLICK = 3;
    public static final int ERROR_TYPE_UNKNOWN = 4;

    public static BrowserFragment newInstance(String url, String title) {
        BrowserFragment mFragment = new BrowserFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putString("title", title);
        mFragment.setArguments(bundle);
        return mFragment;
    }

    XiaoYaWebView webView;
    @Bind(R.id.progress)
    ProgressBar progress;
    @Bind(R.id.scrollView)
    NestedScrollView scrollView;

    @Inject
    Activity mActivity;
    @Inject
    SettingPrefHelper mSettingPrefHelper;
    @Inject
    NetWorkHelper netWorkHelper;

    protected EmptyLayout emptyLayout;
    protected int mStoreEmptyState = -1;//保存EmptyLayout的状态信息


    private String url;
    private String title;

    @Override
    public void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.browser_fragment;
    }

    @Override
    public void getSavedBundle(Bundle bundle) {
        url = bundle.getString("url");
        title = bundle.getString("title");
    }

    @Override
    public void beforeInitView(View view) {

    }

    @Override
    public void initView(View view) {
        webView = new XiaoYaWebView(getActivity());
        NestedScrollView.LayoutParams layoutParams = new NestedScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        webView.setLayoutParams(layoutParams);
        webView.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(),R.color.window_color));
        scrollView.removeAllViews();
        scrollView.addView(webView);

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                if (!TextUtils.isEmpty(BrowserFragment.this.title)) {
                    mActivity.setTitle(BrowserFragment.this.title);
                }
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progress.setProgress(newProgress);
                if (newProgress == 100) {
                    progress.setVisibility(View.GONE);
                } else {
                    progress.setVisibility(View.VISIBLE);
                }
            }
        });

        checkNetWork();
    }

    @Override
    public void initData() {
        if (mSettingPrefHelper.getNightModel() && url.contains("?")) {
            url += "&night=1";
        }

        if(checkNetWork()) {
            webView.loadUrl(url);
        }
    }

    public void reload() {
        if(checkNetWork()) {
            webView.reload();
        }
    }

    public void hideLoading() {
        emptyLayout.dismiss();
        scrollView.setVisibility(View.VISIBLE);
    }

    public void showLoading() {
        emptyLayout.setNetworkLoading();
        scrollView.setVisibility(View.GONE);
    }

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
        scrollView.setVisibility(View.GONE);
    }

    public boolean checkNetWork() {
        if(!netWorkHelper.isAvailableNetwork()) {
            error(ERROR_TYPE_NETWORK,null);
            return false;
        }

        return true;
    }


    @Override
    public void onFinish() {
        getActivity().setTitle(webView.getTitle());
    }

    @Override
    public void onUpdatePager(int page, int total) {

    }

    @Override
    public void onError(WebResourceError error) {
        error(ERROR_TYPE_NETWORK,null);
    }

    @Override
    public void onPause() {
        webView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        webView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        if(null != webView) {
            webView.removeCallBack();
            webView.setVisibility(View.GONE);
            webView.removeAllViews();
            webView.destroy();
        }
        super.onDestroy();
    }
}
