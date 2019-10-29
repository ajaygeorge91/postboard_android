package online.postboard.android.ui.detail;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import online.postboard.android.data.DataManager;
import online.postboard.android.data.events.ArticleActivityResponseEvent;
import online.postboard.android.data.events.CommentReplyButtonClickEvent;
import online.postboard.android.data.events.LoadCommentsEvent;
import online.postboard.android.data.events.LoadMoreSubCommentButtonClickEvent;
import online.postboard.android.data.events.ShowReplyBoxEvent;
import online.postboard.android.data.events.SubCommentActionButtonClickEvent;
import online.postboard.android.data.events.SubCommentAddedEvent;
import online.postboard.android.data.model.AddCommentDTO;
import online.postboard.android.data.model.UserDTO;
import online.postboard.android.injection.ConfigPersistent;
import online.postboard.android.util.RxEventBus;
import online.postboard.android.util.RxUtil;
import timber.log.Timber;

import online.postboard.android.data.events.ArticleActivityActionButtonClickEvent;
import online.postboard.android.data.events.CommentActionButtonClickEvent;
import online.postboard.android.data.events.CommentActivityResponseEvent;
import online.postboard.android.ui.base.BasePresenter;

@ConfigPersistent
public class DetailPresenter extends BasePresenter<DetailMvpView> {

    private DataManager mDataManager;
    private RxEventBus eventBus;

    private Disposable getCommentSubscription;
    private Disposable addCommentSubscription;

    private Disposable getByNodeIdSubscription;

    private Disposable getSubCommentAddedEventSub;
    private Disposable getSubCommentActionSub;
    private Disposable getCommentActionSub;
    private Disposable getCommentActivityResponseSub;

    private Disposable getArticleActivityResponseSub;
    private Disposable getArticleActionSub;

    private Disposable loadMoreSubCommentActionSub;
    private Disposable replyCommentButtonClickSub;
    private Disposable showReplyBoxActionSub;
    private Disposable commentTypeChangeSub;

    @Inject
    public DetailPresenter(DataManager dataManager, RxEventBus eventBus) {
        mDataManager = dataManager;
        this.eventBus = eventBus;
    }

    @Override
    public void attachView(DetailMvpView mvpView) {
        super.attachView(mvpView);

        getArticleActivityResponseSub = eventBus.filteredObservable(ArticleActivityResponseEvent.class).subscribe(e -> {
            getMvpView().updateArticle(e.getArticleID(), e.getUserArticleActivity());
        });
        getArticleActionSub = eventBus.filteredObservable(ArticleActivityActionButtonClickEvent.class).subscribe(e -> {
            getMvpView().articleAction(e);
        });

        getCommentActivityResponseSub = eventBus.filteredObservable(CommentActivityResponseEvent.class).subscribe(e -> {
            getMvpView().updateComment(e.getCommentID(), e.getUserCommentActivity());
        });
        getCommentActionSub = eventBus.filteredObservable(CommentActionButtonClickEvent.class).subscribe(e -> {
            getMvpView().commentAction(e);
        });
        getSubCommentActionSub = eventBus.filteredObservable(SubCommentActionButtonClickEvent.class).subscribe(e -> {
            getMvpView().subCommentAction(e);
        });
        getSubCommentAddedEventSub = eventBus.filteredObservable(SubCommentAddedEvent.class).subscribe(e -> {
            getMvpView().subCommentAddedAction(e.getCommentID(), e.getSubCommentDTO());
        });

        replyCommentButtonClickSub = eventBus.filteredObservable(CommentReplyButtonClickEvent.class).subscribe(e -> {
            getMvpView().loadMoreSubCommentsAction(e.getAdapterPosition());
        });
        loadMoreSubCommentActionSub = eventBus.filteredObservable(LoadMoreSubCommentButtonClickEvent.class).subscribe(e -> {
            getMvpView().loadMoreSubCommentsAction(e.getAdapterPosition());
        });
        showReplyBoxActionSub = eventBus.filteredObservable(ShowReplyBoxEvent.class).subscribe(e -> {
            getMvpView().replyBoxVisible(true);
        });
        commentTypeChangeSub = eventBus.filteredObservable(LoadCommentsEvent.class).subscribe(e -> {
            getMvpView().clearAllCommentsAndLoad(e.getCommentType());
        });

    }

