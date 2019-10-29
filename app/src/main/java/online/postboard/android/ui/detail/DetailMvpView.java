package online.postboard.android.ui.detail;

import java.util.List;

import online.postboard.android.data.events.ArticleActivityActionButtonClickEvent;
import online.postboard.android.data.events.CommentActionButtonClickEvent;
import online.postboard.android.data.events.LoadCommentsEvent;
import online.postboard.android.data.events.SubCommentActionButtonClickEvent;
import online.postboard.android.data.model.ArticleDTO;
import online.postboard.android.data.model.CommentDTO;
import online.postboard.android.data.model.SubCommentDTO;
import online.postboard.android.data.model.UserArticleActivity;
import online.postboard.android.data.model.UserCommentActivity;
import online.postboard.android.data.model.UserDTO;
import online.postboard.android.ui.base.MvpView;

public interface DetailMvpView extends MvpView {

    void showComments(List<CommentDTO> commentDTOs);

    void showCommentsEmpty();

    void showError(String message);

    void clearAllCommentsAndLoad(LoadCommentsEvent.CommentType commentType);

    void updateArticle(String articleID, UserArticleActivity userArticleActivity);

    void articleAction(ArticleActivityActionButtonClickEvent articleActivityActionButtonClickEvent);

    void updateComment(String commentID, UserCommentActivity userCommentActivity);

    void commentAction(CommentActionButtonClickEvent commentActionButtonClickEvent);

    void subCommentAction(SubCommentActionButtonClickEvent subCommentActionButtonClickEvent);

    void subCommentAddedAction(String commentId, SubCommentDTO subCommentDTO);

    void loadMoreSubCommentsAction(int loadMoreSubCommentButtonClickEvent);

    void replyBoxVisible(boolean visible);

    void setUser(UserDTO userDTO);

    void commentPostSuccess(CommentDTO addCommentDTO);

    void commentPostFailed(String message);

    void showArticle(ArticleDTO articleDTO);
}
