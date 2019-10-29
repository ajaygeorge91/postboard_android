package online.postboard.android.ui.list;

import android.app.Activity;

import online.postboard.android.data.DataManager;
import online.postboard.android.data.model.AnyItem;
import online.postboard.android.data.model.MyProgressBar;
import online.postboard.android.ui.base.BaseAdapter;
import online.postboard.android.util.RxEventBus;

import java.util.List;

import javax.inject.Inject;

public class AnyItemAdapter extends BaseAdapter {

    @Inject
    public AnyItemAdapter(Activity activity, DataManager dataManager, RxEventBus eventBus) {
        super(activity, dataManager, eventBus);
        anyItemList.add(new MyProgressBar(""));
    }

    public void addItems(List<? extends AnyItem> anyItemList) {
        this.anyItemList.addAll(this.anyItemList.size() - 1, anyItemList);
        notifyDataSetChanged();
    }

}
