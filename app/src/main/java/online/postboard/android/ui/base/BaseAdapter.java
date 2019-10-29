package online.postboard.android.ui.base;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import online.postboard.android.data.DataManager;
import online.postboard.android.data.events.ArticleActivityActionButtonClickEvent;
import online.postboard.android.data.events.ArticleActivityResponseEvent;
import online.postboard.android.data.events.CommentActionButtonClickEvent;
import online.postboard.android.data.events.CommentActivityResponseEvent;
import online.postboard.android.data.events.SubCommentActionButtonClickEvent;
import online.postboard.android.data.model.AnyItem;
import online.postboard.android.data.model.ArticleDTO;
import online.postboard.android.data.model.CommentDTO;
import online.postboard.android.data.model.CommentMeta;
import online.postboard.android.data.model.CommentReplyBox;
import online.postboard.android.data.model.CommentTypeButtons;
import online.postboard.android.data.model.MyProgressBar;
import online.postboard.android.data.model.NodeLabel;
import online.postboard.android.data.model.NotificationDTO;
import online.postboard.android.data.model.ResponseDTO;
import online.postboard.android.data.model.SubCommentDTO;
import online.postboard.android.data.model.SubCommentLoadMore;
import online.postboard.android.data.model.SubCommentMeta;
import online.postboard.android.data.model.UserActivityDTO;
import online.postboard.android.data.model.UserArticleActivity;
import online.postboard.android.data.model.UserCommentActivity;
import online.postboard.android.data.model.UserDTO;
import online.postboard.android.data.model.UserSubCommentActivity;
import online.postboard.android.ui.list.ArticleViewHolder;
import online.postboard.android.ui.list.CommentReplyBoxViewHolder;
import online.postboard.android.ui.list.CommentTypeSelectorViewHolder;
import online.postboard.android.ui.list.CommentViewHolder;
import online.postboard.android.ui.list.LoadSubCommentsViewHolder;
import online.postboard.android.ui.list.NodeLabelViewHolder;
import online.postboard.android.ui.list.NotificationViewHolder;
import online.postboard.android.ui.list.ProgressViewHolder;
import online.postboard.android.ui.list.SubCommentViewHolder;
import online.postboard.android.ui.list.UserActivityViewHolder;
import online.postboard.android.ui.list.UserProfileCardViewHolder;
import online.postboard.android.util.RxEventBus;
import online.postboard.android.util.ThrowableUtil;
import timber.log.Timber;

public class BaseAdapter extends RecyclerView.Adapter<AnyItemViewHolder> {

    protected List<AnyItem> anyItemList;
    protected Activity activity;
    protected RxEventBus eventBus;
    protected DataManager mDataManager;
    protected ScreenType screenType;

    public BaseAdapter(Activity activity, DataManager dataManager, RxEventBus eventBus, ScreenType screenType) {
        anyItemList = new ArrayList<>();
        this.activity = activity;
        this.eventBus = eventBus;
        this.mDataManager = dataManager;
        this.screenType = screenType;
    }

    public BaseAdapter(Activity activity, DataManager dataManager, RxEventBus eventBus) {
        this(activity, dataManager, eventBus, ScreenType.ARTICLE_LIST);
    }

    @Override
    public int getItemViewType(int position) {
        AnyItem c = getItem(position);
        if (c instanceof MyProgressBar) {
            return ANY_ITEM.PROGRESS_BAR;
        } else if (c instanceof ArticleDTO) {
            return ANY_ITEM.ARTICLE_ITEM;
        } else if (c instanceof CommentTypeButtons) {
            return ANY_ITEM.COMMENT_TYPE_ITEM;
        } else if (c instanceof CommentReplyBox) {
            return ANY_ITEM.COMMENT_REPLY_BOX;
        } else if (c instanceof CommentDTO) {
            return ANY_ITEM.COMMENT_ITEM;
        } else if (c instanceof SubCommentDTO) {
            return ANY_ITEM.SUB_COMMENT_ITEM;
        } else if (c instanceof SubCommentLoadMore) {
            return ANY_ITEM.LOAD_ALL_SUB_COMMENTS;
        } else if (c instanceof UserDTO) {
            return ANY_ITEM.USER_PROFILE_CARD;
        } else if (c instanceof UserActivityDTO) {
            return ANY_ITEM.USER_ACTIVITY_CARD;
        } else if (c instanceof NotificationDTO) {
            return ANY_ITEM.NOTIFICATION_ITEM;
        } else if (c instanceof NodeLabel) {
            return ANY_ITEM.NODE_LABEL;
        } else {
            return ANY_ITEM.PROGRESS_BAR;
        }
    }

