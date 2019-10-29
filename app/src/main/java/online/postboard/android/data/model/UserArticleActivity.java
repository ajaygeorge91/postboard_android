
package online.postboard.android.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;


public class UserArticleActivity implements Serializable {

    private String articleID;
    private long rating;

    public long getRating() {
        return rating;
    }

    public void setRating(Long rating) {
        rating = rating;
    }

    public UserArticleActivity(long rating) {
        this.rating = rating;
    }

    public String getArticleID() {
        return articleID;
    }

    public void setArticleID(String articleID) {
        this.articleID = articleID;
    }
}