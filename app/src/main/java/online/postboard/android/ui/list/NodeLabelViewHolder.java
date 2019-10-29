package online.postboard.android.ui.list;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import online.postboard.android.ui.base.AnyItemViewHolder;
import online.postboard.android.R;
import online.postboard.android.data.model.NodeLabel;
import online.postboard.android.ui.base.ScreenType;
import online.postboard.android.util.RxEventBus;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Android SD-1 on 21-04-2017.
 */

public class NodeLabelViewHolder extends AnyItemViewHolder<NodeLabel> {

    @BindView(R.id.item_node_label_text_view)
    TextView item_node_label_text_view;

    public NodeLabelViewHolder(ViewGroup parent, RxEventBus eventBus) {
        super(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_node_label, parent, false));
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bind(NodeLabel nodeLabel, ScreenType screenType) {
        if (nodeLabel.getMessage() != null && !nodeLabel.getMessage().isEmpty()) {
            item_node_label_text_view.setText(nodeLabel.getMessage());
        }
    }

}
