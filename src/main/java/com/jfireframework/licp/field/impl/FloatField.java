package com.jfireframework.licp.field.impl;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import com.jfireframework.baseutil.collection.buffer.ByteBuf;
import com.jfireframework.licp.InternalLicp;
import com.jfireframework.licp.interceptor.LicpFieldInterceptor;
import com.jfireframework.licp.util.BufferUtil;

public class FloatField extends AbstractCacheField
{
    
    public FloatField(Field field, LicpFieldInterceptor fieldInterceptor)
    {
        super(field, fieldInterceptor);
    }
    
    @Override
    public void write(Object holder, ByteBuf<?> buf, InternalLicp licp)
    {
        float value = unsafe.getFloat(holder, offset);
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
        unsafe.putFloat(holder, offset, value);
    }
    
    @Override
    public void read(Object holder, ByteBuffer buf, InternalLicp licp)
    {
        float value = BufferUtil.readFloat(buf);
        if (fieldInterceptor != null)
        {
            value = fieldInterceptor.deserializeFloat(value);
        }
        unsafe.putFloat(holder, offset, value);
    }
    
}
