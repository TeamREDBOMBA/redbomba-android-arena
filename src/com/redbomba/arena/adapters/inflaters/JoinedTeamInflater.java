package com.redbomba.arena.adapters.inflaters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.redbomba.arena.R;
import com.redbomba.arena.adapters.BaseInflaterAdapter;
import com.redbomba.arena.adapters.IAdapterViewInflater;
import com.redbomba.arena.adapters.JoinedTeamItemData;

/**
 * Created by Sangmok on 2014. 9. 17..
 */
public class JoinedTeamInflater implements IAdapterViewInflater<JoinedTeamItemData> {

    @Override
    public View inflate(final BaseInflaterAdapter<JoinedTeamItemData> adapter, final int pos, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.joinedteam_list, parent, false);
            holder = new ViewHolder(convertView);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        final JoinedTeamItemData item = adapter.getTItem(pos);
        holder.updateDisplay(item);

        return convertView;
    }

    private class ViewHolder
    {
        private View m_rootView;
//        private ImageView m_imageView;
//        private TextView m_textView1;

        public ViewHolder(View rootView) {
            m_rootView = rootView;
//            m_imageView = (ImageView) m_rootView.findViewById(R.id.team_icon);
//            m_textView1 = (TextView) m_rootView.findViewById(R.id.team_name);
        }

        public void updateDisplay(JoinedTeamItemData item) {
            AQuery aq = new AQuery(m_rootView);
            ImageOptions options = new ImageOptions();
            options.round = 1000;
            aq.id(R.id.team_icon).image(item.getText1(), options);
            aq.id(R.id.team_name).text(item.getText2());
        }
    }
}
