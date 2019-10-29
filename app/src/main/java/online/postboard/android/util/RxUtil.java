package online.postboard.android.util;

import io.reactivex.disposables.Disposable;

public class RxUtil {

    public static void dispose(Disposable subscription) {
        if (subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
        }
    }

}
