package com.redbomba.arena.carddetail;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import com.androidquery.AQuery;
import com.redbomba.arena.R;
import com.redbomba.arena.urls.Urls;

import static com.redbomba.arena.db.DB.getJson;
import static com.redbomba.arena.db.DB.getJsonObj_as_String;

/**
 * Created by Sangmok on 2014. 10. 20..
 */
public class ApplyLeagueActivity extends Activity {

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apply_league_step1);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        sharedPreferences = getSharedPreferences("system", 0);
        new Condition().execute();
    }

    private class Condition extends AsyncTask<Void, String, String> {

        @Override
        protected String doInBackground(Void... params) {
            Bundle bundle = getIntent().getExtras();
            String lid = bundle.getString("lid");
            String conditions = getJson(new Urls().LEAGUE_CONDITION(String.valueOf(sharedPreferences.getInt("uid", 0)), lid));
            return conditions;
        }

        @Override
        protected void onPostExecute(String result) {
            final AQuery aq = new AQuery(ApplyLeagueActivity.this);
            String no = getJsonObj_as_String(result, 0, "no"); // 스테이트값이 리턴 됩니다.
            int checker = 0;

            if (no.equals("-2") || no.equals("-1") || no.equals("0")) {
                String group = getJsonObj_as_String(result, 0, "group"); // 그룹 이름이 리턴 됩니다.
                String isLeader = getJsonObj_as_String(result, 0, "isLeader"); // 리더인지 아닌지 0 또는 1 이 리턴 됩니다.
                String groupmem = getJsonObj_as_String(result, 0, "groupmem"); // 그룹 인원 숫자가 리턴 됩니다.
                String gamelink = getJsonObj_as_String(result, 0, "gamelink"); // 그룹원들의 게임 링크 숫자가 리턴 됩니다.
//            System.out.println(gamelink + " ===== " + groupmem );
                if (!group.equals("0")) {
                    aq.id(R.id.apply_check_group).background(R.drawable.ic_check);
                    checker++;
                }
                if (isLeader.equals("1")) {
                    aq.id(R.id.apply_check_leader).background(R.drawable.ic_check);
                    checker++;
                }
                if (Integer.parseInt(groupmem) > 4 ) {
                    aq.id(R.id.apply_check_groupmem).background(R.drawable.ic_check);
                    checker++;
                }
                if (Integer.parseInt(gamelink) > 4) {
                    aq.id(R.id.apply_check_link).background(R.drawable.ic_check);
                    checker++;
                }
            }

            Button goback = (Button) findViewById(R.id.step1_back);
            goback.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch(event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            aq.id(R.id.step1_back).background(R.drawable.ic_arrow_left_red);
                            return true; // if you want to handle the touch event
                        case MotionEvent.ACTION_UP:
                            aq.id(R.id.step1_back).background(R.drawable.ic_arrow_left);
                            onBackPressed();
                            return true; // if you want to handle the touch event
                    }
                    return false;
                }
            });

            if (checker == 4) {
                aq.id(R.id.apply_next_step).background(R.drawable.btn_green);
                Button nextStep = (Button) findViewById(R.id.apply_next_step);
                nextStep.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = getIntent().getExtras();
                        String lid = bundle.getString("lid");
                        String start = bundle.getString("start");
                        String end = bundle.getString("end");
                        Intent intent = new Intent(ApplyLeagueActivity.this, ApplyTimeActivity.class);
                        intent.putExtra("lid", lid);
                        intent.putExtra("start", start);
                        intent.putExtra("end", end);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivityForResult(intent, 1);
                        ApplyLeagueActivity.this.overridePendingTransition(R.anim.stay, R.anim.stay);
                    }
                });
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                this.finish();
            }
        }
    }//onActivityResult

    @Override
    protected void onResume() {
        super.onResume();
        this.onCreate(null);
    }
}
