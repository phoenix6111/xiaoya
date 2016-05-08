package wanghaisheng.com.xiaoya.api.converter;

import com.apkfuns.logutils.LogUtils;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import wanghaisheng.com.xiaoya.api.meizi.ContentParser;
import wanghaisheng.com.xiaoya.beans.GroupResult;

/**
 * Created by sheng on 2016/5/7.
 */
public class MeiziPersonResponseConverter<T> implements Converter<ResponseBody,T> {
    private Type type;

    public MeiziPersonResponseConverter(Type type) {
        this.type = type;
    }

    @Override
    public T convert(ResponseBody res) throws IOException {
//        LogUtils.d("meizipersonresponseconverter...........");

        String resStr = res.string();
        GroupResult result = ContentParser.getCount(resStr);
        LogUtils.d(result);
        return (T) ContentParser.getCount(resStr);
    }
}
