package com.redbomba.arena.adapters.inflaters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.androidquery.AQuery;
import com.redbomba.arena.R;
import com.redbomba.arena.adapters.BaseInflaterAdapter;
import com.redbomba.arena.adapters.IAdapterViewInflater;
import com.redbomba.arena.adapters.RewardItemData;

/**
 * Created by Sangmok on 2014. 9. 17..
 */
public class RewardInflater implements IAdapterViewInflater<RewardItemData> {

    @Override
    public View inflate(final BaseInflaterAdapter<RewardItemData> adapter, final int pos, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.reward_item_list, parent, false);
            holder = new ViewHolder(convertView);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        final RewardItemData item = adapter.getTItem(pos);
        AQuery aq = new AQuery(convertView);
        if (pos == 0) {
            aq.id(R.id.reward_container).background(R.drawable.padding_top_line_start);
            aq.id(R.id.medal).background(R.drawable.medal_1st);
        }
        else if (pos == 1) {
            aq.id(R.id.medal).background(R.drawable.medal_2nd);
        }
        else if (pos == 2) {
            aq.id(R.id.medal).background(R.drawable.medal_3rd);
        }
        holder.updateDisplay(item);

        return convertView;
    }

    private class ViewHolder
    {
        private View m_rootView;
//        private ImageView m_imageView;
        private TextView m_textView1;

        public ViewHolder(View rootView) {
            m_rootView = rootView;
//            m_imageView = (ImageView) m_rootView.findViewById(R.id.team_icon);
        }

        public void updateDisplay(RewardItemData item) {
            AQuery aq = new AQuery(m_rootView);
            aq.id(R.id.ranking).text(item.getText1());
            aq.id(R.id.reward_description).text(item.getText2());
        }
    }
}
