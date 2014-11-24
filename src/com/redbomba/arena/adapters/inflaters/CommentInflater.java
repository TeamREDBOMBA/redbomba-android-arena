package com.redbomba.arena.adapters.inflaters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.redbomba.arena.R;
import com.redbomba.arena.adapters.BaseInflaterAdapter;
import com.redbomba.arena.adapters.CommentItemData;
import com.redbomba.arena.adapters.IAdapterViewInflater;

/**
 * Created by Sangmok on 2014. 10. 8..
 */
public class CommentInflater implements IAdapterViewInflater<CommentItemData> {

    @Override
    public View inflate(final BaseInflaterAdapter<CommentItemData> adapter, final int pos, View convertView, ViewGroup parent)
    {
        ViewHolder holder;

        if (convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.list_item_comment, parent, false);
            holder = new ViewHolder(convertView);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        final CommentItemData item = adapter.getTItem(pos);
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

        public void updateDisplay(CommentItemData item)
        {
            AQuery aq = new AQuery(m_rootView);
            ImageOptions options = new ImageOptions();
            options.round = 1000;
            aq.id(R.id.comment_user_icon).image(item.getText1(), options);
            aq.id(R.id.comment_user_name).text(item.getText2());
            aq.id(R.id.comment_content).text(item.getText4());
            aq.id(R.id.comment_update).text(item.getText3());

        }

    }
}
