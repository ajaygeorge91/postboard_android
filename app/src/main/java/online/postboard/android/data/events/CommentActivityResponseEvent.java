package online.postboard.android.data.events;

import online.postboard.android.data.model.UserCommentActivity;

/**
 * Created by Android SD-1 on 16-06-2017.
 */

public class CommentActivityResponseEvent {

    private UserCommentActivity userCommentActivity;
    private String articleID;
    private String commentID;

    public CommentActivityResponseEvent(UserCommentActivity userCommentActivity, String articleID, String commentID) {
        this.userCommentActivity = userCommentActivity;
        this.articleID = articleID;
        this.commentID = commentID;
    }

    public UserCommentActivity getUserCommentActivity() {
        return userCommentActivity;
    }

    public String getArticleID() {
        return articleID;
    }

    public String getCommentID() {
        return commentID;
    }
}
