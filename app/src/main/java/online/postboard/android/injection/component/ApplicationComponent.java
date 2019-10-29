package online.postboard.android.injection.component;

import android.app.Application;
import android.content.Context;

import online.postboard.android.data.DataManager;
import online.postboard.android.injection.ApplicationContext;
import online.postboard.android.receivers.NetworkChangeReceiver;
import online.postboard.android.data.MyMQTTService;
import online.postboard.android.data.local.PreferencesHelper;
import online.postboard.android.data.remote.PbService;
import online.postboard.android.injection.module.ApplicationModule;
import online.postboard.android.util.RxEventBus;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {


    void inject(MyMQTTService myMQTTService);

    @ApplicationContext
    Context context();
    Application application();
    PbService pbService();
    PreferencesHelper preferencesHelper();
    DataManager dataManager();
    RxEventBus eventBus();

    void inject(NetworkChangeReceiver networkChangeReceiver);
}
