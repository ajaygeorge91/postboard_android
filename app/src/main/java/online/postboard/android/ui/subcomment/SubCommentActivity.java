package online.postboard.android.ui.subcomment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import online.postboard.android.data.events.SubCommentActionButtonClickEvent;
import online.postboard.android.data.model.AnyItem;
import online.postboard.android.data.model.CommentDTO;
import online.postboard.android.data.model.SubCommentDTO;
import online.postboard.android.data.model.UserCommentActivity;
import online.postboard.android.data.model.UserDTO;
import online.postboard.android.ui.base.BaseActivity;
import online.postboard.android.util.ViewUtil;
import online.postboard.android.util.apputils.ProfileUtils;
import online.postboard.android.util.widgets.CircleProgressBar;
import online.postboard.android.util.widgets.CircularImageView;
import online.postboard.android.util.widgets.EndlessRecyclerViewScrollListener;
import online.postboard.android.util.widgets.SwipeBackLayout;
import online.postboard.android.R;
import online.postboard.android.data.events.CommentActionButtonClickEvent;
import online.postboard.android.util.DialogFactory;

import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SubCommentActivity extends BaseActivity implements SubCommentListMvpView {

    public static final String COMMENT_DTO = "_comment_dto";
    public static final String SHOW_REPLY_BOX = "_show_reply_box";
    private CommentDTO commentDTO;
    private boolean showReplyBox;

    @Inject
    SubCommentListPresenter subCommentListPresenter;

    @Inject
    SubCommentsAdapter subCommentsAdapter;

    @BindView(R.id.sub_comment_list)
    RecyclerView subCommentList;

    @BindView(R.id.text_content)
    EditText text_content;

    @BindView(R.id.image_user)
    CircularImageView image_user;

    @BindView(R.id.swipe_layout_sub_comment)
    SwipeBackLayout swipe_layout_sub_comment;

    @BindView(R.id.comment_send_button)
    ImageButton send_button;

    @BindView(R.id.reply_layout)
    LinearLayout reply_layout;

    @BindView(R.id.progressbar)
    CircleProgressBar progressBar;

    private LinearLayoutManager linearLayoutManager;
    private EndlessRecyclerViewScrollListener scrollListener;

    public static void startSubCommentActivity(Activity activity, CommentDTO comment, boolean showReply) {
        Intent intent = new Intent(activity, SubCommentActivity.class);
        intent.putExtra(COMMENT_DTO, comment);
        intent.putExtra(SHOW_REPLY_BOX, showReply);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityComponent().inject(this);
        subCommentListPresenter.attachView(this);
        setContentView(R.layout.activity_sub_comment);
        ButterKnife.bind(this);

        ViewUtil.setWindowTranslucentStatusTrue(this);

        commentDTO = (CommentDTO) getIntent().getSerializableExtra(COMMENT_DTO);
        showReplyBox = getIntent().getBooleanExtra(SHOW_REPLY_BOX, false);
        if (commentDTO == null) {
            finish();
        }

        swipe_layout_sub_comment.setDragDirectMode(SwipeBackLayout.DragDirectMode.VERTICAL);
        swipe_layout_sub_comment.setEnableFlingBack(true);
        swipe_layout_sub_comment.setOnSwipeBackListener((fractionAnchor, fractionScreen) -> {
            progressBar.setProgress((int) (progressBar.getMax() * fractionAnchor));
        });

        linearLayoutManager = new LinearLayoutManager(this) {
            @Override
            public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
                int scrollRange = super.scrollVerticallyBy(dy, recycler, state);
                int overscroll = dy - scrollRange;
                if (overscroll != 0) {
                    swipe_layout_sub_comment.setEnablePullToBack(true);
                }
                return scrollRange;
            }
        };
        subCommentList.setLayoutManager(linearLayoutManager);
        subCommentList.setAdapter(subCommentsAdapter);
        RecyclerView.ItemAnimator animator = subCommentList.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        subCommentsAdapter.addComment(commentDTO);
//        subCommentList.smoothScrollToPosition(2);

        initializeOrResetScrollListener();
        subCommentListPresenter.setUserDetails();
        replyBoxVisible(showReplyBox);
        RxTextView.textChanges(text_content).subscribe(t -> sendButtonVisible(!t.toString().isEmpty()));
        send_button.setOnClickListener(v -> {
            Answers.getInstance().logCustom(new CustomEvent("CommentSendButtonClick"));
            subCommentListPresenter.postSubComment(commentDTO.getId(), text_content.getText().toString());
        });

    }

    private void initializeOrResetScrollListener() {
        if (scrollListener != null) {
            subCommentList.removeOnScrollListener(scrollListener);
            scrollListener = null;
        }
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                subCommentListPresenter.loadSubComments("0", commentDTO.getId(), page);
            }
        };
        subCommentList.addOnScrollListener(scrollListener);
        //just giving a nudge
        scrollListener.onScrolled(subCommentList, 1, 0);
    }

    public void sendButtonVisible(boolean visible) {
        if (visible) {
            send_button.setVisibility(View.VISIBLE);
        } else {
            send_button.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        subCommentListPresenter.onResumeAction();
    }

    @Override
    public void onPause() {
        super.onPause();
        subCommentListPresenter.onPauseAction();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subCommentListPresenter.detachView();
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

    @Override
    public void updateComment(String commentID, UserCommentActivity userCommentActivity) {
        subCommentsAdapter.updateCommentItem(commentID, userCommentActivity);
    }

    @Override
    public void subCommentAction(SubCommentActionButtonClickEvent subCommentActionButtonClickEvent) {
        subCommentsAdapter.subCommentAction(subCommentActionButtonClickEvent);
    }

    @Override
    public void commentAction(CommentActionButtonClickEvent commentActionButtonClickEvent) {
        subCommentsAdapter.commentAction(commentActionButtonClickEvent);
    }

    @Override
    public void subCommentPostSuccess(SubCommentDTO subCommentDTO) {
        subCommentsAdapter.addNewSubCommentToTop(subCommentDTO);
        replyBoxVisible(false);
    }

    @Override
    public void subCommentPostFailed(String message) {
        DialogFactory.INSTANCE.createGenericErrorDialog(this, message).show();
    }

    @Override
    public void showItems(List<? extends AnyItem> anyItemList) {
        subCommentsAdapter.addSubComments(anyItemList);
        subCommentsAdapter.setLoadingMessage("");
    }

    @Override
    public void showError(String errorMessage) {
        subCommentsAdapter.setErrorMessage(errorMessage);
    }

    @Override
    public void showItemsEmpty() {
        subCommentsAdapter.setLoadingMessage(getString(R.string.empty_items));
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
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
    }


}
