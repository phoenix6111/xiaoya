package wanghaisheng.com.xiaoya.presenter.daily;

import android.accounts.NetworkErrorException;

import com.apkfuns.logutils.LogUtils;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import wanghaisheng.com.xiaoya.api.Daily.DailyApi;
import wanghaisheng.com.xiaoya.api.SchedulersCompat;
import wanghaisheng.com.xiaoya.beans.Story;
import wanghaisheng.com.xiaoya.db.DBStory;
import wanghaisheng.com.xiaoya.db.DBStoryDao;
import wanghaisheng.com.xiaoya.db.StoryCollection;
import wanghaisheng.com.xiaoya.db.StoryCollectionDao;
import wanghaisheng.com.xiaoya.presenter.base.BaseDetailPresenter;
import wanghaisheng.com.xiaoya.presenter.base.BaseListView;
import wanghaisheng.com.xiaoya.utils.ListUtils;

/**
 * Created by sheng on 2016/4/15.
 */
public class StoryDetailPresenter extends BaseDetailPresenter<Story,StoryDetailView> {
    private static final String TAG = "StoryDetailPresenter";

    @Inject
    DailyApi dailyApi;

    @Inject
    StoryCollectionDao storyCollectionDao;
    @Inject
    DBStoryDao dbStoryDao;

    @Inject @Singleton
    public StoryDetailPresenter() {}

    @Override
    public void detachView() {
        if (null != iView) {
            iView = null;
        }
    }

    /**
     * 通过DailyApi加载Story的详细数据
     */
    public void loadEntityDetail(int storyId) {
        subscription = dailyApi.getStory(storyId)
                .compose(SchedulersCompat.<Story>applyIoSchedulers())
                .subscribe(new Action1<Story>() {
                    @Override
                    public void call(Story story) {
                        String webPageContent = buildPageStr(story);
//                        LogUtils.v(webPageContent);
                        iView.renderWebview(webPageContent);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (throwable instanceof NetworkErrorException) {
                            iView.error(BaseListView.ERROR_TYPE_NETWORK,null);
                        } else {
                            iView.error(BaseListView.ERROR_TYPE_NODATA_ENABLE_CLICK,null);
                        }
                    }
                });
    }

    /**
     * 根据story的数据，拼接html数据
     * @param story
     * @return
     */
    private String buildPageStr(Story story) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head><meta charset=\"utf-8\"><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\"><meta name=\"viewport\" content=\"user-scalable=no, width=device-width\">");
        sb.append("<title>")
            .append(story.getTitle())
            .append("</title>");
        sb.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"story_detail.css\" /></head><body>");
        sb.append(story.getBody());
        sb.append("</body></html>");

        return sb.toString();
    }

    //收藏story
    public void collectEntity(final Story story) {
        DBStory dbStory = convertStoryToDBStory(story);

        LogUtils.v(TAG,"print DBSTORY................");
        LogUtils.v(dbStory);

        dbStory.setIs_collected(true);
        dbStoryDao.insertOrReplace(dbStory);
        StoryCollection storyCollection = convertStoryToStoryCollection(story);
        storyCollectionDao.insertOrReplace(storyCollection);

        iView.collectSuccess();
    }

    /**
     * 取消收藏
     * @param story
     */
    public void unCollectEntity(final Story story) {
        DBStory dbStory = convertStoryToDBStory(story);
        dbStory.setIs_collected(false);
        dbStoryDao.delete(dbStory);
        StoryCollection storyCollection = convertStoryToStoryCollection(story);
        storyCollectionDao.delete(storyCollection);

        iView.uncollectSuccess();
    }

    /**
     * 检测是否article已经收藏
     * @param story
     * @return
     */
    public void checkIfCollected(final Story story) {
        Observable<StoryCollection> articleCollectionObservable = Observable.create(new Observable.OnSubscribe<StoryCollection>() {
            @Override
            public void call(Subscriber<? super StoryCollection> subscriber) {
                subscriber.onNext(storyCollectionDao.load((long) story.getId()));
            }
        });

        articleCollectionObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<StoryCollection>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.d(e.getMessage());
                    }

                    @Override
                    public void onNext(StoryCollection storyCollection) {
                        iView.updateCollectionFlag(null!=storyCollection);
                    }
                });

    }

    /**
     * 复制daily中的属性到DailyCollection
     * @param story
     * @return
     */
    private StoryCollection convertStoryToStoryCollection(Story story) {
        StoryCollection collection = new StoryCollection();
        collection.setId((long)story.getId());
        collection.setTitle(story.getTitle());
        collection.setImage(story.getImage());
        if(!ListUtils.isEmpty(story.getImages())) {
            collection.setImages(story.getImages().get(0));
        }

        collection.setBody(story.getBody());
        collection.setShare_url(story.getShare_url());

        return collection;
    }

    /**
     * 复制Story中的数据到DBStory中
     * @param story
     * @return
     */
    private DBStory convertStoryToDBStory(Story story) {
        DBStory dbStory = new DBStory();
        dbStory.setId((long) story.getId());
        dbStory.setTitle(story.getTitle());
        dbStory.setImage(story.getImage());
        if(!ListUtils.isEmpty(story.getImages())) {
            dbStory.setImages(story.getImages().get(0));
        }

        dbStory.setBody(story.getBody());
        dbStory.setShare_url(story.getShare_url());

        return dbStory;
    }


}
