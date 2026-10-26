package com.example.lab5;

import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

public class ThirdActivity extends ScreenActivity {

    private int count = 0;

    @Override
    protected int layoutId() {
        return R.layout.activity_third;
    }

    public void performAction(View v) {
        count++;
        TextView counter = findViewById(R.id.counter_text);
        TextView result = findViewById(R.id.action_result);
        counter.setText(String.valueOf(count));

        counter.setScaleX(0.8f);
        counter.setScaleY(0.8f);
        counter.animate().scaleX(1f).scaleY(1f).setDuration(220)
                .setInterpolator(new OvershootInterpolator(3f)).start();

        int msg;
        switch (count) {
            case 1:  msg = R.string.third_msg_1;  break;
            case 5:  msg = R.string.third_msg_5;  break;
            case 10: msg = R.string.third_msg_10; break;
            case 20: msg = R.string.third_msg_20; break;
            default: msg = count > 20 ? R.string.third_msg_other : -1;
        }
        if (msg != -1) {
            result.setText(msg);
        }

        if (count == 10) {
            ConfettiView confetti = findViewById(R.id.confetti);
            confetti.burst(v.getX() + v.getWidth() / 2f, v.getY() + v.getHeight() / 2f);
        }
        if (count == 20) {
            shake(v);
        }
    }

    private void shake(View v) {
        v.animate().translationX(-16f).setDuration(60).withEndAction(() ->
                v.animate().translationX(16f).setDuration(80).withEndAction(() ->
                        v.animate().translationX(0f).setDuration(60).start()).start()).start();
    }
}
