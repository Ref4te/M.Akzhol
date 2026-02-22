package com.example.makzholvtipo_35;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_a1) {
            open(MainActivity.class);
            return true;
        } else if (id == R.id.menu_a2) {
            open(Activity2.class);
            return true;
        } else if (id == R.id.menu_a3) {
            open(Activity3.class);
            return true;
        } else if (id == R.id.menu_a4) {
            open(Activity4.class);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void open(Class<?> cls) {
        // Если уже на этом экране - не открываем заново
        if (getClass().equals(cls)) return;

        Intent intent = new Intent(this, cls);

        // чтобы не плодить одинаковые Activity в стеке
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}