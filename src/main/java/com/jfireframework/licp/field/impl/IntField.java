package com.jfireframework.licp.field.impl;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import com.jfireframework.baseutil.collection.buffer.ByteBuf;
import com.jfireframework.licp.InternalLicp;
import com.jfireframework.licp.interceptor.LicpFieldInterceptor;
import com.jfireframework.licp.util.BufferUtil;

public class IntField extends AbstractCacheField
{
    
    public IntField(Field field, LicpFieldInterceptor fieldInterceptor)
    {
        super(field, fieldInterceptor);
    }
    
    @Override
    public void write(Object holder, ByteBuf<?> buf, InternalLicp licp)
    {
        int value = unsafe.getInt(holder, offset);
        if (fieldInterceptor != null)
        {
            value = fieldInterceptor.serializeInt(value);
        }
        buf.writeVarint(value);
    }
    
    @Override
    public void read(Object holder, ByteBuf<?> buf, InternalLicp licp)
    {
        int value = buf.readVarint();
        if (fieldInterceptor != null)
        {
            value = fieldInterceptor.deserializeInt(value);
        }
        unsafe.putInt(holder, offset, value);
    }
    
    @Override
    public void read(Object holder, ByteBuffer buf, InternalLicp licp)
    {
        int value = BufferUtil.readVarint(buf);
        if (fieldInterceptor != null)
        {
            value = fieldInterceptor.deserializeInt(value);
        }
        unsafe.putInt(holder, offset, value);
    }
    
}
