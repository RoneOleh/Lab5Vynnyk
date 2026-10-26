package com.example.lab5;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/** Базовий клас внутрішніх екранів: заголовок з Intent і анімація повернення. */
public abstract class ScreenActivity extends BaseActivity {

    protected abstract int layoutId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId());

        String title = getIntent().getStringExtra(MainActivity.EXTRA_TITLE);
        TextView titleText = findViewById(R.id.title_text);
        if (title != null && titleText != null) {
            titleText.setText(title);
        }
    }

    public void goback(View v) {
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
