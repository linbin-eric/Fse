package com.jfireframework.fse;

public class User
{
    private int    age;
    private String name;
    private Home   home;

    public Home getHome()
    {
        return home;
    }

    public void setHome(Home home)
    {
        this.home = home;
    }

    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        this.age = age;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
