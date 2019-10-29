package online.postboard.android.ui.signin;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.crashlytics.android.answers.SignUpEvent;

import online.postboard.android.data.DataManager;
import online.postboard.android.data.events.SocialLoginEvent;
import online.postboard.android.data.model.AuthResponseDTO;
import online.postboard.android.data.model.SocialAuthDTO;
import online.postboard.android.util.RxEventBus;
import online.postboard.android.util.RxUtil;
import online.postboard.android.util.apputils.ProfileUtils;
import online.postboard.android.injection.ConfigPersistent;
import online.postboard.android.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Android SD-1 on 13-03-2017.
 */

@ConfigPersistent
public class SignInPresenter extends BasePresenter<SignInMvpView> {

    private DataManager mDataManager;
    private RxEventBus eventBus;
    private Disposable signInSub;
    private Disposable socialSignInSub;

    @Inject
    public SignInPresenter(DataManager dataManager, RxEventBus eventBus) {
        mDataManager = dataManager;
        this.eventBus = eventBus;
    }

    @Override
    public void attachView(SignInMvpView mvpView) {
        super.attachView(mvpView);
        signInSub = eventBus.filteredObservable(AuthResponseDTO.class).subscribe(e -> {
//            ProfileUtils.loginActionAndMsgSub(eventBus,);
//                    mDataManager.getPreferencesHelper().saveToken(e.getToken());
//                    mDataManager.getPreferencesHelper().saveUser(e.getUserDTO());
                    getMvpView().showSignInSuccessful(e);
                }, throwable -> {

                }
        );

        socialSignInSub = eventBus.filteredObservable(SocialLoginEvent.class).subscribe(e -> {
                    if (e.getSocialLoginProvider().equals(SocialLoginEvent.SocialLoginProvider.FACEBOOK)) {
                        getMvpView().facebookLogin();
                    } else if (e.getSocialLoginProvider().equals(SocialLoginEvent.SocialLoginProvider.GOOGLE)) {
                        getMvpView().googleLogin();
                    } else if (e.getSocialLoginProvider().equals(SocialLoginEvent.SocialLoginProvider.APP_LOGIN_PAGE)) {
                        getMvpView().showAppLoginPage();
                    } else {
                        getMvpView().showAppRegisterPage();
                    }
                }, throwable -> {

                }
        );
    }

    @Override
    public void detachView() {
        super.detachView();
        RxUtil.dispose(signInSub);
        RxUtil.dispose(socialSignInSub);
    }

    public void logout() {
        Answers.getInstance().logCustom(new CustomEvent("Logout"));
        ProfileUtils.logoutAction(mDataManager, eventBus);
    }

    public void googleLogin(String authCode, String serverClientIdOrClientId, String redirectURI) {
        mDataManager.getPbService().googleLogin(new SocialAuthDTO(serverClientIdOrClientId, authCode, redirectURI))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(res -> {
                    Answers.getInstance().logSignUp(new SignUpEvent().putMethod("google").putSuccess(res.isSuccess()));
                    if (res.getData() != null && res.isSuccess()) {
                        AuthResponseDTO authResult = res.getData();
                        ProfileUtils.loginActionAndMsgSub(eventBus, mDataManager, authResult);
                    } else {
                        getMvpView().loginFailed(res.getMessage());
                    }
                }, e -> {
                    Timber.e(e, "There was an error while login");
                    getMvpView().loginFailed(e.getMessage());
                });
    }

    public void facebookLogin(String accessToken) {
        mDataManager.getPbService().facebookLogin(new SocialAuthDTO(accessToken))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(res -> {
                    Answers.getInstance().logSignUp(new SignUpEvent().putMethod("facebook").putSuccess(res.isSuccess()));
                    if (res.getData() != null && res.isSuccess()) {
                        AuthResponseDTO authResult = res.getData();
                        ProfileUtils.loginActionAndMsgSub(eventBus, mDataManager, authResult);
                    } else {
                        getMvpView().loginFailed(res.getMessage());
                    }
                }, e -> {
                    Timber.e(e, "There was an error while login");
                    getMvpView().loginFailed(e.getMessage());
                });
    }

}
