package za.co.cporm.model.map.types;

import android.content.ContentValues;
import android.database.Cursor;
import za.co.cporm.model.map.SqlColumnMapping;

/**
 * Created by hennie.brink on 2015-03-19.
 */
public class BooleanType implements SqlColumnMapping {
    @Override
    public Class<?> getJavaType() {
        return Boolean.class;
    }

    @Override
    public String getSqlColumnTypeName() {
        return "INTEGER";
    }

    @Override
    public Integer toSqlType(Object source) {
        return ((Boolean)source) ? 1 : 0;
    }

    @Override
    public Object getColumnValue(Cursor cursor, int columnIndex) {
        return cursor.getInt(columnIndex) ==  0 ? Boolean.FALSE : Boolean.TRUE;
    }

    @Override
    public void setColumnValue(ContentValues contentValues, String key, Object value) {
        contentValues.put(key, toSqlType(value));
    }
}
