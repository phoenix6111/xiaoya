package wanghaisheng.com.xiaoya.api.converter;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import wanghaisheng.com.xiaoya.api.meizi.ContentParser;

/**
 * Created by sheng on 2016/5/7.
 */
public class MeiziGroupResponseConverter<T> implements Converter<ResponseBody,T> {
    private Type type;

    public MeiziGroupResponseConverter(Type type) {
        this.type = type;
    }

    @Override
    public T convert(ResponseBody res) throws IOException {
        String resStr = res.string();

        return (T) ContentParser.parseGroups(resStr);
    }
}
