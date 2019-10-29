package online.postboard.android.ui.list;

import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import online.postboard.android.data.events.CommentReplyButtonClickEvent;
import online.postboard.android.data.model.CommentDTO;
import online.postboard.android.ui.base.ScreenType;
import online.postboard.android.util.TouchFeedbackUtils;
import online.postboard.android.util.apputils.ProfileUtils;
import online.postboard.android.util.widgets.CircularImageView;

import online.postboard.android.R;
import online.postboard.android.data.events.CommentActionButtonClickEvent;
import online.postboard.android.ui.base.AnyItemViewHolder;
import online.postboard.android.util.RxEventBus;
import online.postboard.android.util.StringUtils;
import online.postboard.android.util.widgets.ExpandableTextView;

/**
 * Created by Android SD-1 on 21-04-2017.
 */

public class CommentViewHolder extends AnyItemViewHolder<CommentDTO> {

    @BindView(R.id.image_comment_user)
    CircularImageView commentUserImage;
    @BindView(R.id.text_comment_content)
    ExpandableTextView contentTextView;
    @BindView(R.id.text_username)
    TextView usernameTextView;
    @BindView(R.id.text_time)
    TextView timeTextView;
    @BindView(R.id.image_view_like_comment)
    ImageButton like;
    @BindView(R.id.image_view_dislike_comment)
    ImageButton dislike;
    @BindView(R.id.rating_sum_comment)
    TextView ratingSum;
    @BindView(R.id.button_reply_comment)
    TextView button_reply_comment;
    @BindView(R.id.item_node_label_comment)
    RelativeLayout item_node_label_comment;


    public CommentViewHolder(ViewGroup parent, RxEventBus eventBus) {
        super(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false));
        ButterKnife.bind(this, itemView);
        like.setOnClickListener(v -> {
            Answers.getInstance().logCustom(new CustomEvent("CommentLikeClick"));
            TouchFeedbackUtils.vibrate(itemView.getContext());
            eventBus.post(new CommentActionButtonClickEvent(getAdapterPosition(), CommentActionButtonClickEvent.ActionButton.LIKE));
        });
        dislike.setOnClickListener(v -> {
            Answers.getInstance().logCustom(new CustomEvent("CommentDislikeClick"));
            TouchFeedbackUtils.vibrate(itemView.getContext());
            eventBus.post(new CommentActionButtonClickEvent(getAdapterPosition(), CommentActionButtonClickEvent.ActionButton.DISLIKE));
        });
        button_reply_comment.setOnClickListener(v -> {
            Answers.getInstance().logCustom(new CustomEvent("CommentReplyClick"));
            TouchFeedbackUtils.vibrate(itemView.getContext());
            eventBus.post(new CommentReplyButtonClickEvent(getAdapterPosition()));
        });
    }

    public void bind(final CommentDTO commentDTO, ScreenType screenType) {

        if (commentDTO.getCreatedBy() != null) {
            String avatarUrl = ProfileUtils.getUserAvatarUrl(commentDTO.getCreatedBy());
            Glide.with(itemView.getContext())
                    .load(avatarUrl)
                    .crossFade()
                    .into(commentUserImage);
            usernameTextView.setText(commentDTO.getCreatedBy().getFullName());
        }

        contentTextView.setText(commentDTO.getContent());
        timeTextView.setText(StringUtils.INSTANCE.getDisplayableTime(commentDTO.getCreatedAt()));

        if (commentDTO.getUserCommentActivity() != null) {
            if (commentDTO.getUserCommentActivity().getRating() == 1) {
                like.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.action));
                dislike.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.info_icon_color_light));
            } else if (commentDTO.getUserCommentActivity().getRating() == -1) {
                dislike.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.action));
                like.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.info_icon_color_light));
            } else {
                dislike.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.info_icon_color_light));
                like.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.info_icon_color_light));
            }
        } else {
            dislike.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.info_icon_color_light));
            like.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.info_icon_color_light));
        }

        if (commentDTO.getCommentMeta() != null) {
            ratingSum.setText(String.format(Locale.ENGLISH, "%d", commentDTO.getCommentMeta().getRatingSum()));
        }

        if (commentDTO.getNodeLabel() != null) {
            item_node_label_comment.setVisibility(View.VISIBLE);
        } else {
            item_node_label_comment.setVisibility(View.GONE);
        }

    }

}