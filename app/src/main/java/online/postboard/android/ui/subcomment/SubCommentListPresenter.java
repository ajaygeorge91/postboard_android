package online.postboard.android.ui.subcomment;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import online.postboard.android.data.DataManager;
import online.postboard.android.data.events.CommentActionButtonClickEvent;
import online.postboard.android.data.events.CommentActivityResponseEvent;
import online.postboard.android.data.events.CommentReplyButtonClickEvent;
import online.postboard.android.data.events.SubCommentActionButtonClickEvent;
import online.postboard.android.data.events.SubCommentAddedEvent;
import online.postboard.android.data.model.AddSubCommentDTO;
import online.postboard.android.data.model.UserDTO;
import online.postboard.android.injection.ConfigPersistent;
import online.postboard.android.ui.base.BasePresenter;
import online.postboard.android.util.RxEventBus;
import online.postboard.android.util.RxUtil;
import timber.log.Timber;

/**
 * Created by Android SD-1 on 18-03-2017.
 */

@ConfigPersistent
public class SubCommentListPresenter extends BasePresenter<SubCommentListMvpView> {

    private DataManager mDataManager;
    private RxEventBus eventBus;

    private Disposable getCommentActionSub;
    private Disposable getSubCommentActionSub;
    private Disposable getCommentActivityResponseSub;

    private Disposable mGetSubCommentsSubscription;
    private Disposable addSubCommentSubscription;
    private Disposable replyCommentButtonClickSub;

    @Inject
    public SubCommentListPresenter(DataManager dataManager, RxEventBus eventBus) {
        mDataManager = dataManager;
        this.eventBus = eventBus;
    }

    @Override
    public void attachView(SubCommentListMvpView mvpView) {
        super.attachView(mvpView);

        getCommentActivityResponseSub = eventBus.filteredObservable(CommentActivityResponseEvent.class).subscribe(e -> {
            getMvpView().updateComment(e.getCommentID(), e.getUserCommentActivity());
        });

        getCommentActionSub = eventBus.filteredObservable(CommentActionButtonClickEvent.class).subscribe(e -> {
            getMvpView().commentAction(e);
        });
        getSubCommentActionSub = eventBus.filteredObservable(SubCommentActionButtonClickEvent.class).subscribe(e -> {
            getMvpView().subCommentAction(e);
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        RxUtil.dispose(mGetSubCommentsSubscription);
        RxUtil.dispose(addSubCommentSubscription);
        RxUtil.dispose(getCommentActionSub);
        RxUtil.dispose(getSubCommentActionSub);
        RxUtil.dispose(getCommentActivityResponseSub);
    }

    public void loadSubComments(String articleId, String commentId, int page) {
        checkViewAttached();
        RxUtil.dispose(mGetSubCommentsSubscription);
        mGetSubCommentsSubscription = mDataManager.getPbService().getHotSubComments(articleId, commentId, page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(res -> {
                    if (res.getData().getResult().isEmpty()) {
                        getMvpView().showItemsEmpty();
                    } else {
                        getMvpView().showItems(res.getData().getResult());
                    }
                }, e -> {
                    Timber.e(e, "There was an error loading the ribots.");
                    getMvpView().showError(e.getMessage() == null ? "There was an error loading the ribots." : e.getMessage());
                });

    }

    public void setUserDetails() {
        UserDTO userDTO = mDataManager.getPreferencesHelper().getUser();
        if (userDTO != null) {
            getMvpView().setUser(userDTO);
        }
    }

    public void postSubComment(String commentId, String messageContent) {
        checkViewAttached();
        RxUtil.dispose(addSubCommentSubscription);

        addSubCommentSubscription = mDataManager.getPbService().addSubComment("0", commentId, new AddSubCommentDTO(messageContent, commentId))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(res -> {
                    if (res.isSuccess()) {
                        getMvpView().subCommentPostSuccess(res.getData());
                        eventBus.post(new SubCommentAddedEvent(commentId, res.getData()));
                    } else {
                        getMvpView().subCommentPostFailed(res.getMessage());
                    }
                }, throwable -> {
                    Timber.e(throwable, throwable.getMessage());
                    getMvpView().subCommentPostFailed(throwable.getMessage());
                });
    }

    public void onResumeAction() {
        replyCommentButtonClickSub = eventBus.filteredObservable(CommentReplyButtonClickEvent.class).subscribe(e -> {
            getMvpView().replyBoxVisible(true);
        });
    }

    public void onPauseAction() {
        RxUtil.dispose(replyCommentButtonClickSub);

    }
}
