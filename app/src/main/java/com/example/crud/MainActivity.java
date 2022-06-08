package com.example.crud;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cursoradapter.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity {

    ListView userList;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Получаем объект и устанавливаем на него слушателя
        userList = findViewById(R.id.list); // Назначаем ListView
        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Если на Вьюху userList кликнут - она создаст интент для вызова UserActivity
                // и передаст в нем id строки, по которой было нажатие
                Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                intent.putExtra("id", id);

                // Выводим уведомление
                Toast toast = Toast.makeText(getApplicationContext(), "Выбираем...", Toast.LENGTH_SHORT);
                toast.show();
                startActivity(intent);
            }
        });

        // Создаем экземпляр класса DatabaseHelper
        databaseHelper = new DatabaseHelper(getApplicationContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        // Открываем подключение
        // Чтобы получить объект базы данных, надо использовать метод getReadableDatabase()
        // (получение базы данных для чтения) или getWritableDatabase() для записи.
        db = databaseHelper.getReadableDatabase();

        // Получаем данные из бд в виде курсора
        userCursor = db.rawQuery("select * from " + com.example.crud.DatabaseHelper.TABLE, null);

        // Определяем, какие столбцы из курсора будут выводиться в ListView
        String[] headers = new String[]{com.example.crud.DatabaseHelper.COLUMN_NAME, com.example.crud.DatabaseHelper.COLUMN_YEAR};

        // Создаем адаптер для вставки в Активити, передаем в него курсор
        // Дополнительно для управления курсором в Android имеется класс CursorAdapter.
        // Он позволяет адаптировать полученный с помощью курсора набор к отображению в списковых элементах наподобие ListView.
        // Как правило, при работе с курсором используется подкласс CursorAdapter - SimpleCursorAdapter.
        // Хотя можно использовать и другие адаптеры, типа ArrayAdapter.


        /**
         * Конструктор класса SimpleCursorAdapter принимает шесть параметров:
         * Первым параметром выступает контекст, с которым ассоциируется адаптер, например, текущая activity
         * Второй параметр - ресурс разметки интерфейса, который будет использоваться для отображения результатов выборки
         * Третий параметр - курсор
         * Четвертый параметр - список столбцов из выборки, которые будут отображаться в разметке интерфейса
         * Пятый параметр - элементы внутри ресурса разметки, которые будут отображать значения столбцов из четвертого параметра
         * Шестой параметр - флаги, задающие поведения адаптера */

        userAdapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item,
                userCursor, headers, new int[]{android.R.id.text1, android.R.id.text2}, 0);
        // Подключаем адаптер к ListView
        userList.setAdapter(userAdapter);
    }

    public void add(View view) {
        // При нажатии на кнопку запускается UserActivity, при этом не передается никакого id,
        // то есть в UserActivity id будет равен нулю, значит будет идти добавление данных:
        Intent intent = new Intent(this, UserActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // И также надо учитывать, что если мы используем курсор в SimpleCursorAdapter,
        // То мы не можем использовать метод close(), пока не завершим использование SimpleCursorAdapter.
        // Поэтому метод cursor более предпочтительно вызывать в методе onDestroy() фрагмента или activity.
        db.close();
        userCursor.close();
    }
}