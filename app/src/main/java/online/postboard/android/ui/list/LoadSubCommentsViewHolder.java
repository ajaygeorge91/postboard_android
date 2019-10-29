package online.postboard.android.ui.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import online.postboard.android.data.model.SubCommentLoadMore;
import online.postboard.android.ui.base.AnyItemViewHolder;
import online.postboard.android.ui.base.ScreenType;
import online.postboard.android.util.RxEventBus;
import online.postboard.android.R;
import online.postboard.android.data.events.LoadMoreSubCommentButtonClickEvent;

import android.widget.TextView;

/**
 * Created by Android SD-1 on 21-04-2017.
 */

public class LoadSubCommentsViewHolder extends AnyItemViewHolder<SubCommentLoadMore> {


    @BindView(R.id.load_more_msg)
    TextView loadMoreMessage;

    public LoadSubCommentsViewHolder(ViewGroup parent, RxEventBus eventBus) {
        super(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_load_sub_comments, parent, false));
        ButterKnife.bind(this, itemView);
        loadMoreMessage.setOnClickListener(v -> {
            Answers.getInstance().logCustom(new CustomEvent("LoadSubCommentsClick"));
            eventBus.post(new LoadMoreSubCommentButtonClickEvent(getAdapterPosition(), LoadMoreSubCommentButtonClickEvent.ActionButton.LOAD_MORE));
        });
    }

    @Override
    public void bind(SubCommentLoadMore subCommentLoadMore, ScreenType screenType) {
        if (subCommentLoadMore.getMessage() != null && !subCommentLoadMore.getMessage().isEmpty()) {
            loadMoreMessage.setText(subCommentLoadMore.getMessage());
            loadMoreMessage.setVisibility(View.VISIBLE);
        } else {
            loadMoreMessage.setVisibility(View.GONE);
        }
    }


}
