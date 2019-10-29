package online.postboard.android.data;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import javax.inject.Inject;

import online.postboard.android.BoilerplateApplication;
import online.postboard.android.data.events.MqttSubscribeEvent;
import online.postboard.android.util.NetworkUtil;
import online.postboard.android.util.RxEventBus;
import online.postboard.android.util.RxUtil;
import online.postboard.android.util.apputils.ProfileUtils;
import online.postboard.android.data.events.MqttUnSubscribeEvent;

import online.postboard.android.util.Constants;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MyMQTTService extends Service {

    private static final String TAG = "MyMQTTService";
    public static final String PUBLIC_TOPIC = "test";
    public static final String NOTIFICATION_TOPIC_KEY = "notification_topic_key";
    public static final int MQTT_CONNECTION_TIMEOUT = 1000;
    public static final int MQTT_SUBSCRIPTION_TIMEOUT = 5000;
    private ConnectivityManager mConnMan;
    private volatile IMqttAsyncClient mqttClient;
    private String deviceId;
    private Disposable topicSub;
    private Disposable topicUnSub;

    @Inject
    RxEventBus rxEventBus;

    @Inject
    DataManager dataManager;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, MyMQTTService.class);
    }

    public MyMQTTService() {
    }

    class MQTTBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            IMqttToken token;
            boolean hasConnectivity = NetworkUtil.isNetworkConnected(MyMQTTService.this);
            Log.v(TAG, "hasConn: " + hasConnectivity + " - " + (mqttClient == null || !mqttClient.isConnected()));
            if (hasConnectivity && (mqttClient == null || !mqttClient.isConnected())) {
                doConnect();
            } else if (!hasConnectivity && mqttClient != null && mqttClient.isConnected()) {
                Log.d(TAG, "doDisconnect()");
                try {
                    token = mqttClient.disconnect();
                    token.waitForCompletion(1000);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class MQTTBinder extends Binder {
        public MyMQTTService getService() {
            return MyMQTTService.this;
        }
    }

    @Override
    public void onCreate() {
        BoilerplateApplication.get(this).getComponent().inject(this);

        IntentFilter intentf = new IntentFilter();
        setClientID();
        intentf.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(new MQTTBroadcastReceiver(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        mConnMan = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        topicSub = rxEventBus.filteredObservable(MqttSubscribeEvent.class).subscribe(new TopicSubscriber<>());
        topicUnSub = rxEventBus.filteredObservable(MqttUnSubscribeEvent.class).subscribe(new TopicSubscriber<>());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxUtil.dispose(topicSub);
        RxUtil.dispose(topicUnSub);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG, "onConfigurationChanged()");
        android.os.Debug.waitForDebugger();
        super.onConfigurationChanged(newConfig);
    }

    private void setClientID() {
        deviceId = MqttAsyncClient.generateClientId();
    }

    private void doConnect() {
        Log.d(TAG, "doConnect()");
        IMqttToken token;
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        try {
            mqttClient = new MqttAsyncClient(Constants.INSTANCE.getMQTT_ENDPOINT(), deviceId, new MemoryPersistence());
            token = mqttClient.connect();
            token.waitForCompletion(MQTT_CONNECTION_TIMEOUT);
            mqttClient.setCallback(new MqttEventCallback());
            token = mqttClient.subscribe(PUBLIC_TOPIC, 0);
            token.waitForCompletion(MQTT_SUBSCRIPTION_TIMEOUT);
        } catch (MqttSecurityException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            switch (e.getReasonCode()) {
                case MqttException.REASON_CODE_BROKER_UNAVAILABLE:
                case MqttException.REASON_CODE_CLIENT_TIMEOUT:
                case MqttException.REASON_CODE_CONNECTION_LOST:
                case MqttException.REASON_CODE_SERVER_CONNECT_ERROR:
                    Log.v(TAG, "c" + e.getMessage());
                    e.printStackTrace();
                    break;
                case MqttException.REASON_CODE_FAILED_AUTHENTICATION:
                    Intent i = new Intent("RAISEALLARM");
                    i.putExtra("ALLARM", e);
                    Log.e(TAG, "b" + e.getMessage());
                    break;
                default:
                    Log.e(TAG, "a" + e.getMessage());
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand()");
        return START_STICKY;
    }

    private class MqttEventCallback implements MqttCallback {

        @Override
        public void connectionLost(Throwable arg0) {
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken arg0) {
        }

        @Override
        @SuppressLint("NewApi")
        public void messageArrived(final String topic, final MqttMessage msg) throws Exception {
            Log.i(TAG, "Message arrived from topic " + topic);
            Handler h = new Handler(getMainLooper());
            h.post(() -> {
                ProfileUtils.mqttManageMessage(rxEventBus, dataManager, topic, msg);
//                Toast.makeText(getApplicationContext(), "MQTT Message:\n" + new String(msg.getPayload()), Toast.LENGTH_SHORT).show();
            });
        }
    }

    private class TopicSubscriber<T> implements Consumer<T> {

        @Override
        public void accept(@NonNull T t) throws Exception {
            if (t instanceof MqttSubscribeEvent) {
                if (mqttClient != null) try {
                    IMqttToken token = mqttClient.subscribe(((MqttSubscribeEvent) t).topic, 0);
                    token.waitForCompletion(MQTT_SUBSCRIPTION_TIMEOUT);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            } else if (t instanceof MqttUnSubscribeEvent) {
                if (mqttClient != null) try {
                    IMqttToken token = mqttClient.unsubscribe(((MqttUnSubscribeEvent) t).topic);
                    token.waitForCompletion(MQTT_SUBSCRIPTION_TIMEOUT);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind called");
        return null;
    }

}
