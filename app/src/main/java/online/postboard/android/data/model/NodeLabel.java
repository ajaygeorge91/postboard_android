package online.postboard.android.data.model;

import java.io.Serializable;

/**
 * Created by Android SD-1 on 21-04-2017.
 */

public class NodeLabel extends AnyItem implements Serializable {

    private String message;

    public NodeLabel(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}