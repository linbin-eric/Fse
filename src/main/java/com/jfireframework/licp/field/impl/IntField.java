package com.jfireframework.licp.field.impl;

import com.jfireframework.baseutil.reflect.UNSAFE;
import com.jfireframework.licp.InternalLicp;
import com.jfireframework.licp.buf.ByteBuf;
import com.jfireframework.licp.interceptor.LicpFieldInterceptor;
import com.jfireframework.licp.util.BufferUtil;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

public class IntField extends AbstractCacheField
{
    
    public IntField(Field field, LicpFieldInterceptor fieldInterceptor)
    {
        super(field, fieldInterceptor);
    }
    
    @Override
    public void write(Object holder, ByteBuf<?> buf, InternalLicp licp)
    {
        int value = UNSAFE.getInt(holder, offset);
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
        UNSAFE.putInt(holder, offset, value);
    }
    
    @Override
    public void read(Object holder, ByteBuffer buf, InternalLicp licp)
    {
        int value = BufferUtil.readVarint(buf);
        if (fieldInterceptor != null)
        {
            value = fieldInterceptor.deserializeInt(value);
        }
        UNSAFE.putInt(holder, offset, value);
    }
    
}
