package com.jfireframework.licp.serializer;

import com.jfireframework.baseutil.reflect.ReflectUtil;
import com.jfireframework.baseutil.reflect.UNSAFE;
import com.jfireframework.licp.InternalLicp;
import com.jfireframework.licp.LicpIgnore;
import com.jfireframework.licp.buf.ByteBuf;
import com.jfireframework.licp.field.CacheField;
import com.jfireframework.licp.field.FieldFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.util.*;

public class ObjectSerializer<T> implements LicpSerializer<T>
{
    private final        CacheField[]      fields;
    private static final Comparator<Field> fieldCompator = new Comparator<Field>()
    {

        @Override
        public int compare(Field o1, Field o2)
        {
            return o1.getName().compareTo(o2.getName());
        }
    };
    private final        Class<?>          type;

    public ObjectSerializer(Class<T> type, InternalLicp licp)
    {
        this.type = type;
        Field[]     fields = getAllFields(type);
        List<Field> list   = new LinkedList<Field>();
        for (Field each : fields)
        {
            if (Modifier.isStatic(each.getModifiers()) || each.isAnnotationPresent(LicpIgnore.class))
            {
                continue;
            }
            list.add(each);
        }
        fields = list.toArray(new Field[list.size()]);
        Arrays.sort(fields, fieldCompator);
        CacheField[] tmp = new CacheField[fields.length];
        for (int i = 0; i < tmp.length; i++)
        {
            tmp[i] = FieldFactory.build(fields[i], licp);
        }
        this.fields = tmp;
    }

    protected Field[] getAllFields(Class<?> type)
    {
        List<Field> list = new ArrayList<Field>();
        while (type != Object.class)
        {
            for (Field each : type.getDeclaredFields())
            {
                list.add(each);
            }
            type = type.getSuperclass();
        }
        return list.toArray(new Field[list.size()]);
    }

    @Override
    public void serialize(T src, ByteBuf<?> buf, InternalLicp licp)
    {
        for (CacheField each : fields)
        {
            each.write(src, buf, licp);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public T deserialize(ByteBuf<?> buf, InternalLicp licp)
    {
        T holder = (T) UNSAFE.allocateInstance(type);
        // 在这个地方把对象放入。在外面放入就来不及了
        licp.putObject(holder);
        for (CacheField each : fields)
        {
            each.read(holder, buf, licp);
        }
        return holder;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T deserialize(ByteBuffer buf, InternalLicp licp)
    {
        T holder = (T) UNSAFE.allocateInstance(type);
        // 在这个地方把对象放入。在外面放入就来不及了
        licp.putObject(holder);
        for (CacheField each : fields)
        {
            each.read(holder, buf, licp);
        }
        return holder;
    }
}
