package com.example.makzholvtipo_35;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView mainText;
    private ImageView clickImage;
    private Button btnPlus, btnMinus, btnReset;

    private long score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainText = findViewById(R.id.mainTxt);
        clickImage = findViewById(R.id.clickImage);
        btnPlus = findViewById(R.id.btnPlus);
        btnMinus = findViewById(R.id.btnMinus);
        btnReset = findViewById(R.id.btnReset);

        updateText();

        clickImage.setOnClickListener(v -> {
            score++;
            updateText();
        });

        btnPlus.setOnClickListener(v -> {
            score++;
            updateText();
        });

        btnMinus.setOnClickListener(v -> {
            //if (score > 0) score--;
            score--;
            updateText();
        });

        btnReset.setOnClickListener(v -> {
            score = 0;
            updateText();
        });
    }

    private void updateText() {
        mainText.setText("Кнопка нажата " + score + " " + razWord(score));
    }

    private String razWord(long n) {
        long a = Math.abs(n);
        long lastTwo = a % 100;
        long last = a % 10;

        if (lastTwo >= 11 && lastTwo <= 14) return "раз";
        if (last == 1) return "раз";
        if (last >= 2 && last <= 4) return "раза";
        return "раз";
    }
}