package online.postboard.android.ui.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import online.postboard.android.data.model.AnyItem;

/**
 * Created by Android SD-1 on 21-04-2017.
 */

public abstract class AnyItemViewHolder<T extends AnyItem> extends RecyclerView.ViewHolder {

    public AnyItemViewHolder(View view) {
        super(view);
    }

    public abstract void bind(T anyItem, ScreenType screenType);

}