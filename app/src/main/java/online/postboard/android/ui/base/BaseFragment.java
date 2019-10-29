package online.postboard.android.ui.base;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import timber.log.Timber;
import online.postboard.android.injection.component.FragmentComponent;
import online.postboard.android.injection.module.FragmentModule;
import online.postboard.android.BoilerplateApplication;
import online.postboard.android.injection.component.ConfigPersistentComponent;
import online.postboard.android.injection.component.DaggerConfigPersistentComponent;

/**
 * Created by Android SD-1 on 18-03-2017.
 */

public class BaseFragment extends Fragment {

    private static final String KEY_FRAGMENT_ID = "KEY_FRAGMENT_ID";
    private static final Map<Long, ConfigPersistentComponent> sComponentsMap = new HashMap<>();
    private static final AtomicLong NEXT_ID = new AtomicLong(0);

    private FragmentComponent mFragmentComponent;
    private long mFragmentId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create the FragmentComponent and reuses cached ConfigPersistentComponent if this is
        // being called after a configuration change.
        mFragmentId = savedInstanceState != null ?
                savedInstanceState.getLong(KEY_FRAGMENT_ID) : NEXT_ID.getAndIncrement();
        ConfigPersistentComponent configPersistentComponent;
        if (!sComponentsMap.containsKey(mFragmentId)) {
            Timber.i("Creating new ConfigPersistentComponent id=%d", mFragmentId);
            configPersistentComponent = DaggerConfigPersistentComponent.builder()
                    .applicationComponent(BoilerplateApplication.get(
                            getActivity()).getComponent())
                    .build();
            sComponentsMap.put(mFragmentId, configPersistentComponent);
        } else {
            Timber.i("Reusing ConfigPersistentComponent id=%d", mFragmentId);
            configPersistentComponent = sComponentsMap.get(mFragmentId);
        }
        mFragmentComponent = configPersistentComponent.fragmentComponent(new FragmentModule(this));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_FRAGMENT_ID, mFragmentId);
    }

    @Override
    public void onDestroy() {
        if (!getActivity().isChangingConfigurations()) {
            Timber.i("Clearing ConfigPersistentComponent id=%d", mFragmentId);
            sComponentsMap.remove(mFragmentId);
        }
        super.onDestroy();
    }

    public FragmentComponent fragmentComponent() {
        return mFragmentComponent;
    }
}