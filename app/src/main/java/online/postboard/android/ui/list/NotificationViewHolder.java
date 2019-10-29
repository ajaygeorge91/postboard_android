package online.postboard.android.ui.list;

import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import online.postboard.android.data.events.LoadNodeEvent;
import online.postboard.android.ui.base.AnyItemViewHolder;
import online.postboard.android.ui.base.ScreenType;
import online.postboard.android.R;
import online.postboard.android.data.model.NotificationDTO;
import online.postboard.android.util.RxEventBus;
import online.postboard.android.util.StringUtils;

import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Android SD-1 on 21-04-2017.
 */

public class NotificationViewHolder extends AnyItemViewHolder<NotificationDTO> {

    @BindView(R.id.messageTitle)
    TextView messageTitle;

    @BindView(R.id.notification_item_layout)
    LinearLayout notification_item_layout;

    @BindView(R.id.messageContent)
    TextView messageContent;

    @BindView(R.id.messageTime)
    TextView messageTime;

    public NotificationViewHolder(ViewGroup parent, RxEventBus eventBus) {
        super(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false));
        ButterKnife.bind(this, itemView);
        notification_item_layout.setOnClickListener(v -> {
            Answers.getInstance().logCustom(new CustomEvent("NotificationItemClick"));
            eventBus.post(new LoadNodeEvent(getAdapterPosition()));
        });
    }

    @Override
    public void bind(NotificationDTO notificationDTO, ScreenType screenType) {
        if (notificationDTO.getContent() != null) {
            messageTitle.setText(notificationDTO.getContent());
            messageContent.setText(notificationDTO.getSubContent());
            messageTime.setText(StringUtils.INSTANCE.getDisplayableTime(notificationDTO.getUpdatedAt()));

            if (!notificationDTO.isRead()) {
                notification_item_layout.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.color.white));
            } else {
                notification_item_layout.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.color.common_bg));
            }
        } else {
            messageTitle.setText("no content...");
        }
    }

}