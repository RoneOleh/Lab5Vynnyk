package com.example.lab5;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

/** Діалог виходу, що тричі ввічливо вмовляє залишитись. Користувач завжди може піти. */
final class ExitDialog {

    private ExitDialog() { }

    static void show(final Activity activity, final Runnable onExit) {
        View root = LayoutInflater.from(activity).inflate(R.layout.dialog_exit, null);
        final ImageView face = root.findViewById(R.id.exit_face);
        final TextView title = root.findViewById(R.id.exit_title);
        final TextView message = root.findViewById(R.id.exit_message);
        final Button stay = root.findViewById(R.id.exit_stay);
        final Button leave = root.findViewById(R.id.exit_leave);
        final LinearLayout dots = root.findViewById(R.id.exit_dots);

        final String[] titles = activity.getResources().getStringArray(R.array.exit_titles);
        final String[] messages = activity.getResources().getStringArray(R.array.exit_messages);
        final int[] faces = {R.drawable.ic_face_sad, R.drawable.ic_face_cry, R.drawable.ic_face_neutral};
        final int[] stage = {0};
        final int last = titles.length - 1;

        final int dotSize = Math.round(7 * activity.getResources().getDisplayMetrics().density);
        for (int i = 0; i <= last; i++) {
            View d = new View(activity);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dotSize, dotSize);
            lp.setMargins(dotSize / 2, 0, dotSize / 2, 0);
            dots.addView(d, lp);
        }

        final Runnable render = () -> {
            int s = stage[0];
            title.setText(titles[s]);
            message.setText(messages[s]);
            face.setImageResource(faces[s]);
            leave.setText(s == last ? R.string.exit_leave_final : R.string.exit_leave_soft);
            for (int i = 0; i <= last; i++) {
                dots.getChildAt(i).setBackgroundResource(i <= s ? R.drawable.dot_on : R.drawable.dot_off);
            }
            face.setScaleX(0.6f);
            face.setScaleY(0.6f);
            face.animate().scaleX(1f).scaleY(1f).setDuration(260)
                    .setInterpolator(new OvershootInterpolator(3f)).start();
        };
        render.run();

        final AlertDialog dialog = new AlertDialog.Builder(activity).setView(root).create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        stay.setOnClickListener(v -> {
            dialog.dismiss();
            Toast.makeText(activity, R.string.exit_stayed, Toast.LENGTH_SHORT).show();
        });
        leave.setOnClickListener(v -> {
            if (stage[0] < last) {
                stage[0]++;
                render.run();
            } else {
                dialog.dismiss();
                onExit.run();
            }
        });
        dialog.show();
    }
}
