package com.jfireframework.licp.serializer.extra;

import com.jfireframework.licp.InternalLicp;
import com.jfireframework.licp.buf.ByteBuf;
import com.jfireframework.licp.serializer.LicpSerializer;
import com.jfireframework.licp.util.BufferUtil;

import java.nio.ByteBuffer;
import java.util.HashSet;

public class HashSetSerializer implements LicpSerializer<HashSet<?>>
{
    
    @Override
    public void serialize(HashSet<?> src, ByteBuf<?> buf, InternalLicp licp)
    {
        HashSet<?> set = src;
        int length = set.size();
        buf.writePositive(length);
        for (Object each : set)
        {
            licp._serialize(each, buf);
        }
    }
    
    @Override
    public HashSet<?> deserialize(ByteBuf<?> buf, InternalLicp licp)
    {
        int length = buf.readPositive();
        HashSet<Object> set = new HashSet<Object>(length);
        for (int i = 0; i < length; i++)
        {
            set.add(licp._deserialize(buf));
        }
        return set;
    }
    
    @Override
    public HashSet<?> deserialize(ByteBuffer buf, InternalLicp licp)
    {
        int length = BufferUtil.readPositive(buf);
        HashSet<Object> set = new HashSet<Object>(length);
        for (int i = 0; i < length; i++)
        {
            set.add(licp._deserialize(buf));
        }
        return set;
    }
    
}
