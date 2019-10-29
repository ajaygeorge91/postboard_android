package online.postboard.android.ui.articlenew;

import online.postboard.android.data.DataManager;
import online.postboard.android.data.events.ArticleCreatedEvent;
import online.postboard.android.data.model.UserDTO;
import online.postboard.android.injection.ConfigPersistent;
import online.postboard.android.ui.base.BasePresenter;
import online.postboard.android.util.RxEventBus;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import timber.log.Timber;

@ConfigPersistent
public class NewActivityPresenter extends BasePresenter<DetailMvpView> {

    private final DataManager mDataManager;
    private RxEventBus eventBus;

    @Inject
    public NewActivityPresenter(DataManager dataManager, RxEventBus eventBus) {
        mDataManager = dataManager;
        this.eventBus = eventBus;
    }

    @Override
    public void attachView(DetailMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    public DataManager getDataManager() {
        return mDataManager;
    }

    public void setUserDetails() {
        UserDTO userDTO = mDataManager.getPreferencesHelper().getUser();
        if (userDTO != null) {
            getMvpView().setUser(userDTO);
        } else {
            mDataManager.getPbService().getUser()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(res -> {
                        if (res.isSuccess()) {
                            getMvpView().setUser(res.getData());
                        } else {
                            getMvpView().showError(res.getMessage());
                        }
                    }, throwable -> {
                        Timber.e(throwable, throwable.getMessage());
                        getMvpView().showError(throwable.getMessage());
                    });
        }
    }

    public void addArticle(RequestBody title,RequestBody content, MultipartBody.Part image) {
        mDataManager.getPbService().addArticle(title, image)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(res -> {
                    if (res.isSuccess()) {
                        eventBus.post(new ArticleCreatedEvent(res.getData()));
                        getMvpView().articleCreated(res.getData());
                    } else {
                        getMvpView().showError(res.getMessage());
                    }
                }, throwable -> {
                    Timber.e(throwable, throwable.getMessage());
                    getMvpView().showError(throwable.getMessage());
                });
    }

}
