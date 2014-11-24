package com.redbomba.arena.carddetail.feeds;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import com.redbomba.arena.R;
import com.redbomba.arena.adapters.BaseInflaterAdapter;
import com.redbomba.arena.adapters.FeedItemData;
import com.redbomba.arena.adapters.inflaters.FeedInflater;
import com.redbomba.arena.urls.Urls;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.redbomba.arena.db.DB.*;

/**
 * Created by Sangmok on 2014. 9. 23..
 */
public class FeedActivity extends Activity {

    private String feeds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.feeds);
        Button writeFeed = (Button) findViewById(R.id.write_feed);

        ImageButton goback = (ImageButton) findViewById(R.id.goToCard);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Bundle bundle = getIntent().getExtras();
        final String lid = bundle.getString("lid");
//        if (sup.isOverlayed()) {
//            sup.setSlidingEnabled(false);
//        };
        writeFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(FeedActivity.this, WriteFeedActivity.class);
                intent.putExtra("lid", lid);
                startActivity(intent);
            }
        });

        new Feeds().execute();


    }

    private class Feeds extends AsyncTask<Void, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            leagueTeam = DB.getJson("http://redbomba.net/mobile/?mode=getArenaTicket&id="+uid);
        }

        @Override
        protected String doInBackground(Void... params) {
//            int uid = MainActivity.sharedpreferences.getInt("uid", 0);
            Bundle bundle = getIntent().getExtras();
            String lid = bundle.getString("lid");
            SharedPreferences sharedPreferences = getSharedPreferences("system", 0);
            feeds = getJson("http://redbomba.net/mobile/?mode=getLeagueFeed&lid="+lid + "&uid=" + String.valueOf(sharedPreferences.getInt("uid", 0)));
            return feeds;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            BaseInflaterAdapter<FeedItemData> adapter = new BaseInflaterAdapter<FeedItemData>(new FeedInflater());
            int lenOfJa = getJsonLength(result);
            for (int i = 0; i < lenOfJa; i++) {
                //add items on a card
                FeedItemData data = new FeedItemData(
                        Urls.MIDEA + getJsonObj_as_String(feeds, i, "usericon"),
                        getJsonObj_as_String(feeds, i, "username"),
                        " @ " + getJsonObj_as_String(feeds, i, "groupname"),
                        getJsonObj_as_String(feeds, i, "con"),
                        timeCalc(getJsonObj_as_String(feeds, i, "update")),
                        getJsonObj_as_String(feeds, i, "replynum") + "개의 댓글",
                        "좋아요"+getJsonObj_as_String(feeds, i, "smile_len") + "개"
                );
                adapter.addItem(data, false);
            }
            ListView m_list = (ListView) findViewById(R.id.feed_list);
            m_list.setAdapter(adapter);
            m_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(FeedActivity.this, FeedCommentsActivity.class);
                    intent.putExtra("fid", getJsonObj_as_String(feeds, (int) id, "id"));
                    intent.putExtra("feed", getJsonObj_fromArray(feeds, (int) id));
                    startActivity(intent);
                }
            });
//            m_list.setItemsCanFocus(true);
//            m_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
    }
    @Override
    protected void onResume() {

        super.onResume();
        this.onCreate(null);
    }

    public static String timeCalc(String time) {
        Date currentTime = new Date();

        //change format
        char[] ca = time.toCharArray();
        StringBuilder sb = new StringBuilder();
        sb.append(ca);
        sb.deleteCharAt(22);
        time = sb.toString();

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
        Date convertedDate = null;
        try {
            convertedDate = df.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diff = currentTime.getTime() - convertedDate.getTime();
        diff = diff/1000;
        long year = 31536000;
        long month = 2592000;
        long day = 86400;
        long hour = 3600;
        long min = 60;
        long sec = 1;

        if (diff >= year) {
            return String.valueOf(diff/year) + "년 전";
        }
        else if (diff >= month) {
            return String.valueOf(diff/month) + "달 전";
        }
        else if (diff >= day) {
            return String.valueOf(diff/day) + "일 전";
        }
        else if (diff >= hour) {
            return String.valueOf(diff/hour) + "시간 전";
        }
        else if (diff >= min) {
            return String.valueOf(diff/min) + "분 전";
        }
        else if (diff >= sec) {
            return String.valueOf(diff/sec) + "초 전";
        }
        else if (diff < sec) {
            return "조금 전";
        }
        else {
            return "한 달 이상";
        }
    }
}
