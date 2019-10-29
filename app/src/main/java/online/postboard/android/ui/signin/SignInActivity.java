package online.postboard.android.ui.signin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import online.postboard.android.R;
import online.postboard.android.data.model.AuthResponseDTO;
import online.postboard.android.ui.base.BaseActivity;
import online.postboard.android.util.DialogFactory;

import java.util.Arrays;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class SignInActivity extends BaseActivity implements SignInMvpView {

    public static final String LOGOUT = "Logout";
    private static final int RC_SIGN_IN = 1299;
    private boolean logout;

    @Inject
    SignInPresenter presenter;

    @Inject
    SignInPagerAdapter signInPagerAdapter;

    @BindView(R.id.tabs_signin)
    TabLayout tabs;

    @BindView(R.id.container_signin)
    ViewPager container;

    CallbackManager callbackManager;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);

        logout = getIntent().getBooleanExtra(LOGOUT, false);
        if (logout) {
            presenter.logout();
        }

        presenter.attachView(this);

        container.setAdapter(signInPagerAdapter);
        container.setOffscreenPageLimit(2);
        tabs.setupWithViewPager(container);
        container.setCurrentItem(0);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestServerAuthCode(getString(R.string.server_client_id))
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, connectionResult -> Timber.e(connectionResult.toString()))
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Timber.d(loginResult.getAccessToken().toString());
                presenter.facebookLogin(
                        loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {
                Timber.d("canceled");
            }

            @Override
            public void onError(FacebookException exception) {
                Timber.d(exception);
            }
        });

    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Timber.d("handleSignInResult:" + result.isSuccess());
        if (result.isSuccess() && result.getSignInAccount() != null && result.getSignInAccount().getServerAuthCode() != null) {
            presenter.googleLogin(
                    result.getSignInAccount().getServerAuthCode(),
                    getString(R.string.server_client_id),
                    getString(R.string.google_redirect_uri));
        } else {
            DialogFactory.INSTANCE.createGenericErrorDialog(this, "error");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void showSignInSuccessful(AuthResponseDTO e) {

        onBackPressed();
    }

    @Override
    public void facebookLogin() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
    }

    @Override
    public void showAppLoginPage() {
        container.setCurrentItem(0);
    }

    @Override
    public void showAppRegisterPage() {
        container.setCurrentItem(1);
    }

    @Override
    public void googleLogin() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public static Intent getCleanStartIntent(Context context) {
        Intent intent = new Intent(context, SignInActivity.class);
        intent.putExtra(LOGOUT, true);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Override
    public void loginFailed(String message) {
        DialogFactory.INSTANCE.createGenericErrorDialog(this, message);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
