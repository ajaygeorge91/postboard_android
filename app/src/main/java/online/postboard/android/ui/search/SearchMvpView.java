package online.postboard.android.ui.search;

import java.util.List;

import online.postboard.android.data.events.ArticleActivityActionButtonClickEvent;
import online.postboard.android.data.events.CommentActionButtonClickEvent;
import online.postboard.android.data.events.LoadCommentsEvent;
import online.postboard.android.data.events.ShowArticleDetailEvent;
import online.postboard.android.data.events.ShowUserProfileEvent;
import online.postboard.android.data.events.SubCommentActionButtonClickEvent;
import online.postboard.android.data.model.ArticleDTO;
import online.postboard.android.data.model.CommentDTO;
import online.postboard.android.data.model.SubCommentDTO;
import online.postboard.android.data.model.UserArticleActivity;
import online.postboard.android.data.model.UserCommentActivity;
import online.postboard.android.data.model.UserDTO;
import online.postboard.android.ui.base.MvpView;

public interface SearchMvpView extends MvpView {


    void showArticles(List<ArticleDTO> articleDTOList);

    void showArticlesEmpty();

    void showError(String message);

    void updateArticle(String articleID, UserArticleActivity userArticleActivity);

    void articleAction(ArticleActivityActionButtonClickEvent articleActivityActionButtonClickEvent);

    void articleDetailAction(ShowArticleDetailEvent showArticleDetailEvent);

    void continueListLoading();

    void showUserProfile(ShowUserProfileEvent e);

    void clearArticles();
}
