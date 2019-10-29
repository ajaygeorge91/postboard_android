
package online.postboard.android.data.model;

import java.io.Serializable;


public class ArticleDTO extends AnyItem implements Serializable {

    private ArticleMeta articleMeta;
    private String createdAt;
    private UserDTO createdBy;
    private String id;
    private String title;
    private String content;
    private String link;
    private String articleType;
    private Image image;
    private UserArticleActivity userArticleActivity;
    private PaginatedResult<CommentDTO> comments;
    private CommentDTO linkedComment;

    public ArticleMeta getArticleMeta() {
        return articleMeta;
    }

    public void setArticleMeta(ArticleMeta articleMeta) {
        this.articleMeta = articleMeta;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public UserDTO getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserDTO createdBy) {
        this.createdBy = createdBy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UserArticleActivity getUserArticleActivity() {
        return userArticleActivity;
    }

    public void setUserArticleActivity(UserArticleActivity userArticleActivity) {
        this.userArticleActivity = userArticleActivity;
    }

    public PaginatedResult<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(PaginatedResult<CommentDTO> comments) {
        this.comments = comments;
    }

    public CommentDTO getLinkedComment() {
        return linkedComment;
    }

    public void setLinkedComment(CommentDTO linkedComment) {
        this.linkedComment = linkedComment;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getArticleType() {
        return articleType;
    }

    public void setArticleType(String articleType) {
        this.articleType = articleType;
    }
}
