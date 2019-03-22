package com.jfireframework.licp.field.impl;

import com.jfireframework.baseutil.reflect.UNSAFE;
import com.jfireframework.licp.InternalLicp;
import com.jfireframework.licp.buf.ByteBuf;
import com.jfireframework.licp.interceptor.LicpFieldInterceptor;
import com.jfireframework.licp.util.BufferUtil;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;

public class StringField extends AbstractCacheField
{
    
    public StringField(Field field, LicpFieldInterceptor fieldInterceptor)
    {
        super(field, fieldInterceptor);
    }
    
    @Override
    public void write(Object holder, ByteBuf<?> buf, InternalLicp licp)
    {
        String value = (String) UNSAFE.getObject(holder, offset);
        if (fieldInterceptor != null)
        {
            value = fieldInterceptor.serialize(value);
        }
        if (value == null)
        {
            buf.writePositive(0);
        }
        else
        {
            int length = value.length();
            buf.writePositive((length << 1) | 1);
            for (int i = 0; i < length; i++)
            {
                buf.writeVarChar(value.charAt(i));
            }
        }
    }
    
    @Override
    public void read(Object holder, ByteBuf<?> buf, InternalLicp licp)
    {
        String value;
        int length = buf.readPositive();
        if (length == 0)
        {
            value = null;
        }
        else
        {
            length >>>= 1;
            if (length == 0)
            {
                value = "";
            }
            else
            {
                char[] src = new char[length];
                for (int i = 0; i < length; i++)
                {
                    src[i] = buf.readVarChar();
                }
                value = new String(src);
            }
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
        String value;
        int length = BufferUtil.readPositive(buf);
        if (length == 0)
        {
            value = null;
        }
        else
        {
            length >>>= 1;
            if (length == 0)
            {
                value = "";
            }
            else
            {
                char[] src = new char[length];
                for (int i = 0; i < length; i++)
                {
                    src[i] = BufferUtil.readVarChar(buf);
                }
                value = new String(src);
            }
        }
        if (fieldInterceptor != null)
        {
            value = fieldInterceptor.deserialize(value);
        }
        UNSAFE.putObject(holder, offset, value);
    }
    
}
