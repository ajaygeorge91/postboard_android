package online.postboard.android.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import online.postboard.android.BoilerplateApplication;
import online.postboard.android.data.events.ProgressRetryButtonClickEvent;
import online.postboard.android.util.RxEventBus;

import javax.inject.Inject;

/**
 * Created by ajayg on 8/31/2017.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Inject
    RxEventBus eventBus;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        BoilerplateApplication.get(context).getComponent().inject(this);

        if (isNetworkAvailable(context)) {
            eventBus.post(new ProgressRetryButtonClickEvent());
        }

    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

}