package com.jfireframework.licp.field.impl;

import com.jfireframework.baseutil.reflect.UNSAFE;
import com.jfireframework.licp.InternalLicp;
import com.jfireframework.licp.buf.ByteBuf;
import com.jfireframework.licp.interceptor.LicpFieldInterceptor;
import com.jfireframework.licp.util.BufferUtil;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

public class WCharField extends AbstractCacheField
{
    
    public WCharField(Field field, LicpFieldInterceptor fieldInterceptor)
    {
        super(field, fieldInterceptor);
    }
    
    @Override
    public void write(Object holder, ByteBuf<?> buf, InternalLicp licp)
    {
        Character character = (Character) UNSAFE.getObject(holder, offset);
        if (fieldInterceptor != null)
        {
            character = fieldInterceptor.serialize(character);
        }
        if (character == null)
        {
            buf.put((byte) 0);
        }
        else
        {
            buf.put((byte) 1);
            buf.writeChar(character);
        }
    }
    
    @Override
    public void read(Object holder, ByteBuf<?> buf, InternalLicp licp)
    {
        boolean exist = buf.get() == 1 ? true : false;
        Character value;
        if (exist == false)
        {
            value = null;
        }
        else
        {
            value = buf.readChar();
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
        Character value;
        if (exist == false)
        {
            value = null;
        }
        else
        {
            value = BufferUtil.readChar(buf);
            UNSAFE.putObject(holder, offset, value);
        }
        if (fieldInterceptor != null)
        {
            value = fieldInterceptor.deserialize(value);
        }
        UNSAFE.putObject(holder, offset, value);
    }
    
}
