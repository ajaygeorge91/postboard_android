package online.postboard.android.data;

import online.postboard.android.data.local.DatabaseHelper;
import online.postboard.android.data.local.PreferencesHelper;
import online.postboard.android.data.remote.PbService;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DataManager {

    private final PbService mPbService;
    private final PreferencesHelper mPreferencesHelper;
    private final DatabaseHelper mDatabaseHelper;

    @Inject
    public DataManager(PbService pbService, PreferencesHelper preferencesHelper, DatabaseHelper databaseHelper) {
        mPbService = pbService;
        mPreferencesHelper = preferencesHelper;
        mDatabaseHelper = databaseHelper;
    }

    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }

    public PbService getPbService() {
        return mPbService;
    }

    public DatabaseHelper getDatabaseHelper() {
        return mDatabaseHelper;
    }
}
