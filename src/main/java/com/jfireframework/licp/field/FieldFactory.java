package com.jfireframework.licp.field;

import com.jfireframework.baseutil.reflect.ReflectUtil;
import com.jfireframework.licp.InternalLicp;
import com.jfireframework.licp.field.impl.*;
import com.jfireframework.licp.interceptor.LicpFieldInterceptor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class FieldFactory
{
    private static final Map<Class<?>, Constructor<? extends CacheField>> map = new HashMap<Class<?>, Constructor<? extends CacheField>>();

    static
    {
        try
        {
            map.put(int.class, IntField.class.getConstructor(Field.class, LicpFieldInterceptor.class));
            map.put(short.class, ShortField.class.getConstructor(Field.class, LicpFieldInterceptor.class));
            map.put(byte.class, ByteField.class.getConstructor(Field.class, LicpFieldInterceptor.class));
            map.put(long.class, LongField.class.getConstructor(Field.class, LicpFieldInterceptor.class));
            map.put(float.class, FloatField.class.getConstructor(Field.class, LicpFieldInterceptor.class));
            map.put(double.class, DoubleField.class.getConstructor(Field.class, LicpFieldInterceptor.class));
            map.put(boolean.class, BooleanField.class.getConstructor(Field.class, LicpFieldInterceptor.class));
            map.put(char.class, CharField.class.getConstructor(Field.class, LicpFieldInterceptor.class));
            map.put(Integer.class, IntegerField.class.getConstructor(Field.class, LicpFieldInterceptor.class));
            map.put(Byte.class, WByteField.class.getConstructor(Field.class, LicpFieldInterceptor.class));
            map.put(Character.class, WCharField.class.getConstructor(Field.class, LicpFieldInterceptor.class));
            map.put(Boolean.class, WBooleanField.class.getConstructor(Field.class, LicpFieldInterceptor.class));
            map.put(Long.class, WlongField.class.getConstructor(Field.class, LicpFieldInterceptor.class));
            map.put(Float.class, WFloatField.class.getConstructor(Field.class, LicpFieldInterceptor.class));
            map.put(Short.class, WShortField.class.getConstructor(Field.class, LicpFieldInterceptor.class));
            map.put(Double.class, WDoubleField.class.getConstructor(Field.class, LicpFieldInterceptor.class));
            map.put(String.class, StringField.class.getConstructor(Field.class, LicpFieldInterceptor.class));
        } catch (Exception e)
        {
            ReflectUtil.throwException(e);
        }
    }

    public static final CacheField build(Field field, InternalLicp licp)
    {
        Class<?>                          type             = field.getType();
        Constructor<? extends CacheField> constructor      = map.get(type);
        String                            rule             = field.getDeclaringClass().getName() + '.' + field.getName();
        LicpFieldInterceptor              fieldInterceptor = licp.getFieldInterceptor(rule);
        if (constructor != null)
        {
            try
            {
                return constructor.newInstance(field, fieldInterceptor);
            } catch (Exception e)
            {
                ReflectUtil.throwException(e);
            }
        }
        return new ObjectField(field, licp, fieldInterceptor);
    }
}
