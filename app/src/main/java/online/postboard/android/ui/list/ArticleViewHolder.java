package online.postboard.android.ui.list;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import online.postboard.android.R;
import online.postboard.android.data.events.ArticleActivityActionButtonClickEvent;
import online.postboard.android.data.events.ShowArticleDetailEvent;
import online.postboard.android.data.events.ShowUserProfileEvent;
import online.postboard.android.data.model.ArticleDTO;
import online.postboard.android.ui.base.AnyItemViewHolder;
import online.postboard.android.ui.base.ScreenType;
import online.postboard.android.util.Constants;
import online.postboard.android.util.DimenUtils;
import online.postboard.android.util.RxEventBus;
import online.postboard.android.util.StringUtils;
import online.postboard.android.util.TouchFeedbackUtils;
import online.postboard.android.util.apputils.ArticleUtils;
import online.postboard.android.util.apputils.ProfileUtils;
import online.postboard.android.util.widgets.CircularImageView;
import online.postboard.android.util.widgets.PositionedCropTransformation;

/**
 * Created by Android SD-1 on 21-04-2017.
 */

public class ArticleViewHolder extends AnyItemViewHolder<ArticleDTO> {

    @BindView(R.id.item_article_area)
    LinearLayout item_article_area;
    @BindView(R.id.layout_status_comment)
    LinearLayout layoutStatusComment;
    @BindView(R.id.article_id)
    TextView article_id;
    @BindView(R.id.text_content_post)
    TextView text_content_post;
    @BindView(R.id.image_article)
    ImageView articleImage;
    @BindView(R.id.text_title)
    TextView titleTextView;
    @BindView(R.id.text_link_layout)
    LinearLayout text_link_layout;
    @BindView(R.id.image_link)
    ImageView image_link;
    @BindView(R.id.text_link)
    TextView text_link;
    @BindView(R.id.text_content)
    TextView text_content;
    @BindView(R.id.text_created_by)
    TextView emailTextView;
    @BindView(R.id.text_created_time)
    TextView text_created_time;
    @BindView(R.id.image_view_like)
    ImageButton like;
    @BindView(R.id.image_view_dislike)
    ImageButton dislike;
    @BindView(R.id.rating_sum)
    TextView ratingSum;
    @BindView(R.id.comment_sum)
    TextView commentSum;
    @BindView(R.id.image_article_user)
    CircularImageView image_article_user;
    @BindView(R.id.article_user_detail_header)
    LinearLayout article_user_detail_header;

    public ArticleViewHolder(ViewGroup parent, RxEventBus eventBus, ScreenType screenType) {
        super(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_article, parent, false));
        ButterKnife.bind(this, itemView);
        like.setOnClickListener(v -> {
            Answers.getInstance().logCustom(new CustomEvent("ArticleLikeClick"));
            TouchFeedbackUtils.vibrateStrong(itemView.getContext());
            eventBus.post(new ArticleActivityActionButtonClickEvent(getAdapterPosition(), ArticleActivityActionButtonClickEvent.ActionButton.LIKE));
        });
        dislike.setOnClickListener(v -> {
            Answers.getInstance().logCustom(new CustomEvent("ArticleDislikeClick"));
            TouchFeedbackUtils.vibrateStrong(itemView.getContext());
            eventBus.post(new ArticleActivityActionButtonClickEvent(getAdapterPosition(), ArticleActivityActionButtonClickEvent.ActionButton.DISLIKE));
        });

        if (screenType == ScreenType.USER_PROFILE) {
            article_user_detail_header.setBackground(new StateListDrawable());
        } else {
            article_user_detail_header.setOnClickListener(v -> {
                TouchFeedbackUtils.vibrate(itemView.getContext());
                eventBus.post(new ShowUserProfileEvent(getAdapterPosition()));
            });
        }

