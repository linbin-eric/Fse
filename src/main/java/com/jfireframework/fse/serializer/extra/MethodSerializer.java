package com.jfireframework.fse.serializer.extra;

import com.jfireframework.baseutil.reflect.ReflectUtil;
import com.jfireframework.fse.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MethodSerializer extends CycleFlagSerializer implements FseSerializer
{
    private Map<String, Class> nameToClass = new HashMap<>();

    @Override
    public void init(Class<?> type, SerializerFactory serializerFactory)
    {
    }

    @Override
    public void writeToBytes(Object o, int classIndex, InternalByteArray byteArray, FseContext fseContext, int depth)
    {
        byteArray.writeVarInt(classIndex);
        Method   method         = (Method) o;
        Class<?> declaringClass = method.getDeclaringClass();
        byteArray.writeString(declaringClass.getName());
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
        String className = byteArray.readString();
        Class  ckass     = nameToClass.get(className);
        if (ckass == null)
        {
            try
            {
                ckass = Class.forName(className);
                nameToClass.put(className, ckass);
            }
            catch (ClassNotFoundException e)
            {
                ReflectUtil.throwException(e);
            }
        }
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
