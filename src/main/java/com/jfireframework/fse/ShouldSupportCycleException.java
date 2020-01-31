package com.jfireframework.fse;

public class ShouldSupportCycleException extends RuntimeException
{
    @Override
    public Throwable fillInStackTrace()
    {
        return this;
    }
}
