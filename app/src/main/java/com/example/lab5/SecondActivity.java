package com.example.lab5;

import android.os.Bundle;
import android.widget.TextView;

public class SecondActivity extends ScreenActivity {

    @Override
    protected int layoutId() {
        return R.layout.activity_second;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String title = getIntent().getStringExtra(MainActivity.EXTRA_TITLE);
        TextView info = findViewById(R.id.intent_info);
        info.setText(getString(R.string.intent_info_format, title == null ? "—" : title));
    }
}
