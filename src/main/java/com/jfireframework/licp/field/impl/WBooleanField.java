package com.jfireframework.licp.field.impl;

import com.jfireframework.baseutil.exception.UnSupportException;
import com.jfireframework.baseutil.reflect.UNSAFE;
import com.jfireframework.licp.InternalLicp;
import com.jfireframework.licp.buf.ByteBuf;
import com.jfireframework.licp.interceptor.LicpFieldInterceptor;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

public class WBooleanField extends AbstractCacheField
{
    
    public WBooleanField(Field field, LicpFieldInterceptor fieldInterceptor)
    {
        super(field, fieldInterceptor);
    }
    
    @Override
    public void write(Object holder, ByteBuf<?> buf, InternalLicp licp)
    {
        Boolean value = (Boolean) UNSAFE.getObject(holder, offset);
        if (fieldInterceptor != null)
        {
            value = fieldInterceptor.serialize(value);
        }
        if (value == null)
        {
            buf.put((byte) 0);
        }
        else if (value == true)
        {
            buf.put((byte) 1);
        }
        else if (value == false)
        {
            buf.put((byte) 2);
        }
        else
        {
            throw new UnSupportException("not here");
        }
    }
    
    @Override
    public void read(Object holder, ByteBuf<?> buf, InternalLicp licp)
    {
        byte b = buf.get();
        Boolean value;
        if (b == 0)
        {
            value = null;
        }
        else if (b == 1)
        {
            value = true;
        }
        else if (b == 2)
        {
            value = false;
        }
        else
        {
            throw new UnSupportException("not here");
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
        Boolean value;
        byte b = buf.get();
        if (b == 0)
        {
            value = null;
        }
        else if (b == 1)
        {
            value = true;
        }
        else if (b == 2)
        {
            value = false;
        }
        else
        {
            throw new UnSupportException("not here");
        }
        if (fieldInterceptor != null)
        {
            value = fieldInterceptor.deserialize(value);
        }
        UNSAFE.putObject(holder, offset, value);
    }
    
}
