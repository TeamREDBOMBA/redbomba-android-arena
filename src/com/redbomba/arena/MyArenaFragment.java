package com.redbomba.arena;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.redbomba.arena.adapters.BaseInflaterAdapter;
import com.redbomba.arena.adapters.IAdapterViewInflater;
import com.redbomba.arena.adapters.TicketItemData;
import com.redbomba.arena.db.DB;
import org.json.JSONArray;
import org.json.JSONException;

import static com.redbomba.arena.db.DB.getJson;

/**
 * Created by Sangmok on 2014. 9. 3..
 */
public class MyArenaFragment extends Fragment {
    private String jo = null;

    @Override
    public View onCreateView(LayoutInflater lf, ViewGroup vg, Bundle b) {
        View rootView = lf.inflate(R.layout.fragment_myarena, vg, false);
        ListView ticket_list = (ListView) rootView.findViewById(R.id.ticket_list);

//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

//        int uid = prefs.getInt("uid", 0);
//        System.out.println("$@#^#$%^&%$*$^@#$%^&$#^^#^%@%"+uid);
        String url = "http://redbomba.net/mobile/?mode=getArenaTicket&id="+MainActivity.uid;

        String items = getJson(url);
        JSONArray json = null;
        try {
            json = new JSONArray(items);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String item1 = null;
        try {
            item1 = json.getJSONObject(0).getString("league_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        DB.getJsonObj_as_String(getJson(url), 0, "league_name");

        ticket_list.addHeaderView(new View(getActivity()));
        ticket_list.addFooterView(new View(getActivity()));

        BaseInflaterAdapter<TicketItemData> adapter = new BaseInflaterAdapter<TicketItemData>(new TicketInflater());
        for (int i = 0; i < DB.get_length_of_jsonArray(); i++) {
            //add items on a card
            TicketItemData data = new TicketItemData(
                DB.getJsonObj_as_String(getJson(url), 0, "league_name"),
                DB.getJsonObj_as_String(getJson(url), 0, "group"),
                DB.getJsonObj_as_String(getJson(url), 0, "round")
            );
            adapter.addItem(data, false);
        }
        ticket_list.setAdapter(adapter);

//        AQuery aq = new AQuery(getActivity());
//        aq.ajax(url, JSONArray.class, new AjaxCallback<JSONArray>() {
//
//            @Override
//            public void callback(String url, JSONArray json, AjaxStatus status) {
//                try {
//                    jo = json.getJSONObject(0).toString();
//                    System.out.println(jo);
//                } catch (Exception e) {
//                }
//            }
//        });


        return rootView;
    }

    public class TicketInflater implements IAdapterViewInflater<TicketItemData> {

        @Override
        public View inflate(final BaseInflaterAdapter<TicketItemData> adapter, final int pos, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                convertView = inflater.inflate(R.layout.list_item_ticket, parent, false);
                holder = new ViewHolder(convertView);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final TicketItemData item = adapter.getTItem(pos);
            holder.updateDisplay(item);

            return convertView;
        }

        private class ViewHolder {
            private View m_rootView;
            private TextView ticket_leage_name;
            private TextView ticket_team_name;
            private TextView ticket_round;

            public ViewHolder(View rootView) {
                m_rootView = rootView;
                ticket_leage_name = (TextView) m_rootView.findViewById(R.id.ticket_league_name);
                ticket_team_name = (TextView) m_rootView.findViewById(R.id.ticket_team_name);
                ticket_round = (TextView) m_rootView.findViewById(R.id.ticket_round);

            }

            public void updateDisplay(TicketItemData item) {
                ticket_leage_name.setText(item.getText1());
                ticket_team_name.setText(item.getText2());
                ticket_round.setText(item.getText3());
            }
        }
    }
}
