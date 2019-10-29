package online.postboard.android.injection.component;

/**
 * Created by Android SD-1 on 18-03-2017.
 */


import dagger.Subcomponent;

import online.postboard.android.ui.notifications.NotificationFragment;
import online.postboard.android.injection.PerFragment;
import online.postboard.android.injection.module.FragmentModule;
import online.postboard.android.ui.articles.ArticleListFragment;
import online.postboard.android.ui.myprofile.MyProfileFragment;
import online.postboard.android.ui.subcomment.SubCommentActivity;
import online.postboard.android.ui.list.AnyItemListFragment;
import online.postboard.android.ui.signin.LoginFragment;
import online.postboard.android.ui.signin.RegisterFragment;

/**
 * This component inject dependencies to all Fragments across the application
 */
@PerFragment
@Subcomponent(modules = FragmentModule.class)
public interface FragmentComponent {

    void inject(AnyItemListFragment anyItemListFragment);

    void inject(ArticleListFragment articleListFragment);

    void inject(LoginFragment loginFragment);

    void inject(RegisterFragment registerFragment);

    void inject(SubCommentActivity subCommentActivity);

    void inject(MyProfileFragment myProfileFragment);

    void inject(NotificationFragment notificationFragment);
}
