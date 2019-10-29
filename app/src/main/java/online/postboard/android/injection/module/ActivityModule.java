package online.postboard.android.injection.module;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import dagger.Module;
import dagger.Provides;
import online.postboard.android.injection.ActivityContext;

@Module
public class ActivityModule {

    private AppCompatActivity mActivity;

    public ActivityModule(AppCompatActivity activity) {
        mActivity = activity;
    }

    @Provides
    Activity provideActivity() {
        return mActivity;
    }


    @Provides
    @ActivityContext
    Context providesContext() {
        return mActivity;
    }

    @Provides
    FragmentManager providesFragmentManager() {
        return mActivity.getFragmentManager();
    }
}
