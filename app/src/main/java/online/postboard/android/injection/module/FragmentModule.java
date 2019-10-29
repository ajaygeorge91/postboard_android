package online.postboard.android.injection.module;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import online.postboard.android.injection.ActivityContext;

/**
 * Created by Android SD-1 on 18-03-2017.
 */

@Module
public class FragmentModule {

    private Fragment mFragment;

    public FragmentModule(Fragment fragment) {
        mFragment = fragment;
    }

    @Provides
    Fragment providesFragment() {
        return mFragment;
    }

    @Provides
    Activity provideActivity() {
        return mFragment.getActivity();
    }

    @Provides
    @ActivityContext
    Context providesContext() {
        return mFragment.getActivity();
    }

}
