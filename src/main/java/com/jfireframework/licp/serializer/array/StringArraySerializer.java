package com.jfireframework.licp.serializer.array;

import com.jfireframework.licp.InternalLicp;
import com.jfireframework.licp.buf.ByteBuf;
import com.jfireframework.licp.util.BufferUtil;

import java.nio.ByteBuffer;

public class StringArraySerializer extends AbstractArraySerializer<String[]>
{
    
    public StringArraySerializer()
    {
        super(String[].class);
    }
    
    @Override
    public void serialize(String[] src, ByteBuf<?> buf, InternalLicp licp)
    {
        String[] array = src;
        buf.writePositive(array.length);
        for (String each : array)
        {
            if (each == null)
            {
                buf.writePositive(0);
            }
            else
            {
                int length = each.length();
                buf.writePositive((length << 1 | 1));
                for (int i = 0; i < length; i++)
                {
                    buf.writeVarChar(each.charAt(i));
                }
            }
        }
    }
    
    @Override
    public String[] deserialize(ByteBuf<?> buf, InternalLicp licp)
    {
        int length = buf.readPositive();
        String[] array = new String[length];
        licp.putObject(array);
        for (int i = 0; i < length; i++)
        {
            int strLength = buf.readPositive();
            if (strLength == 0)
            {
                array[i] = null;
            }
            else
            {
                strLength >>>= 1;
                if (strLength == 0)
                {
                    array[i] = "";
                }
                else
                {
                    char[] src = new char[strLength];
                    for (int j = 0; j < strLength; j++)
                    {
                        src[j] = buf.readVarChar();
                    }
                    array[i] = new String(src);
                }
            }
        }
        return array;
    }
    
    @Override
    public String[] deserialize(ByteBuffer buf, InternalLicp licp)
    {
        int length = BufferUtil.readPositive(buf);
        String[] array = new String[length];
        licp.putObject(array);
        for (int i = 0; i < length; i++)
        {
            int strLength = BufferUtil.readPositive(buf);
            if (strLength == 0)
            {
                array[i] = null;
            }
            else
            {
                strLength >>>= 1;
                if (strLength == 0)
                {
                    array[i] = "";
                }
                else
                {
                    char[] src = new char[strLength];
                    for (int j = 0; j < strLength; j++)
                    {
                        src[j] = BufferUtil.readVarChar(buf);
                    }
                    array[i] = new String(src);
                }
            }
        }
        return array;
    }
    
}
