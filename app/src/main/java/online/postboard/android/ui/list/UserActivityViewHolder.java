package online.postboard.android.ui.list;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import online.postboard.android.data.model.UserActivityDTO;
import online.postboard.android.ui.base.AnyItemViewHolder;
import online.postboard.android.ui.base.ScreenType;
import online.postboard.android.util.RxEventBus;
import online.postboard.android.R;

import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Android SD-1 on 21-04-2017.
 */

public class UserActivityViewHolder extends AnyItemViewHolder<UserActivityDTO> {

    @BindView(R.id.message)
    TextView message;

    public UserActivityViewHolder(ViewGroup parent, RxEventBus eventBus) {
        super(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_activity, parent, false));
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bind(UserActivityDTO userActivityDTO, ScreenType screenType) {
        if (userActivityDTO.getNodeLabel() != null) {
            message.setText(userActivityDTO.getNodeLabel());
        } else {
            message.setText("no label...");
        }
    }

}