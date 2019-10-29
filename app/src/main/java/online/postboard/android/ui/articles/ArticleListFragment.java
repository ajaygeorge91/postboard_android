package online.postboard.android.ui.articles;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import online.postboard.android.R;
import online.postboard.android.data.events.ArticleActivityActionButtonClickEvent;
import online.postboard.android.data.events.ShowArticleDetailEvent;
import online.postboard.android.data.events.ShowUserProfileEvent;
import online.postboard.android.data.model.AnyItem;
import online.postboard.android.data.model.ArticleDTO;
import online.postboard.android.data.model.MyProgressBar;
import online.postboard.android.data.model.UserArticleActivity;
import online.postboard.android.data.model.UserDTO;
import online.postboard.android.ui.detail.ArticleDetailActivity;
import online.postboard.android.ui.main.CustomFragment;
import online.postboard.android.ui.userprofile.UserProfileActivity;
import online.postboard.android.util.widgets.EndlessRecyclerViewScrollListener;

public class ArticleListFragment extends CustomFragment implements ArticleListMvpView, SwipeRefreshLayout.OnRefreshListener {

    @Inject
    ArticleListPresenter articleListPresenter;

    @Inject
    ArticlesAdapter articlesAdapter;

    @BindView(R.id.list)
    RecyclerView recyclerView;

    @BindView(R.id.refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    private static final String ARG_ARTICLE_TYPE = "_article_type";
    private EndlessRecyclerViewScrollListener scrollListener;
    private int fragmentItem;

    public ArticleListFragment() {
    }

    public static ArticleListFragment newInstance(int item) {
        ArticleListFragment fragment = new ArticleListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ARTICLE_TYPE, item);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResumeAction() {
        articleListPresenter.onResumeAction();
    }

    @Override
    public void onPauseAction() {
        articleListPresenter.onPauseAction();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        fragmentItem = getArguments().getInt(ARG_ARTICLE_TYPE);
        fragmentComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        articleListPresenter.attachView(this);
        View view = inflater.inflate(R.layout.fragment_article_list, container, false);
        ButterKnife.bind(this, view);

        Context context = view.getContext();
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setProgressViewOffset(false,
                0,
                getResources().getDimensionPixelSize(R.dimen.my_action_bar_and_status_bar_height_plus_shadow)
        );
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadItems(page);
            }
        };
        recyclerView.setAdapter(articlesAdapter);
        recyclerView.getItemAnimator().setChangeDuration(0);
        recyclerView.addOnScrollListener(scrollListener);

        return view;
    }

    private void loadItems(int page) {
        switch (fragmentItem) {
            case ArticleType.HOT:
                articleListPresenter.loadHotArticles(page);
                break;
            case ArticleType.NEW:
                articleListPresenter.loadNewArticles(page);
                break;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        articleListPresenter.detachView();
    }

    @Override
    public void continueListLoading() {
        MyProgressBar myProgressBar = articlesAdapter.getProgressObject();
        if (myProgressBar != null && myProgressBar.isShowRetryButton()) {
            articlesAdapter.resetProgressObject();
            loadItems(scrollListener.getCurrentPage());
        }
    }

    @Override
    public void getUserArticleActivity() {
        switch (fragmentItem) {
            case ArticleType.HOT:
                articleListPresenter.getHotArticleUserActivity(scrollListener.getCurrentPage() + 1);
                break;
            case ArticleType.NEW:
                articleListPresenter.getNewArticleUserActivity(scrollListener.getCurrentPage() + 1);
                break;
        }
    }

    @Override
    public void updateArticleActivity(List<UserArticleActivity> userArticleActivities) {
        articlesAdapter.updateUserArticleActivity(userArticleActivities);
    }

    @Override
    public void showUserProfile(ShowUserProfileEvent showUserProfileEvent) {
        UserDTO userDTO = articlesAdapter.getUserProfile(showUserProfileEvent.getAdapterPosition());
        if (userDTO != null) {
            startActivity(UserProfileActivity.getStartIntent(getActivity(), userDTO));
        }
    }

    @Override
    public void removeUserArticleActivity() {
        articlesAdapter.removeUserArticleActivity();
    }

    @Override
    public void showArticles(List<ArticleDTO> articleDTOs) {
        if (swipeRefreshLayout.isRefreshing()) {
            articlesAdapter.removeItems();
            scrollListener.resetState();
        }
        swipeRefreshLayout.setRefreshing(false);
        articlesAdapter.addItems(articleDTOs);
    }

    @Override
    public void articleAction(ArticleActivityActionButtonClickEvent articleActivityActionButtonClickEvent) {
        articlesAdapter.articleAction(articleActivityActionButtonClickEvent);
    }

    @Override
    public void articleDetailAction(ShowArticleDetailEvent showArticleDetailEvent) {
        AnyItem a = articlesAdapter.getItem(showArticleDetailEvent.getAdapterPosition());
        if (a instanceof ArticleDTO) {
            ArticleDetailActivity.startArticleDetailActivity(getActivity(), ((ArticleDTO) a), showArticleDetailEvent.isShowArticle());
        }
    }

    @Override
    public void updateArticle(String articleID, UserArticleActivity userArticleActivity) {
        articlesAdapter.updateArticleItem(articleID, userArticleActivity);
    }

    @Override
    public void showError(String errorMessage) {
        swipeRefreshLayout.setRefreshing(false);
        articlesAdapter.setErrorMessage(errorMessage);
    }

    @Override
    public void showArticlesEmpty() {
        swipeRefreshLayout.setRefreshing(false);
        articlesAdapter.addItems(Collections.<ArticleDTO>emptyList());
        articlesAdapter.setLoadingMessage(getString(R.string.empty_items));
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        loadItems(0);
    }

    public final class ArticleType {
        public static final int HOT = 0;
        public static final int NEW = 1;
    }

}
