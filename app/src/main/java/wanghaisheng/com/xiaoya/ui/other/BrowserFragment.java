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
import butterknife.ButterKnife;
import wanghaisheng.com.xiaoya.R;
import wanghaisheng.com.xiaoya.ui.BaseFragment;
import wanghaisheng.com.xiaoya.utils.NetWorkHelper;
import wanghaisheng.com.xiaoya.utils.SettingPrefHelper;
import wanghaisheng.com.xiaoya.widget.XiaoYaWebView;

/**
 * Created by sheng on 2016/4/21.
 */
public class BrowserFragment extends BaseFragment implements XiaoYaWebView.XiaoYaWebViewCallBack {

    private Logger logger = Logger.getLogger(BrowserFragment.class.getSimpleName());

    public static BrowserFragment newInstance(String url, String title) {
        BrowserFragment mFragment = new BrowserFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putString("title", title);
        mFragment.setArguments(bundle);
        return mFragment;
    }


//    @Bind(R.id.webview)
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
    public void getBundle(Bundle bundle) {
        url = bundle.getString("url");
        title = bundle.getString("title");
        if (mSettingPrefHelper.getNightModel() && url.contains("?")) {
            url += "&night=1";
        }
    }

    @Override
    public void initUI(View view) {
        ButterKnife.bind(this, view);
        showContent(true);

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
        if(checkNetWork()) {
            webView.loadUrl(url);
        }
    }

    public void reload() {
        if(checkNetWork()) {
            webView.reload();
        }
    }

    public boolean checkNetWork() {
        if(!netWorkHelper.isAvailableNetwork()) {
            setErrorText("网络访问异常，请刷新后重试");
            showError(false);
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
        setErrorText("网络访问异常，请刷新后重试");
        showError(false);
    }

    @Override
    public void onDestroy() {
        if(null != webView) {
            webView.removeCallBack();
            webView.removeAllViews();
            webView.destroy();
        }
        super.onDestroy();
    }
}
