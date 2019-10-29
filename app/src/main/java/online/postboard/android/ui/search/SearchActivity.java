package online.postboard.android.ui.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.widget.EditText;

import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import online.postboard.android.R;
import online.postboard.android.data.events.ArticleActivityActionButtonClickEvent;
import online.postboard.android.data.events.ShowArticleDetailEvent;
import online.postboard.android.data.events.ShowUserProfileEvent;
import online.postboard.android.data.model.AnyItem;
import online.postboard.android.data.model.ArticleDTO;
import online.postboard.android.data.model.MyProgressBar;
import online.postboard.android.data.model.UserArticleActivity;
import online.postboard.android.data.model.UserDTO;
import online.postboard.android.ui.base.BaseActivity;
import online.postboard.android.ui.detail.ArticleDetailActivity;
import online.postboard.android.ui.userprofile.UserProfileActivity;
import online.postboard.android.util.widgets.EndlessRecyclerViewScrollListener;

public class SearchActivity extends BaseActivity implements SearchMvpView {

    @BindView(R.id.search_list)
    RecyclerView search_list;

    @BindView(R.id.search)
    EditText search;

    @Inject
    SearchPresenter searchPresenter;

    @Inject
    SearchAdapter searchAdapter;

    String searchKey = "";

    public static final String SEARCH_KEY = "_search_key";
    private EndlessRecyclerViewScrollListener scrollListener;
    private LinearLayoutManager linearLayoutManager;

    public static void startArticleDetailActivity(Activity activity, String searchKey) {
        Intent intent = new Intent(activity, SearchActivity.class);
        intent.putExtra(SEARCH_KEY, searchKey);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent = getIntent();
        final String action = intent.getAction();


        activityComponent().inject(this);
        searchPresenter.attachView(this);

        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        searchKey = getIntent().getStringExtra(SEARCH_KEY);


        linearLayoutManager = new LinearLayoutManager(this);
        search_list.setLayoutManager(linearLayoutManager);
        search_list.setAdapter(searchAdapter);

        RecyclerView.ItemAnimator animator = search_list.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        initializeOrResetScrollListener();

        RxTextView.afterTextChangeEvents(search)
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    try {
                        String newSearchKey = s.editable().toString();
                        if (newSearchKey.isEmpty()) {
                            return;
                        }
                        if (!newSearchKey.equalsIgnoreCase(searchKey)) {
                            searchKey = newSearchKey;
                            searchAdapter.removeItems();
                            searchPresenter.getArticleFromSearchQuery(searchKey, 0);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        searchPresenter.detachView();
    }

    private void initializeOrResetScrollListener() {
        if (scrollListener != null) {
            search_list.removeOnScrollListener(scrollListener);
            scrollListener = null;
        }
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                searchPresenter.getArticleFromSearchQuery(searchKey, page);
            }
        };
        search_list.addOnScrollListener(scrollListener);
        //just giving a nudge
        scrollListener.onScrolled(search_list, 1, 0);
    }

    @Override
    public void showArticles(List<ArticleDTO> articleDTOS) {
        searchAdapter.addItems(articleDTOS);
    }

    @Override
    public void showArticlesEmpty() {
        searchAdapter.setLoadingMessage(getString(R.string.empty_items));
    }

    @Override
    public void showError(String message) {
        searchAdapter.setLoadingMessage(message.isEmpty() ? "Error" : message);
    }

    @Override
    public void articleAction(ArticleActivityActionButtonClickEvent articleActivityActionButtonClickEvent) {
        searchAdapter.articleAction(articleActivityActionButtonClickEvent);
    }

    @Override
    public void articleDetailAction(ShowArticleDetailEvent showArticleDetailEvent) {
        AnyItem a = searchAdapter.getItem(showArticleDetailEvent.getAdapterPosition());
        if (a instanceof ArticleDTO) {
            ArticleDetailActivity.startArticleDetailActivity(this, ((ArticleDTO) a), showArticleDetailEvent.isShowArticle());
        }
    }

    @Override
    public void updateArticle(String articleID, UserArticleActivity userArticleActivity) {
        searchAdapter.updateArticleItem(articleID, userArticleActivity);
    }

    @Override
    public void continueListLoading() {
        MyProgressBar myProgressBar = searchAdapter.getProgressObject();
        if (myProgressBar != null && myProgressBar.isShowRetryButton()) {
            searchAdapter.resetProgressObject();
            searchPresenter.getArticleFromSearchQuery(searchKey, scrollListener.getCurrentPage());
        }
    }

    @Override
    public void clearArticles() {
        searchAdapter.removeItems();
    }

    @Override
    public void showUserProfile(ShowUserProfileEvent showUserProfileEvent) {
        UserDTO userDTO = searchAdapter.getUserProfile(showUserProfileEvent.getAdapterPosition());
        if (userDTO != null) {
            startActivity(UserProfileActivity.getStartIntent(this, userDTO));
        }
    }


    @OnClick(R.id.back_button_layout)
    public void backButtonClick(View view) {
        onBackPressed();
    }


}
