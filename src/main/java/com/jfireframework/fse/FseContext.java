package com.jfireframework.fse;

public class FseContext
{
    private SerializerFactory serializerFactory = new SerializerFactory();
    private ObjectCollector   objectCollector   = new ObjectCollector();
    private ClassRegistry     classRegistry     = new ClassRegistry(serializerFactory);
    private boolean           withCycle         = true;

    public void setWithCycle(boolean withCycle)
    {
        this.withCycle = withCycle;
    }

    public int collectObject(Object o)
    {
        if (withCycle == false)
        {
            return -1;
        }
        return objectCollector.collect(o);
    }

    public void serialzeClass(InternalByteArray byteArray)
    {
        classRegistry.serializeClass(byteArray);
    }

    public void deSerializeClass(InternalByteArray byteArray)
    {
        classRegistry.deSerializeClass(byteArray);
    }

    public void clear()
    {
        objectCollector.clear();
        classRegistry.clear();
        withCycle = true;
    }

    public Object getObjectByIndex(int i)
    {
        return objectCollector.getObject(i);
    }

    public void registerClass(Class ckass)
    {
        classRegistry.register(ckass);
    }

    public void startSerilaize(Object o, InternalByteArray byteArray)
    {
        if (o == null)
        {
            byteArray.put(Fse.NULL);
            return;
        }
        Class<?>            ckass = o.getClass();
        ClassRegistry.Entry entry = classRegistry.getEntry(ckass);
        if (entry.getSerializer().needSupportCycle())
        {
            withCycle = true;
            entry.getSerializer().writeToBytes(o, entry.getId(), byteArray, this, Fse.SUPPORT_CYCLE);
        }
        else
        {
            try
            {
                withCycle = false;
                entry.getSerializer().writeToBytes(o, entry.getId(), byteArray, this, 0);
                byteArray.setByte(0, Fse.WITHOUT_CYCLE);
            }
            catch (ShouldSupportCycleException | StackOverflowError e)
            {
                withCycle = true;
                entry.getSerializer().supportCycle();
                byteArray.setWritePosi(5);
                entry.getSerializer().writeToBytes(o, entry.getId(), byteArray, this, Fse.SUPPORT_CYCLE);
            }
        }
    }

    public void serialize(Object o, InternalByteArray byteArray, int depth)
    {
        if (o == null)
        {
            byteArray.put(Fse.NULL);
            return;
        }
        Class<?>            ckass = o.getClass();
        ClassRegistry.Entry entry = classRegistry.getEntry(ckass);
        entry.getSerializer().writeToBytes(o, entry.getId(), byteArray, this, depth);
    }

    public ClassRegistry.Entry getClassRegistry(Class ckass)
    {
        return classRegistry.getEntry(ckass);
    }

    public ClassRegistry.Entry getClassRegistry(int id)
    {
        return classRegistry.getEntry(id);
    }

    public void useCompile()
    {
        serializerFactory.setUseCompile(true);
    }

    public void registerFseSerializer(Class ckass, FseSerializer fseSerializer)
    {
        serializerFactory.register(ckass, fseSerializer);
    }
}
