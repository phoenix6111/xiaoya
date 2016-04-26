package wanghaisheng.com.xiaoya.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sheng on 2016/4/12.
 */
public class BaseApi {

    protected String baseUrl;
    protected Retrofit retrofit;
    private static final int CONNECT_TIME_OUT = 5;

    protected BaseApi(String baseUrl) {
        this.baseUrl = baseUrl;

        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }


}
