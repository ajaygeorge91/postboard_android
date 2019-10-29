package online.postboard.android.ui.list;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import online.postboard.android.data.DataManager;
import online.postboard.android.ui.base.BasePresenter;
import online.postboard.android.util.RxUtil;

/**
 * Created by Android SD-1 on 18-03-2017.
 */

public class AnyItemListPresenter extends BasePresenter<AnyItemListMvpView> {

    private DataManager mDataManager;
    private Disposable mSubscription;

    @Inject
    public AnyItemListPresenter(DataManager dataManager) {
        this.mDataManager = dataManager;
    }

    @Override
    public void detachView() {
        super.detachView();
        RxUtil.dispose(mSubscription);
    }

    public void loadArticles(int page) {
        checkViewAttached();
        RxUtil.dispose(mSubscription);
        mSubscription = mDataManager.getPbService().getHotArticles(page)
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
}
