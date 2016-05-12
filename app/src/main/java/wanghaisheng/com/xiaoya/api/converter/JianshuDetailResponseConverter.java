package wanghaisheng.com.xiaoya.api.converter;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by sheng on 2016/5/10.
 */
public class JianshuDetailResponseConverter<T> implements Converter<ResponseBody,T> {
    private Type type;

    public JianshuDetailResponseConverter(Type type) {
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String resStr = value.string();
        return (T) JianshuContentParser.getJianshuDetailContent(resStr);
    }
}