    @Override
    public AnyItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ANY_ITEM.PROGRESS_BAR:
                return new ProgressViewHolder(parent, eventBus);
            case ANY_ITEM.ARTICLE_ITEM:
                return new ArticleViewHolder(parent, eventBus, screenType);
            case ANY_ITEM.COMMENT_TYPE_ITEM:
                return new CommentTypeSelectorViewHolder(parent, eventBus);
            case ANY_ITEM.COMMENT_REPLY_BOX:
                return new CommentReplyBoxViewHolder(parent, eventBus);
            case ANY_ITEM.COMMENT_ITEM:
                return new CommentViewHolder(parent, eventBus);
            case ANY_ITEM.SUB_COMMENT_ITEM:
                return new SubCommentViewHolder(parent, eventBus);
            case ANY_ITEM.LOAD_ALL_SUB_COMMENTS:
                return new LoadSubCommentsViewHolder(parent, eventBus);
            case ANY_ITEM.USER_PROFILE_CARD:
                return new UserProfileCardViewHolder(parent, eventBus);
            case ANY_ITEM.USER_ACTIVITY_CARD:
                return new UserActivityViewHolder(parent, eventBus);
            case ANY_ITEM.NOTIFICATION_ITEM:
                return new NotificationViewHolder(parent, eventBus);
            case ANY_ITEM.NODE_LABEL:
                return new NodeLabelViewHolder(parent, eventBus);
            default:
                return new ProgressViewHolder(parent, eventBus);
        }
    }

    @Override
    public void onBindViewHolder(final AnyItemViewHolder holder, int position) {
        final AnyItem anyItem = anyItemList.get(position);
        holder.bind(anyItem, screenType);
    }

    @Override
    public int getItemCount() {
        return anyItemList.size();
    }

    public AnyItem getItem(int position) {
        return anyItemList.get(position);
    }

    private static class ANY_ITEM {
        static final int PROGRESS_BAR = 0;
        static final int ARTICLE_ITEM = 1;
        static final int COMMENT_TYPE_ITEM = 2;
        static final int COMMENT_ITEM = 3;
        static final int SUB_COMMENT_ITEM = 4;
        static final int LOAD_ALL_SUB_COMMENTS = 5;
        static final int COMMENT_REPLY_BOX = 6;
        static final int USER_PROFILE_CARD = 7;
        static final int USER_ACTIVITY_CARD = 8;
        static final int NOTIFICATION_ITEM = 9;
        static final int NODE_LABEL = 10;
    }

    public void updateArticleItem(String articleID, UserArticleActivity userArticleActivity) {
        for (int i = 0; i < anyItemList.size(); i++) {
            AnyItem a = anyItemList.get(i);
            if (a instanceof ArticleDTO && ((ArticleDTO) a).getId().equalsIgnoreCase(articleID)) {
                long delta = 0;
                if (((ArticleDTO) a).getUserArticleActivity() != null) {
                    long oldRating = ((ArticleDTO) a).getUserArticleActivity().getRating();
                    long newRating = userArticleActivity.getRating();
                    delta = oldRating - newRating;
                    ((ArticleDTO) a).getArticleMeta().setRatingSum(((ArticleDTO) a).getArticleMeta().getRatingSum() - delta);
                } else {
                    delta = userArticleActivity.getRating();
                    ((ArticleDTO) a).getArticleMeta().setRatingSum(((ArticleDTO) a).getArticleMeta().getRatingSum() + delta);
                }
                ((ArticleDTO) a).setUserArticleActivity(userArticleActivity);
                notifyItemChanged(i);
                break;
            }
        }
    }

    public void articleAction(ArticleActivityActionButtonClickEvent articleActivityActionButtonClickEvent) {
        Timber.d("anyItemList.size() = " + anyItemList.size());
        Timber.d("event.getAdapterPosition() = " + articleActivityActionButtonClickEvent.getAdapterPosition());
        if (articleActivityActionButtonClickEvent.getAdapterPosition() > anyItemList.size()) {
            for (AnyItem a : anyItemList) {
                Timber.d("a : " + a.toString());
            }
            Timber.d("class : " + getClass().getSimpleName());
        }

        AnyItem a = anyItemList.get(articleActivityActionButtonClickEvent.getAdapterPosition());
        if (a instanceof ArticleDTO) {
            if (articleActivityActionButtonClickEvent.getActionButton() == ArticleActivityActionButtonClickEvent.ActionButton.LIKE) {
                UserArticleActivity rating;
                if (((ArticleDTO) a).getUserArticleActivity() != null && ((ArticleDTO) a).getUserArticleActivity().getRating() == 1L) {
                    rating = new UserArticleActivity(0);
                } else {
                    rating = new UserArticleActivity(1);
                }
                mDataManager.getPbService()
                        .addArticleReaction(((ArticleDTO) a).getId(), rating)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new UserArticleActivitySub(((ArticleDTO) a).getId()), new ThrowableUtil.ThrowableSub());
            } else if (articleActivityActionButtonClickEvent.getActionButton() == ArticleActivityActionButtonClickEvent.ActionButton.DISLIKE) {
                UserArticleActivity rating;
                if (((ArticleDTO) a).getUserArticleActivity() != null && ((ArticleDTO) a).getUserArticleActivity().getRating() == -1L) {
                    rating = new UserArticleActivity(0);
                } else {
                    rating = new UserArticleActivity(-1);
                }
                mDataManager.getPbService()
                        .addArticleReaction(((ArticleDTO) a).getId(), rating)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new UserArticleActivitySub(((ArticleDTO) a).getId()), new ThrowableUtil.ThrowableSub());
            }
        }
    }

    private class UserArticleActivitySub implements Consumer<ResponseDTO<UserArticleActivity>> {
        String articleID;

        UserArticleActivitySub(String id) {
            this.articleID = id;
        }

        @Override
        public void accept(@NonNull ResponseDTO<UserArticleActivity> userArticleActivityResponseDTO) throws Exception {
            eventBus.post(new ArticleActivityResponseEvent(userArticleActivityResponseDTO.getData(), articleID));
        }
    }

    public void updateCommentItem(String commentId, UserCommentActivity userCommentActivity) {
        for (int i = 0; i < anyItemList.size(); i++) {
            AnyItem a = anyItemList.get(i);
            if (a instanceof CommentDTO && ((CommentDTO) a).getId().equalsIgnoreCase(commentId)) {
                long delta = 0;
                if (((CommentDTO) a).getCommentMeta() == null) {
                    ((CommentDTO) a).setCommentMeta(new CommentMeta(0));
                }
                if (((CommentDTO) a).getUserCommentActivity() != null) {
                    long oldRating = ((CommentDTO) a).getUserCommentActivity().getRating();
                    long newRating = userCommentActivity.getRating();
                    delta = oldRating - newRating;
                    ((CommentDTO) a).getCommentMeta().setRatingSum(((CommentDTO) a).getCommentMeta().getRatingSum() - delta);
                } else {
                    delta = userCommentActivity.getRating();
                    ((CommentDTO) a).getCommentMeta().setRatingSum(((CommentDTO) a).getCommentMeta().getRatingSum() + delta);
                }
                ((CommentDTO) a).setUserCommentActivity(userCommentActivity);
                notifyItemChanged(i);
                break;
            } else if (a instanceof SubCommentDTO && ((SubCommentDTO) a).getId().equalsIgnoreCase(commentId)) {
                long delta = 0;
                UserSubCommentActivity userSubCommentActivity = new UserSubCommentActivity(commentId, userCommentActivity.getRating());
                if (((SubCommentDTO) a).getSubCommentMeta() == null) {
                    ((SubCommentDTO) a).setSubCommentMeta(new SubCommentMeta(0));
                }
                if (((SubCommentDTO) a).getUserSubCommentActivity() != null) {
                    long oldRating = ((SubCommentDTO) a).getUserSubCommentActivity().getRating();
                    long newRating = userSubCommentActivity.getRating();
                    delta = oldRating - newRating;
                    ((SubCommentDTO) a).getSubCommentMeta().setRatingSum(((SubCommentDTO) a).getSubCommentMeta().getRatingSum() - delta);
                } else {
                    delta = userSubCommentActivity.getRating();
                    ((SubCommentDTO) a).getSubCommentMeta().setRatingSum(((SubCommentDTO) a).getSubCommentMeta().getRatingSum() + delta);
                }
                ((SubCommentDTO) a).setUserSubCommentActivity(userSubCommentActivity);
                notifyItemChanged(i);
                break;
            }
        }
    }

    public void commentAction(CommentActionButtonClickEvent commentActionButtonClickEvent) {
        AnyItem a = anyItemList.get(commentActionButtonClickEvent.getAdapterPosition());
        if (a instanceof CommentDTO) {
            if (commentActionButtonClickEvent.getActionButton() == CommentActionButtonClickEvent.ActionButton.LIKE) {
                UserCommentActivity rating;
                if (((CommentDTO) a).getUserCommentActivity() != null && ((CommentDTO) a).getUserCommentActivity().getRating() == 1L) {
                    rating = new UserCommentActivity(((CommentDTO) a).getId(), 0);
                } else {
                    rating = new UserCommentActivity(((CommentDTO) a).getId(), 1);
                }
                mDataManager.getPbService()
                        .addCommentReaction("0", ((CommentDTO) a).getId(), rating)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new UserCommentActivitySub("", ((CommentDTO) a).getId()), new ThrowableUtil.ThrowableSub());

            } else if (commentActionButtonClickEvent.getActionButton() == CommentActionButtonClickEvent.ActionButton.DISLIKE) {
                UserCommentActivity rating;
                if (((CommentDTO) a).getUserCommentActivity() != null && ((CommentDTO) a).getUserCommentActivity().getRating() == -1L) {
                    rating = new UserCommentActivity(((CommentDTO) a).getId(), 0);
                } else {
                    rating = new UserCommentActivity(((CommentDTO) a).getId(), -1);
                }
                mDataManager.getPbService()
                        .addCommentReaction("0", ((CommentDTO) a).getId(), rating)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new UserCommentActivitySub("", ((CommentDTO) a).getId()), new ThrowableUtil.ThrowableSub());
            }
        }
    }

    public void subCommentAction(SubCommentActionButtonClickEvent subCommentActionButtonClickEvent) {
        AnyItem a = anyItemList.get(subCommentActionButtonClickEvent.getAdapterPosition());
        if (a instanceof SubCommentDTO) {
            if (subCommentActionButtonClickEvent.getActionButton() == SubCommentActionButtonClickEvent.ActionButton.LIKE) {
                UserCommentActivity rating;
                if (((SubCommentDTO) a).getUserSubCommentActivity() != null && ((SubCommentDTO) a).getUserSubCommentActivity().getRating() == 1L) {
                    rating = new UserCommentActivity(((SubCommentDTO) a).getId(), 0);
                } else {
                    rating = new UserCommentActivity(((SubCommentDTO) a).getId(), 1);
                }
                mDataManager.getPbService()
                        .addCommentReaction("0", ((SubCommentDTO) a).getId(), rating)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new UserCommentActivitySub("", ((SubCommentDTO) a).getId()), new ThrowableUtil.ThrowableSub());

            } else if (subCommentActionButtonClickEvent.getActionButton() == SubCommentActionButtonClickEvent.ActionButton.DISLIKE) {
                UserCommentActivity rating;
                if (((SubCommentDTO) a).getUserSubCommentActivity() != null && ((SubCommentDTO) a).getUserSubCommentActivity().getRating() == -1L) {
                    rating = new UserCommentActivity(((SubCommentDTO) a).getId(), 0);
                } else {
                    rating = new UserCommentActivity(((SubCommentDTO) a).getId(), -1);
                }
                mDataManager.getPbService()
                        .addCommentReaction("0", ((SubCommentDTO) a).getId(), rating)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new UserCommentActivitySub("", ((SubCommentDTO) a).getId()), new ThrowableUtil.ThrowableSub());
            }
        }
    }

    private class UserCommentActivitySub implements Consumer<ResponseDTO<UserCommentActivity>> {
        String articleID;
        String commentID;

        public UserCommentActivitySub(String articleID, String commentID) {
            this.articleID = articleID;
            this.commentID = commentID;
        }

        @Override
        public void accept(@NonNull ResponseDTO<UserCommentActivity> userCommentActivityResponseDTO) throws Exception {
            eventBus.post(new CommentActivityResponseEvent(userCommentActivityResponseDTO.getData(), articleID, commentID));
        }
    }

    public UserDTO getUserProfile(int adapterPosition) {
        AnyItem anyItem = this.anyItemList.get(adapterPosition);
        UserDTO userDTO = null;
        if (anyItem != null && anyItem instanceof ArticleDTO) {
            userDTO = ((ArticleDTO) anyItem).getCreatedBy();
        }
        if (anyItem != null && anyItem instanceof CommentDTO) {
            userDTO = ((CommentDTO) anyItem).getCreatedBy();
        }
        if (anyItem != null && anyItem instanceof SubCommentDTO) {
            userDTO = ((SubCommentDTO) anyItem).getCreatedBy();
        }

        return userDTO;
    }

    public MyProgressBar getProgressObject() {
        if (this.anyItemList != null && this.anyItemList.get(this.anyItemList.size() - 1) instanceof MyProgressBar) {
            return ((MyProgressBar) this.anyItemList.get(this.anyItemList.size() - 1));
        } else {
            return null;
        }
    }

    public void resetProgressObject() {
        if (this.anyItemList != null && this.anyItemList.get(this.anyItemList.size() - 1) instanceof MyProgressBar) {
            ((MyProgressBar) this.anyItemList.get(this.anyItemList.size() - 1)).setMessage("");
            notifyItemChanged(this.anyItemList.size() - 1);
        }
    }

    public void setErrorMessage(String message) {
        AnyItem anyItem = this.anyItemList.get(this.anyItemList.size() - 1);
        if (anyItem != null && anyItem instanceof MyProgressBar) {
            ((MyProgressBar) this.anyItemList.get(this.anyItemList.size() - 1)).setErrorMsg(message);
            notifyItemChanged(this.anyItemList.size() - 1);
        }
    }

    public void setLoadingMessage(String message) {
        AnyItem anyItem = this.anyItemList.get(this.anyItemList.size() - 1);
        if (anyItem != null && anyItem instanceof MyProgressBar) {
            ((MyProgressBar) this.anyItemList.get(this.anyItemList.size() - 1)).setMessage(message);
            notifyItemChanged(this.anyItemList.size() - 1);
        }
    }

    private int getCommentById(String commentId) {
        for (int i = 0; i < anyItemList.size(); i++) {
            if (anyItemList.get(i) instanceof CommentDTO) {
                if (((CommentDTO) anyItemList.get(i)).getId().equalsIgnoreCase(commentId)) {
                    return i;
                }
            }
        }
        return 0;
    }

    public void addItems(List<? extends AnyItem> anyItemList) {
        int startIndex = this.anyItemList.size() - 1;
        this.anyItemList.addAll(startIndex, anyItemList);
        notifyItemRangeInserted(startIndex + 1, anyItemList.size());
    }

    public void updateUserArticleActivity(List<UserArticleActivity> userArticleActivities) {
        for (int i = 0; i < this.anyItemList.size(); i++) {
            if (this.anyItemList.get(i) instanceof ArticleDTO) {
                for (UserArticleActivity userArticleActivity : userArticleActivities) {
                    if (((ArticleDTO) this.anyItemList.get(i)).getId().equalsIgnoreCase(userArticleActivity.getArticleID())) {
                        ((ArticleDTO) this.anyItemList.get(i)).setUserArticleActivity(userArticleActivity);
                        notifyItemChanged(i);
                    }
                }
            }
        }
    }

    public void removeItems() {
        anyItemList.clear();
        anyItemList.add(new MyProgressBar(""));
        notifyDataSetChanged();
    }

    public void removeUserArticleActivity() {
        for (int i = 0; i < this.anyItemList.size(); i++) {
            if (this.anyItemList.get(i) instanceof ArticleDTO) {
                ((ArticleDTO) this.anyItemList.get(i)).setUserArticleActivity(null);
                notifyItemChanged(i);
            }
        }
    }


}
