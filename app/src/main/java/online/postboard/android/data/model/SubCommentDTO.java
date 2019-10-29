package online.postboard.android.data.model;

import java.io.Serializable;

public class SubCommentDTO extends AnyItem implements Serializable {
    private UserSubCommentActivity userSubCommentActivity;
    private String createdAt;
    private UserDTO createdBy;
    private SubCommentMeta subCommentMeta;
    private String id;
    private String content;

    public void setUserSubCommentActivity(UserSubCommentActivity userSubCommentActivity) {
        this.userSubCommentActivity = userSubCommentActivity;
    }

    public UserSubCommentActivity getUserSubCommentActivity() {
        return userSubCommentActivity;
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

    public void setSubCommentMeta(SubCommentMeta subCommentMeta) {
        this.subCommentMeta = subCommentMeta;
    }

    public SubCommentMeta getSubCommentMeta() {
        return subCommentMeta;
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

    @Override
    public String toString() {
        return
                "Response{" +
                        "userSubCommentActivity = '" + userSubCommentActivity + '\'' +
                        ",createdAt = '" + createdAt + '\'' +
                        ",createdBy = '" + createdBy + '\'' +
                        ",subCommentMeta = '" + subCommentMeta + '\'' +
                        ",id = '" + id + '\'' +
                        ",content = '" + content + '\'' +
                        "}";
    }

}