package com.jfireframework.fse.serializer;

import com.jfireframework.fse.*;

import java.lang.reflect.Array;
import java.lang.reflect.Modifier;

public class ArraySerializer extends CycleFlagSerializer implements FseSerializer
{
    private boolean       componentTypeFinal = false;
    private FseSerializer serializer;
    private Class<?>      componentType;
    private boolean       primitiveOrWrapper = false;

    @Override
    public void init(Class<?> type, SerializerFactory serializerFactory)
    {
        componentType = type.getComponentType();
        if (Modifier.isFinal(componentType.getModifiers()))
        {
            componentTypeFinal = true;
            serializer = serializerFactory.getSerializer(componentType);
        }
        while (type.isArray())
        {
            type = type.getComponentType();
        }
        if (type.isPrimitive() || type == Integer.class//
                || type == Short.class//
                || type == Byte.class//
                || type == Boolean.class//
                || type == Character.class//
                || type == Double.class//
                || type == Float.class//
                || type == Long.class//
                || type == String.class)
        {
            primitiveOrWrapper = true;
        }
    }

    @Override
    public void writeToBytes(Object o, int classIndex, InternalByteArray byteArray, FseContext fseContext, int depth)
    {
        if (primitiveOrWrapper)
        {
            byteArray.writeVarInt(classIndex);
            int length = ((Object[]) o).length;
            byteArray.writePositive(length);
            writeElement((Object[]) o, byteArray, fseContext, -1);
            return;
        }
        else
        {
            super.writeToBytes(o, classIndex, byteArray, fseContext, depth);
        }
    }

    @Override
    public void doWriteToBytes(Object o, InternalByteArray byteArray, FseContext fseContext, int depth)
    {
        int length = ((Object[]) o).length;
        byteArray.writePositive(length);
        writeElement((Object[]) o, byteArray, fseContext, depth);
    }

    private void writeElement(Object[] o, InternalByteArray byteArray, FseContext fseContext, int depth)
    {
        if (componentTypeFinal)
        {
            for (Object each : o)
            {
                serializer.writeToBytesWithoutRegisterClass(each, byteArray, fseContext, depth);
            }
        }
        else
        {
            for (Object each : o)
            {
                fseContext.serialize(each, byteArray, depth);
            }
        }
    }

    @Override
    public void doWriteToBytesWithoutRegisterClass(Object o, InternalByteArray byteArray, FseContext fseContext, int depth)
    {
        int length = ((Object[]) o).length;
        byteArray.writeVarInt(length + 1);
        writeElement((Object[]) o, byteArray, fseContext, depth);
    }

    @Override
    public Object readBytes(InternalByteArray byteArray, FseContext fseContext)
    {
        int len = byteArray.readPositive();
        return readElement(byteArray, fseContext, len);
    }

    private Object readElement(InternalByteArray byteArray, FseContext fseContext, int len)
    {
        Object[] instance = (Object[]) Array.newInstance(componentType, len);
        if (primitiveOrWrapper == false)
        {
            fseContext.collectObject(instance);
        }
        if (componentTypeFinal)
        {
            for (int i = 0; i < len; i++)
            {
                instance[i] = serializer.readBytesWithoutRegisterClass(byteArray, fseContext);
            }
        }
        else
        {
            for (int i = 0; i < len; i++)
            {
                instance[i] = Helper.deSerialize(byteArray, fseContext);
            }
        }
        return instance;
    }

    @Override
    public Object readBytesWithoutRegisterClass(InternalByteArray byteArray, FseContext fseContext)
    {
        int len = byteArray.readVarInt();
        if (len < 0)
        {
            return fseContext.getObjectByIndex(0 - len);
        }
        if (len == 0)
        {
            return null;
        }
        return readElement(byteArray, fseContext, len - 1);
    }
}
