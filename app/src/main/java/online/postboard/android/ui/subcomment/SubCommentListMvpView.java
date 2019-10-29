package online.postboard.android.ui.subcomment;

import online.postboard.android.data.events.CommentActionButtonClickEvent;
import online.postboard.android.data.events.SubCommentActionButtonClickEvent;
import online.postboard.android.data.model.AnyItem;
import online.postboard.android.data.model.SubCommentDTO;
import online.postboard.android.data.model.UserCommentActivity;
import online.postboard.android.data.model.UserDTO;
import online.postboard.android.ui.base.MvpView;

import java.util.List;

public interface SubCommentListMvpView extends MvpView {

    void showItems(List<? extends AnyItem> anyItemList);

    void showItemsEmpty();

    void showError(String message);

    void setUser(UserDTO userDTO);

    void subCommentPostSuccess(SubCommentDTO subCommentDTO);

    void subCommentPostFailed(String message);

    void replyBoxVisible(boolean visible);

    void commentAction(CommentActionButtonClickEvent commentActionButtonClickEvent);

    void subCommentAction(SubCommentActionButtonClickEvent subCommentActionButtonClickEvent);

    void updateComment(String commentID, UserCommentActivity userCommentActivity);
}
