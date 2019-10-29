package online.postboard.android.ui.list;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import online.postboard.android.ui.base.BaseFragment;
import online.postboard.android.util.widgets.EndlessRecyclerViewScrollListener;
import online.postboard.android.R;
import online.postboard.android.data.model.AnyItem;
import online.postboard.android.util.DialogFactory;

public class AnyItemListFragment extends BaseFragment implements AnyItemListMvpView {

    @Inject
    AnyItemListPresenter anyItemListPresenter;

    @Inject
    AnyItemAdapter anyItemAdapter;

    @BindView(R.id.list)
    RecyclerView recyclerView;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private EndlessRecyclerViewScrollListener scrollListener;

    public AnyItemListFragment() {
    }

    public static AnyItemListFragment newInstance(int sectionNumber) {
        AnyItemListFragment fragment = new AnyItemListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        anyItemListPresenter.attachView(this);
        View view = inflater.inflate(R.layout.fragment_article_list, container, false);
        ButterKnife.bind(this, view);

        Context context = view.getContext();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {

            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                anyItemListPresenter.loadArticles(page);
            }
        };
        recyclerView.setAdapter(anyItemAdapter);
        anyItemListPresenter.loadArticles(0);

        recyclerView.addOnScrollListener(scrollListener);


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        anyItemListPresenter.detachView();
    }


    @Override
    public void showError(String errorMessage) {
        anyItemAdapter.setLoadingMessage(errorMessage);
        DialogFactory.INSTANCE.createGenericErrorDialog(getActivity(), errorMessage)
                .show();
    }

    @Override
    public void showItems(List<? extends AnyItem> anyItemList) {
        anyItemAdapter.addItems(anyItemList);
    }

    @Override
    public void showItemsEmpty() {
        anyItemAdapter.setLoadingMessage(getString(R.string.empty_items));
        Toast.makeText(getActivity(), R.string.empty_items, Toast.LENGTH_SHORT).show();
    }

}
