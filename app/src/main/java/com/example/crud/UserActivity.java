package com.example.crud;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class UserActivity extends AppCompatActivity {
    EditText nameBox;
    EditText yearBox;
    Button deleteButton;
    Button saveButton;

    DatabaseHelper databaseHelper;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;
    long userId = 0;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_user);

        // Назначаем переменные для Вьюх
        nameBox = findViewById(R.id.name);
        yearBox = findViewById(R.id.year);
        deleteButton = findViewById(R.id.deleteButton);
        saveButton = findViewById(R.id.saveButton);


        databaseHelper = new DatabaseHelper(this);
        sqLiteDatabase = databaseHelper.getWritableDatabase();

        Bundle extras = getIntent().getExtras(); // Получить доп инфу из интента
        if (extras != null) {
            userId = extras.getLong("id");
        }

        // Если 0, то добавление (была нажата кнопка add)
        if (userId > 0) {
            // Получаем элемент по id из БД
            cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE + " WHERE " +
                    DatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(userId)});
            cursor.moveToFirst();
            nameBox.setText(cursor.getString(1));
            yearBox.setText(String.valueOf(cursor.getString(2)));
            cursor.close();
        } else {
            // Скрываем кнопку удаления
            deleteButton.setVisibility(View.GONE);
        }
    }

    public void save(View view) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COLUMN_NAME, nameBox.getText().toString());
        contentValues.put(DatabaseHelper.COLUMN_YEAR, yearBox.getText().toString());

        if (userId > 0) {
            sqLiteDatabase.update(DatabaseHelper.TABLE, contentValues, DatabaseHelper.COLUMN_ID + "=" + userId, null);
        } else {
            sqLiteDatabase.insert(DatabaseHelper.TABLE, null, contentValues);
        }
        goHome();
    }

    public void delete(View view) {
        sqLiteDatabase.delete(DatabaseHelper.TABLE, "_id = ?", new String[]{String.valueOf(userId)});
        goHome();
    }

    private void goHome() {
        sqLiteDatabase.close();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
