package wanghaisheng.com.xiaoya.api.science;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;
import wanghaisheng.com.xiaoya.beans.ArticleResult;

/**
 * Created by sheng on 2016/5/1.
 */
public interface ArticleService {
    @GET("{article_id}.json")
    Observable<ArticleResult> getArticleDetail(@Path("article_id") int articleId);
}
