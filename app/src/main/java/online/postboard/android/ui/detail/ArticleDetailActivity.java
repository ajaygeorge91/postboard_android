package online.postboard.android.ui.detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import online.postboard.android.R;
import online.postboard.android.data.events.ArticleActivityActionButtonClickEvent;
import online.postboard.android.data.events.CommentActionButtonClickEvent;
import online.postboard.android.data.events.LoadCommentsEvent;
import online.postboard.android.data.events.SubCommentActionButtonClickEvent;
import online.postboard.android.data.model.AnyItem;
import online.postboard.android.data.model.ArticleDTO;
import online.postboard.android.data.model.CommentDTO;
import online.postboard.android.data.model.NodeLabel;
import online.postboard.android.data.model.SubCommentDTO;
import online.postboard.android.data.model.SubCommentLoadMore;
import online.postboard.android.data.model.UserArticleActivity;
import online.postboard.android.data.model.UserCommentActivity;
import online.postboard.android.data.model.UserDTO;
import online.postboard.android.ui.base.BaseActivity;
import online.postboard.android.ui.subcomment.SubCommentActivity;
import online.postboard.android.util.DialogFactory;
import online.postboard.android.util.ViewUtil;
import online.postboard.android.util.apputils.ProfileUtils;
import online.postboard.android.util.widgets.CircleProgressBar;
import online.postboard.android.util.widgets.CircularImageView;
import online.postboard.android.util.widgets.EndlessRecyclerViewScrollListener;
import online.postboard.android.util.widgets.SwipeBackLayout;
import timber.log.Timber;

public class ArticleDetailActivity extends BaseActivity implements DetailMvpView {

    @BindView(R.id.article_detail_root_view)
    RelativeLayout article_detail_root_view;

    @BindView(R.id.article_detail_list)
    RecyclerView article_detail_list;

    @BindView(R.id.toolbar_layout)
    LinearLayout toolbar_layout;

    @BindView(R.id.main_detail_layout)
    RelativeLayout main_detail_layout;

    @BindView(R.id.text_content)
    EditText text_content;

    @BindView(R.id.image_user)
    CircularImageView image_user;

    @BindView(R.id.swipe_layout_article)
    SwipeBackLayout swipe_layout_article;

    @BindView(R.id.comment_send_button)
    ImageButton send_button;

    @BindView(R.id.reply_layout)
    LinearLayout reply_layout;

    @BindView(R.id.progressbar)
    CircleProgressBar progressBar;

    @Inject
    DetailPresenter detailPresenter;

    @Inject
    ArticleDetailAdapter articleDetailAdapter;

    public static final String ARTICLE_DTO = "_article_dto";
    public static final String NODE_ID = "_node_id";
    public static final String SHOW_ARTICLE_DTO = "_show_article_dto";
    private ArticleDTO articleDTO;
    private EndlessRecyclerViewScrollListener scrollListener;
    private LinearLayoutManager linearLayoutManager;
    private LoadCommentsEvent.CommentType commentType = LoadCommentsEvent.CommentType.HOT;
    private boolean showArticle = false;
    private String nodeID;

    public static void startArticleDetailActivity(Activity activity, ArticleDTO articleDTO, boolean showArticle) {
        Intent intent = new Intent(activity, ArticleDetailActivity.class);
        intent.putExtra(ARTICLE_DTO, articleDTO);
        intent.putExtra(SHOW_ARTICLE_DTO, showArticle);
        activity.startActivity(intent);
        if (!showArticle) {
            activity.overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
        }
    }

