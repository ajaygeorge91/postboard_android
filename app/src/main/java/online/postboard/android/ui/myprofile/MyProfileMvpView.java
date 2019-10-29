package online.postboard.android.ui.myprofile;

import online.postboard.android.data.events.ShowArticleDetailEvent;
import online.postboard.android.data.model.ArticleDTO;
import online.postboard.android.data.model.PaginatedResult;
import online.postboard.android.data.model.UserArticleActivity;
import online.postboard.android.data.model.UserDTO;
import online.postboard.android.data.events.ArticleActivityActionButtonClickEvent;
import online.postboard.android.data.model.UserActivityDTO;
import online.postboard.android.ui.base.MvpView;

public interface MyProfileMvpView extends MvpView {
//
//    void showActivities(PaginatedResult<UserActivityDTO> articleDTOList);
//
//    void showArticles(PaginatedResult<ArticleDTO> articleDTOList);
//
//    void showEmpty();

//    void showError(String message);

    void setupUserDto(UserDTO userDTO);

    void signInView();

//    void newArticle(ArticleDTO articleDTO);

//    void articleAction(ArticleActivityActionButtonClickEvent articleActivityActionButtonClickEvent);

//    void articleDetailAction(ShowArticleDetailEvent showArticleDetailEvent);
//
//    void updateArticle(String articleID, UserArticleActivity userArticleActivity);
//
//    void continueListLoading();
}
