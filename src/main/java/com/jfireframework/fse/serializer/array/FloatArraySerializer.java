package com.jfireframework.fse.serializer.array;

import com.jfireframework.fse.*;

public class FloatArraySerializer extends CycleFlagSerializer implements FseSerializer
{
    private final boolean primitive;

    public FloatArraySerializer(boolean primitive)
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
            float[] array = (float[]) o;
            byteArray.writePositive(array.length);
            for (float i : array)
            {
                byteArray.writeFloat(i);
            }
        }
        else
        {
            Float[] array = (Float[]) o;
            byteArray.writePositive(array.length );
            for (Float each : array)
            {
                if (each == null)
                {
                    byteArray.put((byte) 0);
                }
                else
                {
                    byteArray.put((byte) 1);
                    byteArray.writeFloat(each);
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
            float[] array = new float[len];
            for (int i = 0; i < len; i++)
            {
                array[i] = byteArray.readFloat();
            }
            return array;
        }
        else
        {
            Float[] array = new Float[len];
            for (int i = 0; i < len; i++)
            {
                if (byteArray.get() == 0)
                {
                    array[i] = null;
                }
                else
                {
                    array[i] = Float.valueOf(byteArray.readFloat());
                }
            }
            return array;
        }
    }

}
