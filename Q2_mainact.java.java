MainActivity.java
package com.example.studentteacherapp;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText edtTeacherName;
    Button btnSearch;
    ListView listView;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtTeacherName = findViewById(R.id.edtTeacherName);
        btnSearch = findViewById(R.id.btnSearch);
        listView = findViewById(R.id.listView);
        dbHelper = new DatabaseHelper(this);

        // Insert sample data (Remove in production)
        dbHelper.insertSampleData();

        btnSearch.setOnClickListener(view -> searchStudents());
    }

    private void searchStudents() {
        String teacherName = edtTeacherName.getText().toString().trim();
        if (teacherName.isEmpty()) {
            Toast.makeText(this, "Please enter a teacher's name", Toast.LENGTH_SHORT).show();
            return;
        }

        Cursor cursor = dbHelper.getStudentsByTeacher(teacherName);
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No students found for this teacher", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] from = {"s_name", "subject"};
        int[] to = {R.id.txtStudentName, R.id.txtSubject};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this, R.layout.list_item, cursor, from, to, 0);
        listView.setAdapter(adapter);
    }
}
