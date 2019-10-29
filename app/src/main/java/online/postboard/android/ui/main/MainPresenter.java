package online.postboard.android.ui.main;

import javax.inject.Inject;

import online.postboard.android.data.events.CreateNewArticleEvent;
import online.postboard.android.data.events.MqttSubscribeEvent;
import online.postboard.android.data.model.AuthResponseDTO;
import online.postboard.android.util.apputils.NotificationUtils;
import online.postboard.android.util.apputils.ProfileUtils;
import online.postboard.android.data.DataManager;
import online.postboard.android.data.events.LoggedOutEvent;
import online.postboard.android.data.events.NotificationUnreadCountEvent;
import online.postboard.android.data.model.MyMqttMessage;
import online.postboard.android.data.model.NotificationDTO;
import online.postboard.android.injection.ConfigPersistent;
import online.postboard.android.ui.base.BasePresenter;
import online.postboard.android.util.RxEventBus;
import online.postboard.android.util.RxUtil;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

@ConfigPersistent
public class MainPresenter extends BasePresenter<MainMvpView> {

    private final DataManager mDataManager;
    private Disposable signInSub;
    private Disposable loggedOutListener;
    private Disposable newArticleSub;
    private Disposable mUserSubscription;
    private Disposable mNotificationUnreadCountSub;
    private Disposable mNotificationsApiSubscription;
    private Disposable mqttMessageListener;


    @Inject
    RxEventBus eventBus;

    @Inject
    public MainPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(MainMvpView mvpView) {
        super.attachView(mvpView);
        newArticleSub = eventBus.filteredObservable(CreateNewArticleEvent.class).subscribe(e -> {
            getMvpView().newArticle(e);
        });
        mNotificationUnreadCountSub = eventBus.filteredObservable(NotificationUnreadCountEvent.class).subscribe(e -> {
            getMvpView().updateNotificationCount(e.getNotificationUnreadCount());
        });
        loggedOutListener = eventBus.filteredObservable(LoggedOutEvent.class).subscribe(e -> {
            getMvpView().clearUser();
        });
        signInSub = eventBus.filteredObservable(AuthResponseDTO.class).subscribe(e -> {
                    getMvpView().showSignInSuccessful(e);
                }, throwable -> {

                }
        );
        mqttMessageListener = eventBus.filteredObservable(MyMqttMessage.class).subscribe(e -> {
            if (e.getMessageType() != null && e.getMessageType().equalsIgnoreCase("user_notification")) {
                mDataManager.getDatabaseHelper().getNotifications()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(responseDTO -> {
                            String stringCount = NotificationUtils.getCount(responseDTO);
                            eventBus.post(new NotificationUnreadCountEvent(stringCount));
                        });
            } else if (e.getMessageType() != null && e.getMessageType().equalsIgnoreCase("user_notification_list")) {
                List<NotificationDTO> notificationDTOList = (List<NotificationDTO>) e.getData();
                String stringCount = NotificationUtils.getCount(notificationDTOList);
                eventBus.post(new NotificationUnreadCountEvent(stringCount));
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        RxUtil.dispose(signInSub);
        RxUtil.dispose(loggedOutListener);
        RxUtil.dispose(newArticleSub);
        RxUtil.dispose(mUserSubscription);
        RxUtil.dispose(mNotificationUnreadCountSub);
        RxUtil.dispose(mNotificationsApiSubscription);
        RxUtil.dispose(mqttMessageListener);
    }

    public void getUserDTOFromServer() {
        checkViewAttached();
        RxUtil.dispose(mUserSubscription);

        mUserSubscription = mDataManager.getPbService().getUserOpt()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(responseDTO -> {
                    if (responseDTO.isSuccess()) {
                        // TODO.. get subTopic form server
                        eventBus.post(new MqttSubscribeEvent(ProfileUtils.getUserNotificationChannel(responseDTO.getData())));
                        mDataManager.getPreferencesHelper().saveUser(responseDTO.getData());
                        getMvpView().setUserDto(responseDTO.getData());
                    } else {
                        ProfileUtils.logoutAction(mDataManager, eventBus);
                    }
                }, e -> {
                    Timber.e(e, "There was an error getting the user.");
                    getMvpView().showError(e.getMessage());
                });
    }

    public void getNotificatoinsFromServer() {
        RxUtil.dispose(mNotificationsApiSubscription);
        mNotificationsApiSubscription = mDataManager.getPbService().getMyNotifications(0)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(responseDTO -> {
                    if (responseDTO.isSuccess()) {
                        mDataManager.getDatabaseHelper().setNotifications(responseDTO.getData())
                                .subscribeOn(Schedulers.io())
                                .subscribe(notificationDTOList -> {
                                    String stringCount = NotificationUtils.getCount(notificationDTOList);
                                    eventBus.post(new NotificationUnreadCountEvent(stringCount));
                                });
                    }
                }, e -> {
                    Timber.e(e, "There was an error loading the ribots.");
                    getMvpView().showError(e.getMessage());
                });
    }

}
