package wanghaisheng.com.xiaoya.api.feedback;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.Callback;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import wanghaisheng.com.xiaoya.presenter.feedback.Result;
import wanghaisheng.com.xiaoya.utils.FileUtil;
import wanghaisheng.com.xiaoya.utils.RequestHelper;
import wanghaisheng.com.xiaoya.utils.SettingPrefHelper;

/**
 * Created by sheng on 2016/4/22.
 */
public class FeedbackApi {
    public static final String BASE_URL = "http://justgogo.biz/xiaoya/feedback";
    private RequestHelper mRequestHelper;
    private SettingPrefHelper mSettingPrefHelper;

    private Callback<Result> callback;

    //让FeedbackPresenter实现Callback接口，然后注入到FeedbackApi中，实现callback回调
    public void attachCallback(Callback<Result> callback) {
        this.callback = callback;
    }

    public FeedbackApi(RequestHelper requestHelper, SettingPrefHelper settingPrefHelper) {
        this.mRequestHelper = requestHelper;
        this.mSettingPrefHelper = settingPrefHelper;
    }

    /**
     * 发送反馈数据到服务器，包含图片数据
     * @param title
     * @param content
     * @param fileNames
     */
    public void postFeedback(String title, String content, ArrayList<String> fileNames) {
        Map<String, String> params = new HashMap<>();
        params.put("title", title);
        params.put("content", content);
        PostFormBuilder formBuilder = OkHttpUtils.post();
        for(String fileName : fileNames) {
            File file = new File(fileName);
            formBuilder.addFile("feedback_imgs", FileUtil.getFileName(fileName),file);
        }
        formBuilder.url(BASE_URL)//
                .params(params)//
                .headers(mRequestHelper.getHttpRequestMap())//
                .build()//
                .execute(callback);
    }

    /**
     * 发送反馈数据到服务器，不包含图片数据
     * @param title
     * @param content
     */
    public void postFeedback(String title,String content) {
        Map<String, String> params = new HashMap<>();
        params.put("title", title);
        params.put("content", content);
        OkHttpUtils.post()
                .url(BASE_URL)//
                .params(params)//
                .headers(mRequestHelper.getHttpRequestMap())//
                .build()//
                .execute(callback);
    }
}
