package com.example.lab4_5;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    EditText searchEditText;
    Button GObutton;
    ImageView like;
    ImageView dislike;
    ImageView imgMain;
    int counter = 1;
    int[] images = {
            R.drawable.image1,
            R.drawable.image2,
            R.drawable.image3,
            R.drawable.image4,
            R.drawable.image5,
            R.drawable.image6,
            R.drawable.image7
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        searchEditText = findViewById(R.id.SearchEditText);
        GObutton = findViewById(R.id.GObutton);
        imgMain = findViewById(R.id.imgMain);
        like = findViewById(R.id.imgLike);
        dislike = findViewById(R.id.imgDislike);

        GObutton.setOnClickListener(v -> {
            String searchText = searchEditText.getText().toString()
                    .toLowerCase()
                    .trim();

            int resId = getResources().getIdentifier(
                    searchText,
                    "drawable",
                    getPackageName()
            );

            if (resId != 0) {
                imgMain.setImageResource(resId);
                Toast.makeText(MainActivity.this,
                        "Найдена картинка: " + searchText,
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this,
                        "Картинка не найдена",
                        Toast.LENGTH_SHORT).show();
            }
        });


        like.setOnClickListener(v -> {
            imgMain.setImageResource(images[counter]);
            counter++;
            if (counter == images.length) {
                counter = 0;
            }
            Toast.makeText(MainActivity.this, "Like", Toast.LENGTH_SHORT).show();
        });

        dislike.setOnClickListener(v -> {
            imgMain.setImageResource(images[counter]);
            counter++;
            if (counter == images.length) {
                counter = 0;
            }
            Toast.makeText(MainActivity.this, "Dislike", Toast.LENGTH_SHORT).show();
        });
    }
}