package online.postboard.android.data.model;

/**
 * Created by ajayg on 6/29/2017.
 */

public class AuthDTO {

    private String fullName;
    private String email;
    private String password;

    public AuthDTO(String fullName, String email, String password) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
    }

    public AuthDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
