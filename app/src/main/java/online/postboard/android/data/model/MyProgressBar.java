package online.postboard.android.data.model;

import java.io.Serializable;

/**
 * Created by Android SD-1 on 21-04-2017.
 */

public class MyProgressBar extends AnyItem implements Serializable {
    private String message;
    private boolean showRetryButton = false;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        this.showRetryButton = false;
    }

    public MyProgressBar(String message) {
        this.message = message;
        this.showRetryButton = false;
    }

    public void setErrorMsg(String message) {
        this.message = message;
        this.showRetryButton = true;
    }

    public boolean isShowRetryButton() {
        return showRetryButton;
    }
}