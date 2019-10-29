package online.postboard.android.ui.userprofile;

import online.postboard.android.data.DataManager;
import online.postboard.android.data.events.ArticleActivityResponseEvent;
import online.postboard.android.data.events.ShowArticleDetailEvent;
import online.postboard.android.data.model.UserDTO;
import online.postboard.android.util.RxEventBus;
import online.postboard.android.util.RxUtil;
import online.postboard.android.util.apputils.ProfileUtils;
import online.postboard.android.data.events.ArticleActivityActionButtonClickEvent;
import online.postboard.android.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Android SD-1 on 18-03-2017.
 */
public class UserProfilePresenter extends BasePresenter<UserProfileMvpView> {

    private DataManager mDataManager;
    private RxEventBus eventBus;
    private Disposable mUserSubscription;
    private Disposable mMyArticlesSubscription;
    private Disposable getArticleActionSub;
    private Disposable getArticleDetailSub;
    private Disposable getArticleActivityResponseSub;

    @Inject
    public UserProfilePresenter(DataManager dataManager, RxEventBus eventBus) {
        this.mDataManager = dataManager;
        this.eventBus = eventBus;
    }

    @Override
    public void attachView(UserProfileMvpView mvpView) {
        super.attachView(mvpView);
        getArticleActionSub = eventBus.filteredObservable(ArticleActivityActionButtonClickEvent.class).subscribe(e -> {
            if (getMvpView() != null) getMvpView().articleAction(e);
        });
        getArticleDetailSub = eventBus.filteredObservable(ShowArticleDetailEvent.class).subscribe(e -> {
            if (getMvpView() != null) getMvpView().articleDetailAction(e);
        });
        getArticleActivityResponseSub = eventBus.filteredObservable(ArticleActivityResponseEvent.class).subscribe(e -> {
            getMvpView().updateArticle(e.getArticleID(), e.getUserArticleActivity());
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        RxUtil.dispose(mUserSubscription);
        RxUtil.dispose(mMyArticlesSubscription);
        RxUtil.dispose(getArticleActionSub);
        RxUtil.dispose(getArticleDetailSub);
        RxUtil.dispose(getArticleActivityResponseSub);
    }

    public void getUserProfile(String userId) {
        checkViewAttached();
        RxUtil.dispose(mUserSubscription);
        mUserSubscription = mDataManager.getPbService().getUserProfileBundle(userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(responseDTO -> {
                    if (responseDTO.isSuccess()) {
                        getMvpView().setupUserProfileBundleDto(responseDTO.getData());
                    } else {
                        getMvpView().showError(responseDTO.getMessage());
                    }
                }, e -> {
                    Timber.e(e, "There was an error loading the ribots.");
                    getMvpView().showError(e.getMessage());
                });
    }

    public void getUserArticles(String userID, int pageNumber) {
        checkViewAttached();
        RxUtil.dispose(mMyArticlesSubscription);
        mMyArticlesSubscription = mDataManager.getPbService().getUserArticles(userID, pageNumber)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(responseDTO -> {
                    if (responseDTO.isSuccess()) {
                        if (responseDTO.getData().getResult().isEmpty()) {
                            getMvpView().showEmpty();
                        } else {
                            getMvpView().showArticles(responseDTO.getData());
                        }
                    } else {
                        getMvpView().showError(responseDTO.getMessage());
                    }
                }, e -> {
                    Timber.e(e, "There was an error loading the ribots.");
                    getMvpView().showError(e.getMessage());
                });
    }

    public UserDTO getCurrentUserDTO() {
        return mDataManager.getPreferencesHelper().getUser();
    }

    public void logout() {
        ProfileUtils.logoutAction(mDataManager, eventBus);
    }
}
