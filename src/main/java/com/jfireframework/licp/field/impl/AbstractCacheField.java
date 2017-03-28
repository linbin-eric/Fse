package com.jfireframework.licp.field.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import com.jfireframework.baseutil.reflect.ReflectUtil;
import com.jfireframework.licp.field.CacheField;
import com.jfireframework.licp.interceptor.LicpFieldInterceptor;
import sun.misc.Unsafe;

public abstract class AbstractCacheField implements CacheField
{
    protected static final Unsafe        unsafe = ReflectUtil.getUnsafe();
    protected final long                 offset;
    protected final boolean              finalField;
    protected final String               fieldName;
    protected final LicpFieldInterceptor fieldInterceptor;
    
    public AbstractCacheField(Field field, LicpFieldInterceptor fieldInterceptor)
    {
        this.fieldInterceptor = fieldInterceptor;
        offset = unsafe.objectFieldOffset(field);
        if (Modifier.isFinal(field.getType().getModifiers()))
        {
            finalField = true;
        }
        else
        {
            finalField = false;
        }
        fieldName = field.getName();
    }
    
    @Override
    public String getName()
    {
        return fieldName;
    }
}
