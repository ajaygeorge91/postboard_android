package online.postboard.android.data.events;

import online.postboard.android.data.model.UserSubCommentActivity;

/**
 * Created by Android SD-1 on 16-06-2017.
 */

public class SubCommentActivityResponseEvent {

    private UserSubCommentActivity userSubCommentActivity;
    private String articleID;
    private String commentID;

    public SubCommentActivityResponseEvent(UserSubCommentActivity userCommentActivity, String articleID, String commentID) {
        this.userSubCommentActivity = userSubCommentActivity;
        this.articleID = articleID;
        this.commentID = commentID;
    }

    public UserSubCommentActivity getUserSubCommentActivity() {
        return userSubCommentActivity;
    }

    public String getArticleID() {
        return articleID;
    }

    public String getCommentID() {
        return commentID;
    }
}
