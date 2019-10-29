package online.postboard.android.data.model;

/**
 * Created by Android SD-1 on 22-06-2017.
 */

public class CommentReplyBox extends AnyItem {

    private UserDTO userDTO;

    public CommentReplyBox(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }
}
