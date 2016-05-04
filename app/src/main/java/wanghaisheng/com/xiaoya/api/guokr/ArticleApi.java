package wanghaisheng.com.xiaoya.api.guokr;

import rx.Observable;
import wanghaisheng.com.xiaoya.api.BaseApi;
import wanghaisheng.com.xiaoya.api.SchedulersCompat;
import wanghaisheng.com.xiaoya.beans.ArticleResult;

/**
 * Created by sheng on 2016/5/1.
 */
public class ArticleApi extends BaseApi {
    public static final String ARTICLE_DETAIL_URL = "http://apis.guokr.com/minisite/article/";

    private ArticleService articleService;

    public ArticleApi() {
        super(ARTICLE_DETAIL_URL);

        articleService = retrofit.create(ArticleService.class);
    }

    public Observable<ArticleResult> getArticleDetail(int articleid) {
        return articleService.getArticleDetail(articleid).compose(SchedulersCompat.<ArticleResult>applyIoSchedulers());
    }
}
