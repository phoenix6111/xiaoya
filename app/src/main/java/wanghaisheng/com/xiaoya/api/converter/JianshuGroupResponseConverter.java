package wanghaisheng.com.xiaoya.api.converter;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by sheng on 2016/5/10.
 */
public class JianshuGroupResponseConverter<T> implements Converter<ResponseBody,T> {
    private Type type;

    public JianshuGroupResponseConverter(Type type) {
        this.type = type;
    }

    @Override
    public T convert(ResponseBody res) throws IOException {
        String resStr = res.string();
//        LogUtils.d("jian shu data convert data.........");

        return (T) JianshuContentParser.parseGroup(resStr);
    }
}
