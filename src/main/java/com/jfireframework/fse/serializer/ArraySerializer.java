package com.jfireframework.fse.serializer;

import com.jfireframework.fse.*;

import java.lang.reflect.Array;
import java.lang.reflect.Modifier;

public class ArraySerializer extends CycleFlagSerializer implements FseSerializer
{
    private boolean       componentTypeFinal = false;
    private FseSerializer serializer;
    private Class<?>      componentType;

    @Override
    public void init(Class<?> type, SerializerFactory serializerFactory)
    {
        componentType = type.getComponentType();
        if (Modifier.isFinal(componentType.getModifiers()))
        {
            componentTypeFinal = true;
            serializer = serializerFactory.getSerializer(componentType);
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
                if (each == null)
                {
                    byteArray.put(Fse.NULL);
                    continue;
                }
                serializer.writeToBytes(each, Fse.USE_FIELD_TYPE, byteArray, fseContext, depth);
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
    public Object readBytes(InternalByteArray byteArray, FseContext fseContext)
    {
        int len = byteArray.readPositive();
        return readElement(byteArray, fseContext, len);
    }

    private Object readElement(InternalByteArray byteArray, FseContext fseContext, int len)
    {
        Object[] instance = (Object[]) Array.newInstance(componentType, len);
        fseContext.collectObject(instance);
        if (componentTypeFinal)
        {
            for (int i = 0; i < len; i++)
            {
                //读取 Fse.USE_FIELD_TYPE 标记
                int flag = byteArray.readVarInt();
                if (flag == Fse.NULL)
                {
                    instance[i] = null;
                }
                else if (flag == Fse.USE_FIELD_TYPE)
                {
                    instance[i] = serializer.readBytes(byteArray, fseContext);
                }
                else if (flag < 0)
                {
                    instance[i] = fseContext.getObjectByIndex(0 - flag);
                }
                else
                {
                    throw new IllegalStateException("不应该出现此种情况");
                }
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
}
