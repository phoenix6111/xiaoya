package wanghaisheng.com.xiaoya.presenter.jianshu;

import android.accounts.NetworkErrorException;

import com.apkfuns.logutils.LogUtils;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import wanghaisheng.com.xiaoya.api.jianshu.JianshuApi;
import wanghaisheng.com.xiaoya.db.Content;
import wanghaisheng.com.xiaoya.presenter.base.BaseDetailPresenter;
import wanghaisheng.com.xiaoya.presenter.base.BaseListView;

/**
 * Created by sheng on 2016/5/10.
 */
public class JianshuDetailPresenter extends BaseDetailPresenter<JianshuDetailView> {

    @Inject
    JianshuApi jianshuApi;

    @Inject
    public JianshuDetailPresenter(){}

    @Override
    public void loadEntityDetail(int entityId) {

    }

    public void collectEntity(Content entity) {

    }

    public void loadContentHtml(String url) {
        if(null != iView) {
            iView.showLoading();
        }
        jianshuApi.getDetailContent(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String content) {
                        String html = buildHtml(content);
                        if (null != iView) {
                            iView.hideLoading();
                            iView.renderWebview(html);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtils.d(throwable);
                        if(null != iView) {
                            if (throwable instanceof NetworkErrorException) {
                                iView.error(BaseListView.ERROR_TYPE_NETWORK,null);
                            } else {
                                iView.error(BaseListView.ERROR_TYPE_NODATA_ENABLE_CLICK,null);
                            }
                        }
                    }
                });
    }

    public String buildHtml(String content) {
        StringBuilder builder = new StringBuilder();

        String header = "<!DOCTYPE html>\n" +
                "<head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=Edge\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0,user-scalable=no\">\n" +
                "    <meta http-equiv=\"Cache-Control\" content=\"no-siteapp\"/>\n" +
                "    <link rel=\"stylesheet\" media=\"all\" href=\"jianshu_detail.css\"/>\n" +
                "<script src=\"jquery.js\"></script><script src=\"disable_a.js\"></script>"+
                "</head>\n" +
                "\n" +
                "<body class=\"post output zh cn win reader-day-mode reader-font2 \">\n" +
                "<div id=\"show-note-container\">\n" +
                "    <div class=\"post-bg\" id=\"flag\">\n" +
                "        <div class=\"container\">\n" +
                "            <div class=\"article\">\n" +
                "                <div class=\"preview\">\n";
        builder.append(header)
                .append(content);

        String footer = "</div>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";
        builder.append(footer);

        return builder.toString();
    }
}
