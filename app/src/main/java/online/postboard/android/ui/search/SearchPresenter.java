package online.postboard.android.ui.search;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import online.postboard.android.data.DataManager;
import online.postboard.android.data.events.ArticleActivityActionButtonClickEvent;
import online.postboard.android.data.events.ArticleActivityResponseEvent;
import online.postboard.android.data.events.LoggedOutEvent;
import online.postboard.android.data.events.ProgressRetryButtonClickEvent;
import online.postboard.android.data.events.ShowArticleDetailEvent;
import online.postboard.android.data.events.ShowUserProfileEvent;
import online.postboard.android.data.model.AuthResponseDTO;
import online.postboard.android.injection.ConfigPersistent;
import online.postboard.android.ui.base.BasePresenter;
import online.postboard.android.util.RxEventBus;
import online.postboard.android.util.RxUtil;

@ConfigPersistent
public class SearchPresenter extends BasePresenter<SearchMvpView> {

    private DataManager mDataManager;
    private RxEventBus eventBus;
    private Disposable progressBarRetryListener;
    private Disposable getArticleSubscription;
    private Disposable userProfileShowSubscription;
    private Disposable getArticleActivityResponseSub;
    private Disposable getArticleActionSub;
    private Disposable getArticleDetailSub;

    @Inject
    public SearchPresenter(DataManager dataManager, RxEventBus eventBus) {
        this.mDataManager = dataManager;
        this.eventBus = eventBus;
    }

    @Override
    public void attachView(SearchMvpView mvpView) {
        super.attachView(mvpView);
        progressBarRetryListener = eventBus.filteredObservable(ProgressRetryButtonClickEvent.class).subscribe(e -> {
            getMvpView().continueListLoading();
        });
        getArticleActivityResponseSub = eventBus.filteredObservable(ArticleActivityResponseEvent.class).subscribe(e -> {
            getMvpView().updateArticle(e.getArticleID(), e.getUserArticleActivity());
        });

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

    @Override
    public void detachView() {
        super.detachView();
        RxUtil.dispose(progressBarRetryListener);
        RxUtil.dispose(getArticleActivityResponseSub);
        RxUtil.dispose(getArticleSubscription);

        RxUtil.dispose(userProfileShowSubscription);
        RxUtil.dispose(getArticleActionSub);
        RxUtil.dispose(getArticleDetailSub);
    }


    public void getArticleFromSearchQuery(String searchKey, int page) {
        checkViewAttached();
        if(searchKey.isEmpty()){
            getMvpView().clearArticles();
            return;
        }
        RxUtil.dispose(getArticleSubscription);
        getArticleSubscription = mDataManager.getPbService().getArticlesFromSearchQuery(searchKey, page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(res -> {
                    if (res.getData().isEmpty()) {
                        getMvpView().showArticlesEmpty();
                    } else {
                        getMvpView().showArticles(res.getData());
                    }
                }, e -> getMvpView().showError(e.getMessage()));
    }


}
