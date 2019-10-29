package online.postboard.android.util;

import android.content.Context;
import android.os.Vibrator;

/**
 * Created by ajayg on 7/19/2017.
 */

public class TouchFeedbackUtils {

    private static final int VIBRATE_DURATION = 12;
    private static final int LONG_VIBRATE_DURATION = 18;

    public static void vibrate(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {
                0, VIBRATE_DURATION
        };
        vibrator.vibrate(pattern, -1);
    }

    public static void vibrateStrong(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {
                0, LONG_VIBRATE_DURATION
        };
        vibrator.vibrate(pattern, -1);
    }
}
