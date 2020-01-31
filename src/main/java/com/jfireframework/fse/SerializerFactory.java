package com.jfireframework.fse;

import com.jfireframework.fse.serializer.ArraySerializer;
import com.jfireframework.fse.serializer.CompileObjectSerializer;
import com.jfireframework.fse.serializer.ObjectSerializer;
import com.jfireframework.fse.serializer.array.*;
import com.jfireframework.fse.serializer.base.*;
import com.jfireframework.fse.serializer.extra.*;

import java.lang.reflect.Method;
import java.util.*;

public class SerializerFactory
{
    private Map<Class, FseSerializer> serializerMap = new IdentityHashMap<Class, FseSerializer>();
    private boolean                   useCompile    = false;

    public SerializerFactory()
    {
        serializerMap.put(int[].class, new IntArraySerializer(true));
        serializerMap.put(Integer[].class, new IntArraySerializer(false));
        serializerMap.put(short[].class, new ShortArraySerializer(true));
        serializerMap.put(Short[].class, new ShortArraySerializer(false));
        serializerMap.put(byte[].class, new ByteArraySerializer(true));
        serializerMap.put(Byte[].class, new ByteArraySerializer(false));
        serializerMap.put(float[].class, new FloatArraySerializer(true));
        serializerMap.put(Float[].class, new FloatArraySerializer(false));
        serializerMap.put(double[].class, new DoubleArraySerializer(true));
        serializerMap.put(Double[].class, new DoubleArraySerializer(false));
        serializerMap.put(long[].class, new LongArraySerializer(true));
        serializerMap.put(Long[].class, new LongArraySerializer(false));
        serializerMap.put(char[].class, new CharArraySerializer(true));
        serializerMap.put(Character[].class, new CharArraySerializer(false));
        serializerMap.put(boolean[].class, new BooleanArraySerializer(true));
        serializerMap.put(Boolean[].class, new BooleanArraySerializer(false));
        serializerMap.put(String[].class, new StringArraySerializer());
        serializerMap.put(Integer.class, new IntegerSerializer());
        serializerMap.put(Short.class, new ShortSerializer());
        serializerMap.put(Byte.class, new ByteSerializer());
        serializerMap.put(Long.class, new LongSerializer());
        serializerMap.put(Float.class, new FloatSerializer());
        serializerMap.put(Character.class, new CharSserializer());
        serializerMap.put(Double.class, new DoubleSerializer());
        serializerMap.put(Boolean.class, new BooleanSerializer());
        serializerMap.put(String.class, new StringSerializer());
        serializerMap.put(ArrayList.class, new ArrayListSerializer());
        serializerMap.put(Calendar.class, new CalendarSerializer());
        serializerMap.put(Class.class, new ClassSerializer());
        serializerMap.put(HashMap.class, new HashMapSerializer());
        serializerMap.put(HashSet.class, new HashSetSerializer());
        serializerMap.put(LinkedList.class, new LinkedListSerializer());
        serializerMap.put(Date.class, new UtilDateSerializer());
        serializerMap.put(java.sql.Date.class, new UtilDateSerializer());
        serializerMap.put(Object.class, new ObjectSerializer());
        serializerMap.put(Calendar.getInstance().getClass(), new CalendarSerializer());
        serializerMap.put(Method.class, new MethodSerializer());
    }

    public FseSerializer getSerializer(Class type)
    {
        FseSerializer fseSerializer = serializerMap.get(type);
        if (fseSerializer != null)
        {
            return fseSerializer;
        }
        if (EnumSet.class.isAssignableFrom(type))
        {
            fseSerializer = new EnumSetSerialzer();
        }
        else if (type.isArray())
        {
            fseSerializer = new ArraySerializer();
        }
        else
        {
            if (useCompile)
            {
                fseSerializer = new CompileObjectSerializer();
            }
            else
            {
                fseSerializer = new ObjectSerializer();
            }
        }
        serializerMap.put(type, fseSerializer);
        fseSerializer.init(type, this);
        return fseSerializer;
    }

    public void setUseCompile(boolean useCompile)
    {
        this.useCompile = useCompile;
    }

    public void register(Class ckass, FseSerializer fseSerializer)
    {
        serializerMap.put(ckass, fseSerializer);
    }
}
