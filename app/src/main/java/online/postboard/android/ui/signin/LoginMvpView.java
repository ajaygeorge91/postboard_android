package online.postboard.android.ui.signin;

import online.postboard.android.data.model.AuthResponseDTO;
import online.postboard.android.ui.base.MvpView;

public interface LoginMvpView extends MvpView {

    void loginSuccessFul(AuthResponseDTO message);

    void loginFailed(String message);

}
