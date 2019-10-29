package online.postboard.android.util.apputils;

import online.postboard.android.data.model.AnyItem;
import online.postboard.android.data.model.NotificationDTO;

import java.util.List;

/**
 * Created by ajayg on 9/29/2017.
 */

public class NotificationUtils {

    public static String getCount(List<? extends AnyItem> anyItemList) {
        int count = 0;
        for (AnyItem n : anyItemList) {
            if (n instanceof NotificationDTO) {
                if (!((NotificationDTO) n).isRead()) count++;
            }
        }
        if (count > 9) {
            return "9+";
        } else if (count <= 0) {
            return "";
        } else {
            return String.valueOf(count);
        }
    }

}
