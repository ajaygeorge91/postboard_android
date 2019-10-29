package online.postboard.android.data.events;

/**
 * Created by Android SD-1 on 16-06-2017.
 */

public class SubCommentActionButtonClickEvent {

    private int adapterPosition;
    private ActionButton actionButton;

    public SubCommentActionButtonClickEvent(int adapterPosition, ActionButton actionButton) {
        this.adapterPosition = adapterPosition;
        this.actionButton = actionButton;
    }

    public int getAdapterPosition() {
        return adapterPosition;
    }

    public ActionButton getActionButton() {
        return actionButton;
    }

    public enum ActionButton {
        LIKE,
        DISLIKE,
        REPLY
    }
}