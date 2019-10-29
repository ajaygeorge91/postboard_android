package online.postboard.android.ui.notifications;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import online.postboard.android.R;
import online.postboard.android.data.model.AnyItem;
import online.postboard.android.data.model.MyProgressBar;
import online.postboard.android.data.model.NotificationDTO;
import online.postboard.android.data.model.UserDTO;
import online.postboard.android.ui.detail.ArticleDetailActivity;
import online.postboard.android.ui.main.CustomFragment;
import online.postboard.android.ui.signin.SignInActivity;
import online.postboard.android.util.widgets.EndlessRecyclerViewScrollListener;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotificationFragment extends CustomFragment implements NotificationMvpView {

    private static final String ARG_USER_DTO = "_user_dto";

    private UserDTO userDTO;

    @Inject
    NotificationPresenter notificationPresenter;

    @Inject
    NotificationAdapter notificationAdapter;

    @BindView(R.id.profile_list)
    RecyclerView profileList;

    @BindView(R.id.layoutSignInPrompt)
    RelativeLayout layoutSignInPrompt;

    private EndlessRecyclerViewScrollListener scrollListener;
    private LinearLayoutManager linearLayoutManager;

    public NotificationFragment() {
        // Required empty public constructor
    }

    public static NotificationFragment newInstance(UserDTO userDTO) {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        if (userDTO != null)
            args.putSerializable(ARG_USER_DTO, userDTO);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentComponent().inject(this);
        if (getArguments() != null && getArguments().containsKey(ARG_USER_DTO)) {
            userDTO = (UserDTO) getArguments().getSerializable(ARG_USER_DTO);
        } else {
            userDTO = notificationPresenter.getUserDTO();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        notificationPresenter.attachView(this);
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        ButterKnife.bind(this, view);

        linearLayoutManager = new LinearLayoutManager(view.getContext());
        profileList.setLayoutManager(linearLayoutManager);
        profileList.setAdapter(notificationAdapter);
        setupUserDto(userDTO);
        return view;
    }

    @Override
    public void onResumeAction() {
        if (!notificationAdapter.getUnreadCount().isEmpty()) {
            notificationPresenter.readNotifications();
        }
    }

    @Override
    public void onPauseAction() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
        notificationPresenter.publishUnreadCount(notificationAdapter.getUnreadCount());
    }

    @Override
    public void updateNotifications(List<NotificationDTO> notificationDTOList) {
        notificationAdapter.updateNotifications(notificationDTOList);
        notificationPresenter.publishUnreadCount(notificationAdapter.getUnreadCount());
    }

    @Override
    public void showNotificationEmpty() {
        notificationAdapter.setLoadingMessage(getString(R.string.empty_items));
    }

    @Override
    public void showError(String message) {
        notificationAdapter.setErrorMessage(message);
    }

    @Override
    public void setupUserDto(UserDTO userDTO) {
        if (userDTO != null) {
            this.userDTO = userDTO;
            layoutSignInPrompt.setVisibility(View.GONE);
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
            signInView();
        }
    }

    @Override
    public List<NotificationDTO> getNotifications() {
        return notificationAdapter.getItems();
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
            ArticleDetailActivity.startArticleDetailActivity(getActivity(), nodeId);
        }
    }

    @Override
    public void clearItems() {
        notificationAdapter.removeItems();
    }

    @Override
    public void signInView() {
        this.userDTO = null;
        layoutSignInPrompt.setVisibility(View.VISIBLE);
        notificationAdapter.removeItems();
        notificationPresenter.publishUnreadCount("");
    }

    @OnClick(R.id.buttonSignIn)
    public void signInButtonClick(View view) {
        startActivity(SignInActivity.getCleanStartIntent(getActivity()));
    }

}
