package wanghaisheng.com.xiaoya.api.jianshu;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Subscriber;
import wanghaisheng.com.xiaoya.api.converter.JianshuContentParser;
import wanghaisheng.com.xiaoya.api.converter.JianshuDetailConverter;
import wanghaisheng.com.xiaoya.api.converter.JianshuGroupConverter;

/**
 * Created by sheng on 2016/5/10.
 */
public class JianshuApi{
    public static final String BASE_URL = "http://www.jianshu.com/";

    public static final String INDEX_HOT_URL = "trending/now";
    public static final String INDEX_NEW_URL = "recommendations/notes?category_id=56";
    public static final String INDEX_DAYLY_URL = "recommendations/notes?category_id=60";
    public static final String INDEX_WEEKLY_URL = "trending/weekly";
    public static final String INDEX_MONTHLY_URL = "trending/monthly";

    public static final String[] CHANNEL_TAG = {"hot","new","dayly","weekly","monthly"};
    public static final String[] CHANNEL_NAME = {"热门","新上榜","当日热门","七日热门","当月热门"};

    private Retrofit retrofit;
    private JianshuService jianshuService;
    private OkHttpClient okHttpClient;

    public JianshuApi(OkHttpClient client) {
        this.okHttpClient = client;
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(new JianshuGroupConverter())
                .addConverterFactory(new JianshuDetailConverter())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        jianshuService = retrofit.create(JianshuService.class);
    }

    /**
     * 根据channel获取首页的数据
     * @param channel
     * @return
     */
    public Observable<JianshuContentResult> getIndexPageContentResult(String channel) {
        Observable<JianshuContentResult> observable;
        switch (channel) {
            case "hot":
                observable = getGroupResult(INDEX_HOT_URL);
                break;
            case "new":
                observable = getGroupResult(INDEX_NEW_URL);
                break;
            case "dayly":
                observable = getGroupResult(INDEX_DAYLY_URL);
                break;
            case "weekly":
                observable = getGroupResult(INDEX_WEEKLY_URL);
                break;
            case "monthly":
                observable = getGroupResult(INDEX_MONTHLY_URL);
                break;
            default:
                observable = getGroupResult(INDEX_HOT_URL);
                break;
        }


        return observable;
    }

    /**
     * 获取下一页的数据
     * @param url
     * @return
     */
    public Observable<JianshuContentResult> getNextPageContentResult(String url) {
        return getGroupResult(url);
    }

    /**
     * 根据URL获取 列表数据和下一页URL
     * @param url
     * @return
     */
    public Observable<JianshuContentResult> getGroupResult(final String url) {
        return Observable.create(new Observable.OnSubscribe<JianshuContentResult>() {
            @Override
            public void call(final Subscriber<? super JianshuContentResult> subscriber) {
                OkHttpUtils.get().url(BASE_URL+url).build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e) {
                                subscriber.onError(e);
                            }

                            @Override
                            public void onResponse(String response) {
                                JianshuContentResult result = JianshuContentParser.parseGroup(response);
                                subscriber.onNext(result);
                                subscriber.onCompleted();
                            }
                        });
            }
        });
    }

    public Observable<String> getDetailContent(final String url) {
        //return jianshuService.getDetailContent(url);
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                OkHttpUtils.get().url(BASE_URL+url).build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e) {
                                    subscriber.onError(e);
                                }

                                @Override
                                public void onResponse(String response) {
                                    String content = JianshuContentParser.getJianshuDetailContent(response);
                                    subscriber.onNext(content);
                                    subscriber.onCompleted();
                                }
                            });
            }
        });
    }
}
