package online.postboard.android.ui.signin;

import online.postboard.android.data.model.AuthResponseDTO;
import online.postboard.android.ui.base.MvpView;

/**
 * Created by Android SD-1 on 13-03-2017.
 */

public interface SignInMvpView extends MvpView {

    void showSignInSuccessful(AuthResponseDTO e);

    void facebookLogin();

    void googleLogin();

    void showAppLoginPage();

    void showAppRegisterPage();

    void loginFailed(String message);
}
