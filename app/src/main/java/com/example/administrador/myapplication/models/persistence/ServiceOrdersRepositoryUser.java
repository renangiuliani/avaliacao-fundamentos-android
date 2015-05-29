package com.example.administrador.myapplication.models.persistence;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrador.myapplication.models.entities.ServiceOrder;
import com.example.administrador.myapplication.util.AppUtil;

import java.util.LinkedHashMap;
import java.util.Map;

public final class ServiceOrdersRepositoryUser {

    private static Integer sSequence = 0;
    private static Map<Integer, ServiceOrder> sRepository = new LinkedHashMap<>();

    private static class Singleton {
        public static final ServiceOrdersRepositoryUser INSTANCE = new ServiceOrdersRepositoryUser();
    }

    private ServiceOrdersRepositoryUser() {
        super();
    }

    public static ServiceOrdersRepositoryUser getInstance() {
        return Singleton.INSTANCE;
    }

    public boolean login(String user, String password) {
        DatabaseHelper helper = new DatabaseHelper(AppUtil.CONTEXT);
        SQLiteDatabase db = helper.getReadableDatabase();
        String where = ServiceOrderUser.USER + " = ? AND " + ServiceOrderUser.PASSWORD + " = ? ";
        String[] parans = {user, password};
        Cursor cursor = db.query(ServiceOrderUser.TABLE, ServiceOrderUser.COLUNS, where, parans, null, null, null);
        int count = cursor.getCount();
        db.close();
        helper.close();
        return count > 0;
    }

}
