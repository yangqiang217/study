package com.yq.imageviewer.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 反射相关辅助函数
 *
 * @author zewenwang
 */
public class ReflectUtils {

    /**
     * 实例化，考虑到私有构造函数和多参构造函数
     *
     * @param clazz
     * @param parameterTypes
     */
    public static <T> T newInstance(Class<T> clazz, Class<?>[] parameterTypes, Object[] paramValues)
        throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (parameterTypes == null || parameterTypes.length == 0) {
            try {
                return clazz.newInstance();
            } catch (Exception e) {
                // InstantiationException, IllegalAccessException
                // 利用反射去实例化
            }
        }
        Constructor<T> constructor = clazz.getDeclaredConstructor(parameterTypes);
        constructor.setAccessible(true);
        return constructor.newInstance(paramValues);
    }

    /**
     * 反射调用类成员函数
     *
     * @param instance
     * @param name
     * @param paramTypes
     * @param paramValues
     * @return
     * @throws NoSuchMethodException
     * @throws java.lang.reflect.InvocationTargetException
     * @throws IllegalAccessException
     */
    public static Object invokeMethod(Object instance, String name, Class<?>[] paramTypes,
        Object[] paramValues) throws NoSuchMethodException, InvocationTargetException,
        IllegalAccessException {
        Class<?> clazz = instance.getClass();
        Method method = getMethod(clazz, name, paramTypes);
        method.setAccessible(true);
        return method.invoke(instance, paramValues);
    }

    /**
     * 反射调用类静态成员函数
     *
     * @param clazz
     * @param name
     * @param paramTypes
     * @param paramValues
     * @return
     * @throws NoSuchMethodException
     * @throws java.lang.reflect.InvocationTargetException
     * @throws IllegalAccessException
     */
    public static Object invokeStaticMethod(Class<?> clazz, String name, Class<?>[] paramTypes,
        Object[] paramValues) throws NoSuchMethodException, InvocationTargetException,
        IllegalAccessException {
        Method method = getMethod(clazz, name, paramTypes);
        method.setAccessible(true);
        return method.invoke(clazz, paramValues);
    }

    /**
     * 设置类成员变量
     *
     * @param instance
     * @param name
     * @param value
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static void setFieldValue(Object instance, String name, Object value)
        throws NoSuchFieldException, IllegalAccessException {
        Class<?> clazz = instance.getClass();
        Field field = getField(clazz, name);
        field.setAccessible(true);
        field.set(instance, value);
    }

    /**
     * 设置类静态成员变量
     *
     * @param clazz
     * @param name
     * @param value
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static void setStaticFieldValue(Class<?> clazz, String name, Object value)
        throws NoSuchFieldException, IllegalAccessException {
        Field field = getField(clazz, name);
        field.setAccessible(true);
        field.set(clazz, value);
    }

    /**
     * 获取类属性值
     *
     * @param instance
     * @param name
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static Object getFieldValue(Object instance, String name) throws NoSuchFieldException,
        IllegalAccessException {
        Class<?> clazz = instance.getClass();
        Field field = getField(clazz, name);
        field.setAccessible(true);
        return field.get(instance);
    }

    /**
     * 获取类静态属性值
     *
     * @param clazz
     * @param name
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static Object getStaticFieldValue(Class<?> clazz, String name)
        throws NoSuchFieldException, IllegalAccessException {
        Field field = getField(clazz, name);
        field.setAccessible(true);
        return field.get(clazz);
    }

    /**
     * 获取类所有成员变量
     *
     * @param instance
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, Object> getFieldMap(Object instance) throws IllegalAccessException {
        return getFieldMapInternal(instance.getClass(), instance, false);
    }

    /**
     * 获取类所有静态变量
     *
     * @param clazz
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, Object> getStaticFieldMap(Class<?> clazz)
        throws IllegalAccessException {
        return getFieldMapInternal(clazz, null, true);
    }

    /**
     * 获取类所有成员变量，包含定义在父类的成员
     *
     * @param clazz
     * @return
     */
    public static List<Field> getFields(Class<?> clazz) {
        return getFields(clazz, true);
    }

    /**
     * 获取类所有非成员变量，包含定义在父类的成员
     *
     * @param clazz
     * @return
     */
    public static List<Field> getFields(Class<?> clazz, boolean includeStatic) {
        List<Field> fields = new ArrayList<Field>();
        for (Field field : clazz.getDeclaredFields()) {
            if (includeStatic || !Modifier.isStatic(field.getModifiers())) {
                fields.add(field);
            }
        }
        if ((clazz != Object.class) && (clazz.getSuperclass() != null)) {
            fields.addAll(getFields(clazz.getSuperclass(), includeStatic));
        }
        return fields;
    }

    /**
     * 获取类所有成员函数，包含定义在父类的成员
     *
     * @param clazz
     * @return
     */
    public static List<Method> getMethods(Class<?> clazz) {
        List<Method> methods = new ArrayList<Method>();
        for (Method method : clazz.getDeclaredMethods()) {
            methods.add(method);
        }
        if ((clazz != Object.class) && (clazz.getSuperclass() != null)) {
            methods.addAll(getMethods(clazz.getSuperclass()));
        }
        return methods;
    }

    /**
     * 判断一个类是否集成自另一个类
     *
     * @param child
     * @param parent
     * @return
     */
    public static boolean isSubClassOf(Class<?> child, Class<?> parent) {
        if (child == null) {
            return false;
        }
        if (child == parent) {
            return true;
        }
        if (child == Object.class) {
            return false;
        }
        return isSubClassOf(child.getSuperclass(), parent);
    }

    public static Method getMethod(Class<?> clazz, String name, Class<?>[] paramTypes)
        throws NoSuchMethodException {
        Method method;
        try {
            method = clazz.getDeclaredMethod(name, paramTypes);
        } catch (NoSuchMethodException e) {
            if (clazz.getSuperclass() != null) {
                method = getMethod(clazz.getSuperclass(), name, paramTypes);
            } else {
                throw e;
            }
        }
        if (method != null) {
            method.setAccessible(true);
        }
        return method;
    }

    private static Map<Class<?>, Class<?>> sPrimaryClassMapping;

    /**
     * 从Integer等class映射回到int.class等
     */
    public static Class<?> mappingBackToPrimaryClass(Class<?> inputClass) {
        if (sPrimaryClassMapping == null) {
            Map<Class<?>, Class<?>> mapping = new HashMap<>(10);
            mapping.put(Boolean.class, boolean.class);
            mapping.put(Byte.class, byte.class);
            mapping.put(Short.class, short.class);
            mapping.put(Character.class, char.class);

            mapping.put(Integer.class, int.class);
            mapping.put(Long.class, long.class);

            sPrimaryClassMapping = mapping;
        }

        Class<?> newClass = sPrimaryClassMapping.get(inputClass);
        if (newClass != null) {
            return newClass;
        } else {
            return inputClass;
        }
    }

    public static Field getField(Class<?> clazz, String name) throws NoSuchFieldException {
        Field field;
        try {
            field = clazz.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            if (clazz.getSuperclass() != null) {
                field = getField(clazz.getSuperclass(), name);
            } else {
                throw e;
            }
        }
        if (field != null) {
            field.setAccessible(true);
        }
        return field;
    }

    private static Map<String, Object> getFieldMapInternal(Class<?> clazz, Object instance,
        boolean isStatic) throws IllegalAccessException {
        List<Field> fields = getFields(clazz);
        Map<String, Object> result = new HashMap<>();
        for (Field field : fields) {
            field.setAccessible(true);
            if (isStatic && Modifier.isStatic(field.getModifiers())) {
                result.put(field.getName(), field.get(clazz));
            } else {
                if (instance == null) {
                    continue;
                }
                result.put(field.getName(), field.get(instance));
            }
        }
        return result;
    }
}
