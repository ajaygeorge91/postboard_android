package online.postboard.android.data.model;


import java.io.Serializable;

public class SubCommentMeta implements Serializable {
    private long ratingSum;

    public SubCommentMeta(int ratingSum) {
        this.ratingSum = ratingSum;
    }

    public void setRatingSum(long ratingSum) {
        this.ratingSum = ratingSum;
    }

    public long getRatingSum() {
        return ratingSum;
    }

    @Override
    public String toString() {
        return
                "SubCommentMeta{" +
                        "ratingSum = '" + ratingSum + '\'' +
                        "}";
    }

}
