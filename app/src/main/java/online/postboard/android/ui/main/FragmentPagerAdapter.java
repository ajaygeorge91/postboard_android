package online.postboard.android.ui.main;

import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import javax.inject.Inject;

import online.postboard.android.ui.articles.ArticleListFragment;
import online.postboard.android.ui.myprofile.MyProfileFragment;
import online.postboard.android.ui.notifications.NotificationFragment;

/**
 * Created by ajayg on 8/3/2017.
 */

public class FragmentPagerAdapter extends FragmentStatePagerAdapter {

    SparseArray<CustomFragment> pagerFragmentReference;

    @Inject
    FragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        pagerFragmentReference = new SparseArray<>();
    }

    @Override
    public CustomFragment getItem(int fragmentItem) {
        CustomFragment fragment;
        if (fragmentItem == FragmentItem.ARTICLES) {
            fragment = ArticleListFragment.newInstance(ArticleListFragment.ArticleType.HOT);
        } else if (fragmentItem == FragmentItem.PROFILE) {
            fragment = MyProfileFragment.newInstance();
        } else {
            fragment = NotificationFragment.newInstance(null);
        }
        return fragment;
    }

    public CustomFragment getFragment(int tabPosition) {
        return pagerFragmentReference.get(tabPosition);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        CustomFragment fragment = (CustomFragment) super.instantiateItem(container, position);
        pagerFragmentReference.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        pagerFragmentReference.remove(position);
    }

    @Override
    public int getCount() {
        return 2;
    }
}
