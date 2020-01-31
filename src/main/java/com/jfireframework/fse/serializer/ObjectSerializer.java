package com.jfireframework.fse.serializer;

import com.jfireframework.baseutil.reflect.UNSAFE;
import com.jfireframework.baseutil.reflect.ValueAccessor;
import com.jfireframework.fse.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ObjectSerializer extends CycleFlagSerializer implements FseSerializer
{
    public static final byte NULL     = 0;
    public static final byte NOT_NULL = 1;
    Entry[] entries;
    private Class<?> type;

    @Override
    public void init(Class<?> type, SerializerFactory serializerFactory)
    {
        this.type = type;
        List<Field> list = getAllFields(type);
        entries = new Entry[list.size()];
        for (int i = 0; i < list.size(); i++)
        {
            Entry entry = buildEntry(serializerFactory, list.get(i));
            entries[i] = entry;
        }
    }

    private Entry buildEntry(SerializerFactory serializerFactory, Field field)
    {
        Entry entry = new Entry();
        entry.field = field;
        ValueAccessor valueAccessor = new ValueAccessor(field);
        entry.accessor = valueAccessor;
        Class<?> fieldType = field.getType();
        if (fieldType.isArray())
        {
            entry.array = true;
            if (Modifier.isFinal(fieldType.getModifiers()))
            {
                entry.finalType = true;
                entry.serializer = serializerFactory.getSerializer(fieldType);
            }
        }
        else
        {
            entry.type = getType(fieldType);
            if (entry.type == BuildInType.NOT_IN && Modifier.isFinal(fieldType.getModifiers()))
            {
                entry.finalType = true;
                FseSerializer serializer = serializerFactory.getSerializer(fieldType);
                entry.serializer = serializer;
            }
        }
        return entry;
    }

    private List<Field> getAllFields(Class<?> type)
    {
        List<Field> list = new ArrayList<Field>();
        while (type != Object.class)
        {
            for (Field each : type.getDeclaredFields())
            {
                if (each.isAnnotationPresent(FseIgnore.class))
                {
                    continue;
                }
                int modifiers = each.getModifiers();
                if (Modifier.isStatic(modifiers))
                {
                    continue;
                }
                list.add(each);
            }
            type = type.getSuperclass();
        }
        Collections.sort(list, new Comparator<Field>()
        {
            @Override
            public int compare(Field o1, Field o2)
            {
                return o1.toString().compareTo(o2.toString());
            }
        });
        return list;
    }

    private BuildInType getType(Class<?> fieldType)
    {
        if (fieldType.isPrimitive() == false)
        {
            return BuildInType.NOT_IN;
        }
        if (fieldType == int.class)
        {
            return BuildInType.INT;
        }
        else if (fieldType == byte.class)
        {
            return BuildInType.BYTE;
        }
        else if (fieldType == char.class)
        {
            return BuildInType.CHAR;
        }
        else if (fieldType == boolean.class)
        {
            return BuildInType.BOOLEAN;
        }
        else if (fieldType == float.class)
        {
            return BuildInType.FLOAT;
        }
        else if (fieldType == long.class)
        {
            return BuildInType.LONG;
        }
        else if (fieldType == short.class)
        {
            return BuildInType.SHORT;
        }
        else if (fieldType == double.class)
        {
            return BuildInType.DOUBLE;
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void doWriteToBytes(Object o, InternalByteArray byteArray, FseContext fseContext, int depth)
    {
        writeEntries(o, byteArray, fseContext, depth);
    }

    private void writeEntries(Object o, InternalByteArray byteArray, FseContext fseContext, int depth)
    {
        for (Entry entry : entries)
        {
            if (entry.array == false)
            {
                serializeProperty(o, byteArray, fseContext, entry, depth);
            }
            else
            {
                Object property = entry.accessor.get(o);
                if (entry.finalType)
                {
                    entry.serializer.writeToBytesWithoutRegisterClass(entry.accessor.get(o), byteArray, fseContext, depth);
                }
                else
                {
                    fseContext.serialize(property, byteArray, depth);
                }
            }
        }
    }

    @Override
    public void doWriteToBytesWithoutRegisterClass(Object o, InternalByteArray byteArray, FseContext fseContext, int depth)
    {
        byteArray.put((byte) 1);
        writeEntries(o, byteArray, fseContext, depth);
    }

    @Override
    public Object readBytes(InternalByteArray byteArray, FseContext fseContext)
    {
        Object instance = UNSAFE.allocateInstance(type);
        fseContext.collectObject(instance);
        for (Entry entry : entries)
        {
            if (entry.array == false)
            {
                deSerializeProperty(byteArray, fseContext, instance, entry);
            }
            else
            {
                Object result;
                if (entry.finalType)
                {
                    result = entry.serializer.readBytesWithoutRegisterClass(byteArray, fseContext);
                }
                else
                {
                    result = Helper.deSerialize(byteArray, fseContext);
                }
                if (result != null)
                {
                    entry.accessor.setObject(instance, result);
                }
            }
        }
        return instance;
    }

    @Override
    public Object readBytesWithoutRegisterClass(InternalByteArray byteArray, FseContext fseContext)
    {
        int flag = byteArray.readVarInt();
        if (flag == 0)
        {
            return null;
        }
        else if (flag < 0)
        {
            return fseContext.getObjectByIndex(0 - flag);
        }
        return readBytes(byteArray, fseContext);
    }

    private void deSerializeProperty(InternalByteArray byteArray, FseContext fseContext, Object instance, Entry entry)
    {
        switch (entry.type)
        {
            case INT:
                entry.accessor.set(instance, byteArray.readVarInt());
                break;
            case BYTE:
                entry.accessor.set(instance, byteArray.get());
                break;
            case CHAR:
                entry.accessor.set(instance, byteArray.readVarChar());
                break;
            case LONG:
                entry.accessor.set(instance, byteArray.readVarLong());
                break;
            case FLOAT:
                entry.accessor.set(instance, byteArray.readFloat());
                break;
            case SHORT:
                entry.accessor.set(instance, byteArray.readShort());
                break;
            case DOUBLE:
                entry.accessor.set(instance, byteArray.readDouble());
                break;
            case BOOLEAN:
                if (byteArray.get() == 0)
                {
                    entry.accessor.set(instance, true);
                }
                else
                {
                    entry.accessor.set(instance, false);
                }
                break;
            case NOT_IN:
                if (entry.finalType)
                {
                    Object result = entry.serializer.readBytesWithoutRegisterClass(byteArray, fseContext);
                    if (result != null)
                    {
                        entry.accessor.setObject(instance, result);
                    }
                }
                else
                {
                    Object result = Helper.deSerialize(byteArray, fseContext);
                    if (result != null)
                    {
                        entry.accessor.setObject(instance, result);
                    }
                }
                break;
        }
    }

    private void serializeProperty(Object o, InternalByteArray byteArray, FseContext fseContext, Entry entry, int depth)
    {
        switch (entry.type)
        {
            case INT:
                byteArray.writeVarInt(entry.accessor.getInt(o));
                break;
            case BYTE:
                byteArray.put(entry.accessor.getByte(o));
                break;
            case CHAR:
                byteArray.writeVarChar(entry.accessor.getChar(o));
                break;
            case LONG:
                byteArray.writeVarLong(entry.accessor.getLong(o));
                break;
            case FLOAT:
                byteArray.writeFloat(entry.accessor.getFloat(o));
                break;
            case SHORT:
                byteArray.writeShort(entry.accessor.getShort(o));
                break;
            case DOUBLE:
                byteArray.writeDouble(entry.accessor.getDouble(o));
                break;
            case BOOLEAN:
                if (entry.accessor.getBoolean(o))
                {
                    byteArray.put((byte) 0);
                }
                else
                {
                    byteArray.put((byte) 1);
                }
                break;
            case NOT_IN:
                Object o1 = entry.accessor.get(o);
                if (entry.finalType)
                {
                    entry.serializer.writeToBytesWithoutRegisterClass(o1, byteArray, fseContext, depth);
                }
                else
                {
                    fseContext.serialize(o1, byteArray, depth);
                }
                break;
        }
    }

    enum BuildInType
    {
        INT, BYTE, CHAR, BOOLEAN, FLOAT, LONG, SHORT, DOUBLE,//
        NOT_IN
    }

    class Entry
    {
        Field         field;
        boolean       array     = false;
        boolean       finalType = false;
        BuildInType   type;
        ValueAccessor accessor;
        FseSerializer serializer;
    }
}
