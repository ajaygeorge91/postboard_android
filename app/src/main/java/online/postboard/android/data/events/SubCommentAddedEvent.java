package online.postboard.android.data.events;

import online.postboard.android.data.model.SubCommentDTO;

/**
 * Created by Android SD-1 on 16-06-2017.
 */

public class SubCommentAddedEvent {

    private SubCommentDTO subCommentDTO;
    private String commentID;

    public SubCommentAddedEvent(String commentID, SubCommentDTO subCommentDTO) {
        this.subCommentDTO = subCommentDTO;
        this.commentID = commentID;
    }

    public SubCommentDTO getSubCommentDTO() {
        return subCommentDTO;
    }

    public void setSubCommentDTO(SubCommentDTO subCommentDTO) {
        this.subCommentDTO = subCommentDTO;
    }

    public String getCommentID() {
        return commentID;
    }

    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }
}
