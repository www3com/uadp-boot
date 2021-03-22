package com.upbos.data.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Title: ReflectUtils.java</p>
 * <p>Description: 反射工具</p>
 * <p>Copyright: Copyright (c) 2010-2020</p>
 * <p>Company: yideb.com</p>
 *
 * @author wangjz
 * @version 5.0.0
 * @since 2018年9月28日
 */
public class ReflectUtils {

    /**
     * 对象转成map
     *
     * @param obj
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, Object> objectToMap(Object obj) {
        Map<String, Object> map = new HashMap<>(16);
        for (Class<?> clz = obj.getClass(); clz != Object.class; clz = clz.getSuperclass()) {
            Field[] fields = clz.getDeclaredFields();
            try {
                for (Field field : fields) {
                    if (field.isAccessible()) {
                        map.put(field.getName(), field.get(field.getName()));
                    } else {
                        field.setAccessible(true);
                        map.put(field.getName(), field.get(obj));
                        field.setAccessible(false);
                    }
                }
            } catch (IllegalAccessException exception) {
                throw new RuntimeException();
            }
        }

        return map;
    }

    /**
     * 获取obj对象fieldName的Field
     *
     * @param obj       obj
     * @param fieldName fieldName
     * @return field
     */
    public static Field getFieldByFieldName(Object obj, String fieldName) {
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
            }
        }
        return null;
    }

    /**
     * 获取obj对象fieldName的属性值
     *
     * @param obj       obj
     * @param fieldName fieldName
     * @return object
     * @throws SecurityException        exception
     * @throws NoSuchFieldException     exception
     * @throws IllegalArgumentException exception
     * @throws IllegalAccessException   exception
     */
    public static Object getValueByFieldName(Object obj, String fieldName) {
        Field field = getFieldByFieldName(obj, fieldName);
        Object value = null;
        try {
            if (field != null) {
                if (field.isAccessible()) {
                    value = field.get(obj);
                } else {
                    field.setAccessible(true);
                    value = field.get(obj);
                    field.setAccessible(false);
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return value;
    }

    /**
     * 设置obj对象fieldName的属性值
     *
     * @param obj       obj
     * @param fieldName fieldName
     * @param value     value
     * @throws SecurityException        exception
     * @throws NoSuchFieldException     exception
     * @throws IllegalArgumentException exception
     * @throws IllegalAccessException   exception
     */
    public static void setValueByFieldName(Object obj, String fieldName,
                                           Object value) {
        Field field = getFieldByFieldName(obj, fieldName);
        if (field == null) {
            return;
        }
        try {
            if (field.isAccessible()) {
                field.set(obj, value);
            } else {
                field.setAccessible(true);
                field.set(obj, value);
                field.setAccessible(false);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException();
        }
    }
}
