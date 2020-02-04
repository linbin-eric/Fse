package com.jfireframework.fse.serializer.extra;

import com.jfireframework.baseutil.reflect.ReflectUtil;
import com.jfireframework.fse.*;

import java.util.HashMap;
import java.util.Map;

public class ClassSerializer extends CycleFlagSerializer implements FseSerializer
{
    private Map<String, Class> nameToClass = new HashMap<String, Class>();

    public ClassSerializer()
    {
        nameToClass.put("void", void.class);
    }

    @Override
    public void init(Class<?> type, SerializerFactory serializerFactory)
    {
    }

    @Override
    public void writeToBytes(Object o, int classIndex, InternalByteArray byteArray, FseContext fseContext, int depth)
    {
        byteArray.writeVarInt(classIndex);
        byteArray.writeString(((Class) o).getName());
    }


    @Override
    public Object readBytes(InternalByteArray byteArray, FseContext fseContext)
    {
        String className = byteArray.readString();
        Class  ckass     = nameToClass.get(className);
        if (ckass != null)
        {
            return ckass;
        }
        try
        {
            ckass = Class.forName(className);
            nameToClass.put(className, ckass);
            return ckass;
        }
        catch (ClassNotFoundException e)
        {
            ReflectUtil.throwException(e);
            return null;
        }
    }

}
