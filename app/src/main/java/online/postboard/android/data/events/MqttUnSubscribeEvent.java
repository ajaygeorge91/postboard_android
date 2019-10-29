package online.postboard.android.data.events;

/**
 * Created by Android SD-1 on 20-03-2017.
 */

public class MqttUnSubscribeEvent {
    public String topic;

    public MqttUnSubscribeEvent(String topic) {
        this.topic = topic;
    }
}
