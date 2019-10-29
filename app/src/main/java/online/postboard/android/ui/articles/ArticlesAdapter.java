package online.postboard.android.ui.articles;

import android.app.Activity;

import java.util.List;

import javax.inject.Inject;

import online.postboard.android.data.DataManager;
import online.postboard.android.data.model.AnyItem;
import online.postboard.android.data.model.ArticleDTO;
import online.postboard.android.data.model.UserArticleActivity;
import online.postboard.android.ui.base.BaseAdapter;
import online.postboard.android.util.RxEventBus;
import online.postboard.android.data.model.MyProgressBar;

public class ArticlesAdapter extends BaseAdapter {

    @Inject
    public ArticlesAdapter(Activity activity, RxEventBus eventBus, DataManager dataManager) {
        super(activity, dataManager, eventBus);
        anyItemList.add(new MyProgressBar(""));
    }

}
