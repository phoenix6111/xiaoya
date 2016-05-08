package wanghaisheng.com.xiaoya.api;

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

    protected BaseApi(String baseUrl,OkHttpClient client) {
        this.baseUrl = baseUrl;

        retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }


}
