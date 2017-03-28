package com.jfireframework.licp.field.impl;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import com.jfireframework.baseutil.collection.buffer.ByteBuf;
import com.jfireframework.licp.InternalLicp;
import com.jfireframework.licp.interceptor.LicpFieldInterceptor;

public class ByteField extends AbstractCacheField
{
    
    public ByteField(Field field, LicpFieldInterceptor fieldInterceptor)
    {
        super(field, fieldInterceptor);
    }
    
    @Override
    public void write(Object holder, ByteBuf<?> buf, InternalLicp licp)
    {
        byte value = unsafe.getByte(holder, offset);
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
        unsafe.putByte(holder, offset, value);
    }
    
    @Override
    public void read(Object holder, ByteBuffer buf, InternalLicp licp)
    {
        byte value = buf.get();
        if (fieldInterceptor != null)
        {
            value = fieldInterceptor.deserializeByte(value);
        }
        unsafe.putByte(holder, offset, value);
    }
    
}
