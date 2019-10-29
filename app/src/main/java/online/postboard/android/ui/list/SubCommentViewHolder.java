package online.postboard.android.ui.list;

import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import online.postboard.android.data.events.SubCommentActionButtonClickEvent;
import online.postboard.android.data.model.SubCommentDTO;
import online.postboard.android.ui.base.AnyItemViewHolder;
import online.postboard.android.ui.base.ScreenType;
import online.postboard.android.util.RxEventBus;
import online.postboard.android.util.TouchFeedbackUtils;
import online.postboard.android.util.apputils.ProfileUtils;
import online.postboard.android.util.widgets.CircularImageView;
import online.postboard.android.util.widgets.ExpandableTextView;

import online.postboard.android.R;
import online.postboard.android.util.StringUtils;

/**
 * Created by Android SD-1 on 21-04-2017.
 */

public class SubCommentViewHolder extends AnyItemViewHolder<SubCommentDTO> {

    @BindView(R.id.image_sub_comment_user)
    CircularImageView commentUserImage;
    @BindView(R.id.text_sub_comment_content)
    ExpandableTextView contentTextView;
    @BindView(R.id.text_username_sc)
    TextView usernameScTextView;
    @BindView(R.id.text_time_sc)
    TextView timeScTextView;
    @BindView(R.id.image_view_like_sub_comment)
    ImageButton like;
    @BindView(R.id.image_view_dislike_sub_comment)
    ImageButton dislike;
    @BindView(R.id.rating_sum_sub_comment)
    TextView ratingSum;

    public SubCommentViewHolder(ViewGroup parent, RxEventBus eventBus) {
        super(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sub_comment, parent, false));
        ButterKnife.bind(this, itemView);
        like.setOnClickListener(v -> {
            Answers.getInstance().logCustom(new CustomEvent("SubCommentLikeClick"));
            TouchFeedbackUtils.vibrate(itemView.getContext());
            eventBus.post(new SubCommentActionButtonClickEvent(getAdapterPosition(), SubCommentActionButtonClickEvent.ActionButton.LIKE));
        });
        dislike.setOnClickListener(v -> {
            Answers.getInstance().logCustom(new CustomEvent("SubCommentDislikeClick"));
            TouchFeedbackUtils.vibrate(itemView.getContext());
            eventBus.post(new SubCommentActionButtonClickEvent(getAdapterPosition(), SubCommentActionButtonClickEvent.ActionButton.DISLIKE));
        });
    }

    public void bind(final SubCommentDTO subCommentDTO, ScreenType screenType) {

        String avatarUrl = ProfileUtils.getUserAvatarUrl(subCommentDTO.getCreatedBy());
        Glide.with(itemView.getContext())
                .load(avatarUrl)
                .crossFade()
                .into(commentUserImage);

        contentTextView.setText(subCommentDTO.getContent());
        usernameScTextView.setText(subCommentDTO.getCreatedBy().getFullName());
        timeScTextView.setText(StringUtils.INSTANCE.getDisplayableTime(subCommentDTO.getCreatedAt()));

        if (subCommentDTO.getUserSubCommentActivity() != null) {
            if (subCommentDTO.getUserSubCommentActivity().getRating() == 1) {
                like.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.action));
                dislike.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.info_icon_color_light));
            } else if (subCommentDTO.getUserSubCommentActivity().getRating() == -1) {
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

        if (subCommentDTO.getSubCommentMeta() != null) {
            ratingSum.setText(String.format(Locale.ENGLISH, "%d", subCommentDTO.getSubCommentMeta().getRatingSum()));
        }

    }

}
