package online.postboard.android.data.events;

import online.postboard.android.data.model.ArticleDTO;

/**
 * Created by Android SD-1 on 16-06-2017.
 */

public class ArticleCreatedEvent {

    private ArticleDTO articleDTO;

    public ArticleCreatedEvent(ArticleDTO articleDTO) {
        this.articleDTO = articleDTO;
    }


    public ArticleDTO getArticleDTO() {
        return articleDTO;
    }
}
