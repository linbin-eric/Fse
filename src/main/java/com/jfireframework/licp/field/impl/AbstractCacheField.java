package com.jfireframework.licp.field.impl;

import com.jfireframework.baseutil.reflect.UNSAFE;
import com.jfireframework.licp.field.CacheField;
import com.jfireframework.licp.interceptor.LicpFieldInterceptor;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public abstract class AbstractCacheField implements CacheField
{
    protected final long                 offset;
    protected final boolean              finalField;
    protected final String               fieldName;
    protected final LicpFieldInterceptor fieldInterceptor;
    
    public AbstractCacheField(Field field, LicpFieldInterceptor fieldInterceptor)
    {
        this.fieldInterceptor = fieldInterceptor;
        offset = UNSAFE.objectFieldOffset(field);
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
