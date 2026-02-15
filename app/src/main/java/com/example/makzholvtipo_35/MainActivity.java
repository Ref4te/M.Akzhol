package com.example.makzholvtipo_35;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView mainText;
    Button mainBtn;
    private long score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // находим элементы по id
        mainText = findViewById(R.id.mainTxt);
        mainBtn = findViewById(R.id.main_btn);

        // создаем обработчик нажатия
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                score++;

                String s = "Кликов: " + score;
                mainText.setText(s);
            }
        };

        // назначаем обработчик кнопке
        mainBtn.setOnClickListener(clickListener);
    }
}