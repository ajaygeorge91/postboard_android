package online.postboard.android.data.events;

/**
 * Created by Android SD-1 on 16-06-2017.
 */

public class ShowArticleDetailEvent {

    private int adapterPosition;
    private boolean showArticle = false;

    public ShowArticleDetailEvent(int adapterPosition) {
        this.adapterPosition = adapterPosition;
    }

    public ShowArticleDetailEvent(int adapterPosition, boolean showArticle) {
        this.adapterPosition = adapterPosition;
        this.showArticle = showArticle;
    }

    public int getAdapterPosition() {
        return adapterPosition;
    }

    public boolean isShowArticle() {
        return showArticle;
    }
}
