package online.postboard.android.ui.myprofile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import online.postboard.android.R;
import online.postboard.android.data.model.UserDTO;
import online.postboard.android.ui.main.CustomFragment;
import online.postboard.android.ui.signin.SignInActivity;
import online.postboard.android.util.widgets.CircularImageView;

public class MyProfileFragment extends CustomFragment implements MyProfileMvpView {

    private static final String ARG_USER_DTO = "_user_dto";

    private UserDTO userDTO;

    @Inject
    ProfileListPresenter profileListPresenter;

    @BindView(R.id.layoutSignInPrompt)
    RelativeLayout layoutSignInPrompt;

    @BindView(R.id.text_profile_username)
    TextView text_profile_username;

    @BindView(R.id.image_profile_user)
    CircularImageView image_profile_user;

//    private EndlessRecyclerViewScrollListener scrollListener;
//    private LinearLayoutManager linearLayoutManager;

    public MyProfileFragment() {
    }

    public static MyProfileFragment newInstance() {
        MyProfileFragment fragment = new MyProfileFragment();
        Bundle args = new Bundle();
//        if (userDTO != null)
//            args.putSerializable(ARG_USER_DTO, userDTO);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentComponent().inject(this);
//        if (getArguments() != null && getArguments().containsKey(ARG_USER_DTO)) {
//            userDTO = (UserDTO) getArguments().getSerializable(ARG_USER_DTO);
//        } else {
//            userDTO = profileListPresenter.getUserDTO();
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        profileListPresenter.attachView(this);
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);

//        linearLayoutManager = new LinearLayoutManager(view.getContext());
//        profileList.setLayoutManager(linearLayoutManager);
//        profileList.setAdapter(myProfileAdapter);
        userDTO = profileListPresenter.getUserDTO();
        setupUserDto(userDTO);
        return view;
    }

//    @Override
//    public void articleAction(ArticleActivityActionButtonClickEvent articleActivityActionButtonClickEvent) {
//        myProfileAdapter.articleAction(articleActivityActionButtonClickEvent);
//    }
//
//    @Override
//    public void articleDetailAction(ShowArticleDetailEvent showArticleDetailEvent) {
//        AnyItem a = myProfileAdapter.getItem(showArticleDetailEvent.getAdapterPosition());
//        if (a instanceof ArticleDTO) {
//            ArticleDetailActivity.startArticleDetailActivity(getActivity(), ((ArticleDTO) a), showArticleDetailEvent.isShowArticle());
//        }
//    }
//
//    @Override
//    public void continueListLoading() {
//        MyProgressBar myProgressBar = myProfileAdapter.getProgressObject();
//        if (myProgressBar != null && myProgressBar.isShowRetryButton()) {
//            profileListPresenter.getMyArticles(scrollListener.getCurrentPage());
//            myProfileAdapter.resetProgressObject();
//        }
//    }


    @Override
    public void onResumeAction() {
        profileListPresenter.onResumeAction();
    }

    @Override
    public void onPauseAction() {
        profileListPresenter.onPauseAction();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        profileListPresenter.detachView();
    }

    @Override
    public void setupUserDto(UserDTO userDTO) {
        if (userDTO != null) {
            this.userDTO = userDTO;
            text_profile_username.setText(userDTO.getFullName());

            Glide.with(this)
                    .load(userDTO.getAvatarURL())
                    .crossFade()
                    .dontTransform()
                    .into(image_profile_user);
            layoutSignInPrompt.setVisibility(View.GONE);
//            scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
//
//                @Override
//                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
//                    profileListPresenter.getMyArticles(page);
//                }
//            };
//            RecyclerView.ItemAnimator animator = profileList.getItemAnimator();
//            if (animator instanceof SimpleItemAnimator) {
//                ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
//            }
//            profileList.addOnScrollListener(scrollListener);
//            //just giving a nudge
//            scrollListener.onScrolled(profileList, 1, 0);
        } else {
            signInView();
        }
    }

    @Override
    public void signInView() {
        this.userDTO = null;
        layoutSignInPrompt.setVisibility(View.VISIBLE);
    }


    @OnClick(R.id.buttonSignIn)
    public void signInButtonClick() {
        startActivity(SignInActivity.getCleanStartIntent(getActivity()));
    }

    @OnClick(R.id.layoutSignOut)
    public void signOutButtonClick() {
        profileListPresenter.logout();
    }

    @OnClick(R.id.layoutSettings)
    public void settingsButtonClick() {
        Toast.makeText(getActivity(), "Not implemented yet :(", Toast.LENGTH_SHORT).show();
    }

}
