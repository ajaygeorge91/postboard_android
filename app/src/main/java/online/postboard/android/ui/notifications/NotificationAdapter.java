package online.postboard.android.ui.notifications;

import android.app.Activity;

import online.postboard.android.data.DataManager;
import online.postboard.android.data.model.AnyItem;
import online.postboard.android.data.model.MyProgressBar;
import online.postboard.android.data.model.NotificationDTO;
import online.postboard.android.ui.base.BaseAdapter;
import online.postboard.android.util.RxEventBus;
import online.postboard.android.util.apputils.NotificationUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class NotificationAdapter extends BaseAdapter {

    @Inject
    public NotificationAdapter(Activity activity, DataManager dataManager, RxEventBus eventBus) {
        super(activity, dataManager, eventBus);
        anyItemList.add(new MyProgressBar(""));
    }

    public void addNotifications(List<? extends AnyItem> anyItemList) {
        this.anyItemList.addAll(this.anyItemList.size() - 1, anyItemList);
        notifyDataSetChanged();
    }

    public void removeItems() {
        this.anyItemList.clear();
        anyItemList.add(new MyProgressBar(""));
        notifyDataSetChanged();
    }

    public void readItems() {
        for (int i = 0; i < this.anyItemList.size(); i++) {
            if (this.anyItemList.get(i) instanceof NotificationDTO) {
                ((NotificationDTO) this.anyItemList.get(i)).setRead(true);
            }
        }
        notifyDataSetChanged();
    }

    public void updateNotifications(List<NotificationDTO> anyItemList) {
        outerLoop:
        for (NotificationDTO n : anyItemList) {
            for (int i = 0; i < this.anyItemList.size(); i++) {
                if (this.anyItemList.get(i) instanceof NotificationDTO) {
                    if (n.getId().equalsIgnoreCase(((NotificationDTO) this.anyItemList.get(i)).getId())) {
                        this.anyItemList.set(i, n);
                        continue outerLoop;
                    }
                }
            }
            this.anyItemList.add(0, n);
        }
        notifyDataSetChanged();
    }

    public String getUnreadCount() {
        return NotificationUtils.getCount(this.anyItemList);
    }

    public List<NotificationDTO> getItems() {
        List<NotificationDTO> notificationDTOList = new ArrayList<>();
        for (AnyItem n : this.anyItemList) {
            if (n instanceof NotificationDTO) {
                notificationDTOList.add((NotificationDTO) n);
            }
        }
        return notificationDTOList;
    }

}
