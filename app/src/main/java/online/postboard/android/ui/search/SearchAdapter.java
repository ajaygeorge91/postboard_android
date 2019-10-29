package online.postboard.android.ui.search;

import android.app.Activity;

import javax.inject.Inject;

import online.postboard.android.data.DataManager;
import online.postboard.android.data.model.MyProgressBar;
import online.postboard.android.ui.base.BaseAdapter;
import online.postboard.android.util.RxEventBus;

public class SearchAdapter extends BaseAdapter {

    @Inject
    public SearchAdapter(Activity activity, RxEventBus eventBus, DataManager dataManager) {
        super(activity, dataManager, eventBus);
        anyItemList.add(new MyProgressBar(""));
    }

}
