package online.postboard.android.data.events;

import online.postboard.android.data.model.UserArticleActivity;

/**
 * Created by Android SD-1 on 16-06-2017.
 */

public class ArticleActivityResponseEvent {

    private UserArticleActivity userArticleActivity;
    private String articleID;

    public ArticleActivityResponseEvent(UserArticleActivity userArticleActivity, String articleID) {
        this.userArticleActivity = userArticleActivity;
        this.articleID = articleID;
    }

    public UserArticleActivity getUserArticleActivity() {
        return userArticleActivity;
    }

    public String getArticleID() {
        return articleID;
    }
}
