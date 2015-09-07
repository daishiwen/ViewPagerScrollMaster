package com.master;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by KEVIN.DAI on 15/9/7.
 */
public class DataAdapter extends BaseAdapter {

    private Context mContext;
    private int mCount;

    public DataAdapter(Context context, int count) {

        mContext = context;
        mCount = count;
    }

    @Override
    public int getCount() {

        return mCount;
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public Object getItem(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {

            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_test, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {

            holder = (ViewHolder) convertView.getTag();
        }
        holder.invalidate(position);
        return convertView;
    }

    private class ViewHolder {

        private TextView mTvTest;

        public ViewHolder(View convertView) {

            mTvTest = (TextView) convertView.findViewById(R.id.tvTest);
        }

        public void invalidate(int posi) {

            mTvTest.setText("position: " + (posi + 1));
        }
    }
}
