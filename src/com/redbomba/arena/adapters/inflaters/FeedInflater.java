package com.redbomba.arena.adapters.inflaters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.redbomba.arena.R;
import com.redbomba.arena.adapters.BaseInflaterAdapter;
import com.redbomba.arena.adapters.FeedItemData;
import com.redbomba.arena.adapters.IAdapterViewInflater;

/**
 * Created by Sangmok on 2014. 10. 8..
 */
public class FeedInflater implements IAdapterViewInflater<FeedItemData> {

    @Override
    public View inflate(final BaseInflaterAdapter<FeedItemData> adapter, final int pos, View convertView, ViewGroup parent)
    {
        ViewHolder holder;

        if (convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.list_item_feed, parent, false);
            holder = new ViewHolder(convertView);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        final FeedItemData item = adapter.getTItem(pos);
        holder.updateDisplay(item);

        return convertView;
    }

    private class ViewHolder
    {
        private View m_rootView;

        public ViewHolder(View rootView)
        {
            m_rootView = rootView;
            rootView.setTag(this);
        }

        public void updateDisplay(FeedItemData item)
        {
            AQuery aq = new AQuery(m_rootView);
            ImageOptions options = new ImageOptions();
            options.round = 1000;
            aq.id(R.id.feed_user_icon).image(item.getText1(), options);
            aq.id(R.id.feed_user_name).text(item.getText2());
            aq.id(R.id.feed_group_name).text(item.getText3());
            aq.id(R.id.feed_content).text(item.getText4());
            aq.id(R.id.feed_update).text(item.getText5());
            aq.id(R.id.feed_replynum).text(item.getText6());
            aq.id(R.id.feed_like_len).text(item.getText7());
        }

    }
}
