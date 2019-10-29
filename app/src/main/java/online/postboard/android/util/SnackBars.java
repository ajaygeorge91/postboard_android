package online.postboard.android.util;

import android.support.design.widget.Snackbar;
import android.view.View;

import online.postboard.android.R;

/**
 * Created by Android SD-1 on 16-03-2017.
 */

public class SnackBars {

    public static void showDismissibleSandbar(View view, String message) {
        final Snackbar snackBar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackBar.setAction("Dismiss", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackBar.dismiss();
            }
        }).show();
    }

}
