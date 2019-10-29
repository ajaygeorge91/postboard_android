package online.postboard.android.ui.signin;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import javax.inject.Inject;

/**
 * Created by Android SD-1 on 17-03-2017.
 */

public class SignInPagerAdapter extends FragmentStatePagerAdapter {

    @Inject
    public SignInPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case FRAGMENT_ITEM.LOGIN:
                return LoginFragment.newInstance();
            case FRAGMENT_ITEM.REGISTER:
                return RegisterFragment.newInstance();
        }
        return LoginFragment.newInstance();

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case FRAGMENT_ITEM.LOGIN:
                return "LOGIN";
            case FRAGMENT_ITEM.REGISTER:
                return "REGISTER";
        }
        return ".......";
    }

    final class FRAGMENT_ITEM {
        public static final int LOGIN = 0;
        public static final int REGISTER = 1;
    }

}