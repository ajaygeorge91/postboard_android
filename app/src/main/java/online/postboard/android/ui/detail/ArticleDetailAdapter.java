package online.postboard.android.ui.detail;

import android.app.Activity;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import online.postboard.android.data.DataManager;
import online.postboard.android.data.events.LoadCommentsEvent;
import online.postboard.android.data.model.AnyItem;
import online.postboard.android.data.model.ArticleDTO;
import online.postboard.android.data.model.CommentDTO;
import online.postboard.android.data.model.CommentReplyBox;
import online.postboard.android.data.model.CommentTypeButtons;
import online.postboard.android.data.model.MyProgressBar;
import online.postboard.android.data.model.PaginatedResult;
import online.postboard.android.data.model.SubCommentDTO;
import online.postboard.android.data.model.SubCommentLoadMore;
import online.postboard.android.ui.base.BaseAdapter;
import online.postboard.android.ui.base.ScreenType;
import online.postboard.android.util.RxEventBus;

public class ArticleDetailAdapter extends BaseAdapter {

    private static final int COMMENT_START_INDEX = 2;
    private static final int COMMENT_TYPE_BUTTON_INDEX = 0;
    private static final int COMMENT_START_INDEX_SINGLE_PAGE_VIEW = 3;
    private static final int COMMENT_TYPE_BUTTON_INDEX_SINGLE_PAGE_VIEW = 1;

    private int commentStartIndex = COMMENT_START_INDEX;
    private int commentTypeButtonIndex = COMMENT_TYPE_BUTTON_INDEX;

    @Inject
    public ArticleDetailAdapter(Activity activity, DataManager dataManager, RxEventBus eventBus) {
        super(activity, dataManager, eventBus, ScreenType.ARTICLE_DETAIL);
        anyItemList.add(new MyProgressBar(""));
        notifyDataSetChanged();
    }

    public void clearAllComments() {
        anyItemList.subList(commentStartIndex, anyItemList.size()).clear();
        anyItemList.add(new MyProgressBar(""));
        notifyDataSetChanged();
    }

    public void addComments(List<? extends AnyItem> anyItemList) {
        int start = this.anyItemList.size();
        for (AnyItem item : anyItemList) {
            this.anyItemList.add(this.anyItemList.size() - 1, item);
            if (item instanceof CommentDTO) {
                PaginatedResult<SubCommentDTO> subCommentDTOP = ((CommentDTO) item).getSubCommentListP();
                for (SubCommentDTO subCommentDTO : subCommentDTOP.getResult()) {
                    this.anyItemList.add(this.anyItemList.size() - 1, subCommentDTO);
                }
                if (subCommentDTOP.getTotalCount() != subCommentDTOP.getResult().size()) {
                    this.anyItemList.add(this.anyItemList.size() - 1, new SubCommentLoadMore(
                            ((CommentDTO) item).getId(),
                            String.format(Locale.ENGLISH, "Load all %d comments", subCommentDTOP.getTotalCount()),
                            ((CommentDTO) item)
                    ));
                }
            }
        }
        notifyItemRangeInserted(start, this.anyItemList.size() - 1);
    }

    public void addSubComment(String commentId, SubCommentDTO subCommentDTO) {
        for (int i = 0; i < anyItemList.size(); i++) {
            AnyItem item= anyItemList.get(i);
            if (item instanceof CommentDTO && ((CommentDTO) item).getId().equalsIgnoreCase(commentId)) {
                this.anyItemList.add(i+1, subCommentDTO);
                break;
            }
        }
        notifyDataSetChanged();
    }

    public void addArticle(ArticleDTO articleDTO, boolean showArticle) {
        int start = this.anyItemList.size();
        if (showArticle) {
            this.anyItemList.add(this.anyItemList.size() - 1, articleDTO);
            this.commentStartIndex = COMMENT_START_INDEX_SINGLE_PAGE_VIEW;
            this.commentTypeButtonIndex = COMMENT_TYPE_BUTTON_INDEX_SINGLE_PAGE_VIEW;
        } else {
            this.commentStartIndex = COMMENT_START_INDEX;
            this.commentTypeButtonIndex = COMMENT_TYPE_BUTTON_INDEX;
        }

        LoadCommentsEvent.CommentType cType = LoadCommentsEvent.CommentType.HOT;
        if (articleDTO.getComments() != null &&
                articleDTO.getComments().getOrderBy() != null &&
                articleDTO.getComments().getOrderBy().equalsIgnoreCase("new")) {
            cType = LoadCommentsEvent.CommentType.NEW;
        }
        this.anyItemList.add(this.anyItemList.size() - 1, new CommentTypeButtons(cType));
        this.anyItemList.add(this.anyItemList.size() - 1, new CommentReplyBox(articleDTO.getCreatedBy()));

        notifyItemRangeInserted(start, this.anyItemList.size() - 1);
    }

    public void addNewCommentToTop(CommentDTO commentDTO) {
        this.anyItemList.add(commentStartIndex, commentDTO);
        notifyItemInserted(commentStartIndex);
    }

    public void setCommentType(LoadCommentsEvent.CommentType commentType) {
        this.anyItemList.set(commentTypeButtonIndex, new CommentTypeButtons(commentType));
        notifyItemChanged(commentTypeButtonIndex);
    }

}
