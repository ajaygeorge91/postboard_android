package online.postboard.android.ui.signin;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.LoginEvent;

import online.postboard.android.data.model.AuthResponseDTO;
import online.postboard.android.ui.base.BaseFragment;
import online.postboard.android.R;

import online.postboard.android.util.DialogFactory;

public class LoginFragment extends BaseFragment implements LoginMvpView {

    @Inject
    LoginPresenter loginPresenter;

    @BindView(R.id.loginEmail)
    EditText loginEmail;

    @BindView(R.id.loginPassword)
    EditText loginPassword;

    @BindView(R.id.loginButton)
    Button loginButton;

    @BindView(R.id.loginFacebook)
    ImageView loginFacebook;

    @BindView(R.id.loginGoogle)
    ImageView loginGoogle;

    @BindView(R.id.input_layout_password)
    TextInputLayout input_layout_password;

    @BindView(R.id.input_layout_email)
    TextInputLayout input_layout_email;

    public LoginFragment() {
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        loginPresenter.attachView(this);
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        loginPresenter.detachView();
    }

    @OnClick(R.id.loginFacebook)
    public void fbLoginButtonClick() {
        loginPresenter.loginFacebook();
    }

    @OnClick(R.id.loginGoogle)
    public void googleLoginButtonClick() {
        loginPresenter.loginGoogle();
    }

    @OnClick(R.id.register_tv)
    public void appRegisterButtonClick() {
        loginPresenter.registerPageShow();
    }

    @OnClick(R.id.loginButton)
    public void loginButtonClick() {
        if (loginPassword.getText().toString().isEmpty()) {
            input_layout_password.setError("Enter password");
            input_layout_password.requestFocus();
        }
        if (loginEmail.getText().toString().isEmpty()) {
            input_layout_email.setError("Enter email");
            input_layout_email.requestFocus();
            return;
        }
        input_layout_password.setErrorEnabled(false);
        input_layout_email.setErrorEnabled(false);

        // login
        loginButton.setEnabled(false);
        loginPresenter.login(loginEmail.getText().toString(), loginPassword.getText().toString());
    }

    @Override
    public void loginSuccessFul(AuthResponseDTO message) {
        // do nothing
        loginButton.setEnabled(true);
        Answers.getInstance().logLogin(new LoginEvent().putMethod("app").putSuccess(true));
    }

    @Override
    public void loginFailed(String message) {
        loginButton.setEnabled(true);
        DialogFactory.INSTANCE.createGenericErrorDialog(getActivity(), "Login failed", message).show();
        Answers.getInstance().logLogin(new LoginEvent().putMethod("app").putSuccess(false));
    }

}
