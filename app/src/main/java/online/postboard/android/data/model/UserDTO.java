package online.postboard.android.data.model;

import java.io.Serializable;

public class UserDTO extends AnyItem implements Serializable {

    private String avatarURL;
    private LoginInfo loginInfo;
    private String fullName;
    private String notificationChannel;
    private String id;
    private UserProfile userProfile;

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setLoginInfo(LoginInfo loginInfo) {
        this.loginInfo = loginInfo;
    }

    public LoginInfo getLoginInfo() {
        return loginInfo;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    @Override
    public String toString() {
        return
                "UserDTO{" +
                        "avatarURL = '" + avatarURL + '\'' +
                        ",loginInfo = '" + loginInfo + '\'' +
                        ",fullName = '" + fullName + '\'' +
                        ",id = '" + id + '\'' +
                        ",userProfile = '" + userProfile + '\'' +
                        "}";
    }

    public UserDTO() {
    }

    public String getNotificationChannel() {
        return notificationChannel;
    }

    public void setNotificationChannel(String notificationChannel) {
        this.notificationChannel = notificationChannel;
    }
}