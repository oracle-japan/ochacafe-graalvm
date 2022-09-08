package com.example;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class App 
{
    public static void main(String[] args) throws Exception{

        Class<?> clazz = Class.forName(args[0]);
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        Object object = constructor.newInstance();

        Method method = clazz.getDeclaredMethod(args[1]);
        method.invoke(object);
    }

}
