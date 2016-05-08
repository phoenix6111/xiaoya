package wanghaisheng.com.xiaoya.presenter.feedback;

import com.apkfuns.logutils.LogUtils;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;

import javax.inject.Inject;

import okhttp3.Call;
import okhttp3.Response;
import wanghaisheng.com.xiaoya.api.feedback.FeedbackApi;
import wanghaisheng.com.xiaoya.utils.ListUtils;

/**
 * Created by sheng on 2016/4/22.
 */
public class FeedbackPresenter extends Callback<Result> {
    private FeedbackView mFeedbackView;

    @Inject
    FeedbackApi mFeedbackApi;

    @Inject
    public FeedbackPresenter(FeedbackApi feedbackApi) {
        this.mFeedbackApi = feedbackApi;
        mFeedbackApi.attachCallback(this);
    }

    @Override
    public Result parseNetworkResponse(Response response) throws Exception {
        String string = response.body().string();
        Result result = new Gson().fromJson(string, Result.class);
        return result;
    }

    @Override
    public void onError(Call call, Exception e) {
        LogUtils.v(e.getMessage());
        mFeedbackView.postFailure(e.getMessage());
        mFeedbackView.hideLoading();
    }

    @Override
    public void onResponse(Result response) {
        if(response.success) {
            mFeedbackView.postSuccess();
        } else {
            mFeedbackView.postFailure(response.msg);
        }
        mFeedbackView.hideLoading();
    }


    public void postFeedbackData(String title,String content,ArrayList<String> datas) {
        mFeedbackView.showLoading();
        if(ListUtils.isEmpty(datas)) {
            mFeedbackApi.postFeedback(title,content);
        } else {
            mFeedbackApi.postFeedback(title,content,datas);
        }

    }

    public void attachView(FeedbackView feedbackView) {
        this.mFeedbackView = feedbackView;
    }

    public void detachView() {
        if(null != mFeedbackView) {
            mFeedbackView = null;
        }
    }



}
