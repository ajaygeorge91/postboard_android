package online.postboard.android.ui.list;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import online.postboard.android.data.model.UserDTO;
import online.postboard.android.ui.base.AnyItemViewHolder;
import online.postboard.android.ui.base.ScreenType;
import online.postboard.android.util.RxEventBus;
import online.postboard.android.util.widgets.CircularImageView;
import online.postboard.android.R;

import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Android SD-1 on 21-04-2017.
 */

public class UserProfileCardViewHolder extends AnyItemViewHolder<UserDTO> {

    @BindView(R.id.text_profile_username)
    TextView text_profile_username;

    @BindView(R.id.text_profile_about)
    TextView text_profile_about;

    @BindView(R.id.image_profile_user)
    CircularImageView image_profile_user;

    public UserProfileCardViewHolder(ViewGroup parent, RxEventBus eventBus) {
        super(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_profile_card, parent, false));
        ButterKnife.bind(this, itemView);
//        button_new_article.setOnClickListener(v -> {
//            Answers.getInstance().logCustom(new CustomEvent("NewArticleClick"));
//            eventBus.post(new CreateNewArticleEvent());
//        });
//        button_profile_logout.setOnClickListener(v -> {
//            Answers.getInstance().logCustom(new CustomEvent("LogoutButtonClick"));
//            eventBus.post(new LogOutButtonClickEvent());
//        });
    }

    @Override
    public void bind(UserDTO userDTO, ScreenType screenType) {
        text_profile_username.setText(userDTO.getFullName());

        Glide.with(itemView.getContext())
                .load(userDTO.getAvatarURL())
                .crossFade()
                .dontTransform()
                .into(image_profile_user);

    }


}