        int maxLength = Constants.INSTANCE.getMAX_LENGTH_CONTENT_TEXT();
        if (screenType == ScreenType.ARTICLE_DETAIL) {
            maxLength = Integer.MAX_VALUE;
            text_content.setMaxLines(maxLength);
            text_content_post.setMaxLines(maxLength);
            layoutStatusComment.setBackground(new StateListDrawable());
        } else {
            layoutStatusComment.setOnClickListener(v -> {
                TouchFeedbackUtils.vibrate(itemView.getContext());
                eventBus.post(new ShowArticleDetailEvent(getAdapterPosition()));
                Answers.getInstance().logCustom(new CustomEvent("ArticleClick").putCustomAttribute("from", "layoutStatusComment"));
            });
            item_article_area.setOnClickListener(v -> {
                TouchFeedbackUtils.vibrate(itemView.getContext());
                eventBus.post(new ShowArticleDetailEvent(getAdapterPosition(), true));
                Answers.getInstance().logCustom(new CustomEvent("ArticleClick").putCustomAttribute("from", "item_article_card"));
            });
            maxLength = Constants.INSTANCE.getMAX_LENGTH_CONTENT_TEXT();
        }
        text_content.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        text_content_post.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
    }

    public void bind(ArticleDTO articleDTO, ScreenType screenType) {
        if (screenType == ScreenType.ARTICLE_DETAIL) {
            bind(articleDTO, false);
        } else {
            bind(articleDTO, true);
        }
    }

    public void bind(ArticleDTO articleDTO, boolean listView) {

        article_id.setText(articleDTO.getId());

        boolean articleTruncated = false;
        boolean simplePost = false;
        int artHeight = 0;
        Point dispPoint = DimenUtils.getRealScreenSize(itemView.getContext());
        int screenWidth = dispPoint.x;
        int screenHeight = dispPoint.y;

        if (articleDTO.getImage() != null &&
                articleDTO.getImage().getImageURL() != null &&
                !articleDTO.getImage().getImageURL().isEmpty()) {

            int viewWidth = screenWidth
                    - itemView.getContext().getResources().getDimensionPixelSize(R.dimen.article_padding)
                    - itemView.getContext().getResources().getDimensionPixelSize(R.dimen.article_padding);

            artHeight = ArticleUtils.getHeight(viewWidth, articleDTO.getImage().getHeightByWidth());
            int minHeight = ((artHeight > screenHeight) && listView) ? screenHeight : artHeight;
            articleTruncated = (artHeight > screenHeight);

            articleImage.setMinimumHeight(minHeight);
            Glide.with(itemView.getContext())
                    .load(articleDTO.getImage().getImageURL())
                    .override(viewWidth, minHeight)
                    .transform(new PositionedCropTransformation(itemView.getContext(), 1, 0))
                    .into(articleImage);

            articleImage.setVisibility(View.VISIBLE);
        } else {
            articleImage.setVisibility(View.GONE);
        }

        if (articleDTO.getCreatedBy() != null) {
            article_user_detail_header.setVisibility(View.VISIBLE);
            String avatarUrl = ProfileUtils.getUserAvatarUrl(articleDTO.getCreatedBy());
            Glide.with(itemView.getContext())
                    .load(avatarUrl)
                    .crossFade()
                    .dontTransform()
                    .into(image_article_user);
            emailTextView.setText(String.format("%s",
                    articleDTO.getCreatedBy().getFullName()));
            String time = StringUtils.INSTANCE.getDisplayableTime(articleDTO.getCreatedAt());
            if (time.isEmpty()) {
                text_created_time.setVisibility(View.GONE);
            } else {
                text_created_time.setVisibility(View.VISIBLE);
                text_created_time.setText(String.format("%s",
                        time));
            }
        } else {
            article_user_detail_header.setVisibility(View.GONE);
        }

        if (articleDTO.getTitle() == null || articleDTO.getTitle().isEmpty()) {
            titleTextView.setVisibility(View.GONE);
        } else {
            titleTextView.setVisibility(View.VISIBLE);
            titleTextView.setText(articleDTO.getTitle());
        }

        if (articleDTO.getArticleType() != null &&
                !articleDTO.getArticleType().isEmpty() &&
                articleDTO.getArticleType().equalsIgnoreCase(Constants.INSTANCE.getARTICLE_TYPE_POST())) {
            simplePost = true;
        } else {
            simplePost = false;
        }


//        if ((listView && articleTruncated) || articleDTO.getContent() == null || articleDTO.getContent().isEmpty()) {
        if (articleDTO.getContent() == null || articleDTO.getContent().isEmpty()) {
            text_content.setVisibility(View.GONE);
            text_content_post.setVisibility(View.GONE);
        } else {
            if (simplePost) {
                text_content_post.setText(articleDTO.getContent());
                text_content_post.setVisibility(View.VISIBLE);
                text_content.setVisibility(View.GONE);
            } else {
                text_content.setText(articleDTO.getContent());
                text_content.setVisibility(View.VISIBLE);
                text_content_post.setVisibility(View.GONE);
            }

        }

        if (articleDTO.getLink() == null || articleDTO.getLink().isEmpty()) {
            text_link_layout.setVisibility(View.GONE);
        } else {
            text_link_layout.setVisibility(View.VISIBLE);
            Glide.with(itemView.getContext())
                    .load(StringUtils.INSTANCE.getFavicon(articleDTO.getLink()))
                    .into(image_link);
            text_link.setText(StringUtils.INSTANCE.getCanonicalPage(articleDTO.getLink()));
        }


        if (articleDTO.getUserArticleActivity() != null) {
            if (articleDTO.getUserArticleActivity().getRating() == 1) {
                like.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.action));
                dislike.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.info_icon_color_light));
            } else if (articleDTO.getUserArticleActivity().getRating() == -1) {
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

        if (articleDTO.getArticleMeta() != null) {
            ratingSum.setText(String.valueOf(articleDTO.getArticleMeta().getRatingSum()));
            commentSum.setText(String.valueOf(articleDTO.getArticleMeta().getCommentCount()));
        }

    }

    @OnClick(R.id.share_button)
    public void shareButtonClick(View view) {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, Constants.INSTANCE.getArticleLink(article_id.getText().toString()));
            itemView.getContext().startActivity(Intent.createChooser(shareIntent, "Share"));

        } catch (Exception e) {
            //e.toString();
        }

    }


}