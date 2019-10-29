package online.postboard.android.data.events;

/**
 * Created by ajayg on 8/7/2017.
 */

public class SocialLoginEvent {
    public SocialLoginProvider getSocialLoginProvider() {
        return socialLoginProvider;
    }

    public enum SocialLoginProvider {
        FACEBOOK, GOOGLE, APP_LOGIN_PAGE, APP_REGISTER_PAGE
    }

    private SocialLoginProvider socialLoginProvider;

    public SocialLoginEvent(SocialLoginProvider socialLoginProvider) {
        this.socialLoginProvider = socialLoginProvider;
    }

}
