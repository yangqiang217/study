
package com.gxd.gxddb.orm;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * @说明 ORM映射转换工具
 * @注 本处理器只做了对基本类型的处理
 */
public class ORMUtil {

    private static ORMUtil sInstance = new ORMUtil();
    private Map<Class, Map<String, Field>> mORM_Table = new HashMap<Class, Map<String, Field>>();

    private ORMUtil() {
    }

    public static ORMUtil getInstance() {
        return sInstance;
    }

    public boolean ormUpdate(Class clazz, Object object, ContentValues value) {
        Set<Entry<String, Object>> set = value.valueSet();
        Map<String, Field> map = this.checkCotainORMTable(clazz);
        Field[] fields = clazz.getFields();
        boolean isUpdate = false;
        for (Entry<String, Object> entry : set) {
            String column = entry.getKey();
            Field field = map.get(column);
            if (field != null) {
                field.setAccessible(true);
                try {
                    if (!field.get(object).equals(entry.getValue())) {
                        field.set(object, entry.getValue());
                        isUpdate = true;
                    }
                } catch (Exception e) {
                }
            }
        }

        return isUpdate;
    }

    /**
     * 采用ORM映射必须保证注入ORM注解 {@link ORM} 保证注入的属性是public
     */
    public void ormInsert(Class clazz, Object object, ContentValues values) {
        Map<String, Field> map = this.checkCotainORMTable(clazz);
        for (Entry<String, Field> entry : map.entrySet()) {
            Field f = entry.getValue();
            f.setAccessible(true);
            ORM orm = f.getAnnotation(ORM.class);
            if (orm.isInsert()) {
                try {
                    values.put(entry.getKey(), f.get(object).toString());
                } catch (Exception e) {
                }
            }
        }

    }

    /**
     * 采用ORM映射必须保证注入ORM注解 {@link ORM} 保证注入的属性是public
     * 
     * @param clazz
     */
    public void ormQuery(Class clazz, Object object, Cursor cursor) {
        Map<String, Field> map = this.checkCotainORMTable(clazz);
        for (Entry<String, Field> entry : map.entrySet()) {
            Field f = entry.getValue();
            f.setAccessible(true);
            Object o = queryByColumn(entry.getKey(), cursor, entry.getValue().getType().getSimpleName());
            try {
                f.set(object, o);
            } catch (Exception e) {
            }
        }
    }

    public Map<String, Field> checkCotainORMTable(Class clazz) {
        Map<String, Field> map = null;
        if (!mORM_Table.containsKey(clazz)) {
            Field[] fields = clazz.getFields();
            map = new HashMap<String, Field>(fields.length);
            for (Field f : fields) {
                f.setAccessible(true);
                ORM orm = f.getAnnotation(ORM.class);
                if (orm != null) {
                    String columnName = orm.mappingColumn();
                    map.put(columnName, f);
                }
            }
            mORM_Table.put(clazz, map);
        } else {
            map = mORM_Table.get(clazz);
        }
        return map;
    }

    /**
     * @param type in{String,Int,Long,Double,Float}
     */
    private Object queryByColumn(String columnName, Cursor cursor, String type) {
        int index = cursor.getColumnIndex(columnName);
        Object o = null;
        if (type.equals("int")) {
            o = cursor.getInt(index);
        } else if (type.equals("long")) {
            o = cursor.getLong(index);
        } else if (type.equals("String")) {
            o = cursor.getString(index);
        } else if (type.equals("double")) {
            o = cursor.getDouble(index);
        } else if (type.equals("float")) {
            o = cursor.getFloat(index);
        } else if (type.equals("byte[]")) {
            o = cursor.getBlob(index);
        }
        return o;
    }

}
