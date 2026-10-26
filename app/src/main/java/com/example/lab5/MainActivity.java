package com.example.lab5;

import android.animation.ValueAnimator;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends BaseActivity {

    /** Ключ даних, що передаються між Activity через Intent. */
    public static final String EXTRA_TITLE = "title";
    private static final int REQUEST_VOICE_INPUT = 501;

    private ValueAnimator micPulse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        updateThemeToggle();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startMicPulse();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (micPulse != null) {
            micPulse.cancel();
        }
    }

    // ---------- Навігація ----------

    public void openSecond(View v) {
        startNewActivity(SecondActivity.class, getString(R.string.second_title));
    }

    public void openThird(View v) {
        startNewActivity(ThirdActivity.class, getString(R.string.third_title));
    }

    public void openFourth(View v) {
        startNewActivity(FourthActivity.class, getString(R.string.fourth_title));
    }

    private void startNewActivity(Class<?> target, String title) {
        Intent intent = new Intent(this, target);
        intent.putExtra(EXTRA_TITLE, title);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    // ---------- Вихід ----------

    public void exitApp(View v) {
        ExitDialog.show(this, this::finishAffinity);
    }

    // ---------- Тема ----------

    public void toggleTheme(View v) {
        setTheme(!isDark());
    }

    private void setTheme(final boolean dark) {
        if (dark == isDark()) {
            return;
        }
        Toast.makeText(this, dark ? R.string.theme_to_dark : R.string.theme_to_light,
                Toast.LENGTH_SHORT).show();

        View target = findViewById(dark ? R.id.icon_moon : R.id.icon_sun);
        target.animate().rotation(360f).scaleX(1.35f).scaleY(1.35f).setDuration(300)
                .withEndAction(() -> {
                    saveDark(dark);
                    AppCompatDelegate.setDefaultNightMode(dark
                            ? AppCompatDelegate.MODE_NIGHT_YES
                            : AppCompatDelegate.MODE_NIGHT_NO);
                }).start();
    }

    private void updateThemeToggle() {
        boolean dark = isDark();
        styleSegment((ImageView) findViewById(R.id.icon_sun), !dark);
        styleSegment((ImageView) findViewById(R.id.icon_moon), dark);
    }

    private void styleSegment(ImageView icon, boolean selected) {
        icon.setBackgroundResource(selected ? R.drawable.bg_seg_selected : 0);
        icon.setColorFilter(ContextCompat.getColor(this,
                selected ? R.color.on_accent : R.color.text_secondary), PorterDuff.Mode.SRC_IN);
    }

    // ---------- Голос ----------

    private void startMicPulse() {
        final View icon = findViewById(R.id.voice_icon);
        micPulse = ValueAnimator.ofFloat(1f, 1.18f);
        micPulse.setDuration(900);
        micPulse.setRepeatMode(ValueAnimator.REVERSE);
        micPulse.setRepeatCount(ValueAnimator.INFINITE);
        micPulse.setInterpolator(new AccelerateDecelerateInterpolator());
        micPulse.addUpdateListener(a -> {
            float s = (float) a.getAnimatedValue();
            icon.setScaleX(s);
            icon.setScaleY(s);
        });
        micPulse.start();
    }

    public void startVoiceInput(View v) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "uk-UA");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "uk-UA");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.voice_prompt));
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);

        try {
            startActivityForResult(intent, REQUEST_VOICE_INPUT);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, R.string.voice_unavailable, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != REQUEST_VOICE_INPUT || resultCode != RESULT_OK || data == null) {
            return;
        }

        ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        if (results == null || results.isEmpty()) {
            Toast.makeText(this, R.string.voice_not_recognized, Toast.LENGTH_SHORT).show();
            return;
        }

        String command = results.get(0).toLowerCase(new Locale("uk", "UA"));
        if (command.contains("темн") || command.contains("нічн")) {
            setTheme(true);
        } else if (command.contains("світл") || command.contains("денн")) {
            setTheme(false);
        } else if (command.contains("навігац") || command.contains("перш") || command.contains("один")) {
            openSecond(null);
        } else if (command.contains("інтерактив") || command.contains("друг") || command.contains("два")) {
            openThird(null);
        } else if (command.contains("про застосунок") || command.contains("інформац")
                || command.contains("трет") || command.contains("три")
                || command.contains("четверт") || command.contains("чотири")) {
            openFourth(null);
        } else if (command.contains("вихід") || command.contains("вийти") || command.contains("закрити")) {
            exitApp(null);
        } else {
            Toast.makeText(this, getString(R.string.voice_unknown_command, results.get(0)),
                    Toast.LENGTH_LONG).show();
        }
    }
}
