package com.jfireframework.licp.field.impl;

import com.jfireframework.baseutil.reflect.UNSAFE;
import com.jfireframework.licp.InternalLicp;
import com.jfireframework.licp.buf.ByteBuf;
import com.jfireframework.licp.interceptor.LicpFieldInterceptor;
import com.jfireframework.licp.util.BufferUtil;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

public class DoubleField extends AbstractCacheField
{
    
    public DoubleField(Field field, LicpFieldInterceptor fieldInterceptor)
    {
        super(field, fieldInterceptor);
    }
    
    @Override
    public void write(Object holder, ByteBuf<?> buf, InternalLicp licp)
    {
        double value = UNSAFE.getDouble(holder, offset);
        if (fieldInterceptor != null)
        {
            value = fieldInterceptor.serializeDouble(value);
        }
        buf.writeDouble(value);
    }
    
    @Override
    public void read(Object holder, ByteBuf<?> buf, InternalLicp licp)
    {
        double value = buf.readDouble();
        if (fieldInterceptor != null)
        {
            value = fieldInterceptor.deserializeDouble(value);
        }
        UNSAFE.putDouble(holder, offset, value);
    }
    
    @Override
    public void read(Object holder, ByteBuffer buf, InternalLicp licp)
    {
        double value = BufferUtil.readDouble(buf);
        if (fieldInterceptor != null)
        {
            value = fieldInterceptor.deserializeDouble(value);
        }
        UNSAFE.putDouble(holder, offset, value);
    }
    
}
