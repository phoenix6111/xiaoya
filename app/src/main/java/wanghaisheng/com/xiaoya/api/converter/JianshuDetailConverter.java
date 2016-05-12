package wanghaisheng.com.xiaoya.api.converter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by sheng on 2016/5/10.
 */
public class JianshuDetailConverter extends Converter.Factory {
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if(type == String.class) {
            return new JianshuDetailResponseConverter<>(type);
        }

        return null;
    }
}
