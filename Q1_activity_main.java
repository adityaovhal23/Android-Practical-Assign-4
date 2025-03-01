package com.example.companyapp;

import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper myDb;
    EditText etName, etAddress, etPhone;
    Button btnInsert, btnView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDb = new DatabaseHelper(this);

        etName = findViewById(R.id.etName);
        etAddress = findViewById(R.id.etAddress);
        etPhone = findViewById(R.id.etPhone);

        btnInsert = findViewById(R.id.btnInsert);
        btnView = findViewById(R.id.btnView);

        addData();
        display();
    }

    public void addData() {
        btnInsert.setOnClickListener(view -> {
            boolean isInserted = myDb.insertData(
                    etName.getText().toString(),
                    etAddress.getText().toString(),
                    etPhone.getText().toString()
            );

            if (isInserted) {
                Toast.makeText(MainActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
                etName.setText("");
                etAddress.setText("");
                etPhone.setText("");
            } else {
                Toast.makeText(MainActivity.this, "Insertion Failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void display() {
        btnView.setOnClickListener(view -> {
            Cursor res = myDb.getAllData();
            if (res.getCount() == 0) {
                Toast.makeText(MainActivity.this, "No Data Found", Toast.LENGTH_LONG).show();
                return;
            }

            StringBuilder buffer = new StringBuilder();
            while (res.moveToNext()) {
                buffer.append("ID: ").append(res.getString(0)).append("\n");
                buffer.append("Name: ").append(res.getString(1)).append("\n");
                buffer.append("Address: ").append(res.getString(2)).append("\n");
                buffer.append("Phone: ").append(res.getString(3)).append("\n\n");
            }

            Toast.makeText(MainActivity.this, buffer.toString(), Toast.LENGTH_LONG).show();
        });
    }
}

