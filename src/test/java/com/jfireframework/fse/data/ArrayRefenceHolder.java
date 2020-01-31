package com.jfireframework.fse.data;

public class ArrayRefenceHolder
{
    private int[][] a;
    private int[][] b;

    public ArrayRefenceHolder()
    {
        a = new int[][]{{1, 2}, {3, 4}};
        b = a;
    }

    public int[][] getA()
    {
        return a;
    }

    public int[][] getB()
    {
        return b;
    }
}
