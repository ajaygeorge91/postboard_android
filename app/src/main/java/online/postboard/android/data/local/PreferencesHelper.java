package online.postboard.android.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;

import online.postboard.android.data.model.UserDTO;
import online.postboard.android.injection.ApplicationContext;

@Singleton
public class PreferencesHelper {

    public static final String PREF_FILE_NAME = "android_boilerplate_pref_file";

    public static final String PREF_KEY_USER_TOKEN = "android_boilerplate_user_token";
    public static final String PREF_KEY_USER_DTO = "android_boilerplate_user_dto";

    private final SharedPreferences mPref;

    @Inject
    public PreferencesHelper(@ApplicationContext Context context) {
        mPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public void clear() {
        mPref.edit().clear().apply();
    }

    public void saveToken(String token) {
        mPref.edit().putString(PREF_KEY_USER_TOKEN, token).apply();
    }

    public String getToken() {
        return mPref.getString(PREF_KEY_USER_TOKEN, "");
    }

    public void saveUser(UserDTO userDTO) {
        mPref.edit().putString(PREF_KEY_USER_DTO, new Gson().toJson(userDTO)).apply();
    }

    public UserDTO getUser() {
        final String userString = mPref.getString(PREF_KEY_USER_DTO, "");
        if (userString.isEmpty())
            return null;
        return new Gson().fromJson(userString, UserDTO.class);
    }

}
