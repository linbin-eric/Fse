package com.jfireframework.fse.serializer.extra;

import com.jfireframework.baseutil.reflect.ReflectUtil;
import com.jfireframework.fse.*;

import java.lang.reflect.Method;

public class MethodSerializer extends CycleFlagSerializer implements FseSerializer
{
    @Override
    public void init(Class<?> type, SerializerFactory serializerFactory)
    {
    }

    @Override
    public void writeToBytes(Object o, int classIndex, InternalByteArray byteArray, FseContext fseContext, int depth)
    {
        if (o == null)
        {
            byteArray.writeVarInt(0);
            return;
        }
        byteArray.writeVarInt(classIndex);
        this.writeToBytesWithoutRegisterClass(o, byteArray, fseContext, depth);
    }

    @Override
    public void writeToBytesWithoutRegisterClass(Object o, InternalByteArray byteArray, FseContext fseContext, int depth)
    {
        Method              method         = (Method) o;
        Class<?>            declaringClass = method.getDeclaringClass();
        ClassRegistry.Entry classRegistry  = fseContext.getClassRegistry(declaringClass);
        byteArray.writePositive(classRegistry.getId());
        byteArray.writeString(method.getName());
        Class<?>[] parameterTypes = method.getParameterTypes();
        byteArray.writePositive(parameterTypes.length);
        for (Class<?> each : parameterTypes)
        {
            int id = fseContext.getClassRegistry(each).getId();
            byteArray.writePositive(id);
        }
    }

    @Override
    public Object readBytes(InternalByteArray byteArray, FseContext fseContext)
    {
        return readBytesWithoutRegisterClass(byteArray, fseContext);
    }

    @Override
    public Object readBytesWithoutRegisterClass(InternalByteArray byteArray, FseContext fseContext)
    {
        int    id                 = byteArray.readPositive();
        Class  ckass              = fseContext.getClassRegistry(id).getCkass();
        String methodName         = byteArray.readString();
        int    numOfParameterType = byteArray.readPositive();
        try
        {
            if (numOfParameterType == 0)
            {
                return ckass.getDeclaredMethod(methodName);
            }
            else
            {
                Class[] parameterTypes = new Class[numOfParameterType];
                for (int i = 0; i < parameterTypes.length; i++)
                {
                    parameterTypes[i] = fseContext.getClassRegistry(byteArray.readPositive()).getCkass();
                }
                return ckass.getDeclaredMethod(methodName, parameterTypes);
            }
        }
        catch (NoSuchMethodException e)
        {
            ReflectUtil.throwException(e);
            return null;
        }
    }
}
