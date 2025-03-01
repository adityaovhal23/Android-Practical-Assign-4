package com.example.empdeptapp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper db;
    EditText etDeptName, etDeptLocation, etEmpName, etEmpAddress, etEmpPhone, etEmpSalary, etEmpDept, etDeleteDept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);

        etDeptName = findViewById(R.id.etDeptName);
        etDeptLocation = findViewById(R.id.etDeptLocation);
        etEmpName = findViewById(R.id.etEmpName);
        etEmpAddress = findViewById(R.id.etEmpAddress);
        etEmpPhone = findViewById(R.id.etEmpPhone);
        etEmpSalary = findViewById(R.id.etEmpSalary);
        etEmpDept = findViewById(R.id.etEmpDept);
        etDeleteDept = findViewById(R.id.etDeleteDept);

        findViewById(R.id.btnAddDept).setOnClickListener(v -> {
            db.insertDepartment(etDeptName.getText().toString(), etDeptLocation.getText().toString());
            Toast.makeText(this, "Department Added", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.btnDeleteEmp).setOnClickListener(v -> {
            db.deleteEmployeesByDept(etDeleteDept.getText().toString());
            Toast.makeText(this, "Employees Deleted", Toast.LENGTH_SHORT).show();
        });
    }
}



