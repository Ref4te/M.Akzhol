package com.example.makzholvtipo_35;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.TextView;п

import androidx.appcompat.app.AppCompatActivity;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MainActivity extends AppCompatActivity {

    private TextView display;

    private Button btnAC, btnDel, btnDiv, btnMul, btnSub, btnAdd, btnEq, btnSign;
    private Button btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btnDot;

    private String input = "0";
    private boolean typing = false;        // сейчас вводим число
    private boolean justEvaluated = false; // только что нажали "="

    private BigDecimal acc = null;         // аккумулятор
    private Op pending = null;             // ожидаемая операция

    // для повтора "="
    private BigDecimal lastRight = null;
    private Op lastOp = null;

    private enum Op { ADD, SUB, MUL, DIV }

    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = findViewById(R.id.edt);

        btnAC = findViewById(R.id.btnC);
        btnDel = findViewById(R.id.btnDel);
        btnDiv = findViewById(R.id.btnDiv);
        btnMul = findViewById(R.id.btnMul);
        btnSub = findViewById(R.id.btnSub);
        btnAdd = findViewById(R.id.btnAdd);
        btnEq  = findViewById(R.id.btnEq);
        btnSign = findViewById(R.id.btnSign);

        btn0 = findViewById(R.id.btn0);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);
        btn7 = findViewById(R.id.btn7);
        btn8 = findViewById(R.id.btn8);
        btn9 = findViewById(R.id.btn9);
        btnDot = findViewById(R.id.btnDot);

        // цифры
        btn0.setOnClickListener(v -> pressDigit("0"));
        btn1.setOnClickListener(v -> pressDigit("1"));
        btn2.setOnClickListener(v -> pressDigit("2"));
        btn3.setOnClickListener(v -> pressDigit("3"));
        btn4.setOnClickListener(v -> pressDigit("4"));
        btn5.setOnClickListener(v -> pressDigit("5"));
        btn6.setOnClickListener(v -> pressDigit("6"));
        btn7.setOnClickListener(v -> pressDigit("7"));
        btn8.setOnClickListener(v -> pressDigit("8"));
        btn9.setOnClickListener(v -> pressDigit("9"));

        btnDot.setOnClickListener(v -> pressDot());

        // функции
        btnAC.setOnClickListener(v -> pressAC());
        btnDel.setOnClickListener(v -> backspace());
        btnSign.setOnClickListener(v -> pressSign());

        // операции
        btnAdd.setOnClickListener(v -> pressOp(Op.ADD));
        btnSub.setOnClickListener(v -> pressOp(Op.SUB));
        btnMul.setOnClickListener(v -> pressOp(Op.MUL));
        btnDiv.setOnClickListener(v -> pressOp(Op.DIV));

        // равно
        btnEq.setOnClickListener(v -> pressEq());

        // свайп влево по дисплею = backspace
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            private static final int SWIPE_THRESHOLD = 80;
            private static final int SWIPE_VELOCITY_THRESHOLD = 80;

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float diffX = e2.getX() - e1.getX();
                float diffY = e2.getY() - e1.getY();

                if (Math.abs(diffX) > Math.abs(diffY)
                        && Math.abs(diffX) > SWIPE_THRESHOLD
                        && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {

                    if (diffX < 0) { // влево
                        backspace();
                        return true;
                    }
                }
                return false;
            }
        });

        display.setOnTouchListener((v, ev) -> {
            gestureDetector.onTouchEvent(ev);
            return true;
        });

        render();
    }

    private void pressDigit(String d) {
        if (!typing || justEvaluated) {
            input = d;
            typing = true;
            justEvaluated = false;
        } else {
            if (input.equals("0")) input = d;
            else input += d;
        }
        render();
    }

    private void pressDot() {
        if (!typing || justEvaluated) {
            input = "0.";
            typing = true;
            justEvaluated = false;
        } else if (!input.contains(".")) {
            input += ".";
        }
        render();
    }

    private void pressAC() {
        // Полный сброс
        input = "0";
        typing = false;
        justEvaluated = false;

        acc = null;
        pending = null;
        lastRight = null;
        lastOp = null;

        render();
    }

    private void pressSign() {
        BigDecimal x = parseInput();
        x = x.negate();
        input = format(x);
        typing = true;
        justEvaluated = false;
        render();
    }

    private void pressOp(Op op) {
        BigDecimal x = parseInput();

        if (acc == null) {
            acc = x;
        } else if (pending != null && typing) {
            // цепочка операций
            acc = apply(acc, pending, x);
            input = format(acc);
        } else {
            // если меняем операцию без ввода второго числа — acc не меняем
        }

        pending = op;
        typing = false;
        justEvaluated = false;

        render();
    }

    private void pressEq() {
        BigDecimal x = parseInput();

        if (pending != null) {
            if (acc == null) acc = BigDecimal.ZERO;

            // правый операнд: если только что вводили — берём x, иначе повторяем последний
            BigDecimal right = typing ? x : (lastRight != null ? lastRight : x);

            acc = apply(acc, pending, right);

            // запоминаем для повторного "="
            lastRight = right;
            lastOp = pending;

            pending = null;
            typing = false;
            justEvaluated = true;

            input = format(acc);
        } else if (lastOp != null && lastRight != null) {
            // повтор "="
            if (acc == null) acc = x;
            acc = apply(acc, lastOp, lastRight);
            input = format(acc);
            typing = false;
            justEvaluated = true;
        }

        render();
    }

    private void backspace() {
        if (!typing || justEvaluated) return;

        if (input.length() <= 1 || (input.length() == 2 && input.startsWith("-"))) {
            input = "0";
            typing = false;
        } else {
            input = input.substring(0, input.length() - 1);
            if (input.endsWith(".")) input = input.substring(0, input.length() - 1);
        }
        render();
    }

    private BigDecimal parseInput() {
        try {
            return new BigDecimal(input);
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    private BigDecimal apply(BigDecimal a, Op op, BigDecimal b) {
        switch (op) {
            case ADD:
                return a.add(b);
            case SUB:
                return a.subtract(b);
            case MUL:
                return a.multiply(b);
            case DIV:
                if (b.compareTo(BigDecimal.ZERO) == 0) {
                    // можно сделать "Error", но для лабы проще 0
                    return BigDecimal.ZERO;
                }
                return a.divide(b, 12, RoundingMode.HALF_UP);
        }
        return a;
    }

    private String format(BigDecimal x) {
        x = x.stripTrailingZeros();
        String s = x.toPlainString();
        if (s.equals("-0")) s = "0";

        // Простое ограничение длины на дисплее
        if (s.length() > 14) {
            try {
                BigDecimal rounded = x.round(new java.math.MathContext(10, RoundingMode.HALF_UP));
                s = rounded.toEngineeringString();
            } catch (Exception ignored) {}
        }
        return s;
    }

    private void render() {
        display.setText(input);
    }
}