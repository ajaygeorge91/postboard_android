package online.postboard.android.ui.articlenew;

import online.postboard.android.data.model.ArticleDTO;
import online.postboard.android.data.model.UserDTO;
import online.postboard.android.ui.base.MvpView;

public interface DetailMvpView extends MvpView {

    void setUser(UserDTO userDTO);

    void showError(String message);

    void articleCreated(ArticleDTO articleDTO);
}
