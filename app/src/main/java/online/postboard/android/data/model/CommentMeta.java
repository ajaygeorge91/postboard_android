package online.postboard.android.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;


public class CommentMeta implements Serializable {
    private long ratingSum;

    public CommentMeta(int ratingSum) {
        this.ratingSum = ratingSum;
    }

    public void setRatingSum(long ratingSum) {
        this.ratingSum = ratingSum;
    }

    public long getRatingSum() {
        return ratingSum;
    }


}