    @Override
    public void detachView() {
        super.detachView();
        RxUtil.dispose(getCommentSubscription);
        RxUtil.dispose(addCommentSubscription);
        RxUtil.dispose(getByNodeIdSubscription);
        RxUtil.dispose(getCommentActivityResponseSub);
        RxUtil.dispose(getCommentActionSub);
        RxUtil.dispose(getSubCommentAddedEventSub);
        RxUtil.dispose(getSubCommentActionSub);
        RxUtil.dispose(getArticleActivityResponseSub);
        RxUtil.dispose(getArticleActionSub);
        RxUtil.dispose(loadMoreSubCommentActionSub);
        RxUtil.dispose(replyCommentButtonClickSub);
        RxUtil.dispose(showReplyBoxActionSub);
        RxUtil.dispose(commentTypeChangeSub);
    }

    public DataManager getDataManager() {
        return mDataManager;
    }

    public void loadComments(String articleId, int page, LoadCommentsEvent.CommentType commentType) {
        checkViewAttached();
        RxUtil.dispose(getCommentSubscription);
        Timber.d(commentType.toString() + " : " + page);
        if (commentType.equals(LoadCommentsEvent.CommentType.HOT)) {
            getCommentSubscription = mDataManager.getPbService().getHotComments(articleId, page)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(res -> {
                        if (res.getData().getResult().isEmpty()) {
                            getMvpView().showCommentsEmpty();
                        } else {
                            getMvpView().showComments(res.getData().getResult());
                        }
                    }, throwable -> {
                        Timber.e(throwable, throwable.getMessage());
                        getMvpView().showError(throwable.getMessage());
                    });
        } else {
            getCommentSubscription = mDataManager.getPbService().getNewComments(articleId, page)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(res -> {
                        if (res.getData().getResult().isEmpty()) {
                            getMvpView().showCommentsEmpty();
                        } else {
                            getMvpView().showComments(res.getData().getResult());
                        }
                    }, throwable -> {
                        Timber.e(throwable, throwable.getMessage());
                        getMvpView().showError(throwable.getMessage());
                    });
        }
    }

    public void postComment(String articleId, String comment) {
        checkViewAttached();
        RxUtil.dispose(addCommentSubscription);

        addCommentSubscription = mDataManager.getPbService().addComment(articleId, new AddCommentDTO(comment, articleId))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(res -> {
                    if (res.isSuccess()) {
                        getMvpView().commentPostSuccess(res.getData());
                    } else {
                        getMvpView().commentPostFailed(res.getMessage());
                    }
                }, throwable -> {
                    Timber.e(throwable, throwable.getMessage());
                    getMvpView().commentPostFailed(throwable.getMessage());
                });

    }

    public void setUserDetails() {
        UserDTO userDTO = mDataManager.getPreferencesHelper().getUser();
        if (userDTO != null) {
            getMvpView().setUser(userDTO);
        }
    }

    public void loadByNodeId(String nodeID) {
        checkViewAttached();
        RxUtil.dispose(getByNodeIdSubscription);
        getByNodeIdSubscription = mDataManager.getPbService().getByNodeID(nodeID)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(res -> {
                    if (res.isSuccess()) {
                        getMvpView().showArticle(res.getData());
                    } else {
                        getMvpView().showError(res.getMessage());
                    }
                }, throwable -> {
                    Timber.e(throwable, throwable.getMessage());
                    getMvpView().showError(throwable.getMessage());
                });
    }


}
