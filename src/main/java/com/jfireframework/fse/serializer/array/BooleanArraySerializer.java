package com.jfireframework.fse.serializer.array;

import com.jfireframework.fse.*;

public class BooleanArraySerializer extends CycleFlagSerializer implements FseSerializer
{
    private final boolean primitive;

    public BooleanArraySerializer(boolean primitive)
    {
        this.primitive = primitive;
    }

    @Override
    public void init(Class<?> type, SerializerFactory serializerFactory)
    {
    }

    @Override
    public void writeToBytes(Object o, int classIndex, InternalByteArray byteArray, FseContext fseContext, int depth)
    {
        byteArray.writeVarInt(classIndex);
        writeElement(o, byteArray);
    }

    private void writeElement(Object o, InternalByteArray byteArray)
    {
        if (primitive)
        {
            boolean[] array = (boolean[]) o;
            byteArray.writePositive(array.length);
            for (boolean i : array)
            {
                if (i)
                {
                    byteArray.put((byte) 0);
                }
                else
                {
                    byteArray.put((byte) 1);
                }
            }
        }
        else
        {
            Boolean[] array = (Boolean[]) o;
            byteArray.writePositive(array.length);
            for (Boolean each : array)
            {
                if (each == null)
                {
                    byteArray.put((byte) -1);
                }
                else if (each == true)
                {
                    byteArray.put((byte) 0);
                }
                else
                {
                    byteArray.put((byte) 1);
                }
            }
        }
    }

    @Override
    public Object readBytes(InternalByteArray byteArray, FseContext fseContext)
    {
        int len = byteArray.readPositive();
        return readElement(byteArray, len);
    }

    private Object readElement(InternalByteArray byteArray, int len)
    {
        if (primitive)
        {
            boolean[] array = new boolean[len];
            for (int i = 0; i < len; i++)
            {
                if (byteArray.get() == 0)
                {
                    array[i] = true;
                }
                else
                {
                    array[i] = false;
                }
            }
            return array;
        }
        else
        {
            Boolean[] array = new Boolean[len];
            for (int i = 0; i < len; i++)
            {
                byte b = byteArray.get();
                if (b == (byte) -1)
                {
                    array[i] = null;
                }
                else if (b == 0)
                {
                    array[i] = true;
                }
                else
                {
                    array[i] = false;
                }
            }
            return array;
        }
    }
//    @Override
//    public Object readBytesWithoutRegisterClass(InternalByteArray byteArray, FseContext fseContext)
//    {
//        int len = byteArray.readPositive();
//        if (len == 0)
//        {
//            return null;
//        }
//        return readElement(byteArray, len - 1);
//    }
}
