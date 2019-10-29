
package online.postboard.android.data.model;

import java.util.List;

public class UserActivityDTO extends AnyItem {

    private ArticleDTO article;
    private CommentDTO comment;
    private String nodeLabel;
    private List<UserActivityWithLabel> userActivityList;

    public ArticleDTO getArticle() {
        return article;
    }

    public void setArticle(ArticleDTO article) {
        article = article;
    }

    public CommentDTO getComment() {
        return comment;
    }

    public void setComment(CommentDTO comment) {
        comment = comment;
    }

    public String getNodeLabel() {
        return nodeLabel;
    }

    public void setNodeLabel(String nodeLabel) {
        nodeLabel = nodeLabel;
    }

    public List<UserActivityWithLabel> getUserActivityList() {
        return userActivityList;
    }

    public void setUserActivityList(List<UserActivityWithLabel> userActivityList) {
        userActivityList = userActivityList;
    }

}
