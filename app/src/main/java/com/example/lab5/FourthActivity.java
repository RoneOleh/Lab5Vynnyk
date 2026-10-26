package com.example.lab5;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

public class FourthActivity extends ScreenActivity {

    private static final int TAPS_NEEDED = 5;
    private int taps = 0;

    @Override
    protected int layoutId() {
        return R.layout.activity_fourth;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViewById(R.id.version_text).setOnClickListener(this::onVersionTap);
    }

    private void onVersionTap(View v) {
        taps++;
        int left = TAPS_NEEDED - taps;
        if (left == 2 || left == 1) {
            Toast.makeText(this, getString(R.string.about_hint, left), Toast.LENGTH_SHORT).show();
        } else if (left <= 0) {
            taps = 0;
            new AlertDialog.Builder(this)
                    .setTitle(R.string.egg_title)
                    .setMessage(R.string.egg_message)
                    .setPositiveButton(R.string.egg_ok, null)
                    .show();
        }
    }
}
