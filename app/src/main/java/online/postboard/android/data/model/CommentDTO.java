package online.postboard.android.data.model;

import java.io.Serializable;

public class CommentDTO extends AnyItem implements Serializable {
    private String createdAt;
    private UserDTO createdBy;
    private UserCommentActivity userCommentActivity;
    private CommentMeta commentMeta;
    private String id;
    private String content;
    private PaginatedResult<SubCommentDTO> subCommentListP;
    private NodeLabel nodeLabel;

    public CommentDTO() {
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedBy(UserDTO createdBy) {
        this.createdBy = createdBy;
    }

    public UserDTO getCreatedBy() {
        return createdBy;
    }

    public void setUserCommentActivity(UserCommentActivity userCommentActivity) {
        this.userCommentActivity = userCommentActivity;
    }

    public UserCommentActivity getUserCommentActivity() {
        return userCommentActivity;
    }

    public void setCommentMeta(CommentMeta commentMeta) {
        this.commentMeta = commentMeta;
    }

    public CommentMeta getCommentMeta() {
        return commentMeta;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setSubCommentListP(PaginatedResult<SubCommentDTO> subCommentListP) {
        this.subCommentListP = subCommentListP;
    }

    public PaginatedResult<SubCommentDTO> getSubCommentListP() {
        return subCommentListP;
    }

    public NodeLabel getNodeLabel() {
        return nodeLabel;
    }

    public void setNodeLabel(NodeLabel nodeLabel) {
        this.nodeLabel = nodeLabel;
    }
}