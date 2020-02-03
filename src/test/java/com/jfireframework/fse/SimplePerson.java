package com.jfireframework.fse;

public class SimplePerson
{
    private int    age;
    private String name;
    private Float  weight;

    @Override
    public String toString()
    {
        return "Person{" + "age=" + age + ", name='" + name + '\'' + ", weight=" + weight + '}';
    }

    @Override
    public boolean equals(Object x)
    {
        if (x instanceof SimplePerson)
        {
            SimplePerson target = (SimplePerson) x;
            if (target.age == age && name.equals(target.name) && target.weight.floatValue() == weight.floatValue())
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
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

    public Float getWeight()
    {
        return weight;
    }

    public void setWeight(Float weight)
    {
        this.weight = weight;
    }
}
