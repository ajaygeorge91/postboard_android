package online.postboard.android.ui.articles;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import online.postboard.android.data.events.ArticleActivityResponseEvent;
import online.postboard.android.data.events.ProgressRetryButtonClickEvent;
import online.postboard.android.data.model.AuthResponseDTO;
import online.postboard.android.data.DataManager;
import online.postboard.android.data.events.LoggedOutEvent;
import online.postboard.android.data.events.ShowUserProfileEvent;
import online.postboard.android.util.RxEventBus;
import online.postboard.android.util.RxUtil;
import timber.log.Timber;

import online.postboard.android.data.events.ShowArticleDetailEvent;
import online.postboard.android.data.events.ArticleActivityActionButtonClickEvent;
import online.postboard.android.ui.base.BasePresenter;

/**
 * Created by Android SD-1 on 18-03-2017.
 */

public class ArticleListPresenter extends BasePresenter<ArticleListMvpView> {

    private DataManager mDataManager;
    private RxEventBus eventBus;
    private Disposable signInListener;
    private Disposable progressBarRetryListener;
    private Disposable logOutListener;
    private Disposable getArticleSubscription;
    private Disposable userProfileShowSubscription;
    private Disposable getArticleActivityResponseSub;
    private Disposable getArticleActionSub;
    private Disposable getArticleDetailSub;

    @Inject
    public ArticleListPresenter(DataManager dataManager, RxEventBus eventBus) {
        this.mDataManager = dataManager;
        this.eventBus = eventBus;
    }

    @Override
    public void attachView(ArticleListMvpView mvpView) {
        super.attachView(mvpView);
        progressBarRetryListener = eventBus.filteredObservable(ProgressRetryButtonClickEvent.class).subscribe(e -> {
            getMvpView().continueListLoading();
        });
        getArticleActivityResponseSub = eventBus.filteredObservable(ArticleActivityResponseEvent.class).subscribe(e -> {
            getMvpView().updateArticle(e.getArticleID(), e.getUserArticleActivity());
        });
        signInListener = eventBus.filteredObservable(AuthResponseDTO.class).subscribe(e -> {
            getMvpView().getUserArticleActivity();
        });
        logOutListener = eventBus.filteredObservable(LoggedOutEvent.class).subscribe(e -> {
            getMvpView().removeUserArticleActivity();
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        RxUtil.dispose(progressBarRetryListener);
        RxUtil.dispose(getArticleActivityResponseSub);
        RxUtil.dispose(getArticleSubscription);
        RxUtil.dispose(signInListener);
        RxUtil.dispose(logOutListener);
    }

    // the action event shall not be subscribed from other activity(article detail)
    public void onPauseAction() {
        RxUtil.dispose(userProfileShowSubscription);
        RxUtil.dispose(getArticleActionSub);
        RxUtil.dispose(getArticleDetailSub);
    }

    public void onResumeAction() {
        RxUtil.dispose(userProfileShowSubscription);
        userProfileShowSubscription = eventBus.filteredObservable(ShowUserProfileEvent.class).subscribe(e -> {
            getMvpView().showUserProfile(e);
        });

        RxUtil.dispose(getArticleActionSub);
        getArticleActionSub = eventBus.filteredObservable(ArticleActivityActionButtonClickEvent.class).subscribe(e -> {
            if (getMvpView() != null) getMvpView().articleAction(e);
        });

        RxUtil.dispose(getArticleDetailSub);
        getArticleDetailSub = eventBus.filteredObservable(ShowArticleDetailEvent.class).subscribe(e -> {
            if (getMvpView() != null) getMvpView().articleDetailAction(e);
        });
    }

    public void loadNewArticles(int page) {
        checkViewAttached();
        RxUtil.dispose(getArticleSubscription);
        getArticleSubscription = mDataManager.getPbService().getNewArticles(page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(res -> {
                    if (res.getData().getResult().isEmpty()) {
                        getMvpView().showArticlesEmpty();
                    } else {
                        getMvpView().showArticles(res.getData().getResult());
                    }
                }, e -> getMvpView().showError(e.getMessage()));
    }

    public void loadHotArticles(int page) {
        checkViewAttached();
        RxUtil.dispose(getArticleSubscription);
        getArticleSubscription = mDataManager.getPbService().getHotArticles(page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(res -> {
                    if (res.getData().getResult().isEmpty()) {
                        getMvpView().showArticlesEmpty();
                    } else {
                        getMvpView().showArticles(res.getData().getResult());
                    }
                }, e -> {
                    Timber.e(e, "There was an error loading the ribots.");
                    getMvpView().showError(e.getMessage() == null ? "There was an error loading the ribots." : e.getMessage());
                });
    }

    public void getHotArticleUserActivity(int page) {
        checkViewAttached();
        RxUtil.dispose(getArticleSubscription);
        getArticleSubscription = mDataManager.getPbService().getHotArticleUserActivity(page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(res -> {
                    if (!res.getData().isEmpty()) {
                        getMvpView().updateArticleActivity(res.getData());
                    }
                }, e -> Timber.e(e, "There was an error loading the User Activities."));
    }

    public void getNewArticleUserActivity(int page) {
        checkViewAttached();
        RxUtil.dispose(getArticleSubscription);
        getArticleSubscription = mDataManager.getPbService().getNewArticleUserActivity(page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(res -> {
                    if (!res.getData().isEmpty()) {
                        getMvpView().updateArticleActivity(res.getData());
                    }
                }, e -> Timber.e(e, "There was an error loading the User Activities."));
    }


}
