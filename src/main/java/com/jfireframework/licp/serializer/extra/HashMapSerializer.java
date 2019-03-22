package com.jfireframework.licp.serializer.extra;

import com.jfireframework.licp.InternalLicp;
import com.jfireframework.licp.buf.ByteBuf;
import com.jfireframework.licp.serializer.LicpSerializer;
import com.jfireframework.licp.util.BufferUtil;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map.Entry;

public class HashMapSerializer implements LicpSerializer<HashMap<?, ?>>
{
    
    @Override
    public void serialize(HashMap<?, ?> src, ByteBuf<?> buf, InternalLicp licp)
    {
        HashMap<?, ?> map = src;
        int size = map.size();
        buf.writePositive(size);
        for (Entry<?, ?> entry : map.entrySet())
        {
            licp._serialize(entry.getKey(), buf);
            licp._serialize(entry.getValue(), buf);
        }
    }
    
    @Override
    public HashMap<?, ?> deserialize(ByteBuf<?> buf, InternalLicp licp)
    {
        int size = buf.readPositive();
        HashMap<Object, Object> map = new HashMap<Object, Object>(size);
        licp.putObject(map);
        for (int i = 0; i < size; i++)
        {
            Object key = licp._deserialize(buf);
            Object value = licp._deserialize(buf);
            map.put(key, value);
        }
        return map;
    }
    
    @Override
    public HashMap<?, ?> deserialize(ByteBuffer buf, InternalLicp licp)
    {
        int size = BufferUtil.readPositive(buf);
        HashMap<Object, Object> map = new HashMap<Object, Object>(size);
        licp.putObject(map);
        for (int i = 0; i < size; i++)
        {
            Object key = licp._deserialize(buf);
            Object value = licp._deserialize(buf);
            map.put(key, value);
        }
        return map;
    }
    
}
