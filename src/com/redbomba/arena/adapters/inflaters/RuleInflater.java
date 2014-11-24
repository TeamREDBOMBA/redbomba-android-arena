package com.redbomba.arena.adapters.inflaters;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.androidquery.AQuery;
import com.redbomba.arena.R;
import com.redbomba.arena.adapters.BaseInflaterAdapter;
import com.redbomba.arena.adapters.IAdapterViewInflater;
import com.redbomba.arena.adapters.JoinedTeamItemData;
import com.redbomba.arena.adapters.RuleItemData;

/**
 * Created by Sangmok on 2014. 9. 19..
 */
public class RuleInflater implements IAdapterViewInflater<RuleItemData> {

    @Override
    public View inflate(final BaseInflaterAdapter<RuleItemData> adapter, final int pos, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.rule_list, parent, false);
            holder = new ViewHolder(convertView);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        final RuleItemData item = adapter.getTItem(pos);
        holder.updateDisplay(item);

        return convertView;
    }

    private class ViewHolder
    {
        private View m_rootView;
        private TextView rule_title;
        private TextView rule_contents;

        public ViewHolder(View rootView) {
            m_rootView = rootView;
            rule_title = (TextView) m_rootView.findViewById(R.id.rule_title);
            rule_contents = (TextView) m_rootView.findViewById(R.id.rule_contents);
        }

        public void updateDisplay(RuleItemData item) {
//            AQuery aq = new AQuery(m_rootView);
//            aq.id(R.id.rule_title).text(item.getText1());
//            aq.id(R.id.rule_expander).image(item.getText2());
//            aq.id(R.id.rule_contents).text(item.getText3());

            rule_title.setText(Html.fromHtml(item.getText1()));
            rule_contents.setText(Html.fromHtml(item.getText3()));
            rule_title.setOnClickListener(new Expander(rule_contents));

        }
    }

    class Expander implements View.OnClickListener {
        TextView tv;

        Expander (TextView textview) {
            tv = textview;
        }
        @Override
        public void onClick(View v) {
            if (tv.getVisibility() == View.VISIBLE) {
                tv.setVisibility(View.GONE);
            }
            else {
//                int height_in_pixels = tv.getLineCount() * tv.getLineHeight();
//                tv.setHeight(height_in_pixels);
                tv.setVisibility(View.VISIBLE);
            }
        }
    }
}
