package online.postboard.android.util.apputils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.reactivex.schedulers.Schedulers;
import online.postboard.android.data.DataManager;
import online.postboard.android.data.events.LoggedOutEvent;
import online.postboard.android.data.events.MqttSubscribeEvent;
import online.postboard.android.data.events.MqttUnSubscribeEvent;
import online.postboard.android.data.events.NotificationUnreadCountEvent;
import online.postboard.android.data.model.AuthResponseDTO;
import online.postboard.android.data.model.MyMqttMessage;
import online.postboard.android.data.model.NotificationDTO;
import online.postboard.android.data.model.UserDTO;
import online.postboard.android.util.Constants;
import online.postboard.android.util.RxEventBus;

/**
 * Created by ajayg on 7/31/2017.
 */

public class ProfileUtils {

    public static void logoutAction(DataManager dataManager, RxEventBus eventBus) {
        UserDTO userDTO = dataManager.getPreferencesHelper().getUser();
        if (userDTO != null) {
            eventBus.post(new MqttUnSubscribeEvent(ProfileUtils.getUserNotificationChannel(userDTO)));
        }
        dataManager.getPreferencesHelper().clear();
        dataManager.getDatabaseHelper().clearNotifications();
        eventBus.post(new LoggedOutEvent());
    }

    public static String getUserNotificationChannel(UserDTO userDTO) {
        if (userDTO != null) {
            if (userDTO.getNotificationChannel() != null && !userDTO.getNotificationChannel().isEmpty()) {
                return userDTO.getNotificationChannel();
            } else {
                return userDTO.getId() + "/notification";
            }
        } else {
            return "";
        }
    }

    public static void loginActionAndMsgSub(RxEventBus eventBus, DataManager mDataManager, AuthResponseDTO authResponseDTO) {
        if (authResponseDTO.getUserDTO() != null) {
            eventBus.post(new MqttSubscribeEvent(ProfileUtils.getUserNotificationChannel(authResponseDTO.getUserDTO())));
            mDataManager.getPreferencesHelper().saveUser(authResponseDTO.getUserDTO());
        }
        if (authResponseDTO.getToken() != null && !authResponseDTO.getToken().isEmpty()) {
            mDataManager.getPreferencesHelper().saveToken(authResponseDTO.getToken());
        }
        eventBus.post(authResponseDTO);
    }

    public static void mqttManageMessage(RxEventBus rxEventBus, DataManager dataManager, String topic, MqttMessage msg) {
        String msgString = new String(msg.getPayload());
        if (!msgString.isEmpty()) {
            try {
                JSONObject jsonObj = new JSONObject(msgString);
                String messageType = jsonObj.getString("messageType");
                if (messageType != null && messageType.equalsIgnoreCase("user_notification")) {
                    MyMqttMessage<NotificationDTO> notMsg = new Gson().fromJson(msgString, new TypeToken<MyMqttMessage<NotificationDTO>>() {
                    }.getType());

                    NotificationDTO n = notMsg.getData();
                    dataManager.getDatabaseHelper().setNotification(n)
                            .subscribeOn(Schedulers.io())
                            .subscribe(notificationDTO -> {
                                dataManager.getDatabaseHelper().getNotifications()
                                        .subscribeOn(Schedulers.io())
                                        .subscribe(nList -> {
                                            String stringCount = NotificationUtils.getCount(nList);
                                            rxEventBus.post(new NotificationUnreadCountEvent(stringCount));
                                        });
                            });

                } else if (messageType != null && messageType.equalsIgnoreCase("user_notification_list")) {
                    MyMqttMessage<List<NotificationDTO>> notListMsg = new Gson().fromJson(msgString, new TypeToken<MyMqttMessage<List<NotificationDTO>>>() {
                    }.getType());

                    List<NotificationDTO> notificationDTOList = notListMsg.getData();
                    dataManager.getDatabaseHelper().setNotifications(notificationDTOList)
                            .subscribeOn(Schedulers.io())
                            .subscribe(nList -> {
                                String stringCount = NotificationUtils.getCount(nList);
                                rxEventBus.post(new NotificationUnreadCountEvent(stringCount));
                            });
                } else {
                    rxEventBus.post(String.format("%s : %s", topic, new String(msg.getPayload())));
                }
            } catch (final JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public static String getUserAvatarUrl(UserDTO userDTO) {
        String avatarUrl = Constants.INSTANCE.getAVATAR_URL_ANONYMOUS();
        if (userDTO != null && userDTO.getAvatarURL() != null) {
            avatarUrl = userDTO.getAvatarURL();
        }
        return avatarUrl;
    }
}
