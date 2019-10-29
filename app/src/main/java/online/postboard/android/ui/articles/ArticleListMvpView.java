package online.postboard.android.ui.articles;

import java.util.List;

import online.postboard.android.data.events.ShowArticleDetailEvent;
import online.postboard.android.data.events.ShowUserProfileEvent;
import online.postboard.android.data.model.ArticleDTO;
import online.postboard.android.data.model.UserArticleActivity;
import online.postboard.android.data.events.ArticleActivityActionButtonClickEvent;
import online.postboard.android.ui.base.MvpView;

public interface ArticleListMvpView extends MvpView {

    void showArticles(List<ArticleDTO> articleDTOList);

    void showArticlesEmpty();

    void showError(String message);

    void updateArticle(String articleID, UserArticleActivity userArticleActivity);

    void articleAction(ArticleActivityActionButtonClickEvent articleActivityActionButtonClickEvent);

    void articleDetailAction(ShowArticleDetailEvent showArticleDetailEvent);

    void removeUserArticleActivity();

    void getUserArticleActivity();

    void continueListLoading();

    void updateArticleActivity(List<UserArticleActivity> userArticleActivities);

    void showUserProfile(ShowUserProfileEvent e);
}
