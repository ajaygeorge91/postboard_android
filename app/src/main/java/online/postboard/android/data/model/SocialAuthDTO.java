
package online.postboard.android.data.model;


public class SocialAuthDTO {

    private String clientId;
    private String code;
    private String redirectUri;
    private String access_token;

    public SocialAuthDTO(String clientId, String code, String redirectUri) {
        this.clientId = clientId;
        this.code = code;
        this.redirectUri = redirectUri;
    }

    public SocialAuthDTO(String access_token) {
        this.access_token = access_token;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        clientId = clientId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        code = code;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        redirectUri = redirectUri;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
}
