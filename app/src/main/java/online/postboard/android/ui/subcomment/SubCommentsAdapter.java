package online.postboard.android.ui.subcomment;

import android.app.Activity;

import online.postboard.android.data.DataManager;
import online.postboard.android.data.model.AnyItem;
import online.postboard.android.data.model.CommentDTO;
import online.postboard.android.data.model.MyProgressBar;
import online.postboard.android.data.model.SubCommentDTO;
import online.postboard.android.ui.base.BaseAdapter;
import online.postboard.android.util.RxEventBus;

import java.util.List;

import javax.inject.Inject;

public class SubCommentsAdapter extends BaseAdapter {

    private static final int SUB_COMMENT_START_INDEX = 1;

    @Inject
    public SubCommentsAdapter(Activity activity, DataManager dataManager, RxEventBus eventBus) {
        super(activity, dataManager, eventBus);
        anyItemList.add(new MyProgressBar(""));
    }

    public void addSubComments(List<? extends AnyItem> anyItemList) {
            this.anyItemList.addAll(this.anyItemList.size() - 1, anyItemList);
            notifyDataSetChanged();
    }

    public void addComment(CommentDTO commentDTO) {
        this.anyItemList.add(this.anyItemList.size() - 1, commentDTO);
        notifyDataSetChanged();
    }

    public void addNewSubCommentToTop(SubCommentDTO subCommentDTO) {
        this.anyItemList.add(SUB_COMMENT_START_INDEX, subCommentDTO);
        notifyItemInserted(SUB_COMMENT_START_INDEX);
    }

}
