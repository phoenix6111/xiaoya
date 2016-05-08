package wanghaisheng.com.xiaoya.api.converter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import wanghaisheng.com.xiaoya.beans.GroupResult;

/**
 * Created by sheng on 2016/5/7.
 */
public class MeiziPersonConverter extends Converter.Factory {

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        //根据type判断是否是自己能处理的类型，不能的话，return null ,交给后面的Converter.Factory
//        LogUtils.d("MeiziPersonConverter....before...........");
        if (type == GroupResult.class) {
            return new MeiziPersonResponseConverter<>(type);
        }

        return null;
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
//        LogUtils.d(retrofit.);

        return super.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
    }
}
