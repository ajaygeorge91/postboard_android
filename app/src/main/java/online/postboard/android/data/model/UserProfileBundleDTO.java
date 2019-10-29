package online.postboard.android.data.model;

/**
 * Created by ajayg on 9/25/2017.
 */

public class UserProfileBundleDTO {

    private UserDTO userDTO;
    private PaginatedResult<ArticleDTO> articleList;

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public PaginatedResult<ArticleDTO> getArticleList() {
        return articleList;
    }

    public void setArticleList(PaginatedResult<ArticleDTO> articleList) {
        this.articleList = articleList;
    }
}
