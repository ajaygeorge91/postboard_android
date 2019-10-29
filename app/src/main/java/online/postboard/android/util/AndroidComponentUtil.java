package online.postboard.android.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

public final class AndroidComponentUtil {

    public static void toggleComponent(Context context, Class componentClass, boolean enable) {
        ComponentName componentName = new ComponentName(context, componentClass);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(componentName,
                enable ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED :
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    public static boolean isServiceRunning(Context context, Class serviceClass) {
        ActivityManager manager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static void setTransparentStatusBar(Activity context, Toolbar toolbar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window w = context.getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

            //set margin to toolbar
            if (toolbar.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.LayoutParams p = toolbar.getLayoutParams();
                int statusBarHeight = DimenUtils.getStatusBarHeight(context);
                p.height = p.height + statusBarHeight;
                toolbar.setPadding(toolbar.getPaddingLeft(), toolbar.getPaddingTop() + statusBarHeight, toolbar.getPaddingRight(), toolbar.getPaddingBottom());

            }
        }
    }


    public static void applyFontForCollapsingToolbarLayout(CollapsingToolbarLayout collapsingToolbarLayout, Activity context) {
        try {
            final Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/ProximaNovaBold.otf");
            collapsingToolbarLayout.setCollapsedTitleTypeface(tf);
            collapsingToolbarLayout.setExpandedTitleTypeface(tf);
            collapsingToolbarLayout.setExpandedTitleColor(context.getResources().getColor(android.R.color.white));
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

}
