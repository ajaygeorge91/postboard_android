package online.postboard.android.ui.signin

import javax.inject.Inject

import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import online.postboard.android.data.DataManager
import online.postboard.android.data.events.SocialLoginEvent
import online.postboard.android.data.model.AuthDTO
import online.postboard.android.ui.base.BasePresenter
import online.postboard.android.util.RxEventBus
import online.postboard.android.util.RxUtil
import online.postboard.android.util.apputils.ProfileUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

/**
 * Created by Android SD-1 on 18-03-2017.
 */

class LoginPresenter @Inject
constructor(private val mDataManager: DataManager, private val eventBus: RxEventBus) : BasePresenter<LoginMvpView>() {


    private var loginSub: Disposable? = null;

    override fun attachView(mvpView: LoginMvpView) {
        super.attachView(mvpView)
    }

    override fun detachView() {
        super.detachView()
        RxUtil.dispose(loginSub)
    }

    fun loginFacebook() {
        eventBus.post(SocialLoginEvent(SocialLoginEvent.SocialLoginProvider.FACEBOOK))
    }

    fun loginGoogle() {
        eventBus.post(SocialLoginEvent(SocialLoginEvent.SocialLoginProvider.GOOGLE))
    }

    fun registerPageShow() {
        eventBus.post(SocialLoginEvent(SocialLoginEvent.SocialLoginProvider.APP_REGISTER_PAGE))
    }

    fun login(email: String, password: String) {
        loginSub = mDataManager.pbService.login(AuthDTO(email, password))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ res ->
                    if (res.data != null && res.isSuccess) {
                        val authResult = res.data
                        ProfileUtils.loginActionAndMsgSub(eventBus, mDataManager, authResult)
                        mvpView.loginSuccessFul(authResult)
                    } else {
                        mvpView.loginFailed(if (res.message == null) "Login failed" else res.message)
                    }
                }) { e ->
                    Timber.e(e, "There was an error while login")
                    mvpView.loginFailed(e.message)
                }

    }
}
