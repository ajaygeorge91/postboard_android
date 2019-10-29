package online.postboard.android.ui.list;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import online.postboard.android.R;
import online.postboard.android.data.events.LoadCommentsEvent;
import online.postboard.android.data.model.CommentTypeButtons;
import online.postboard.android.ui.base.AnyItemViewHolder;
import online.postboard.android.ui.base.ScreenType;
import online.postboard.android.util.RxEventBus;

/**
 * Created by Android SD-1 on 21-04-2017.
 */

public class CommentTypeSelectorViewHolder extends AnyItemViewHolder<CommentTypeButtons> {

    @BindView(R.id.button_hot)
    TextView buttonHot;

    @BindView(R.id.button_new)
    TextView buttonNew;

    @BindView(R.id.button_hot_layout)
    LinearLayout buttonHot_layout;

    @BindView(R.id.button_new_layout)
    LinearLayout buttonNew_layout;

    private Context context;

    public CommentTypeSelectorViewHolder(ViewGroup parent, RxEventBus eventBus) {
        super(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment_type, parent, false));
        ButterKnife.bind(this, itemView);
        this.context = itemView.getContext();
        buttonHot.setOnClickListener(v -> {
            Answers.getInstance().logCustom(new CustomEvent("CommentsHotButtonClick"));
            eventBus.post(new LoadCommentsEvent(getAdapterPosition(), LoadCommentsEvent.CommentType.HOT));
        });
        buttonNew.setOnClickListener(v -> {
            Answers.getInstance().logCustom(new CustomEvent("CommentsNewButtonClick"));
            eventBus.post(new LoadCommentsEvent(getAdapterPosition(), LoadCommentsEvent.CommentType.NEW));
        });
    }

    @Override
    public void bind(CommentTypeButtons commentTypeButtons, ScreenType screenType) {
        if (commentTypeButtons.getCommentType().equals(LoadCommentsEvent.CommentType.HOT)) {
//            buttonHot_layout.setBackground(ContextCompat.getDrawable(context, R.drawable.dark_border_rounded));
            buttonHot.setTextColor(ContextCompat.getColor(context, R.color.primary_text));
//            buttonNew_layout.setBackground(ContextCompat.getDrawable(context, R.drawable.light_border_rounded));
            buttonNew.setTextColor(ContextCompat.getColor(context, R.color.secondary_text));
        } else {
//            buttonNew_layout.setBackground(ContextCompat.getDrawable(context, R.drawable.dark_border_rounded));
            buttonNew.setTextColor(ContextCompat.getColor(context, R.color.primary_text));
//            buttonHot_layout.setBackground(ContextCompat.getDrawable(context, R.drawable.light_border_rounded));
            buttonHot.setTextColor(ContextCompat.getColor(context, R.color.secondary_text));
        }
    }
}