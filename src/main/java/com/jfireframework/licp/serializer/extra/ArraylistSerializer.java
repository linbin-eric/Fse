package com.jfireframework.licp.serializer.extra;

import com.jfireframework.licp.InternalLicp;
import com.jfireframework.licp.buf.ByteBuf;
import com.jfireframework.licp.serializer.LicpSerializer;
import com.jfireframework.licp.util.BufferUtil;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class ArraylistSerializer implements LicpSerializer<ArrayList<?>>
{
    
    @Override
    public void serialize(ArrayList<?> src, ByteBuf<?> buf, InternalLicp licp)
    {
        ArrayList<?> list = src;
        int length = list.size();
        buf.writePositive(length);
        for (Object each : list)
        {
            licp._serialize(each, buf);
        }
    }
    
    @Override
    public ArrayList<?> deserialize(ByteBuf<?> buf, InternalLicp licp)
    {
        int length = buf.readPositive();
        ArrayList<Object> list = new ArrayList<Object>(length);
        licp.putObject(list);
        for (int i = 0; i < length; i++)
        {
            list.add(licp._deserialize(buf));
        }
        return list;
    }
    
    @Override
    public ArrayList<?> deserialize(ByteBuffer buf, InternalLicp licp)
    {
        int length = BufferUtil.readPositive(buf);
        ArrayList<Object> list = new ArrayList<Object>(length);
        licp.putObject(list);
        for (int i = 0; i < length; i++)
        {
            list.add(licp._deserialize(buf));
        }
        return list;
    }
    
}
