package com.example.administrador.myapplication.models.persistence;

import android.content.ContentValues;

import com.example.administrador.myapplication.models.entities.ServiceOrder;

public class ServiceOrderUser {

    public static final String TABLE = "service_order_user";
    public static final String ID = "id";
    public static final String USER = "user";
    public static final String PASSWORD = "password";

    public static final String[] COLUNS = {ID, USER, PASSWORD};

    public static String createTable() {
        final StringBuilder sql = new StringBuilder();
        sql.append(" CREATE TABLE ");
        sql.append(TABLE);
        sql.append(" ( ");
        sql.append(ID + " INTEGER PRIMARY KEY, ");
        sql.append(USER + " TEXT, ");
        sql.append(PASSWORD + " TEXT ");
        sql.append(" ); ");
        return sql.toString();
    }


    public static ContentValues getContentValues(ServiceOrder serviceOrder) {
        ContentValues content = new ContentValues();
        content.put(ID, serviceOrder.getId());
        content.put(USER, serviceOrder.getClient());
        content.put(PASSWORD, serviceOrder.getPhone());
        return content;
    }

}