package com.redbomba.arena.adapters.inflaters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.androidquery.AQuery;
import com.redbomba.arena.R;
import com.redbomba.arena.adapters.BaseInflaterAdapter;
import com.redbomba.arena.adapters.IAdapterViewInflater;
import com.redbomba.arena.adapters.TimeSelectData;

/**
 * Created by Sangmok on 2014. 11. 3..
 */
public class TimeSelectInflater implements IAdapterViewInflater<TimeSelectData> {

    @Override
    public View inflate(final BaseInflaterAdapter<TimeSelectData> adapter, final int pos, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.list_item_time_selection, parent, false);
            holder = new ViewHolder(convertView);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        final TimeSelectData item = adapter.getTItem(pos);
        AQuery aq = new AQuery(convertView);
        if (pos == 0) {
            aq.id(R.id.time_select_container).margin(0,0,0,0);
        }
        holder.updateDisplay(item);

        return convertView;
    }

    private class ViewHolder
    {
        private View m_rootView;
        public ViewHolder(View rootView) {
            m_rootView = rootView;
        }

        public void updateDisplay(TimeSelectData item) {
            AQuery aq = new AQuery(m_rootView);
            aq.id(R.id.time_select_hour).text(item.getText1());
            aq.id(R.id.time_select_color).background(item.getText2());
        }
    }
}
