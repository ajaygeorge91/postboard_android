package online.postboard.android.ui.myprofile;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import online.postboard.android.data.DataManager;
import online.postboard.android.data.events.ArticleActivityResponseEvent;
import online.postboard.android.data.events.ArticleCreatedEvent;
import online.postboard.android.data.events.LogOutButtonClickEvent;
import online.postboard.android.data.events.ProgressRetryButtonClickEvent;
import online.postboard.android.data.events.ShowArticleDetailEvent;
import online.postboard.android.data.model.AuthResponseDTO;
import online.postboard.android.data.model.UserDTO;
import online.postboard.android.util.RxUtil;
import online.postboard.android.util.apputils.ProfileUtils;
import timber.log.Timber;

import online.postboard.android.data.events.ArticleActivityActionButtonClickEvent;
import online.postboard.android.data.events.LoggedOutEvent;
import online.postboard.android.ui.base.BasePresenter;
import online.postboard.android.util.RxEventBus;

/**
 * Created by Android SD-1 on 18-03-2017.
 */
public class ProfileListPresenter extends BasePresenter<MyProfileMvpView> {

    private DataManager mDataManager;
    private Disposable mMyArticlesSubscription;
    private Disposable signInListener;
    private Disposable loggedOutListener;
    private Disposable logOutButtonClickListener;
    private RxEventBus eventBus;

    @Inject
    public ProfileListPresenter(DataManager dataManager, RxEventBus eventBus) {
        this.mDataManager = dataManager;
        this.eventBus = eventBus;
    }

    // the action event shall not be subscribed from other activity(article detail)
    public void onPauseAction() {

    }

    public void onResumeAction() {

    }

    @Override
    public void attachView(MyProfileMvpView mvpView) {
        super.attachView(mvpView);
        signInListener = eventBus.filteredObservable(AuthResponseDTO.class).subscribe(e -> {
            getMvpView().setupUserDto(e.getUserDTO());
        });
        logOutButtonClickListener = eventBus.filteredObservable(LogOutButtonClickEvent.class).subscribe(e -> {
            ProfileUtils.logoutAction(mDataManager, eventBus);
        });
        loggedOutListener = eventBus.filteredObservable(LoggedOutEvent.class).subscribe(e -> {
            getMvpView().signInView();
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        RxUtil.dispose(mMyArticlesSubscription);
        RxUtil.dispose(signInListener);
        RxUtil.dispose(logOutButtonClickListener);
        RxUtil.dispose(loggedOutListener);
    }

//    public void getMyActivities(int pageNumber) {
//        checkViewAttached();
//        RxUtil.dispose(mMyArticlesSubscription);
//        mMyArticlesSubscription = mDataManager.getPbService().getMyActivities(pageNumber)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(responseDTO -> {
//                    if (responseDTO.isSuccess()) {
//                        if (responseDTO.getData().getResult().isEmpty()) {
//                            getMvpView().showEmpty();
//                        } else {
//                            getMvpView().showActivities(responseDTO.getData());
//                        }
//                    } else {
//                        getMvpView().showError(responseDTO.getMessage());
//                    }
//                }, e -> {
//                    Timber.e(e, "There was an error loading the ribots.");
//                    getMvpView().showError(e.getMessage());
//                });
//    }
//
//    public void getMyArticles(int pageNumber) {
//        checkViewAttached();
//        RxUtil.dispose(mMyArticlesSubscription);
//        mMyArticlesSubscription = mDataManager.getPbService().getMyArticles(pageNumber)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(responseDTO -> {
//                    if (responseDTO.isSuccess()) {
//                        if (responseDTO.getData().getResult().isEmpty()) {
//                            getMvpView().showEmpty();
//                        } else {
//                            getMvpView().showArticles(responseDTO.getData());
//                        }
//                    } else {
//                        getMvpView().showError(responseDTO.getMessage());
//                    }
//                }, e -> {
//                    Timber.e(e, "There was an error loading the ribots.");
//                    getMvpView().showError(e.getMessage());
//                });
//    }

    public UserDTO getUserDTO() {
        return mDataManager.getPreferencesHelper().getUser();
    }

    public void logout() {
        ProfileUtils.logoutAction(mDataManager, eventBus);
    }

}
