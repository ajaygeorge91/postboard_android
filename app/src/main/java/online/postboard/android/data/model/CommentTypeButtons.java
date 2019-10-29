package online.postboard.android.data.model;

import online.postboard.android.data.events.LoadCommentsEvent;

/**
 * Created by Android SD-1 on 22-06-2017.
 */

public class CommentTypeButtons extends AnyItem {
    LoadCommentsEvent.CommentType commentType;

    public CommentTypeButtons(LoadCommentsEvent.CommentType commentType) {
        this.commentType = commentType;
    }

    public LoadCommentsEvent.CommentType getCommentType() {
        return commentType;
    }
}
