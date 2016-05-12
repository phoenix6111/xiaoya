package wanghaisheng.com.xiaoya.presenter.daily;

import android.accounts.NetworkErrorException;

import com.apkfuns.logutils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import wanghaisheng.com.xiaoya.api.Daily.DailyApi;
import wanghaisheng.com.xiaoya.api.SchedulersCompat;
import wanghaisheng.com.xiaoya.beans.Daily;
import wanghaisheng.com.xiaoya.beans.Story;
import wanghaisheng.com.xiaoya.datasource.DailyData;
import wanghaisheng.com.xiaoya.db.DBStory;
import wanghaisheng.com.xiaoya.db.DBStoryDao;
import wanghaisheng.com.xiaoya.presenter.base.BaseListPresenter;
import wanghaisheng.com.xiaoya.presenter.base.BaseListView;
import wanghaisheng.com.xiaoya.utils.ListUtils;

/**
 * Created by sheng on 2016/4/14.
 */
public class StoryListPresenter extends BaseListPresenter<StoryListView> {

    @Inject
    DailyApi dailyApi;
    @Inject
    DailyData dailyData;
    @Inject
    DBStoryDao storyDao;
    /*@Inject
    StoryCollectionDao storyCollection;*/

    @Inject
    public StoryListPresenter() {

    }


    /**
     * 第一次加载数据时调用，三级缓存：memory,disk,network
     * @param themeId
     */
    public void firstLoadData(final int themeId) {
        iView.showLoading();

        LogUtils.d(dailyData.getDataSourceText());

        Subscription subscription = dailyData.subscribeData(themeId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<Daily>() {
                @Override
                public void onCompleted() {
                    if(null != iView) {
                        iView.hideLoading();
                    }
                }

                @Override
                public void onError(Throwable e) {
                    LogUtils.d(e);
                    if(null != iView) {
                        iView.hideLoading();
                        if(e instanceof NetworkErrorException) {
                            iView.error(BaseListView.ERROR_TYPE_NETWORK,null);
                        } else {
                            iView.error(BaseListView.ERROR_TYPE_NODATA_ENABLE_CLICK,null);
                        }
                    }
                }

                @Override
                public void onNext(Daily daily) {
                    if(null != iView) {
                        iView.hideLoading();
                        iView.renderFirstLoadData(daily);
                    }
                }
            });

        compositeSubscription.add(subscription);
    }


    /**
     * 加载最新数据
     * @param themeId
     */
    public void loadNewestData(final int themeId) {
        Subscription subscription = dailyData.network(themeId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Daily>() {
                    @Override
                    public void call(Daily daily) {
                        if(null != iView) {
                            iView.refreshComplete(daily);
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
        compositeSubscription.add(subscription);
    }

    /**
     * 加载更多
     * @param lastDateTime
     */
    public void loadMoreData(int lastDateTime) {
        Subscription subscription = dailyApi.getDaily(lastDateTime)
                .compose(SchedulersCompat.<Daily>applyIoSchedulers())
                .subscribe(new Action1<Daily>() {
                    @Override
                    public void call(Daily daily) {
                        if(null != iView) {
                            iView.loadMoreComplete(daily);
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

        compositeSubscription.add(subscription);
    }

    private List<DBStory> convertStoryToDBStory(List<Story> stories,int themeId) {
        List<DBStory> dbStories = new ArrayList<>();
        for (Story story : stories) {
            DBStory dbStory = new DBStory();
            dbStory.setId((long)story.getId());
            dbStory.setTitle(story.getTitle());
            dbStory.setImage(story.getImage());
            dbStory.setTheme_id(themeId);
            if(!ListUtils.isEmpty(story.getImages())) {
                dbStory.setImages(story.getImages().get(0));
            }
//            daily.setCss(story.getCss().get(0));
            dbStory.setBody(story.getBody());
            dbStories.add(dbStory);
        }

        return dbStories;
    }

    private List<Story> convertDailyToStory(List<DBStory> dbStories) {
        List<Story> stories = new ArrayList<>();
        for (DBStory dbStory : dbStories) {
            Story story = new Story();
            story.setId(Integer.valueOf(dbStory.getId()+""));
            story.setTitle(dbStory.getTitle());
            story.setImage(dbStory.getImage());
            List<String> images = new ArrayList<>();
            images.add(dbStory.getImages());
            story.setImages(images);
            /*List<String> css = new ArrayList<>();
            css.add(daily.getCss());
            story.setCss(css);*/
            story.setBody(dbStory.getBody());
            stories.add(story);
        }
        return stories;
    }



}
