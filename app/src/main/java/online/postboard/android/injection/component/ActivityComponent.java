package online.postboard.android.injection.component;

import dagger.Subcomponent;
import online.postboard.android.injection.PerActivity;
import online.postboard.android.injection.module.ActivityModule;
import online.postboard.android.ui.articlenew.NewArticleActivity;
import online.postboard.android.ui.detail.ArticleDetailActivity;
import online.postboard.android.ui.main.MainActivity;
import online.postboard.android.ui.notifications.NotificationActivity;
import online.postboard.android.ui.search.SearchActivity;
import online.postboard.android.ui.signin.SignInActivity;
import online.postboard.android.ui.subcomment.SubCommentActivity;
import online.postboard.android.ui.userprofile.UserProfileActivity;

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity mainActivity);

    void inject(SignInActivity signInActivity);

    void inject(ArticleDetailActivity articleDetailActivity1);

    void inject(NewArticleActivity newArticleActivity);

    void inject(SubCommentActivity subCommentActivity);

    void inject(UserProfileActivity userProfileActivity);

    void inject(NotificationActivity notificationActivity);

    void inject(SearchActivity searchActivity);
}
