package com.redbomba.arena;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.redbomba.arena.adapters.BaseInflaterAdapter;
import com.redbomba.arena.adapters.CardItemData;
import com.redbomba.arena.adapters.inflaters.CardInflater;
import com.redbomba.arena.db.DB;

/**
 * Created by Sangmok on 2014. 9. 1..
 */
public class ArenaListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater lf, ViewGroup vg, Bundle b) {
        View rootView = lf.inflate(R.layout.fragment_cardlist, vg, false);
        ListView m_list = (ListView) rootView.findViewById(R.id.arena_cardlist);

        m_list.addHeaderView(new View(getActivity()));
        m_list.addFooterView(new View(getActivity()));

        BaseInflaterAdapter<CardItemData> adapter = new BaseInflaterAdapter<CardItemData>(new CardInflater());
        for (int i = 0; i < DB.get_length_of_jsonArray(); i++) {
            //add items on a card
            CardItemData data = new CardItemData(
                    DB.get_league_info(i, "name"),
                    "참가자 레벨: " + DB.get_league_info(i, "level"),
                    DB.d_day(i),
                    "http://redbomba.net" + DB.get_league_info(i, "poster"),
                    100 * DB.get_league_team(DB.get_league_round(DB.get_league_info(i, "id"))) / Integer.parseInt(DB.get_league_info(i, "max_team")),
                    "참가자 현황: " + String.valueOf(DB.get_league_team(DB.get_league_round(DB.get_league_info(i, "id")))) + "/" + DB.get_league_info(i, "max_team")
            );
            adapter.addItem(data, false);
        }
        m_list.setAdapter(adapter);

        m_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent intent = new Intent(getActivity(), CardDetailActivity.class);
                startActivity(intent);
            }
        });
        m_list.setItemsCanFocus(true);
        m_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

            if(vg == null) {
                return null;
            }
        return rootView;
    }

}
