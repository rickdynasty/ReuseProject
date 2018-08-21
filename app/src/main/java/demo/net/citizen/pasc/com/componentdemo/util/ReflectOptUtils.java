package demo.net.citizen.pasc.com.componentdemo.util;

import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class ReflectOptUtils {

    private static final String TAG = "ReflectOptUtils";
    public static int gCallFieldNums = 0;
    public static int gCallMethodNums = 0;


    //private static HashMap<String, ReflectClassComponent> mReflectFileds = new HashMap<String, ReflectClassComponent>();
    private static ConcurrentHashMap<String, ReflectClassComponent> mReflectFileds = new ConcurrentHashMap<String, ReflectClassComponent>();

    static class ReflectClassComponent {
        //String clsName;
        HashMap<String, Field> fields;
        HashMap<String, Method> methods; //method_name + param
    }

    public ReflectOptUtils() {
        // TODO Auto-generated constructor stub
    }

    public static Field getDeclaredField(Class<?> cls, String fieldName) {
        ReflectClassComponent tmpRcc = mReflectFileds.get(cls.getName());
        if (tmpRcc == null) {
            tmpRcc = new ReflectClassComponent();
            tmpRcc.fields = new HashMap<String, Field>();
            tmpRcc.methods = new HashMap<String, Method>();
            mReflectFileds.put(cls.getName(), tmpRcc);
        }

        Field field = tmpRcc.fields.get(fieldName);
        if (field != null) {
            //Log.d(TAG, "getDeclaredField| field=" + field.getName());
            return field;
        }

        //Log.d(TAG, "getDeclaredField|cls.getName=" + cls.getName() + ", fieldName=" + fieldName + ", tmpRcc.fields.size =" + tmpRcc.fields.size());

        gCallFieldNums++;
        try {
            field = cls.getDeclaredField(fieldName);
            field.setAccessible(true);
            tmpRcc.fields.put(fieldName, field);
            return field;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.i(TAG, "getFieldValue|cls=" + cls + ", fieldName=" + fieldName);

        return null;
    }

    //find the method in current class or super class
    public static Field getFieldRecursive(Class<?> clazz, String fieldName) throws Exception {
        Field field = null;
        try {
            field = clazz.getDeclaredField(fieldName);
            return field;
        } catch (NoSuchFieldException e) {

            if (clazz.getSuperclass() == null) {
                return null;
            } else {
                field = getFieldRecursive(clazz.getSuperclass(), fieldName);
            }

        }

        return field;
    }

    public static Object getFieldValue(Field field, Object object) {
        try {
            return field.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.i(TAG, "getFieldValue|field=" + field);

        return null;
    }

    @Deprecated
    public static Object getFieldValue(String fieldName, Object object) {
        Field field = getDeclaredField(object.getClass(), fieldName);

        return getFieldValue(field, object);
    }

    public static Object getFieldValue(String fieldName, Object object, Class<?> clz) {
        Field field = getDeclaredField(clz, fieldName);

        return getFieldValue(field, object);
    }

    public static void setFieldValue(Object object, Field field, Object value) {
        try {
            field.set(object, value);
            return;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.i(TAG, "setFieldValue|field=" + field + ", value=" + value);
    }

    public static Method getDeclaredMethod(Class<?> clz, String methodName, Class<?>... parameterTypes) {

        ReflectClassComponent tmpRcc = mReflectFileds.get(clz.getName());
        if (tmpRcc == null) {
            tmpRcc = new ReflectClassComponent();
            tmpRcc.fields = new HashMap<String, Field>();
            tmpRcc.methods = new HashMap<String, Method>();
            mReflectFileds.put(clz.getName(), tmpRcc);
        }
        String methodKey = new String(methodName); //methodName+param
        int len = parameterTypes.length;
        for (int i = 0; i < len; i++) {
            methodKey = methodKey + "_" + parameterTypes[i].getSimpleName();
        }
        Method method = tmpRcc.methods.get(methodKey);
        if (method != null) {
            return method;
        }
        //Log.d(TAG, "getDeclaredMethod|clz=" + clz.getName() + ", methodKey=" + methodKey);

        gCallMethodNums++;

        try {
            method = clz.getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            tmpRcc.methods.put(methodKey, method);
            return method;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.i(TAG, "getDeclaredMethod|clz=" + clz + ", methodName=" + methodName);

        return null;
    }

    //find the method in current class or super class
    public static Method getMethodRecursive(Class<?> clz, String methodName, Class<?>... parameterTypes) throws Exception {
        //Log.d(PlugConstants.TAG, "getMethodRecursive clz = " + clz.getName() + ", method = " + methodName);
        Method method = null;
        try {
            method = clz.getDeclaredMethod(methodName, parameterTypes);
            return method;
        } catch (NoSuchMethodException e) {

            if (clz.getSuperclass() == null) {
                return null;
            } else {
                method = getMethodRecursive(clz.getSuperclass(), methodName, parameterTypes);
            }

        }

        return method;

    }


    public static Constructor<?> getDeclaredConstructor(Class<?> clz, Class<?>... parameterTypes) {
        gCallMethodNums++;
        try {
            Constructor<?> constructor = clz.getDeclaredConstructor(parameterTypes);
            constructor.setAccessible(true);
            return constructor;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.i(TAG, "getDeclaredConstructor|clz=" + clz);

        return null;
    }

    public static Object invoke(Method method, Object receiver, Object... args) {
        try {
            return method.invoke(receiver, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.i(TAG, "invoke|method=" + method);

        return null;
    }

    public static Class<?> forClassName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Log.i(TAG, "forClassName|className=" + className);

        return null;
    }
}
