package online.postboard.android.data.events;

/**
 * Created by Android SD-1 on 16-06-2017.
 */

public class CommentReplyButtonClickEvent {

    private int adapterPosition;

    public CommentReplyButtonClickEvent(int adapterPosition) {
        this.adapterPosition = adapterPosition;
    }

    public int getAdapterPosition() {
        return adapterPosition;
    }

}
