package com.ganziqim.utils;

/**
 * Created by ganzi on 2016/11/23.
 */
public class ArrayUtils {
    public static <T> boolean contains(T[] array, T obj) {
        for (T i : array) {
            if (i.equals(obj)) {
                return true;
            }
        }
        return false;
    }
}
