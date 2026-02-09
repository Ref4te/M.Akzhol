package com.example.makzholvtipo_35;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // UI-элементы
    private TextView Result;        // Текст для вывода результата/подсказок
    private EditText InputNumbers;  // Поле ввода числа пользователем
    private Button GRNButton;       // Кнопка проверки числа
    private Button Reset;           // Кнопка сброса игры

    private int randomNumber;       // Загаданное случайное число
    private boolean isGameOver = false; // Флаг окончания игры
    private Random random = new Random(); // Генератор случайных чисел

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(
                    systemBars.left,
                    systemBars.top,
                    systemBars.right,
                    systemBars.bottom
            );
            return insets;
        });

        // Привязываем элементы интерфейса по ID из layout
        Result = findViewById(R.id.Result);
        InputNumbers = findViewById(R.id.InputNumber);
        GRNButton = findViewById(R.id.GRNButton);
        Reset = findViewById(R.id.Reset);

        // Запускаем новую игру при старте Activity
        resetGame();

        // Обработчик нажатия кнопки "Угадать"
        GRNButton.setOnClickListener(view -> {

            // Получаем введённое пользователем значение как строку
            String input = InputNumbers.getText().toString();

            // Если игра уже закончена начинаем новую
            if (isGameOver) {
                resetGame();
                InputNumbers.setText("");
                Toast.makeText(this, "Игра сброшена", Toast.LENGTH_SHORT).show();
            }

            // Проверка: если поле пустое просим ввести число
            if (input.isEmpty()) {
                Result.setText("Введите число!");
                return;
            }

            // Преобразуем строку в число
            int userGuess = Integer.parseInt(input);

            // Сравниваем с загаданным числом
            if (userGuess > randomNumber) {
                Result.setText("Меньше!");
            } else if (userGuess < randomNumber) {
                Result.setText("Больше!");
            } else {
                // Пользователь угадал
                Result.setText("Поздравляю! Вы угадали!");
                isGameOver = true;
            }
        });

        // Обработчик кнопки сброса
        Reset.setOnClickListener(view -> {
            resetGame();               // Генерируем новое число
            InputNumbers.setText("");  // Очищаем поле ввода
            Toast.makeText(this, "Игра сброшена", Toast.LENGTH_SHORT).show(); //Всплывающее окошко
        });
    }

    // Функция сброса игры и генерации нового числа
    private void resetGame() {
        // Генерируем случайное число от 1 до 100 включительно
        this.randomNumber = random.nextInt(100) + 1;

        // Выводим стартовое сообщение
        Result.setText("Угадайте число от 1 до 100");

        // Сбрасываем флаг окончания игры
        isGameOver = false;
    }
}