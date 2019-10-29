package online.postboard.android.util.apputils;

import online.postboard.android.util.Constants;

/**
 * Created by Android SD-1 on 20-04-2017.
 */

public class ArticleUtils {

    public static int getHeight(int width, double heightByWidth) {
        double height = (width * heightByWidth) / 100;
        return (int) Math.round(height);
    }

    public static boolean shouldTruncate(String displayText) {
        if (displayText == null || displayText.isEmpty()) {
            return false;
        }
        if (displayText.split("\n").length > 2) {
            return true;
        }
        if (displayText.length() >= Constants.INSTANCE.getMAX_LENGTH_CONTENT_TEXT()) {
            return true;
        }
        return false;
    }

}
