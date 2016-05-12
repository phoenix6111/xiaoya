package wanghaisheng.com.xiaoya.api.converter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import wanghaisheng.com.xiaoya.api.jianshu.JianshuContentResult;

/**
 * Created by sheng on 2016/5/10.
 * 解析首页的数据，生成jianshuContent的list和下一页的url
 */
public class JianshuGroupConverter extends Converter.Factory{
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        //如果是该converter能处理的泛型类，则处理，否则返回null,给后面的converter处理
        if(type == JianshuContentResult.class) {
            return new JianshuGroupResponseConverter<>(type);
        }

        return null;
    }
}
