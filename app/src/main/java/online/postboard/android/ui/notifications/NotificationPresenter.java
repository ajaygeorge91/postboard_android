package online.postboard.android.ui.notifications;

import online.postboard.android.data.DataManager;
import online.postboard.android.data.events.LoadNodeEvent;
import online.postboard.android.data.events.LoggedOutEvent;
import online.postboard.android.data.events.NotificationUnreadCountEvent;
import online.postboard.android.data.events.ProgressRetryButtonClickEvent;
import online.postboard.android.data.model.AuthResponseDTO;
import online.postboard.android.data.model.MyMqttMessage;
import online.postboard.android.data.model.UserDTO;
import online.postboard.android.util.RxEventBus;
import online.postboard.android.util.RxUtil;
import online.postboard.android.data.model.NotificationDTO;
import online.postboard.android.ui.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Android SD-1 on 18-03-2017.
 */
public class NotificationPresenter extends BasePresenter<NotificationMvpView> {

    private DataManager mDataManager;
    private Disposable mNotificationsApiSubscription;
    private Disposable mNotificationsCachedSubscription;
    private Disposable mNotificationsReadApiSubscription;
    private Disposable mNodeClickSubscription;
    private Disposable signInListener;
    private Disposable progressBarRetryListener;
    private Disposable logOutListener;
    private Disposable mqttMessageListener;

    private RxEventBus eventBus;

    @Inject
    public NotificationPresenter(DataManager dataManager, RxEventBus eventBus) {
        this.mDataManager = dataManager;
        this.eventBus = eventBus;
    }

    @Override
    public void attachView(NotificationMvpView mvpView) {
        super.attachView(mvpView);
        signInListener = eventBus.filteredObservable(AuthResponseDTO.class).subscribe(e -> {
            getMvpView().setupUserDto(e.getUserDTO());
        });
        progressBarRetryListener = eventBus.filteredObservable(ProgressRetryButtonClickEvent.class).subscribe(e -> {
            getMvpView().continueListLoading();
        });
        logOutListener = eventBus.filteredObservable(LoggedOutEvent.class).subscribe(e -> {
            getMvpView().signInView();
        });
        mNodeClickSubscription = eventBus.filteredObservable(LoadNodeEvent.class).subscribe(e -> {
            getMvpView().loadNodeActivity(e.getAdapterPosition());
        });
        mqttMessageListener = eventBus.filteredObservable(MyMqttMessage.class).subscribe(e -> {
            if (e.getMessageType() != null && e.getMessageType().equalsIgnoreCase("user_notification")) {
                NotificationDTO n = ((NotificationDTO) e.getData());
                List<NotificationDTO> notificationDTOList = new ArrayList<>();
                notificationDTOList.add(n);
                getMvpView().updateNotifications(notificationDTOList);
            } else if (e.getMessageType() != null && e.getMessageType().equalsIgnoreCase("user_notification_list")) {
                List<NotificationDTO> notificationDTOList = (List<NotificationDTO>) e.getData();
                getMvpView().updateNotifications(notificationDTOList);
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        RxUtil.dispose(mNodeClickSubscription);
        RxUtil.dispose(mNotificationsApiSubscription);
        RxUtil.dispose(mNotificationsReadApiSubscription);
        RxUtil.dispose(mNotificationsCachedSubscription);
        RxUtil.dispose(logOutListener);
        RxUtil.dispose(progressBarRetryListener);
        RxUtil.dispose(signInListener);
        RxUtil.dispose(mqttMessageListener);
    }

    public void getCachedNotifications() {
        checkViewAttached();
        RxUtil.dispose(mNotificationsCachedSubscription);
        mNotificationsCachedSubscription = mDataManager.getDatabaseHelper().getNotifications()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(responseDTO -> {
                    getMvpView().showCachedNotifications(responseDTO);
                }, e -> {
                    Timber.e(e, "There was an error loading the ribots.");
                    getMvpView().showError(e.getMessage());
                });
    }

    public void getMyNotifications(int pageNumber) {
        checkViewAttached();
        RxUtil.dispose(mNotificationsApiSubscription);
        mNotificationsApiSubscription = mDataManager.getPbService().getMyNotifications(pageNumber)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(responseDTO -> {
                    if (responseDTO.isSuccess()) {
                        if (pageNumber == 0) {
                            getMvpView().clearItems();
                            mDataManager.getDatabaseHelper().setNotifications(responseDTO.getData());
                        }
                        if (responseDTO.getData().isEmpty()) {
                            getMvpView().showNotificationEmpty();
                        } else {
                            getMvpView().showNotifications(responseDTO.getData());
                        }
                    } else {
                        getMvpView().showError(responseDTO.getMessage());
                    }
                }, e -> {
                    Timber.e(e, "There was an error loading the ribots.");
                    getMvpView().showError(e.getMessage());
                });
    }

    public UserDTO getUserDTO() {
        return mDataManager.getPreferencesHelper().getUser();
    }

    public void publishUnreadCount(String unreadCount) {
        eventBus.post(new NotificationUnreadCountEvent(unreadCount));
    }

    public void readNotifications() {
        RxUtil.dispose(mNotificationsReadApiSubscription);
        mNotificationsReadApiSubscription = mDataManager.getPbService().readNotifications()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(responseDTO -> {
                    if (responseDTO.isSuccess()) {
                        getMvpView().readAllNotifications();
                        mDataManager.getDatabaseHelper().setNotifications(getMvpView().getNotifications());
                    } else {
//                        getMvpView().showError(responseDTO.getMessage());
                    }
                }, e -> {
                    Timber.e(e, "There was an error loading the ribots.");
//                    getMvpView().showError(e.getMessage());
                });
    }
}
