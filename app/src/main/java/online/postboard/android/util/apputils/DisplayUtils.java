package online.postboard.android.util.apputils;

import android.app.Activity;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;

import online.postboard.android.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by ajayg on 10/5/2017.
 */

public class DisplayUtils {

    public static void setStatusBarDarkMode(Activity activity, boolean darkmode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.getWindow().getDecorView() != null) {
                int flags = activity.getWindow().getDecorView().getSystemUiVisibility();
                flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                activity.getWindow().getDecorView().setSystemUiVisibility(flags);

                if (darkmode) {
                    activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, R.color.status_bar_color));
                    activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                } else {
                    activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, R.color.primary_dark));
                    activity.getWindow().getDecorView().setSystemUiVisibility(0);
                }
            }
            Class<? extends Window> clazz = activity.getWindow().getClass();
            try {
                int darkModeFlag;
                Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
