package com.redbomba.arena.carddetail;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.redbomba.arena.R;
import com.redbomba.arena.Settings;
import com.redbomba.arena.adapters.BaseInflaterAdapter;
import com.redbomba.arena.adapters.RewardItemData;
import com.redbomba.arena.adapters.inflaters.RewardInflater;
import com.redbomba.arena.carddetail.feeds.FeedActivity;
import com.redbomba.arena.db.DB;
import com.redbomba.arena.urls.Urls;
import com.roomorama.caldroid.CaldroidFragment;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.redbomba.arena.db.DB.*;

/**
 * Created by Sangmok on 2014. 8. 25..
 */
public class CardDetailActivity extends FragmentActivity {

    SharedPreferences sharedPreferences;
    Settings settings;
    private String dlinfo = null;
    private CaldroidFragment caldroidFragment;

    private void setCustomResourceForDates2(Date start, Date end, int bgcolor) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(start.getTime());

        long diff = end.getTime() - start.getTime();
        diff = diff/86400000;

        for (int i=0; i<=(int) diff; i++)
        if (caldroidFragment != null) {
            Calendar c = Calendar.getInstance();
            System.out.println("-=-="+cal.DATE);
            System.out.println("-=-="+cal.DAY_OF_MONTH);

            cal.setTimeInMillis(start.getTime() + 86400000 * i);

            Date date = cal.getTime();
            caldroidFragment.setBackgroundResourceForDate(bgcolor,
                    date);
            caldroidFragment.setTextColorForDate(R.color.white, date);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = (Settings) getApplicationContext();
        setContentView(R.layout.card_detail);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        sharedPreferences = getSharedPreferences("system", 0);
        final AQuery aq = new AQuery(this);
        Bundle bundle = getIntent().getExtras();
        final String lid = bundle.getString("leagueId");
        ImageView host_icon = (ImageView) findViewById(R.id.host_icon);
        LinearLayout testView = (LinearLayout) findViewById(R.id.testView);
        ListView rewardList = (ListView) testView.findViewById(R.id.reward_list);

        final String detailLeagueInfo = DB.getJson(Urls.DETAILED_LEAGUE_INFO(String.valueOf(sharedPreferences.getInt("uid", 0)) , bundle.getString("leagueId")));
        dlinfo = detailLeagueInfo;

        caldroidFragment = new CaldroidFragment();

        if (savedInstanceState != null) {
            caldroidFragment.restoreStatesFromKey(savedInstanceState,
                    "CALDROID_SAVED_STATE");
        }
        else {
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);

            caldroidFragment.setArguments(args);
        }

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.cal, caldroidFragment);
        t.commit();

        aq.id(R.id.detail_poster).image(Urls.MIDEA + getJsonObj_as_String(detailLeagueInfo, 0, "poster"));
        aq.id(R.id.detail_league_name).text(getJsonObj_as_String(detailLeagueInfo, 0, "name"));
        aq.id(R.id.description).text(getJsonObj_as_String(detailLeagueInfo, 0, "concept"));
        aq.id(R.id.detail_joined_user).text(getJsonObj_as_String(detailLeagueInfo, 0, "now_team") + " 팀 참가중");
        aq.id(R.id.detail_joined_total).text("정원: " + getJsonObj_as_String(detailLeagueInfo, 0, "max_team") + " 팀");
        ImageOptions options = new ImageOptions();
        options.round = 1000;
        aq.id(R.id.host_icon).image(Urls.MIDEA
                + getJsonObj_as_String(detailLeagueInfo, 0, "hosticon"), options);
        aq.id(R.id.host_name).text("주최자\n" + getJsonObj_as_String(detailLeagueInfo, 0, "hostname"));

        String rounds = DB.getJson("http://redbomba.net/mobile/?mode=getLeagueRound&id="+lid);
        int rlen = getJsonLength(rounds);

        LinearLayout schedule_container = (LinearLayout) findViewById(R.id.schedule);

        setCustomResourceForDates2(convertDate2(getJsonObj_as_String(detailLeagueInfo, 0, "start_apply")),
                convertDate2(getJsonObj_as_String(detailLeagueInfo, 0, "end_apply")),
                R.color.green);
        schedule_container.addView(getList("신청 기간", convertDate(getJsonObj_as_String(detailLeagueInfo, 0, "start_apply"))
                +" ~ "+convertDate(getJsonObj_as_String(detailLeagueInfo, 0, "end_apply")), R.drawable.bg_radius_rect_green));
        for (int i=0; i<rlen; i++) {
            schedule_container.addView(getList(getJsonObj_as_String(rounds, i, "round")+" 라운드", convertDate(getJsonObj_as_String(rounds, i, "start")) +
                    " ~ " + convertDate(getJsonObj_as_String(rounds, i, "end")), R.drawable.bg_radius_rect_carrot));
            setCustomResourceForDates2(convertDate2(getJsonObj_as_String(rounds, i, "start")),
                    convertDate2(getJsonObj_as_String(rounds, i, "end")),
                    R.color.carrot);

        }

        host_icon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        LinearLayout goToQnA = (LinearLayout) findViewById(R.id.goToQnA);
        LinearLayout goToRule = (LinearLayout) findViewById(R.id.goToRule);
        LinearLayout goToTeam = (LinearLayout) findViewById(R.id.goToTeam);

        goToQnA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(CardDetailActivity.this, FeedActivity.class);
                intent.putExtra("lid", lid);
                startActivity(intent);
            }
        });
        goToRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(CardDetailActivity.this, RuleFAQActivity.class);
                intent.putExtra("leagueInfo", detailLeagueInfo);
                intent.putExtra("rule", getJsonObj_as_String(detailLeagueInfo, 0, "rule"));
                intent.putExtra("poster", getJsonObj_as_String(detailLeagueInfo, 0, "poster"));
                intent.putExtra("league_name", getJsonObj_as_String(detailLeagueInfo, 0, "name"));
                intent.putExtra("pageNum", 0);
                startActivity(intent);
            }
        });
        goToTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(CardDetailActivity.this, JoinedTeamActivity.class);
                intent.putExtra("leagueInfo", detailLeagueInfo);
                startActivity(intent);
            }
        });


        String rewards = DB.getJson(Urls.REWARDS + lid);
        int len = DB.getJsonLength(rewards);

        BaseInflaterAdapter<RewardItemData> adapter = new BaseInflaterAdapter<RewardItemData>(new RewardInflater());
        for (int i = 0; i < len; i++) {
            if (i < 1) {

            }
            //add items on a card
            RewardItemData data = new RewardItemData(
                    getJsonObj_as_String(rewards, i, "name") + " 등",
                    getJsonObj_as_String(rewards, i, "con")
            );
            adapter.addItem(data, false);
        }
        rewardList.setAdapter(adapter);
        updateListViewHeight(rewardList);

        ImageButton goback = (ImageButton) findViewById(R.id.goToMain);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CardDetailActivity.this, ApplyTimeActivity.class);
                intent.putExtra("lid", lid);
                intent.putExtra("start", getJsonObj_as_String(detailLeagueInfo, 0, "frstart"));
                intent.putExtra("end", getJsonObj_as_String(detailLeagueInfo, 0, "frend"));
                startActivity(intent);
