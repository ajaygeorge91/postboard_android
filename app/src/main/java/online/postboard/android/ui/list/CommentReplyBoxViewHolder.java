package online.postboard.android.ui.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import online.postboard.android.data.events.ShowReplyBoxEvent;
import online.postboard.android.ui.base.AnyItemViewHolder;
import online.postboard.android.ui.base.ScreenType;
import online.postboard.android.util.RxEventBus;
import online.postboard.android.util.TouchFeedbackUtils;
import online.postboard.android.util.apputils.ProfileUtils;
import online.postboard.android.util.widgets.CircularImageView;
import online.postboard.android.R;
import online.postboard.android.data.model.CommentReplyBox;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Android SD-1 on 21-04-2017.
 */

public class CommentReplyBoxViewHolder extends AnyItemViewHolder<CommentReplyBox> {

    @BindView(R.id.image_user)
    CircularImageView userImage;

    @BindView(R.id.comment_reply_view)
    View comment_reply_view;

    @BindView(R.id.comment_send_button)
    ImageButton comment_send_button;

    public CommentReplyBoxViewHolder(ViewGroup parent, RxEventBus eventBus) {
        super(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment_reply_box, parent, false));
        ButterKnife.bind(this, itemView);
        comment_reply_view.setOnClickListener(v -> {
            Answers.getInstance().logCustom(new CustomEvent("CommentReplyViewClick"));
            TouchFeedbackUtils.vibrate(itemView.getContext());
            eventBus.post(new ShowReplyBoxEvent());
        });
        comment_send_button.setVisibility(View.GONE);
    }

    @Override
    public void bind(CommentReplyBox commentReplyBox, ScreenType screenType) {
        String avatarUrl = ProfileUtils.getUserAvatarUrl(commentReplyBox.getUserDTO());
        Glide.with(itemView.getContext())
                .load(avatarUrl)
                .crossFade()
                .into(userImage);
    }

}