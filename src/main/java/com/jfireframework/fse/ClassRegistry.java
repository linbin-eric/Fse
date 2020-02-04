package com.jfireframework.fse;

import com.jfireframework.baseutil.reflect.ReflectUtil;

import java.lang.reflect.Method;
import java.util.*;

public class ClassRegistry
{
    private       Entry[]            entries      = new Entry[64];
    private final int                startIndex   = 1;
    private       int                sequence     = startIndex;
    private       int                buildInIndex = startIndex;
    private       SerializerFactory  serializerFactory;
    private       Map<String, Class> nameToClass  = new HashMap<String, Class>();

    public ClassRegistry(SerializerFactory serializerFactory)
    {
        this.serializerFactory = serializerFactory;
        processBuildIn();
    }

    private void processBuildIn()
    {
        List<Class<?>> buildIn = new LinkedList<Class<?>>();
        buildIn.add(int[].class);
        buildIn.add(byte[].class);
        buildIn.add(char[].class);
        buildIn.add(short[].class);
        buildIn.add(long[].class);
        buildIn.add(double[].class);
        buildIn.add(float[].class);
        buildIn.add(boolean[].class);
        buildIn.add(String.class);
        buildIn.add(Integer.class);
        buildIn.add(Float.class);
        buildIn.add(Double.class);
        buildIn.add(Short.class);
        buildIn.add(Byte.class);
        buildIn.add(Character.class);
        buildIn.add(Boolean.class);
        buildIn.add(Long.class);
        buildIn.add(Object.class);
        buildIn.add(LinkedList.class);
        buildIn.add(ArrayList.class);
        buildIn.add(HashSet.class);
        buildIn.add(HashMap.class);
        buildIn.add(Method.class);
        for (Class<?> each : buildIn)
        {
            register(each);
        }
    }

    /**
     * 注册一个类
     *
     * @param ckass
     */
    public void register(Class<?> ckass)
    {
        if (sequence != buildInIndex)
        {
            throw new IllegalStateException();
        }
        int id = getEntry(ckass).getId();
        if (id < 0)
        {
            throw new IllegalArgumentException("该对象已经注册过");
        }
        else if (sequence != buildInIndex + 1)
        {
            throw new IllegalArgumentException("不能在收集了自定义对象后注册对象");
        }
        buildInIndex += 1;
    }

    public Entry getEntry(int id)
    {
        return entries[id];
    }

    public Entry getEntry(Class<?> src)
    {
        for (int i = startIndex; i < sequence; i++)
        {
            Entry entry = entries[i];
            if (entry.ckass == src)
            {
                return entry;
            }
        }
        if (sequence == entries.length)
        {
            Entry[] tmp = new Entry[entries.length << 1];
            System.arraycopy(entries, 0, tmp, 0, entries.length);
            entries = tmp;
        }
        Entry entry = new Entry();
        entry.setCkass(src);
        entry.setId(sequence);
        entry.setSerializer(serializerFactory.getSerializer(src));
        entries[sequence] = entry;
        sequence += 1;
        return entry;
    }

    public void clear()
    {
        for (int i = buildInIndex; i < sequence; i++)
        {
            entries[i] = null;
        }
        sequence = buildInIndex;
    }

    public void serializeClass(InternalByteArray byteArray)
    {
        for (int i = buildInIndex; i < sequence; i++)
        {
            String name = entries[i].getCkass().getName();
            byteArray.writeString(name);
        }
    }

    public void deSerializeClass(InternalByteArray byteArray)
    {
        while (byteArray.remainRead())
        {
            String s      = byteArray.readString();
            Class  aClass = nameToClass.get(s);
            if (aClass == null)
            {
                try
                {
                    aClass = Class.forName(s);
                    nameToClass.put(s, aClass);
                }
                catch (ClassNotFoundException e)
                {
                    ReflectUtil.throwException(e);
                }
            }
            getEntry(aClass);
        }
    }

    public static class Entry
    {
        Class         ckass;
        int           id;
        FseSerializer serializer;

        public Class getCkass()
        {
            return ckass;
        }

        public void setCkass(Class ckass)
        {
            this.ckass = ckass;
        }

        public int getId()
        {
            return id;
        }

        public void setId(int id)
        {
            this.id = id;
        }

        public FseSerializer getSerializer()
        {
            return serializer;
        }

        public void setSerializer(FseSerializer serializer)
        {
            this.serializer = serializer;
        }
    }
}
