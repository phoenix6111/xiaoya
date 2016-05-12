package wanghaisheng.com.xiaoya.ui.jianshu;

import android.os.Bundle;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import butterknife.Bind;
import wanghaisheng.com.xiaoya.R;
import wanghaisheng.com.xiaoya.api.jianshu.JianshuContent;
import wanghaisheng.com.xiaoya.presenter.jianshu.JianshuDetailPresenter;
import wanghaisheng.com.xiaoya.presenter.jianshu.JianshuDetailView;
import wanghaisheng.com.xiaoya.ui.BaseDetailActivity;
import wanghaisheng.com.xiaoya.utils.DateUtils;

/**
 * Created by sheng on 2016/5/10.
 */
public class JianshuDetailActivity extends BaseDetailActivity implements JianshuDetailView{
    public static final String ARG_JIANSHU_DETAIL = "arg_detail";

    @Inject
    JianshuDetailPresenter presenter;
    private JianshuContent content;

    @Bind(R.id.tv_title)
    TextView jianshuTitle;
    @Bind(R.id.tv_read)
    TextView readCount;
    @Bind(R.id.tv_date)
    TextView createDate;

    @Override
    public void collect() {

    }

    @Override
    public void onReloadClick() {
        if(checkNetWork()&& (null != presenter)&&(null != content)) {
            presenter.loadContentHtml(content.getUrl());
        }
    }

    @Override
    public void getDatas(Bundle savedInstanceState) {
        this.content = (JianshuContent) getIntent().getSerializableExtra(ARG_JIANSHU_DETAIL);
    }

    @Override
    protected boolean isApplyStatusBarTranslucency() {
        return false;
    }

    @Override
    protected boolean isApplyStatusBarColor() {
        return false;
    }

    @Override
    protected void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.jianshu_detail;
    }

    @Override
    public void initView() {
        if(null != content) {
            mToolbar.setTitle(content.getTitle());
            setTitle(content.getTitle());

            jianshuTitle.setText(content.getTitle());
            readCount.setText(content.getReadCount()+"");
            SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date date = simple.parse(content.getDate());
                String resultDate = DateUtils.format(date);
                createDate.setText(resultDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initData() {
        presenter.attachView(this);
        //检测是否有网络连接再加载数据
        if(checkNetWork() && (null != presenter)&&(null != content)) {
            presenter.loadContentHtml(content.getUrl());
        }

    }

    @Override
    public void renderWebview(String webPageStr) {
        webView.loadDataWithBaseURL("file:///android_asset/",webPageStr,"text/html", "utf-8", null);
    }

    @Override
    protected void onDestroy() {
        if(null != presenter) {
            presenter.detachView();
            this.presenter = null;
        }
        super.onDestroy();
    }
}
