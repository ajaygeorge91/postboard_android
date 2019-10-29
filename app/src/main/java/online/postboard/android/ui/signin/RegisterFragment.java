package online.postboard.android.ui.signin;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.SignUpEvent;

import online.postboard.android.data.model.AuthResponseDTO;
import online.postboard.android.ui.base.BaseFragment;
import online.postboard.android.R;
import online.postboard.android.util.DialogFactory;

public class RegisterFragment extends BaseFragment implements RegisterMvpView {

    @Inject
    RegisterPresenter registerPresenter;

    @BindView(R.id.signUpName)
    EditText signUpName;

    @BindView(R.id.signUpEmail)
    EditText signUpEmail;

    @BindView(R.id.signUpPassword)
    EditText signUpPassword;

    @BindView(R.id.signUpButton)
    Button signUpButton;

    @BindView(R.id.input_layout_full_name)
    TextInputLayout input_layout_full_name;

    @BindView(R.id.input_layout_password)
    TextInputLayout input_layout_password;

    @BindView(R.id.input_layout_email)
    TextInputLayout input_layout_email;


    public RegisterFragment() {
    }

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        registerPresenter.attachView(this);
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        registerPresenter.detachView();
    }

    @Override
    public void registerSuccessFul(AuthResponseDTO message) {
        signUpButton.setEnabled(true);
        Answers.getInstance().logSignUp(new SignUpEvent().putMethod("app").putSuccess(true));
    }

    @Override
    public void registerFailed(String message) {
        signUpButton.setEnabled(true);
        Answers.getInstance().logSignUp(new SignUpEvent().putMethod("app").putSuccess(false));
        DialogFactory.INSTANCE.createGenericErrorDialog(getActivity(), message);
    }

    @OnClick(R.id.loginFacebook)
    public void fbLoginButtonClick() {
        registerPresenter.loginFacebook();
    }

    @OnClick(R.id.loginGoogle)
    public void googleLoginButtonClick() {
        registerPresenter.loginGoogle();
    }

    @OnClick(R.id.login_tv)
    public void logInButtonClick() {
        registerPresenter.loginPageShow();
    }

    @OnClick(R.id.signUpButton)
    public void signUpButtonClick() {
        if (signUpPassword.getText().toString().isEmpty()) {
            input_layout_password.setError("Enter password");
            input_layout_password.requestFocus();
        }
        if (signUpEmail.getText().toString().isEmpty()) {
            input_layout_email.setError("Enter email");
            input_layout_email.requestFocus();
            return;
        }
        if (signUpName.getText().toString().isEmpty()) {
            input_layout_full_name.setError("Enter Fullname");
            input_layout_full_name.requestFocus();
            return;
        }
        input_layout_password.setErrorEnabled(false);
        input_layout_email.setErrorEnabled(false);
        input_layout_full_name.setErrorEnabled(false);

        // register
        signUpButton.setEnabled(false);
        registerPresenter.register(signUpName.getText().toString(), signUpEmail.getText().toString(), signUpPassword.getText().toString());
    }

}
