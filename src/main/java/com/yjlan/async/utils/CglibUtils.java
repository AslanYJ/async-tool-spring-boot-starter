package com.yjlan.async.utils;

/**
 * @author yjlan
 * @version V1.0
 * @Description 动态代理工具类
 * @date 2022.02.24 09:31
 */
public class CglibUtils {

    public static Class<?> filterCglibProxyClass(Class<?> clazz) {
        while (isCglibProxyClass(clazz)) {
            clazz = clazz.getSuperclass();
        }
        return clazz;
    }

    public static boolean isCglibProxyClass(Class<?> clazz) {
        return clazz != null && clazz.getName().contains("$$");
    }

}
