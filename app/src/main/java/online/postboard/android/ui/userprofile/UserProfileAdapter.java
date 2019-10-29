package online.postboard.android.ui.userprofile;

import android.app.Activity;

import java.util.List;

import javax.inject.Inject;

import online.postboard.android.data.DataManager;
import online.postboard.android.data.model.ArticleDTO;
import online.postboard.android.data.model.MyProgressBar;
import online.postboard.android.data.model.UserActivityDTO;
import online.postboard.android.data.model.UserDTO;
import online.postboard.android.ui.base.BaseAdapter;
import online.postboard.android.ui.base.ScreenType;
import online.postboard.android.util.RxEventBus;

public class UserProfileAdapter extends BaseAdapter {

    @Inject
    public UserProfileAdapter(Activity activity, DataManager dataManager, RxEventBus eventBus) {
        super(activity, dataManager, eventBus, ScreenType.USER_PROFILE);
        anyItemList.add(new MyProgressBar(""));
    }

    public void addUserDTO(UserDTO userDTO) {
        if (this.anyItemList.size() > 0 && this.anyItemList.get(0) instanceof UserDTO) {
            this.anyItemList.set(0, userDTO);
        } else {
            this.anyItemList.add(0, userDTO);
        }
        notifyDataSetChanged();
    }

    public void addNewArticleOnTop(ArticleDTO articleDTO) {
        if (this.anyItemList.size() > 0 && this.anyItemList.get(0) instanceof UserDTO) {
            this.anyItemList.set(1, articleDTO);
        } else {
            this.anyItemList.add(0, articleDTO);
        }
        notifyDataSetChanged();
    }

    public void addUserActivities(List<UserActivityDTO> userActivityDTOList, UserDTO userDTO) {
        this.anyItemList.addAll(this.anyItemList.size() - 1, userActivityDTOList);
        notifyDataSetChanged();
    }

    public void addUserArticles(List<ArticleDTO> articleDTOs) {
        this.anyItemList.addAll(this.anyItemList.size() - 1, articleDTOs);
        notifyDataSetChanged();
    }

    public void removeArticles(UserDTO userDTO) {
        anyItemList.clear();
        anyItemList.add(new MyProgressBar(""));
        addUserDTO(userDTO);
        notifyDataSetChanged();
    }

}
