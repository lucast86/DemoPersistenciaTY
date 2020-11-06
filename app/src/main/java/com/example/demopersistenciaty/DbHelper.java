package com.example.demopersistenciaty;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    private final static String NAME_DB = "practica.sqlite";
    private final static int VERSION_DB = 1;

    private final String sqlCreate = "CREATE TABLE Empleados (idempleado INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "nombre TEXT NOT NULL, domicilio TEXT NOT NULL,telefono TEXT NOT NULL,email TEXT NOT NULL," +
            "password TEXT NOT NULL,habilitado INTEGER NOT NULL DEFAULT 1,favorito INTEGER NOT NULL DEFAULT 0);";

    public DbHelper(Context context) {
        super(context, NAME_DB, null, VERSION_DB);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //se elimina version anterior de tabla
        db.execSQL("DROP TABLE IF EXISTS Empleados");
        //se crea nueva version de tabla
        db.execSQL(sqlCreate);
    }
}

