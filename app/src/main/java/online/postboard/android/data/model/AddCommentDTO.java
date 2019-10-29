package online.postboard.android.data.model;

/**
 * Created by ajayg on 6/29/2017.
 */

public class AddCommentDTO {
    private String content;
    private String articleID;

    public AddCommentDTO(String content, String articleID) {
        this.content = content;
        this.articleID = articleID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getArticleID() {
        return articleID;
    }

    public void setArticleID(String articleID) {
        this.articleID = articleID;
    }
}
