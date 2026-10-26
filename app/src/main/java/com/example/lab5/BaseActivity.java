package com.example.lab5;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

/** Спільна база: застосовує збережену тему до кожного екрана. */
public abstract class BaseActivity extends AppCompatActivity {

    protected static final String PREFS = "settings";
    protected static final String KEY_DARK = "dark_theme";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(isDark()
                ? AppCompatDelegate.MODE_NIGHT_YES
                : AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
    }

    protected boolean isDark() {
        return getSharedPreferences(PREFS, MODE_PRIVATE).getBoolean(KEY_DARK, false);
    }

    protected void saveDark(boolean dark) {
        SharedPreferences.Editor e = getSharedPreferences(PREFS, MODE_PRIVATE).edit();
        e.putBoolean(KEY_DARK, dark).apply();
    }
}
