package online.postboard.android.ui.signin;

import javax.inject.Inject;

import online.postboard.android.data.DataManager;
import online.postboard.android.data.events.SocialLoginEvent;
import online.postboard.android.data.model.AuthResponseDTO;
import online.postboard.android.util.RxEventBus;
import online.postboard.android.util.apputils.ProfileUtils;
import online.postboard.android.data.model.AuthDTO;
import online.postboard.android.ui.base.BasePresenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Android SD-1 on 18-03-2017.
 */

public class RegisterPresenter extends BasePresenter<RegisterMvpView> {

    private DataManager mDataManager;
    private RxEventBus eventBus;
    private Disposable registerSub;

    @Inject
    public RegisterPresenter(DataManager dataManager, RxEventBus eventBus) {
        this.mDataManager = dataManager;
        this.eventBus = eventBus;
    }

    @Override
    public void attachView(RegisterMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    public void loginFacebook() {
        eventBus.post(new SocialLoginEvent(SocialLoginEvent.SocialLoginProvider.FACEBOOK));
    }

    public void loginGoogle() {
        eventBus.post(new SocialLoginEvent(SocialLoginEvent.SocialLoginProvider.GOOGLE));
    }

    public void loginPageShow() {
        eventBus.post(new SocialLoginEvent(SocialLoginEvent.SocialLoginProvider.APP_LOGIN_PAGE));
    }

    public void register(String fullName, String email, String password) {
        registerSub = mDataManager.getPbService().register(new AuthDTO(fullName, email, password))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(res -> {
                    if (res.getData() != null && res.isSuccess()) {
                        AuthResponseDTO authResult = res.getData();
                        ProfileUtils.loginActionAndMsgSub(eventBus, mDataManager, authResult);
                        getMvpView().registerSuccessFul(authResult);
                    } else {
                        getMvpView().registerFailed(res.getMessage());
                    }
                }, e -> {
                    Timber.e(e, "There was an error while registering");
                    getMvpView().registerFailed(e.getMessage());
                });
    }

}
