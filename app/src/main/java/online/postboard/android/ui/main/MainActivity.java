package online.postboard.android.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import online.postboard.android.R;
import online.postboard.android.data.MyMQTTService;
import online.postboard.android.data.events.CreateNewArticleEvent;
import online.postboard.android.data.model.AuthResponseDTO;
import online.postboard.android.data.model.UserDTO;
import online.postboard.android.ui.articlenew.NewArticleActivity;
import online.postboard.android.ui.base.BaseActivity;
import online.postboard.android.ui.detail.ArticleDetailActivity;
import online.postboard.android.ui.notifications.NotificationActivity;
import online.postboard.android.ui.search.SearchActivity;
import online.postboard.android.util.DialogFactory;
import online.postboard.android.util.widgets.CircularImageView;

public class MainActivity extends BaseActivity implements MainMvpView {

    @Inject
    MainPresenter mMainPresenter;

    @Inject
    FragmentPagerAdapter fragmentPagerAdapter;

    @BindView(R.id.appbar_main)
    AppBarLayout appbar_main;

    @BindView(R.id.main_container)
    ViewPager container;

    @BindView(R.id.action_profile_layout)
    RelativeLayout action_profile_layout;

    @BindView(R.id.action_notification_layout)
    RelativeLayout action_notification_layout;

    @BindView(R.id.action_search_layout)
    RelativeLayout action_search_layout;

    @BindView(R.id.action_home_layout)
    RelativeLayout action_home_layout;

    @BindView(R.id.notification_badge_tv)
    TextView notification_badge_tv;

    @BindView(R.id.action_profile)
    CircularImageView action_profile;

    @BindView(R.id.action_home)
    ImageButton action_home;

    private UserDTO userDTO;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        mMainPresenter.attachView(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.app_name));
        }


        mMainPresenter.getUserDTOFromServer();
        container.setAdapter(fragmentPagerAdapter);
//        setFragment(ArticleListFragment.newInstance(FragmentItem.ARTICLES));
        startService(MyMQTTService.getStartIntent(this));
        container.setOffscreenPageLimit(4);
        container.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //https://stackoverflow.com/questions/16074058/onpageselected-doesnt-work-for-first-page/34675705#34675705
            boolean first = true;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (first && positionOffset == 0 && positionOffsetPixels == 0) {
                    onPageSelected(0);
                    first = false;
                }
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case FragmentItem.ARTICLES:
                        setTint(action_home);
                        break;
