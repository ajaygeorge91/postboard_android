package online.postboard.android.data.events;

/**
 * Created by Android SD-1 on 16-06-2017.
 */

public class LoadCommentsEvent {

    private int adapterPosition;
    private CommentType commentType;

    public LoadCommentsEvent(int adapterPosition, CommentType commentType) {
        this.adapterPosition = adapterPosition;
        this.commentType = commentType;
    }

    public int getAdapterPosition() {
        return adapterPosition;
    }

    public CommentType getCommentType() {
        return commentType;
    }

    public enum CommentType {
        HOT,
        NEW
    }

}
