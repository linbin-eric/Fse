package com.jfireframework.licp.serializer.array;

import com.jfireframework.licp.InternalLicp;
import com.jfireframework.licp.buf.ByteBuf;
import com.jfireframework.licp.util.BufferUtil;

import java.nio.ByteBuffer;

public class FloatArraySerializer extends AbstractArraySerializer<float[]>
{
    
    public FloatArraySerializer()
    {
        super(float[].class);
    }
    
    @Override
    public void serialize(float[] src, ByteBuf<?> buf, InternalLicp licp)
    {
        float[] array = src;
        buf.writePositive(array.length);
        for (float each : array)
        {
            buf.writeFloat(each);
        }
    }
    
    @Override
    public float[] deserialize(ByteBuf<?> buf, InternalLicp licp)
    {
        int length = buf.readPositive();
        float[] array = new float[length];
        licp.putObject(array);
        for (int i = 0; i < length; i++)
        {
            array[i] = buf.readFloat();
        }
        return array;
    }
    
    @Override
    public float[] deserialize(ByteBuffer buf, InternalLicp licp)
    {
        int length = BufferUtil.readPositive(buf);
        float[] array = new float[length];
        licp.putObject(array);
        for (int i = 0; i < length; i++)
        {
            array[i] = BufferUtil.readFloat(buf);
        }
        return array;
    }
    
}
