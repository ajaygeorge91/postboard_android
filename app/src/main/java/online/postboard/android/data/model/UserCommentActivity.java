package online.postboard.android.data.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class UserCommentActivity implements Serializable {

    private long rating;
    private String commentID;

    public UserCommentActivity(String commentID, long rating) {
        this.rating = rating;
        this.commentID = commentID;
    }

    public long getRating() {
        return rating;
    }

    public String getCommentID() {
        return commentID;
    }

}