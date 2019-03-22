package com.jfireframework.licp.serializer;

import com.jfireframework.licp.InternalLicp;
import com.jfireframework.licp.serializer.array.*;
import com.jfireframework.licp.serializer.base.*;
import com.jfireframework.licp.serializer.extra.*;

import java.util.*;

public class SerializerFactory
{
    private final HashMap<Class<?>, LicpSerializer<?>> serializerMap = new HashMap<Class<?>, LicpSerializer<?>>();
    
    public SerializerFactory()
    {
        serializerMap.put(int[].class, new IntArraySerializer());
        serializerMap.put(byte[].class, new ByteArraySerializer());
        serializerMap.put(short[].class, new ShortArraySerializer());
        serializerMap.put(long[].class, new LongArraySerializer());
        serializerMap.put(char[].class, new CharArraySerializer());
        serializerMap.put(boolean[].class, new BooleanArraySerializer());
        serializerMap.put(float[].class, new FloatArraySerializer());
        serializerMap.put(double[].class, new DoubleArraySerializer());
        /**************************/
        serializerMap.put(Integer.class, new IntegerSerializer());
        serializerMap.put(Boolean.class, new BooleanSerializer());
        serializerMap.put(Character.class, new CharSerializer());
        serializerMap.put(Short.class, new ShortSerializer());
        serializerMap.put(Byte.class, new ByteSerializer());
        serializerMap.put(Long.class, new LongSerializer());
        serializerMap.put(Float.class, new FloatSerializer());
        serializerMap.put(Double.class, new DoubleSerializer());
        serializerMap.put(String.class, new StringSerializer());
        /**************************/
        serializerMap.put(String[].class, new StringArraySerializer());
        serializerMap.put(Integer[].class, new IntegerArraySerializer());
        serializerMap.put(Long[].class, new WLongArraySerializer());
        serializerMap.put(Short[].class, new WShortArraySerializer());
        serializerMap.put(Byte[].class, new WByteArraySerializer());
        serializerMap.put(Character[].class, new WCharArraySerializer());
        serializerMap.put(Float[].class, new WFloatArraySerializer());
        serializerMap.put(Double[].class, new WDoubleArraySerializer());
        serializerMap.put(Boolean[].class, new WBooleanArraySerializer());
        /**************************/
        serializerMap.put(Date.class, new UtilDateSerializer());
        serializerMap.put(java.sql.Date.class, new SqlDateSerializer());
        serializerMap.put(Calendar.class, new CalendarSerializer());
        serializerMap.put(ArrayList.class, new ArraylistSerializer());
        serializerMap.put(LinkedList.class, new LinkedListSerializer());
        serializerMap.put(HashMap.class, new HashMapSerializer());
        serializerMap.put(HashSet.class, new HashSetSerializer());
    }
    
    public void register(Class<?> type, LicpSerializer<?> serializer)
    {
        serializerMap.put(type, serializer);
    }
    
    @SuppressWarnings("unchecked")
    public <T> LicpSerializer<T> get(Class<T> type, InternalLicp licp)
    {
        LicpSerializer<T> serializer = (LicpSerializer<T>) serializerMap.get(type);
        if (serializer != null)
        {
            return serializer;
        }
        if (type.isArray())
        {
            serializer = new ObjectArraySerializer<T>(type, licp);
        }
        else
        {
            serializer = new ObjectSerializer<T>(type, licp);
        }
        serializerMap.put(type, serializer);
        return serializer;
        
    }
}