//                    case FragmentItem.ARTICLES_NEW:
//                        setTint(action_home);
//                        break;
//                    case FragmentItem.NOTIFICATIONS:
//                        setTint(action_notification);
//                        break;
                    case FragmentItem.PROFILE:
                        setTint(action_profile);
                        break;
                }
                pauseResumeAction(position);
                appbar_main.setExpanded(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setTint(action_home);
        action_notification_layout.setVisibility(View.GONE);

        // deep linking and stuff
        final Intent intent = getIntent();
        final String action = intent.getAction();
        if (Intent.ACTION_VIEW.equals(action)) {
            if (intent.getData() != null) {
                final List<String> segments = intent.getData().getPathSegments();
                if (segments.size() >= 2) {
                    if (segments.get(0).equalsIgnoreCase("posts")) {
                        ArticleDetailActivity.startArticleDetailActivity(this, segments.get(1));
                    }
                }
            }
//            String segments = intent.getDataString();
//            Timber.d("ACTION_VIEW segments" + segments);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMainPresenter.detachView();
    }

    /*****
     * MVP View methods implementation
     ****/

    @Override
    public void showSignInSuccessful(AuthResponseDTO authResponseDTO) {
        setUserDto(authResponseDTO.getUserDTO());
    }

    @Override
    public void setUserDto(UserDTO userDTO) {
        this.userDTO = userDTO;
        action_notification_layout.setVisibility(View.VISIBLE);
        mMainPresenter.getNotificatoinsFromServer();
        if (userDTO.getAvatarURL() != null && !userDTO.getAvatarURL().isEmpty()) {
            Glide.with(this)
                    .load(userDTO.getAvatarURL())
                    .crossFade()
                    .dontTransform()
                    .into(action_profile);
        }
    }

    @Override
    public void clearUser() {
        this.userDTO = null;
        action_notification_layout.setVisibility(View.GONE);
        action_profile.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_action_avatar));
    }

    @Override
    public void updateNotificationCount(String unreadCountEvent) {
        runOnUiThread(() -> {
            if (unreadCountEvent.isEmpty() || unreadCountEvent.equalsIgnoreCase("0")) {
                notification_badge_tv.setVisibility(View.GONE);
            } else {
                notification_badge_tv.setText(unreadCountEvent);
                notification_badge_tv.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void newArticle(CreateNewArticleEvent createNewArticleEvent) {
        NewArticleActivity.startNewActivity(this);
    }

    @Override
    public void showError(String errorMessage) {
        DialogFactory.INSTANCE.createGenericErrorDialog(this, errorMessage)
                .show();
    }

    protected void setFragment(int fragmentItem) {
        container.setCurrentItem(fragmentItem);
    }

    @OnClick({R.id.action_notification_layout, R.id.action_profile_layout, R.id.action_home_layout, R.id.action_search_layout})
    public void onNavigationImageSelected(View view) {
        switch (view.getId()) {
            case R.id.action_profile_layout:
                setFragment(FragmentItem.PROFILE);
                setTint(action_profile);
                break;
            case R.id.action_home_layout:
                setFragment(FragmentItem.ARTICLES);
                setTint(action_home);
                break;
            case R.id.action_search_layout:
                SearchActivity.startArticleDetailActivity(this, "");
                break;
            case R.id.action_notification_layout:
                startActivity(NotificationActivity.getStartIntent(this));
        }
    }

    private void setTint(View view) {
        action_home.setColorFilter(ContextCompat.getColor(this, R.color.secondary_text));
        action_profile.setBorderColor(ContextCompat.getColor(this, R.color.secondary_text));

        if (view instanceof TextView) {
            ((TextView) view).setTextColor(ContextCompat.getColor(this, R.color.primary_text));
        }
        if (view instanceof ImageButton) {
            ((ImageButton) view).setColorFilter(ContextCompat.getColor(this, R.color.primary_text));
        }
        if (view instanceof CircularImageView) {
            ((CircularImageView) view).setBorderColor(ContextCompat.getColor(this, R.color.primary_text));
        }
    }

    private void pauseResumeAction(int position) {
        try {
            for (int i = 0; i < fragmentPagerAdapter.getCount(); i++) {
                if (i == position) {
                    fragmentPagerAdapter.getFragment(i).onResumeAction();
                } else {
                    fragmentPagerAdapter.getFragment(i).onPauseAction();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        for (int i = 0; i < fragmentPagerAdapter.getCount(); i++) {
            fragmentPagerAdapter.getFragment(i).onPauseAction();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (fragmentPagerAdapter != null && fragmentPagerAdapter.getFragment(container.getCurrentItem()) != null) {
            fragmentPagerAdapter.getFragment(container.getCurrentItem()).onResumeAction();
        }
    }

    @Override
    public void onBackPressed() {
        CustomFragment f = fragmentPagerAdapter.pagerFragmentReference.get(0);
        if (container.getCurrentItem() == 0) {
//            if (f == null) {
            super.onBackPressed();
//                return;
//            }
//            if (f instanceof ArticleListFragment) {
//                if (((ArticleListFragment) f).scrollUp()) {
//                    super.onBackPressed();
//                } else {
//                    ((ArticleListFragment) f).onRefresh();
//                }
//            }
        } else {
            container.setCurrentItem(0, true);
        }
    }


}
