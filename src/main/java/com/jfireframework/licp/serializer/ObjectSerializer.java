package com.jfireframework.licp.serializer;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import com.jfireframework.baseutil.collection.buffer.ByteBuf;
import com.jfireframework.baseutil.exception.JustThrowException;
import com.jfireframework.baseutil.reflect.ReflectUtil;
import com.jfireframework.licp.InternalLicp;
import com.jfireframework.licp.LicpIgnore;
import com.jfireframework.licp.field.CacheField;
import com.jfireframework.licp.field.FieldFactory;
import sun.misc.Unsafe;

public class ObjectSerializer<T> implements LicpSerializer<T>
{
    private final CacheField[]             fields;
    private static final Comparator<Field> fieldCompator = new Comparator<Field>() {
                                                             
                                                             @Override
                                                             public int compare(Field o1, Field o2)
                                                             {
                                                                 return o1.getName().compareTo(o2.getName());
                                                             }
                                                             
                                                         };
    private final Class<?>                 type;
    private final static Unsafe            unsafe        = ReflectUtil.getUnsafe();
    
    public ObjectSerializer(Class<T> type, InternalLicp licp)
    {
        this.type = type;
        Field[] fields = ReflectUtil.getAllFields(type);
        List<Field> list = new LinkedList<Field>();
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
        try
        {
            T holder = (T) unsafe.allocateInstance(type);
            // 在这个地方把对象放入。在外面放入就来不及了
            licp.putObject(holder);
            for (CacheField each : fields)
            {
                each.read(holder, buf, licp);
            }
            return holder;
        }
        catch (InstantiationException e)
        {
            throw new JustThrowException(e);
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public T deserialize(ByteBuffer buf, InternalLicp licp)
    {
        try
        {
            T holder = (T) unsafe.allocateInstance(type);
            // 在这个地方把对象放入。在外面放入就来不及了
            licp.putObject(holder);
            for (CacheField each : fields)
            {
                each.read(holder, buf, licp);
            }
            return holder;
        }
        catch (InstantiationException e)
        {
            throw new JustThrowException(e);
        }
    }
    
}
