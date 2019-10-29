package online.postboard.android.data.model;

/**
 * Created by ajayg on 6/29/2017.
 */

public class AuthResponseDTO {

    private String token;
    private UserDTO userDTO;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }
}
