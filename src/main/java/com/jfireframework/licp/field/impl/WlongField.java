package com.jfireframework.licp.field.impl;

import com.jfireframework.baseutil.reflect.UNSAFE;
import com.jfireframework.licp.InternalLicp;
import com.jfireframework.licp.buf.ByteBuf;
import com.jfireframework.licp.interceptor.LicpFieldInterceptor;
import com.jfireframework.licp.util.BufferUtil;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

public class WlongField extends AbstractCacheField
{
    
    public WlongField(Field field, LicpFieldInterceptor fieldInterceptor)
    {
        super(field, fieldInterceptor);
    }
    
    @Override
    public void write(Object holder, ByteBuf<?> buf, InternalLicp licp)
    {
        Long d = (Long) UNSAFE.getObject(holder, offset);
        if (fieldInterceptor != null)
        {
            d = fieldInterceptor.serialize(d);
        }
        if (d == null)
        {
            buf.put((byte) 0);
        }
        else
        {
            buf.put((byte) 1);
            buf.writeVarLong(d);
        }
    }
    
    @Override
    public void read(Object holder, ByteBuf<?> buf, InternalLicp licp)
    {
        boolean exist = buf.get() == 1 ? true : false;
        Long value;
        if (exist)
        {
            value = buf.readVarLong();
        }
        else
        {
            value = null;
        }
        if (fieldInterceptor != null)
        {
            value = fieldInterceptor.deserialize(value);
        }
        UNSAFE.putObject(holder, offset, value);
    }
    
    @Override
    public void read(Object holder, ByteBuffer buf, InternalLicp licp)
    {
        boolean exist = buf.get() == 1 ? true : false;
        Long value;
        if (exist)
        {
            value = BufferUtil.readVarLong(buf);
        }
        else
        {
            value = null;
        }
        if (fieldInterceptor != null)
        {
            value = fieldInterceptor.deserialize(value);
        }
        UNSAFE.putObject(holder, offset, value);
    }
    
}
