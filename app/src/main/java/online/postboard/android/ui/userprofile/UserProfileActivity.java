package online.postboard.android.ui.userprofile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import online.postboard.android.data.model.PaginatedResult;
import online.postboard.android.data.model.UserArticleActivity;
import online.postboard.android.data.model.UserProfileBundleDTO;
import online.postboard.android.ui.detail.ArticleDetailActivity;
import online.postboard.android.util.apputils.ProfileUtils;
import online.postboard.android.util.widgets.CircularImageView;
import online.postboard.android.util.widgets.EndlessRecyclerViewScrollListener;
import online.postboard.android.R;
import online.postboard.android.data.events.ArticleActivityActionButtonClickEvent;
import online.postboard.android.data.events.ShowArticleDetailEvent;
import online.postboard.android.data.model.AnyItem;
import online.postboard.android.data.model.ArticleDTO;
import online.postboard.android.data.model.UserDTO;
import online.postboard.android.ui.base.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserProfileActivity extends BaseActivity implements UserProfileMvpView {

    private static final String USER_DTO = "_user_dto";
    public static final int ANIMATION_DURATION = 200;

    @Inject
    UserProfilePresenter userProfilePresenter;
    @Inject
    UserProfileAdapter userProfileAdapter;

    @BindView(R.id.toolbar_user_profile)
    Toolbar toolbar_user_profile;
    @BindView(R.id.back_button_user_profile)
    ImageButton back_button_user_profile;
    @BindView(R.id.toolbar_user_profile_layout)
    LinearLayout toolbar_user_profile_layout;
    @BindView(R.id.user_profile_image_toolbar)
    CircularImageView user_profile_image_toolbar;
    @BindView(R.id.user_name_profile_toolbar)
    TextView user_name_profile_toolbar;
    @BindView(R.id.recycler_view_user_profile)
    RecyclerView recycler_view_user_profile;

    private LinearLayoutManager linearLayoutManager;
    private EndlessRecyclerViewScrollListener scrollListener;

    private UserDTO userDTO;
    private boolean showToolbarProfile = true;

    public static Intent getStartIntent(Context context, UserDTO userDTO) {
        Intent intent = new Intent(context, UserProfileActivity.class);
        intent.putExtra(USER_DTO, userDTO);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityComponent().inject(this);
        userProfilePresenter.attachView(this);

        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);

        userDTO = (UserDTO) getIntent().getSerializableExtra(USER_DTO);
        setSupportActionBar(toolbar_user_profile);

        showToolbarUserProfile(false);
        setupUserDto(userDTO);
        userProfilePresenter.getUserProfile(userDTO.getId());

        linearLayoutManager = new LinearLayoutManager(this);
        recycler_view_user_profile.setLayoutManager(linearLayoutManager);
        recycler_view_user_profile.setAdapter(userProfileAdapter);
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager, 1) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                userProfilePresenter.getUserArticles(userDTO.getId(), page);
            }

            @Override
            public void onScrolled(RecyclerView view, int dx, int dy) {
                super.onScrolled(view, dx, dy);
                showToolbarUserProfile(linearLayoutManager.findFirstVisibleItemPosition() > 0);
            }
        };
        scrollListener.resetState();
        recycler_view_user_profile.addOnScrollListener(scrollListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userProfilePresenter.detachView();
    }

    private void showToolbarUserProfile(boolean showToolbarProfile) {
        if (this.showToolbarProfile != showToolbarProfile) {
            this.showToolbarProfile = showToolbarProfile;
            if (showToolbarProfile) {
                toolbar_user_profile_layout.animate().alpha(1).setDuration(ANIMATION_DURATION);
            } else {
                toolbar_user_profile_layout.animate().alpha(0).setDuration(ANIMATION_DURATION);
            }
        }
    }

    private void setupUserViews(UserDTO userDTO) {
        if (userDTO == null) {
            return;
        }
        UserDTO currentUserDto = userProfilePresenter.getCurrentUserDTO();
        if (currentUserDto != null && userDTO.getId().equalsIgnoreCase(currentUserDto.getId())) {
            //TODO
        }
    }

    @Override
    public void articleAction(ArticleActivityActionButtonClickEvent articleActivityActionButtonClickEvent) {
        userProfileAdapter.articleAction(articleActivityActionButtonClickEvent);
    }

    @Override
    public void articleDetailAction(ShowArticleDetailEvent showArticleDetailEvent) {
        AnyItem a = userProfileAdapter.getItem(showArticleDetailEvent.getAdapterPosition());
        if (a instanceof ArticleDTO) {
            ArticleDetailActivity.startArticleDetailActivity(this, ((ArticleDTO) a), showArticleDetailEvent.isShowArticle());
        }
    }

    @Override
    public void updateArticle(String articleID, UserArticleActivity userArticleActivity) {
        userProfileAdapter.updateArticleItem(articleID, userArticleActivity);
    }


    @Override
    public void setupUserDto(UserDTO userDTO) {
        if (userDTO == null) {
            Toast.makeText(this, "No user data", Toast.LENGTH_SHORT).show();
            return;
        }
        setupUserViews(userDTO);
        userProfileAdapter.addUserDTO(userDTO);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(userDTO.getFullName());
        }

        user_name_profile_toolbar.setText(userDTO.getFullName());
        String avatarUrl = ProfileUtils.getUserAvatarUrl(userDTO);
        Glide.with(this)
                .load(avatarUrl)
                .dontTransform()
                .into(user_profile_image_toolbar);
    }

    @Override
    public void setupUserProfileBundleDto(UserProfileBundleDTO userProfileBundleDTO) {
        if (userProfileBundleDTO.getUserDTO() != null) {
            this.userDTO = userProfileBundleDTO.getUserDTO();
            setupUserDto(userProfileBundleDTO.getUserDTO());
        }

        if (userProfileBundleDTO.getArticleList() != null
                && userProfileBundleDTO.getArticleList().getResult() != null
                && !userProfileBundleDTO.getArticleList().getResult().isEmpty()) {
            userProfileAdapter.addUserArticles(userProfileBundleDTO.getArticleList().getResult());
        } else {
            showEmpty();
        }
    }

    @Override
    public void showArticles(PaginatedResult<ArticleDTO> userActivityDTO) {
        userProfileAdapter.addUserArticles(userActivityDTO.getResult());
    }

    @Override
    public void showEmpty() {
        if (userProfileAdapter.getItemCount() > 1) {
            userProfileAdapter.setLoadingMessage(getString(R.string.empty_items));
        } else {
            userProfileAdapter.setLoadingMessage(getString(R.string.nothing_to_load));
        }
    }

    @Override
    public void showError(String message) {
        userProfileAdapter.setErrorMessage(message);
    }

    @OnClick(R.id.back_button_layout_user_profile)
    public void backButtonClick(View view) {
        onBackPressed();
    }

}