//                onBackPressed();
            }
        });
        new Condition().execute();
    }

    private View getList(String round, String date, int bg) {
        View v = new View(this);

        // Layout Inflater 객체 참조
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // button.xml에 정의된 레이아웃에 대한 인플레이션 수행
        v = (View) inflater.inflate(R.layout.schedule_view, null);

        TextView roundview = (TextView) v.findViewById(R.id.schedule_round);
        TextView scheduleview = (TextView) v.findViewById(R.id.schedule_date);

        roundview.setBackgroundResource(bg);
        roundview.setText(round);
        scheduleview.setText(date);
        return v;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.stay, R.anim.slide_in_down);
    }

    @Override
    public void onResume() {
        super.onResume();
        new Condition().execute();
    }

    public static String convertDate(String date) {
        char[] ca = date.toCharArray();
        StringBuilder sb = new StringBuilder();
        sb.append(ca);
        sb.deleteCharAt(22);
        date = sb.toString();

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
        Date convertedDate = null;
        DateFormat format = DateFormat.getDateInstance(DateFormat.LONG);
        try {
            convertedDate = df.parse(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return format.format(convertedDate);
    }

    public static Date convertDate2(String date) {
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

    public void updateListViewHeight(ListView myListView) {
        ListAdapter myListAdapter = myListView.getAdapter();
        if (myListAdapter == null) {
            return;
        }
        //get listview height
        int totalHeight = 0;
        int adapterCount = myListAdapter.getCount();
        for (int size = 0; size < adapterCount ; size++) {
            View listItem = myListAdapter.getView(size, null, myListView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        //Change Height of ListView
        ViewGroup.LayoutParams params = myListView.getLayoutParams();
        params.height = totalHeight + (myListView.getDividerHeight() * (adapterCount - 1));
        myListView.setLayoutParams(params);
    }

    private class Condition extends AsyncTask<Void, String, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            Bundle bundle = getIntent().getExtras();
            String lid = bundle.getString("leagueId");
            sharedPreferences = getSharedPreferences("system", 0);

            String conditions = getJson(new Urls().LEAGUE_CONDITION(String.valueOf(sharedPreferences.getInt("uid", 0)), lid));
            String dlinf = getJson(Urls.DETAILED_LEAGUE_INFO(String.valueOf(sharedPreferences.getInt("uid", 0)), lid));
            String result[] = {conditions, dlinf};
            return result;
        }

        @Override
        protected void onPostExecute(String[] result) {
            final AQuery aq = new AQuery(CardDetailActivity.this);
            String no = getJsonObj_as_String(result[0], 0, "no");
//            System.out.println("-=-=-="+result[0]);
//            System.out.println("-=-=-="+no);
            Bundle bundle = getIntent().getExtras();
            final String lid = bundle.getString("leagueId");
            final String detailLeagueInfo = result[1];
            if (no.equals("-2") || no.equals("-1") || no.equals("0")) {
                aq.id(R.id.join_league).text("참가하기");
                aq.id(R.id.join_league).background(R.drawable.btn_green);
                Button joinLeague = (Button) findViewById(R.id.join_league);
                joinLeague.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Intent intent = new Intent(CardDetailActivity.this, ApplyLeagueActivity.class);
                        intent.putExtra("lid", lid);
                        intent.putExtra("start", getJsonObj_as_String(detailLeagueInfo, 0, "frstart"));
                        intent.putExtra("end", getJsonObj_as_String(detailLeagueInfo, 0, "frend"));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivityForResult(intent, 1);
                        startActivity(intent);
                    }
                });
            }
            else if (no.equals("1")) {
                aq.id(R.id.join_league).text("참가 취소");
                aq.id(R.id.join_league).background(R.drawable.btn_red);

                final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                setLeagueTeam(String.valueOf(sharedPreferences.getInt("uid", 0)), lid, "delete", "", "1");
                                new Condition().execute();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };
                Button joinLeague = (Button) findViewById(R.id.join_league);
                joinLeague.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(CardDetailActivity.this);
                        TextView msg = new TextView(CardDetailActivity.this);
                        msg.setText("정말 취소 하시겠습니까?");
                        msg.setPadding(0,30,0,0);
                        msg.setGravity(Gravity.CENTER);
                        msg.setTextSize(18);
                        builder.setView(msg).setPositiveButton("예", dialogClickListener)
                                .setNegativeButton("아니오", dialogClickListener).show();
                    }
                });
            }
            else {
                Button joinLeague = (Button) findViewById(R.id.join_league);
                aq.id(R.id.join_league).background(R.drawable.unselected_btn_grey);
                joinLeague.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(CardDetailActivity.this, "대회가 이미 시작 되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            Button like = (Button) findViewById(R.id.detail_like);
            if (getJsonObj_as_String(detailLeagueInfo, 0, "is_wish").equals("false")) {
                aq.id(R.id.detail_like).background(R.drawable.ic_action_like);
            }
            else {
                aq.id(R.id.detail_like).background(R.drawable.ic_action_like_selected);
            }
            aq.id(R.id.like_len).text(getJsonObj_as_String(detailLeagueInfo, 0, "wish_len"));
            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (getJsonObj_as_String(detailLeagueInfo, 0, "is_wish").equals("false")) {
//                        postLeagueWish(String.valueOf(sharedPreferences.getInt("uid", 0)), lid);
//                        aq.id(R.id.detail_like).background(R.drawable.ic_action_like_selected);
//                        onResume();
//                    }
//                    else {
                        postLeagueWish(String.valueOf(sharedPreferences.getInt("uid", 0)), lid);
//                        aq.id(R.id.detail_like).background(R.drawable.ic_action_like);
                        onResume();
//                    }
                }
            });
        }
    }

    public String setLeagueTeam(String uid, String lid, String action, String ftime, String round) {
        ArrayList<NameValuePair> post = new ArrayList<NameValuePair>();
        post.add(new BasicNameValuePair("id", uid));
        post.add(new BasicNameValuePair("league_id", lid));
        post.add(new BasicNameValuePair("action", action));
        post.add(new BasicNameValuePair("feasible_time", ftime));
        post.add(new BasicNameValuePair("round", round));
//        post.add(new BasicNameValuePair("is_complete", comp));

        // 연결 HttpClient 객체 생성
        HttpClient client = new DefaultHttpClient();

        // 객체 연결 설정 부분, 연결 최대시간 등등
        HttpParams params = client.getParams();
        HttpConnectionParams.setConnectionTimeout(params, 5000);
        HttpConnectionParams.setSoTimeout(params, 5000);

        // Post객체 생성
        HttpPost httpPost = new HttpPost("http://redbomba.net/mobile/?mode=postLeagueTeam");
        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(post, "UTF-8");
            httpPost.setEntity(entity);
            client.execute(httpPost);
            return EntityUtils.getContentCharSet(entity);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String postLeagueWish(String uid, String lid) {
        ArrayList<NameValuePair> post = new ArrayList<NameValuePair>();
        post.add(new BasicNameValuePair("uid", uid));
        post.add(new BasicNameValuePair("lid", lid));

        // 연결 HttpClient 객체 생성
        HttpClient client = new DefaultHttpClient();

        // 객체 연결 설정 부분, 연결 최대시간 등등
        HttpParams params = client.getParams();
        HttpConnectionParams.setConnectionTimeout(params, 5000);
        HttpConnectionParams.setSoTimeout(params, 5000);

        // Post객체 생성
        HttpPost httpPost = new HttpPost("http://redbomba.net/mobile/?mode=postLeagueWish");
        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(post, "UTF-8");
            httpPost.setEntity(entity);
            client.execute(httpPost);
            return EntityUtils.getContentCharSet(entity);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
