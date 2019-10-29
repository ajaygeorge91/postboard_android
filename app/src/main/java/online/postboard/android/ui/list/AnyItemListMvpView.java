package online.postboard.android.ui.list;

import java.util.List;

import online.postboard.android.data.model.AnyItem;
import online.postboard.android.ui.base.MvpView;

public interface AnyItemListMvpView extends MvpView {

    void showItems(List<? extends AnyItem> anyItemList);

    void showItemsEmpty();

    void showError(String message);

}
