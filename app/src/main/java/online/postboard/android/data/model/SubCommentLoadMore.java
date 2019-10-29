package online.postboard.android.data.model;

/**
 * Created by Android SD-1 on 21-04-2017.
 */

public class SubCommentLoadMore extends AnyItem {
    private String message;
    private String commentID;
    private CommentDTO comment;

    public SubCommentLoadMore(String commentID, String message, CommentDTO comment) {
        this.message = message;
        this.commentID = commentID;
        this.comment = comment;
    }

    public String getMessage() {
        return message;
    }

    public String getCommentID() {
        return commentID;
    }

    public CommentDTO getComment() {
        return comment;
    }

}