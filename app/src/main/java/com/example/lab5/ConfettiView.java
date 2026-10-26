package com.example.lab5;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/** Легке конфеті без сторонніх бібліотек. Не перехоплює дотики. */
public class ConfettiView extends View {

    private static final int[] COLORS = {
            0xFF007C86, 0xFF37D5DE, 0xFF8DECF0, 0xFFF2B84B, 0xFFE0674F, 0xFFB98CE0
    };

    private static class Piece {
        float x, y, vx, vy, rot, vr, size;
        int color;
    }

    private final List<Piece> pieces = new ArrayList<>();
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Random rnd = new Random();
    private long last;

    public ConfettiView(Context c) { super(c); init(); }
    public ConfettiView(Context c, AttributeSet a) { super(c, a); init(); }
    public ConfettiView(Context c, AttributeSet a, int s) { super(c, a, s); init(); }

    private void init() {
        setClickable(false);
        setFocusable(false);
    }

    @Override
    public boolean onTouchEvent(android.view.MotionEvent e) {
        return false;
    }

    public void burst(float cx, float cy) {
        float density = getResources().getDisplayMetrics().density;
        for (int i = 0; i < 80; i++) {
            Piece p = new Piece();
            double angle = Math.toRadians(200 + rnd.nextInt(140));
            float speed = (260 + rnd.nextInt(520)) * density;
            p.x = cx;
            p.y = cy;
            p.vx = (float) Math.cos(angle) * speed;
            p.vy = (float) Math.sin(angle) * speed;
            p.rot = rnd.nextInt(360);
            p.vr = -360 + rnd.nextInt(720);
            p.size = (5 + rnd.nextInt(6)) * density;
            p.color = COLORS[rnd.nextInt(COLORS.length)];
            pieces.add(p);
        }
        last = System.nanoTime();
        postInvalidateOnAnimation();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        long now = System.nanoTime();
        float dt = Math.min((now - last) / 1_000_000_000f, 0.05f);
        last = now;
        float gravity = 1400f * getResources().getDisplayMetrics().density;

        Iterator<Piece> it = pieces.iterator();
        while (it.hasNext()) {
            Piece p = it.next();
            p.vy += gravity * dt;
            p.vx *= 0.99f;
            p.x += p.vx * dt;
            p.y += p.vy * dt;
            p.rot += p.vr * dt;
            if (p.y > getHeight() + p.size * 2) {
                it.remove();
                continue;
            }
            paint.setColor(p.color);
            canvas.save();
            canvas.rotate(p.rot, p.x, p.y);
            canvas.drawRect(p.x - p.size / 2, p.y - p.size / 4, p.x + p.size / 2, p.y + p.size / 4, paint);
            canvas.restore();
        }
        if (!pieces.isEmpty()) {
            postInvalidateOnAnimation();
        }
    }
}
