package com.master;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

/**
 * @author Daisw
 */
public class ListFragment1 extends ListFragment {

    /**
     * {@inheritDoc}
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        ListView listView = getListView();

        View header = getLayoutInflater(savedInstanceState).inflate(R.layout.act_main_header, null);
        header.setVisibility(View.INVISIBLE);
        listView.addHeaderView(header);

        HeaderView headerView = (HeaderView) getActivity().findViewById(R.id.carousel_header);
        listView.setOnScrollListener(new ScrollListener(headerView, HeaderView.TAB_INDEX_FIRST));
        listView.setSelector(android.R.color.transparent);
        listView.setDivider(null);
        listView.setScrollingCacheEnabled(false);
        listView.setFadingEdgeLength(0);

        setListAdapter(new DataAdapter(getActivity(), 10));
    }
}
