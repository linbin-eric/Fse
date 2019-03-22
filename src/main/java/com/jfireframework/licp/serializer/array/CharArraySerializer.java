package com.jfireframework.licp.serializer.array;

import com.jfireframework.licp.InternalLicp;
import com.jfireframework.licp.buf.ByteBuf;
import com.jfireframework.licp.util.BufferUtil;

import java.nio.ByteBuffer;

public class CharArraySerializer extends AbstractArraySerializer<char[]>
{
    
    public CharArraySerializer()
    {
        super(char[].class);
    }
    
    @Override
    public void serialize(char[] src, ByteBuf<?> buf, InternalLicp licp)
    {
        char[] array = src;
        buf.writePositive(array.length);
        for (char each : array)
        {
            buf.writeChar(each);
        }
    }
    
    @Override
    public char[] deserialize(ByteBuf<?> buf, InternalLicp licp)
    {
        int length = buf.readPositive();
        char[] array = new char[length];
        licp.putObject(array);
        for (int i = 0; i < length; i++)
        {
            array[i] = buf.readChar();
        }
        return array;
    }
    
    @Override
    public char[] deserialize(ByteBuffer buf, InternalLicp licp)
    {
        int length = BufferUtil.readPositive(buf);
        char[] array = new char[length];
        licp.putObject(array);
        for (int i = 0; i < length; i++)
        {
            array[i] = BufferUtil.readChar(buf);
        }
        return array;
    }
    
}
