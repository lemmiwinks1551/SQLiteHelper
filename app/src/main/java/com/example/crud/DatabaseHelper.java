package com.example.crud;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Для упрощения работы с базами данных SQLite в Android нередко применяется класс SQLiteOpenHelper.
    // Для использования необходимо создать класс-наследник от SQLiteOpenHelper

    // Название бд
    private static final String DATABASE_NAME = "userstore.db";

    // Версия базы данных
    private static final int SCHEMA = 1;

    // Название таблицы в бд
    static final String TABLE = "users";

    // Названия столбцов
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_YEAR = "year";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Вызывается при попытке доступа к базе данных, но когда еще эта база данных не создана

        db.execSQL("CREATE TABLE users (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME
                + " TEXT, " + COLUMN_YEAR + " INTEGER);");

        // Добавление первую строчку
        db.execSQL("INSERT INTO " + TABLE + " (" + COLUMN_NAME
                + ", " + COLUMN_YEAR + ") VALUES ('Том Смит', 1981);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Вызывается, когда необходимо обновление схемы базы данных.
        // Здесь можно пересоздать ранее созданную базу данных в onCreate(),
        // установив соответствующие правила преобразования от старой бд к новой
        if (oldVersion == 1) {
            //Код, выполняемый для версии базы данных 1
        }
        if (oldVersion < 3) {
            //Код, выполняемый для версии базы данных 1 или 2
        }
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Метод onDowngrade() предназначен для возврата базы данных к предыдущей версии.
    }
}
