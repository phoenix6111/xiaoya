package wanghaisheng.com.xiaoya;

import com.apkfuns.logutils.LogUtils;

import junit.framework.Assert;

import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import wanghaisheng.com.xiaoya.api.jianshu.JianshuApi;
import wanghaisheng.com.xiaoya.api.jianshu.JianshuContentResult;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
        String testStr = "阅读 17325";
        String data = getNumbers(testStr);
        assertEquals("17325",data);
    }

    public void testParseJianshuIndexPage() throws Exception {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(20 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout(20 * 1000, TimeUnit.MILLISECONDS);
        OkHttpClient client = builder.build();

        JianshuApi jianshuApi = new JianshuApi(client);

        jianshuApi.getIndexPageContentResult()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<JianshuContentResult>() {
                        @Override
                        public void call(JianshuContentResult jianshuContentResult) {
                            Assert.assertEquals(20,jianshuContentResult.contents.size());
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            LogUtils.d(throwable);
                        }
                    });
    }

    public String getNumbers(String content) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }
}