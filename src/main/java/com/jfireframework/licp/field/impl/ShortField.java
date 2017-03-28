package com.jfireframework.licp.field.impl;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import com.jfireframework.baseutil.collection.buffer.ByteBuf;
import com.jfireframework.licp.InternalLicp;
import com.jfireframework.licp.interceptor.LicpFieldInterceptor;
import com.jfireframework.licp.util.BufferUtil;

public class ShortField extends AbstractCacheField
{
    
    public ShortField(Field field, LicpFieldInterceptor fieldInterceptor)
    {
        super(field, fieldInterceptor);
    }
    
    @Override
    public void write(Object holder, ByteBuf<?> buf, InternalLicp licp)
    {
        short value = unsafe.getShort(holder, offset);
        if (fieldInterceptor != null)
        {
            value = fieldInterceptor.serializeShort(value);
        }
        buf.writeShort(value);
    }
    
    @Override
    public void read(Object holder, ByteBuf<?> buf, InternalLicp licp)
    {
        short value = buf.readShort();
        if (fieldInterceptor != null)
        {
            value = fieldInterceptor.deserializeShort(value);
        }
        unsafe.putShort(holder, offset, value);
    }
    
    @Override
    public void read(Object holder, ByteBuffer buf, InternalLicp licp)
    {
        short value = BufferUtil.readShort(buf);
        if (fieldInterceptor != null)
        {
            value = fieldInterceptor.deserializeShort(value);
        }
        unsafe.putShort(holder, offset, value);
    }
    
}
