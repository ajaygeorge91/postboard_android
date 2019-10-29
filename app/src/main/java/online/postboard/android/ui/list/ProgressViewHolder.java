package online.postboard.android.ui.list;

import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import online.postboard.android.data.events.ProgressRetryButtonClickEvent;
import online.postboard.android.data.model.MyProgressBar;
import online.postboard.android.ui.base.AnyItemViewHolder;

import online.postboard.android.R;
import online.postboard.android.ui.base.ScreenType;
import online.postboard.android.util.RxEventBus;
import android.widget.TextView;

/**
 * Created by Android SD-1 on 21-04-2017.
 */

public class ProgressViewHolder extends AnyItemViewHolder<MyProgressBar> {

    @BindView(R.id.pbHeaderProgress)
    ProgressBar pbHeaderProgress;

    @BindView(R.id.progressMessage)
    TextView progressMessage;

    @BindView(R.id.retryButton)
    Button retryButton;

    public ProgressViewHolder(ViewGroup parent, RxEventBus eventBus) {
        super(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.progress_bar, parent, false));
        ButterKnife.bind(this, itemView);
        retryButton.setOnClickListener(v -> {
            Answers.getInstance().logCustom(new CustomEvent("RetryButtonClick"));
            eventBus.post(new ProgressRetryButtonClickEvent());
        });
    }

    @Override
    public void bind(MyProgressBar myProgressBar, ScreenType screenType) {
        if (myProgressBar.getMessage() != null && myProgressBar.getMessage().isEmpty()) {
            progressMessage.setText(myProgressBar.getMessage());
            progressMessage.setVisibility(View.GONE);
            pbHeaderProgress.setVisibility(View.VISIBLE);
        } else {
            progressMessage.setText(myProgressBar.getMessage());
            progressMessage.setVisibility(View.VISIBLE);
            pbHeaderProgress.setVisibility(View.GONE);
        }
        if (myProgressBar.getMessage().equalsIgnoreCase(itemView.getContext().getString(R.string.nothing_to_load))) {
            progressMessage.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.gray));
        }

        if (myProgressBar.isShowRetryButton()) {
            retryButton.setVisibility(View.VISIBLE);
        } else {
            retryButton.setVisibility(View.GONE);
        }
    }

}