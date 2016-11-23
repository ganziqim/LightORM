package com.ganziqim.utils;

import java.lang.reflect.Field;

/**
 * Created by ganzi on 2016/11/24.
 */
public class InstanceValueGetter {
    public static String getGetterName(final String fieldName) {
        StringBuilder result = new StringBuilder("get" + fieldName);

        result.setCharAt(3, Character.toUpperCase(fieldName.charAt(0)));

        return result.toString();
    }

    //clazz.getMethod("get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1)).invoke(obj)

    public static Object getValue(Object obj, Field field) throws Exception {
        return obj.getClass().getMethod(getGetterName(field.getName())).invoke(obj);
    }
}
