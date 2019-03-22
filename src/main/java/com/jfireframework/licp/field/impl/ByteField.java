package com.jfireframework.licp.field.impl;

import com.jfireframework.baseutil.reflect.UNSAFE;
import com.jfireframework.licp.InternalLicp;
import com.jfireframework.licp.buf.ByteBuf;
import com.jfireframework.licp.interceptor.LicpFieldInterceptor;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

public class ByteField extends AbstractCacheField
{
    
    public ByteField(Field field, LicpFieldInterceptor fieldInterceptor)
    {
        super(field, fieldInterceptor);
    }
    
    @Override
    public void write(Object holder, ByteBuf<?> buf, InternalLicp licp)
    {
        byte value = UNSAFE.getByte(holder, offset);
        if (fieldInterceptor != null)
        {
            value = fieldInterceptor.serializeByte(value);
        }
        buf.put(value);
    }
    
    @Override
    public void read(Object holder, ByteBuf<?> buf, InternalLicp licp)
    {
        byte value = buf.get();
        if (fieldInterceptor != null)
        {
            value = fieldInterceptor.deserializeByte(value);
        }
        UNSAFE.putByte(holder, offset, value);
    }
    
    @Override
    public void read(Object holder, ByteBuffer buf, InternalLicp licp)
    {
        byte value = buf.get();
        if (fieldInterceptor != null)
        {
            value = fieldInterceptor.deserializeByte(value);
        }
        UNSAFE.putByte(holder, offset, value);
    }
    
}
