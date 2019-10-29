package online.postboard.android.data.events;

/**
 * Created by Android SD-1 on 20-03-2017.
 */

public class MqttSubscribeEvent {
    public String topic;

    public MqttSubscribeEvent(String topic) {
        this.topic = topic;
    }
}
