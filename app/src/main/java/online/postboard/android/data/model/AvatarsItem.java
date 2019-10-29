package online.postboard.android.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;


public class AvatarsItem implements Serializable {
    private String provider;
    private String url;

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getProvider() {
        return provider;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return
                "AvatarsItem{" +
                        "provider = '" + provider + '\'' +
                        ",url = '" + url + '\'' +
                        "}";
    }

}