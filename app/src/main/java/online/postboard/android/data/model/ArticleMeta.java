
package online.postboard.android.data.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class ArticleMeta implements Serializable {

    private long commentCount;
    private long ratingSum;

    public long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(long commentCount) {
        this.commentCount = commentCount;
    }

    public long getRatingSum() {
        return ratingSum;
    }

    public void setRatingSum(long ratingSum) {
        this.ratingSum = ratingSum;
    }

}