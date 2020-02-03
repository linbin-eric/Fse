package com.jfireframework.fse.serializer;

import com.jfireframework.baseutil.reflect.ReflectUtil;
import com.jfireframework.baseutil.smc.SmcHelper;
import com.jfireframework.baseutil.smc.compiler.CompileHelper;
import com.jfireframework.baseutil.smc.model.ClassModel;
import com.jfireframework.baseutil.smc.model.FieldModel;
import com.jfireframework.baseutil.smc.model.MethodModel;
import com.jfireframework.fse.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CompileObjectSerializer extends CycleFlagSerializer implements FseSerializer
{
    private Output output;

    @Override
    public void init(Class<?> type, SerializerFactory serializerFactory)
    {
        List<Entry> entries    = new ArrayList<Entry>();
        List<Field> allFields  = getAllFields(type);
        ClassModel  classModel = new ClassModel("output_" + type.getSimpleName());
        classModel.addImport(Helper.class);
        classModel.addInterface(Output.class);
        int count = 0;
        try
        {
            Method      serialize        = Output.class.getMethod("serialize", new Class[]{Object.class, InternalByteArray.class, FseContext.class, int.class});
            Method      deSerialize      = Output.class.getMethod("deSerialize", new Class[]{InternalByteArray.class, FseContext.class});
            MethodModel serializeModel   = new MethodModel(serialize, classModel);
            MethodModel deSerializeModel = new MethodModel(deSerialize, classModel);
            classModel.putMethodModel(serializeModel);
            classModel.putMethodModel(deSerializeModel);
            StringBuilder serializeBody   = new StringBuilder();
            StringBuilder deSerializeBody = new StringBuilder();
            String        referenceName   = SmcHelper.getReferenceName(type, classModel);
            serializeBody.append(referenceName).append(" _target = (").append(referenceName).append(")$0;\r\n");
            deSerializeBody.append(referenceName).append(" _target =new ").append(referenceName).append("();\r\n");
            deSerializeBody.append("$1.collectObject(_target);\r\n");
            for (Field each : allFields)
            {
                if (isFieldGetAndSetMethodExist(each) == false)
                {
                    continue;
                }
                if (each.getType().isPrimitive())
                {
                    if (each.getType() == int.class)
                    {
                        serializeBody.append("$1.writeVarInt(_target.").append(buildGetMethodName(each)).append("());\r\n");
                        deSerializeBody.append("_target.").append(buildSetMethodName(each)).append("($0.readVarInt());\r\n");
                    }
                    else if (each.getType() == byte.class)
                    {
                        serializeBody.append("$1.put(_target.").append(buildGetMethodName(each)).append("());\r\n");
                        deSerializeBody.append("_target.").append(buildSetMethodName(each)).append("($0.get());\r\n");
                    }
                    else if (each.getType() == char.class)
                    {
                        serializeBody.append("$1.writeVarChar(_target.").append(buildGetMethodName(each)).append("());\r\n");
                        deSerializeBody.append("_target.").append(buildSetMethodName(each)).append("($0.readVarChar());\r\n");
                    }
                    else if (each.getType() == long.class)
                    {
                        serializeBody.append("$1.writeVarLong(_target.").append(buildGetMethodName(each)).append("());\r\n");
                        deSerializeBody.append("_target.").append(buildSetMethodName(each)).append("($0.readVarLong());\r\n");
                    }
                    else if (each.getType() == float.class)
                    {
                        serializeBody.append("$1.writeFloat(_target.").append(buildGetMethodName(each)).append("());\r\n");
                        deSerializeBody.append("_target.").append(buildSetMethodName(each)).append("($0.readFloat());\r\n");
                    }
                    else if (each.getType() == short.class)
                    {
                        serializeBody.append("$1.writeShort(_target.").append(buildGetMethodName(each)).append("());\r\n");
                        deSerializeBody.append("_target.").append(buildSetMethodName(each)).append("($0.readShort());\r\n");
                    }
                    else if (each.getType() == double.class)
                    {
                        serializeBody.append("$1.writeDouble(_target.").append(buildGetMethodName(each)).append("());\r\n");
                        deSerializeBody.append("_target.").append(buildSetMethodName(each)).append("($0.readDouble());\r\n");
                    }
                    else if (each.getType() == boolean.class)
                    {
                        serializeBody.append("if(_target.").append(buildGetMethodName(each)).append("()){\r\n");
                        serializeBody.append("$1.put((byte)0);\r\n");
                        serializeBody.append("}else{\r\n");
                        serializeBody.append("$1.put((byte)1);\r\n");
                        serializeBody.append("}\r\n");
                        deSerializeBody.append("if($0.get()==0){\r\n");
                        deSerializeBody.append("_target.").append(buildSetMethodName(each)).append("(true);\r\n");
                        deSerializeBody.append("}\r\n");
                        deSerializeBody.append("else{\r\n");
                        deSerializeBody.append("_target.").append(buildSetMethodName(each)).append("(false);\r\n");
                        deSerializeBody.append("}\r\n");
                    }
                }
                else
                {
                    if (Modifier.isFinal(each.getType().getModifiers()))
                    {
                        FseSerializer serializer = serializerFactory.getSerializer(each.getType());
                        Entry         entry      = new Entry();
                        entry.name = "_s_" + count;
                        count++;
                        entry.serializer = serializer;
                        FieldModel fieldModel = new FieldModel(entry.name, FseSerializer.class, classModel);
                        classModel.addField(fieldModel);
                        MethodModel setMethod = new MethodModel(classModel);
                        setMethod.setAccessLevel(MethodModel.AccessLevel.PUBLIC);
                        setMethod.setReturnType(void.class);
                        setMethod.setMethodName("set" + entry.name);
                        setMethod.setParamterTypes(FseSerializer.class);
                        setMethod.setBody(entry.name + "=$0;\r\n");
                        classModel.putMethodModel(setMethod);
                        entries.add(entry);
                        serializeBody.append(entry.name).append(".writeToBytesWithoutRegisterClass(_target.").append(buildGetMethodName(each)).append("(),$1,$2,$3);\r\n");
                        deSerializeBody.append("_target.").append(buildSetMethodName(each)).append("((" + SmcHelper.getReferenceName(each.getType(), classModel) + ")").append(entry.name).append(".readBytesWithoutRegisterClass($0,$1));\r\n");
                    }
                    else
                    {
                        serializeBody.append("$2.serialize(_target.").append(buildGetMethodName(each)).append("(),$1,$3);\r\n");
                        deSerializeBody.append("_target.").append(buildSetMethodName(each)).append("((" + SmcHelper.getReferenceName(each.getType(), classModel) + ")").append("Helper.deSerialize($0,$1));\r\n");
                    }
                }
            }
            serializeModel.setBody(serializeBody.toString());
            deSerializeBody.append("return _target;\r\n");
            deSerializeModel.setBody(deSerializeBody.toString());
            CompileHelper compileHelper = new CompileHelper();
            Class<?>      compile       = compileHelper.compile(classModel);
            output = (Output) compile.newInstance();
            for (Entry entry : entries)
            {
                Method setMethod = compile.getMethod("set" + entry.name, FseSerializer.class);
                setMethod.invoke(output, entry.serializer);
            }
        }
        catch (Throwable e)
        {
            ReflectUtil.throwException(e);
        }
    }

    private boolean isFieldGetAndSetMethodExist(Field field)
    {
        String getMethodName = buildGetMethodName(field);
        String setMethodName = buildSetMethodName(field);
        try
        {
            field.getDeclaringClass().getDeclaredMethod(getMethodName);
            field.getDeclaringClass().getDeclaredMethod(setMethodName,field.getType());
            return true;
        }
        catch (NoSuchMethodException e)
        {
            return false;
        }
    }

    private String buildGetMethodName(Field field)
    {
        if (field.getType().isPrimitive())
        {
            if (field.getType() == boolean.class)
            {
                return "is" + field.getName().toUpperCase().substring(0, 1) + field.getName().substring(1);
            }
            else
            {
                return "get" + field.getName().toUpperCase().substring(0, 1) + field.getName().substring(1);
            }
        }
        else
        {
            return "get" + field.getName().toUpperCase().substring(0, 1) + field.getName().substring(1);
        }
    }

    private String buildSetMethodName(Field field)
    {
        return "set" + field.getName().toUpperCase().substring(0, 1) + field.getName().substring(1);
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

    @Override
    public void doWriteToBytes(Object o, InternalByteArray byteArray, FseContext fseContext, int depth)
    {
        output.serialize(o, byteArray, fseContext, depth);
    }

    @Override
    public void doWriteToBytesWithoutRegisterClass(Object o, InternalByteArray byteArray, FseContext fseContext, int depth)
    {
        byteArray.put((byte) 1);
        output.serialize(o, byteArray, fseContext, depth);
    }

    @Override
    public Object readBytes(InternalByteArray byteArray, FseContext fseContext)
    {
        return output.deSerialize(byteArray, fseContext);
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

    public static interface Output
    {
        void serialize(Object o, InternalByteArray byteArray, FseContext fseContext, int depth);

        Object deSerialize(InternalByteArray byteArray, FseContext fseContext);
    }

    class Entry
    {
        String        name;
        FseSerializer serializer;
    }
}
