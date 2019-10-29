package online.postboard.android.ui.notifications;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import online.postboard.android.data.model.AnyItem;
import online.postboard.android.data.model.MyProgressBar;
import online.postboard.android.data.model.UserDTO;
import online.postboard.android.ui.base.BaseActivity;
import online.postboard.android.ui.detail.ArticleDetailActivity;
import online.postboard.android.util.widgets.EndlessRecyclerViewScrollListener;
import online.postboard.android.R;
import online.postboard.android.data.model.NotificationDTO;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotificationActivity extends BaseActivity implements NotificationMvpView {

    private UserDTO userDTO;

    @Inject
    NotificationPresenter notificationPresenter;
    @Inject
    NotificationAdapter notificationAdapter;

    @BindView(R.id.profile_list)
    RecyclerView profileList;

    private EndlessRecyclerViewScrollListener scrollListener;
    private LinearLayoutManager linearLayoutManager;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, NotificationActivity.class);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        notificationPresenter.attachView(this);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);

        linearLayoutManager = new LinearLayoutManager(this);
        profileList.setLayoutManager(linearLayoutManager);
        profileList.setAdapter(notificationAdapter);
        userDTO = notificationPresenter.getUserDTO();
        setupUserDto(userDTO);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        notificationPresenter.detachView();
    }

    @Override
    public void showCachedNotifications(List<NotificationDTO> responseDTO) {
        if (notificationAdapter.getItemCount() <= 1) {
            showNotifications(responseDTO);
        }
    }

    @Override
    public void showNotifications(List<NotificationDTO> notificationDTOList) {
        notificationAdapter.addNotifications(notificationDTOList);
        if (!notificationAdapter.getUnreadCount().isEmpty()) {
            notificationPresenter.readNotifications();
        } else {
            notificationPresenter.publishUnreadCount("");
        }
    }

    @Override
    public void updateNotifications(List<NotificationDTO> notificationDTOList) {
        notificationAdapter.updateNotifications(notificationDTOList);
        notificationPresenter.publishUnreadCount(notificationAdapter.getUnreadCount());
    }

    @Override
    public void showNotificationEmpty() {
        if (notificationAdapter.getItemCount() > 1) {
            notificationAdapter.setLoadingMessage(getString(R.string.empty_items));
        } else {
            notificationAdapter.setLoadingMessage(getString(R.string.nothing_to_load));
        }
    }

    @Override
    public void showError(String message) {
        notificationAdapter.setErrorMessage(message);
    }

    @Override
    public void setupUserDto(UserDTO userDTO) {
        if (userDTO != null) {
//            this.userDTO = userDTO;
            notificationPresenter.getCachedNotifications();
            scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {

                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    notificationPresenter.getMyNotifications(page);
                }
            };
            profileList.addOnScrollListener(scrollListener);
            //just giving a nudge
            scrollListener.onScrolled(profileList, 1, 0);

        } else {
            finish();
        }
    }

    @Override
    public List<NotificationDTO> getNotifications() {
        return notificationAdapter.getItems();
    }

    @Override
    public void clearItems() {
        notificationAdapter.removeItems();
    }

    @Override
    public void readAllNotifications() {
        notificationAdapter.readItems();
        notificationPresenter.publishUnreadCount("");
    }

    @Override
    public void continueListLoading() {
        MyProgressBar myProgressBar = notificationAdapter.getProgressObject();
        if (myProgressBar != null && myProgressBar.isShowRetryButton()) {
            notificationPresenter.getMyNotifications(scrollListener.getCurrentPage());
            notificationAdapter.resetProgressObject();
        }
    }

    @Override
    public void loadNodeActivity(int itemPosition) {
        AnyItem anyItem = notificationAdapter.getItem(itemPosition);
        if (anyItem instanceof NotificationDTO) {
            String nodeId = ((NotificationDTO) anyItem).getNodeIdOfInterest();
            ArticleDetailActivity.startArticleDetailActivity(this, nodeId);
        }
    }

    @Override
    public void signInView() {
        this.userDTO = null;
        notificationAdapter.removeItems();
        notificationPresenter.publishUnreadCount("");
        finish();
    }

    @OnClick(R.id.back_button_layout_notifications)
    public void backButtonClick(View view) {
        onBackPressed();
    }

}
