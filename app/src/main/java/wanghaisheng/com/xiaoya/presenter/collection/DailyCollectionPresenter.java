package wanghaisheng.com.xiaoya.presenter.collection;

import android.text.TextUtils;

import com.apkfuns.logutils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import wanghaisheng.com.xiaoya.beans.Story;
import wanghaisheng.com.xiaoya.db.StoryCollection;
import wanghaisheng.com.xiaoya.db.StoryCollectionDao;
import wanghaisheng.com.xiaoya.presenter.base.Presenter;

/**
 * Created by sheng on 2016/4/20.
 */
public class DailyCollectionPresenter extends Presenter<Story,DailyCollectionView> {
    private static final String TAG = "DailyCollectionPresenter";

    @Inject
    StoryCollectionDao storyCollectionDao;

    @Inject @Singleton
    public DailyCollectionPresenter() {}

    /**
     * 从数据库加载数据
     * @param page
     */
    public void loadStoryFromDb(final int page) {
//        LogUtils.v(TAG,"execute method loadStoryFromDB................");
        iView.showLoading();
        Observable<List<Story>> listObservable = Observable.create(new Observable.OnSubscribe<List<Story>>() {
            @Override
            public void call(Subscriber<? super List<Story>> subscriber) {
                subscriber.onNext(loadStories());
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io());
        listObservable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<List<Story>>() {
            @Override
            public void onCompleted() {
//                LogUtils.v(TAG,"execute onCompleted.....................................");
                iView.hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                LogUtils.d(e);
                iView.error("数据访问异常，请稍后重试");
                iView.hideLoading();
            }

            @Override
            public void onNext(List<Story> stories) {
//                LogUtils.v(TAG,"onNext.........................");
//                LogUtils.v(stories);
//                iView.hideLoading();
                iView.renderStories(page,stories);
            }
        });
    }

    public List<Story> loadStories() {
        List<StoryCollection> storyCollections = storyCollectionDao.loadAll();
        return convertStoryCollectionToStory(storyCollections);
    }

    /**
     * 将StoryCollection转换为Story
     * @param storyCollections
     * @return
     */
    public List<Story> convertStoryCollectionToStory(List<StoryCollection> storyCollections) {
        List<Story> stories = new ArrayList<>();

        for (StoryCollection storyCollection : storyCollections) {
            Story story = new Story();
            story.setId(Integer.valueOf(storyCollection.getId()+""));
            story.setTitle(storyCollection.getTitle());
            story.setBody(storyCollection.getBody());
            story.setImage(storyCollection.getImage());
            if(!TextUtils.isEmpty(storyCollection.getImages())) {
                List<String> images = new ArrayList<>();
                images.add(storyCollection.getImages());
                story.setImages(images);
            }

            if(!TextUtils.isEmpty(storyCollection.getCss())) {
                List<String> csss = new ArrayList<>();
                csss.add(storyCollection.getCss());
            }
            story.setShare_url(storyCollection.getShare_url());
            stories.add(story);
        }

        return stories;
    }


    @Override
    public void detachView() {
        if(null != iView) {
            iView = null;
        }
    }
}
