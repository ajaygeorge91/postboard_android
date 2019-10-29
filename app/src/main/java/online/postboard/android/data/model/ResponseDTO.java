package online.postboard.android.data.model;

public class ResponseDTO<Data> {
    private boolean success;
    private String message;
    private Data data;

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return
                "ResponseDTO{" +
                        "success = '" + success + '\'' +
                        "}";
    }
}
