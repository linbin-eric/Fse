package com.jfireframework.licp.field.impl;

import com.jfireframework.baseutil.reflect.UNSAFE;
import com.jfireframework.licp.InternalLicp;
import com.jfireframework.licp.buf.ByteBuf;
import com.jfireframework.licp.interceptor.LicpFieldInterceptor;
import com.jfireframework.licp.util.BufferUtil;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

public class FloatField extends AbstractCacheField
{
    
    public FloatField(Field field, LicpFieldInterceptor fieldInterceptor)
    {
        super(field, fieldInterceptor);
    }
    
    @Override
    public void write(Object holder, ByteBuf<?> buf, InternalLicp licp)
    {
        float value = UNSAFE.getFloat(holder, offset);
        if (fieldInterceptor != null)
        {
            value = fieldInterceptor.serializeFloat(value);
        }
        buf.writeFloat(value);
    }
    
    @Override
    public void read(Object holder, ByteBuf<?> buf, InternalLicp licp)
    {
        float value = buf.readFloat();
        if (fieldInterceptor != null)
        {
            value = fieldInterceptor.deserializeFloat(value);
        }
        UNSAFE.putFloat(holder, offset, value);
    }
    
    @Override
    public void read(Object holder, ByteBuffer buf, InternalLicp licp)
    {
        float value = BufferUtil.readFloat(buf);
        if (fieldInterceptor != null)
        {
            value = fieldInterceptor.deserializeFloat(value);
        }
        UNSAFE.putFloat(holder, offset, value);
    }
    
}
