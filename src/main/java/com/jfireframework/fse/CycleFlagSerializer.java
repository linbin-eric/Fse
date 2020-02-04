package com.jfireframework.fse;

public abstract class CycleFlagSerializer implements FseSerializer
{
    protected boolean needSupportCycle = false;

    @Override
    public boolean needSupportCycle()
    {
        return needSupportCycle;
    }

    @Override
    public void supportCycle()
    {
        needSupportCycle = true;
    }

    @Override
    public void writeToBytes(Object o, int classIndex, InternalByteArray byteArray, FseContext fseContext, int depth)
    {
        if (depth == Fse.SUPPORT_CYCLE)
        {
            int result = fseContext.collectObject(o);
            if (result > 0)
            {
                byteArray.writeVarInt(0 - result);
                return;
            }
        }
        else
        {
            depth += 1;
            if (depth > Fse.maxDepth)
            {
                needSupportCycle = true;
                throw new ShouldSupportCycleException();
            }
        }
        byteArray.writeVarInt(classIndex);
        doWriteToBytes(o, byteArray, fseContext, depth);
    }

    public void doWriteToBytes(Object o, InternalByteArray byteArray, FseContext fseContext, int depth)
    {
        throw new UnsupportedOperationException();
    }
}
