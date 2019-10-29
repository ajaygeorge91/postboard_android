package online.postboard.android.ui.notifications;

import online.postboard.android.data.model.UserDTO;
import online.postboard.android.ui.base.MvpView;
import online.postboard.android.data.model.NotificationDTO;

import java.util.List;

public interface NotificationMvpView extends MvpView {

    void showNotifications(List<NotificationDTO> notificationDTOList);

    void updateNotifications(List<NotificationDTO> notificationDTOList);

    void showNotificationEmpty();

    void showError(String message);

    void setupUserDto(UserDTO userDTO);

    void signInView();

    void loadNodeActivity(int itemPosition);

    void continueListLoading();

    void readAllNotifications();

    List<NotificationDTO> getNotifications();

    void clearItems();

    void showCachedNotifications(List<NotificationDTO> responseDTO);
}
