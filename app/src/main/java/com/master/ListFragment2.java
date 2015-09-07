package com.master;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

/**
 * @author Daisw
 */
public class ListFragment2 extends ListFragment implements ScrollListener.OnScrollListener {

    @Override
    public void onScroll(float distance) {

        ((MainFragmentActivity) getActivity()).Y_COORDINATE[1] = distance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        ListView listView = getListView();
        listView.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));

        View header = getLayoutInflater(savedInstanceState).inflate(R.layout.act_main_header, null);
        header.setVisibility(View.INVISIBLE);
        listView.addHeaderView(header);

        HeaderView headerView = (HeaderView) getActivity().findViewById(R.id.carousel_header);
        ScrollListener scrollLis = new ScrollListener(headerView, HeaderView.TAB_INDEX_SECOND);
        scrollLis.setOnScrollListener(this);
        listView.setOnScrollListener(scrollLis);
        listView.setSelector(android.R.color.transparent);
        listView.setDivider(null);
        listView.setScrollingCacheEnabled(false);
        listView.setFadingEdgeLength(0);

        setListAdapter(new DataAdapter(getActivity(), 20));
    }
}
