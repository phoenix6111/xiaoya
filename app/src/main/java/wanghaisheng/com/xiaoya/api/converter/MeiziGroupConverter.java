package wanghaisheng.com.xiaoya.api.converter;

import com.apkfuns.logutils.LogUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import wanghaisheng.com.xiaoya.db.Group;

/**
 * Created by sheng on 2016/5/7.
 */
public class MeiziGroupConverter extends Converter.Factory {

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        //根据type判断是否是自己能处理的类型，不能的话，return null ,交给后面的Converter.Factory
        if(type instanceof ParameterizedType) {//支持返回值是List<Group>
            Type rawType = ((ParameterizedType) type).getRawType();
            Type actualType = ((ParameterizedType) type).getActualTypeArguments()[0];

//            LogUtils.d("MeiziGroupConverter....before...........");

            if(rawType == List.class && actualType== Group.class) {
                return new MeiziGroupResponseConverter<>(type);
            }
            LogUtils.d("MeiziGroupConverter....after...........");

        }

        return null;
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
//        LogUtils.d(retrofit.);

        return super.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
    }
}
