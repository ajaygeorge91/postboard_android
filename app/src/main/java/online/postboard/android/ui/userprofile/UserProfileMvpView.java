package online.postboard.android.ui.userprofile;

import online.postboard.android.data.events.ShowArticleDetailEvent;
import online.postboard.android.data.model.PaginatedResult;
import online.postboard.android.data.model.UserArticleActivity;
import online.postboard.android.data.model.UserDTO;
import online.postboard.android.data.model.UserProfileBundleDTO;
import online.postboard.android.data.events.ArticleActivityActionButtonClickEvent;
import online.postboard.android.data.model.ArticleDTO;
import online.postboard.android.ui.base.MvpView;

public interface UserProfileMvpView extends MvpView {

    void setupUserDto(UserDTO userDTO);

    void setupUserProfileBundleDto(UserProfileBundleDTO userProfileBundleDTO);

    void showArticles(PaginatedResult<ArticleDTO> articleDTOList);

    void showError(String message);

    void showEmpty();

    void articleAction(ArticleActivityActionButtonClickEvent articleActivityActionButtonClickEvent);

    void articleDetailAction(ShowArticleDetailEvent showArticleDetailEvent);

    void updateArticle(String articleID, UserArticleActivity userArticleActivity);

}
