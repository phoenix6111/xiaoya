package wanghaisheng.com.xiaoya.api.science;

import okhttp3.OkHttpClient;
import rx.Observable;
import wanghaisheng.com.xiaoya.api.BaseApi;
import wanghaisheng.com.xiaoya.beans.ArticleResult;

/**
 * Created by sheng on 2016/5/1.
 */
public class ArticleApi extends BaseApi {
    public static final String ARTICLE_DETAIL_URL = "http://apis.guokr.com/minisite/article/";

    private ArticleService articleService;

    public ArticleApi(OkHttpClient client) {
        super(ARTICLE_DETAIL_URL,client);

        articleService = retrofit.create(ArticleService.class);
    }

    public Observable<ArticleResult> getArticleDetail(int articleid) {
        return articleService.getArticleDetail(articleid);
//                .compose(SchedulersCompat.<ArticleResult>applyIoSchedulers());
    }
}
