package com.redbomba.arena.main;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.redbomba.arena.carddetail.CardDetailActivity;
import com.redbomba.arena.R;
import com.redbomba.arena.adapters.BaseInflaterAdapter;
import com.redbomba.arena.adapters.CardItemData;
import com.redbomba.arena.adapters.inflaters.CardInflater;
import com.redbomba.arena.urls.Urls;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.redbomba.arena.db.DB.*;

/**
 * Created by Sangmok on 2014. 9. 1..
 */
public class LeagueListFragment extends Fragment {

    private String simpleLeagueInfo;
    private ListView m_list;

//    @Override
//    public void onResume(){
//        super.onResume();
//        getActivity().setTitle("대회 목록");
//    }

    @Override
    public View onCreateView(LayoutInflater lf, ViewGroup vg, Bundle b) {
        View rootView = lf.inflate(R.layout.fragment_cardlist, vg, false);
        m_list = (ListView) rootView.findViewById(R.id.arena_cardlist);


        new SimpleLeagueInfo().execute();

        if(vg == null) {
            return null;
        }

        return rootView;
    }

    private class SimpleLeagueInfo extends AsyncTask<Void, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            leagueTeam = DB.getJson("http://redbomba.net/mobile/?mode=getArenaTicket&id="+uid);
        }

        @Override
        protected String doInBackground(Void... params) {
//            int uid = MainActivity.sharedpreferences.getInt("uid", 0);
            simpleLeagueInfo = getJson(Urls.SIMPLE_LEAGUE_INFO);
            return simpleLeagueInfo;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            m_list.addHeaderView(new View(getActivity()));
            m_list.addFooterView(new View(getActivity()));

            BaseInflaterAdapter<CardItemData> adapter = new BaseInflaterAdapter<CardItemData>(new CardInflater());
            int lenOfJa = getJsonLength(result);
            for (int i = 0; i < lenOfJa; i++) {
                //add items on a card
                CardItemData data = new CardItemData(
                        getJsonObj_as_String(result, i, "name"),
                        getJsonObj_as_String(result, i, "wish_len"),
                        dayCalc(getJsonObj_as_String(result, i, "end_apply")),
                        Urls.MIDEA + getJsonObj_as_String(result, i, "poster"),
                        100 * Integer.parseInt(getJsonObj_as_String(result, i, "now_team")) / Integer.parseInt(getJsonObj_as_String(result, i, "max_team")),
                        getJsonObj_as_String(result, i, "now_team") + " 팀 참가중",
                        "정원: " + getJsonObj_as_String(result, i, "max_team") + " 팀"
                );
                adapter.addItem(data, false);
            }
            m_list.setAdapter(adapter);
            m_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(), CardDetailActivity.class);
                    int cardId = (int) id;
                    intent.putExtra("cardId", cardId);
                    intent.putExtra("leagueId", getJsonObj_as_String(simpleLeagueInfo, cardId, "id"));
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.stay);
                }
            });
            m_list.setItemsCanFocus(true);
            m_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }

        public Date dateConverter(String date) {
            char[] ca = date.toCharArray();
            StringBuilder sb = new StringBuilder();
            sb.append(ca);
            sb.deleteCharAt(22);
            date = sb.toString();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
            Date convertedDate = null;
            try {
                convertedDate = df.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return convertedDate;
        }

//        public String[] timeperiod = {"년 전", "달 전", "일 전", "시간 전", "분 전", "초 전", "조금 전"};
//        public String[] timeperiod2 = {"Y - ", "M - ", "D - ", "H - ", "m - ", "s - ", "신청 불가"};
//
//        public String[] timeCalc(long time, String[] timeperiod){
//            String[] result;
//
//            time = time/1000;
//            long year = 31536000;
//            long month = 2592000;
//            long day = 86400;
//            long hour = 3600;
//            long min = 60;
//            long sec = 1;
//
//            if (time >= year) {
//                return new String[]{String.valueOf(time / year), timeperiod[0]};
//            }
//            else if (time >= month) {
//                return new String[]{String.valueOf(time / month), timeperiod[1]};
//            }
//            else if (time >= day) {
//                return new String[]{String.valueOf(time / day), timeperiod[2]};
//            }
//            else if (time >= hour) {
//                return new String[]{String.valueOf(time / hour), timeperiod[3]};
//            }
//            else if (time >= min) {
//                return new String[]{String.valueOf(time / min), timeperiod[4]};
//            }
//            else if (time >= sec) {
//                return new String[]{String.valueOf(time / sec), timeperiod[5]};
//            }
//            else if (time < sec) {
//                return new String[]{timeperiod[6], timeperiod[6]};
//            }
//        }


        private String[] dayCalc(String date) {
            long result;
            Date currentTime = new Date();
            String[] calcDay = new String[2];
            Date convertedDate = dateConverter(date);

            long dday = convertedDate.getTime() - currentTime.getTime();
            long aday = 86400000;
            long hour = 3600000;
            long dhour = (dday/hour);
            dday = (dday / aday);

            if (dday > 0) {
                result = dday;
                calcDay[0] = "OK";
                calcDay[1] = "D - " + String.valueOf(result);
            }
            else if (dday == 0) {
                result = dhour;
                if (result > 0) {
                    calcDay[0] = "OK";
                    calcDay[1] = "H - " + String.valueOf(result);
                }
                else {
                    calcDay[0] = "NO";
                    calcDay[1] = "신청\n불가";
                }
            }
            else {
                calcDay[0] = "NO";
                calcDay[1] = "신청\n불가";
            }
            return calcDay;
        }

    }
}
