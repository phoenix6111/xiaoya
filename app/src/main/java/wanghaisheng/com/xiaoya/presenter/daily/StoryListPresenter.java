package wanghaisheng.com.xiaoya.presenter.daily;

import com.apkfuns.logutils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import wanghaisheng.com.xiaoya.api.Daily.DailyApi;
import wanghaisheng.com.xiaoya.beans.Daily;
import wanghaisheng.com.xiaoya.beans.Story;
import wanghaisheng.com.xiaoya.db.DBStory;
import wanghaisheng.com.xiaoya.db.DBStoryDao;
import wanghaisheng.com.xiaoya.presenter.base.Presenter;
import wanghaisheng.com.xiaoya.utils.ListUtils;

/**
 * Created by sheng on 2016/4/14.
 */
public class StoryListPresenter extends Presenter<Story,TopDailyView> {

    @Inject
    DailyApi dailyApi;
    @Inject
    DBStoryDao storyDao;
    /*@Inject
    StoryCollectionDao storyCollection;*/

    private DailyListView dailyListView;
    private int lastDateTime = 0;
    private boolean isFirst = true;
    @Inject @Singleton
    public StoryListPresenter() {

    }

    /**
     * 查询顶部viewpager的数据，暂时无用
     */
    public void loadTopStories() {
        dailyApi.getDaily(lastDateTime).subscribe(new Subscriber<Daily>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LogUtils.v(e);
                LogUtils.v("error................................................");
                //dailyListView.error("数据访问异常，请稍后重试");
            }

            @Override
            public void onNext(Daily daily) {
                LogUtils.v(daily);
                iView.renderStories(daily.getTop_stories());
            }
        });
    }

    /**
     * 根据themeId从数据库查询数据
     * @param themeId
     */
    public void loadFromDb(final int themeId) {
        //LogUtils.v("load newest stories...........................................................");
        //先从数据库里面查
        dailyListView.showLoading();
        Observable.create(new Observable.OnSubscribe<List<Story>>() {
            @Override
            public void call(Subscriber<? super List<Story>> subscriber) {
                subscriber.onNext(loadNewestFromDb(themeId));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Story>>() {
                    @Override
                    public void call(List<Story> stories) {
                        LogUtils.v("theme id..........................."+themeId);
                        LogUtils.v(stories);
                        dailyListView.hideLoading();
                        dailyListView.renderDbData(stories);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtils.d(throwable.getMessage());
                        dailyListView.hideLoading();
                        dailyListView.error("数据访问异常，请刷新后重试");
                    }
                });

    }

    public void loadNewestFromNet(final int themeId, final boolean showLoading) {
        if(showLoading) {
            dailyListView.showLoading();
        }
        Subscriber<Daily> subscriber = new Subscriber<Daily>() {
            @Override
            public void onCompleted() {
                //如果是第一次从远程加载，则不显示loadmore的下拉框
                if(showLoading) {
                    dailyListView.hideLoading();
                }
            }

            @Override
            public void onError(Throwable e) {
                LogUtils.v(e);
                if(showLoading) {
                    dailyListView.hideLoading();
                }
                dailyListView.error("数据访问异常，请刷新重试");

            }

            @Override
            public void onNext(Daily daily) {
//                        LogUtils.v("it the first+++++++++++"+isFirst+".....................");
                List<Story> stories = daily.getStories();
                if(themeId!=1&&!ListUtils.isEmpty(stories)) {
                    stories.remove(0);
                }
//                LogUtils.v("theme id ........."+themeId);
//                LogUtils.v(stories);
                if(isFirst) {
                    isFirst = false;
                    dailyListView.renderNetData(stories);
//                            LogUtils.v("it the first  inner inner+++++++++++" + isFirst + ".....................");
                } else {
                    dailyListView.refreshComplete(stories);
                }
                //                    LogUtils.v("inner inner+++++++++++" + isFirst + ".....................");
//                        LogUtils.v(daily.getStories());

                if(!ListUtils.isEmpty(stories)) {
                    saveToDb(stories,themeId);
                    lastDateTime = daily.getDate();
                }

            }
        };

        if(themeId==DailyApi.THEME_ID[0]) {
            dailyApi.getDaily(lastDateTime)
                    .subscribe(subscriber);
        } else {
            dailyApi.getDailyByTheme(themeId)
                    .subscribe(subscriber);
        }


    }


    /**
     * 一开始从数据库里面查，显示到界面，然后再从网络查最新的数据
     */
    private List<Story> loadNewestFromDb(int themeId) {
        List<DBStory> dbStories = storyDao.queryBuilder().where(DBStoryDao.Properties.Theme_id.eq(themeId)).list();
        return convertDailyToStory(dbStories);
    }

    /**
     * 将最新数据写入数据库，先将旧的数据全部删除，数据库只保存最新的数据
     * @param stories
     */
    private void saveToDb(final List<Story> stories,int themeId) {
        if(stories == null || stories.isEmpty()){
            return;
        }

        //删除daily表的全部themeId数据
        List<DBStory> oldStories = storyDao.queryBuilder().where(DBStoryDao.Properties.Theme_id.eq(themeId)).list();
        storyDao.deleteInTx(oldStories);
        final List<DBStory> dbStories = convertStoryToDBStory(stories,themeId);
        //批量保存daily数据
        storyDao.getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                for(int i=0; i<stories.size(); i++){
                    DBStory dbStory = dbStories.get(i);
                    storyDao.insertOrReplace(dbStory);
                }
            }
        });

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

    public void loadMoreStories() {

        dailyApi.getDaily(lastDateTime)
                .subscribe(new Subscriber<Daily>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        dailyListView.error("加载数据出错");
                    }

                    @Override
                    public void onNext(Daily daily) {
                        dailyListView.loadMoreComplete(daily.getStories());
                        lastDateTime = daily.getDate();
                    }
                });
    }

    @Override
    public void detachView() {
        if(this.iView != null) {
            this.iView = null;
        }
        if(this.dailyListView!=null) {
            this.dailyListView = null;
        }
    }

    public void attachDailyListView(DailyListView dailyListView) {
        this.dailyListView = dailyListView;
    }

}
