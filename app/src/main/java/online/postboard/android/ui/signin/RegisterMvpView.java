package online.postboard.android.ui.signin;

import online.postboard.android.data.model.AuthResponseDTO;
import online.postboard.android.ui.base.MvpView;

public interface RegisterMvpView extends MvpView {

    void registerSuccessFul(AuthResponseDTO message);

    void registerFailed(String message);

}
