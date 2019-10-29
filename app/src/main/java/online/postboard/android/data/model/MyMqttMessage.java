package online.postboard.android.data.model;

/**
 * Created by ajayg on 8/21/2017.
 */

public class MyMqttMessage<T> {

    private String topic;
    private String messageType;
    private T data;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
