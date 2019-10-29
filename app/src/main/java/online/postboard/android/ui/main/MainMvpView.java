package online.postboard.android.ui.main;

import online.postboard.android.data.events.CreateNewArticleEvent;
import online.postboard.android.data.model.AuthResponseDTO;
import online.postboard.android.data.model.UserDTO;
import online.postboard.android.ui.base.MvpView;

public interface MainMvpView extends MvpView {

    void showError(String errorMessage);

    void newArticle(CreateNewArticleEvent createNewArticleEvent);

    void updateNotificationCount(String unreadCountEvent);

    void setUserDto(UserDTO userDTO);

    void clearUser();

    void showSignInSuccessful(AuthResponseDTO authResponseDTO);
}
