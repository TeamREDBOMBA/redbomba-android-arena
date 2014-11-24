package com.redbomba.arena.carddetail;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import com.androidquery.AQuery;
import com.redbomba.arena.R;
import com.redbomba.arena.adapters.BaseInflaterAdapter;
import com.redbomba.arena.adapters.JoinedTeamItemData;
import com.redbomba.arena.adapters.inflaters.JoinedTeamInflater;
import com.redbomba.arena.urls.Urls;

import static com.redbomba.arena.db.DB.*;

/**
 * Created by Sangmok on 2014. 9. 16..
 */
public class JoinedTeamActivity extends Activity {

    AQuery aq = new AQuery(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joinedteam);

        new JoinedTeam().execute();
    }

    private class JoinedTeam extends AsyncTask<Void, String, String> {

        @Override
        protected String doInBackground(Void... params) {
            Bundle bundle = getIntent().getExtras();
            String leagueInfo = bundle.getString("leagueInfo");
            ImageView poster = (ImageView) findViewById(R.id.joined_team_poster);

            aq.id(R.id.joinedteam_league_name).text(getJsonObj_as_String(leagueInfo, 0, "name"));
            aq.id(R.id.joined_team_poster).image(Urls.MIDEA + getJsonObj_as_String(leagueInfo, 0, "poster"));
            poster.setScaleType(ImageView.ScaleType.CENTER_CROP);
            String url = Urls.LEAGUE_TEAM + getJsonObj_as_String(leagueInfo, 0, "firstround");
            return getJson(url);
        }

        @Override
        protected void onPostExecute(String result) {
            ListView team_list_even = (ListView) findViewById(R.id.team_list_even);
            ListView team_list_odd = (ListView) findViewById(R.id.team_list_odd);
            BaseInflaterAdapter<JoinedTeamItemData> adapter_even = new BaseInflaterAdapter<JoinedTeamItemData>(new JoinedTeamInflater());
            BaseInflaterAdapter<JoinedTeamItemData> adapter_odd = new BaseInflaterAdapter<JoinedTeamItemData>(new JoinedTeamInflater());
            for (int i=0; i<getJsonLength(result); i++) {
                JoinedTeamItemData data = new JoinedTeamItemData(
                        Urls.GROUP_ICON + getJsonObj_as_String(result, i, "groupicon"),
                        getJsonObj_as_String(result, i, "gname")
                );
                if (i%2 == 0) {
                    adapter_even.addItem(data, false);
                }
                else {
                    adapter_odd.addItem(data,false);
                }
            }
            team_list_even.setAdapter(adapter_even);
            team_list_odd.setAdapter(adapter_odd);
        }
    }
}
