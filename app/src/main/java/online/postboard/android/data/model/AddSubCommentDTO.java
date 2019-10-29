package online.postboard.android.data.model;

/**
 * Created by ajayg on 6/29/2017.
 */

public class AddSubCommentDTO {
    private String content;
    private String commentID;

    public AddSubCommentDTO(String content, String commentID) {
        this.content = content;
        this.commentID = commentID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCommentID() {
        return commentID;
    }

    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }
}
