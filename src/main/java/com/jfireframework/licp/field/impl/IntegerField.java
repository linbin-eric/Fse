package com.jfireframework.licp.field.impl;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import com.jfireframework.baseutil.collection.buffer.ByteBuf;
import com.jfireframework.licp.InternalLicp;
import com.jfireframework.licp.interceptor.LicpFieldInterceptor;
import com.jfireframework.licp.util.BufferUtil;

public class IntegerField extends AbstractCacheField
{
    public IntegerField(Field field, LicpFieldInterceptor fieldInterceptor)
    {
        super(field, fieldInterceptor);
    }
    
    @Override
    public void write(Object holder, ByteBuf<?> buf, InternalLicp licp)
    {
        Integer value = (Integer) unsafe.getObject(holder, offset);
        if (fieldInterceptor != null)
        {
            value = fieldInterceptor.serialize(value);
        }
        if (value == null)
        {
            buf.put((byte) 0);
        }
        else
        {
            buf.put((byte) 1);
            buf.writeVarint(value);
        }
    }
    
    @Override
    public void read(Object holder, ByteBuf<?> buf, InternalLicp licp)
    {
        boolean exist = buf.get() == 1 ? true : false;
        if (exist)
        {
            Integer value = buf.readVarint();
            if (fieldInterceptor != null)
            {
                value = fieldInterceptor.deserialize(value);
            }
            unsafe.putObject(holder, offset, value);
        }
        else
        {
            Integer value = null;
            if (fieldInterceptor != null)
            {
                value = fieldInterceptor.deserialize(value);
            }
            unsafe.putObject(holder, offset, value);
        }
    }
    
    @Override
    public void read(Object holder, ByteBuffer buf, InternalLicp licp)
    {
        boolean exist = buf.get() == 1 ? true : false;
        if (exist)
        {
            Integer value = BufferUtil.readVarint(buf);
            if (fieldInterceptor != null)
            {
                value = fieldInterceptor.deserialize(value);
            }
            unsafe.putObject(holder, offset, value);
        }
        else
        {
            Integer value = null;
            if (fieldInterceptor != null)
            {
                value = fieldInterceptor.deserialize(value);
            }
            unsafe.putObject(holder, offset, value);
        }
        
    }
    
}
