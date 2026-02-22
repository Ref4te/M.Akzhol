package com.example.makzholvtipo_35;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Activity2 extends BaseActivity {

    public static final String EXTRA_NAME = "extra_name";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        EditText etName = findViewById(R.id.textName);
        Button btnOpen = findViewById(R.id.btnActThree);
        btnOpen.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(this, "Введите имя", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(this, Activity3.class);
            intent.putExtra(EXTRA_NAME, name);   // передача данных
            startActivity(intent);
        });
    }
}