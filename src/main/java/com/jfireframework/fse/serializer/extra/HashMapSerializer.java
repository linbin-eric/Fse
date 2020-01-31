package com.jfireframework.fse.serializer.extra;

import com.jfireframework.fse.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HashMapSerializer extends CycleFlagSerializer implements FseSerializer
{
    @Override
    public void init(Class<?> type, SerializerFactory serializerFactory)
    {
    }

    @Override
    public void doWriteToBytes(Object o, InternalByteArray byteArray, FseContext fseContext, int depth)
    {
        HashMap map = (HashMap) o;
        byteArray.writePositive(map.size());
        Set<Map.Entry> set = map.entrySet();
        for (Map.Entry entry : set)
        {
            fseContext.serialize(entry.getKey(), byteArray, depth);
            fseContext.serialize(entry.getValue(), byteArray, depth);
        }
    }

    @Override
    public Object readBytes(InternalByteArray byteArray, FseContext fseContext)
    {
        HashMap map = new HashMap();
        fseContext.collectObject(map);
        int size = byteArray.readPositive();
        for (int i = 0; i < size; i++)
        {
            int    result = byteArray.readVarInt();
            Object key;
            if (result > 0)
            {
                key = fseContext.getClassRegistry(result).getSerializer().readBytes(byteArray, fseContext);
            }
            else
            {
                key = fseContext.getObjectByIndex(0 - result);
            }
            Object value;
            result = byteArray.readVarInt();
            if (result == 0)
            {
                value = null;
            }
            else if (result > 0)
            {
                value = fseContext.getClassRegistry(result).getSerializer().readBytes(byteArray, fseContext);
            }
            else
            {
                value = fseContext.getObjectByIndex(0 - result);
            }
            map.put(key, value);
        }
        return map;
    }

    @Override
    public void writeToBytesWithoutRegisterClass(Object o, InternalByteArray byteArray, FseContext fseContext, int depth)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object readBytesWithoutRegisterClass(InternalByteArray byteArray, FseContext fseContext)
    {
        throw new UnsupportedOperationException();
    }
}
