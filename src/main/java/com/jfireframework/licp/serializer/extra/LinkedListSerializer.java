package com.jfireframework.licp.serializer.extra;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import com.jfireframework.baseutil.collection.buffer.ByteBuf;
import com.jfireframework.licp.InternalLicp;
import com.jfireframework.licp.serializer.LicpSerializer;
import com.jfireframework.licp.util.BufferUtil;

public class LinkedListSerializer implements LicpSerializer<LinkedList<?>>
{
    @Override
    public void serialize(LinkedList<?> src, ByteBuf<?> buf, InternalLicp licp)
    {
        LinkedList<?> list = src;
        int length = list.size();
        buf.writePositive(length);
        for (Object each : list)
        {
            licp._serialize(each, buf);
        }
    }
    
    @Override
    public LinkedList<?> deserialize(ByteBuf<?> buf, InternalLicp licp)
    {
        LinkedList<Object> list = new LinkedList<Object>();
        licp.putObject(list);
        int length = buf.readPositive();
        for (int i = 0; i < length; i++)
        {
            list.add(licp._deserialize(buf));
        }
        return list;
    }
    
    @Override
    public LinkedList<?> deserialize(ByteBuffer buf, InternalLicp licp)
    {
        LinkedList<Object> list = new LinkedList<Object>();
        licp.putObject(list);
        int length = BufferUtil.readPositive(buf);
        for (int i = 0; i < length; i++)
        {
            list.add(licp._deserialize(buf));
        }
        return list;
    }
    
}
