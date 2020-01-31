package com.jfireframework.fse;

import java.util.IdentityHashMap;

public class ObjectCollector
{
    protected IdentityHashMap<Object, Integer> store;
    protected Object[]                         data;
    protected int                              sequence = 0;

    public ObjectCollector()
    {
        this(64);
    }

    public ObjectCollector(int len)
    {
        data = new Object[len];
        store = new IdentityHashMap<Object, Integer>(len);
    }

    /**
     * 向对象收集器放入对象。如果对象已经存在，返回对象下标。如果对象不存在，则收集到集合中，并且返回-1
     * 对象下标从1开始。
     *
     * @param src
     * @return
     */
    public int collect(Object src)
    {
        Integer exist = store.get(src);
        if (exist != null)
        {
            return exist;
        }
        if (sequence == data.length)
        {
            Object[] tmp = new Object[data.length << 1];
            System.arraycopy(data, 0, tmp, 0, data.length);
            data = tmp;
        }
        data[sequence] = src;
        sequence += 1;
        store.put(src, sequence);
        return -1;
    }

    public void clear()
    {
        for (int i = 0; i < sequence; i++)
        {
            data[i] = null;
        }
        store.clear();
        sequence = 0;
    }

    public Object getObject(int i)
    {
        return data[i - 1];
    }
}
