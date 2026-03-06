package com.example.makzholvtipo_35;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private final String[] items = {
            "activity 2",
            "activity 3",
            "activity 4"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.listView);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                items
        );
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Class<?> target;

            switch (position) {
                case 0: target = Activity2.class; break;
                case 1: target = Activity3.class; break;
                case 2: target = Activity4.class; break;
                default: target = MainActivity.class;break;
            }

            startActivity(new Intent(this, target));
        });
    }
}