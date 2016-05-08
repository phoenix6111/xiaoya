package wanghaisheng.com.xiaoya.api.meizi;

import android.graphics.BitmapFactory;

import com.zhy.http.okhttp.OkHttpUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import wanghaisheng.com.xiaoya.api.converter.MeiziGroupConverter;
import wanghaisheng.com.xiaoya.api.converter.MeiziPersonConverter;
import wanghaisheng.com.xiaoya.beans.GroupResult;
import wanghaisheng.com.xiaoya.db.Content;
import wanghaisheng.com.xiaoya.db.Group;
import wanghaisheng.com.xiaoya.utils.UrlUtils;

/**
 * Created by sheng on 2016/5/6.
 */
public class MeiziApi {
    public static String BASE_URL = "http://www.mzitu.com/";

    public static final String[] CHANNEL_TAG = {"home", "xinggan", "taiwan", "japan", "mm"};
    public static final String[] CHANNEL_TITLE = {"首页", "性感妹子", "台湾妹子", "日本妹子", "清纯妹子"};

    private MeiziService meiziService;
    protected Retrofit retrofit;
    private OkHttpClient okHttpClient;

    public static final String LOAD_IMAGE_INFO_TAG = "load_image_info";

    public MeiziApi(OkHttpClient client) {
        this.okHttpClient = client;
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(new MeiziPersonConverter())
                .addConverterFactory(new MeiziGroupConverter())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        meiziService = retrofit.create(MeiziService.class);
    }

    public Observable<List<Group>> getGroup(final String type, int page) {
        final String tempType = (type.equals("home")) ? "" : type;
        return meiziService.getGroup(tempType, page)
                //添加type
                .map(new Func1<List<Group>, List<Group>>() {
                    @Override
                    public List<Group> call(List<Group> groups) {
//                        LogUtils.d(s);
                        List datas = new ArrayList();
                        for (Group group : groups) {
                            group.setType(type);
                            datas.add(group);
                        }
                        groups = null;
                        return datas;
                    }
                });
    }

    public Observable<Content> getContents(final String groupid) {

        final Observable<Content> temp = getPersonListResult(groupid)
                .subscribeOn(Schedulers.io())
                .map(new Func1<GroupResult, List<Content>>() {
                    @Override
                    public List<Content> call(final GroupResult groupResult) {
//                        LogUtils.d(groupResult);
                        List<Content> datas = new ArrayList<Content>();
                        for(int i=1; i<groupResult.getCount(); ) {
                            Content content = new Content();
                            String url = UrlUtils.handleUrl(groupResult.getIndexImageUrl(), i);
                            content.setUrl(url);
                            content.setGroupid(groupid);
                            content.setOrder(Integer.parseInt(groupid + (i++)));
                            datas.add(content);
                        }

                        return datas;
                    }
                }).flatMap(new Func1<List<Content>, Observable<Content>>() {
                    @Override
                    public Observable<Content> call(List<Content> contents) {
                        return Observable.from(contents);
                    }
                }).map(new Func1<Content, Content>() {
                    @Override
                    public Content call(Content content) {
                        /*try {
                            content = handleContent(content,groupid);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }*/
                        return content;
                    }
                }).doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        //当取消订阅的时候，取消没有完成的加载图片信息的请求
                        OkHttpUtils.getInstance().cancelTag(LOAD_IMAGE_INFO_TAG);
                    }
                });

        return temp;
        /*Observable<List<Content>> observable = Observable.create(new Observable.OnSubscribe<List<Content>>() {
            @Override
            public void call(final Subscriber<? super List<Content>> subscriber) {
                final List<Content> contents = new ArrayList<Content>();
                temp.observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Content>() {
                            @Override
                            public void onCompleted() {
                                subscriber.onNext(contents);
                                subscriber.onCompleted();
                            }

                            @Override
                            public void onError(Throwable e) {
                                subscriber.onError(e);
                            }

                            @Override
                            public void onNext(Content content) {
                                contents.add(content);
                            }
                        });
            }
        });*/

//        return observable;
    }

    private Content handleContent(Content content,String groupId) throws IOException {

        Response response = OkHttpUtils.getInstance(okHttpClient).get().url(content.getUrl()).tag(LOAD_IMAGE_INFO_TAG).build().execute();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(response.body().byteStream(), null, options);
        content.setImagewidth(options.outWidth);
        content.setImageheight(options.outHeight);
        return content;
    }


    public Observable<GroupResult> getPersonListResult(String groupId) {
        return meiziService.getContentResult(groupId);
    }


}
