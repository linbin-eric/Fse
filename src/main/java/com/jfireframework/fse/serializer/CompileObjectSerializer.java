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
        classModel.addImport(Fse.class);
        classModel.addInterface(Output.class);
        int count = 0;
        try
        {
            Method      serialize      = Output.class.getMethod("serialize", new Class[]{Object.class, InternalByteArray.class, FseContext.class, int.class});
            Method      deSerialize    = Output.class.getMethod("deSerialize", new Class[]{InternalByteArray.class, FseContext.class});
            MethodModel serializeModel = new MethodModel(serialize, classModel);
            serializeModel.setParamterNames(new String[]{"o", "byteArray", "fseContext", "depth"});
            MethodModel deSerializeModel = new MethodModel(deSerialize, classModel);
            deSerializeModel.setParamterNames(new String[]{"byteArray", "fseContext"});
            classModel.putMethodModel(serializeModel);
            classModel.putMethodModel(deSerializeModel);
            StringBuilder serializeBody   = new StringBuilder();
            StringBuilder deSerializeBody = new StringBuilder();
            String        referenceName   = SmcHelper.getReferenceName(type, classModel);
            serializeBody.append(referenceName).append(" _target = (").append(referenceName).append(")o;\r\n");
            deSerializeBody.append(referenceName).append(" _target =new ").append(referenceName).append("();\r\n");
            deSerializeBody.append("fseContext.collectObject(_target);\r\n");
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
                        serializeBody.append("byteArray.writeVarInt(_target.").append(buildGetMethodName(each)).append("());\r\n");
                        deSerializeBody.append("_target.").append(buildSetMethodName(each)).append("(byteArray.readVarInt());\r\n");
                    }
                    else if (each.getType() == byte.class)
                    {
                        serializeBody.append("byteArray.put(_target.").append(buildGetMethodName(each)).append("());\r\n");
                        deSerializeBody.append("_target.").append(buildSetMethodName(each)).append("(byteArray.get());\r\n");
                    }
                    else if (each.getType() == char.class)
                    {
                        serializeBody.append("byteArray.writeVarChar(_target.").append(buildGetMethodName(each)).append("());\r\n");
                        deSerializeBody.append("_target.").append(buildSetMethodName(each)).append("(byteArray.readVarChar());\r\n");
                    }
                    else if (each.getType() == long.class)
                    {
                        serializeBody.append("byteArray.writeVarLong(_target.").append(buildGetMethodName(each)).append("());\r\n");
                        deSerializeBody.append("_target.").append(buildSetMethodName(each)).append("(byteArray.readVarLong());\r\n");
                    }
                    else if (each.getType() == float.class)
                    {
                        serializeBody.append("byteArray.writeFloat(_target.").append(buildGetMethodName(each)).append("());\r\n");
                        deSerializeBody.append("_target.").append(buildSetMethodName(each)).append("(byteArray.readFloat());\r\n");
                    }
                    else if (each.getType() == short.class)
                    {
                        serializeBody.append("byteArray.writeShort(_target.").append(buildGetMethodName(each)).append("());\r\n");
                        deSerializeBody.append("_target.").append(buildSetMethodName(each)).append("(byteArray.readShort());\r\n");
                    }
                    else if (each.getType() == double.class)
                    {
                        serializeBody.append("byteArray.writeDouble(_target.").append(buildGetMethodName(each)).append("());\r\n");
                        deSerializeBody.append("_target.").append(buildSetMethodName(each)).append("(byteArray.readDouble());\r\n");
                    }
                    else if (each.getType() == boolean.class)
                    {
                        serializeBody.append("if(_target.").append(buildGetMethodName(each)).append("()){\r\n");
                        serializeBody.append("byteArray.put((byte)0);\r\n");
                        serializeBody.append("}else{\r\n");
                        serializeBody.append("byteArray.put((byte)1);\r\n");
                        serializeBody.append("}\r\n");
                        deSerializeBody.append("if(byteArray.get()==0){\r\n");
                        deSerializeBody.append("_target.").append(buildSetMethodName(each)).append("(true);\r\n");
                        deSerializeBody.append("}\r\n");
                        deSerializeBody.append("else{\r\n");
                        deSerializeBody.append("_target.").append(buildSetMethodName(each)).append("(false);\r\n");
                        deSerializeBody.append("}\r\n");
                    }
                }
                else
                {
                    int modifiers = each.getType().getModifiers();
                    if (Modifier.isFinal(modifiers) || (Modifier.isAbstract(modifiers) == false && Modifier.isInterface(modifiers)))
                    {
                        FseSerializer serializer = serializerFactory.getSerializer(each.getType());
                        Entry         entry      = new Entry();
                        entry.serializerName = "_s_" + count;
                        count++;
                        entry.serializer = serializer;
                        FieldModel fieldModel = new FieldModel(entry.serializerName, FseSerializer.class, classModel);
                        classModel.addField(fieldModel);
                        MethodModel setMethod = new MethodModel(classModel);
                        setMethod.setAccessLevel(MethodModel.AccessLevel.PUBLIC);
                        setMethod.setReturnType(void.class);
                        setMethod.setMethodName("set" + entry.serializerName);
                        setMethod.setParamterTypes(FseSerializer.class);
                        setMethod.setBody(entry.serializerName + "=$0;\r\n");
                        classModel.putMethodModel(setMethod);
                        entries.add(entry);
                        String propertyName = "_value_" + count;
                        count++;
                        serializeBody.append("Object " + propertyName + " = _target." + buildGetMethodName(each) + "();\r\n");
                        serializeBody.append("if(" + propertyName + " == null ){byteArray.put(Fse.NULL);}");
                        if (Modifier.isFinal(modifiers))
                        {
                            serializeBody.append("else{\r\n");
                            serializeBody.append(entry.serializerName).append(".writeToBytes(").append(propertyName).append("," + Fse.USE_FIELD_TYPE + ",byteArray,fseContext,depth);\r\n");
                            serializeBody.append("}\r\n");
                        }
                        else
                        {
                            serializeBody.append("else if(" + propertyName + ".getClass()==" + SmcHelper.getReferenceName(each.getType(), classModel) + ".class){\r\n");
                            serializeBody.append(entry.serializerName).append(".writeToBytes(").append(propertyName).append("," + Fse.USE_FIELD_TYPE + ",byteArray,fseContext,depth);\r\n");
                            serializeBody.append("}\r\n");
                            serializeBody.append("else{\r\n");
                            serializeBody.append("fseContext.serialize(" + propertyName + ",byteArray,depth);\r\n");
                            serializeBody.append("}\r\n");
                        }
                        String flagName = "_flag_" + count;
                        count++;
                        deSerializeBody.append("int " + flagName + " = byteArray.readVarInt();\r\n");
                        deSerializeBody.append("if(" + flagName + "==0){\r\n_target.").append(buildSetMethodName(each)).append("(null);\r\n}\r\n");
                        deSerializeBody.append("else if(" + flagName + "==Fse.USE_FIELD_TYPE){\r\n");
                        deSerializeBody.append("_target." + buildSetMethodName(each) + "((" + SmcHelper.getReferenceName(each.getType(), classModel) + ")" + entry.serializerName).append(".readBytes(byteArray,fseContext));\r\n");
                        deSerializeBody.append("}\r\n");
                        deSerializeBody.append("else if(" + flagName + "<0){\r\n");
                        deSerializeBody.append("_target.").append(buildSetMethodName(each)).append("((" + SmcHelper.getReferenceName(each.getType(), classModel) + ")fseContext.getObjectByIndex(0-" + flagName + "));\r\n");
                        deSerializeBody.append("}\r\n");
                        deSerializeBody.append("else{\r\n");
                        deSerializeBody.append("_target.").append(buildSetMethodName(each)).append("((" + SmcHelper.getReferenceName(each.getType(), classModel) + ")fseContext.getClassRegistry(" + flagName + ").getSerializer().readBytes(byteArray,fseContext));\r\n");
                        deSerializeBody.append("}\r\n");
                    }
                    else
                    {
                        serializeBody.append("fseContext.serialize(_target." + buildGetMethodName(each) + "(),byteArray,depth);\r\n");
                        String flagName = "_flag_" + count;
                        count++;
                        deSerializeBody.append("int " + flagName + " = byteArray.readVarInt();\r\n");
                        deSerializeBody.append("if(" + flagName + "==0){\r\n_target.").append(buildSetMethodName(each)).append("(null);\r\n}\r\n");
                        deSerializeBody.append("else if(" + flagName + "<0){\r\n");
                        deSerializeBody.append("_target.").append(buildSetMethodName(each)).append("((" + SmcHelper.getReferenceName(each.getType(), classModel) + ")fseContext.getObjectByIndex(0-" + flagName + "));\r\n");
                        deSerializeBody.append("}\r\n");
                        deSerializeBody.append("else{\r\n");
                        deSerializeBody.append("_target.").append(buildSetMethodName(each)).append("((" + SmcHelper.getReferenceName(each.getType(), classModel) + ")fseContext.getClassRegistry(" + flagName + ").getSerializer().readBytes(byteArray,fseContext));\r\n");
                        deSerializeBody.append("}\r\n");
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
                Method setMethod = compile.getMethod("set" + entry.serializerName, FseSerializer.class);
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
            field.getDeclaringClass().getDeclaredMethod(setMethodName, field.getType());
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
    public Object readBytes(InternalByteArray byteArray, FseContext fseContext)
    {
        return output.deSerialize(byteArray, fseContext);
    }

    public static interface Output
    {
        void serialize(Object o, InternalByteArray byteArray, FseContext fseContext, int depth);

        Object deSerialize(InternalByteArray byteArray, FseContext fseContext);
    }

    class Entry
    {
        String        serializerName;
        FseSerializer serializer;
    }
}