    public static void startArticleDetailActivity(Activity activity, String nodeId) {
        Intent intent = new Intent(activity, ArticleDetailActivity.class);
        intent.putExtra(NODE_ID, nodeId);
        intent.putExtra(SHOW_ARTICLE_DTO, true);
        activity.startActivity(intent);
//        activity.overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent = getIntent();
        final String action = intent.getAction();


        activityComponent().inject(this);
        detailPresenter.attachView(this);

        setContentView(R.layout.activity_article_detail);
        ButterKnife.bind(this);

        articleDTO = (ArticleDTO) getIntent().getSerializableExtra(ARTICLE_DTO);
        nodeID = getIntent().getStringExtra(NODE_ID);
        showArticle = getIntent().getBooleanExtra(SHOW_ARTICLE_DTO, false);

        if (Intent.ACTION_VIEW.equals(action)) {
            if (intent.getData() != null) {
                final List<String> segments = intent.getData().getPathSegments();
                if (segments.size() >= 2) {
                    nodeID = segments.get(1);
                }
            }
            String segments = intent.getDataString();
            Timber.d("ACTION_VIEW segments" + segments);
        }

        if (articleDTO == null && nodeID == null) {
            finish();
        }

        if (showArticle) {
            swipe_layout_article.setEnableFlingBack(false);
            swipe_layout_article.setEnablePullToBack(false);
            main_detail_layout.setBackground(ContextCompat.getDrawable(this, R.drawable.non_rounded_border));
            toolbar_layout.setBackground(ContextCompat.getDrawable(this, R.drawable.non_rounded_border_top_half));
            reply_layout.setBackground(ContextCompat.getDrawable(this, R.drawable.non_rounded_border));
        } else {
            swipe_layout_article.setDragDirectMode(SwipeBackLayout.DragDirectMode.VERTICAL);
            swipe_layout_article.setEnableFlingBack(true);
            swipe_layout_article.setOnSwipeBackListener((fractionAnchor, fractionScreen) -> {
                progressBar.setProgress((int) (progressBar.getMax() * fractionAnchor));
            });
            ViewUtil.setWindowTranslucentStatusTrue(this);
        }

        linearLayoutManager = new LinearLayoutManager(this) {
            @Override
            public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
                int scrollRange = super.scrollVerticallyBy(dy, recycler, state);
                int overscroll = dy - scrollRange;
                if (overscroll != 0 && !showArticle) {
                    swipe_layout_article.setEnablePullToBack(true);
                }
                return scrollRange;
            }
        };
        article_detail_list.setLayoutManager(linearLayoutManager);
        article_detail_list.setAdapter(articleDetailAdapter);
        RecyclerView.ItemAnimator animator = article_detail_list.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        if (articleDTO != null) {
            setupArticleDTO(articleDTO);
        } else {
            detailPresenter.loadByNodeId(nodeID);
        }

    }

    private void setupArticleDTO(ArticleDTO articleDTO) {
        articleDetailAdapter.addArticle(articleDTO, showArticle);
//        article_detail_list.smoothScrollToPosition(2);
        initializeOrResetScrollListener();
        detailPresenter.setUserDetails();
        replyBoxVisible(false);
        RxTextView.textChanges(text_content).subscribe(t -> sendButtonVisible(!t.toString().isEmpty()));
        send_button.setOnClickListener(v -> detailPresenter.postComment(this.articleDTO.getId(), text_content.getText().toString()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        detailPresenter.detachView();
    }

    private void initializeOrResetScrollListener() {
        if (scrollListener != null) {
            article_detail_list.removeOnScrollListener(scrollListener);
            scrollListener = null;
        }
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                detailPresenter.loadComments(articleDTO.getId(), page, commentType);
            }
        };
        article_detail_list.addOnScrollListener(scrollListener);
        //just giving a nudge
        scrollListener.onScrolled(article_detail_list, 1, 0);
    }

    @Override
    public void showComments(List<CommentDTO> commentDTOs) {
        articleDetailAdapter.addComments(commentDTOs);
    }

    @Override
    public void showCommentsEmpty() {
        articleDetailAdapter.setLoadingMessage(getString(R.string.empty_items));
    }

    @Override
    public void showError(String message) {
        articleDetailAdapter.setLoadingMessage(message.isEmpty() ? "Error" : message);
    }

    @Override
    public void clearAllCommentsAndLoad(LoadCommentsEvent.CommentType commentType) {
        if (!commentType.equals(this.commentType)) {
            this.commentType = commentType;
            articleDetailAdapter.clearAllComments();
            articleDetailAdapter.setCommentType(commentType);
            initializeOrResetScrollListener();
        }
    }

    @Override
    public void articleAction(ArticleActivityActionButtonClickEvent articleActivityActionButtonClickEvent) {
        articleDetailAdapter.articleAction(articleActivityActionButtonClickEvent);
    }

    @Override
    public void subCommentAction(SubCommentActionButtonClickEvent subCommentActionButtonClickEvent) {
        articleDetailAdapter.subCommentAction(subCommentActionButtonClickEvent);
    }

    @Override
    public void subCommentAddedAction(String commentId, SubCommentDTO subCommentDTO) {
        articleDetailAdapter.addSubComment(commentId, subCommentDTO);
    }

    @Override
    public void commentAction(CommentActionButtonClickEvent commentActionButtonClickEvent) {
        articleDetailAdapter.commentAction(commentActionButtonClickEvent);
    }

    @Override
    public void updateArticle(String articleID, UserArticleActivity userArticleActivity) {
        articleDetailAdapter.updateArticleItem(articleID, userArticleActivity);
    }

    @Override
    public void updateComment(String commentID, UserCommentActivity userCommentActivity) {
        articleDetailAdapter.updateCommentItem(commentID, userCommentActivity);
    }

    @Override
    public void setUser(UserDTO userDTO) {

        String avatarUrl = ProfileUtils.getUserAvatarUrl(userDTO);

        Glide.with(this)
                .load(avatarUrl)
                .crossFade()
                .dontTransform()
                .into(image_user);
    }

    @Override
    public void commentPostSuccess(CommentDTO commentDTO) {
        articleDetailAdapter.addNewCommentToTop(commentDTO);
        replyBoxVisible(false);
    }

    @Override
    public void commentPostFailed(String message) {
        DialogFactory.INSTANCE.createGenericErrorDialog(this, message);
    }

    @Override
    public void showArticle(ArticleDTO articleDTO) {
        this.articleDTO = articleDTO;
        setupArticleDTO(articleDTO);
        if (articleDTO.getLinkedComment() != null) {
            articleDTO.getLinkedComment().setNodeLabel(new NodeLabel(""));
            ArrayList dummyList = new ArrayList<>();
            dummyList.add(articleDTO.getLinkedComment());
            showComments(dummyList);
        }
        if (articleDTO.getComments() != null && articleDTO.getComments().getResult() != null) {
            showComments(articleDTO.getComments().getResult());
        }
    }

    @Override
    public void replyBoxVisible(boolean visible) {
        if (visible) {
            reply_layout.setVisibility(View.VISIBLE);
            ViewUtil.showKeyboard(this, text_content);
        } else {
            text_content.setText("");
            ViewUtil.hideKeyboard(this);
            reply_layout.setVisibility(View.GONE);
        }
    }

    public void sendButtonVisible(boolean visible) {
        if (visible) {
            send_button.setVisibility(View.VISIBLE);
        } else {
            send_button.setVisibility(View.GONE);
        }
    }

    @Override
    public void loadMoreSubCommentsAction(int adapterPosition) {
        AnyItem a = articleDetailAdapter.getItem(adapterPosition);
        if (a instanceof SubCommentLoadMore) {
            showSubCommentActivity((((SubCommentLoadMore) a).getComment()), false);
        } else if (a instanceof CommentDTO) {
            showSubCommentActivity(((CommentDTO) a), true);
        }
    }

    private void showSubCommentActivity(CommentDTO commentDTO, boolean showReply) {
        SubCommentActivity.startSubCommentActivity(this, commentDTO, showReply);
    }

    @OnClick(R.id.back_button_layout)
    public void backButtonClick(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (reply_layout != null && reply_layout.getVisibility() == View.VISIBLE) {
            replyBoxVisible(false);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void finish() {
        super.finish();
        if (!showArticle) {
            overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
        }
    }

}
