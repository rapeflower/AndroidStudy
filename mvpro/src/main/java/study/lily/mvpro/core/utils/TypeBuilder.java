package study.lily.mvpro.core.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @Author rape flower
 * @Date 2019-02-27 10:16
 * @Describe
 */
public class TypeBuilder {

    public static <T> Class<T> getTypeByClass(Class<?> cls) {
        Type type = cls.getGenericSuperclass();
        if (type == null || !(type instanceof ParameterizedType)) {
            return null;
        }

        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type[] types = parameterizedType.getActualTypeArguments();
        if (types == null || types.length == 0) {
            return null;
        }
        return (Class<T>) types[0];
    }
}
